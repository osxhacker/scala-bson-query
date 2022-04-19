package com.github.osxhacker.query.reactive

import com.github.osxhacker.query.criteria.expression.IgnoreCase
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import reactivemongo.api.bson._


/**
 * The '''TypedCriteriaSpec''' type unit tests the
 * [[com.github.osxhacker.query.reactive.typed]] EDSL functionality and serves
 * both to verify fitness as well as an exemplar to how the
 * [[com.github.osxhacker.query.reactive.typed]] functionality can be used.
 */
class TypedCriteriaSpec
	extends AnyFlatSpec
	with Matchers
{
	/// Class Types
	case class Grandchild (saying : String)

	case class Nested (
		description : String,
		score : Double,
		grandchild : Grandchild
		)

	case class ExampleDocument (age : Int, name : String, nested : Nested)


	/// Class Imports
	import typed.criteria._


	"A Typed criteria" should "support equality comparisons" in
	{
		/// Since a typed.criteria is being used, the compiler will enforce
		/// the leaf property types given to the criteria method.
		val q1 = criteria[ExampleDocument] (_.name) =/= "a value"
		val q2 = criteria[ExampleDocument] (_.age) @== 99

		BSONDocument.pretty (q1) shouldBe
			BSONDocument.pretty (
				BSONDocument (
					"name" -> BSONDocument ("$ne" -> BSONString ("a value"))
					 )
				)

		BSONDocument.pretty (q2) shouldBe
			BSONDocument.pretty (
				BSONDocument ("age" -> BSONInteger (99))
				)
	}

	it should "support nested object selectors" in
	{
		/// Since a typed.criteria is being used, the compiler will enforce
		/// both the validity of the selector path as well as the ultimate
		/// type referenced (a String in this case).
		val q = criteria[ExampleDocument] (_.nested.grandchild.saying) =/=
			"something"

		BSONDocument.pretty (q) shouldBe
			BSONDocument.pretty (
				BSONDocument (
					"nested.grandchild.saying" ->
					BSONDocument ("$ne" -> "something")
					)
				)
	}

	it should "support ordering comparisons" in
	{
		/// Since a Typed.criteria is being used, the compiler will enforce
		/// the leaf property types given to the criteria method.
		BSONDocument.pretty (
			criteria[ExampleDocument] (_.nested.score) >= 2.3
			) shouldBe
			BSONDocument.pretty (
				BSONDocument (
					"nested.score" ->
					BSONDocument ("$gte" -> BSONDouble (2.3))
					)
				)

		BSONDocument.pretty (
			criteria[ExampleDocument] (_.age) < 99
			) shouldBe
			BSONDocument.pretty (
				BSONDocument ("age" ->
					BSONDocument ("$lt" -> BSONInteger (99))
					)
				)
	}

	it should "support String operations" in
	{
		val q = criteria[ExampleDocument] (_.name) =~ "^test|re"

		BSONDocument.pretty (q) shouldBe
			BSONDocument.pretty (
				BSONDocument (
					"name" -> BSONDocument (
						"$regex" -> BSONRegex ("^test|re", "")
						)
					)
				)
	}

	it should "support String operations with flags" in
	{
		val q = criteria[ExampleDocument] (_.name) =~ "^test|re" -> IgnoreCase

		BSONDocument.pretty (q) shouldBe
			BSONDocument.pretty (
				BSONDocument (
					"name" -> BSONDocument (
						"$regex" -> BSONRegex ("^test|re", "i")
						)
					)
				)
	}

	it should "support multi-value equality" in
	{
		BSONDocument.pretty (
			criteria[ExampleDocument] (_.age).in (21 to 25)
			) shouldBe
			BSONDocument.pretty (
				BSONDocument (
					"age" -> BSONDocument (
						"$in" -> BSONArray (
							BSONInteger (21),
							BSONInteger (22),
							BSONInteger (23),
							BSONInteger (24),
							BSONInteger (25)
							)
						)
					)
				)
	}

	it should "support multi-value inequality" in
	{
		BSONDocument.pretty (
			!criteria[ExampleDocument] (_.nested.description).in (
				"hello",
				"world"
				)
			) shouldBe
			BSONDocument.pretty (
				BSONDocument (
					"nested.description" -> BSONDocument (
						"$nin" -> BSONArray (
							BSONString ("hello"),
							BSONString ("world")
							)
						)
					)
				)
	}

	it should "support conjunctions" in
	{
		val q = criteria[ExampleDocument] (_.age) < 90 &&
			criteria[ExampleDocument] (_.nested.score) >= 20.0

		BSONDocument.pretty (BSONDocument (q)) shouldBe
			BSONDocument.pretty (
				BSONDocument (
					"$and" -> BSONArray (
						BSONDocument (
							"age" -> BSONDocument (
								"$lt" -> BSONInteger (90)
								)
							),
						BSONDocument (
							"nested.score" -> BSONDocument (
								"$gte" -> BSONDouble (20.0)
								)
							)
						)
					)
				)
	}

	it should "support disjunctions" in
	{
		val q = criteria[ExampleDocument] (_.age) < 90 ||
			criteria[ExampleDocument] (_.nested.score) >= 20.0

		BSONDocument.pretty (q) shouldBe
			BSONDocument.pretty (
				BSONDocument (
					"$or" -> BSONArray (
						BSONDocument (
							"age" -> BSONDocument ("$lt" -> BSONInteger (90))
							),
						BSONDocument (
							"nested.score" -> BSONDocument (
								"$gte" -> BSONDouble(20.0)
								)
							)
						)
					)
				)
	}

	it should "combine adjacent conjunctions" in
	{
		val q = criteria[ExampleDocument] (_.age) < 90 &&
			criteria[ExampleDocument] (_.nested.score) >= 0.0 &&
			criteria[ExampleDocument] (_.nested.score) < 20.0

		BSONDocument.pretty (q) shouldBe
			BSONDocument.pretty (
				BSONDocument (
					"$and" -> BSONArray (
						BSONDocument (
							"age" -> BSONDocument ("$lt" -> BSONInteger (90))
							),
						BSONDocument (
							"nested.score" -> BSONDocument (
								"$gte" -> BSONDouble(0.0)
								)
							),
						BSONDocument (
							"nested.score" ->
							BSONDocument ("$lt" -> BSONDouble (20.0))
							)
						)
					)
				)
	}

	it should "combine adjacent disjunctions" in
	{
		val q = criteria[ExampleDocument] (_.age) < 90 ||
			criteria[ExampleDocument] (_.nested.score) >= 0.0 ||
			criteria[ExampleDocument] (_.nested.score) < 20.0

		BSONDocument.pretty (q) shouldBe
			BSONDocument.pretty (
				BSONDocument (
					"$or" -> BSONArray (
						BSONDocument (
							"age" -> BSONDocument ("$lt" -> BSONInteger (90))
							),
						BSONDocument (
							"nested.score" -> BSONDocument (
								"$gte" -> BSONDouble(0.0)
								)
							),
						BSONDocument (
							"nested.score" ->
							BSONDocument ("$lt" -> BSONDouble (20.0))
							)
						)
					)
				)
	}

	it should "support compound filtering" in
	{
		val q = criteria[ExampleDocument] (_.age) < 90 && (
			criteria[ExampleDocument] (_.nested.score) >= 20.0 ||
			criteria[ExampleDocument] (_.nested.score).in (0.0, 1.0)
			)

		BSONDocument.pretty (q) shouldBe
			BSONDocument.pretty (
				BSONDocument (
					"$and" -> BSONArray (
						BSONDocument (
							"age" -> BSONDocument ("$lt" -> BSONInteger (90))
							),
						BSONDocument (
							"$or" -> BSONArray (
								BSONDocument (
									"nested.score" -> BSONDocument (
										"$gte" -> BSONDouble(20.0)
										)
									),
								BSONDocument (
									"nested.score" -> BSONDocument (
										"$in" -> BSONArray (
											BSONDouble (0.0),
											BSONDouble (1.0)
											)
										)
									)
								)
							)
						)
					)
				)
	}

	it should "support negative existence constraints" in
	{
		BSONDocument.pretty (
			!criteria[ExampleDocument] (_.name).exists
			) shouldBe
			BSONDocument.pretty (
				BSONDocument (
					"name" -> BSONDocument ("$exists" -> BSONBoolean (false))
					)
				)
	}
}


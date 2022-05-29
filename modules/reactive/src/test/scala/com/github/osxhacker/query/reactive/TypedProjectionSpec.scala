package com.github.osxhacker.query.reactive

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import reactivemongo.api.bson._


/**
 * The '''TypedProjectionSpec''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.reactive]] types in the context of typed
 * projection for fitness of purpose and serves as an exemplar of their use.
 */
final class TypedProjectionSpec ()
    extends AnyWordSpec
        with Matchers
{
    /// Class Imports
    import typed.projection._


    /// Class Types
    final case class Inner (val d : Double)


    final case class Result (
        val a : String,
        val b : Option[Int],
        val c : List[Inner]
    )


    "The MongoDB typed projection support" must {
        "be able to project into a case class" in {
            val specification : BSONDocument = into[Result]()

            BSONDocument.pretty (specification) shouldBe
                BSONDocument.pretty (
                    BSONDocument (
                        "a" -> 1,
                        "b" -> 1,
                        "c.d" -> 1
                        )
                    )
            }
        }
}

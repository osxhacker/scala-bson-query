package com.github.osxhacker.query.mongodb

import org.bson.json.{
    JsonMode,
    JsonWriterSettings
    }

import org.mongodb.scala.bson.BsonDocument
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import shapeless.{
    syntax => _,
    _
    }


/**
 * The '''UntypedSortingSpec ''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.mongodb]] types in the context of untyped
 * sorting for fitness of purpose and serves as an exemplar of their use.
 */
final class UntypedSortingSpec ()
    extends AnyWordSpec
        with Matchers
{
    /// Class Imports
    import untyped.sorting._


    /// Class Types
    final case class DocumentToSort (
        val firstName : String,
        val lastName : String,
        val age : Int
        )


    /// Instance Properties
    private val settings = JsonWriterSettings.builder ()
        .outputMode (JsonMode.RELAXED)
        .indent (true)
        .build ()


    "The MongoDB typed sorting support" must {
        "be able to produce a valid sort document with an HList" in {
            val specification : BsonDocument = byEach {
                doc =>
                    doc.age.descending ::
                    doc.lastName.asc ::
                    doc.firstName.asc ::
                    HNil
            }

            specification.toJson (settings) shouldBe
                """{
                  |  "age": -1,
                  |  "lastName": 1,
                  |  "firstName": 1
                  |}""".stripMargin
        }

        "be able to produce a valid sort document with one field" in {
            val specification : BsonDocument = by (_.age.descending)

            specification.toJson (settings) shouldBe
                """{
                  |  "age": -1
                  |}""".stripMargin
        }

        "be able to produce a valid sort document with multiple fields" in {
            val specification : BsonDocument = by (
                _.age.descending,
                _.lastName.asc,
                _.firstName.asc
                )

            specification.toJson (settings) shouldBe
                """{
                  |  "age": -1,
                  |  "lastName": 1,
                  |  "firstName": 1
                  |}""".stripMargin
        }
    }
}

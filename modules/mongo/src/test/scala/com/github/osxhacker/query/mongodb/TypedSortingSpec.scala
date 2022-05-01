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
 * The '''TypedSortingSpec ''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.mongodb]] types in the context of typed
 * sorting for fitness of purpose and serves as an exemplar of their use.
 */
final class TypedSortingSpec ()
    extends AnyWordSpec
        with Matchers
{
    /// Class Imports
    import typed.sorting._


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
        "be able to produce a valid sort document with one field" in {
            val specification : BsonDocument = by[DocumentToSort] {
                doc =>
                    doc (_.age).descending ::
                    HNil
                }

            specification.toJson (settings) shouldBe
                """{
                  |  "age": -1
                  |}""".stripMargin
            }

        "be able to produce a valid sort document with multiple fields" in {
            val specification : BsonDocument = by[DocumentToSort] {
                doc =>
                    doc (_.age).descending ::
                    doc (_.lastName).asc ::
                    doc (_.firstName).asc ::
                    HNil
                }

            specification.toJson (settings) shouldBe
                """{
                  |  "age": -1,
                  |  "lastName": 1,
                  |  "firstName": 1
                  |}""".stripMargin
            }
    }
}

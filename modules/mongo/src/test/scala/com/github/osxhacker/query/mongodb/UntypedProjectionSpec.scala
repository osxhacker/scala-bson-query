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
 * The '''UntypedProjectionSpec''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.mongodb]] types in the context of untyped
 * projection for fitness of purpose and serves as an exemplar of their use.
 */
final class UntypedProjectionSpec ()
    extends AnyWordSpec
        with Matchers
{
    /// Class Imports
    import untyped.projection._


    /// Instance Properties
    private val settings = JsonWriterSettings.builder ()
        .outputMode (JsonMode.RELAXED)
        .indent (true)
        .build ()


    "The MongoDB untyped projection support" must {
        "be able to produce a valid projectio document" in {
            val specification : BsonDocument = returning {
                doc =>
                    doc.property ::
                    doc.another ::
                    HNil
                }

            specification.toJson (settings) shouldBe
                """{
                  |  "property": 1,
                  |  "another": 1
                  |}""".stripMargin
            }
        }
}

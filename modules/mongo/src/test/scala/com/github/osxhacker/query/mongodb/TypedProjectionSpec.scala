package com.github.osxhacker.query.mongodb

import org.bson.json.{
    JsonMode,
    JsonWriterSettings
    }

import org.mongodb.scala.bson.BsonDocument
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers


/**
 * The '''TypedProjectionSpec''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.mongodb]] types in the context of typed
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


    /// Instance Properties
    private val settings = JsonWriterSettings.builder ()
        .outputMode (JsonMode.RELAXED)
        .indent (true)
        .build ()


    "The MongoDB typed projection support" must {
        "be able to project into a case class" in {
            val specification : BsonDocument = into[Result] ()

            specification.toJson (settings) shouldBe
                """{
                  |  "a": 1,
                  |  "b": 1,
                  |  "c.d": 1
                  |}""".stripMargin
            }
        }
}

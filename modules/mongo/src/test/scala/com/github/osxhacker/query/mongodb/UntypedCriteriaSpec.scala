package com.github.osxhacker.query.mongodb

import org.bson.json.JsonWriterSettings
import org.mongodb.scala.bson.BsonDocument
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers


/**
 * The '''UntypedCriteriaSpec''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.mongodb]] types in the context of typed
 * criteria for fitness of purpose * and serves as an exemplar of their use.
 */
final class UntypedCriteriaSpec ()
    extends AnyWordSpec
        with Matchers
{
    /// Class Imports
    import untyped.criteria._


    /// Instance Properties
    private val settings = JsonWriterSettings.builder ()
        .indent (true)
        .build ()


    "The MongoDB untyped criteria support" must {
        "support complex criteria" in {
            val expression : BsonDocument = where {
                doc =>
                    doc.a > 10 && doc.b.in ("some", "text") ||
                    doc.c.exists
            }

            expression.toJson(settings) shouldBe
            """{
              |  "$or": [
              |    {
              |      "$and": [
              |        {
              |          "a": {
              |            "$gt": 10
              |          }
              |        },
              |        {
              |          "b": {
              |            "$in": [
              |              "some",
              |              "text"
              |            ]
              |          }
              |        }
              |      ]
              |    },
              |    {
              |      "c": {
              |        "$exists": true
              |      }
              |    }
              |  ]
              |}""".stripMargin
            }
        }
}

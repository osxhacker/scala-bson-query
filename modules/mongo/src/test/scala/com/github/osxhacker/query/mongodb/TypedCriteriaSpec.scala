package com.github.osxhacker.query.mongodb

import com.github.osxhacker.query.criteria.expression.{
    Expression,
    IgnoreCase
    }

import org.bson.json.JsonWriterSettings
import org.mongodb.scala.bson.BsonDocument
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers


/**
 * The '''TypedCriteriaSpec''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.mongodb]] types in the context of typed
 * criteria for fitness of purpose * and serves as an exemplar of their use.
 */
final class TypedCriteriaSpec ()
    extends AnyWordSpec
        with Matchers
{
    /// Class Imports
    import typed.criteria._


    /// Class Types
    final case class MyDocument (
        count : Int,
        text : String,
        messages : Vector[String]
    )


    /// Instance Properties
    private val settings = JsonWriterSettings.builder ()
        .indent (true)
        .build ()


    "The MongoDB type criteria support" must {
        "support complex logical expressions" in {
            val expression : BsonDocument = where[MyDocument] {
                doc =>
                    (doc (_.count) === 1 && doc (_.text) === "foo") ||
                    doc (_.messages).all ("a", "b") ||
                    doc (_.text) =~ """^\w{3}\s*\d$""".r -> IgnoreCase
                }

            expression.toJson (settings) shouldBe
                """{
                  |  "$or": [
                  |    {
                  |      "$and": [
                  |        {
                  |          "count": {
                  |            "$eq": 1
                  |          }
                  |        },
                  |        {
                  |          "text": {
                  |            "$eq": "foo"
                  |          }
                  |        }
                  |      ]
                  |    },
                  |    {
                  |      "messages": {
                  |        "$all": [
                  |          "a",
                  |          "b"
                  |        ]
                  |      }
                  |    },
                  |    {
                  |      "text": {
                  |        "$regularExpression": {
                  |          "pattern": "^\\w{3}\\s*\\d$",
                  |          "options": "i"
                  |        }
                  |      }
                  |    }
                  |  ]
                  |}""".stripMargin
            }

        "support optional expressions" in {
            val partial : BsonDocument = where[MyDocument] {
                doc =>
                    None && doc (_.count).exists
                }

            val noCriteria : BsonDocument = useMessage (false)

            partial.toJson (settings) shouldBe
                """{
                  |  "count": {
                  |    "$exists": true
                  |  }
                  |}""".stripMargin

            noCriteria.toJson (settings) shouldBe "{}"
            }
        }


    private def useMessage (yes : Boolean) : Option[Expression[BsonDocument]] =
        Option {
            where[MyDocument] {
                doc =>
                    doc (_.messages).exists
            }
        }
            .filter(_ => yes)
}

package com.github.osxhacker.query.criteria.expression

import cats.data.Chain
import com.github.osxhacker.query.criteria._


/**
 * The '''ComparisonSpec''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.criteria.expression.Comparison]] types for
 * fitness of purpose and serve as an exemplar of their use.
 */
final class ComparisonSpec ()
    extends ProjectSpec
{
    /// Class Imports
    import syntax._


    "The equality comparison expression" must {
        "support untyped equality" in {
            val field = Field[Any] ("foo")
            val value = Value (1)
            val expression = field @== value

            expression shouldBe EqualTo (field, value)
        }

        "support untyped inequality" in {
            val field = Field[Any] ("foo")
            val value = Value (1)
            val expression = field =/= value

            expression shouldBe NotEqualTo (field, value)
        }

        "support untyped existence" in {
            val field = Field[Any] ("foo")
            val expression = field.exists

            expression shouldBe Exists (field, true)
        }

        "support untyped 'in'" in {
            val field = Field[Any] ("foo")
            val expression = field.in ("a", 2, "bee")

            expression shouldBe In (field, Chain ("a", 2, "bee"))
        }

        "support typed equality" in {
            val field = Field[Int] ("foo")
            val value = Value (1)
            val expression = field.bar @== value

            expression shouldBe EqualTo (field.bar, value)
            expression.field._property$path shouldBe "foo.bar"

            assertDoesNotCompile(
                """
                  val intField = Field[Int] ("foo")
                  val stringVal = Value ("content")
                  val invalid = field @== stringVal
                """
                )
        }

        "support typed inequality" in {
            val field = Field[Int] ("foo")
            val value = Value (1)
            val expression = field.bar <> value

            expression shouldBe NotEqualTo (field.bar, value)
            expression.field._property$path shouldBe "foo.bar"

            assertDoesNotCompile(
                """
                  val intField = Field[Int] ("foo")
                  val stringVal = Value ("content")
                  val invalid = field =/= stringVal
                """
                )
        }

        "support typed existence" in {
            val field = Field[Float] ("foo")
            val expression = field.exists

            expression shouldBe Exists (field, true)
        }

        "support typed 'in'" in {
            val field = Field[Int] ("foo")
            val expression = field.in (1, 2)

            expression shouldBe In (field, Chain (1, 2))
            expression.negate () shouldBe NotIn (field, Chain (1, 2))
        }
    }
}

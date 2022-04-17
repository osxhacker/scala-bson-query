package com.github.osxhacker.query.criteria.expression

import com.github.osxhacker.query.criteria._


/**
 * The '''EvaluationSpec''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.criteria.expression.Evaluation]] types for
 * fitness of purpose and serves as an exemplar of their use.
 */
final class EvaluationSpec ()
    extends ProjectSpec
{
    /// Class Imports
    import syntax._


    "The evaluation operators" must {
        "support string regular expressions for string fields (typed)" in {
            val field = Field[String] ("x")
            val expression = field =~ """^\d+$"""

            expression shouldBe RegularExpression (field, Value ("""^\d+$"""))
        }

        "support compiled regular expressions for string fields (typed)" in {
            val field = Field[String] ("x")
            val expression = field =~ """^\d+$""".r

            expression shouldBe RegularExpression (field, Value ("""^\d+$"""))
        }

        "support string regular expressions for string fields (untyped)" in {
            val field = Field[Any] ("x")
            val expression = field =~ """^\d+$"""

            expression shouldBe RegularExpression (field, Value ("""^\d+$"""))
        }

        "support compiled regular expressions for string fields (untyped)" in {
            val field = Field[Any] ("x")
            val expression = field =~ """^\d+$""".r

            expression shouldBe RegularExpression (field, Value ("""^\d+$"""))
        }
    }
}

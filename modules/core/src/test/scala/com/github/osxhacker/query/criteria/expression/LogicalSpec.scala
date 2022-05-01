package com.github.osxhacker.query.criteria.expression

import cats.data.{
    Chain,
    NonEmptyChain
    }

import com.github.osxhacker.query.ProjectSpec
import com.github.osxhacker.query.criteria._


/**
 * The '''LogicalSpec''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.criteria.expression.Logical]] types for
 * fitness of purpose and serve as an exemplar of their use.
 */
final class LogicalSpec ()
    extends ProjectSpec
{
    /// Class Imports
    import syntax._


    /// Instance Properties
    val aProperty = Field[Int] ("a")
    val bProperty = Field[Int] ("b")


    "The logical operators" must {
        "support conjunction" in {
            val expression = (aProperty @== 1) && (bProperty @== 2)
            val additional = (aProperty @== 1) && (bProperty @== 2) && (aProperty > 0)

            expression shouldBe And (
                NonEmptyChain (
                    EqualTo (aProperty, 1),
                    EqualTo (bProperty, 2)
                )
            )

            additional shouldBe And (
                NonEmptyChain (
                    EqualTo (aProperty, 1),
                    EqualTo (bProperty, 2),
                    GreaterThan (aProperty, 0)
                )
            )
        }

        "support disjunction" in {
            val expression = (aProperty @== 1) || (bProperty @== 2)
            val additional = expression || (aProperty > 0)

            expression shouldBe Or (
                NonEmptyChain (
                    EqualTo (aProperty, 1),
                    EqualTo (bProperty, 2)
                    )
                )

            additional shouldBe Or (
                NonEmptyChain (
                    EqualTo (aProperty, 1),
                    EqualTo (bProperty, 2),
                    GreaterThan (aProperty, 0)
                )
            )
        }

        "support negation" in {
            val notE = !aProperty.exists
            val notI = !bProperty.in (1, 2, 3, 4)
            val notLT = !(aProperty < 99)
            val notGTE = !(bProperty >= 0)

            notE shouldBe Exists (aProperty, false)
            !notE shouldBe Exists (aProperty, true)
            notI shouldBe NotIn (bProperty, Chain (1, 2, 3, 4))
            !notI shouldBe In (bProperty, Chain (1, 2, 3, 4))
            notLT shouldBe GreaterThanOrEqual (aProperty, 99)
            notGTE shouldBe LessThan (bProperty, 0)
        }
    }
}

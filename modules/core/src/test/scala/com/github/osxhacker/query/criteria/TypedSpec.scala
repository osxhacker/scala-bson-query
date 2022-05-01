package com.github.osxhacker.query.criteria

import cats.data.{
    Chain,
    NonEmptyChain
    }

import com.github.osxhacker.query.ProjectSpec
import com.github.osxhacker.query.criteria.expression._


/**
 * The '''TypedSpec''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.criteria.typed]] types for fitness of purpose
 * and serves as an exemplar of their use.
 */
final class TypedSpec ()
    extends ProjectSpec
{
    /// Class Imports
    import expression.syntax._


    "Typed criteria" must {
        "support a unary where" in {
            val expression = typed.where[SampleDocument] {
                doc =>
                    (
                        doc (_.name) === "Bob" ||
                        doc (_.address.state).in ("CA", "NY")
                    ) && doc (_.age) >= 18
                }

            expression shouldBe And (
                NonEmptyChain (
                    Or (
                        NonEmptyChain (
                            EqualTo (
                                Field[String] ("name"),
                                Value ("Bob")
                                ),

                            In (
                                Field[String] ("address.state"),
                                Chain ("CA", "NY")
                                )
                            )
                        ),

                    GreaterThanOrEqual (
                        Field[Int] ("age"),
                        Value (18)
                        )
                    )
                )
            }

        "support optional expressions (some, rhs)" in {
            val expression = typed.where[SampleDocument] {
                doc =>
                    doc (_.name) <> "Alice" &&
                    Some (doc (_.age) < 21)
                }

            expression shouldBe And (
                NonEmptyChain (
                    NotEqualTo (
                        Field[String] ("name"),
                        Value ("Alice")
                        ),

                    LessThan (
                        Field[Int] ("age"),
                        Value (21)
                        )
                    )
                )
            }

        "support optional expressions (none, rhs)" in {
            val expression = typed.where[SampleDocument] {
                doc =>
                    doc (_.name) <> "Alice" &&
                    None
                }

            expression shouldBe NotEqualTo (
                Field[String] ("name"),
                Value ("Alice")
                )
            }

        "support optional expressions (some, lhs)" in {
            val expression = typed.where[SampleDocument] {
                doc =>
                    Some (doc (_.name) <> "Alice") &&
                    doc (_.age) < 21
                }

            expression shouldBe And (
                NonEmptyChain (
                    NotEqualTo (
                        Field[String] ("name"),
                        Value ("Alice")
                    ),

                    LessThan (
                        Field[Int] ("age"),
                        Value (21)
                        )
                    )
                )
            }

        "support optional expressions (some, lhs + rhs)" in {
            val expression = typed.where[SampleDocument] {
                doc =>
                    Some (doc (_.name) <> "Alice") ||
                    !Some (doc (_.age) < 21)
                }

            expression shouldBe Some (
                Or (
                    NonEmptyChain (
                        NotEqualTo (
                            Field[String] ("name"),
                            Value ("Alice")
                            ),

                        GreaterThanOrEqual (
                            Field[Int] ("age"),
                            Value (21)
                            )
                        )
                    )
                )
            }

        "support optional expressions (none, lhs + rhs)" in {
            val expression = typed.where[SampleDocument] {
                _ =>
                    None || None
                }

            expression shouldBe None
            }

        "support 'between' operator" in {
            val expression = typed.where[SampleDocument] {
                doc =>
                    doc (_.age).between[HalfOpen] (20, 30)
                }

            expression shouldBe Between (
                BetweenPolicy.HalfOpen,
                Field[Int] ("age"),
                Value (20),
                Value (30)
                )
            }

        "support 'exists' suffix operator" in {
            val expression = typed.where[SampleDocument] {
                doc =>
                    doc (_.name).exists
                }

            expression shouldBe Exists(
                Field[String] ("name"),
                true
                )
            }

        "support regular expression operators" in {
            val expression = typed.where[SampleDocument] {
                doc =>
                    doc (_.address.state) =~ """^\w{2}$""".r -> (
                        IgnoreCase | ExtendedExpressions
                        )
                }

            expression shouldBe RegularExpression (
                Field[String] ("address.state"),
                Value ("""^\w{2}$"""),
                Some (IgnoreCase | ExtendedExpressions)
                )
            }

        "require a 'String' or 'Regex' with regular expression operators" in {
            assertDoesNotCompile(
                """
                  | typed.where[SampleDocument] {
                  |     doc =>
                  |         doc (_.address.state) =~ 99
                  |         }
                  |""".stripMargin
                )
            }
    }
}

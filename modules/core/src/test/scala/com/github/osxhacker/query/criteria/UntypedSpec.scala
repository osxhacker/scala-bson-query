package com.github.osxhacker.query.criteria

import cats.data.{
    Chain,
    NonEmptyChain
    }

import com.github.osxhacker.query.criteria.expression._


/**
 * The '''UntypedSpec''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.criteria.untyped]] types for fitness of purpose
 * and serves as an exemplar of their use.
 */
final class UntypedSpec ()
    extends ProjectSpec
{
    /// Class Imports
    import expression.syntax._


    "Untyped criteria" must {
        "support a unary where" in {
            val expression = untyped.where {
                doc =>
                    (
                        doc.name === "Bob" || doc.address.state.in ("CA", "NY")
                    ) && doc.age >= 18
                }

            expression shouldBe And (
                NonEmptyChain (
                    Or (
                        NonEmptyChain (
                            EqualTo (
                                Field ("name"),
                                Value ("Bob")
                                ),

                            In (
                                Field ("address.state"),
                                Chain ("CA", "NY")
                                )
                            )
                        ),

                    GreaterThanOrEqual (
                        Field ("age"),
                        Value (18)
                        )
                    )
                )
            }

        "support a binary where" in {
            val expression = untyped.where {
                _.age > 30 && _.name <> "Alice"
                }

            expression shouldBe And (
                NonEmptyChain (
                    GreaterThan (
                        Field ("age"),
                        Value (30)
                        ),

                    NotEqualTo (
                        Field ("name"),
                        Value ("Alice")
                        )
                    )
                )
            }

        "support optional expressions (some, rhs)" in {
            val expression = untyped.where {
                doc =>
                    doc.name <> "Alice" && Some (doc.age < 21)
                }

            expression shouldBe And (
                NonEmptyChain (
                    NotEqualTo (
                        Field ("name"),
                        Value ("Alice")
                        ),

                    LessThan (
                        Field ("age"),
                        Value (21)
                        )
                    )
                )
            }

        "support optional expressions (none, rhs)" in {
            val expression = untyped.where {
                doc =>
                    doc.name <> "Alice" && None
                }

            expression shouldBe NotEqualTo (
                Field[String] ("name"),
                Value ("Alice")
                )
            }

        "support optional expressions (some, lhs)" in {
            val expression = untyped.where {
                doc =>
                    Some (doc.name <> "Alice") && doc.age < 21
                }

            expression shouldBe And (
                NonEmptyChain (
                    NotEqualTo (
                        Field ("name"),
                        Value ("Alice")
                        ),

                    LessThan (
                        Field ("age"),
                        Value (21)
                        )
                    )
                )
            }

        "support optional expressions (some, lhs + rhs)" in {
            val expression = untyped.where {
                doc =>
                    Some (doc.name <> "Alice") || !Some (doc.age < 21)
                }

            expression shouldBe Some (
                Or (
                    NonEmptyChain (
                        NotEqualTo (
                            Field ("name"),
                            Value ("Alice")
                        ),

                        GreaterThanOrEqual (
                            Field ("age"),
                            Value (21)
                            )
                        )
                    )
                )
            }

        "support optional expressions (none, lhs + rhs)" in {
            val expression = untyped.where {
                (_, _) =>
                    None || None
                }

            expression shouldBe None
            }

        "support 'between' operator" in {
            val expression = untyped.where {
                _.age.between[Closed] (20, 29)
                }

            expression shouldBe Between (
                BetweenPolicy.Closed,
                Field[Any] ("age"),
                Value (20),
                Value (29)
                )
            }

        "support 'exists' suffix operator" in {
            val expression = untyped.where {
                _.name.exists
                }

            expression shouldBe Exists (
                Field[Any] ("name"),
                true
                )
            }

        "support regular expression operators" in {
            val expression = untyped.where {
                _.address.state =~ """^\w{2}$""".r -> IgnoreCase
                }

            expression shouldBe RegularExpression (
                Field[String] ("address.state"),
                Value ("""^\w{2}$"""),
                Some (IgnoreCase)
                )
            }

        "require a 'String' or 'Regex' with regular expression operators" in {
            assertDoesNotCompile(
                """
                  | untyped.where {
                  |     doc =>
                  |         doc (_.address.state) =~ 99
                  |         }
                  |""".stripMargin
                )
            }
        }
}

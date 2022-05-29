package com.github.osxhacker.query.projection

import com.github.osxhacker.query.ProjectSpec
import org.scalatest.diagrams.Diagrams

import shapeless.{
    syntax => _,
    _
    }


/**
 * The '''UntypedFunctionsSpec''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.projection.untyped]] logic for fitness of
 * purpose and serves as an exemplar of their use.
 */
final class UntypedFunctionsSpec ()
    extends ProjectSpec
        with Diagrams
{
    /// Class Imports
    import untyped._


    "Projection functions" must {
        "support specifying what fields to return" in {
            val specification = returning {
                available =>
                    available.a.b.c ::
                    HNil
                }

            specification shouldBe ProjectionSpecification[Any] (
                ProjectField[Any] ("a.b.c") ::
                HNil
                )
            }

        "support specifying multiple fields" in {
            val specification = returning {
                available =>
                    available.first ::
                    available.second.field ::
                    available.another.nested.field ::
                    HNil
                }

            specification shouldBe ProjectionSpecification[Any] (
                ProjectField[Any] ("first") ::
                ProjectField[Any] ("second.field") ::
                ProjectField[Any] ("another.nested.field") ::
                HNil
                )
            }
        }
}

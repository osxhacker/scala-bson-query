package com.github.osxhacker.query.sorting

import com.github.osxhacker.query.ProjectSpec
import org.scalatest.diagrams.Diagrams
import shapeless.{
    syntax => _,
    _
    }


/**
 * The '''UntypedFunctionsSpec''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.sorting.typed]] logic for fitness of purpose
 * and serves as an exemplar of their use.
 */
final class UntypedFunctionsSpec ()
    extends ProjectSpec
        with Diagrams
{
    /// Class Imports
    import expression.syntax._
    import untyped._


    "Sorting functions" must {
        "require at least one field" in {
            assertDoesNotCompile(
                """
                  |untyped.by[SampleDocument] (_ => HNil)
                  |""".stripMargin
            )
        }

        "support being provided one sort field" in {
            val specification = by {
                _.someField.descending
                }

            assert (specification.fields.length.toInt === 1)
            specification shouldBe SortSpecification[SampleDocument] (
                SortField[String] ("someField", false) ::
                HNil
            )
        }

        "support being provided an HList fields to use" in {
            val specification = byEach {
                doc =>
                    doc.name.ascending ::
                    doc.another.ascending ::
                    HNil
                }

            assert (specification.fields.length.toInt === 2)
            specification shouldBe SortSpecification[SampleDocument] (
                SortField[String] ("name", true) ::
                SortField[String] ("another", true) ::
                HNil
                )
            }

        "support being provided multiple fields to use" in {
            val specification = by (
                _.name.asc,
                _.address.street.desc
            )

            assert (specification.fields.length.toInt === 2)
            specification shouldBe SortSpecification[SampleDocument] (
                SortField[String] ("name", true) ::
                    SortField[String] ("address.street", false) ::
                    HNil
                )
            }
    }
}

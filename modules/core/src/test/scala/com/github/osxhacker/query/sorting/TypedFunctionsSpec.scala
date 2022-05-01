package com.github.osxhacker.query.sorting

import com.github.osxhacker.query.ProjectSpec
import org.scalatest.diagrams.Diagrams
import shapeless.{
    syntax => _,
    _
    }


/**
 * The '''TypedFunctionsSpec''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.sorting.typed]] logic for fitness of purpose
 * and serves as an exemplar of their use.
 */
final class TypedFunctionsSpec ()
    extends ProjectSpec
        with Diagrams
{
    /// Class Imports
    import expression.syntax._
    import typed._


    "Sorting functions" must {
        "require at least one field" in {
            assertDoesNotCompile(
                """
                  |typed.by[SampleDocument] (_ => HNil)
                  |""".stripMargin
                )
            }

        "support being provided one field to use" in {
            val specification = by[SampleDocument] {
                doc =>
                    doc (_.name).ascending ::
                    HNil
                }

            assert (specification.fields.length.toInt === 1)
            specification shouldBe SortSpecification[SampleDocument] (
                SortField[String] ("name", true) ::
                HNil
                )
            }

        "support being provided multiple fields to use" in {
            val specification = by[SampleDocument] {
                doc =>
                    doc (_.name).asc ::
                    doc (_.address.street).desc ::
                    HNil
                }

                assert (specification.fields.length.toInt === 2)
                specification shouldBe SortSpecification[SampleDocument] (
                    SortField[String] ("name", true) ::
                    SortField[String] ("address.street", false) ::
                    HNil
                    )
        }
    }
}

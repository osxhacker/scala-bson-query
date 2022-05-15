package com.github.osxhacker.query.projection

import com.github.osxhacker.query.ProjectSpec
import com.github.osxhacker.query.criteria.Field
import org.scalatest.diagrams.Diagrams

import shapeless.{
    syntax => _,
    _
    }


/**
 * The '''TypedFunctionsSpec''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.projection.typed]] logic for fitness of purpose
 * and serves as an exemplar of their use.
 */
final class TypedFunctionsSpec ()
    extends ProjectSpec
        with Diagrams
{
    /// Class Imports
    import typed._


    /// Class Types
    final case class Outer (name : Option[String], inners : List[Inner])


    final case class Inner (a : Int, b : String, c : Option[Double])


    final case class LimitedResults (
        val name : String,
        val age : Int
    )


    "Projection functions" must {
        "be able to perform an identity projection" in {
            val specification = into[SampleDocument] ()

            specification shouldBe ProjectionSpecification[SampleDocument] (
                Field[String] ("name") ::
                Field[Int] ("age") ::
                Field[String] ("address.street") ::
                Field[String] ("address.city") ::
                Field[String] ("address.state") ::
                HNil
                )
            }

        "be able to project into a subset type" in {
            val specification = into[LimitedResults] ()

            specification shouldBe ProjectionSpecification[LimitedResults] (
                Field[String] ("name") ::
                Field[Int] ("age") ::
                HNil
                )
            }

        "require all fields in the desired type exist in the document" in {
            final case class AddedField (val foo : Int)


            assertDoesNotCompile("""
                into[SampleDocument, AddedField] ()
                """)
        }

        "be able to identify collection-like properties" in {
            val specification = into[Outer] ()

            specification shouldBe ProjectionSpecification[Outer] (
                Field[String] ("name") ::
                Field[Int] ("inners.a") ::
                Field[Int] ("inners.b") ::
                Field[Int] ("inners.c") ::
                HNil
            )
        }
    }
}

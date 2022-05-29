package com.github.osxhacker.query.projection

import com.github.osxhacker.query.ProjectSpec
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
                ProjectField[String] ("name") ::
                ProjectField[Int] ("age") ::
                ProjectField[String] ("address.street") ::
                ProjectField[String] ("address.city") ::
                ProjectField[String] ("address.state") ::
                HNil
                )
            }

        "be able to project into a subset type" in {
            val specification = into[LimitedResults] ()

            specification shouldBe ProjectionSpecification[LimitedResults] (
                ProjectField[String] ("name") ::
                ProjectField[Int] ("age") ::
                HNil
                )
            }

        "be able to identify collection-like properties" in {
            val specification = into[Outer] ()

            specification shouldBe ProjectionSpecification[Outer] (
                ProjectField[String] ("name") ::
                ProjectField[Int] ("inners.a") ::
                ProjectField[Int] ("inners.b") ::
                ProjectField[Int] ("inners.c") ::
                HNil
                )
            }

        "require a Product type to project into" in {
            final class NotAProduct(val a : String, b : Int)

            assertDoesNotCompile(
                """
                  |into[NotAProduct] ()
                  |""".stripMargin
                )
            }
    }
}

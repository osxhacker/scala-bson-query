package com.github.osxhacker.query.criteria.expression

import cats.data.Chain
import com.github.osxhacker.query.ProjectSpec
import com.github.osxhacker.query.criteria._


/**
 * The '''CollectionSpec''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.criteria.expression.Collection]] types for
 * fitness of purpose and serves as an exemplar of their use.
 */
final class CollectionSpec ()
    extends ProjectSpec
{
    /// Class Imports
    import syntax._


    "The collection operators" must {
        "support the 'all' operator (typed seq)" in {
            val field = Field[Seq[String]] ("foo")
            val expression = field.all ("a" :: "bee" :: Nil)

            expression shouldBe All (field, Chain ("a", "bee"))
        }

        "support the 'all' operator (typed varargs)" in {
            val field = Field[Seq[String]] ("foo")
            val expression = field.all ("a", "bee")

            expression shouldBe All (
                Field[Seq[Any]] (field._property$path),
                Chain ("a", "bee")
            )
        }

        "support the 'all' operator (untyped seq)" in {
            val field = Field[Seq[Any]] ("foo")
            val expression = field.all ("a" :: "bee" :: Nil)

            expression shouldBe All (
                Field[Seq[Any]] (field._property$path),
                Chain ("a", "bee")
            )
        }

        "support the 'all' operator (untyped varargs)" in {
            val field = Field[Seq[Any]] ("foo")
            val expression = field.all ("a", "bee")

            expression shouldBe All (
                Field[Seq[Any]] (field._property$path),
                Chain ("a", "bee")
            )
        }

        "disallow non-collection types" in {
            assertDoesNotCompile("""Field[Int] ("bad").all (1)""")
        }
    }
}

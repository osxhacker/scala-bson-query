package com.github.osxhacker.query.reactive

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import reactivemongo.api.bson._
import shapeless.{
    syntax => _,
    _
    }


/**
 * The '''TypedSortingSpec ''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.reactive]] types in the context of untyped
 * sorting for fitness of purpose and serves as an exemplar of their use.
 */
final class UntypedSortingSpec ()
    extends AnyWordSpec
        with Matchers
{
    /// Class Imports
    import untyped.all._


    /// Class Types
    final case class DocumentToSort (
        val firstName : String,
        val lastName : String,
        val age : Int
        )


    "The MongoDB typed sorting support" must {
        "be able to produce a valid sort document with an HList" in {
            val specification : BSONDocument = byEach {
                doc =>
                    doc.age.descending ::
                    doc.group.ascending ::
                    HNil
                }

            BSONDocument.pretty (specification) shouldBe
                BSONDocument.pretty (
                    BSONDocument (
                        "age" -> -1,
                        "group" -> 1
                        )
                    )
            }

        "be able to produce a valid sort document with one field" in {
            val specification : BSONDocument = by (
                _.age.descending
                )

            BSONDocument.pretty (specification) shouldBe
                BSONDocument.pretty (
                    BSONDocument (
                        "age" -> -1
                        )
                    )
            }

        "be able to produce a valid sort document with multiple fields" in {
            val specification : BSONDocument = by (
                _.age.descending,
                _.lastName.asc,
                _.firstName.asc
            )

            BSONDocument.pretty (specification) shouldBe
                BSONDocument.pretty (
                    BSONDocument (
                        "age" -> -1,
                        "lastName" -> 1,
                        "firstName" -> 1
                    )
                )
        }
    }
}

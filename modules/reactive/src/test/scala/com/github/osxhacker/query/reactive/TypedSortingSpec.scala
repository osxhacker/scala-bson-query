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
 * [[com.github.osxhacker.query.reactive]] types in the context of typed
 * sorting for fitness of purpose and serves as an exemplar of their use.
 */
final class TypedSortingSpec ()
    extends AnyWordSpec
        with Matchers
{
    /// Class Imports
    import typed.sorting._


    /// Class Types
    final case class DocumentToSort (
        val firstName : String,
        val lastName : String,
        val age : Int
    )


    "The MongoDB typed sorting support" must {
        "be able to produce a valid sort document with one field" in {
            val specification : BSONDocument = by[DocumentToSort] {
                doc =>
                    doc (_.age).descending ::
                    HNil
                }

            BSONDocument.pretty (specification) shouldBe
                BSONDocument.pretty (
                    BSONDocument (
                        "age" -> -1
                        )
                    )
            }

        "be able to produce a valid sort document with multiple fields" in {
            val specification : BSONDocument = by[DocumentToSort] {
                doc =>
                    doc (_.age).descending ::
                        doc (_.lastName).asc ::
                        doc (_.firstName).asc ::
                        HNil
                }

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

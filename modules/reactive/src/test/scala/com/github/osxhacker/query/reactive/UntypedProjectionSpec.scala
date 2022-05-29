package com.github.osxhacker.query.reactive

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import reactivemongo.api.bson._
import shapeless.{
    syntax => _,
    _
    }


/**
 * The '''UntypedProjectionSpec''' type defines the unit-tests which certify the
 * [[com.github.osxhacker.query.reactive]] types in the context of untyped
 * projection for fitness of purpose and serves as an exemplar of their use.
 */
final class UntypedProjectionSpec ()
    extends AnyWordSpec
        with Matchers
{
    /// Class Imports
    import untyped.projection._


    "The MongoDB untyped projection support" must {
        "be able to produce a valid projectio document" in {
            val specification : BSONDocument = returning {
                doc =>
                    doc.property ::
                    doc.another ::
                    HNil
                }

            BSONDocument.pretty (specification) shouldBe
                BSONDocument.pretty (
                    BSONDocument (
                        "property" -> 1,
                        "another" -> 1
                        )
                    )
            }
        }
}

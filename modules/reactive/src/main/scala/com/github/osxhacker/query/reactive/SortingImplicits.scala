package com.github.osxhacker.query.reactive

import scala.language.implicitConversions

import com.github.osxhacker.query.sorting.{
    SortField,
    SortSpecification
    }

import reactivemongo.api.bson._


/**
 * The '''SortingImplicits''' type defines `implicit` conversions relating to
 * [[com.github.osxhacker.query.sorting.expression]] types.
 */
trait SortingImplicits
{
    /// Class Imports
    import mouse.boolean._
    import shapeless._


    /// Class Types
    object CreateSortDocument
        extends Poly
    {
        implicit def caseSortField[A]
        : ProductCase.Aux[
            BSONDocument :: SortField[A] :: HNil,
            BSONDocument
        ] =
            use {
                (accum : BSONDocument, sortField : SortField[A]) =>
                    val direction = sortField.ascending
                        .fold (1, -1)

                    accum ++ BSONDocument (sortField.path -> direction)
            }
    }


    /// Implicit Conversions
    implicit def sortSpecificationToBSONDocument[T <: AnyRef, HL <: HList] (
        specification : SortSpecification[T, HL]
    )
        (
            implicit
            folder : ops.hlist.LeftFolder.Aux[
                HL,
                BSONDocument,
                CreateSortDocument.type,
                BSONDocument
            ]
        )
    : BSONDocument =
    {
        folder (specification.fields, BSONDocument ())
    }
}

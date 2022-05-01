package com.github.osxhacker.query.mongodb

import scala.language.implicitConversions

import com.github.osxhacker.query.sorting.{
    SortField,
    SortSpecification
    }

import org.mongodb.scala.bson.{
    BsonDocument,
    BsonInt32
    }


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
            BsonDocument :: SortField[A] :: HNil,
            BsonDocument
        ] =
            use {
                (accum : BsonDocument, sortField : SortField[A]) =>
                    val direction = sortField.ascending
                        .fold (1, -1)

                    accum.append (sortField.path, BsonInt32 (direction))
            }
    }


    /// Implicit Conversions
    implicit def sortSpecificationToBsonDocument[T, HL <: HList] (
        specification : SortSpecification[T, HL]
    )
        (
            implicit
            folder : ops.hlist.LeftFolder.Aux[
                HL,
                BsonDocument,
                CreateSortDocument.type,
                BsonDocument
            ]
        )
    : BsonDocument =
    {
        folder (specification.fields, BsonDocument ())
    }
}

package com.github.osxhacker.query.reactive

import scala.language.implicitConversions

import com.github.osxhacker.query.projection.ProjectionSpecification
import reactivemongo.api.bson._


/**
 * The '''ProjectionImplicits''' type defines `implicit` conversions relating to
 * [[com.github.osxhacker.query.projection]] types.
 */
trait ProjectionImplicits
{
    /// Implicit Conversions
    implicit def projectionSpecificationToBsonDocument[T] (
        specification : ProjectionSpecification[T]
    )
    : BSONDocument =
    {
        specification.fields.foldLeft (BSONDocument ()) {
            case (accum, field) =>
                accum ++ BSONDocument (field._property$path -> 1)
        }
    }
}

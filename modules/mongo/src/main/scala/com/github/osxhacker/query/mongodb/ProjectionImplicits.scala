package com.github.osxhacker.query.mongodb

import scala.language.implicitConversions

import com.github.osxhacker.query.projection.ProjectionSpecification
import org.mongodb.scala.bson.{
    BsonDocument,
    BsonInt32
    }


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
    : BsonDocument =
    {
        specification.fields.foldLeft (BsonDocument ()) {
            case (accum, field) =>
                accum.append (field._property$path, BsonInt32 (1))
        }
    }
}

package io.github.osxhacker.query.mongodb

import scala.language.implicitConversions

import io.github.osxhacker.query.criteria.expression.Expression
import org.mongodb.scala.bson.BsonDocument


/**
 * The '''ExpressionImplicits''' type defines `implicit` conversions relating
 * to [[io.github.osxhacker.query.criteria.expression]] types.
 */
trait ExpressionImplicits
{
    /// Implicit Conversions
    implicit def expressionToBsonDocument (
        expression : Expression[BsonDocument]
        )
    : BsonDocument =
        expression.toDocument ()


    implicit def optionalExpressionToBsonDocument (
        expression : Option[Expression[BsonDocument]]
        )
    : BsonDocument =
        expression.fold (BsonDocument ()) {
            _.toDocument ()
        }
}


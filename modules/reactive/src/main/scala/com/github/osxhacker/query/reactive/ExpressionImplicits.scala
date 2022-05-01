package com.github.osxhacker.query.reactive

import scala.language.implicitConversions

import com.github.osxhacker.query.criteria.expression.Expression
import reactivemongo.api.bson.BSONDocument


/**
 * The '''ExpressionImplicits''' type defines `implicit` conversions relating
 * to [[com.github.osxhacker.query.criteria.expression]] types.
 */
trait ExpressionImplicits
{
    /// Implicit Conversions
    implicit def expressionToBSONDocument (
        expression : Expression[BSONDocument]
        )
    : BSONDocument =
        expression.toDocument ()


    implicit def optionalExpressionToBSONDocument (
        expression : Option[Expression[BSONDocument]]
        )
    : BSONDocument =
        expression.fold (BSONDocument ()) {
            _.toDocument ()
        }
}


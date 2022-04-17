package com.github.osxhacker.query.criteria.expression

import scala.language.implicitConversions

import com.github.osxhacker.query.DocumentWriter
import com.github.osxhacker.query.criteria.Field
import com.github.osxhacker.query.model.Operator


/**
 * The '''Element''' trait is the common ancestor for __all__ abstract syntax
 * tree element operators supported.
 */
sealed trait Element[DocumentT <: AnyRef]
{
    /// Self Type Constraints
    this : Expression[DocumentT] =>
}


final case class Exists[DocumentT <: AnyRef, T] (
    val field : Field[T],
    val value : Boolean
    )
    (
        implicit
        private val writer : DocumentWriter[Exists[DocumentT, T], DocumentT]
    )
    extends UnaryExpression[DocumentT, T]
        with Element[DocumentT]
{
    /// Instance Properties
    override val operand : Operator = Operator.Exists


    override def negate () : Exists[DocumentT, T] = Exists (field, !value)


    override def toDocument () : DocumentT = writer.write (this)
}


final class ElementOps[T] (private val self : Field[T])
    extends AnyVal
{
    /**
     * Field existence: '''$exists'''.
     */
    def exists[DocumentT <: AnyRef] (
        implicit writer : DocumentWriter[Exists[DocumentT, T], DocumentT]
    )
    : Exists[DocumentT, T] =
        Exists (self, true)
}


trait ElementSyntax
{
    /// Implicit Conversions
    implicit def toElementOps[T] (field : Field[T]) : ElementOps[T] =
        new ElementOps[T] (field)
}


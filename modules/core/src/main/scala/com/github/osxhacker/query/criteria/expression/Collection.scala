package com.github.osxhacker.query.criteria.expression

import scala.language.implicitConversions

import cats.data.Chain
import com.github.osxhacker.query.DocumentWriter
import com.github.osxhacker.query.criteria.Field
import com.github.osxhacker.query.model.Operator


/**
 * The '''Collection''' trait is the common ancestor for __all__ abstract syntax
 * tree array operators supported.
 */
sealed trait Collection[DocumentT <: AnyRef]
{
    /// Self Type Constraints
    this : Expression[DocumentT] =>
}


final case class All[DocumentT <: AnyRef, T, U <: T] (
    val field : Field[Seq[T]],
    val values : Chain[U]
    )
    (
        implicit
        private val writer : DocumentWriter[All[DocumentT, T, U], DocumentT],
        private val notWriter : DocumentWriter[Not[DocumentT], DocumentT]
    )
    extends BinaryExpression[DocumentT, T, Chain[U]]
        with Collection[DocumentT]
{
    /// Instance Properties
    override val operand : Operator = Operator.All


    override def negate () : Expression[DocumentT] = Not (this)


    override def toDocument () : DocumentT = writer.write (this)
}


final class CollectionOps[T] (
    private val self : Field[Seq[T]]
    )
    extends AnyVal
{
    /**
     * Collection containment: '''$all'''.
     */
    def all[DocumentT <: AnyRef, U <: T] (value : U, additional : U*)
        (
            implicit
            writer : DocumentWriter[All[DocumentT, T, U], DocumentT],
            notWriter : DocumentWriter[Not[DocumentT], DocumentT]
        )
    : All[DocumentT, T, U] =
        All (self, Chain (value) ++ Chain.fromSeq (additional))


    /**
     * Collection containment: '''$all'''.
     */
    def all[DocumentT <: AnyRef, U <: T] (values : Seq[U])
        (
            implicit
            writer : DocumentWriter[All[DocumentT, T, U], DocumentT],
            notWriter : DocumentWriter[Not[DocumentT], DocumentT]
        )
    : All[DocumentT, T, U] =
        All (self, Chain.fromSeq (values))
}


sealed trait LowPriorityCollectionSyntax
{
    /// Implicit Conversions
    implicit def toUntypedCollectionOps[C[_] <: Seq[_], Any] (
        field : Field[C[Any]]
        )
    : CollectionOps[Any] =
        new CollectionOps[Any] (Field[Seq[Any]] (field._property$path))
}


trait CollectionSyntax
    extends LowPriorityCollectionSyntax
{
    /// Implicit Conversions
    implicit def toCollectionOps[C[_] <: Seq[_], T] (field : Field[C[T]])
    : CollectionOps[T] =
        new CollectionOps[T] (Field[Seq[T]] (field._property$path))
}


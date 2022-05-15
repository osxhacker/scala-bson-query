package com.github.osxhacker.query.projection

import scala.language.experimental.macros

import com.github.osxhacker.query.criteria.Field
import shapeless.{
    syntax => _,
    _
    }

import ops.hlist.ToTraversable


/**
 * The '''ProjectionSpecification''' captures the definition of zero or more
 * [[com.github.osxhacker.query.criteria.Field]]s which should be returned
 * from a query.
 */
final case class ProjectionSpecification[T] (val fields : Seq[Field[_]])


object ProjectionSpecification
{
    /// Class Types
    final class PartiallyConstructed[T <: Product]
    {
        def apply ()
        : ProjectionSpecification[T] =
            macro TypedMacros.deriveProjection[T]


        def apply[HL <: HList, N <: Nat] (fields : HL)
            (
                implicit
                toTraversableAux : ToTraversable.Aux[HL, Seq, Field[_]],
                length : ops.hlist.Length.Aux[HL, N],
                nonEmpty : ops.nat.GT[N, _0]
            )
        : ProjectionSpecification[T] =
            new ProjectionSpecification[T] (toTraversableAux(fields))
    }


    def apply[T <: Product] : PartiallyConstructed[T] =
        new PartiallyConstructed[T]
}

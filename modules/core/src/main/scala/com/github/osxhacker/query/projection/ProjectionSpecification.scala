package com.github.osxhacker.query.projection

import shapeless.{
    syntax => _,
    _
    }

import ops.hlist.ToTraversable


/**
 * The '''ProjectionSpecification''' captures the definition of zero or more
 * [[com.github.osxhacker.query.projection.ProjectField]]s which should be
 * returned from a query.
 */
final case class ProjectionSpecification[T] (val fields : Seq[ProjectField[_]])


object ProjectionSpecification
{
    /// Class Types
    final class PartiallyConstructed[T]
    {
        def apply[HL <: HList, N <: Nat] (fields : HL)
            (
                implicit
                toTraversableAux : ToTraversable.Aux[HL, Seq, ProjectField[_]],
                length : ops.hlist.Length.Aux[HL, N],
                nonEmpty : ops.nat.GT[N, _0]
            )
        : ProjectionSpecification[T] =
            new ProjectionSpecification[T] (toTraversableAux(fields))
    }


    def apply[T] : PartiallyConstructed[T] =
        new PartiallyConstructed[T]
}

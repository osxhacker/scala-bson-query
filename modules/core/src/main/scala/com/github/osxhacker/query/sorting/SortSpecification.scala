package com.github.osxhacker.query.sorting

import shapeless.{
    syntax => _,
    _
    }

import ops.hlist.Mapped


/**
 * The '''SortSpecification''' type captures the ordered definition of one or
 * more [[com.github.osxhacker.query.sorting.SortField]]s participating in the
 * production of a `sort`.
 */
final case class SortSpecification[T, HL <: HList] (val fields : HL)
    (implicit private val mapped : Mapped[HL, SortField])


object SortSpecification
{
    /// Class Types
    final class PartiallyConstructed[T <: AnyRef]
    {
        def apply[HL <: HList, N <: Nat] (fields : HL)
            (
                implicit
                mapped : Mapped[HL, SortField],
                length : ops.hlist.Length.Aux[HL, N],
                nonEmpty : ops.nat.GT[N, _0]
            )
        : SortSpecification[T, HL] =
            new SortSpecification[T, HL] (fields)
    }


    def apply[T <: AnyRef] : PartiallyConstructed[T] =
        new PartiallyConstructed[T]
}

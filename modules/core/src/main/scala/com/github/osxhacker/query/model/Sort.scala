package com.github.osxhacker.query.model

import scala.language.implicitConversions

import com.github.osxhacker.query.sorting.{
    SortField,
    SortSpecification
    }

import shapeless.{
    syntax => _,
    _
    }


/**
 * The '''Sort''' type defines a type class used in constructing
 * [[com.github.osxhacker.query.sorting.SortSpecification]]s with both the
 * `typed` and `untyped` `by` methods.
 */
sealed trait Sort[T <: AnyRef]
{
    /// Class Types
    type FieldsType <: HList


    def apply (accessors : FieldsType) : SortSpecification[T, FieldsType]
}


object Sort
    extends SortImplicits
{
    // Class Types
    type Aux[T <: AnyRef, HL] = Sort[T] {
        type FieldsType = HL
    }


    final class PartiallyConstructed[T <: AnyRef] (
        private val accessor : SortFieldAccess[T]
        )
    {
        def apply[HL <: HList] (f : SortFieldAccess[T] => HL)
            (implicit sort : Sort.Aux[T, HL])
        : SortSpecification[T, HL] =
            sort (f (accessor))
    }


    def apply[T <: AnyRef] (accessor : SortFieldAccess[T])
    : PartiallyConstructed[T] =
        new PartiallyConstructed[T] (accessor)
}


sealed trait SortImplicits
{
    /// Implicit Conversions
    implicit def toSort[T <: AnyRef, HL <: HList, N <: Nat] (
        implicit
        mapped : ops.hlist.Mapped[HL, SortField],
        length : Lazy[ops.hlist.Length.Aux[HL, N]],
        nonEmpty : Lazy[ops.nat.GT[N, _0]]
    )
    : Sort.Aux[T, HL] =
        new Sort[T] {
            override type FieldsType = HL


            override def apply (accessors : HL) : SortSpecification[T, HL] =
                new SortSpecification[T, HL] (accessors)
        }
}

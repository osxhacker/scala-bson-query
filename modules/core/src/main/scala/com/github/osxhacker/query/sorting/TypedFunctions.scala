package com.github.osxhacker.query.sorting

import com.github.osxhacker.query.model.{
    Sort,
    SortFieldAccess
    }


/**
 * The '''TypedFunctions''' type provides the ability to ''lift'' arbitrary
 * [[com.github.osxhacker.query.sorting.SortField]]s into a type-safe
 * [[com.github.osxhacker.query.sorting.SortSpecification]].
 */
trait TypedFunctions
{
    /**
     * The by method provides syntactic sugar for creating a typed
     * [[com.github.osxhacker.query.sorting.SortSpecification]] of arbitrary
     * length.
     */
    def by[T <: AnyRef] : Sort.PartiallyConstructed[T] =
        Sort (new SortFieldAccess[T])
}

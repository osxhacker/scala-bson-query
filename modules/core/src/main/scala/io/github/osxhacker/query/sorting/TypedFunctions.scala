package io.github.osxhacker.query.sorting

import io.github.osxhacker.query.model.{
    Sort,
    SortFieldAccess
    }


/**
 * The '''TypedFunctions''' type provides the ability to ''lift'' arbitrary
 * [[io.github.osxhacker.query.sorting.SortField]]s into a type-safe
 * [[io.github.osxhacker.query.sorting.SortSpecification]].
 */
trait TypedFunctions
{
    /**
     * The by method provides syntactic sugar for creating a typed
     * [[io.github.osxhacker.query.sorting.SortSpecification]] of arbitrary
     * length.
     */
    def by[T <: AnyRef] : Sort.PartiallyConstructed[T] =
        Sort (new SortFieldAccess[T])
}

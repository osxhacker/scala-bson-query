package com.github.osxhacker.query.sorting.expression

import scala.language.implicitConversions

import com.github.osxhacker.query.sorting.{
    PartialSortField,
    SortField
    }


/**
 * The '''PartialSortFieldOps''' type defines the operations supported for
 * creating [[com.github.osxhacker.query.sorting.SortField]]s from arbitrary
 * [[com.github.osxhacker.query.sorting.PartialSortField]]s.
 */
final class PartialSortFieldOps[T] (private val self : PartialSortField[T])
    extends AnyVal
{
    /**
     * Sorting: ascending.
     */
    def asc : SortField[T] = SortField (self._property$path, true)


    /**
     * Sorting: ascending.
     */
    def ascending : SortField[T] = asc


    /**
     * Sorting: descending.
     */
    def desc : SortField[T] = SortField (self._property$path, false)


    /**
     * Sorting: descending.
     */
    def descending : SortField[T] = desc
}


trait SortingSyntax
{
    /// Implicit Conversions
    implicit def toPartialSortFieldOps[T] (field : PartialSortField[T])
    : PartialSortFieldOps[T] =
        new PartialSortFieldOps[T] (field)
}


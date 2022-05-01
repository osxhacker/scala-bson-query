package com.github.osxhacker.query.model

import scala.language.experimental.macros

import com.github.osxhacker.query.sorting.{
    PartialSortField,
    TypedMacros
    }


/**
 * The '''SortFieldAccess''' type exists to capture the property path specified
 * for a [[com.github.osxhacker.query.sorting.SortField]].
 */
final class SortFieldAccess[ParentT <: AnyRef] ()
{
    def apply[T] (statement : ParentT => T) : PartialSortField[T] =
        macro TypedMacros.createSortField[ParentT, T]
}

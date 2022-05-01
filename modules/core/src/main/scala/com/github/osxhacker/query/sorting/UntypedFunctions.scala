package com.github.osxhacker.query.sorting

import com.github.osxhacker.query.model.Sort
import shapeless._


trait UntypedFunctions
    extends LowPriorityUntypedFunctions
{
    /**
     * The byEach method constructs a
     * [[com.github.osxhacker.query.sorting.SortSpecification]] from one or
     * more [[com.github.osxhacker.query.sorting.SortField]]s in the given
     * ''HL''.
     */
    def byEach[HL <: HList] (block : Untyped => HL)
        (implicit sort : Sort.Aux[Untyped, HL])
    : SortSpecification[Untyped, HL] =
        sort (block (Untyped ()))


    /**
     * This version of the by method constructs a
     * [[com.github.osxhacker.query.sorting.SortSpecification]] from one
     * [[com.github.osxhacker.query.sorting.SortField]].
     */
    def by (block : Untyped => SortField[Any])
    : SortSpecification[Untyped, SortField[Any] :: HNil] =
        SortSpecification[Untyped, SortField[Any] :: HNil] (
            block (Untyped ()) :: HNil
        )


    /**
     * This version of the by method constructs a
     * [[com.github.osxhacker.query.sorting.SortSpecification]] from two
     * [[com.github.osxhacker.query.sorting.SortField]]s.
     */
    def by (
        first : Untyped => SortField[Any],
        second : Untyped => SortField[Any]
        )
        (
            implicit
            sort : Sort.Aux[Untyped, SortField[Any] :: SortField[Any] :: HNil]
        )
    : SortSpecification[Untyped, SortField[Any] :: SortField[Any] :: HNil] =
        sort (first (Untyped ()) :: second (Untyped ()) :: HNil)


    /**
     * This version of the by method constructs a
     * [[com.github.osxhacker.query.sorting.SortSpecification]] from three
     * [[com.github.osxhacker.query.sorting.SortField]]s.
     */
    def by (
        first : Untyped => SortField[Any],
        second : Untyped => SortField[Any],
        third : Untyped => SortField[Any]
        )
        (
            implicit
            sort : Sort.Aux[
                Untyped,
                SortField[Any] :: SortField[Any] :: SortField[Any] :: HNil
                ]
        )
    : SortSpecification[
        Untyped,
        SortField[Any] :: SortField[Any] :: SortField[Any] :: HNil
    ] =
        sort (
            first (Untyped ()) ::
            second (Untyped ()) ::
            third (Untyped ()) ::
            HNil
        )


    /**
     * This version of the by method constructs a
     * [[com.github.osxhacker.query.sorting.SortSpecification]] from four
     * [[com.github.osxhacker.query.sorting.SortField]]s.
     */
    def by (
        first : Untyped => SortField[Any],
        second : Untyped => SortField[Any],
        third : Untyped => SortField[Any],
        fourth : Untyped => SortField[Any]
        )
        (
            implicit
            sort : Sort.Aux[
                Untyped,
                SortField[Any] ::
                SortField[Any] ::
                SortField[Any] ::
                SortField[Any] ::
                HNil
                ]
        )
    : SortSpecification[
        Untyped,
        SortField[Any] ::
        SortField[Any] ::
        SortField[Any] ::
        SortField[Any] ::
        HNil
        ] =
        sort (
            first (Untyped ()) ::
            second (Untyped ()) ::
            third (Untyped ()) ::
            fourth (Untyped ()) ::
            HNil
            )
}


sealed trait LowPriorityUntypedFunctions
{
}


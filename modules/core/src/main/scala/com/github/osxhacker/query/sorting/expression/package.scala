package com.github.osxhacker.query.sorting


/**
 * The '''expression''' `package` defines syntactic convenience constructs for
 * defining [[com.github.osxhacker.query.sorting.SortSpecification]]s.  It is
 * what enables being able to write:
 *
 * {{{
 *     import com.github.osxhacker.query.sorting
 *     import untyped._
 *
 *     val sortOrder = by (_.first ^ :: _.second v :: HNil)
 * }}}
 *
 * And have the be the equivalent of:
 *
 * {{{
 *     val specification = SortSpecification (
 *         SortField (Field ("first"), ascending = true) ::
 *         SortField (Field ("second"), ascending = false) ::
 *         HNil
 *         )
 * }}}
 */
package object expression
{
    /// Class Types
    object syntax
        extends SortingSyntax
}

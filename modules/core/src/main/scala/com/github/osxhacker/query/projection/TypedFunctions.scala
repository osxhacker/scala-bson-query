package com.github.osxhacker.query.projection

import scala.language.experimental.macros


/**
 * The '''TypedFunctions''' type provides the ability to ''lift'' arbitrary
 * [[com.github.osxhacker.query.model.FieldAccess]]s into a type-safe
 * [[com.github.osxhacker.query.projection.ProjectionSpecification]].
 */
trait TypedFunctions
{
    /**
     * The into method deduces all primary constructor properties and nested
     * [[scala.Product]] properties defined in ''T'' and creates from that a
     * [[com.github.osxhacker.query.projection.ProjectionSpecification]].
     * Container-like property types are detected where possible.  For
     * example:
     *
     * {{{
     *     case class Outer (name : Option[String], inners : List[Inner])
     *     case class Inner (a : Int, b : String, c : Option[Double] = None)
     *
     *     val specification = into[Outer] ()
     *
     *     /// Is the same as
     *     val explicitly = untyped.returning {
     *         available =>
     *             available.name ::
     *             available.inners.a ::
     *             available.inners.b ::
     *             available.inners.c ::
     *             HNil
     *     }
     * }}}
     *
     * This is a convenience method for when ''T'' is __precisely__ what is
     * desired.
     *
     * Use `returning` for more general cases.
     */
    def into[T <: Product] () : ProjectionSpecification[T] =
        macro TypedMacros.deriveProjection[T]
}

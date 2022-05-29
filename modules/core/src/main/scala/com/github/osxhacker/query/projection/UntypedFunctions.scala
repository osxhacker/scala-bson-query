package com.github.osxhacker.query.projection

import com.github.osxhacker.query.model.Project

import shapeless.{
    syntax => _,
    _
    }


trait UntypedFunctions
{
    /**
     * The returning method provides syntactic sugar for creating a
     * [[com.github.osxhacker.query.projection.ProjectionSpecification]] with
     * arbitrary fields.  For example:
     *
     * {{{
     *     case class Outer (name : Option[String], inners : List[Inner])
     *     case class Inner (a : Int, b : String, c : Option[Double] = None)
     *
     *     val explicitly = returning {
     *         available =>
     *             available.name ::
     *             available.inners.a ::
     *             HNil
     *     }
     * }}}
     */
    def returning[HL <: HList] (block : Untyped => HL)
        (implicit project : Project.Aux[Untyped, HL])
    : ProjectionSpecification[Untyped] =
        project (block (Untyped ()))
}

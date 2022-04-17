package com.github.osxhacker.query.model

import scala.language.experimental.macros

import com.github.osxhacker.query.criteria.{
    Field,
    TypedMacros
    }


/**
 * The '''FieldAccess''' type exists for syntactic convenience when the
 * typed `criteria` or `where` methods are used.  The tandem allow for
 * constructs such as:
 *
 * {{{
 * import typed._
 *
 * val typeCheckedQuery = criteria[SomeType] (_.first) < 10 && (
 *    criteria[SomeType] (_.second) >= 20.0 ||
 *    criteria[SomeType] (_.second).in (0.0, 1.0)
 *    )
 * }}}
 */
final class FieldAccess[ParentT <: AnyRef]
{
    def apply[T] (statement : ParentT => T) : Field[T] =
        macro TypedMacros.createField[ParentT, T]
}


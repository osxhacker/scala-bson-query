package com.github.osxhacker.query.model

import scala.language.experimental.macros

import com.github.osxhacker.query.projection.{
    ProjectField,
    TypedMacros
    }


/**
 * The '''ProjectFieldAccess''' type provides the ability to define an explicit
 * path rooted in ''ParentT'' for use in a
 * [[com.github.osxhacker.query.projection.ProjectField]].
 */
final case class ProjectFieldAccess[ParentT] ()
{
    def apply[T] (statement : ParentT => T) : ProjectField[T] =
        macro TypedMacros.createProjectField[ParentT, T]
}

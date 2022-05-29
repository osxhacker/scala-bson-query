package com.github.osxhacker.query.projection

import scala.language.dynamics

/**
 * The '''ProjectField''' type captures a specific property path to use when
 * projecting the result of a [[https://www.mongodb.com/ MongoDB]] operation.
 */
final case class ProjectField[T] (val _property$path : String)
    extends Dynamic
{
    def selectDynamic (path : String) : ProjectField[Any] =
        ProjectField[Any] (_property$path + "." + path)
}

package com.github.osxhacker.query.projection
import scala.language.dynamics


/**
 * The '''Untyped''' type defines the behaviour expected of sorts where the
 * MongoDB document may not correspond to a Scala type known to the system
 * using this abstraction.
 */
sealed trait Untyped
    extends Dynamic
{
    def selectDynamic (path : String) : ProjectField[Any] =
        ProjectField[Any] (path)
}


object Untyped
    extends UntypedFunctions
{
    /**
     * The apply method is provided for syntactic convenience.
     */
    def apply () : Untyped = new Untyped {}
}

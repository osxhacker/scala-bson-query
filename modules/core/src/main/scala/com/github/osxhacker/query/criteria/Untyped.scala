package com.github.osxhacker.query.criteria

import scala.language.dynamics


/**
 * The '''Untyped''' type defines the behaviour expected of queries where the
 * MongoDB document may not correspond to a Scala type known to the system
 * using this abstraction.
 */
sealed trait Untyped
	extends Dynamic
{
	def selectDynamic (path : String) : Field[Any] = Field[Any] (path)
}


object Untyped
	extends UntypedFunctions
{
	/**
	 * The apply method is provided for syntactic convenience.  It is equivalent
	 * to `Untyped.criteria`.
	 */
	def apply () : Untyped = new Untyped {}
}


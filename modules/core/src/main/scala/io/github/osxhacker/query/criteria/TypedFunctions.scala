package io.github.osxhacker.query.criteria

import io.github.osxhacker.query.model.{
	FieldAccess,
	Where
	}


/**
 * The '''TypedFunctions''' type provides the ability to ''lift'' an arbitrary
 * type `T` into the [[io.github.osxhacker.query.criteria]] world.  Each
 * property is represented as a [[io.github.osxhacker.query.criteria.Term]].
 */
trait TypedFunctions
{
	/**
	 * The criteria method produces a type which enforces the existence of
	 * property names within ''T''.
	 */
	def criteria[T <: AnyRef] : FieldAccess[T] = new FieldAccess[T]


	/**
	 * The where method provides syntactic sugar for creating a typed
	 * [[io.github.osxhacker.query.criteria.expression.Expression]] of
	 * arbitrary complexity.
	 */
	def where[T <: AnyRef] : Where.PartiallyConstructed[FieldAccess[T]] =
		Where (new FieldAccess[T])
}


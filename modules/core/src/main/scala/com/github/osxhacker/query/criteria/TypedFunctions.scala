package com.github.osxhacker.query.criteria

import com.github.osxhacker.query.model.{
	FieldAccess,
	Where
	}


/**
 * The '''TypedFunctions''' type provides the ability to ''lift'' an arbitrary
 * type `T` into the [[com.github.osxhacker.query.criteria]] world.  Each
 * property is represented as a [[com.github.osxhacker.query.criteria.Term]].
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
	 * [[com.github.osxhacker.query.criteria.expression.Expression]] of
	 * arbitrary complexity.
	 */
	def where[T <: AnyRef] : Where.PartiallyConstructed[FieldAccess[T]] =
		Where (new FieldAccess[T])
}


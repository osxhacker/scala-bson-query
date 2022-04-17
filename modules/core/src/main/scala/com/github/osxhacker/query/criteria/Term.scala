package com.github.osxhacker.query.criteria

import scala.language.{
	dynamics,
	implicitConversions
	}


/**
 * A '''Term''' instance reifies the use of a MongoDB document
 * [[com.github.osxhacker.query.criteria.Field]] or a
 * [[com.github.osxhacker.query.criteria.Value]] used in an
 * [[com.github.osxhacker.query.criteria.expression.Expression]].  The
 * supported '''Term''' operators __require__ the left-hand side of all
 * [[com.github.osxhacker.query.criteria.expression.Expression]]s to be a
 * [[com.github.osxhacker.query.criteria.Field]] type.
 *
 * All '''Term'''s are driver agnostic.
 */
sealed trait Term[T]


/**
 * The '''Field''' type is a [[com.github.osxhacker.query.criteria.Term]] which
 * represents a MongoDB property path for some arbitrary document.  Path
 * navigation is represented as "dotted notation" within `_property$path`.
 */
final case class Field[T] (val _property$path : String)
	extends Dynamic
		with Term[T]
{
	def selectDynamic (field : String) : Field[T] = Field[T] (
		_property$path + "." + field
	)
}


/**
 * The '''Value''' type is a [[com.github.osxhacker.query.criteria.Term]] which
 * captures an arbitrary `instance` of type ''T''.
 */
final case class Value[T] (val instance : T)
	extends Term[T]


object Value
{
	/// Implicit Conversions
	implicit def instanceToValue[T] (instance : T) : Value[T] = Value (instance)
}


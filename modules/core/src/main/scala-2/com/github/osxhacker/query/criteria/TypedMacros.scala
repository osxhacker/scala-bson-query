package io.github.osxhacker.query.criteria

import scala.reflect.macros.blackbox

import io.github.osxhacker.query.model.FieldAccess


/**
 * The '''TypedMacros''' `object` defines the
 * [[http://docs.scala-lang.org/overviews/macros/overview.html Scala Macros]]
 * used in supporting type-checked [[io.github.osxhacker.query.criteria.Term]]
 * creation.
 */
object TypedMacros
{
	def createField[T <: AnyRef : c.WeakTypeTag, U : c.WeakTypeTag]
		(c : blackbox.Context { type PrefixType = FieldAccess[T] })
		(statement : c.Tree)
		: c.Tree =
	{
		import c.universe._

		val q"""(..${ _ }) => $select""" = statement
		val selectors = select.collect {
			case Select (_, TermName (property)) =>
				property
			}.reverse.mkString (".")

		val propertyType = weakTypeOf[U]

		q"""new _root_.io.github.osxhacker.query.criteria.Field[$propertyType] ($selectors)"""
	}
}


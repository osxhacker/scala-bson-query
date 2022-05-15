package com.github.osxhacker.query.sorting

import scala.reflect.macros.blackbox

import com.github.osxhacker.query.model.SortFieldAccess


/**
 * The '''TypedMacros''' `object` defines the
 * [[http://docs.scala-lang.org/overviews/macros/overview.html Scala Macros]]
 * used in supporting type-checked
 * [[com.github.osxhacker.query.sorting.SortField]] creation.
 */
object TypedMacros
{
    def createSortField[T <: AnyRef : c.WeakTypeTag, U : c.WeakTypeTag]
    (c : blackbox.Context { type PrefixType = SortFieldAccess[T] })
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

        q"""new _root_.com.github.osxhacker.query.sorting.PartialSortField[$propertyType] ($selectors)"""
    }
}

package com.github.osxhacker.query.sorting

import scala.language.dynamics


/**
 * The '''PartialSortField''' type provides the ability to define a property
 * path to be used in the construction of a
 * [[com.github.osxhacker.query.sorting.SortField]].
 */
final case class PartialSortField[T] (val _property$path : String)
    extends Dynamic
{
    def selectDynamic (field : String) : PartialSortField[T] =
        PartialSortField[T] (_property$path + "." + field)
}


/**
 * The '''SortField''' type defines the ability to specify how a specific
 * property path participates in ordering of a
 * [[https://www.mongodb.com/ MongoDB]] operation.
 */
final case class SortField[T] (val path : String, val ascending : Boolean)


object SortField
{
    def apply[T] (partial : PartialSortField[T], ascending : Boolean)
    : SortField[T] =
        SortField (partial._property$path, ascending)
}

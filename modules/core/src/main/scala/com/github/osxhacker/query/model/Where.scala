package com.github.osxhacker.query.model

import com.github.osxhacker.query.criteria.expression.Expression


/**
 * The '''Where''' type defines a type class used in constructing
 * [[com.github.osxhacker.query.criteria.expression.Expression]]s with both the
 * `typed` and `untyped` `where` methods.
 */
sealed trait Where[AccessorT]
{
    /// Class Types
    type ResultType
}


object Where
{
    /// Class Types
    type Aux[A, R] = Where[A] {
        type ResultType = R
        }


    final class PartiallyConstructed[T] (args : T)
    {
        def apply[ResultT] (f : T => ResultT)
            (implicit where : Where.Aux[T, ResultT])
        : ResultT =
            f (args)
    }


    def apply[T] (args : T) : PartiallyConstructed[T] =
        new PartiallyConstructed[T] (args)


    /// Implicit Conversions
    implicit def whereExpression[A, R <: Expression[_]] : Where.Aux[A, R] =
        new Where[A] {
            override type ResultType = R
            }


    implicit def whereOptionalExpression[A, R <: Expression[_]]
    : Where.Aux[A, Option[R]] =
        new Where[A] {
            override type ResultType = Option[R]
            }
}


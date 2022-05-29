package com.github.osxhacker.query.model

import scala.language.implicitConversions

import com.github.osxhacker.query.projection.{
    ProjectField,
    ProjectionSpecification
    }

import shapeless.{
    syntax => _,
    _
    }


/**
 * The '''Project''' type defines a type class used in constructing
 * [[com.github.osxhacker.query.projection.ProjectionSpecification]]s with both
 * the `typed` and `untyped` `returning` methods.
 */
sealed trait Project[T <: AnyRef]
{
    /// Class Types
    type FieldsType <: HList


    def apply (accessors : FieldsType) : ProjectionSpecification[T]
}


object Project
    extends ProjectImplicits
{
    // Class Types
    type Aux[T <: AnyRef, HL] = Project[T] {
        type FieldsType = HL
    }


    final class PartiallyConstructed[T <: AnyRef] (
        private val accessor : ProjectFieldAccess[T]
    )
    {
        def apply[HL <: HList] (f : ProjectFieldAccess[T] => HL)
            (implicit project : Project.Aux[T, HL])
        : ProjectionSpecification[T] =
            project (f (accessor))
    }


    def apply[T <: AnyRef] (accessor : ProjectFieldAccess[T])
    : PartiallyConstructed[T] =
        new PartiallyConstructed[T] (accessor)
}


sealed trait ProjectImplicits
{
    /// Class Imports
    import ops.hlist.{
        Length,
        ToTraversable
        }


    /// Implicit Conversions
    implicit def toProject[T <: AnyRef, HL <: HList, N <: Nat] (
        implicit
        toTraversableAux : ToTraversable.Aux[HL, Seq, ProjectField[_]],
        length : Lazy[Length.Aux[HL, N]],
        nonEmpty : Lazy[ops.nat.GT[N, _0]]
    )
    : Project.Aux[T, HL] =
        new Project[T] {
            override type FieldsType = HL


            override def apply (accessors : HL) : ProjectionSpecification[T] =
                new ProjectionSpecification[T] (toTraversableAux (accessors))
        }
}

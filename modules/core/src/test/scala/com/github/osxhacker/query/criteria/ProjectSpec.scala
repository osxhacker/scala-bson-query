package com.github.osxhacker.query.criteria

import com.github.osxhacker.query.DocumentWriter
import com.github.osxhacker.query.criteria.expression._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


/**
 * The '''ProjectSpec''' `trait` defines a common
 * [[http://www.scalatest.org/user_guide/selecting_a_style Scalatest style]]
 * to use across unit tests in the `package`.
 */
trait ProjectSpec
    extends AnyWordSpec
        with Matchers
{
    /// Class Types
    final case class Address (
        val street : String,
        val city : String,
        val state : String
        )


    final case class SampleDocument (
        val name : String,
        val age : Int,
        val address : Address
        )


    /// Implicit Conversions
    implicit def allWriter[A, B <: A]
    : DocumentWriter[All[AnyRef, A, B], AnyRef] =
    {
        new DocumentWriter[All[AnyRef, A, B], AnyRef] {
            override def write (tree : All[AnyRef, A, B])
            : AnyRef =
                tree
        }
    }


    implicit def andWriter
    : DocumentWriter[And[AnyRef], AnyRef] =
    {
        new DocumentWriter[And[AnyRef], AnyRef] {
            override def write (tree : And[AnyRef]) : AnyRef =
                tree
        }
    }


    implicit def equalToWriter[A, B <: A]
    : DocumentWriter[EqualTo[AnyRef, A, B], AnyRef] =
    {
        new DocumentWriter[EqualTo[AnyRef, A, B], AnyRef] {
            override def write (tree : EqualTo[AnyRef, A, B]) : AnyRef =
                tree
        }
    }


    implicit def existsWriter[A]
    : DocumentWriter[Exists[AnyRef, A], AnyRef] =
    {
        new DocumentWriter[Exists[AnyRef, A], AnyRef] {
            override def write (tree : Exists[AnyRef, A])
            : AnyRef =
                tree
        }
    }


    implicit def greaterThanWriter[A, B <: A]
    : DocumentWriter[GreaterThan[AnyRef, A, B], AnyRef] =
    {
        new DocumentWriter[GreaterThan[AnyRef, A, B], AnyRef] {
            override def write (tree : GreaterThan[AnyRef, A, B])
            : AnyRef =
                tree
        }
    }


    implicit def greaterThanOrEqualWriter[A, B <: A]
    : DocumentWriter[GreaterThanOrEqual[AnyRef, A, B], AnyRef] =
    {
        new DocumentWriter[GreaterThanOrEqual[AnyRef, A, B], AnyRef] {
            override def write (tree : GreaterThanOrEqual[AnyRef, A, B])
            : AnyRef =
                tree
        }
    }


    implicit def inWriter[A, B <: A]
    : DocumentWriter[In[AnyRef, A, B], AnyRef] =
    {
        new DocumentWriter[In[AnyRef, A, B], AnyRef] {
            override def write (tree : In[AnyRef, A, B]) : AnyRef =
                tree
        }
    }


    implicit def lessThanWriter[A, B <: A]
    : DocumentWriter[LessThan[AnyRef, A, B], AnyRef] =
    {
        new DocumentWriter[LessThan[AnyRef, A, B], AnyRef] {
            override def write (tree : LessThan[AnyRef, A, B])
            : AnyRef =
                tree
        }
    }


    implicit def lessThanOrEqualWriter[A, B <: A]
    : DocumentWriter[LessThanOrEqual[AnyRef, A, B], AnyRef] =
    {
        new DocumentWriter[LessThanOrEqual[AnyRef, A, B], AnyRef] {
            override def write (tree : LessThanOrEqual[AnyRef, A, B])
            : AnyRef =
                tree
        }
    }


    implicit def norWriter[A <: AnyRef]
    : DocumentWriter[Nor[A], AnyRef] =
    {
        new DocumentWriter[Nor[A], AnyRef] {
            override def write (tree : Nor[A]) : AnyRef =
                tree
        }
    }


    implicit def notWriter
    : DocumentWriter[Not[AnyRef], AnyRef] =
    {
        new DocumentWriter[Not[AnyRef], AnyRef] {
            override def write (tree : Not[AnyRef]) : AnyRef =
                tree
        }
    }


    implicit def notEqualToWriter[A, B <: A]
    : DocumentWriter[NotEqualTo[AnyRef, A, B], AnyRef] =
    {
        new DocumentWriter[NotEqualTo[AnyRef, A, B], AnyRef] {
            override def write (tree : NotEqualTo[AnyRef, A, B]) : AnyRef =
                tree
        }
    }


    implicit def notInWriter[A, B <: A]
    : DocumentWriter[NotIn[AnyRef, A, B], AnyRef] =
    {
        new DocumentWriter[NotIn[AnyRef, A, B], AnyRef] {
            override def write (tree : NotIn[AnyRef, A, B]) : AnyRef =
                tree
        }
    }


    implicit def orWriter
    : DocumentWriter[Or[AnyRef], AnyRef] =
    {
        new DocumentWriter[Or[AnyRef], AnyRef] {
            override def write (tree : Or[AnyRef]) : AnyRef =
                tree
        }
    }


    implicit def regularExpressionWriter[A >: String]
    : DocumentWriter[RegularExpression[AnyRef, A], AnyRef] =
    {
        new DocumentWriter[RegularExpression[AnyRef, A], AnyRef] {
            override def write (tree : RegularExpression[AnyRef, A])
            : AnyRef =
                tree
        }
    }
}

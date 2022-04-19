package com.github.osxhacker.query.reactive

import java.time.OffsetDateTime
import java.util.{
	Date,
	UUID
	}

import com.github.osxhacker.query.DocumentWriter
import com.github.osxhacker.query.criteria.expression._
import reactivemongo.api.bson._



sealed trait ValueWriterImplicits
{
    /// Implicit Conversions
    implicit val booleanWriter : DocumentWriter[Boolean, BSONValue] =
        new DocumentWriter[Boolean, BSONValue] {
            override def write (value : Boolean) : BSONValue =
                BSONBoolean (value)
        }

    implicit val dateWriter : DocumentWriter[Date, BSONValue] =
        new DocumentWriter[Date, BSONValue] {
            override def write (value : Date) : BSONValue =
                BSONDateTime (value.getTime)
        }

    implicit val doubleWriter : DocumentWriter[Double, BSONValue] =
        new DocumentWriter[Double, BSONValue] {
            override def write (value : Double) : BSONValue =
                BSONDouble (value)
        }

    implicit val floatWriter : DocumentWriter[Float, BSONValue] =
        new DocumentWriter[Float, BSONValue] {
            override def write (value : Float) : BSONValue =
                BSONDouble (value)
        }

    implicit val intWriter : DocumentWriter[Int, BSONValue] =
        new DocumentWriter[Int, BSONValue] {
            override def write (value : Int) : BSONValue =
                BSONInteger (value)
        }

    implicit val longWriter : DocumentWriter[Long, BSONValue] =
        new DocumentWriter[Long, BSONValue] {
            override def write (value : Long) : BSONValue =
                BSONLong (value)
        }

    implicit val offsetDateTimeWriter
    : DocumentWriter[OffsetDateTime, BSONValue] =
        new DocumentWriter[OffsetDateTime, BSONValue] {
            override def write (value : OffsetDateTime) : BSONValue =
                BSONDateTime (value.toEpochSecond)
        }

    implicit val stringWriter : DocumentWriter[String, BSONValue] =
        new DocumentWriter[String, BSONValue] {
            override def write (value : String) : BSONValue =
                BSONString (value)
        }

    implicit val uuidWriter : DocumentWriter[UUID, BSONValue] =
        new DocumentWriter[UUID, BSONValue] {
            override def write (value : UUID) : BSONValue =
                BSONObjectID.fromTime (value.timestamp ())
        }
}


trait CriteriaWriterImplicits
    extends ValueWriterImplicits
{
    /// Implicit Conversions
    implicit def allWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[A, BSONValue]
    )
    : DocumentWriter[All[BSONDocument, A, B], BSONDocument] =
    {
        new DocumentWriter[All[BSONDocument, A, B], BSONDocument] {
            override def write (tree : All[BSONDocument, A, B])
            : BSONDocument =
                BSONDocument (
                    tree.field._property$path ->
                    BSONDocument (
                        tree.operand.name ->
                            BSONArray (
                                tree.values
                                    .map (valueWriter.write(_))
                                    .toVector
                            )
                        )
                    )
        }
    }


    implicit def andWriter
    : DocumentWriter[And[BSONDocument], BSONDocument] =
    {
        new DocumentWriter[And[BSONDocument], BSONDocument] {
            override def write (tree : And[BSONDocument]) : BSONDocument =
                BSONDocument (
                    tree.operand.name ->
                    BSONArray (
                        tree.expressions
                            .map (_.toDocument ())
                            .toNonEmptyVector
                            .toVector
                        )
                    )
        }
    }


    implicit def equalToWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[B, BSONValue]
    )
    : DocumentWriter[EqualTo[BSONDocument, A, B], BSONDocument] =
    {
        new DocumentWriter[EqualTo[BSONDocument, A, B], BSONDocument] {
            override def write (tree : EqualTo[BSONDocument, A, B]) : BSONDocument =
                BSONDocument (
                    tree.field._property$path ->
                    valueWriter.write (tree.value.instance)
                    )
        }
    }


    implicit def existsWriter[A]
    : DocumentWriter[Exists[BSONDocument, A], BSONDocument] =
    {
        new DocumentWriter[Exists[BSONDocument, A], BSONDocument] {
            override def write (tree : Exists[BSONDocument, A])
            : BSONDocument =
                BSONDocument (
                    tree.field._property$path ->
                    BSONDocument (
                        tree.operand.name ->
                        booleanWriter.write (tree.value)
                        )
                    )
        }
    }


    implicit def greaterThanWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[B, BSONValue]
        )
    : DocumentWriter[GreaterThan[BSONDocument, A, B], BSONDocument] =
    {
        new DocumentWriter[GreaterThan[BSONDocument, A, B], BSONDocument] {
            override def write (tree : GreaterThan[BSONDocument, A, B])
            : BSONDocument =
                BSONDocument (
                    tree.field._property$path ->
                    BSONDocument (
                        tree.operand.name ->
                            valueWriter.write (tree.value.instance)
                        )
                    )
        }
    }


    implicit def greaterThanOrEqualWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[B, BSONValue]
        )
    : DocumentWriter[GreaterThanOrEqual[BSONDocument, A, B], BSONDocument] =
    {
        new DocumentWriter[GreaterThanOrEqual[BSONDocument, A, B], BSONDocument] {
            override def write (tree : GreaterThanOrEqual[BSONDocument, A, B])
            : BSONDocument =
                BSONDocument (
                    tree.field._property$path ->
                    BSONDocument (
                        tree.operand.name ->
                        valueWriter.write (tree.value.instance)
                        )
                    )
            }
    }


    implicit def inWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[B, BSONValue]
        )
    : DocumentWriter[In[BSONDocument, A, B], BSONDocument] =
    {
        new DocumentWriter[In[BSONDocument, A, B], BSONDocument] {
            override def write (tree : In[BSONDocument, A, B]) : BSONDocument =
                BSONDocument (
                    tree.field._property$path ->
                    BSONDocument (
                        tree.operand.name ->
                        BSONArray (
                            tree.values
                                .map (v => valueWriter.write (v))
                                .toVector
                        )
                    )
                )
        }
    }


    implicit def lessThanWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[B, BSONValue]
        )
    : DocumentWriter[LessThan[BSONDocument, A, B], BSONDocument] =
    {
        new DocumentWriter[LessThan[BSONDocument, A, B], BSONDocument] {
            override def write (tree : LessThan[BSONDocument, A, B])
            : BSONDocument =
                BSONDocument (
                    tree.field._property$path ->
                    BSONDocument (
                        tree.operand.name ->
                        valueWriter.write (tree.value.instance)
                        )
                    )
        }
    }


    implicit def lessThanOrEqualWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[B, BSONValue]
        )
    : DocumentWriter[LessThanOrEqual[BSONDocument, A, B], BSONDocument] =
    {
        new DocumentWriter[LessThanOrEqual[BSONDocument, A, B], BSONDocument] {
            override def write (tree : LessThanOrEqual[BSONDocument, A, B])
            : BSONDocument =
                BSONDocument (
                    tree.field._property$path ->
                    BSONDocument (
                        tree.operand.name ->
                        valueWriter.write (tree.value.instance)
                        )
                    )
        }
    }


    implicit def norWriter[A <: BSONDocument]
    : DocumentWriter[Nor[A], BSONDocument] =
    {
        new DocumentWriter[Nor[A], BSONDocument] {
            override def write (tree : Nor[A]) : BSONDocument =
                BSONDocument (
                    tree.operand.name ->
                    BSONArray (
                        tree.expressions
                            .map (_.toDocument ())
                            .toNonEmptyVector
                            .toVector
                        )
                    )
        }
    }


    implicit def notWriter
    : DocumentWriter[Not[BSONDocument], BSONDocument] =
    {
        new DocumentWriter[Not[BSONDocument], BSONDocument] {
            override def write (tree : Not[BSONDocument]) : BSONDocument =
                BSONDocument (
                    tree.operand.name -> tree.expression.toDocument ()
                )
        }
    }


    implicit def notEqualToWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[B, BSONValue]
        )
    : DocumentWriter[NotEqualTo[BSONDocument, A, B], BSONDocument] =
    {
        new DocumentWriter[NotEqualTo[BSONDocument, A, B], BSONDocument] {
            override def write (tree : NotEqualTo[BSONDocument, A, B]) : BSONDocument =
                BSONDocument (
                    tree.field._property$path ->
                    BSONDocument (
                        tree.operand.name ->
                        valueWriter.write (tree.value.instance)
                        )
                    )
        }
    }


    implicit def notInWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[B, BSONValue]
        )
    : DocumentWriter[NotIn[BSONDocument, A, B], BSONDocument] =
    {
        new DocumentWriter[NotIn[BSONDocument, A, B], BSONDocument] {
            override def write (tree : NotIn[BSONDocument, A, B]) : BSONDocument =
                BSONDocument (
                    tree.field._property$path ->
                    BSONDocument (
                        tree.operand.name ->
                        BSONArray (
                            tree.values
                                .map (v => valueWriter.write (v))
                                .toVector
                            )
                        )
                    )
        }
    }


    implicit def orWriter
    : DocumentWriter[Or[BSONDocument], BSONDocument] =
    {
        new DocumentWriter[Or[BSONDocument], BSONDocument] {
            override def write (tree : Or[BSONDocument]) : BSONDocument =
                BSONDocument (
                    tree.operand.name ->
                    BSONArray (
                        tree.expressions
                            .map (_.toDocument ())
                            .toNonEmptyVector
                            .toVector
                    )
                )
        }
    }


    implicit def regularExpressionWriter[A >: String]
    : DocumentWriter[RegularExpression[BSONDocument, A], BSONDocument] =
    {
        new DocumentWriter[RegularExpression[BSONDocument, A], BSONDocument] {
            override def write (tree : RegularExpression[BSONDocument, A])
            : BSONDocument =
                BSONDocument (
                    tree.field._property$path ->
                    BSONDocument (
                        tree.operand.name ->
                        tree.modifier
                            .map (_.value)
                            .fold (BSONRegex (tree.value.instance, "")) (
                                BSONRegex (tree.value.instance, _)
                            )
                    )
                )
        }
    }
}


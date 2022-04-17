package com.github.osxhacker.query.mongodb

import java.time.OffsetDateTime
import java.util.Date

import com.github.osxhacker.query.DocumentWriter
import com.github.osxhacker.query.criteria.expression._
import org.mongodb.scala.bson._

import java.util.UUID


sealed trait ValueWriterImplicits
{
    /// Implicit Conversions
    implicit val bigDecimalWriter : DocumentWriter[BigDecimal, BsonValue] =
        new DocumentWriter[BigDecimal, BsonValue] {
            override def write (value : BigDecimal) : BsonValue =
                BsonDecimal128 (value)
        }

    implicit val bigIntWriter : DocumentWriter[BigInt, BsonValue] =
        new DocumentWriter[BigInt, BsonValue] {
            override def write (value : BigInt) : BsonValue =
                BsonDecimal128 (value.toString ())
        }

    implicit val booleanWriter : DocumentWriter[Boolean, BsonValue] =
        new DocumentWriter[Boolean, BsonValue] {
            override def write (value : Boolean) : BsonValue =
                BsonBoolean (value)
        }

    implicit val dateWriter : DocumentWriter[Date, BsonValue] =
        new DocumentWriter[Date, BsonValue] {
            override def write (value : Date) : BsonValue =
                BsonDateTime (value)
        }

    implicit val doubleWriter : DocumentWriter[Double, BsonValue] =
        new DocumentWriter[Double, BsonValue] {
            override def write (value : Double) : BsonValue =
                BsonDouble (value)
        }

    implicit val floatWriter : DocumentWriter[Float, BsonValue] =
        new DocumentWriter[Float, BsonValue] {
            override def write (value : Float) : BsonValue =
                BsonDouble (value)
        }

    implicit val intWriter : DocumentWriter[Int, BsonValue] =
        new DocumentWriter[Int, BsonValue] {
            override def write (value : Int) : BsonValue =
                BsonInt32 (value)
        }

    implicit val longWriter : DocumentWriter[Long, BsonValue] =
        new DocumentWriter[Long, BsonValue] {
            override def write (value : Long) : BsonValue =
                BsonInt64 (value)
        }

    implicit val offsetDateTimeWriter
    : DocumentWriter[OffsetDateTime, BsonValue] =
        new DocumentWriter[OffsetDateTime, BsonValue] {
            override def write (value : OffsetDateTime) : BsonValue =
                BsonDateTime (value.toEpochSecond)
        }

    implicit val stringWriter : DocumentWriter[String, BsonValue] =
        new DocumentWriter[String, BsonValue] {
            override def write (value : String) : BsonValue =
                BsonString (value)
        }

    implicit val uuidWriter : DocumentWriter[UUID, BsonValue] =
        new DocumentWriter[UUID, BsonValue] {
            override def write (value : UUID) : BsonValue =
                BsonObjectId (value.toString ())
        }
}


trait CriteriaWriterImplicits
    extends ValueWriterImplicits
{
    /// Implicit Conversions
    implicit def allWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[A, BsonValue]
    )
    : DocumentWriter[All[BsonDocument, A, B], BsonDocument] =
    {
        new DocumentWriter[All[BsonDocument, A, B], BsonDocument] {
            override def write (tree : All[BsonDocument, A, B])
            : BsonDocument =
                BsonDocument (
                    tree.field._property$path ->
                    BsonDocument (
                        tree.operand.name ->
                            BsonArray.fromIterable (
                                tree.values
                                    .map (valueWriter.write(_))
                                    .toVector
                            )
                        )
                    )
        }
    }


    implicit def andWriter
    : DocumentWriter[And[BsonDocument], BsonDocument] =
    {
        new DocumentWriter[And[BsonDocument], BsonDocument] {
            override def write (tree : And[BsonDocument]) : BsonDocument =
                BsonDocument (
                    tree.operand.name ->
                    BsonArray.fromIterable (
                        tree.expressions
                            .map (_.toDocument ())
                            .toNonEmptyVector
                            .toVector
                        )
                    )
        }
    }


    implicit def equalToWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[B, BsonValue]
    )
    : DocumentWriter[EqualTo[BsonDocument, A, B], BsonDocument] =
    {
        new DocumentWriter[EqualTo[BsonDocument, A, B], BsonDocument] {
            override def write (tree : EqualTo[BsonDocument, A, B]) : BsonDocument =
                BsonDocument (
                    tree.field._property$path ->
                    BsonDocument (
                        tree.operand.name ->
                        valueWriter.write (tree.value.instance)
                        )
                    )
        }
    }


    implicit def existsWriter[A]
    : DocumentWriter[Exists[BsonDocument, A], BsonDocument] =
    {
        new DocumentWriter[Exists[BsonDocument, A], BsonDocument] {
            override def write (tree : Exists[BsonDocument, A])
            : BsonDocument =
                BsonDocument (
                    tree.field._property$path ->
                    BsonDocument (
                        tree.operand.name ->
                        booleanWriter.write (tree.value)
                        )
                    )
        }
    }


    implicit def greaterThanWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[B, BsonValue]
        )
    : DocumentWriter[GreaterThan[BsonDocument, A, B], BsonDocument] =
    {
        new DocumentWriter[GreaterThan[BsonDocument, A, B], BsonDocument] {
            override def write (tree : GreaterThan[BsonDocument, A, B])
            : BsonDocument =
                BsonDocument (
                    tree.field._property$path ->
                    BsonDocument (
                        tree.operand.name ->
                            valueWriter.write (tree.value.instance)
                        )
                    )
        }
    }


    implicit def greaterThanOrEqualWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[B, BsonValue]
        )
    : DocumentWriter[GreaterThanOrEqual[BsonDocument, A, B], BsonDocument] =
    {
        new DocumentWriter[GreaterThanOrEqual[BsonDocument, A, B], BsonDocument] {
            override def write (tree : GreaterThanOrEqual[BsonDocument, A, B])
            : BsonDocument =
                BsonDocument (
                    tree.field._property$path ->
                    BsonDocument (
                        tree.operand.name ->
                        valueWriter.write (tree.value.instance)
                        )
                    )
            }
    }


    implicit def inWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[B, BsonValue]
        )
    : DocumentWriter[In[BsonDocument, A, B], BsonDocument] =
    {
        new DocumentWriter[In[BsonDocument, A, B], BsonDocument] {
            override def write (tree : In[BsonDocument, A, B]) : BsonDocument =
                BsonDocument (
                    tree.field._property$path ->
                    BsonDocument (
                        tree.operand.name ->
                        BsonArray.fromIterable (
                            tree.values
                                .map (v => valueWriter.write (v))
                                .toVector
                        )
                    )
                )
        }
    }


    implicit def lessThanWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[B, BsonValue]
        )
    : DocumentWriter[LessThan[BsonDocument, A, B], BsonDocument] =
    {
        new DocumentWriter[LessThan[BsonDocument, A, B], BsonDocument] {
            override def write (tree : LessThan[BsonDocument, A, B])
            : BsonDocument =
                BsonDocument (
                    tree.field._property$path ->
                    BsonDocument (
                        tree.operand.name ->
                        valueWriter.write (tree.value.instance)
                        )
                    )
        }
    }


    implicit def lessThanOrEqualWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[B, BsonValue]
        )
    : DocumentWriter[LessThanOrEqual[BsonDocument, A, B], BsonDocument] =
    {
        new DocumentWriter[LessThanOrEqual[BsonDocument, A, B], BsonDocument] {
            override def write (tree : LessThanOrEqual[BsonDocument, A, B])
            : BsonDocument =
                BsonDocument (
                    tree.field._property$path ->
                    BsonDocument (
                        tree.operand.name ->
                        valueWriter.write (tree.value.instance)
                        )
                    )
        }
    }


    implicit def norWriter[A <: BsonDocument]
    : DocumentWriter[Nor[A], BsonDocument] =
    {
        new DocumentWriter[Nor[A], BsonDocument] {
            override def write (tree : Nor[A]) : BsonDocument =
                BsonDocument (
                    tree.operand.name ->
                    BsonArray.fromIterable (
                        tree.expressions
                            .map (_.toDocument ())
                            .toNonEmptyVector
                            .toVector
                        )
                    )
        }
    }


    implicit def notWriter
    : DocumentWriter[Not[BsonDocument], BsonDocument] =
    {
        new DocumentWriter[Not[BsonDocument], BsonDocument] {
            override def write (tree : Not[BsonDocument]) : BsonDocument =
                BsonDocument (
                    tree.operand.name -> tree.expression.toDocument ()
                )
        }
    }


    implicit def notEqualToWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[B, BsonValue]
        )
    : DocumentWriter[NotEqualTo[BsonDocument, A, B], BsonDocument] =
    {
        new DocumentWriter[NotEqualTo[BsonDocument, A, B], BsonDocument] {
            override def write (tree : NotEqualTo[BsonDocument, A, B]) : BsonDocument =
                BsonDocument (
                    tree.field._property$path ->
                    BsonDocument (
                        tree.operand.name ->
                        valueWriter.write (tree.value.instance)
                        )
                    )
        }
    }


    implicit def notInWriter[A, B <: A] (
        implicit valueWriter : DocumentWriter[B, BsonValue]
        )
    : DocumentWriter[NotIn[BsonDocument, A, B], BsonDocument] =
    {
        new DocumentWriter[NotIn[BsonDocument, A, B], BsonDocument] {
            override def write (tree : NotIn[BsonDocument, A, B]) : BsonDocument =
                BsonDocument (
                    tree.field._property$path ->
                    BsonDocument (
                        tree.operand.name ->
                        BsonArray.fromIterable (
                            tree.values
                                .map (v => valueWriter.write (v))
                                .toVector
                            )
                        )
                    )
        }
    }


    implicit def orWriter
    : DocumentWriter[Or[BsonDocument], BsonDocument] =
    {
        new DocumentWriter[Or[BsonDocument], BsonDocument] {
            override def write (tree : Or[BsonDocument]) : BsonDocument =
                BsonDocument (
                    tree.operand.name ->
                    BsonArray.fromIterable (
                        tree.expressions
                            .map (_.toDocument ())
                            .toNonEmptyVector
                            .toVector
                    )
                )
        }
    }


    implicit def regularExpressionWriter[A >: String]
    : DocumentWriter[RegularExpression[BsonDocument, A], BsonDocument] =
    {
        new DocumentWriter[RegularExpression[BsonDocument, A], BsonDocument] {
            override def write (tree : RegularExpression[BsonDocument, A])
            : BsonDocument =
                BsonDocument (
                    tree.field._property$path ->
                    tree.modifier
                        .map (_.value)
                        .fold (BsonRegularExpression (tree.value.instance)) (
                            BsonRegularExpression (tree.value.instance, _)
                            )
                )
        }
    }
}


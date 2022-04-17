package com.github.osxhacker.query.criteria.expression

import scala.language.implicitConversions
import cats.data.{
    Chain,
    NonEmptyChain
    }

import com.github.osxhacker.query.DocumentWriter
import com.github.osxhacker.query.criteria.{
    Field,
    Value
    }

import com.github.osxhacker.query.model.Operator


/**
 * The '''Comparison''' trait is the common ancestor for __all__ abstract syntax
 * tree comparison operators supported.
 */
sealed trait Comparison[DocumentT <: AnyRef]
{
    /// Self Type Constraints
    this : Expression[DocumentT] =>
}


/**
 * The '''BetweenPolicy''' trait defines how the start and end values
 * are used when interpreting a
 * [[com.github.osxhacker.query.criteria.expression.Between]] instance.
 */
sealed trait BetweenPolicy
{
    def apply[DocumentT <: AnyRef, T, U <: T] (
        field : Field[T],
        start : Value[U],
        end : Value[U]
        )
        (
            implicit
            writer : DocumentWriter[And[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Not[DocumentT], DocumentT],
            gtWriter : DocumentWriter[GreaterThan[DocumentT, T, U], DocumentT],
            gteWriter : DocumentWriter[
                GreaterThanOrEqual[DocumentT, T, U],
                DocumentT
                ],

            ltWriter : DocumentWriter[LessThan[DocumentT, T, U], DocumentT],
            lteWriter : DocumentWriter[
                LessThanOrEqual[DocumentT, T, U],
                DocumentT
                ]
        )
    : Expression[DocumentT]
}


/**
 * The '''Closed''' type specifies the
 * [[com.github.osxhacker.query.criteria.expression.BetweenPolicy]] for
 * including the start and end
 * [[com.github.osxhacker.query.criteria.Value]]s used in a
 * [[com.github.osxhacker.query.criteria.expression.Between]] instance.
 */
sealed trait Closed
    extends BetweenPolicy
{
    def apply[DocumentT <: AnyRef, T, U <: T] (
        field : Field[T],
        start : Value[U],
        end : Value[U]
        )
        (
            implicit
            writer : DocumentWriter[And[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Not[DocumentT], DocumentT],
            gtWriter : DocumentWriter[GreaterThan[DocumentT, T, U], DocumentT],
            gteWriter : DocumentWriter[
                GreaterThanOrEqual[DocumentT, T, U],
                DocumentT
                ],

            ltWriter : DocumentWriter[LessThan[DocumentT, T, U], DocumentT],
            lteWriter : DocumentWriter[
                LessThanOrEqual[DocumentT, T, U],
                DocumentT
                ]
        )
    : Expression[DocumentT] =
        And (
            NonEmptyChain (
                GreaterThanOrEqual (field, start),
                LessThanOrEqual (field, end)
            )
        )
}


/**
 * The '''HalfOpen''' type specifies the
 * [[com.github.osxhacker.query.criteria.expression.BetweenPolicy]] for
 * including the start and excluding the end
 * [[com.github.osxhacker.query.criteria.Value]]s used in a
 * [[com.github.osxhacker.query.criteria.expression.Between]] instance.
 */
sealed trait HalfOpen
    extends  BetweenPolicy
{
    def apply[DocumentT <: AnyRef, T, U <: T] (
        field : Field[T],
        start : Value[U],
        end : Value[U]
        )
        (
            implicit
            writer : DocumentWriter[And[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Not[DocumentT], DocumentT],
            gtWriter : DocumentWriter[GreaterThan[DocumentT, T, U], DocumentT],
            gteWriter : DocumentWriter[
                GreaterThanOrEqual[DocumentT, T, U],
                DocumentT
                ],

            ltWriter : DocumentWriter[LessThan[DocumentT, T, U], DocumentT],
            lteWriter : DocumentWriter[
                LessThanOrEqual[DocumentT, T, U],
                DocumentT
                ]
        )
    : Expression[DocumentT] =
        And (
            NonEmptyChain (
                GreaterThanOrEqual (field, start),
                LessThan (field, end)
            )
        )
}


/**
 * The '''Open''' type specifies the
 * [[com.github.osxhacker.query.criteria.expression.BetweenPolicy]] for
 * excluding the start and end
 * [[com.github.osxhacker.query.criteria.Value]]s used in a
 * [[com.github.osxhacker.query.criteria.expression.Between]] instance.
 */
sealed trait Open
    extends  BetweenPolicy
{
    def apply[DocumentT <: AnyRef, T, U <: T] (
        field : Field[T],
        start : Value[U],
        end : Value[U]
        )
        (
            implicit
            writer : DocumentWriter[And[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Not[DocumentT], DocumentT],
            gtWriter : DocumentWriter[GreaterThan[DocumentT, T, U], DocumentT],
            gteWriter : DocumentWriter[
                GreaterThanOrEqual[DocumentT, T, U],
                DocumentT
                ],

            ltWriter : DocumentWriter[LessThan[DocumentT, T, U], DocumentT],
            lteWriter : DocumentWriter[
                LessThanOrEqual[DocumentT, T, U],
                DocumentT
                ]
        )
    : Expression[DocumentT] =
        And (
            NonEmptyChain (
                GreaterThan (field, start),
                LessThan (field, end)
            )
        )
}


object BetweenPolicy
{
    /// Class Types
    implicit case object Closed
        extends BetweenPolicy
            with Closed


    implicit case object HalfOpen
        extends BetweenPolicy
            with HalfOpen


    implicit case object Open
        extends BetweenPolicy
            with Open
}


final case class Between[
    DocumentT <: AnyRef,
    BetweenPolicyT <: BetweenPolicy,
    T,
    U <: T
    ] (
        val policy : BetweenPolicyT,
        val field : Field[T],
        val start : Value[U],
        val end : Value[U]
    )
    (
        implicit
        private val writer : DocumentWriter[And[DocumentT], DocumentT],
        private val negationWriter : DocumentWriter[Not[DocumentT], DocumentT],
        private val gtWriter : DocumentWriter[
            GreaterThan[DocumentT, T, U],
            DocumentT
            ],

        private val gteWriter : DocumentWriter[
            GreaterThanOrEqual[DocumentT, T, U],
            DocumentT
            ],

        private val ltWriter : DocumentWriter[
            LessThan[DocumentT, T, U],
            DocumentT
            ],

        private val lteWriter : DocumentWriter[
            LessThanOrEqual[DocumentT, T, U],
            DocumentT
            ]
    )
    extends NaryExpression[DocumentT, T]
        with Comparison[DocumentT]
{
    /// Instance Properties
    override val operand : Operator = Operator.And


    override def negate () : Not[DocumentT] = Not (this)


    override def toDocument () : DocumentT =
        policy (field, start, end).toDocument ()
}


final case class EqualTo[DocumentT <: AnyRef, T, U <: T] (
    val field : Field[T],
    val value : Value[U]
    )
    (
        implicit
        private val writer : DocumentWriter[
            EqualTo[DocumentT, T, U],
            DocumentT
            ],

        private val negationWriter : DocumentWriter[
            NotEqualTo[DocumentT, T, U],
            DocumentT
            ]
    )
    extends BinaryExpression[DocumentT, T, U]
        with Comparison[DocumentT]
{
    /// Instance Properties
    override val operand : Operator = Operator.Equal


    override def negate () : NotEqualTo[DocumentT, T, U] =
        NotEqualTo (field, value)


    override def toDocument () : DocumentT = writer.write (this)
}


final case class GreaterThan[DocumentT <: AnyRef, T, U <: T] (
    val field : Field[T],
    val value : Value[U]
    )
    (
        implicit
        private val writer : DocumentWriter[
            GreaterThan[DocumentT, T, U],
            DocumentT
            ],

        private val negationWriter : DocumentWriter[
            LessThanOrEqual[DocumentT, T, U],
            DocumentT
            ]
    )
    extends BinaryExpression[DocumentT, T, U]
        with Comparison[DocumentT]
{
    /// Instance Properties
    override val operand : Operator = Operator.GreaterThan


    override def negate () : LessThanOrEqual[DocumentT, T, U] =
        LessThanOrEqual (field, value)


    override def toDocument () : DocumentT = writer.write (this)
}


final case class GreaterThanOrEqual[DocumentT <: AnyRef, T, U <: T] (
    val field : Field[T],
    val value : Value[U]
    )
    (
        implicit
        private val writer : DocumentWriter[
            GreaterThanOrEqual[DocumentT, T, U],
            DocumentT
            ],

        private val negationWriter : DocumentWriter[
            LessThan[DocumentT, T, U],
            DocumentT
            ]
    )
    extends BinaryExpression[DocumentT, T, U]
        with Comparison[DocumentT]
{
    /// Instance Properties
    override val operand : Operator = Operator.GreaterThanOrEqual


    override def negate () : LessThan[DocumentT, T, U] =
        LessThan (field, value)


    override def toDocument () : DocumentT = writer.write (this)
}


final case class In[DocumentT <: AnyRef, T, U <: T] (
    val field : Field[T],
    val values : Chain[U]
    )
    (
        implicit
        private val writer : DocumentWriter[In[DocumentT, T, U], DocumentT],
        private val negationWriter : DocumentWriter[
            NotIn[DocumentT, T, U],
            DocumentT
            ]
    )
    extends BinaryExpression[DocumentT, T, U]
        with Comparison[DocumentT]
{
    /// Instance Properties
    override val operand : Operator = Operator.In


    override def negate () : NotIn[DocumentT, T, U] = NotIn (field, values)


    override def toDocument () : DocumentT = writer.write (this)
}


final case class LessThan[DocumentT <: AnyRef, T, U <: T] (
    val field : Field[T],
    val value : Value[U]
    )
    (
        implicit
        private val writer : DocumentWriter[
            LessThan[DocumentT, T, U],
            DocumentT
            ],

        private val negationWriter : DocumentWriter[
            GreaterThanOrEqual[DocumentT, T, U],
            DocumentT
            ]
    )
    extends BinaryExpression[DocumentT, T, U]
        with Comparison[DocumentT]
{
    /// Instance Properties
    override val operand : Operator = Operator.LessThan


    override def negate () : GreaterThanOrEqual[DocumentT, T, U] =
        GreaterThanOrEqual (field, value)


    override def toDocument () : DocumentT = writer.write (this)
}


final case class LessThanOrEqual[DocumentT <: AnyRef, T, U <: T] (
    val field : Field[T],
    val value : Value[U]
    )
    (
        implicit
        private val writer : DocumentWriter[
            LessThanOrEqual[DocumentT, T, U],
            DocumentT
            ],

        private val negationWriter : DocumentWriter[
            GreaterThan[DocumentT, T, U],
            DocumentT
            ]
    )
    extends BinaryExpression[DocumentT, T, U]
        with Comparison[DocumentT]
{
    /// Instance Properties
    override val operand : Operator = Operator.LessThanOrEqual


    override def negate () : GreaterThan[DocumentT, T, U] =
        GreaterThan (field, value)


    override def toDocument () : DocumentT = writer.write (this)
}


final case class NotEqualTo[DocumentT <: AnyRef, T, U <: T] (
    val field : Field[T],
    val value : Value[U]
    )
    (
        implicit
        private val writer : DocumentWriter[
            NotEqualTo[DocumentT, T, U],
            DocumentT
            ],

        private val negationWriter : DocumentWriter[
            EqualTo[DocumentT, T, U],
            DocumentT
            ]
    )
    extends BinaryExpression[DocumentT, T, U]
        with Comparison[DocumentT]
{
    /// Instance Properties
    override val operand : Operator = Operator.NotEqual


    override def negate () : EqualTo[DocumentT, T, U] = EqualTo (field, value)


    override def toDocument () : DocumentT = writer.write (this)
}


final case class NotIn[DocumentT <: AnyRef, T, U <: T] (
    val field : Field[T],
    val values : Chain[U]
    )
    (
        implicit
        private val writer : DocumentWriter[NotIn[DocumentT, T, U], DocumentT],
        private val negationWriter : DocumentWriter[
            In[DocumentT, T, U],
            DocumentT
            ]
    )
    extends BinaryExpression[DocumentT, T, U]
        with Comparison[DocumentT]
{
    /// Instance Properties
    override val operand : Operator = Operator.NotIn


    override def negate () : In[DocumentT, T, U] = In (field, values)


    override def toDocument () : DocumentT = writer.write (this)
}


final class BetweenOps[BetweenPolicyT <: BetweenPolicy, T] (
    private val self : Field[T]
    )
{
    def apply[DocumentT <: AnyRef, U <: T] (start : Value[U], end : Value[U])
        (
            implicit
            policy : BetweenPolicyT,
            writer : DocumentWriter[And[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Not[DocumentT], DocumentT],
            gtWriter : DocumentWriter[GreaterThan[DocumentT, T, U], DocumentT],
            gteWriter : DocumentWriter[
                GreaterThanOrEqual[DocumentT, T, U],
                DocumentT
                ],

            ltWriter : DocumentWriter[LessThan[DocumentT, T, U], DocumentT],
            lteWriter : DocumentWriter[
                LessThanOrEqual[DocumentT, T, U],
                DocumentT
                ]
        )
    : Between[DocumentT, BetweenPolicyT, T, U] =
        Between[DocumentT, BetweenPolicyT, T, U] (policy, self, start, end)
}


final class ComparisonOps[T] (private val self : Field[T])
    extends AnyVal
{
    /**
     * Logical equality: '''$eq'''.
     */
    def ===[DocumentT <: AnyRef, U <: T] (value : Value[U])
        (
            implicit
            writer : DocumentWriter[EqualTo[DocumentT, T, U], DocumentT],
            negationWriter : DocumentWriter[
                NotEqualTo[DocumentT, T, U],
                DocumentT
                ]
        )
    : EqualTo[DocumentT, T, U] =
        EqualTo (self, value)


    /**
     * Logical equality: '''$eq'''.
     */
    def @==[DocumentT <: AnyRef, U <: T] (value : Value[U])
        (
            implicit
            writer : DocumentWriter[EqualTo[DocumentT, T, U], DocumentT],
            negationWriter : DocumentWriter[
                NotEqualTo[DocumentT, T, U],
                DocumentT
                ]
        )
    : EqualTo[DocumentT, T, U] =
        EqualTo (self, value)


    /**
     * Logical inequality: '''$ne'''.
     */
    def <>[DocumentT <: AnyRef, U <: T] (value : Value[U])
        (
            implicit
            writer : DocumentWriter[NotEqualTo[DocumentT, T, U], DocumentT],
            negationWriter : DocumentWriter[EqualTo[DocumentT, T, U], DocumentT]
        )
    : NotEqualTo[DocumentT, T, U] =
        NotEqualTo (self, value)


    /**
     * Logical inequality: '''$ne'''.
     */
    def =/=[DocumentT <: AnyRef, U <: T] (value : Value[U])
        (
            implicit
            writer : DocumentWriter[NotEqualTo[DocumentT, T, U], DocumentT],
            negationWriter : DocumentWriter[EqualTo[DocumentT, T, U], DocumentT]
        )
    : NotEqualTo[DocumentT, T, U] =
        NotEqualTo (self, value)


    /**
     * Logical inequality: '''$ne'''.
     */
    def !==[DocumentT <: AnyRef, U <: T] (value : Value[U])
        (
            implicit
            writer : DocumentWriter[NotEqualTo[DocumentT, T, U], DocumentT],
            negationWriter : DocumentWriter[EqualTo[DocumentT, T, U], DocumentT]
        )
    : NotEqualTo[DocumentT, T, U] =
        NotEqualTo (self, value)


    /**
     * Less-than comparison: '''$lt'''.
     */
    def <[DocumentT <: AnyRef, U <: T] (value : Value[U])
        (
            implicit
            writer : DocumentWriter[LessThan[DocumentT, T, U], DocumentT],
            negationWriter : DocumentWriter[
                GreaterThanOrEqual[DocumentT, T, U],
                DocumentT
                ]
        )
    : LessThan[DocumentT, T, U] =
        LessThan (self, value)


    /**
     * Less-than or equal comparison: '''$lte'''.
     */
    def <=[DocumentT <: AnyRef, U <: T] (value : Value[U])
        (
            implicit
            writer : DocumentWriter[
                LessThanOrEqual[DocumentT, T, U],
                DocumentT
                ],

            negationWriter : DocumentWriter[
                GreaterThan[DocumentT, T, U],
                DocumentT
                ]
        )
    : LessThanOrEqual[DocumentT, T, U] =
        LessThanOrEqual (self, value)


    /**
     * Greater-than comparison: '''$gt'''.
     */
    def >[DocumentT <: AnyRef, U <: T] (value : Value[U])
        (
            implicit
            writer : DocumentWriter[GreaterThan[DocumentT, T, U], DocumentT],
            negationWriter : DocumentWriter[
                LessThanOrEqual[DocumentT, T, U],
                DocumentT
                ]
        )
    : GreaterThan[DocumentT, T, U] =
        GreaterThan (self, value)


    /**
     * Greater-than or equal comparison: '''$gte'''.
     */
    def >=[DocumentT <: AnyRef, U <: T] (value : Value[U])
        (
            implicit
            writer : DocumentWriter[
                GreaterThanOrEqual[DocumentT, T, U],
                DocumentT
                ],

            negationWriter : DocumentWriter[
                LessThan[DocumentT, T, U],
                DocumentT
                ]
        )
    : GreaterThanOrEqual[DocumentT, T, U] =
        GreaterThanOrEqual (self, value)


    /**
     * Field is between '''start''' and '''end''', as determined by the
     * ''BetweenPolicyT''.
     */
    def between[BetweenPolicyT <: BetweenPolicy]
    : BetweenOps[BetweenPolicyT, T] =
        new BetweenOps[BetweenPolicyT, T] (self)


    /**
     * Field value equals one of the '''values''': '''$in'''.
     */
    def in[DocumentT <: AnyRef, U <: T] (values : Iterable[U])
        (
            implicit
            writer : DocumentWriter[In[DocumentT, T, U], DocumentT],
            negationWriter : DocumentWriter[NotIn[DocumentT, T, U], DocumentT]
        )
    : In[DocumentT, T, U] =
        In (self, Chain.fromSeq (values.toVector))


    /**
     * Field value equals either '''head''' or one of the (optional)
     * '''tail''' values: '''$in'''.
     */
    def in[DocumentT <: AnyRef, U <: T] (value : U, additional : U*)
        (
            implicit
            writer : DocumentWriter[In[DocumentT, T, U], DocumentT],
            negationWriter : DocumentWriter[NotIn[DocumentT, T, U], DocumentT]
        )
    : In[DocumentT, T, U] =
        In (self, Chain.one (value) ++ Chain.fromSeq (additional))
}


trait ComparisonSyntax
{
    /// Implicit Conversions
    implicit def toComparisonOps[T] (field : Field[T]) : ComparisonOps[T] =
        new ComparisonOps[T] (field)
}


package com.github.osxhacker.query.criteria.expression

import scala.language.implicitConversions
import cats.Semigroup
import cats.data.NonEmptyChain
import com.github.osxhacker.query.DocumentWriter
import com.github.osxhacker.query.model.Operator


/**
 * The '''Logical''' trait is the common ancestor for __all__ abstract syntax
 * tree logical operators supported.
 */
sealed trait Logical[DocumentT <: AnyRef]
{
    /// Self Type Constraints
    this : Expression[DocumentT] =>
}


final case class And[DocumentT <: AnyRef] (
    val expressions : NonEmptyChain[Expression[DocumentT]]
    )
    (
        implicit
        private val writer : DocumentWriter[And[DocumentT], DocumentT],
        private val negationWriter : DocumentWriter[Not[DocumentT], DocumentT]
    )
    extends NaryExpression[DocumentT, Boolean]
        with Logical[DocumentT]
{
    /// Class Imports
    import cats.syntax.semigroup._


    /// Instance Properties
    override val operand : Operator = Operator.And


    override def negate () : Not[DocumentT] = Not (this)


    override def toDocument () : DocumentT = writer.write (this)


    /**
     * Conjunction: ''AND''.
     */
    def &&[A] (rhs : A)
        (implicit ev : A <:< And[DocumentT])
    : And[DocumentT] =
        this |+| ev (rhs)


    /**
     * Conjunction: ''AND''.
     */
    def && (rhs : Expression[DocumentT]) : And[DocumentT] =
        And (expressions append rhs)
}


object And
{
    /// Class Imports
    import cats.syntax.semigroup._


    /// Implicit Conversions
    implicit def semigroup[DocumentT <: AnyRef] (
        implicit
        writer : DocumentWriter[And[DocumentT], DocumentT],
        negationWriter : DocumentWriter[Not[DocumentT], DocumentT]
    ) : Semigroup[And[DocumentT]] =
        new Semigroup[And[DocumentT]] {
            override def combine (x : And[DocumentT], y : And[DocumentT])
            : And[DocumentT] =
                And (x.expressions |+| y.expressions)
        }
}


final case class Nor[DocumentT <: AnyRef] (
    val expressions : NonEmptyChain[Expression[DocumentT]]
    )
    (
        implicit
        private val writer : DocumentWriter[Nor[DocumentT], DocumentT],
        private val negationWriter : DocumentWriter[Or[DocumentT], DocumentT]
    )
    extends NaryExpression[DocumentT, Boolean]
        with Logical[DocumentT]
{
    /// Class Imports
    import cats.syntax.semigroup._


    /// Instance Properties
    override val operand : Operator = Operator.Nor


    override def negate () : Or[DocumentT] = Or (expressions)


    override def toDocument () : DocumentT = writer.write (this)


    /**
     * Negation of disjunction: ''NOR''.
     */
    def !||[A] (rhs : A)
        (implicit ev : A <:< Nor[DocumentT])
    : Nor[DocumentT] =
        this |+| ev (rhs)


    /**
     * Negation of disjunction: ''NOR''.
     */
    def !||[A] (rhs : Expression[DocumentT]) : Nor[DocumentT] =
        Nor (expressions.append (rhs))
}


object Nor
{
    /// Class Imports
    import cats.syntax.semigroup._


    /// Implicit Conversions
    implicit def semigroup[DocumentT <: AnyRef] (
        implicit
        writer : DocumentWriter[Nor[DocumentT], DocumentT],
        orWriter : DocumentWriter[Or[DocumentT], DocumentT]
        )
    : Semigroup[Nor[DocumentT]] =
        new Semigroup[Nor[DocumentT]] {
            override def combine (x : Nor[DocumentT], y : Nor[DocumentT])
            : Nor[DocumentT] =
                Nor (x.expressions |+| y.expressions)
        }
}


final case class Not[DocumentT <: AnyRef] (
    val expression : Expression[DocumentT]
    )
    (implicit private val writer : DocumentWriter[Not[DocumentT], DocumentT])
    extends UnaryExpression[DocumentT, Boolean]
        with Logical[DocumentT]
{
    /// Instance Properties
    override val operand : Operator = Operator.Not


    override def negate () : Expression[DocumentT] = expression


    override def toDocument () : DocumentT = writer.write (this)
}


final case class Or[DocumentT <: AnyRef] (
    val expressions : NonEmptyChain[Expression[DocumentT]]
    )
    (
        implicit
        private val writer : DocumentWriter[Or[DocumentT], DocumentT],
        private val negationWriter : DocumentWriter[Nor[DocumentT], DocumentT]
    )
    extends NaryExpression[DocumentT, Boolean]
        with Logical[DocumentT]
{
    /// Class Imports
    import cats.syntax.semigroup._


    /// Instance Properties
    override val operand : Operator = Operator.Or


    override def negate () : Nor[DocumentT] = Nor (expressions)


    override def toDocument () : DocumentT = writer.write (this)


    /**
     * Disjunction: ''OR''.
     */
    def ||[A] (rhs : A)
        (implicit ev : A <:< Or[DocumentT])
    : Or[DocumentT] =
        this |+| ev (rhs)


    /**
     * Disjunction: ''OR''.
     */
    def || (rhs : Expression[DocumentT]) : Or[DocumentT] =
        Or (expressions append rhs)
}


object Or
{
    /// Class Imports
    import cats.syntax.semigroup._


    /// Implicit Conversions
    implicit def semigroup[DocumentT <: AnyRef] (
        implicit
        writer : DocumentWriter[Or[DocumentT], DocumentT],
        norWriter : DocumentWriter[Nor[DocumentT], DocumentT]
        )
    : Semigroup[Or[DocumentT]] =
        new Semigroup[Or[DocumentT]] {
            override def combine (x : Or[DocumentT], y : Or[DocumentT])
            : Or[DocumentT] =
                Or (x.expressions |+| y.expressions)
        }
}


sealed trait LowerPriorityLogicalOps[DocumentT <: AnyRef]
{
    /// Instance Properties
    protected val self : Expression[DocumentT]


    /**
     * Negation.
     */
    def unary_! : Expression[DocumentT] = self.negate ()


    /**
     * Conjunction: ''AND''.
     */
    def && (rhs : Expression[DocumentT])
        (
            implicit
            writer : DocumentWriter[And[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Not[DocumentT], DocumentT]
        )
    : And[DocumentT] =
        And (NonEmptyChain (self, rhs))


    /**
     * Conjunction: ''AND''.
     */
    def && (rhs : Option[Expression[DocumentT]])
        (
            implicit
            writer : DocumentWriter[And[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Not[DocumentT], DocumentT]
        )
    : Expression[DocumentT] =
        rhs.fold (self) {
            expression =>
                And (NonEmptyChain (self, expression))
        }


    /**
     * Negation of disjunction: ''NOR''.
     */
    def !|| (rhs : Expression[DocumentT])
        (
            implicit
            writer : DocumentWriter[Nor[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Or[DocumentT], DocumentT]
        )
    : Nor[DocumentT] =
        Nor (NonEmptyChain (self, rhs))


    /**
     * Negation of disjunction: ''NOR''.
     */
    def !|| (rhs : Option[Expression[DocumentT]])
        (
            implicit
            writer : DocumentWriter[Nor[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Or[DocumentT], DocumentT]
        )
    : Expression[DocumentT] =
        rhs.fold (self) {
            expression =>
                Nor (NonEmptyChain (self, expression))
        }


    /**
     * Disjunction: ''OR''.
     */
    def || (rhs : Expression[DocumentT])
        (
            implicit
            writer : DocumentWriter[Or[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Nor[DocumentT], DocumentT]
        )
    : Or[DocumentT] =
        Or (NonEmptyChain (self, rhs))


    /**
     * Disjunction: ''OR''.
     */
    def || (rhs : Option[Expression[DocumentT]])
        (
            implicit
            writer : DocumentWriter[Or[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Nor[DocumentT], DocumentT]
        )
    : Expression[DocumentT] =
        rhs.fold (self) {
            expression =>
                Or (NonEmptyChain (self, expression))
        }
}


final class LogicalOps[DocumentT <: AnyRef] (
    override protected val self : Expression[DocumentT]
    )
    extends LowerPriorityLogicalOps[DocumentT]
{
    /**
     * Conjunction: ''AND''.
     */
    def && (rhs : And[DocumentT])
        (
            implicit
            writer : DocumentWriter[And[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Not[DocumentT], DocumentT]
        )
    : And[DocumentT] =
        And (self +: rhs.expressions)


    /**
     * Negation of disjunction: ''NOR''.
     */
    def !|| (rhs : Nor[DocumentT])
        (
            implicit
            writer : DocumentWriter[Nor[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Or[DocumentT], DocumentT]
        )
    : Nor[DocumentT] =
        Nor (self +: rhs.expressions)


    /**
     * Disjunction: ''OR''.
     */
    def || (rhs : Or[DocumentT])
        (
            implicit
            writer : DocumentWriter[Or[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Nor[DocumentT], DocumentT]
        )
    : Or[DocumentT] =
        Or (self +: rhs.expressions)
}


final class OptionalLogicalOps[DocumentT <: AnyRef] (
    private val self : Option[Expression[DocumentT]]
    )
    extends AnyVal
{
    /**
     * Negation.
     */
    def unary_! : Option[Expression[DocumentT]] = self.map (_.negate ())


    /**
     * Conjunction: ''AND''.
     */
    def && (rhs : Expression[DocumentT])
        (
            implicit
            writer : DocumentWriter[And[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Not[DocumentT], DocumentT]
        )
    : Expression[DocumentT] =
        self.fold (rhs) {
            lhs =>
                And (NonEmptyChain (lhs, rhs))
            }


    /**
     * Conjunction: ''AND''.
     */
    def && (expression : Option[Expression[DocumentT]])
        (
            implicit
            writer : DocumentWriter[And[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Not[DocumentT], DocumentT]
        )
    : Option[Expression[DocumentT]] =
        (self, expression) match {
            case (Some (lhs), Some (rhs)) =>
                Some (And (NonEmptyChain (lhs, rhs)))

            case (None, rhs) =>
                rhs

            case _ =>
                self
            }


    /**
     * Negation of disjunction: ''NOR''.
     */
    def !|| (rhs : Expression[DocumentT])
        (
            implicit
            writer : DocumentWriter[Nor[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Or[DocumentT], DocumentT]
        )
    : Expression[DocumentT] =
        self.fold (rhs) {
            lhs =>
                Nor (NonEmptyChain (lhs, rhs))
            }


    /**
     * Negation of disjunction: ''NOR''.
     */
    def !|| (expression : Option[Expression[DocumentT]])
        (
            implicit
            writer : DocumentWriter[Nor[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Or[DocumentT], DocumentT]
        )
    : Option[Expression[DocumentT]] =
        (self, expression) match {
            case (Some (lhs), Some (rhs)) =>
                Some (Nor (NonEmptyChain (lhs, rhs)))

            case (None, rhs) =>
                rhs

            case _ =>
                self
            }


    /**
     * Disjunction: ''OR''.
     */
    def || (rhs : Expression[DocumentT])
        (
            implicit
            writer : DocumentWriter[Or[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Nor[DocumentT], DocumentT]
        )
    : Expression[DocumentT] =
        self.fold (rhs) {
            lhs =>
                Or (NonEmptyChain (lhs, rhs))
            }


    /**
     * Disjunction: ''OR''.
     */
    def || (expression : Option[Expression[DocumentT]])
        (
            implicit
            writer : DocumentWriter[Or[DocumentT], DocumentT],
            negationWriter : DocumentWriter[Nor[DocumentT], DocumentT]
        )
    : Option[Expression[DocumentT]] =
        (self, expression) match {
            case (Some (lhs), Some (rhs)) =>
                Some (Or (NonEmptyChain (lhs, rhs)))

            case (None, rhs) =>
                rhs

            case _ =>
                self
            }
}


trait LogicalSyntax
{
    /// Implicit Conversions
    implicit def toLogicalOps[DocumentT <: AnyRef] (
        expression : Expression[DocumentT]
        ) : LogicalOps[DocumentT] =
        new LogicalOps (expression)


    implicit def toOptionalLogicalOps[DocumentT <: AnyRef] (
        expression : Option[Expression[DocumentT]]
        )
    : OptionalLogicalOps[DocumentT] =
        new OptionalLogicalOps (expression)
}


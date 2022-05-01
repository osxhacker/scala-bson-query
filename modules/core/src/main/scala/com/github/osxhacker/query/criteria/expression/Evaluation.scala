package com.github.osxhacker.query.criteria.expression

import scala.language.implicitConversions
import scala.util.matching

import cats.Semigroup
import com.github.osxhacker.query.DocumentWriter
import com.github.osxhacker.query.criteria.{
    Field,
    Value
    }

import com.github.osxhacker.query.model.Operator

import scala.util.matching.Regex


/**
 * The '''Element''' trait is the common ancestor for __all__ abstract syntax
 * tree element operators supported.
 */
sealed trait Evaluation[DocumentT <: AnyRef]
{
    /// Self Type Constraints
    this : Expression[DocumentT] =>
}


final case class RegularExpression[DocumentT <: AnyRef, LhsT >: String] (
    val field : Field[LhsT],
    val value : Value[String],
    val modifier : Option[RegexModifier] = None
    )
    (
        implicit
        private val writer : DocumentWriter[
            RegularExpression[DocumentT, LhsT],
            DocumentT
            ],

        private val negationWriter : DocumentWriter[Not[DocumentT], DocumentT]
    )
    extends BinaryExpression[DocumentT, LhsT, String]
        with Evaluation[DocumentT]
{
    /// Instance Properties
    override val operand : Operator = Operator.Regex


    override def negate () : Expression[DocumentT] = Not (this)


    override def toDocument () : DocumentT = writer.write (this)
}


/**
 * '''RegexModifier''' types provide the ability for developers to specify
 * `$regex` modifiers using type-checked Scala types.  For example, specifying
 * a `$regex` which ignores case for the `surname` property can be written as:
 *
 * {{{
 *
 * criteria.surname =~ "smith" -> IgnoreCase
 *
 * }}}
 *
 * Multiple modifiers can be combined using the or (`|`) operator,
 * producing an implementation-defined ordering.
 */
sealed trait RegexModifier
{
    /// Class Imports
    import cats.syntax.semigroup._


    /// Instance Properties
    def value : String


    /**
     * Use the or operator to combine two or more '''RegexModifier'''s into
     * one logical value.
     */
    def | (other : RegexModifier) : RegexModifier =
        this |+| other
}


object RegexModifier
{
    /// Class Types
    final case class Combined (override val value : String)
        extends RegexModifier


    /// Implicit Conversions
    implicit val semigroupRegexModifier : Semigroup[RegexModifier] =
        new Semigroup[RegexModifier] {
            override def combine (x : RegexModifier, y : RegexModifier)
            : RegexModifier =
                Combined (x.value + y.value)
        }
}


case object DotMatchesEverything
    extends RegexModifier
{
    override val value : String = "s"
}


case object ExtendedExpressions
    extends RegexModifier
{
    override val value : String = "x"
}


case object IgnoreCase
    extends RegexModifier
{
    override val value : String = "i"
}


case object MultilineMatching
    extends RegexModifier
{
    override val value : String = "m"
}


sealed trait SupportedTypesWitness[T]
{
    def apply (instance : T) : String
}


object SupportedTypesWitness
{
    /// Implicit Conversions
    implicit val stringWitness : SupportedTypesWitness[String] =
        new SupportedTypesWitness[String] {
            override def apply (instance : String) : String = instance
        }

    implicit val regexWitness : SupportedTypesWitness[matching.Regex] =
        new SupportedTypesWitness[Regex] {
            override def apply (instance : Regex) : String =
                instance.toString ()
        }
}


final class EvaluationOps[T >: String] (private val self : Field[T])
    extends AnyVal
{
    /**
     * Field match regular expression: '''$regex'''.
     */
    def =~[DocumentT <: AnyRef] (re : matching.Regex)
        (
            implicit
            writer : DocumentWriter[RegularExpression[DocumentT, T], DocumentT],
            negationWriter : DocumentWriter[Not[DocumentT], DocumentT]
        )
    : RegularExpression[DocumentT, T] =
        RegularExpression (self, re.toString ())


    /**
     * Field match regular expression: '''$regex'''.
     */
    def =~[DocumentT <: AnyRef] (re : String)
        (
            implicit
            writer : DocumentWriter[RegularExpression[DocumentT, T], DocumentT],
            negationWriter : DocumentWriter[Not[DocumentT], DocumentT]
        )
    : RegularExpression[DocumentT, T] =
        RegularExpression (self, re)


    /**
     * Field match regular expression: '''$regex'''.
     */
    def =~[DocumentT <: AnyRef, A] (re : (A, RegexModifier))
        (
            implicit
            witness : SupportedTypesWitness[A],
            writer : DocumentWriter[RegularExpression[DocumentT, T], DocumentT],
            negationWriter : DocumentWriter[Not[DocumentT], DocumentT]
        )
    : RegularExpression[DocumentT, T] =
        RegularExpression (self, witness (re._1), Some (re._2))


    /**
     * Field does not match regular expression: '''$regex'''.
     */
    def !~[DocumentT <: AnyRef] (re : matching.Regex)
        (
            implicit
            writer : DocumentWriter[RegularExpression[DocumentT, T], DocumentT],
            negationWriter : DocumentWriter[Not[DocumentT], DocumentT]
        )
    : Not[DocumentT] =
        Not (RegularExpression (self, re.toString ()))


    /**
     * Field does not match regular expression: '''$regex'''.
     */
    def !~[DocumentT <: AnyRef] (re : String)
        (
            implicit
            writer : DocumentWriter[RegularExpression[DocumentT, T], DocumentT],
            negationWriter : DocumentWriter[Not[DocumentT], DocumentT]
        )
    : Not[DocumentT] =
        Not (RegularExpression (self, re))
}


trait EvaluationSyntax
{
    /// Implicit Conversions
    implicit def toEvaluationOps[T >: String] (field : Field[T])
    : EvaluationOps[T] =
        new EvaluationOps[T] (field)
}


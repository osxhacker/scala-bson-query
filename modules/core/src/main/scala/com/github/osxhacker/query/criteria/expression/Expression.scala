package com.github.osxhacker.query.criteria.expression

import com.github.osxhacker.query.model.Operator


/**
 * The '''Expression''' type is the common ancestor for __all__ abstract
 * syntax tree (AST) types available for criteria use.
 */
sealed trait Expression[DocumentT <: AnyRef]
    extends Product
{
    /// Instance Properties
    def operand : Operator


    /**
     * The negate method creates the logical negation of '''this'''
     * '''Expression'''.  Where possible, a suitable `operand` is chosen,
     * falling back to a
     * [[com.github.osxhacker.query.criteria.expression.Logical]] negation.
     */
    def negate () : Expression[DocumentT]


    /**
     * The toDocument method defines the ability to translate an arbitrary
     * '''Expression''' type to its underlying ''DocumentT'' representation.
     */
    def toDocument () : DocumentT
}


trait BinaryExpression[DocumentT <: AnyRef, LhsT, RhsT]
    extends Expression[DocumentT]


trait NaryExpression[DocumentT <: AnyRef, T]
    extends Expression[DocumentT]


trait UnaryExpression[DocumentT <: AnyRef, T]
    extends Expression[DocumentT]


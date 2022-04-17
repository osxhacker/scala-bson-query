package com.github.osxhacker.query.criteria


/**
 * The '''expression''' `package` defines a recursive propositional abstract
 * syntax tree central to the MongoDB embedded domain-specific language (EDSL).
 * It is the main abstraction used to provide the EDSL and results in being
 * able to write:
 *
 * {{{
 * import com.github.osxhacker.query.criteria._
 * import untyped._
 *
 * val edslQuery = criteria.first < 10 && (
 *	criteria.second >= 20.0 || criteria.second.in (0.0, 1.0)
 *	)
 * }}}
 *
 * And have that be equivalent to this abstract syntax tree:
 *
 * {{{
 * val ast = And (
 *   LessThan (
 *     Field ("first"),
 *     Value (10)
 *     ),
 *   Or (
 *     GreaterThanOrEqual (
 *       Field ("second"),
 *       Value (20.0)
 *       ),
 *     In (
 *       Field ("second"),
 *       Chain (0.0, 1.0)
 *       )
 *     )
 *   )
 * }}}
 */
package object expression
{
    /// Class Types
    object syntax
        extends ComparisonSyntax
            with CollectionSyntax
            with ElementSyntax
            with EvaluationSyntax
            with LogicalSyntax
}


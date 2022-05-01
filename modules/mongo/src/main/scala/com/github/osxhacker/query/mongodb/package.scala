package com.github.osxhacker.query

import com.github.osxhacker.query.{
    criteria => CoreCriteria,
    sorting => CoreSorting
    }


/**
 * The '''mongodb''' `package` defines the `query` types specific to the
 * [[https://www.mongodb.com/docs/manual/reference/bson-types/ MongoDB BSON types]].
 */
package object mongodb
{
    /// Class Types
    object typed
    {
        object all
            extends CoreCriteria.expression.ComparisonSyntax
                with CoreCriteria.expression.CollectionSyntax
                with CoreCriteria.expression.ElementSyntax
                with CoreCriteria.expression.EvaluationSyntax
                with CoreCriteria.expression.LogicalSyntax
                with CoreSorting.expression.SortingSyntax
                with CoreCriteria.TypedFunctions
                with CoreSorting.TypedFunctions
                with CriteriaWriterImplicits
                with ExpressionImplicits
                with SortingImplicits


        object criteria
            extends CoreCriteria.expression.ComparisonSyntax
                with CoreCriteria.expression.CollectionSyntax
                with CoreCriteria.expression.ElementSyntax
                with CoreCriteria.expression.EvaluationSyntax
                with CoreCriteria.expression.LogicalSyntax
                with CoreCriteria.TypedFunctions
                with CriteriaWriterImplicits
                with ExpressionImplicits


        object sorting
            extends CoreSorting.expression.SortingSyntax
                with CoreSorting.TypedFunctions
                with SortingImplicits
    }


    object untyped
    {
        object all
            extends CoreCriteria.expression.ComparisonSyntax
                with CoreCriteria.expression.CollectionSyntax
                with CoreCriteria.expression.ElementSyntax
                with CoreCriteria.expression.EvaluationSyntax
                with CoreCriteria.expression.LogicalSyntax
                with CoreSorting.expression.SortingSyntax
                with CoreCriteria.UntypedFunctions
                with CoreSorting.UntypedFunctions
                with CriteriaWriterImplicits
                with ExpressionImplicits
                with SortingImplicits


        object criteria
            extends CoreCriteria.expression.ComparisonSyntax
                with CoreCriteria.expression.CollectionSyntax
                with CoreCriteria.expression.ElementSyntax
                with CoreCriteria.expression.EvaluationSyntax
                with CoreCriteria.expression.LogicalSyntax
                with CoreCriteria.UntypedFunctions
                with CriteriaWriterImplicits
                with ExpressionImplicits


        object sorting
            extends CoreSorting.expression.SortingSyntax
                with CoreSorting.UntypedFunctions
                with SortingImplicits
    }
}


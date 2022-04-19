package com.github.osxhacker.query


/**
 * The '''mongodb''' `package` defines the `query` types specific to the
 * [[https://www.mongodb.com/docs/manual/reference/bson-types/ MongoDB BSON types]].
 */
package object reactive
{
    /// Class Imports
    import criteria.{
        TypedFunctions,
        UntypedFunctions,
        expression
        }


    /// Class Types
    object typed
    {
        object all
            extends expression.ComparisonSyntax
                with expression.CollectionSyntax
                with expression.ElementSyntax
                with expression.EvaluationSyntax
                with expression.LogicalSyntax
                with TypedFunctions
                with CriteriaWriterImplicits
                with ExpressionImplicits


        object criteria
            extends expression.ComparisonSyntax
                with expression.CollectionSyntax
                with expression.ElementSyntax
                with expression.EvaluationSyntax
                with expression.LogicalSyntax
                with TypedFunctions
                with CriteriaWriterImplicits
                with ExpressionImplicits
    }


    object untyped
    {
        object all
            extends expression.ComparisonSyntax
                with expression.CollectionSyntax
                with expression.ElementSyntax
                with expression.EvaluationSyntax
                with expression.LogicalSyntax
                with UntypedFunctions
                with CriteriaWriterImplicits
                with ExpressionImplicits


        object criteria
            extends expression.ComparisonSyntax
                with expression.CollectionSyntax
                with expression.ElementSyntax
                with expression.EvaluationSyntax
                with expression.LogicalSyntax
                with UntypedFunctions
                with CriteriaWriterImplicits
                with ExpressionImplicits
    }
}


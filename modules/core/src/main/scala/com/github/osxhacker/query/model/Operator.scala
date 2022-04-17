package com.github.osxhacker.query.model


/**
 * The '''Operator''' type defines __all__
 * [[https://docs.mongodb.com/manual/reference/operator/query/ Query Operators]]
 * supported by [[com.github.osxhacker.query]].
 */
sealed trait Operator
{
    /// Instance Properties
    def name : String
}


object Operator
{
    /// Class Types

    /// Logical
    case object And
        extends Operator
    {
        override val name = "$and"
    }


    case object Nor
        extends Operator
    {
        override val name = "$nor"
    }


    case object Not
        extends Operator
    {
        override val name = "$not"
    }


    case object Or
        extends Operator
    {
        override val name = "$or"
    }


    /// Comparison
    case object Equal
        extends Operator
    {
        override val name = "$eq"
    }


    case object GreaterThan
        extends Operator
    {
        override val name = "$gt"
    }


    case object GreaterThanOrEqual
        extends Operator
    {
        override val name = "$gte"
    }


    case object In
        extends Operator
    {
        override val name = "$in"
    }


    case object LessThan
        extends Operator
    {
        override val name = "$lt"
    }


    case object LessThanOrEqual
        extends Operator
    {
        override val name = "$lte"
    }


    case object NotEqual
        extends Operator
    {
        override val name = "$ne"
    }


    case object NotIn
        extends Operator
    {
        override val name = "$nin"
    }


    /// Element
    case object Exists
        extends Operator
    {
        override val name = "$exists"
    }


    case object Type
        extends Operator
    {
        override val name = "$type"
    }


    /// Evaluation
    case object Regex
        extends Operator
    {
        override val name = "$regex"
    }


    case object Text
        extends Operator
    {
        override val name = "$text"
    }


    /// Array
    case object All
        extends Operator
    {
        override val name = "$all"
    }


    case object ElementMatches
        extends Operator
    {
        override val name = "$elemMatches"
    }


    case object Size
        extends Operator
    {
        override val name = "$size"
    }
}


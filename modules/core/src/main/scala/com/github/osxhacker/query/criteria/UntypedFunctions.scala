package com.github.osxhacker.query.criteria

import com.github.osxhacker.query.model.Where


trait UntypedFunctions
{
    /**
     * The criteria property is a ''factory'' of '''Untyped''' instances.
     */
    def criteria : Untyped = Untyped ()


    def where[R] (block : Untyped => R)
        (implicit ev : Where.Aux[Untyped, R])
    : R =
        block (criteria)


    def where[R] (block : (Untyped, Untyped) => R)
        (implicit ev : Where.Aux[(Untyped, Untyped), R])
    : R =
        block (criteria, criteria)


    def where[R] (block : (Untyped, Untyped, Untyped) => R)
        (implicit ev : Where.Aux[(Untyped, Untyped, Untyped), R])
    : R =
        block (criteria, criteria, criteria)


    def where[R] (block : (Untyped, Untyped, Untyped, Untyped) => R)
        (implicit ev : Where.Aux[(Untyped, Untyped, Untyped, Untyped), R])
    : R =
        block (criteria, criteria, criteria, criteria)


    def where[R] (block : (Untyped, Untyped, Untyped, Untyped, Untyped) => R)
        (implicit ev : Where.Aux[(Untyped, Untyped, Untyped, Untyped, Untyped), R])
    : R =
        block (criteria, criteria, criteria, criteria, criteria)


    def where[R] (block : (Untyped, Untyped, Untyped, Untyped, Untyped, Untyped) => R)
        (implicit ev : Where.Aux[(Untyped, Untyped, Untyped, Untyped, Untyped, Untyped), R])
    : R =
        block (criteria, criteria, criteria, criteria, criteria, criteria)


    def where[R] (block : (Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped) => R)
        (implicit ev : Where.Aux[(Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped), R])
    : R =
        block (criteria, criteria, criteria, criteria, criteria, criteria, criteria)


    def where[R] (block : (Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped) => R)
        (implicit ev : Where.Aux[(Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped), R])
    : R =
        block (criteria, criteria, criteria, criteria, criteria, criteria, criteria, criteria)


    def where[R] (block : (Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped) => R)
        (implicit ev : Where.Aux[(Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped), R])
    : R =
        block (criteria, criteria, criteria, criteria, criteria, criteria, criteria, criteria, criteria)


    def where[R] (block : (Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped) => R)
        (implicit ev : Where.Aux[(Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped, Untyped), R])
    : R =
        block (criteria, criteria, criteria, criteria, criteria, criteria, criteria, criteria, criteria, criteria)
}


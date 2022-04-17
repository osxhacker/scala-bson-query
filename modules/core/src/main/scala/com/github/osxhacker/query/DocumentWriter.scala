package com.github.osxhacker.query


/**
 * The '''DocumentWriter''' type defines the type class contract for being able
 * to produce a driver-specific BSON document or value.
 */
trait DocumentWriter[CoreT, DocumentT <: AnyRef]
{
    def write (tree : CoreT) : DocumentT
}


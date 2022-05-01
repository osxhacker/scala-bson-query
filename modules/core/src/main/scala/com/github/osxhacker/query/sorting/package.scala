package com.github.osxhacker.query


/**
 * The '''sorting''' `package` defines an EDSL relating to sorting documents
 * retrieved from a [[https://www.mongodb.com/ MongoDB]] collection.
 */
package object sorting
{
    /// Class Types
    object typed
        extends TypedFunctions


    object untyped
        extends UntypedFunctions
}

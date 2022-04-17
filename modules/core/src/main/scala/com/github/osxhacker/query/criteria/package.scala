package com.github.osxhacker.query


/**
 * The '''criteria''' `package` defines types related to defining filters for
 * retrieving documents from a [[https://www.mongodb.com/ MongoDB]] collection.
 */
package object criteria
{
    /// Class Types
    object typed
        extends TypedFunctions


    object untyped
        extends UntypedFunctions
}


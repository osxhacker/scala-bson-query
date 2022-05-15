package com.github.osxhacker.query


/**
 * The '''projection''' `package` defines an EDSL relating to specifying the
 * contents of documents * retrieved from a
 * [[https://www.mongodb.com/ MongoDB]] collection.
 */
package object projection
{
    /// Class Types
    object typed
        extends TypedFunctions


    object untyped
        extends UntypedFunctions
}

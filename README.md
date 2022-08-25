scala-bson-query
======================

Defines a DSL for creating [MongoDB](http://mongodb.github.io/mongo-java-driver/) and [ReactiveMongo](https://github.com/ReactiveMongo/ReactiveMongo) queries.

More detailed information can be found in the [documentation](https://osxhacker.github.io/scala-bson-query).

## Overview

The original version of this library only supported the ReactiveMongo driver.  This project supports that and the official MongoDB Scala/Java driver as well.

There may be additional `modules` introduced, such as support for [Circe](https://circe.github.io/circe/) and [Refined](https://github.com/fthomas/refined) in the future.

Until `v1.0.0`, the documentation found here should be considered as being in a state of transition.

### Original Query Syntax

The `reactivemongo.api.collections.GenericCollection` type provides the `find` method used to find documents matching a criteria.  It is this interaction which the DSL targets.  Originally, providing a selector to `find` had an interaction similar to:

```scala
  val cursor = collection.find (BSONDocument ("firstName" -> "Jack")).cursor[BSONDocument]
```

This is, of course, still supported as the DSL does not preclude this usage.

### Criteria DSL

#### Untyped DSL Support

What the DSL *does* provide is the ablity to formulate queries thusly:

```scala
  // Using an untyped.criteria
  {
  import com.github.osxhacker.query.mongodb.untyped.criteria._

  // The MongoDB properties referenced are not enforced by the compiler
  // to belong to any particular type.  This is what is meant by "Untyped".
  val adhoc = criteria.firstName === "Jack" && criteria.age >= 18
  val cursor = collection.find (adhoc).cursor[BSONDocument]
  }
```

Another form which achieves the same result is to use one of the `where` methods available:

```scala
  // Using one of the untyped.where overloads
  {
  import com.github.osxhacker.query.reactive.untyped.criteria._

  val cursor = collection.find (
    where (_.firstName === "Jack" && _.age >= 18)
    ).cursor[BSONDocument]
  }
```

There are overloads for between 1 and 22 place holders using the `where` method.  Should more than 22 be needed, then the 1 argument version should be used with a named parameter.  This allows an infinite number of property constraints to be specified.

#### Typed DSL Support

For situations where the MongoDB document structure is well known and a developer wishes to enforce property existence **during compilation**, the `typed` Criteria can be used:

```scala
  {
  // Using a typed criteria which restricts properties to those
  // within a given type and/or those directly accessible
  // through property selectors.
  import com.github.osxhacker.query.reactive.typed.criteria._

  case class Nested (rating : Double)
  case class ExampleDocument (aProperty : String, another : Int, nested : Nested)

  val byKnownProperties = where[ExampleDocument] {
    doc =>
      doc (_.aProperty) =~ "^[A-Z]\\w+" && (
        doc (_.another) > 0 ||
        doc (_.nested.rating) < 10.0
        )
      }

  val cursor = collection.find (byKnownProperties).cursor[BSONDocument]
  }
```

When the `typed` version of the DSL is employed, compilation will fail if the provided property navigation does not exist from the *root type* (specified as the type parameter to `criteria` above) **or** the leaf type is not type-compatible with the value(s) provided (if any).

An easy way to think of this is that if it doesn't compile in "regular usage", then it definitely will not when used in a `typed.criteria`.


### Usage Considerations

Note that `typed` and `untyped` serve different needs.  When the structure of a document collection is both known *and* identified as static, `typed` makes sense to employ.  However, `untyped` is compelling when document structure can vary within a collection.  These are considerations which can easily vary between projects and even within different modules of one project.

Feel free to use either or both `typed` and `untyped` as they make sense for the problem at hand.  One thing to keep in mind is that the examples shown above assumes only one is in scope.


### Roadmap

This section details the functionality either currently or planned to be supported by ReactiveMongo-Criteria.

- Update documentation to reflect migration to new project
- Ability to formulate queries without requiring knowledge of document structure. *COMPLETE*
- Ability to ''type check'' query constraints by specifying a Scala type. *COMPLETE*
- Define and add support for an [EDSL](http://scalamacros.org/usecases/advanced-domain-specific-languages.html) specific to sorting. *COMPLETE*
- Define and add support for an [EDSL](http://scalamacros.org/usecases/advanced-domain-specific-languages.html) specific to projections. *COMPLETE*


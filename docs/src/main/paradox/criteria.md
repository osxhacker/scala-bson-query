## Criteria

Defines a DSL for creating [MongoDB](http://mongodb.github.io/mongo-java-driver/) and [ReactiveMongo](https://github.com/ReactiveMongo/ReactiveMongo) criteria for queries.

### Overview

The original version of this library only supported the ReactiveMongo driver.  This project supports that and the official MongoDB Scala/Java driver as well.

There may be additional `modules` introduced, such as support for [Circe](https://circe.github.io/circe/) and [Refined](https://github.com/fthomas/refined) in the future.

Until `v1.0.0`, the documentation found here should be considered as being in a state of transition.

## Original Query Syntax

The `reactivemongo.api.collections.GenericCollection` type provides the `find` method used to find documents matching a criteria.  It is this interaction which the DSL targets.  Originally, providing a selector to `find` had an interaction similar to:

```scala
  val cursor = collection.find(BSONDocument("firstName" -> "Jack")).cursor[BSONDocument]
```

This is, of course, still supported as the DSL does not preclude this usage.

## DSL

### Untyped DSL Support

What the DSL *does* provide is the ablity to formulate queries thusly:

```scala
  // Using an untyped.criteria
  {
  import com.github.osxhacker.query.mongodb.untyped.criteria._

  // The MongoDB properties referenced are not enforced by the compiler
  // to belong to any particular type.  This is what is meant by "Untyped".
  val adhoc = criteria.firstName === "Jack" && criteria.age >= 18
  val matching = collection.find (adhoc).collect ()
  }
```

Another form which achieves the same result is to use one of the `where` methods available:

```scala
  // Using one of the untyped.where overloads
  {
  import com.github.osxhacker.query.reactive.untyped.criteria._

  val matching = collection.find (
    where (_.firstName === "Jack" && _.age >= 18)
	).collect ()
  }
```

There are overloads for between 1 and 22 place holders using the `where` method.  Should more than 22 be needed, then the 1 argument version should be used with a named parameter.  This allows an infinite number of property constraints to be specified.

### Typed DSL Support

For situations where the MongoDB document structure is well known and a developer wishes enforce property existence **during compilation**, the `typed` Criteria can be used:

```scala
  {
  // Using a typed criteria which restricts properties to those
  // within a given type and/or those directly accessible
  // through property selectors.
  import com.github.osxhacker.query.mongo.typed.criteria._

  case class Nested (rating : Double)
  case class ExampleDocument (aProperty : String, another : Int, nested : Nested)

  val byKnownProperties = where[ExampleDocument] {
    doc =>
      doc (_.aProperty) =~ "^[A-Z]\\w+" && (
        doc (_.another) > 0 ||
        doc (_.nested.rating) < 10.0
	    )
	  }

  val results = collection.find (byKnownProperties).collect ()
  }
```

When the `typed` version is employed, compilation will fail if the provided property navigation does not exist from the *root type* (specified as the type parameter to `criteria` above) **or** the leaf type is not type-compatible with the value(s) provided (if any).

An easy way to think of this is that if it doesn't compile in "regular usage", then it definitely will not when used in a `typed.criteria`.


### Usage Considerations

Note that `typed` and `untyped` serve different needs.  When the structure of a document collection is both known *and* identified as static, `typed` makes sense to employ.  However, `untyped` is compelling when document structure can vary within a collection.  These are considerations which can easily vary between projects and even within different modules of one project.

Feel free to use either or both `typed` and `untyped` as they make sense for the problem at hand.  One thing to keep in mind is that the examples shown above assumes only one is in scope.


## Operators

When using the Criteria DSL, the fact that the operators adhere to the expectations of both programmers and Scala precedences, most uses will "just work."  For example, explicitly defining grouping is done with parentheses, just as you would do with any other bit of Scala code.

For the purposes of the operator API reference, assume the following code is in scope:

```scala
import com.github.osxhacker.query.reactive.untyped.criteria._
```

### Comparison Operators

With the majority of comparison operators, keep in mind that the definition of their ordering is dependent on the type involved.  For example, strings will use lexigraphical ordering whereas numbers use natural ordering.

* **===**, **@==** Matches properties based on value equality.

```scala
criteria.aProperty === "value"
```

```scala
criteria.aProperty @== "value"
```

* **<>**, **=/=**, **!==** Matches properties which do not have the given value.

```scala
criteria.aProperty <> "value"
```

```scala
criteria.aProperty =/= "value"
```

```scala
criteria.aProperty !== "value"
```

* **<** Matches properties which compare "less than" a given value.

```scala
criteria.aNumber < 99
```

* **<=** Matches properties which compare "less than or equal to" a given value.

```scala
criteria.aNumber <= 99
```

* **>** Matches properties which compare "greater than" a given value.

```scala
criteria.aProperty > "Alice"
```

* **>=** Matches properties which compare "greater than or equal to" a given value.

```scala
criteria.aNumber >= 100
```

* **between** Shorthand for specifying a range of values.  The `BetweenPolicy` determines how the boundary values are considered.

```scala
criteria.aString.between[HalfOpen] ("A", "B")
```

### Existence Operators

* **exists** Matches any document which has the specified field.  Use the unary not operator to match based on the leaf property being absent entirely.

```scala
criteria.aProperty.exists	// Requires 'aProperty' to be in the document
!criteria.aProperty.exists	// Only matches documents without 'aProperty'
```

* **in** Matches properties which equal one of the given values or array properties having one element which equals any of the given values.  Combine with the unary not operator to specify "not in."

```scala
criteria.ranking.in (1, 2, 3, 4, 5)
!criteria.ranking.in (1, 2, 3, 4, 5)
```

* **all** Matches array properties which contain all of the given values.

```scala
criteria.strings.all ("hello", "world")
```

### String Operators

* **=~** Matches a string property which satisfies the given regular expression `String`, optionally with [regex flags](https://docs.mongodb.com/manual/reference/operator/query/regex/).

```scala
criteria.aProperty =~ """^(value)|(someting\s+else)"""
criteria.aProperty =~ """^(value)|(someting\s+else)""" -> IgnoreCase
```

* **!~** Matches a string property which does _not_ satisfy the given regular expression `String`, optionally with [regex flags](https://docs.mongodb.com/manual/reference/operator/query/regex/).

```scala
criteria.aProperty !~ """\d+"""
criteria.aProperty !~ """foo.*bar""" -> (IgnoreCase | MultilineMatching)
```

### Logical Operators

* **!** The unary not operator provides logical negation of an `Expression`.

```scala
!(criteria.aProperty === "value")
```

* **&&** Defines logical conjunction (''AND'').

```scala
criteria.aProperty === "value" && criteria.another > 0
```

* **!&&** Defines negation of conjunction (''NOR'').

```scala
criteria.aProperty === "value" !&& criteria.aProperty @== "other value"
```

* **||** Defines logical disjunction (''OR'').

```scala
criteria.aProperty === "value" || criteria.aProperty === "other value"
```


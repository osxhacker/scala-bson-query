## Sorting

The official MongoDB Scala driver supports sorting using the [Sorts](https://mongodb.github.io/mongo-java-driver/4.5/apidocs/mongodb-driver-core/com/mongodb/client/model/Sorts.html) helper.  One can still use that approach if desired.

However, `scala-bson-query` defines `sorting` support which allows:

```scala
  // Using untyped.sorting
  {
  import com.github.osxhacker.query.mongodb.untyped.sorting._
  import shapeless._

  val result = collection.sort (by (_.name.ascending)).collect ()
  val equivalent = collection.sort (
    byEach {
      doc =>
        doc.name.ascending ::
        HNil
      }
      .collect ()
  }

  // Using typed.sorting
  {
  import com.github.osxhacker.query.mongodb.typed.sorting._
  import shapeless._

  case class Nested (rating : Double)
  case class ExampleDocument (aProperty : String, another : Int, nested : Nested)

  val result = collection.sort (
    by[ExampleDocument] {
      doc =>
        doc (_.another).desc ::
        doc (_.nested.rating).asc ::
        HNil
      }
    )
  }
```

### Methods

#### Typed

For the purposes of the method API reference, assume the following code is in scope:

```scala
import com.github.osxhacker.query.{
  model,
  sorting
  }
```

* **by**, a method which requires a functor that takes a `model.SortFieldAccess` and produces a non-empty `HList` of `SortField` definitions.


#### Untyped

For the purposes of the method API reference, assume the following code is in scope:

```scala
import com.github.osxhacker.query.sorting
```

* **by**, overloaded method accepting between 1 and 4 functions taking `sorting.Untyped` instance(s), each which produce a `SortField` instance.

* **byEach**, a method which requires a functor that takes a `sorting.Untyped` and produces a non-empty `HList` of `SortField` definitions.

### Operators

For the purposes of the operator API reference, assume the following code is in scope:

```scala
import com.github.osxhacker.query.reactive.untyped.sorting._
```

* **asc**, **ascending** Indicates ascending sort order.

```scala
by (_.aProperty.ascending, _.anotherProperty.asc)
```

* **desc**, **descending** Indicates descending sort order.

```scala
by (_.aProperty.descending, _.anotherProperty.desc)
```


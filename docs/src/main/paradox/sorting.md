## Sorting

The official MongoDB Scala driver supports sorting using the [Sorts](https://mongodb.github.io/mongo-java-driver/4.5/apidocs/mongodb-driver-core/com/mongodb/client/model/Sorts.html) helper.  One can still use that approach if desired.

However, `scala-bson-query` defines a `sorting` package which allows:

```scala
  // Using untyped.sorting
  {
  import com.github.osxhacker.query.mongodb.untyped.sorting._

  val result = collection.sort (by (_.name.ascending)).collect ()
  val equivalent = collection.sort (
    byEach {
	  doc =>
	    doc.name.ascending ::
		HNil
	  }
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

### Operators

TBD


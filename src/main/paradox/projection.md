## Projection

The official MongoDB Scala driver supports projection using the [Projections](https://mongodb.github.io/mongo-java-driver/4.5/apidocs/mongodb-driver-core/com/mongodb/client/model/Projections.html) helper.  One can still use that approach if desired.

However, `scala-bson-query` defines `projection` support which allows:

```scala
  // Using untyped.projection
  {
  import com.github.osxhacker.query.mongodb.untyped.projection._
  import shapeless._

  val result = collection.find ().projection (
    returning {
        available =>
          available.a ::
          availalble.b ::
          HNil
      }
    )
  }

  // Using typed.projection
  {
  import com.github.osxhacker.query.mongodb.typed.projection._

  case class Nested (rating : Double)
  case class ExampleDocument (aProperty : String, another : Int, nested : Nested)

  val result = collection.find ().projection (into[ExampleDocument] ())
  }
```

### Methods

#### Typed

For the purposes of the method API reference, assume the following code is in scope:

```scala
import com.github.osxhacker.query.projection.typed._
```

* **into**, a method which inspects the parameterized `case class` type, producing a projection containing all discoverable properties.


#### Untyped

For the purposes of the method API reference, assume the following code is in scope:

```scala
import com.github.osxhacker.query.projection.untyped._
```

* **returning**, method which allows for the production of an `HList` having each property to return explicitly specified.



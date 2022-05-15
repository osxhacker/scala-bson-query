package com.github.osxhacker.query.projection

import scala.reflect.macros.blackbox


/**
 * The '''TypedMacros''' `object` defines the
 * [[http://docs.scala-lang.org/overviews/macros/overview.html Scala Macros]]
 * used in supporting type-checked
 * [[com.github.osxhacker.query.projection.ProjectionSpecification]] creation.
 */
object TypedMacros
{
    def deriveProjection[T <: Product : c.WeakTypeTag] (
        c : blackbox.Context { type PrefixType = ProjectionSpecification[T] }
        )
        ()
    : c.Tree =
    {
        import c.universe._

        def discover (
            ctor : Option[c.universe.MethodSymbol],
            path : List[String] = Nil
            )
        : Iterable[c.Tree] =
        {
            val names = ctor.flatMap(_.paramLists.headOption)
                .toList
                .flatten

            names.flatMap {
                case collection if isCollectionLike (c) (collection) &&
                    isProductType (c) (collection.typeSignature.resultType.typeArgs.head)
                =>
                    val product = collection.typeSignature
                        .typeArgs
                        .headOption
                        .flatMap {
                            _.decls.collectFirst {
                                case ctor : MethodSymbol if ctor.isPrimaryConstructor =>
                                    ctor
                            }
                        }

                    discover (product, path :+ collection.name.toString ())

                case collection if isCollectionLike (c) (collection) =>
                    import c.universe._

                    val fullPath = (path :+ collection.name.toString ()).mkString(".")

                    List(
                        q"""
                        new _root_.com.github.osxhacker.query.criteria.Field[Any] (
                           ${ Literal (Constant (fullPath)) }
                           )
                       """)

                case product if isProduct (c) (product) =>
                    val primaryCtor = product.typeSignature.decls.collectFirst {
                        case ctor : MethodSymbol if ctor.isPrimaryConstructor =>
                            ctor
                    }

                    discover (primaryCtor, path :+ product.name.toString ())

                case argument =>
                    import c.universe._

                    val fullPath = (path :+ argument.name.toString ()).mkString(".")

                    List(
                        q"""
                        new _root_.com.github.osxhacker.query.criteria.Field[Any] (
                           ${ Literal (Constant (fullPath)) }
                           )
                       """)
            }
        }

        val primaryCtor = weakTypeOf[T].decls
            .collect {
                case ctor : MethodSymbol if ctor.isPrimaryConstructor =>
                    ctor
            }
            .headOption

        q"""
            new _root_.com.github.osxhacker.query.projection.ProjectionSpecification (
                Seq (
                   ..${ discover (primaryCtor) }
                    )
                )
        """
    }


    private def isCollectionLike (c : blackbox.Context)
        (name : c.Symbol)
    : Boolean =
        name.typeSignature.resultType.typeArgs.length == 1


    private def isProduct (c : blackbox.Context)
        (name : c.Symbol)
    : Boolean =
        isProductType (c) (name.typeSignature)


    private def isProductType (c : blackbox.Context)
        (aType : c.Type)
    : Boolean =
    {
        import c.universe._

        aType <:< typeOf[Product]
    }
}

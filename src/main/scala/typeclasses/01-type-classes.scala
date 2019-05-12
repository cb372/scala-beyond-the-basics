
object typeclasses {

  /*
   * A type class is a way of retroactively adding a common interface to a
   * number of different types.
   *
   * These may be types that are 'out of your control', e.g. types defined in
   * the Scala standard library or a 3rd-party library.
   */

  /*
   * In Haskell, the language has first-class support for type classes.
   * Scala does not, but there is a common convention on how to encode them.
   *
   * We use traits with type parameters to represent type classes, and
   * implicits to represent type class instances. This means we can make use of
   * Scala's implicit search mechanism to look up instances for us.
   *
   * In some ways this makes Scala's type class mechanism more powerful than
   * Haskell's, which has limited support for "orphan" or "incoherent"
   * instances.
   */

  trait Monoid[A] {
    def unit: A
    def combine(x: A, y: A): A
  }

  implicit val intAdditionMonoid: Monoid[Int] = new Monoid[Int] {
    def unit: Int = 0
    def combine(x: Int, y: Int): Int = x + y
  }

  object multiply {
    implicit val intMultiplicationMonoid: Monoid[Int] = new Monoid[Int] {
      def unit: Int = 1
      def combine(x: Int, y: Int): Int = x * y
    }
  }

  implicit val stringMonoid: Monoid[String] = new Monoid[String] {
    def unit: String = ""
    def combine(x: String, y: String): String = x ++ y
  }

  /*
   * Here's an example of chained implicits:
   *
   * if you give me evidence that V is a Monoid,
   * I'll give you evidence that Map[K, V] is a Monoid
   */
  implicit def mapMonoid[K, V](implicit valueMonoid: Monoid[V]): Monoid[Map[K, V]] = new Monoid[Map[K, V]] {
    def unit = Map.empty[K, V]
    def combine(xs: Map[K, V], ys: Map[K, V]): Map[K, V] = {
      xs.foldLeft(ys) {
        case (acc, (k, x)) =>
          val v = acc.get(k) match {
            case Some(y) => valueMonoid.combine(x, y)
            case None => x
          }
          acc.updated(k, v)
      }
    }
  }

  /*
   * As well as the trait defining the interface, a good type class also comes
   * with a set of laws that all instances must obey.
   *
   * These laws are usually tested using property-based testing.
   * Cats uses a library called Discipline to help with this.
   *
   * For Monoid above, the laws are
   *
   * - left identity:  for all x, combine(unit, x) == x
   * - right identity: for all x, combine(x, unit) == x
   * - associativity:  for all x, y, z, combine(combine(x, y) z) == combine(x, combine(y, z))
   */

}

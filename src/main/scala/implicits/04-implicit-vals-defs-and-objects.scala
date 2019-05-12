
object valdefobj {

  case class Foo(value: Int)

  /*
   * The simplest way to define an implicit value is with `implicit val`.
   */

  implicit val fooOrdering: Ordering[Foo] = Ordering.by(_.value)

  /*
   * You can also achieve the same using an implicit object.
   */

  implicit object FooOrdering extends Ordering[Foo] {
    def compare(x: Foo, y: Foo): Int = x.value - y.value
  }

  /*
   * If you want to do something generically, you can use an implicit def.
   */

  implicit def sillyOrdering[A]: Ordering[A] = new Ordering[A] {
    def compare(x: A, y: A): Int = x.hashCode - y.hashCode
  }

  /*
   * Where things start to get interesting is when you have an implicit def that in turn takes implicit arguments.
   *
   * This is known as implicit chaining.
   */

  trait Show[A] {
    def show(a: A): String
  }

  implicit def integralShow[A](implicit integral: Integral[A]): Show[A] = new Show[A] {
    def show(a: A): String = s"approximately ${integral.toInt(a)}"
  }

  /*
   * The above method can be read as:
   *
   * for any type A,
   * if you can give me evidence that there's an Integral instance for A,
   * I'll give you evidence that there's a Show instance for A.
   */

}

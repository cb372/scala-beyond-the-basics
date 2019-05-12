import scala.concurrent.ExecutionContext

object implicitlookup {

  val x = 42 // ignore this

  /*
   * Where does the compiler look when doing implicit search?
   *
   * Answer:
   *
   * First it looks in lexical scope.
   *
   * If it can't find any suitable implicit in lexical scope, it looks in
   * implicit scope.
   */

  /*
   * Lexical scope
   *
   * Imagine the compiler is looking at the following line of code, somewhere
   * in your Scala program.
   */

  val y: Int = x + 1

  /*
   * The compiler needs to find a variable which can be referenced using the
   * name `x`, without prefixing it with any path (e.g. you haven't referenced
   * it as `Foo.bar.x`, just as `x`).
   *
   * It will look in a few places, in a certain order, to find `x`:
   *
   * - Starting with the local scope, it will expand its search out into
   *   enclosing scopes.
   *
   * - If it can't find `x` in scope in the local file, it will look for an `x`
   *   brought into scope via an import, starting with local imports and then
   *   moving onto imports in enclosing scopes.
   *
   * - If it can't find `x` using imports, it looks for an `x` in the current
   *   package in different source files.
   *
   * Implicit search in lexical scope uses the same rules. However, for
   * implicits we don't care what the name is. We only care that it's a
   * "simple" name (e.g. `x`, not `Foo.bar.x`).
   */

  /*
   * Exercise: what does the following code print, and why?
   */
  object Y {
    implicit val n: Int = 17
    trait T {
      implicit val i: Int = 17
    }
    object X extends T {
      implicit val n: Int = 42
      implicit val s: String = "hello, world\n"
      def f(implicit s: String) = implicitly[String] * implicitly[Int]
    }
  }
  import Y.X._
  println(f)

  /*
   * Implicit scope
   *
   * If no suitable implicit is found in lexical scope, the compiler looks in implicit scope.
   *
   * Implicit scope depends on the type of the implicit being searched for.
   *
   */

  def foo(implicit ec: ExecutionContext): Unit = ???

  /*
   * Here the implicit scope would include the companion object of
   * ExecutionContext (which doesn't actually contain any implicit
   * ExecutionContext instances).
   */

  def bar[A](implicit ord: Ordering[A]): Unit = ???

  /*
   * Here the implicit scope would include the companion object of `Ordering`
   * but also the companion object of `A`.
   *
   * The companion objects of all supertypes of both `Ordering` and `A` would
   * also be searched.
   */

  /*
   * Implicits in a companion object have higher priority than those in a trait
   * that it extends. Because of this, you will sometimes see libraries use a
   * hierarchy of traits to explicitly control the priority of implicits and
   * avoid ambiguity:
   */

  trait LowestPriorityImplicits {

    //implicit val x ...

    //implicit def y(...)

  }

  trait MediumPriorityImplicits extends LowestPriorityImplicits {

    // ...

  }

  object CompanionObject extends MediumPriorityImplicits {

    //implicit val highestPriority ...

  }

  /*
   * Exercise: what will `printThing(Thing(42))` print?
   */

  trait Show[A] {
    def show(a: A): String
  }

  case class Thing(x: Int)

  object Show {

    implicit val showThing: Show[Thing] = new Show[Thing] {
      def show(thing: Thing): String = "Some kind of thing"
    }

  }

  trait Wow1 {

    implicit val showThing: Show[Thing] = new Show[Thing] {
      def show(thing: Thing): String = s"thing with value ${thing.x}"
    }

  }

  object Wow extends Wow1

  def printThing(thing: Thing)(implicit S: Show[Thing]): Unit = println(S.show(thing))

  printThing(Thing(42))

  /*
   * Finally, some tips on how to debug what implicits are actually being used in your code:
   * https://medium.com/virtuslab/debugging-implicits-2666b38ae415
   *
   * (although these days IntelliJ can tell you the same thing)
   */

}

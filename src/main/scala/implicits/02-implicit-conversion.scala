
object implicitconversion {

  /*
   * Implicit conversion means using an `implicit def` to convert from one type
   * to another.
   */

  case class Foo(a: Int, b: String)
  case class Bar(x: Int, y: String)

  implicit def foo2bar(foo: Foo): Bar = Bar(foo.a, foo.b)

  /*
   * When you pass a `Foo` to a function that expects a `Bar`, the compiler
   * will search for a function that can do the necessary conversion.
   */

  def doStuff(bar: Bar): Unit = println(bar)

  doStuff(Foo(123, "hello")) // compiles thanks to foo2bar

  /*
   * BEST PRACTICES:
   *
   * In general, DON'T DO THIS!
   *
   * When people complain about implicits being confusing, this is usually what
   * they are talking about.
   */

}

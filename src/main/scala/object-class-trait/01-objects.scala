
object objects {

  /*
   * An object is the simplest way of organising your Scala code.
   *
   * It's just a bag of functions and data.
   *
   * It has no constructor and no type parameters.
   */

  object Foo {

    val x = 42

    def greet(name: String): String = s"Hello, $name!"

    def addOne(n: Int): Int = n + 1

  }

  /*
   * Objects can extend one class and/or one or more traits.
   *
   * Warning: it's tempting to mixin a lot of traits but it should be avoided.
   * It can make your code very hard to follow, because the members referenced
   * in the object could come from any of the mixed in traits. Mixing in a lot
   * of traits is a symptom of the 'cake pattern', which used to be popular a
   * few years ago but has fallen out of favour.
   *
   */

  trait TraitOne
  trait TraitTwo

  class MyClass

  object Bar extends MyClass with TraitOne with TraitTwo

  /*
   * Objects cannot be extended.
   *
   * object A
   * object B extends A // does not compile
   */

  /*
   * To make an entrypoint into a Scala application, you use an object that
   * extends the scala.App trait.
   */
  object Main extends App {
    println("I'm running!")
  }

  /*
   * WHEN TO USE AN OBJECT:
   *
   * Using the principle of least power, start with an object by default.
   *
   * If you find you are passing the same argument to a lot of its functions,
   * either:
   *
   * - upgrade it to a class so you can pass things to its constructor
   * - or consider the Reader pattern (see the Cats documentation)
   */
}

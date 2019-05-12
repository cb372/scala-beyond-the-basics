
object extensionmethods {

  /*
   * We can use an implicit class to add extra methods to types in 3rd-party libraries.
   */

  implicit class StringOps(string: String) {

    def shout: String = string.toUpperCase

  }

  "hello".shout // "HELLO"

  /*
   * BEST PRACTICES:
   *
   * - By convention, the name of the implicit class should be <the type being extended> + "Ops"
   *
   * - This behaviour can be very confusing to people reading the code unless
   *   you explicitly opt in to the extension methods somewhere near the call
   *   site. This is usually done using a so-called "syntax import" (see
   *   below).
   */

  object syntax {

    implicit class IntOps(x: Int) {
      def plusOne: Int = x + 1
    }

  }

  // meanwhile, at the callsite somewhere in another package...

  val x = 42

  //x.plusOne does not compile

  import syntax._ // opt in to the extension methods

  x.plusOne // compiles

}

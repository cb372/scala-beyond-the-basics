
object implicitparams {

  /*
   * Implicit parameters are a convenient way to avoid explicitly passing the same parameter all the way down a call stack.
   *
   * They are often used for passing some kind of 'context', e.g. an ExecutionContext or some information about the HTTP request being processed.
   */

  case class User(id: Int, name: String)

  trait UsersAPI { def findUserByName(name: String, traceToken: String): Option[User] }
  val usersAPI: UsersAPI = ???

  case class RequestContext(traceToken: String)

  def lookupUser(name: String)(implicit rc: RequestContext): Option[User] = {
    if (name.isEmpty) {
      logError("name cannot be empty")
      None
    } else {
      callUserService(name)
    }
  }

  def callUserService(name: String)(implicit rc: RequestContext): Option[User] =
    usersAPI.findUserByName(name, rc.traceToken)

  def logError(message: String)(implicit rc: RequestContext): Unit =
    println(s"$message (trace token = ${rc.traceToken}")

  /*
   * The value is defined using the `implicit` keyword, then automitically
   * passed as a parameter wherever it is needed.
   */

  {
    implicit val requestContext: RequestContext = RequestContext(traceToken = "abc123")

    lookupUser("Chris")
  }

  /*
   * BEST PRACTICES:
   *
   * - Always add an explicit type annotation when defining an `implicit val` or `implicit def`.
   *
   * - Never take a primitive type (e.g. String, Int) as an implicit parameter.
   *   This will quickly lead to ambiguous implicits and very confusing code.
   *   Instead, wrap the primitive in a well-named case class, like we did
   *   above, wrapping a String into a RequestContext.
   */

}


object traits {

  /*
   * A trait is a way of defining an abstract interface or module.
   *
   * Usually they only define abstract members.
   *
   * Traits can use type parameters to define generic interfaces.
   *
   * Unlike classes, they do not have constructors, so they cannot take
   * parameters.
   */

  trait Foo[A] {

    def createOne(): A

    def combineTwoTogether(x: A, y: A): A

  }

  /*
   * WHEN TO USE A TRAIT:
   *
   * Traits are used to define modules when modularising a large code base.
   *
   * Traits are used as the encoding of type classes.
   *
   * Sealed traits (or sealed abstract classes) are used to define sum types in
   * ADTs.
   */

}


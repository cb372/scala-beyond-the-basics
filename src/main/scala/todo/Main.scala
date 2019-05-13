package todo

import scala.annotation.tailrec
import scala.util.Try
import cats.{Eval, Monad}
import cats.data.{State, StateT}

case class Item(value: String, done: Boolean)

object Main extends App {

  type Items = Map[Int, Item]
  type ST[A] = State[Items, A]
  val M = Monad[ST]

  def eval[A](effect: => A): ST[A] =
    StateT.liftF(Eval.always(effect))

  def step: ST[Unit] = {
    for {
      command <- eval(scala.io.StdIn.readLine("Command? (list/add buy milk/done 42) "))
      _ <- handleCommand(command)
    } yield ()
  }

  def handleCommand(command: String): ST[Unit] = command match {
    case "list" =>
      listItems
    case command if command.startsWith("add") =>
      addItem(command.substring(4))
    case command if command.startsWith("done") && Try(command.substring(5).toInt).isSuccess =>
      markDone(command.substring(5).toInt)
    case other =>
      eval(println(s"Unrecognised command: $other"))
  }

  def listItems: ST[Unit] = {
    for {
      items <- State.get
      _ <- eval {
        items.toList.sortBy(_._1).foreach { case (id, Item(value, done)) =>
          val status = if (done) "DONE" else "TODO"
          println(s"$id: [$status] $value")
        }
      }
    } yield ()
  }

  def addItem(value: String): ST[Unit] = {
    val item = Item(value, done = false)
    for {
      _ <- State.modify[Items](items => items + (items.size + 1 -> item))
      _ <- eval(println("Added"))
    } yield ()
  }

  def markDone(id: Int): ST[Unit] = {
    for {
      items <- State.get
      _ <- markDoneOrPrintError(id, items.get(id))
    } yield ()
  }

  private def markDoneOrPrintError(id: Int, maybeItem: Option[Item]): ST[Unit] = maybeItem match {
    case Some(item) =>
      for {
        _ <- State.modify[Items](items => items + (id -> item.copy(done = true)))
        _ <- eval(println("Marked as done"))
      } yield ()
    case None =>
      eval(println(s"No item with ID $id"))
  }

  M.whileM_(M.pure(true))(step).run(Map.empty).value

}

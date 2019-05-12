package todo

import scala.annotation.tailrec

case class Item(value: String, done: Boolean)

object Main extends App {

  var items: Map[Int, Item] = Map.empty

  @tailrec
  def loop(): Unit = {
    scala.io.StdIn.readLine("Command? (list/add buy milk/done 42) ") match {
      case "list" =>
        items.toList.sortBy(_._1).foreach { case (id, Item(value, done)) =>
          val status = if (done) "DONE" else "TODO"
          println(s"$id: [$status] $value")
        }
        println()
        loop()
      case command if command.startsWith("add") =>
        val value = command.substring(4)
        items = items + (items.size + 1 -> Item(value, done = false))
        println("Added")
        println()
        loop()
      case command if command.startsWith("done") =>
        val id = command.substring(5).toInt
        items.get(id) match {
          case Some(item) =>
            items = items + (id -> item.copy(done = true))
            println("Marked as done")
          case None =>
            println(s"No item with ID $id")
        }
        println()
        loop()
      case other =>
        println(s"Unrecognised command: $other")
        println()
        loop()
    }

  }

  loop()
}

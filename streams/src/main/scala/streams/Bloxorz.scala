package streams

import scala.sys.process.Process

/**
 * A main object that can be used to execute the Bloxorz solver
 */
object Bloxorz extends App {
  def sendMove(move: Move) = {
    val command = s"xdotool search --name bloxorz windowactivate key --delay 100 --clearmodifiers $move"
    val process = Process(command)
    process.!
    Thread.sleep(200)
    //		println("sent " + move)
  }

  def sendSolution(level: Level) = {
    val solution = level.solution
    println("Found solution with " + solution.length + " moves")
    for ((move, index) <- solution zipWithIndex) {
      println(s"$index: $move")
      sendMove(move)
    }
  }
  
  sendSolution(Level23Part2)
}

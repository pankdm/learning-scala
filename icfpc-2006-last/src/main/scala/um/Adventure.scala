package um

import scala.runtime.StopException
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.File

class AdventureSolver {

  val pipe = Config.home + "pipe1"

  //var umInput = new PipeReader(pipe)

  var umInput = new BufferedConsole()
  var umOutput = new ConsoleOutput()
  var um: UM = null

  def run = {
    var array = RunMachine.getUMCode
    println("Loading..")
    um = new UM(array, umInput, umOutput)

    //login("yang", "U+262F")
    //login("ftd", "falderal90")
    //login("howie", "xyzzy")
    //downloadMarbles
    adventure

    continue
  }

  def login(user: String, password: String) = {
    send(user)
    send(password)
  }

  def adventure = {
    login("howie", "xyzzy")
//    send("./adventure")
//
//    manual
//    explore(4)
  }
  def explore(numIter: Int) = {
    for (i <- 0 to numIter) {
      val info = send("examine")
      val goRegex = """leads (\w+)""".r
      val itemRegex = """Underneath the (\w+)""".r

      val directions = goRegex.findAllMatchIn(info).map(_.group(1)).toList
      val items = itemRegex.findAllMatchIn(info).map(_.group(1)).toList
      println(items)

      items.foreach(item => {
        send("examine " + item)
      })
      send(directions(0))
    }
  }

  def manual = {
    send("north")
    send("take bolt")
    send("take spring")
    send("inc spring")
    send("take button")
    send("take processor")
    send("take pill")
    send("inc pill")
    send("take radio")
    send("take cache")
    send("combine processor cache")
    send("take blue transistor")
    send("combine radio transistor")
    send("take antenna")
    send("inc antenna")
    send("take screw")
    send("take motherboard")
    send("combine motherboard screw")
    send("take A-1920-IXB")
    send("combine A-1920-IXB processor")
    send("combine A-1920-IXB bolt")
    send("take transistor")
    send("take keypad")
    send("combine keypad button")
    send("combine A-1920-IXB radio")
    send("combine A-1920-IXB transistor")
    send("combine A-1920-IXB motherboard")
    send("combine keypad motherboard")
    send("south")
    send("use keypad")
  }

  def continue = {
    while (true) {
      um.executeOne
    }
  }

  def send(cmd: String): Array[Char] = {
    println("sending " + cmd)
    umInput.buffer.appendAll(cmd + "\n")
    umOutput.clear
    um.executeUntilInput
    umOutput.getData
  }
}

object Adventure extends App {
  override def main(args: Array[String]) {
    var solver = new AdventureSolver()
    try {
      solver.run
    } catch {
      case e: StopException => {
        println("Program halted")
      }
    }
  }

}
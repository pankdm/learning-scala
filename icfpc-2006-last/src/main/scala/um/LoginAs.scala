package um

import scala.runtime.StopException
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.File

class UMLauncher {

  val pipe = Config.home + "pipe1"

  var umInput = new PipeReader(pipe)

  //var umInput = new BufferedConsole()
  var umOutput = new ConsoleOutput()
  var um: UM = null

  def run = {
    var array = RunMachine.getUMCode
    println("Loading..")
    um = new UM(array, umInput, umOutput)

    login("yang", "U+262F")
    //login("ftd", "falderal90")
    //login("howie", "xyzzy")
    //downloadMarbles
    //adventure
    //login("hmonk", "COMEFROM")
    continue
  }

  def login(user: String, password: String) = {
    send(user)
    send(password)
  }

  def adventure = {
    login("howie", "xyzzy")
    send("./adventure")
    send("n")

  }

  def continue = {
    while (true) {
      um.executeOne
    }
  }

  def downloadMarbles = {
    send("bbarker")
    send("plinko")
    send("mail")

    val names = MarbleSolver3.getMarbleNames
    names.foreach(num => {
      send("./bk_specs")
      var info = send(num)
      val fileName = Config.home + "marbles/" + num
      var file = new PrintWriter(new File(fileName))
      file.write(info)
      file.close()
    })
  }

  def send(cmd: String): Array[Char] = {
    println("sending " + cmd)
    umInput.buffer.appendAll(cmd + "\n")
    umOutput.clear
    um.executeUntilInput
    umOutput.getData
  }
}

object Scrolls extends App {
  override def main(args: Array[String]) {
    var umLauncher = new UMLauncher()
    try {
      umLauncher.run
    } catch {
      case e: StopException => {
        println("Program halted")
      }
    }
  }

}
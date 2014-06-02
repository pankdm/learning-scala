package um

import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import java.io.FileInputStream
import java.io.DataInputStream
import scala.runtime.StopException
import java.io.BufferedInputStream

object Config {
  val home = "/home/pankdm/programming/icfpc/2006/"
  val codex = home + "codex.umz"
  val sandmark = home + "sandmark.umz"
  val inside = home + "inside.um"
}

object RunMachine extends App {

  override def main(args: Array[String]) {
    println(System.getProperty("user.dir"))

    var dis = getStream(Config.sandmark)
    //var dis = getStreamForUM(Config.inside)
    run(dis)
  }

  def getUMCode = {
    var input = getStreamForUM(Config.inside)
    var array = new ArrayBuffer[Long]()
    while (input.available() != 0) {
      val x = input.readInt()
      array.append(UM.mod32(x))
    }
    array.toArray
  }
  
  def getStream(name: String): DataInputStream = {
    val fileInput = new FileInputStream(name)
    new DataInputStream(fileInput)
  }

  def getStreamForUM(name: String): DataInputStream = {
    val fileInput = new BufferedInputStream(new FileInputStream(name))
    var dis = new DataInputStream(fileInput)
    var waitColon = false
    var stop = false
    var read = new ArrayBuffer[Char]()
    while (!stop) {
      val c = dis.readByte().toChar
      //println("char " + c + " was read")
      read.append(c)
      if (c == '?') {
        waitColon = true
      }
      if (waitColon && c == ':') {
        stop = true
      }
    }
    //println("read:" + (read mkString))
    println("please wait for the system to be loaded")
    dis
  }

  def run(input: DataInputStream) = {
    var array = new ArrayBuffer[Long]()
    while (input.available() != 0) {
      val x = input.readInt()
      array.append(UM.mod32(x))
    }

    val pipe = Config.home + "pipe1"

    var umInput = new PipeReader(pipe)
    //var umInput = new BufferedConsole();

    var umOutput = new ConsoleOutput();

    var um = new UM(array.toArray, umInput, umOutput)

    try {
      while (true) {
        um.executeOne
      }
    } catch {
      case e: StopException => {
        println("Program halted")
      }

    }
  }

}
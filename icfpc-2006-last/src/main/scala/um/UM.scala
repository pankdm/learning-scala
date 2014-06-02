package um

import UM.Platter
import UM.mod32
import scala.collection.mutable.HashMap
import scala.runtime.StopException
import java.io.FileOutputStream
import scala.collection.mutable.ArrayBuffer
import java.io.FileReader
import java.io.BufferedReader
import java.io.PrintWriter
import java.io.File
import scala.collection.mutable.Queue

object UM {
  type Platter = Long

  def mod32(value: Long): Long = {
    value & 0xffffffffL
  }
}

class PipeWriter(val pipe: String) {
  def send(cmd: String) = {
    var output = new PrintWriter(new File(pipe))
    output.write(cmd + "\n")
    output.flush()
    output.close()
  }
}

abstract class UMOutput {
  def putChar(c: Char): Unit
}

class ConsoleOutput extends UMOutput {
  var buffer = new ArrayBuffer[Char]();
  def putChar(c: Char) = {
    buffer.append(c);
    Console.print(c)
  }

  def clear = buffer.clear
  def getData = buffer.toArray
}

abstract class UMInput {
  def getChar: Char
  def hasNext: Boolean
}

class BufferedConsole extends UMInput {
  var buffer = new ArrayBuffer[Char]()
  var index = 0

  def hasNext = index < buffer.size

  def getChar: Char = {
    if (!hasNext) {
      Console.print(":> ")
      val s = Console.readLine + "\n"
      buffer.clear
      buffer.appendAll(s.getBytes().map(_ & 0xFF).map(_.toChar))
      index = 0
    }
    val c = buffer(index)
    index += 1
    c
  }
}

class PipeReader(val pipe: String) extends UMInput {
  var buffer = new ArrayBuffer[Char]()
  var index = 0

  def hasNext = index < buffer.size

  def readFromPipe: Boolean = {
    var reader = new BufferedReader(new FileReader(pipe))
    var s = ""
    var finished = false
    var read = false
    while (!finished) {
      var s = reader.readLine()
      if (s == null) {
        finished = true
      } else {
        buffer.appendAll(s + "\n")
        println(" < " + s)
        read = true
      }
    }
    read
  }

  def getChar: Char = {
    if (!hasNext) {
      buffer.clear
      index = 0
      while (!readFromPipe) {}
    }
    val c = buffer(index)
    index += 1
    c
  }

}

class UM(program: Array[Platter], var input: UMInput, var output: UMOutput) {
  // State
  var r = Array.fill[Platter](8)(0)
  //var arrays = new HashMap[Platter, Array[Platter]]()
  var arrays = new ArrayBuffer[Array[Platter]]()
  arrays append program
  var main = program
  var available = new Queue[Int]()
  var finger = 0

  // Logging
  var buffer = new Array[Byte](1)
  val logFile = "logs/log-" + System.currentTimeMillis() + ".txt"
  var log = new FileOutputStream(logFile);

  // Operators
  object OperatorType extends Enumeration {
    type OperatorType = Value
    // Do not change the order!
    val ConditionalMove, ArrayIndex, ArrayAmend, Addition, Multiplication, Division, NotAnd, Halt, Allocation, Abandon, Output, Input, LoadProgram, Orthography = Value
  }
  import OperatorType._

  val INVALID_VALUE = -1
  class Operator(
    val operatorType: OperatorType,
    val a: Int,
    val b: Int,
    val c: Int = INVALID_VALUE) {

    def sr(index: Int) = {
      if (0 <= index && index < 8) r(index)
      else index
    }
    
    override def toString = {
      s"Operator[type=$operatorType, a = ${sr(a)}, b = ${sr(b)}, c = ${sr(c)}]"
    }

    def doExecute = operatorType match {
      case ConditionalMove => if (r(c) != 0) r(a) = r(b)
      case ArrayIndex => r(a) = arrays(r(b).toInt)(r(c).toInt)
      case ArrayAmend => arrays(r(a).toInt)(r(b).toInt) = r(c)
      case Addition => r(a) = mod32(r(b) + r(c))
      case Multiplication => r(a) = mod32(r(b) * r(c))
      case Division => r(a) = mod32(r(b) / r(c))
      case NotAnd => r(a) = (r(b) & r(c)) ^ 0xffffffffL
      case Halt => {
        log.close()
        throw new StopException()
      }
      case Allocation => {
        val size = r(c).toInt
        val memory = Array.fill(size)(0l)
        if (available.isEmpty) {
          r(b) = arrays.length
          arrays.append(memory)
        } else {
          r(b) = available.dequeue
          arrays update (r(b).toInt, memory)
        }
      }
      case Abandon => {
        val index = r(c).toInt
        arrays.update(index, null)
        available.enqueue(index)
      }
      case Output => {
        assert(0 <= r(c) && r(c) <= 255)
        output.putChar(r(c).toChar);
        buffer(0) = r(c).toByte
        log.write(buffer)
        //        print(r(c).toChar)
      }
      case Input => {
        val char = input.getChar
        //println(char.toInt + " was read")
        r(c) = char
        assert(0 <= r(c) && r(c) <= 255);
      }
      case LoadProgram => {
        if (r(b) != 0) {
          var newArray = arrays(r(b).toInt).clone
          arrays update (0, newArray)
        }
        main = arrays(r(b).toInt)
        finger = r(c).toInt
      }
      case Orthography => r(a) = b
    }

    def execute = {
      doExecute
    }
  }

  def executeUntilInput = {
    var stop = false
    while (!stop) {
      val operator = peekNextOperator
      if (operator.operatorType == Input && !input.hasNext) {
        stop = true
      } else {
        advanceFinger
        operator.execute
      }
    }
  }

  def executeOne = {
    val operator = peekNextOperator
    advanceFinger
    operator.execute
  }

  def advanceFinger = finger += 1

  def peekNextOperator: Operator = {
    def extractNumFast(value: Platter, startByte: Int, byteCount: Int): Int = {
      (value >> startByte & ((1l << byteCount) - 1)).toInt
    }

    val value = main(finger)
    //println("current value is " + value)
    val index = extractNumFast(value, 28, 4)
    val operatorType = OperatorType(index)
    if (operatorType != OperatorType.Orthography) {
      val c = extractNumFast(value, 0, 3)
      val b = extractNumFast(value, 3, 3)
      val a = extractNumFast(value, 6, 3)
      new Operator(operatorType, a, b, c)
    } else {
      val a = extractNumFast(value, 25, 3)
      val b = extractNumFast(value, 0, 25)
      new Operator(operatorType, a, b)
    }
  }
}
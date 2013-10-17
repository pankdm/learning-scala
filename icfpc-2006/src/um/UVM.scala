package um

import scala.collection.mutable.HashMap
import java.util.concurrent.locks.Condition

class UVM(program: Array[UInt]) {
  // State
  var r = Array.fill(8)(UInt(0))

  def getr(x: Int): String = {
    if (0 <= x && x < 8) r(x).toString
    else "None"
  }

  var arrays = new HashMap[UInt, Array[UInt]]()
  arrays update (0, program)
  var finger = 0
  var fingerArray = 0
  var lastId = 0

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

    def mod(value: Long): Int = {
      (value & 0xffffffffL).toInt
    }

    def not(x: UInt): UInt = {
      UInt((x.toLong ^ 0xffffffffL).toInt)
    }

    override def toString = {
      "Operator[type = " + operatorType + ", a = " + getr(a) + ", b = " + getr(b) + ", c = " + getr(c) + "]"
    }

    def doExecute = operatorType match {
      case ConditionalMove => if (r(c) != 0) r(a) = r(b)
      case ArrayIndex => r(a) = arrays(r(b))(r(c).toInt)
      case ArrayAmend => arrays(r(a))(r(b).toInt) = r(c)
      case Addition => r(a) = r(b) + r(c)
      case Multiplication => r(a) = r(b) * r(c)
      case Division => r(a) = r(b) / r(c)
      case NotAnd => r(a) = not(r(b) & r(c))
      case Halt => ???
      case Allocation => {
        lastId += 1
        assert(!(arrays contains lastId))
        val size = r(c).toInt
        if (size > 1000) {
          println("allocating with size = " + size)
        }
        arrays update (lastId, Array.fill(size)(0))

        r(b) = lastId
      }
      case Abandon => arrays remove r(c)
      case Output => print(r(c).toChar)
      case Input => {
        val char = 	Console.in.read.toChar
        println(char + " was read")
        r(c) = char
      }
      case LoadProgram => {
        if (r(b) != 0) {
          val size = arrays(r(b)).size
          var newArray = new Array[UInt](size)

          for (i <- 0 until size) {
            newArray(i) = new UInt(arrays(r(b))(i).toInt)
          }
          if (size > 1000) {
            println("loading program of size " + size)
          }

          arrays update (0, newArray)
        }
        fingerArray = r(b).toInt
        finger = r(c).toInt
      }
      case Orthography => r(a) = b
    }

    def execute = {
      doExecute
      println("executed " + this)
    }
  }

  // Execution
  def extractNum(value: UInt, startByte: Int, byteCount: Int): Int = {
    var res = 0
    for (byte <- 0 until byteCount) {
      val bit = (1 << (byte + startByte))
      if ((value & bit) > 0) res |= (1 << byte)
    }
    res
  }

  def executeNext = {
    val operator = readNextOperator
    execute(operator)
  }

  def execute(operator: Operator) = {
    operator.execute
  }

  def readNextOperator: Operator = {
    val value = arrays(fingerArray)(finger)
    finger += 1

    println("current value is " + value)
    val index = extractNum(value, 28, 4)
    val operatorType = OperatorType(index)
    if (operatorType != OperatorType.Orthography) {
      val c = extractNum(value, 0, 3)
      val b = extractNum(value, 3, 3)
      val a = extractNum(value, 6, 3)
      new Operator(operatorType, a, b, c)
    } else {
      val a = extractNum(value, 25, 3)
      val b = extractNum(value, 0, 25)
      new Operator(operatorType, a, b)
    }
  }
}
package um

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.ArrayBuffer

import scala.util.control.Breaks._

// TODO: write better execution (signed vs unsigned)

class Balance {
  // TODO: move constants to better place
  val MATH = 1
  val LOGIC = 2
  val SCIENCE = 0
  val PHYSICS = 3

  abstract class BalanceSolver {
    var solution = new ListBuffer[String]()
    val name: String

    // commands
    abstract class Command {
      def execute: Unit
    }
    class Math(val d: Int, s1: Int, s2: Int) extends Command {
      def execute = {
        println("MATH " + d + " " + s1 + " " + s2)
        val minus = M(sR(s1 + 1)) - M(sR(s2 + 1))
        println(s"M [${dR(d + 1)}] = ${M(sR(s1 + 1)) } - ${M(sR(s2 + 1))}")
        println(s"M [${dR(d + 1)}] = M [${sR(s1 + 1)}] - M [${sR(s2 + 1)}]")
        M(dR(d + 1)) = minus

        val plus = M(sR(s1)) + M(sR(s2))
        println(s"M [${dR(d)}] = ${M(sR(s1)) } + ${M(sR(s2))}")
        println(s"M [${dR(d)}] = M [${sR(s1)}] + M [${sR(s2)}]")
        M(dR(d)) = plus
      }
    }
    class Logic(val d: Int, s1: Int, s2: Int) extends Command {
      def execute = {
        val xor = M(sR(s1 + 1)) ^ M(sR(s2 + 1))
        M(dR(d + 1)) = xor

        val and = M(sR(s1)) & M(sR(s2))
        M(dR(d)) = and
      }
    }
    class Physics(val imm: Int) extends Command {
      def execute = {
        println("PHYSICS " + imm)
        sourceReg(0) += imm
        sourceReg(0) %= 256
        if (sourceReg(0) < 0) {
          sourceReg(0) += 256
        }
        var indexes = new ArrayBuffer[Int]()
        for (bit <- 0 to 4) {
          if ((imm & (1 << bit)) != 0) {
            indexes.append(bit)
          }
        }
        indexes.append(5)
        val sr0 = sR(0)
        for (i <- (indexes.size - 1) to 0 by -1) {
          var value = -1
          val bit1 = indexes(i)
          if (i > 0) {
            val bit2 = indexes(i - 1)
            value = if (bit2 < 2) dR(1 - bit2) else sR(3 - (bit2 - 2))
          } else {
            value = sr0
          }
          if (bit1 < 2) {
            dstReg(1 - bit1) = value
          } else {
            sourceReg(3 - (bit1 - 2)) = value
          }
        }
      }
    }
    class Science(val imm: Int) extends Command {
      def execute = {
        println("SCIENCE " + imm)
        println("at " + sR(0) + " = " + M(sR(0)))
        if (M(sR(0)) != 0) {
          speed = imm
          if (imm == 0) {
            terminated = true
          }
        }
      }
    }

    // emulation
    var M = Array.fill(256)(0)
    var sourceReg = Array.fill(4)(0)
    var dstReg = Array.fill(2)(0)
    var terminated = false
    var code = ArrayBuffer[Command]()
    var speed = 1
    var pointer = 0

    def sR(index: Int) = {
      sourceReg(if (index < 4) index else index - 4)
    }
    def dR(index: Int) = {
      dstReg(if (index < 2) index else index - 2)
    }

    def dump = {
      println("M: " + (M take 30 mkString " "))
      print("S: " + (sourceReg mkString " ") + ", ")
      println("R: " + (dstReg mkString " "))
      println("pointer = " + pointer)
      println()
    }

    def addToSolution(data: Int) = {
      var hex = data.toHexString
      if (hex.length() < 2) hex = "0" + hex
      println("data of " + data + " is " + hex)
      solution.append(hex)
    }

    def put(opCode: Int, d: Int, s1: Int, s2: Int) = {
      assert(d < 2)
      assert(s1 < 4)
      assert(s2 < 4)
      addToSolution((opCode << 5) + (d << 4) + (s1 << 2) + s2)

      var cmd: Command = null
      if (opCode == MATH) {
        cmd = new Math(d, s1, s2)
      }
      if (opCode == LOGIC) {
        cmd = new Logic(d, s1, s2)
      }
      code.append(cmd)
      cmd.execute
      dump
    }

    def put(opCode: Int, imm: Int) = {
      assert(imm < 32)
      var signedImm = imm;
      if (signedImm >= 16) {
        signedImm -= 32
      }
      var unsignedImm = imm
      if (unsignedImm < 0) {
        unsignedImm += 32
      }
      addToSolution((opCode << 5) + unsignedImm)

      var cmd: Command = null
      if (opCode == SCIENCE) {
        cmd = new Science(signedImm)
      }
      if (opCode == PHYSICS) {
        cmd = new Physics(signedImm)
      }
      code.append(cmd)
      cmd.execute
      dump
    }

    def solve: Unit
    def check = {}

    def trace = {
      var iterLimit = 300
      var numCycles = 30
      breakable {
        while (!terminated) {
          val cmd = code(pointer)
          cmd.execute
          pointer += speed
          if (pointer >= code.length) {
            numCycles -= 1
            if (numCycles == 0) break
          }
          pointer %= code.length
          iterLimit -= 1
          if (iterLimit == 0) {
            break
          }
          dump
          val s = Console.readLine
        }
      }
    }

    def run: String = {
      solve
      //trace
      check
      assert(terminated == true, "\nProgram wasn't terminated")
      val result = solution mkString ""
      println("solution = " + result)
      result
    }
  }

  class PuzzleStop extends BalanceSolver {
    val name = "stop"
    def solve = {
      put(PHYSICS, 1)
      put(SCIENCE, 0)
    }
  }

  class PuzzleStop1 extends BalanceSolver {
    val name = "stop1"
    def solve = {
      put(PHYSICS, 1)
      put(SCIENCE, 0)
    }
  }

  class PuzzleStop127 extends BalanceSolver {
    val name = "stop127"
    def solve = {
      M(127) = 1
      put(PHYSICS, 1)
      put(SCIENCE, 0)
    }
  }

  class PuzzleStop128 extends BalanceSolver {
    val name = "stop128"
    def solve = {
      put(PHYSICS, 1)
      put(SCIENCE, 0)
    }
  }

  class PuzzleCopyMem extends BalanceSolver {
    val name = "copymem"
    val a = 5
    def solve = {
      M(0) = a
      M(1) = 1
      
      put(PHYSICS, 1)

      put(PHYSICS, 8)
      put(PHYSICS, 8)
      put(PHYSICS, -7)
      
      put(PHYSICS, 1)
      put(PHYSICS, 1)
      put(MATH, 0, 2, 0)
      put(PHYSICS, 1)
      
      put(SCIENCE, 1) // <-- 1
      
      put(PHYSICS, 1)
      put(MATH, 0, 3, 1)
      put(PHYSICS, 1)

      
      put(SCIENCE, -4) // jump to 1
      put(PHYSICS, -2)
      put(PHYSICS, -1)
      put(SCIENCE, 0) // stop
    }
    override def check = {
      assert(sR(3) == a)
    }
  }

  class PuzzleSwapMem extends BalanceSolver {
    val name = "swapmem"
    def solve = {
      sourceReg = Array(0, 1, 2, 3)
      dstReg = Array(4, 5)
      for (i <- 0 to 7) {
        M(i) = 1 << i
      }
      put(PHYSICS, 16)
      put(PHYSICS, 1)
      put(MATH, 0, 0, 0)
      put(PHYSICS, 1)
      put(PHYSICS, 3)
      put(MATH, 0, 3, 3)
      put(SCIENCE, 0)

      assert(M(4) == (1 << 6))
      assert(M(6) == (1 << 4))
    }
  }

  class PuzzleSwapReg extends BalanceSolver {
    val name = "swapreg"
    def solve = {
      sourceReg = Array(0, 1, 2, 3)
      dstReg = Array(4, 5)
      M = Array.fill(256)(1)

      put(PHYSICS, 3)
      put(PHYSICS, 4)
      put(PHYSICS, 1)
      put(SCIENCE, 0)

      assert(dstReg(0) == 5)
      assert(dstReg(1) == 4)
    }
  }

  class PuzzleSwapReg2 extends BalanceSolver {
    val name = "swapreg2"
    val a = 10
    val y = 60
    def solve = {
      sourceReg = Array(a, 20, 30, 40)
      dstReg = Array(50, y)
      M = Array.fill(256)(1)
      
      put(PHYSICS, -1)
      put(PHYSICS, 1)
      put(PHYSICS, 1)
      put(PHYSICS, 2)
      put(SCIENCE, 0)
    }
   override def check = {
     assert(sourceReg(0) == y)
     assert(dstReg(1) == a)
   }
  }

  class PuzzleAddMem extends BalanceSolver {
    val name = "addmem"
    val a = 45
    val b = 33

    def solve = {
      sourceReg = Array(0, 1, 2, 3)
      dstReg = Array(4, 5)
      M(0) = a
      M(1) = b

      put(PHYSICS, 2)
      put(PHYSICS, -4)
      put(MATH, 0, 3, 0)
      put(SCIENCE, 0)

    }
    override def check = {
      assert(M(2) == a + b)
    }
  }

  class PuzzleAddMem2 extends BalanceSolver {
    val name = "addmem2"
    val a = 45
    val b = 33

    def solve = {
      sourceReg = Array(0, 1, 2, 3)
      dstReg = Array(4, 5)
      M(0) = a
      M(1) = b

      put(PHYSICS, 2)
      put(PHYSICS, -4)
      put(MATH, 0, 3, 0)
      put(LOGIC, 0, 1, 1)
      put(SCIENCE, 0)
    }
    override def check = {
      assert(M(0) == a)
      assert(M(1) == b)
      assert(M(2) == a + b)
      assert((M drop 3).forall(_ == 0))
    }
  }

  class PuzzleClearReg extends BalanceSolver {
    val name = "clearreg"
    def solve = {
      sourceReg = Array(0, 1, 2, 3)
      dstReg = Array(4, 5)
      M = (0 to 256).toArray

      put(PHYSICS, -10)
      put(PHYSICS, -1)
      put(PHYSICS, -3)
      put(PHYSICS, -2)
      put(PHYSICS, -4)
      put(PHYSICS, 10)
      put(MATH, 0, 0, 0)
      put(PHYSICS, -5)
      put(SCIENCE, 0)
    }
    override def check = {
      assert(sourceReg.forall(_ == 0))
      assert(dstReg.forall(_ == 0))
    }
  }

  var output = new PipeWriter(Config.home + "pipe1")

  def sendPuzzle(puzzle: BalanceSolver) {
    val name = puzzle.name
    val fileName = name + ".txt"
    val solution = puzzle.run
    output.send("rm " + fileName)
    output.send("")
    output.send("/bin/umodem " + fileName + " :::")
    output.send(solution)
    output.send(":::")
    //
    //        output.send("./step_balance " + name + " " + fileName)
    //        for (i <- 1 to 10) {
    //          output.send("")
    //        }
    //        
    output.send("./certify " + name + " " + fileName)
  }

  def runAll = {
    val puzzle = new PuzzleSwapReg2()
    //puzzle.run
    sendPuzzle(puzzle)
  }
}
object Balance extends App {
  override def main(args: Array[String]) {
    val balance = new Balance
    balance.runAll
    //var output = new PipeWriter(Config.home + "pipe1")
  }
}



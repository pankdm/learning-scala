package um

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import java.io.PrintWriter
import java.io.File

object MarbleSolver3 extends App {

  def getMarbleNames = {
    val data =
      """
  000
  010
  020
  030
  040
  050
  100
  200
  300
  400
  500
"""
    data.split("\\s+").filterNot(_.isEmpty)
  }

  override def main(args: Array[String]) {
    //getMarbleNames.foreach(solvePuzzle(_))
    solvePuzzle("020")
  }

  type Solution = ListBuffer[Pair[Int, Int]]

  def solvePuzzle(name: String) = {
    println("solving file " + name)
    val inputFile = Config.home + "marbles/" + name
    var target = new ArrayBuffer[Int]
    var plinks = new ArrayBuffer[Int]

    val input = Source.fromFile(inputFile)

    val pattern = """(\d+) -> \((\d+),(\d+)\)""".r
    input.getLines.foreach(line => {
      val m = pattern.findFirstMatchIn(line)
      if (m != None) {
        val value = m.get
        assert(value.group(1).toInt == target.size)
        target.append(value.group(2).toInt)
        plinks.append(value.group(3).toInt)
      }
    })

    //println(target)
    //println(plinks)

    val solution = solve(target.toArray, plinks.toArray)
    val result = convertToString(solution, target.size)
    println(result)

    val outputFile = Config.home + "marbles-solved/" + name
    val output = new PrintWriter(new File(outputFile))
    output.write(result)
    output.close()
  }

  def convertToString(solution: Solution, numMarbles: Int): String = {
    var result = new ArrayBuffer[String]
    val baseString = Array.fill(numMarbles)('|')

    for ((i, j) <- solution) {
      val currentString = baseString.clone
      currentString(i) = '>'
      currentString(j) = '<'
      result.append(currentString mkString)
    }
    result mkString "\n"
  }

  class Swapper(input: Array[Int]) {
    var current = input.clone
    var solution = new Solution

    def swap(i1: Int, i2: Int) = {
      println("swapping " + i1 + " " + i2)
      solution.append((i1, i2))
      val elem1 = current(i1)
      val elem2 = current(i2)

      current(i2) = elem1
      current(i1) = elem2
    }
  }

  def solve(target: Array[Int], plinks: Array[Int]): Solution = {
    val numMarbles = target.size

    var nSwaps = new Array[Int](numMarbles)
    for (i <- 0 until numMarbles) {
      nSwaps(target(i)) = plinks(i)
    }
    
    var minSwaps = new Array[Int](numMarbles)
    for (i <- 0 until numMarbles) {
      var cnt = 0
      for (j <- i + 1 until numMarbles) {
        if (target(j) < target(i)) {
          cnt += 1
        }
      }
      minSwaps(target(i)) = cnt
    }

    println(minSwaps mkString " ")
    println(nSwaps mkString " ")

    
    var swapper = new Swapper(target)

    var stop = false
    var changed = true
    while (!stop && changed) {
      stop = true
      changed = false
      println()
      println("cur      = " + (swapper.current mkString " "))
      println("nSwaps   = " + (nSwaps mkString " "))
      println("minSwaps = " + (minSwaps mkString " "))
      
      var index = -1
      var best = -1
      for (i <- 0 until numMarbles - 1) {
        val a = swapper.current(i)
        val b = swapper.current(i + 1)
        assert(nSwaps(a) >= minSwaps(a))

        if (nSwaps(a) != 0) {
          stop = false
        }
        if (a > b) {
          changed = true
//          swapper.swap(i, i + 1)

          assert(minSwaps(a) > 0)
//          nSwaps(a) -= 1
//          minSwaps(a) -= 1
          
          val delta = nSwaps(a) - minSwaps(a)
          if (delta > best) {
            index = i
            best = delta
          }
        } else {
          // a < b
          if (minSwaps(b) + 1 <= nSwaps(b) && minSwaps(a) <= nSwaps(a) - 1) {
            changed = true
//            swapper.swap(i, i + 1)
//            minSwaps(b) += 1
//            nSwaps(a) -= 1
            val delta = math.min(nSwaps(a) - minSwaps(a) - 1, nSwaps(b) - minSwaps(b) - 1)
            if (delta > best) {
              index = i
              best = delta
            }
          }
        }
      }
      if (changed) {
        val a = swapper.current(index)
        val b = swapper.current(index + 1)
        if (a > b) {
          swapper.swap(index, index + 1)
          nSwaps(a) -= 1
          minSwaps(a) -= 1
          
        } else {
          swapper.swap(index, index + 1)
          minSwaps(b) += 1
          nSwaps(a) -= 1
        }
      }
    }
    for (i <- 0 until numMarbles) {
      assert (swapper.current(i) == i)
      assert (nSwaps(i) == 0)
    }
    swapper.solution
  }

}
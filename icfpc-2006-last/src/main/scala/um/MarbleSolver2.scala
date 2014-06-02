package um

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashSet

import scala.io.Source
import java.io.PrintWriter
import java.io.File

object MarbleSolver2 extends App {

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
    solvePuzzle("010")
//        val now = Vector(1, 2, 0, 3)
//        val plinks = Vector(1, 5, 1, 3)
//        println(solveInitial(now, plinks))

    //    val state = new State(now, plinks, countMinSwaps(now).toVector)
    //    println(solve(state).get)
    //(4,5,1) (5,1,1) (3,1,0) (6,3,0)
    
  }

  def countMinSwaps(target: Seq[Int]) = {
    val numMarbles = target.size
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
    minSwaps
  }

  def solveInitial(target: Vector[Int], plinks: Vector[Int]): List[Int] = {
    val minSwaps = countMinSwaps(target).toVector
    val state = new State(target, plinks, minSwaps)
    print(state)
    List()
//    var visited = new HashSet[State]()
//    solve(state, visited).get
  }

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
    val realPlinks = new Array[Int](target.size)
    for (i <- 0 until target.size) {
      realPlinks(target(i)) = plinks(i)
    }

    println("target = " + target)
    println("plinks = " + (realPlinks mkString " "))
    val solution = solveInitial(target.toVector, realPlinks.toVector)

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

    for (i <- solution) {
      val currentString = baseString.clone
      currentString(i) = '>'
      currentString(i + 1) = '<'
      result.append(currentString mkString)
    }
    result mkString "\n"
  }

  def changeElem(source: Vector[Int], index: Int, delta: Int) = {
    source.updated(index, source(index) + delta)
  }

  class State(
    val now: Vector[Int],
    val plinks: Vector[Int],
    val minSwaps: Vector[Int]) {
    def swap(i1: Int, i2: Int): State = {
      val leftElem = now(i1)
      val rightElem = now(i2)
      val newTarget = now.updated(i2, leftElem).updated(i1, rightElem)
      val newPlinks = changeElem(plinks, leftElem, -1)
      val newMinSwaps = if (leftElem > rightElem) {
        changeElem(minSwaps, leftElem, -1)
      } else {
        changeElem(minSwaps, rightElem, +1)
      }
      val res = new State(newTarget, newPlinks, newMinSwaps)
      res
    }
    def isBad: Boolean = {
      //      println("checking for badness " + this)
      if ((plinks zip minSwaps).exists(x => x._1 < x._2)) {
        return true
      }
      var start = 0
      for (elem <- 0 until now.length) {
        if (plinks(elem) == 0) {
          val otherPlinks = for (otherElem <- start until elem)
            yield (plinks(otherElem) - minSwaps(otherElem)).toInt
          start = elem + 1
          if (!otherPlinks.isEmpty) {
            val sm = otherPlinks.sum
            val mx = otherPlinks.max
            if (mx > sm - mx) {
              println("bad: " + this)
              return true
            }
          }
        }
      }
      false
    }

    def nextPossibleStates: Seq[Pair[State, Int]] = {
      for {
        i <- 1 until now.length;
        if plinks(now(i - 1)) > 0;
        val next = this.swap(i - 1, i)
        if !next.isBad
      } yield (next, i - 1)
    }

    def isFinal: Boolean = {
      now.zipWithIndex.forall(x => (x._1 == x._2)) &&
        plinks.forall(_ == 0)
    }

    lazy val points = (plinks zip minSwaps).map(x => x._1 - x._2).max
    override def toString = {
      val info = for (t <- now) yield (t, plinks(t), minSwaps(t))
      val delta = for(t <- now) yield (plinks(t) - minSwaps(t))
      (info mkString " ") +  "\ndelta = " + (delta mkString " ")
      
    }
  }

  type Solution = List[Int]

  var positionCount = 0
  def solve(state: State, visited: HashSet[State]): Option[Solution] = {
    visited.add(state)
    positionCount += 1
    println(positionCount + " at state " + state)
    if (state.isFinal) {
      return Some(Nil)
    }
    assert(!state.isBad)
    val nextStates = state.nextPossibleStates
    for ((next, move) <- nextStates.sortBy(_._1.points)) {
      if (!(visited contains next)) {
        val other = solve(next, visited)
        if (other != None) {
          return Some(move :: other.get)
        }
      }
    }
    //    println("not found solution at state " + state)
    None
  }

}
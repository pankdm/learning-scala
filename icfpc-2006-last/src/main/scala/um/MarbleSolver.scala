package um

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Set
import scala.io.Source
import java.io.PrintWriter
import java.io.File

object MarbleSolver extends App {

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
    visited = Set[State]()
    solvePuzzle("020")
  }

  def solvePuzzle(name: String) = {
    val inputFile = Config.home + "marbles/" + name
    val target = new ArrayBuffer[Int]
    val plinks = new ArrayBuffer[Int]

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

    val solution = solve(target.toArray, realPlinks)
  }
  type Solution = ListBuffer[Pair[Int, Int]]

  def countMinSwaps(target: Seq[Int]) = {
    val numMarbles = target.size
    val minSwaps = new Array[Int](numMarbles)
    val solution = new Solution
    for (i <- (numMarbles - 1) to 0 by -1) {
      var cnt = 0
      for (j <- i + 1 until numMarbles) {
        if (target(i) > target(j)) {
          cnt += 1
        }
      }
      for (j <- 0 until cnt) {
        solution prepend ((i - j - 1, i - j))
      }
      minSwaps(target(i)) = cnt
    }
    (minSwaps, solution)
  }

  def convert(solution: DeltaSolution): Solution = ???
  
  def solve(target: Array[Int], plinks: Array[Int]): Solution = {
    val (minSwaps, solution) = countMinSwaps(target)
    for (i <- target) {
      val d = plinks(i) - minSwaps(i)
      println(s"$i: $d ||  ${plinks(i)}, ${minSwaps(i)}")
    }
    println("target   = " + (target mkString " "))
    println("plinks   = " + (plinks mkString " "))
    println("minSwaps = " + (minSwaps mkString " "))
    val debug = target.map(x => (x, plinks(x) - minSwaps(x))).toVector
    println("debug = " + debug)
    val deltas = target.map(x => (plinks(x) - minSwaps(x))).toVector
    println("deltas = " + deltas)
    val preSolution = solveDelta(deltas)
    show(preSolution.get)
    solution
  }

  type State = Vector[Int]
  def isLast(deltas: State) = deltas.forall(_ == 0)

  def show(solution: DeltaSolution) = {
    solution.foreach {
      x => println(x)
    }
  }
  
  type DeltaSolution = List[Move]
  case class Move(val index: Int, val num: Int)

  def nextMoves(deltas: State): Stream[Move] = {
    val firstNonZero = deltas.indexWhere(_ != 0)
    assert(firstNonZero != -1)
    val value = deltas(firstNonZero)
    val toRight = deltas.length - firstNonZero - 1
    val first = (
      for (i <- 1 to math.min(toRight, value))
        yield Move(firstNonZero, i))
    val second = (
      for (i <- (firstNonZero + 1) until deltas.length)
        yield Move(i, -(i - firstNonZero)))
    first.toStream ++ second.toStream
  }

  def dec(state: Option[State], index: Int): Option[State] = state match {
    case None => None
    case Some(x) => {
      val value = x(index)
      if (value == 0) None
      else Some(x updated (index, value - 1))
    }
  }

  def applyMove(state: State, move: Move): Option[State] = move match {
    case Move(index, num) => {
      if (state(index) < num) None
      else {
        if (num == 0) Some(state)
        else {
          val applied = dec(dec(Some(state), index), index + num)
          applied match {
            case None => None
            case Some(x) => applyMove(x, Move(index, num - 1))
          }
        }
      }
    }
  }

  var visited: Set[State] = _
  def solveDelta(state: State): Option[DeltaSolution] = {
    if (isLast(state)) return Some(List())
    visited update (state, true)
    for (move <- nextMoves(state)) {
      val nextState = applyMove(state, move)
      if (nextState != None) {
        if (!(visited contains nextState.get)) {
          val solution = solveDelta(nextState.get)
          if (solution != None) {
            return Some(move :: solution.get)
          }
        }
      }
    }
    None
  }

}
package p321

import scala.collection.immutable.Vector

class Sliding(n: Int) {
  object Cell extends Enumeration {
    type Cell = Value
    val White, Black, Empty = Value
  }
  import Cell._

  type State = Vector[Cell.Value]
  val initialState =
    ((for (i <- 1 to n) yield White) ++
      Vector(Empty) ++
      (for (i <- 1 to n) yield Black)).toVector

  class Move(val from: Int, val to: Int) {
    def change(state: State): State = {
      assert(state(to) == Empty)
      assert(from != to)
      assert(math.abs(from - to) <= 2)
      state updated (to, state(from)) updated (from, state(to))
    }
  }

  def possibleMoves(state: State): List[Move] = {
    val empty = state indexOf Empty
    (for (
      from <- (0 max empty - 2) to ((state.length - 1) min empty + 2);
      if from != empty
    ) yield new Move(from, empty)).toList
  }
  
  def onlyGoodMoves(state: State): List[Move] = {
    (for (
    	move <- possibleMoves(state)
    	if ((move.from < move.to && state(move.from) == White) ||
    		(move.from > move.to && state(move.from) == Black))
    	  ) yield move).toList
  }
  
  class Path(val state: State, val numSteps: Int) {
    def extend(move: Move) = new Path(move change state, numSteps + 1)
    override def toString = (state.toString + " ") + "--> " + numSteps
  }
  
  def from(paths: Set[Path], explored: Set[State]): Stream[Set[Path]] = {
    if (paths.isEmpty) Stream.empty
    else {
      val more = for {
        path <- paths
        next <- onlyGoodMoves(path.state) map path.extend
        if !(explored contains next.state)
      } yield next
      paths #:: from(more, explored ++ (more map (_.state)))
    }
  }

  def initialPath = new Path(initialState, 0)  
  val pathSets = from(Set(initialPath), Set(initialState))
  
  def solution(targetState: State): Stream[Path] = {
     for {
      pathSet <- pathSets
      path <- pathSet
      if path.state equals targetState
    } yield path
  }
  
  val targetState = initialState.reverse
  
}
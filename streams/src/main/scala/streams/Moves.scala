package streams

/**
 * In Bloxorz, we can move left, right, Up or down.
 * These moves are encoded as case objects.
 */
sealed abstract class Move
case object Left extends Move
case object Right extends Move
case object Up extends Move
case object Down extends Move


// TODO: move to proper place (or rename)
sealed abstract class Cell
case object Hole extends Cell
case object Tile extends Cell
case object FragileTile extends Cell
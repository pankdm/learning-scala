package um

import scala.collection.mutable.ListBuffer

class Module2D(val name: String) {
  val XSIZE = 200
  val YSIZE = 200

  var f = Array.fill[Char](YSIZE, XSIZE)(' ')

  def setTitle(y: Int, x: Int) = {
    // add name
    for ((char, index) <- name zipWithIndex) {
      f(y + 1)(x + 1 + index) = char
    }
  }

  def addBox(cmd: String, y: Int, x: Int) = {
    val len = cmd.length + 2
    val xLast = x + len - 1
    // add border
    for (y0 <- List(y, y + 2)) {
      for (x0 <- (x + 1) until xLast) {
        f(y0)(x0) = '='
      }
      f(y0)(x) = '*'
      f(y0)(xLast) = '*'
    }
    f(y + 1)(x) = '!'
    f(y + 1)(xLast) = '!'

    // add command
    for ((char, index) <- cmd zipWithIndex) {
      f(y + 1)(x + 1 + index) = char
    }
  }
  def connect(y1: Int, x1: Int, y2: Int, x2: Int) = {
    
  }

  def toText: String = {
    // add border
    val cross = for { y <- 0 until f.length; x <- 0 until f(0).length } yield (y, x)
    val nonEmpty = cross.filter(p => f(p._1)(p._2) != ' ')
    val xMin = nonEmpty.map(_._2).min - 1
    val xMax = nonEmpty.map(_._2).max + 1
    val yMin = nonEmpty.map(_._1).min - 1
    val yMax = nonEmpty.map(_._1).max + 1
    // top and bottom
    for (y <- List(yMin, yMax)) {
      for (x <- xMin to xMax) {
        if (f(y)(x) == ' ') {
          f(y)(x) = '.'
        }
      }
    }
    // left and right
    for (x <- List(xMin, xMax)) {
      for (y <- yMin to yMax) {
        if (f(y)(x) == ' ') {
          f(y)(x) = ':'
        }
      }
    }
    // corners
    for (y <- List(yMin, yMax); x <- List(xMin, xMax)) {
      f(y)(x) = ','
    }

    val height = yMax - yMin + 1
    val width = xMax - xMin + 1
    val square = height * width
    println(s"S = $height * $width = $square")

    (yMin to yMax).map(y => {
      (xMin to xMax).map(f(y)(_)) mkString ""
    }) mkString "\n"
  }
}

class Creator2D {}

object Creator2D extends App {
  override def main(args: Array[String]) {
    var module = new Module2D("plus")
    module.setTitle(1, 7)
    module.addBox("send [(W,S),(W,E)]", 2, 13)
    module.addBox("case N of S, E", 5, 21)
    module.addBox("send [(N,E)]", 5, 39)
    module.addBox("use plus", 8, 11)
    module.addBox("send [(Inl W,E)]", 8, 30)
    println(module.toText)
  }
}
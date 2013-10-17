package endo

import RnaExecutor._
import scala.collection.mutable
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object RnaExecutor {
  sealed abstract trait Color
  case class RGB(val r: Int, val g: Int, val b: Int) extends Color
  case class Transparency(val a: Int) extends Color

  object Direction {
    val all = List(North, East, South, West)
  }

  sealed abstract trait Direction {
    def clockwise = this match {
      case North => East
      case East => South
      case South => West
      case West => North
    }

    def counterClockwise = this match {
      case North => West
      case East => North
      case South => East
      case West => South
    }
  }

  case object North extends Direction
  case object East extends Direction
  case object South extends Direction
  case object West extends Direction

  case class Pixel(val rgb: RGB, val trans: Transparency) {
    def compose(p: Pixel) = p match {
      case Pixel(RGB(r, g, b), Transparency(a)) => {
        val r0 = rgb.r + r * (255 - trans.a) / 255
        val g0 = rgb.g + g * (255 - trans.a) / 255
        val b0 = rgb.b + b * (255 - trans.a) / 255
        val a0 = trans.a + a * (255 - trans.a) / 255
        Pixel(RGB(r0, g0, b0), Transparency(a0))
      }
    }

    def moreTransparent(a: Int) = {
      val r0 = rgb.r * a / 255
      val g0 = rgb.g * a / 255
      val b0 = rgb.b * a / 255
      val a0 = trans.a * a / 255
      Pixel(RGB(r0, g0, b0), Transparency(a0))
    }

    def toJavaRGB = {
      rgb.b + (rgb.g << 8) + (rgb.r << 16) + (0xFF << 24)

    }
  }

  val MaxCoord = 600

  object Bitmap {
    def createTransparent = {
      Array.fill(MaxCoord, MaxCoord)(Pixel(black, transparent))
    }
  }

  type Bitmap = Array[Array[Pixel]]
  type Bitmaps = IndexedSeq[Bitmap]

  val black = RGB(0, 0, 0)
  val red = RGB(255, 0, 0)
  val green = RGB(0, 255, 0)
  val yellow = RGB(255, 255, 0)
  val blue = RGB(0, 0, 255)
  val magenta = RGB(255, 0, 255)
  val cyan = RGB(0, 255, 255)
  val white = RGB(255, 255, 255)

  val transparent = Transparency(0)
  val opaque = Transparency(255)

  class Averager(val default: Int) {
    var sum: Long = 0
    var total: Int = 0
    def add(value: Int) = {
      sum += value
      total += 1
    }
    def get: Int = {
      assert (sum <= Int.MaxValue)
      if (total == 0) default
      else (sum / total).toInt
    }
    def clear = {
      sum = 0
      total = 0
    }
  }

  class Bucket {
    def addColor(c: Color) = c match {
      case RGB(r, g, b) => {
        rc add r
        gc add g
        bc add b
      }
      case Transparency(a) => {
        ac add a
      }
    }
    def currentPixel: Pixel = {
      val a = ac.get
      val r = rc.get * ac.get / 255
      val g = gc.get * ac.get / 255
      val b = bc.get * ac.get / 255
      Pixel(RGB(r, g, b), Transparency(a))
    }
    def clear = {
      rc.clear
      gc.clear
      bc.clear
      ac.clear
    }

    val rc = new Averager(0)
    val gc = new Averager(0)
    val bc = new Averager(0)
    val ac = new Averager(255)
  }

  def unsigned_mod(value: Int, mod: Int) = {
    var x = value
    if (value >= mod) value % mod
    else if (value < 0) value % mod + mod
    else value
  }

  object Pos {
    def create(x: Int, y: Int) = {
      Pos(unsigned_mod(x, MaxCoord), unsigned_mod(y, MaxCoord))
    }
  }

  case class Pos(val x: Int, val y: Int) {
    require(0 <= x && x < MaxCoord, "x must be in bounds")
    require(0 <= y && y < MaxCoord, "y must be in bounds")

    def move(d: Direction) = d match {
      case North => Pos.create(x, y - 1)
      case East => Pos.create(x + 1, y)
      case South => Pos.create(x, y + 1)
      case West => Pos.create(x - 1, y)
    }
  }
}

class RnaExecutor(val rna: Seq[Char]) {
  val bucket = new Bucket
  var position = Pos(0, 0)
  var mark = Pos(0, 0)
  var direction: Direction = North
  val bitmaps = mutable.Stack[Bitmap](Bitmap.createTransparent)

  def build = {
    rna.grouped(7) foreach (_.toString match {
      case "PIPIIIC" => addColor(black)
      case "PIPIIIP" => addColor(red)
      case "PIPIICC" => addColor(green)
      case "PIPIICF" => addColor(yellow)
      case "PIPIICP" => addColor(blue)
      case "PIPIIFC" => addColor(magenta)
      case "PIPIIFF" => addColor(cyan)
      case "PIPIIPC" => addColor(white)
      case "PIPIIPF" => addColor(transparent)
      case "PIPIIPP" => addColor(opaque)
      case "PIIPICP" => bucket.clear
      case "PIIIIIP" => position = position.move(direction)
      case "PCCCCCP" => direction = direction.counterClockwise
      case "PFFFFFP" => direction = direction.clockwise
      case "PCCIFFP" => mark = position
      case "PFFICCP" => line(position, mark)
      case "PIIPIIP" => tryFill
      case "PCCPFFP" => addBitmap(Bitmap.createTransparent)
      case "PFFPCCP" => compose
      case "PFFICCF" => clip
      case x => //println(s"unknown rna command: $x")
    })
  }

  def addColor(c: Color) = {
    bucket.addColor(c)
  }

  def getPixel(p: Pos): Pixel = bitmaps(0)(p.x)(p.y)
  def setPixel(p: Pos) = {
    bitmaps(0)(p.x)(p.y) = bucket.currentPixel
  }

  def line(p0: Pos, p1: Pos) = (p0, p1) match {
    case (Pos(x0, y0), Pos(x1, y1)) => {
      val dx = x1 - x0
      val dy = y1 - y0
      val d = math.max(math.abs(dx), math.abs(dy))
      val c = if (dx * dy <= 0) 1 else 0
      var x = x0 * d + (d - c) / 2
      var y = y0 * d + (d - c) / 2
      for (i <- 1 to d) {
        setPixel(Pos(x / d, y / d))
        x = x + dx
        y = y + dy
      }
      setPixel(p1)
    }
  }

  def tryFill = {
    val next = bucket.currentPixel
    val old = getPixel(position)
    if (next != old) fill(position, old)
  }

  def fill(start: Pos, initial: Pixel): Unit = {
    val q = mutable.Queue[Pos](start)
    while (!q.isEmpty) {
      val pos = q.dequeue
      if (getPixel(pos) == initial) {
        setPixel(pos)
        pos match {
          case Pos(x, y) => {
            if (x > 0) q enqueue Pos(x - 1, y)
            if (x + 1 < MaxCoord) q enqueue Pos(x + 1, y)
            if (y > 0) q enqueue Pos(x, y - 1)
            if (y + 1 < MaxCoord) q enqueue Pos(x, y + 1)
          }
        }
      }
    }
  }

  def addBitmap(bitmap: Bitmap) = {
    if (bitmaps.length < 10) bitmaps push bitmap
  }

  def compose = {
    if (bitmaps.length >= 2) {
      val b0 = bitmaps.pop
      val b1 = bitmaps.head
      for (x <- 0 until MaxCoord; y <- 0 until MaxCoord) {
        b1(x)(y) = b0(x)(y) compose b1(x)(y)
      }
    }
  }

  def clip = {
    if (bitmaps.length >= 2) {
      val b0 = bitmaps.pop
      val b1 = bitmaps.head
      for (x <- 0 until MaxCoord; y <- 0 until MaxCoord) {
        b1(x)(y) = b1(x)(y) moreTransparent b0(x)(y).trans.a
      }
    }
  }

  def saveImage(fileName: String) = {
    val image = new BufferedImage(MaxCoord, MaxCoord, BufferedImage.TYPE_INT_ARGB)
    val b = bitmaps.head
    for (x <- 0 until MaxCoord; y <- 0 until MaxCoord) {
      image.setRGB(MaxCoord - 1 - y, x, b(x)(y).toJavaRGB)
    }
    val outputfile = new File(fileName);
    ImageIO.write(image, "png", outputfile);
  }

}
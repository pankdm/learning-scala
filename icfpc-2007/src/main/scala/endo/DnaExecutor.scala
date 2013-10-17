package endo

import scala.collection.mutable
import java.lang.StringBuffer
import scala.compat.Platform
import java.io.{File, PrintWriter}
import scala.runtime.StopException
import org.ahmadsoft.ropes.Rope

import MyTypes._
import DnaExecutor._

package object MyTypes {
  type Rna = StringBuffer
  type Pattern = Seq[PItem]
  type Template = Seq[TItem]

  type Environment = mutable.ArrayBuffer[Dna]
}

// pattern
sealed abstract class PItem

case class BaseP(c: Char) extends PItem

case class Skip(n: Int) extends PItem

case class Search(dna: Dna) extends PItem

case object OpenGroup extends PItem

case object CloseGroup extends PItem

// template
sealed abstract class TItem

case class BaseT(c: Char) extends TItem

case class Protection(n: Int, level: Int) extends TItem

case class AsNat(n: Int) extends TItem

object DnaExecutor {
  // TODO: better print functions
  def printE(e: Environment): Unit = {
    e.view.zipWithIndex foreach {
      x =>
        println(s"e[${x._2}] = ${x._1.preview}")
    }
  }

  def previewP(pattern: Pattern): String = {
    (for {p <- pattern} yield {
      p match {
        case BaseP(c) => c
        case Skip(n) => "!" + n
        case Search(dna: Dna) => "?'" + dna.toString + "'"
        case OpenGroup => "("
        case CloseGroup => ")"
      }
    }) mkString ""
  }

  def previewT(template: Template): String = {
    template map (_ match {
      case BaseT(c) => c
      case Protection(n, l) => {
        "\\" + n
      }
      case AsNat(n) => "/" + n
    }) mkString ""
  }

  def protect(l: Int, s: Dna): Dna = {
    if (l == 0) s
    else {
      //println("protecting length " + s.length)
      // TODO: rewrite more efficient
      val builder = new mutable.StringBuilder()
      val iter = s.value.iterator()
      while (iter.hasNext) {
        builder ++= QuoteCache.get(iter.next(), l)
      }
      Dna.fromString(builder.toString())
    }
  }

  object QuoteCache {
    val cache = mutable.ArrayBuffer[String]("I")

    def delta(c: Char) = c match {
      case 'I' => 0
      case 'C' => 1
      case 'F' => 2
      case 'P' => 3
    }

    def quoteOne(c: Char): String = c match {
      case 'I' => "C"
      case 'C' => "F"
      case 'F' => "P"
      case 'P' => "IC"
      case _ => ???
    }

    def get(c: Char, level: Int) = getOrCreate(level + delta(c))

    def getOrCreate(level: Int): String = {
      if (level >= cache.length) {
        println(s"Calculating ${level - cache.length + 1} elements in cache")
        while (level >= cache.length) {
          val builder = new mutable.StringBuilder
          for (c <- cache.last) {
            builder ++= quoteOne(c)
          }
          cache append builder.toString
        }
        println(s"Cache is ready, current size = ${cache.length}")
      }
      cache(level)
    }
  }

  def asNat(n: Int): List[Char] = {
    if (n == 0) List('P')
    else if (n % 2 == 0) 'I' :: asNat(n / 2)
    else if (n % 2 == 1) 'C' :: asNat(n / 2)
    else ???
  }
}

class DnaExecutor(var dna: Dna) {
  var debugDna = dna.asString
  var rna = new Rna()

  var printInfo = false
  var iteration = 0
  var counter = 0
  val infoPeriod = 10000
  val startTime = Platform.currentTime
  var prevTime = Platform.currentTime
  val file = new PrintWriter(new File("output.rna"))

  def finish() = {
    file.close()
    println("finish! ")
    throw new StopException()
  }

  def addToRna(s: Dna) = {
    rna append s
    file.write(s.toString)
  }

  def readNat(): Long = {
    if (dna.tryRead("P")) 0
    else if (dna.tryRead("I") || dna.tryRead("F")) {
      val n = 2 * readNat()
      assert(n <= Int.MaxValue)
      n
    } else if (dna.tryRead("C")) {
      val n = 2 * readNat() + 1
      assert(n <= Int.MaxValue)
      n
    } else finish()
  }

  def readConsts(): List[Char] = {
    if (dna.tryRead("C")) 'I' :: readConsts()
    else if (dna.tryRead("F")) 'C' :: readConsts()
    else if (dna.tryRead("P")) 'F' :: readConsts()
    else if (dna.tryRead("IC")) 'P' :: readConsts()
    else List()
  }

  def readPattern(): Pattern = {
    //println("reading pattern")
    var lvl = 0
    var p = new mutable.ListBuffer[PItem]()
    while (true) {
      // TODO: think of better case statements
      if (dna.tryRead("C")) p += BaseP('I')
      else if (dna.tryRead("F")) p += BaseP('C')
      else if (dna.tryRead("P")) p += BaseP('F')
      else if (dna.tryRead("IC")) p += BaseP('P')
      else if (dna.tryRead("IP")) {
        val n = readNat()
        p += Skip(n.toInt)
      } else if (dna.tryRead("IF")) {
        dna.read(1)
        val s = Dna.fromSeq(readConsts())
        p += Search(s)
      } else if (dna.tryRead("IIP")) {
        lvl += 1
        p += OpenGroup
      } else if (dna.tryRead("IIC") || dna.tryRead("IIF")) {
        if (lvl == 0) return p
        lvl -= 1
        p += CloseGroup
      } else if (dna.tryRead("III")) {
        addToRna(dna.slice(0, 7))
        dna.read(7)
      } else {
        finish()
      }
    }
    p
  }

  def readTemplate(): Template = {
    var t = new mutable.ListBuffer[TItem]
    while (true) {
      if (dna.tryRead("C")) t += BaseT('I')
      else if (dna.tryRead("F")) t += BaseT('C')
      else if (dna.tryRead("P")) t += BaseT('F')
      else if (dna.tryRead("IC")) t += BaseT('P')
      else if (dna.tryRead("IF") || dna.tryRead("IP")) {
        val l = readNat()
        val n = readNat()
        t += Protection(n.toInt, l.toInt)
      } else if (dna.tryRead("IIC") || dna.tryRead("IIF")) {
        return t
      } else if (dna.tryRead("IIP")) {
        val n = readNat()
        t += AsNat(n.toInt)
      } else if (dna.tryRead("III")) {
        addToRna(dna.slice(0, 7))
        dna.read(7)
      } else {
        finish()
      }
    }
    t
  }

  def matchReplace(pattern: Pattern, template: Template): Unit = {
    var i = 0
    val e = new Environment
    val stack = new mutable.Stack[Int]
    pattern foreach (_ match {
      case BaseP(c) => {
        if (dna.get(i) != c) return
        i += 1
      }
      case Skip(n) => {
        i += n
        if (i > dna.length) return
      }
      case Search(s) => {
        val index = dna.find(s, i)
        if (index == -1) return
        i = index + s.length
      }
      case OpenGroup => {
        stack push i
      }
      case CloseGroup => {
        e append dna.slice(stack.head, i)
        stack.pop
      }
    })
    dna.read(i)
    if (printInfo) {
      println("successfull match of length " + i)
      println("last " + dna.length + " remained")
    }
    replace(template, e)
  }

  def replace(template: Template, e: Environment): Unit = {
    if (printInfo) {
      printE(e)
    }
    val r = new Dna(Rope.BUILDER.build(""))
    template foreach (_ match {
      case BaseT(c) => {
        r append c
      }
      case Protection(n, l) => {
        if (n < e.length) {
          r append protect(l, e(n))
        }
      }
      case AsNat(n) => {
        val length = if (n < e.length) e(n).length else 0
        r append Dna.fromSeq(asNat(length))
      }
    })
    dna prepend r
  }

  def step() = {
    if (counter == 0) {
      printInfo = true
    } else {
      printInfo = false
    }

    if (printInfo) {
      println()
      println("iteration = " + iteration)
      println("dna = " + dna.preview)
    }

    val pattern = readPattern()
    if (printInfo) println("pattern = " + previewP(pattern))
    val template = readTemplate()
    if (printInfo) println("template = " + previewT(template))
    matchReplace(pattern, template)
    if (printInfo) {
      println("len(rna) = " + rna.length / 7)

      val currentTime = Platform.currentTime
      val delta = (currentTime - prevTime).toFloat / 1000
      val totalDelta = (currentTime - startTime).toFloat / 1000
      val speed = infoPeriod.toFloat / delta
      val totalSpeed = iteration.toFloat / totalDelta
      println(s"elapsed time = $delta seconds")
      println(s"speed = $speed iter/sec")
      println(s"total elapsed time = $totalDelta")
      println(s"total speed = $totalSpeed")
      prevTime = currentTime
    }

    iteration += 1
    counter += 1
    if (counter >= infoPeriod) {
      counter = 0
    }
  }
}

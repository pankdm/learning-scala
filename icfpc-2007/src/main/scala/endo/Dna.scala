package endo

import org.ahmadsoft.ropes.Rope

object Dna {
  def fromString(s: String): Dna = new Dna(Rope.BUILDER.build(s))

  def fromSeq(s: Seq[Char]): Dna = fromString(s mkString "")
}

// TODO: more efficient iteration over
class Dna(var value: Rope) {
  val showLength = 20
  var index = 0

  def startsWith(s: String) = value.startsWith(s, index)

  def read(n: Int) = {
    index += n
  }

  def doRead(n: Int) = {
    if (n > 0) {
      value = value.subSequence(n, value.length)
    }
  }

  def slice(from: Int, until: Int): Dna = {
    val right = math.min(value.length, until + index)
    new Dna(value.subSequence(from + index, right))
  }

  def get(i: Int): Char = value.charAt(index + i)

  def length: Int = value.length - index

  def find(s: Dna, startIndex: Int): Int = {
    val result = value.indexOf(s.value, startIndex + index)
    if (result != -1) result - index
    else -1
  }

  def prepend(other: Dna): Unit = {
    doRead(index)
    index = 0
    value = other.value append value
  }

  def append(other: Dna): Unit = value = value append other.value

  def append(c: Char): Unit = value = value append c

  def toList: List[Char] = value.toString.toList

  def asString: String = value.subSequence(index, value.length).toString

  def preview: String = {
    val toShow = if (length >= showLength) slice(0, showLength) else value
    s"$toShow... ($length bases)"
  }

  override def toString = asString

  def tryRead(s: String) = {
    //println("try read " + s)
    if (startsWith(s)) {
      read(s.length())
      true
    } else {
      false
    }
  }
}



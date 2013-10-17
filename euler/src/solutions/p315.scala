package solutions

object p315 extends App {

  val digits = Vector(
    List(
      " _ ",
      "| |",
      "|_|"),
    List(
      "   ",
      "  |",
      "  |"),
    List(
      " _ ",
      " _|",
      "|_ "),
    List(
      " _ ",
      " _|",
      " _|"),
    List(
      "   ",
      "|_|",
      "  |"),
    List(
      " _ ",
      "|_ ",
      " _|"),
    List(
      " _ ",
      "|_ ",
      "|_|"),
    List(
      " _ ",
      "| |",
      "  |"),
    List(
      " _ ",
      "|_|",
      "|_|"),
    List(
      " _ ",
      "|_|",
      " _|"))

  def weight(x: Int) = {
    (for {
      d <- digits(x)
      v <- d
      if v != ' '
    } yield {
      1
    }).sum
  }

  def charToInt(c: Char) = c.toInt - '0'.toInt

  def weight1(s: String) = s.map(c => weight(charToInt(c))).sum

  def sumDigits(n: Int): Int = n.toString.map(charToInt).sum
  
  def row(n: Int): List[String] = {
	  if (n < 10) List(n.toString) 
	  else n.toString :: row(sumDigits(n))
  }
  
  def samClock(n: Int) = row(n).map(weight1).sum * 2

  def deltaWeightInt(x: Int, y: Int) = {
    var res = 0
    for {
      (d1, d2) <- digits(x) zip digits(y)
      (v1, v2) <- d1 zip d2
    } yield {
      if (v1 != v2) {
        res += 1
      }
    }
    res
  }
  
  def deltaWeight(c1: Char, c2: Char) = {
    if (c1 == emptyChar) weight(charToInt(c2))
    else if (c2 == emptyChar) weight(charToInt(c1))
    else deltaWeightInt(charToInt(c1), charToInt(c2))
  }

  val emptyChar = ' '
  
  def delta(s1: String, s2: String) = {
    var res = 0
    for (i <- 1 to math.max(s1.length(), s2.length())) {
    	val i1 = s1.length() - i
    	val i2 = s2.length() - i
    	
    	val c1 = if (i1 >= 0) s1(i1) else emptyChar
    	val c2 = if (i2 >= 0) s2(i2) else emptyChar
    	res += deltaWeight(c1, c2)
    }
    res
  }

  def rowForMax(n: Int): List[String] = {
    "" :: row(n) ++ List("")
  }
  
  def maxClock(n: Int) = {
	  val r = rowForMax(n)
	  (r zip r.tail).map(x => delta(x._1,x._2)).sum
  }
  
  var res = 0l
  val nmin = 10 * 1000 * 1000
  val nx = 2 * nmin
  var isPrime = Array.fill(nx)(true)
  var iter = 0
  for (i <- 2 until nx) {
    if (isPrime(i)) {
      if (i >= nmin) {
        iter += 1
        if (iter > 100000) {
          println(i)
          iter = 0
        }
        //println(i)
        res += samClock(i) - maxClock(i)
      }
      for (d <- (i + i) until nx by i) {
        isPrime(d) = false
      } 
    }
  }
  println("res = " + res)
}
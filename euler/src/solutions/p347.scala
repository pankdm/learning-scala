package solutions

import scala.collection.mutable.ArrayBuffer

object p347 extends App {
  val nx = 10000000

  val isPrime = Array.fill(nx + 5)(true)

  println("Init primes")

  var primes = (0 to nx) map (x => ArrayBuffer[Int]())
  println("Generating primes")
  for (i <- 2 to nx) {
    if (isPrime(i)) {
      for (d <- (i + i) to nx by i) {
        isPrime(d) = false
        primes(d).append(i)
      }
    }
  }

  primes
  var was = Set[Pair[Int, Int]]()

  println("Go!")
  var res = 0l
  for (i <- nx to 1 by -1) {
    if (primes(i).size == 2) {
      val pair = Pair(primes(i)(0), primes(i)(1))
      if (!(was contains pair)) {
        was = was + pair
        res += i
      }
    }
  }

  println(res)
}
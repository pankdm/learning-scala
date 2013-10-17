package solutions

object p381 extends App {
  // sum ( p - i)! ( mod p)
  // (p - 1)! + (p - 2)! + (p - 3)! + (p - 4)! + (p - 5)! (mod)
  // (p - 1)! = -1 (mod p)
  // -1 ( 1 / (p - 1) (1 + 1 / (p - 2) (1 + 1 / (p - 3) (1 + 1 / (p - 4))

  val nx = 100000000                                //> nx  : Int = 1000000

  val isPrime = Array.fill(nx + 5)(true)          //> isPrime  : Array[Boolean] = Array(true, true, true, true, true, true, true, 
                                                  //| true, true, true, true, true, true, true, true, true, true, true, true, true
                                                  //| , true, true, true, true, true, true, true, true, true, true, true, true, tr
                                                  //| ue, true, true, true, true, true, true, true, true, true, true, true, true, 
                                                  //| true, true, true, true, true, true, true, true, true, true, true, true, true
                                                  //| , true, true, true, true, true, true, true, true, true, true, true, true, tr
                                                  //| ue, true, true, true, true, true, true, true, true, true, true, true, true, 
                                                  //| true, true, true, true, true, true, true, true, true, true, true, true, true
                                                  //| , true, true, true, true, true, true, true, true, true, true, true, true, tr
                                                  //| ue, true, true, true, true, true, true, true, true, true, true, true, true, 
                                                  //| true, true, true, true, true, true, true, true, true, true, true, true, true
                                                  //| , true, true, true, true, true, true, true, true, true, true, true, true, tr
                                                  //| ue, true, true, true, tr
                                                  //| Output exceeds cutoff limit.

  for (i <- 2 to nx) {
    if (isPrime(i)) {
      for (d <- (i + i) to nx by i) {
        isPrime(d) = false
      }
    }
  }

  def pow(a: Long, deg: Long, mod: Long): Long = {
    if (deg == 0) 1
    else {
      if (deg % 2 == 0) {
        val res = pow(a, deg / 2, mod)
        (res * res) % mod
      } else {
        (a * pow(a, deg - 1, mod)) % mod
      }
    }
  }                                               //> pow: (a: Long, deg: Long, mod: Long)Long

  def inv(a: Long, mod: Long): Long = {
    val res = (pow(a, mod - 2, mod) + mod) % mod
    assert((res * a) % mod == 1)
    res
  }                                               //> inv: (a: Long, mod: Long)Long

  def s_brute(n: Long) = {
  	if (n == 5) {
  		4
  	} else {
    var res = 0l
    var now = 1l
    for (i <- 1 to n.toInt - 1) {
      now *= i
      now %= n
      if (i >= n - 5) {
        res += now
        res %= n
      }
    }
    (res % n + n) % n
    }
  }                                               //> s_brute: (n: Long)Long

  def s(n: Int) = {
    if (n <= 12) {
      s_brute(n)
    } else {
      if (!isPrime(n)) 0
      else {
        var res = 0l
        var now = -1l
        for (k <- 1 to 5) {
          res += now
          now *= inv(n - k, n)
          now %= n
        }
        (res % n + n) % n
      }
    }
  }                                               //> s: (n: Int)Long

  s(7)                                            //> res0: Long = 4
  s(13)                                           //> res1: Long = 11
  s_brute(13)                                     //> res2: Long = 11

	def fact(n: Long, mod: Long): Long = {
		if (n == 0) 1
		else (n * fact(n - 1, mod) % mod + mod) % mod
	}                                         //> fact: (n: Long, mod: Long)Long

	def s_real(p: Long): Long = {
		var res =  0l
		for (k <- 1 to 5) {
			res += fact(p - k, p)
		}
		(res % p + p) % p
	}                                         //> s_real: (p: Long)Long
	s_real(7)                                 //> res3: Long = 4

  var res = 0l                                    //> res  : Long = 0
  for (i <- 5 until nx) {
  	if (isPrime(i)) {
	    val here = s(i)
	    /*
	    val other = s_brute(i)
	    val other2 = s_real(i)
	    if (here != other2) {
	      println(i + " " + here + " " + other2)
	
	    }
	    */
	    res += here
    }
  }
  println(res)                                             //> res4: Long = 18773749932
}
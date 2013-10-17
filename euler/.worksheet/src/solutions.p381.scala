package solutions

object p381 {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(245); 
  // sum ( p - i)! ( mod p)
  // (p - 1)! + (p - 2)! + (p - 3)! + (p - 4)! + (p - 5)! (mod)
  // (p - 1)! = -1 (mod p)
  // -1 ( 1 / (p - 1) (1 + 1 / (p - 2) (1 + 1 / (p - 3) (1 + 1 / (p - 4))

  val nx = 1000000;System.out.println("""nx  : Int = """ + $show(nx ));$skip(42); 

  val isPrime = Array.fill(nx + 5)(true);System.out.println("""isPrime  : Array[Boolean] = """ + $show(isPrime ));$skip(125); 

  for (i <- 2 to nx) {
    if (isPrime(i)) {
      for (d <- (i + i) to nx by i) {
        isPrime(d) = false
      }
    }
  };$skip(252); 

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
  };System.out.println("""pow: (a: Long, deg: Long, mod: Long)Long""");$skip(135); 

  def inv(a: Long, mod: Long): Long = {
    val res = (pow(a, mod - 2, mod) + mod) % mod
    assert((res * a) % mod == 1)
    res
  };System.out.println("""inv: (a: Long, mod: Long)Long""");$skip(267); 

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
  };System.out.println("""s_brute: (n: Long)Long""");$skip(304); 

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
  };System.out.println("""s: (n: Int)Long""");$skip(8); val res$0 = 

  s(7);System.out.println("""res0: Long = """ + $show(res$0));$skip(8); val res$1 = 
  s(13);System.out.println("""res1: Long = """ + $show(res$1));$skip(14); val res$2 = 
  s_brute(13);System.out.println("""res2: Long = """ + $show(res$2));$skip(108); 

	def fact(n: Long, mod: Long): Long = {
		if (n == 0) 1
		else (n * fact(n - 1, mod) % mod + mod) % mod
	};System.out.println("""fact: (n: Long, mod: Long)Long""");$skip(122); 

	def s_real(p: Long): Long = {
		var res =  0l
		for (k <- 1 to 5) {
			res += fact(p - k, p)
		}
		(res % p + p) % p
	};System.out.println("""s_real: (p: Long)Long""");$skip(11); val res$3 = 
	s_real(7);System.out.println("""res3: Long = """ + $show(res$3));$skip(16); 

  var res = 0l;System.out.println("""res  : Long = """ + $show(res ));$skip(245); 
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
  };$skip(10); val res$4 = 
  res;System.out.println("""res4: Long = """ + $show(res$4))}
}

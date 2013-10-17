package solutions

import scala.collection.mutable.ArrayBuffer

object p347 {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(91); 
	val nx = 100;System.out.println("""nx  : Int = """ + $show(nx ));$skip(63); 
	//val nx = 10000000
	
	val isPrime = Array.fill(nx + 5)(true);System.out.println("""isPrime  : Array[Boolean] = """ + $show(isPrime ));$skip(56); 
	
	var primes = (0 to nx) map (x => ArrayBuffer[Int]());System.out.println("""primes  : scala.collection.immutable.IndexedSeq[scala.collection.mutable.ArrayBuffer[Int]] = """ + $show(primes ));$skip(30); 
	println("Generating primes");$skip(131); 
	for (i <- 2 to nx) {
		if (isPrime(i)) {
		for (d <- (i + i) to nx by i) {
				isPrime(d) = false
				primes(d).append(i)
		}
		}
	};$skip(13); val res$0 = 
	
	primes;System.out.println("""res0: scala.collection.immutable.IndexedSeq[scala.collection.mutable.ArrayBuffer[Int]] = """ + $show(res$0));$skip(33); 
	var was = Set[Pair[Int, Int]]();System.out.println("""was  : scala.collection.immutable.Set[(Int, Int)] = """ + $show(was ));$skip(16); 
	
	var res = 0l;System.out.println("""res  : Long = """ + $show(res ));$skip(178); 
	for (i <- nx to 1 by -1) {
		if (primes(i).size == 2) {
			val pair = Pair(primes(i)(0), primes(i)(1))
			if (!(was contains pair)) {
				was = was + pair
				res += i
			}
		}
	};$skip(9); val res$1 = 

	res;System.out.println("""res1: Long = """ + $show(res$1))}
	
}

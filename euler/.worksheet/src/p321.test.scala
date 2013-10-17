package p321

import scala.collection.mutable._

object test {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(306); 
	// current: n * (n + 2)
	// triangular: n * (n + 1) / 2

	// n * (n + 2) = k * (k + 1) / 2


  for (i <- 1 to 3) {
    val slide = new Sliding(i)
    val path = slide.solution(slide.targetState)(1)
    println(i + " --> " + path.numSteps)
  };$skip(17); 

	val nx = 10000;System.out.println("""nx  : Int = """ + $show(nx ));$skip(31); 
	var tri = HashMap[Int, Int]();System.out.println("""tri  : scala.collection.mutable.HashMap[Int,Int] = """ + $show(tri ));$skip(55); 

	for (n <- 1 to nx) {
		tri.update( n*(n + 1) / 2, n)
	};$skip(56); 
	
	def from(n: Int): Stream[Int] = n #:: from(n + 1);System.out.println("""from: (n: Int)Stream[Int]""");$skip(126); 

	for (n <- 1 to nx) {
		val z = n * (n + 2)
		if (tri contains z) {
			println(n + " --> " + z + " tri -> " + tri(z))
		}
	};$skip(16); 
	val terms = 40;System.out.println("""terms  : Int = """ + $show(terms ));$skip(41); 
	var a = ArrayBuffer[BigInt](1, 1, 2, 4);System.out.println("""a  : scala.collection.mutable.ArrayBuffer[BigInt] = """ + $show(a ));$skip(66); 

	for (i <- 4 to terms + 5) {
		a.append(6 * a(i - 2) - a(i - 4))
	};$skip(7); val res$0 = 

	a;System.out.println("""res0: scala.collection.mutable.ArrayBuffer[BigInt] = """ + $show(res$0));$skip(37); val res$1 = 
	a drop 2 take terms map (_ - 1) sum;System.out.println("""res1: scala.math.BigInt = """ + $show(res$1))}

}

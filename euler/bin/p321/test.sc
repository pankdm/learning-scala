package p321

import scala.collection.mutable._

object test {
	// current: n * (n + 2)
	// triangular: n * (n + 1) / 2

	// n * (n + 2) = k * (k + 1) / 2


  for (i <- 1 to 3) {
    val slide = new Sliding(i)
    val path = slide.solution(slide.targetState)(1)
    println(i + " --> " + path.numSteps)
  }                                               //> 1 --> 3
                                                  //| 2 --> 8
                                                  //| 3 --> 15

	val nx = 10000                            //> nx  : Int = 10000
	var tri = HashMap[Int, Int]()             //> tri  : scala.collection.mutable.HashMap[Int,Int] = Map()

	for (n <- 1 to nx) {
		tri.update( n*(n + 1) / 2, n)
	}
	
	def from(n: Int): Stream[Int] = n #:: from(n + 1)
                                                  //> from: (n: Int)Stream[Int]

	for (n <- 1 to nx) {
		val z = n * (n + 2)
		if (tri contains z) {
			println(n + " --> " + z + " tri -> " + tri(z))
		}
	}                                         //> 1 --> 3 tri -> 2
                                                  //| 3 --> 15 tri -> 5
                                                  //| 10 --> 120 tri -> 15
                                                  //| 22 --> 528 tri -> 32
                                                  //| 63 --> 4095 tri -> 90
                                                  //| 133 --> 17955 tri -> 189
                                                  //| 372 --> 139128 tri -> 527
                                                  //| 780 --> 609960 tri -> 1104
                                                  //| 2173 --> 4726275 tri -> 3074
                                                  //| 4551 --> 20720703 tri -> 6437
	val terms = 40                            //> terms  : Int = 40
	var a = ArrayBuffer[BigInt](1, 1, 2, 4)   //> a  : scala.collection.mutable.ArrayBuffer[BigInt] = ArrayBuffer(1, 1, 2, 4)

	for (i <- 4 to terms + 5) {
		a.append(6 * a(i - 2) - a(i - 4))
	}

	a                                         //> res0: scala.collection.mutable.ArrayBuffer[BigInt] = ArrayBuffer(1, 1, 2, 4,
                                                  //|  11, 23, 64, 134, 373, 781, 2174, 4552, 12671, 26531, 73852, 154634, 430441,
                                                  //|  901273, 2508794, 5253004, 14622323, 30616751, 85225144, 178447502, 49672854
                                                  //| 1, 1040068261, 2895146102, 6061962064, 16874148071, 35331704123, 98349742324
                                                  //| , 205928262674, 573224305873, 1200237871921, 3340996092914, 6995498968852, 1
                                                  //| 9472752251611, 40772755941191, 113495517416752, 237641036678294, 66150035224
                                                  //| 8901, 1385073464128573, 3855506596076654, 8072799748093144, 2247153922421102
                                                  //| 3, 47051725024430291)
	a drop 2 take terms map (_ - 1) sum       //> res1: scala.math.BigInt = 2470433131948040

}
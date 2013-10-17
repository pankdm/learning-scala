package solutions

import scala.collection.mutable.HashSet

object p346 {
	//val nx = 1000
	val nx = 	math.pow(10, 12).toLong   //> nx  : Long = 1000000000000
	
	val nums = new HashSet[Long]              //> nums  : scala.collection.mutable.HashSet[Long] = Set()
	
	val bx = math.sqrt(nx).toInt              //> bx  : Int = 1000000
	for(b <- 2 to bx) {
		var now = b.toLong * b + b + 1
		while (now < nx) {
			nums.add(now)
			now = now * b + 1
		}
	}

	//nums.toList.sorted
	1 + nums.foldLeft(0l)(_ + _)              //> res0: Long = 336108797689259276

}
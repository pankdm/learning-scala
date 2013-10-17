package solutions

import scala.collection.mutable.HashSet

object p346 {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(125); 
	//val nx = 1000
	val nx = 	math.pow(10, 12).toLong;System.out.println("""nx  : Long = """ + $show(nx ));$skip(32); 
	
	val nums = new HashSet[Long];System.out.println("""nums  : scala.collection.mutable.HashSet[Long] = """ + $show(nums ));$skip(32); 
	
	val bx = math.sqrt(nx).toInt;System.out.println("""bx  : Int = """ + $show(bx ));$skip(120); 
	for(b <- 2 to bx) {
		var now = b.toLong * b + b + 1
		while (now < nx) {
			nums.add(now)
			now = now * b + 1
		}
	};$skip(53); val res$0 = 

	//nums.toList.sorted
	1 + nums.foldLeft(0l)(_ + _);System.out.println("""res0: Long = """ + $show(res$0))}

}

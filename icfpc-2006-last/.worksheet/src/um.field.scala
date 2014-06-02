package um

object field {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(64); 
  var f = Array.fill[Char](4, 5)(' ');System.out.println("""f  : Array[Array[Char]] = """ + $show(f ));$skip(16); 
 	f(3)(2) = '=';$skip(11); val res$0 = 
  f.length;System.out.println("""res0: Int = """ + $show(res$0));$skip(14); val res$1 = 
 	f(0).length;System.out.println("""res1: Int = """ + $show(res$1));$skip(81); 
	val cross = for { y <- 0 until f.length; x <- 0 until f(0).length} yield (y, x);System.out.println("""cross  : scala.collection.immutable.IndexedSeq[(Int, Int)] = """ + $show(cross ));$skip(56); 
	val nonEmpty = cross.filter(p => f(p._1)(p._2) != ' ');System.out.println("""nonEmpty  : scala.collection.immutable.IndexedSeq[(Int, Int)] = """ + $show(nonEmpty ));$skip(35); 
	val xMin = nonEmpty.map(_._2).min;System.out.println("""xMin  : Int = """ + $show(xMin ))}
}

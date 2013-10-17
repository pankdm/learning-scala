package solutions

object test {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(48); 
	val s1 = "111";System.out.println("""s1  : String = """ + $show(s1 ));$skip(17); 
	val s2 = "2343";System.out.println("""s2  : String = """ + $show(s2 ));$skip(11); val res$0 = 
	s1 zip s2;System.out.println("""res0: scala.collection.immutable.IndexedSeq[(Char, Char)] = """ + $show(res$0))}
}

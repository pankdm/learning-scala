package endo

import DnaHelper._

object test {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(88); 
	def join(l: List[Char]) = l mkString "";System.out.println("""join: (l: List[Char])String""");$skip(17); val res$0 = 
  join(asNat(6));System.out.println("""res0: String = """ + $show(res$0));$skip(17); val res$1 = 
  join(asNat(8));System.out.println("""res1: String = """ + $show(res$1));$skip(18); val res$2 = 
  join(asNat(28));System.out.println("""res2: String = """ + $show(res$2));$skip(19); val res$3 = 
	join(asNat(4096));System.out.println("""res3: String = """ + $show(res$3))}
}

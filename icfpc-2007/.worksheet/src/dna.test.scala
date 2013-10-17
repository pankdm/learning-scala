package dna

import scalaz.Rope

object test {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(90); 
  println("Welcome to the Scala worksheet");$skip(34); 
  var r = Rope.fromString("abcd");System.out.println("""r  : scalaz.Rope[Char] = """ + $show(r ));$skip(14); 
	r = '1' +: r;$skip(14); 
	r = r :+ '4';$skip(20); val res$0 = 
	r ++ r.slice(1, 3);System.out.println("""res0: scalaz.Rope[Char] = """ + $show(res$0));$skip(24); 
	println(r mkString "");$skip(8); val res$1 = 
	Seq(1);System.out.println("""res1: Seq[Int] = """ + $show(res$1));$skip(22); 

	val c = Seq[Char]();System.out.println("""c  : Seq[Char] = """ + $show(c ));$skip(29); 
	val cc = "sdf" ++ c ++ "11";System.out.println("""cc  : String = """ + $show(cc ));$skip(96); 
	
	cc.toList match {
		case 's' :: other => println("here")
		case _ => println("not found")
	}
	
	case class Y(var a: Int, var b: Int)
	
	case class X(var a: Int) {
		def inc = a += 10
		def toY(x: Int) = new Y(a, a + x)
	};$skip(166); 
	
	implicit def X2y(x: X) = x.toY(7);System.out.println("""X2y: (x: dna.test.X)dna.test.Y""");$skip(24); 

	
	val y: Y = new X(5);System.out.println("""y  : dna.test.Y = """ + $show(y ));$skip(27); 

	var dna = new dna.Dna(r);System.out.println("""dna  : <error> = """ + $show(dna ))}
	
}

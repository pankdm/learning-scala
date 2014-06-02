package um

import java.io.FileOutputStream
import java.io.FileReader
import java.io.BufferedReader
import scala.collection.mutable.Buffer
import scala.io.Source
import scala.util.matching.Regex

object test {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(247); val res$0 = 
  (1l << 31 + 1l << 31) & 0xffffffffL;System.out.println("""res0: Long = """ + $show(res$0));$skip(94); val res$1 = 
  //val source = Source.fromFile(codex)(scala.io.Codec.ISO8859)
  (~123).toLong & 0xffffffffL;System.out.println("""res1: Long = """ + $show(res$1));$skip(21); val res$2 = 
  123l ^ 0xffffffffL;System.out.println("""res2: Long(4294967172L) = """ + $show(res$2));$skip(186); 

  val lines =
    """
* Means that if you drop a marble into pipe x, it comes out pipe y,
* and you hear z plinks
0 -> (8,10)
1 -> (1,5)
2 -> (2,5)
3 -> (0,5)
4 -> (5,4)
5 -> (4,6)
""";System.out.println("""lines  : String = """ + $show(lines ));$skip(28); 
  var l = lines.split('\n');System.out.println("""l  : Array[String] = """ + $show(l ));$skip(43); 
  var r = """(\d+) -> \((\d+),(\d+)\)""".r;System.out.println("""r  : scala.util.matching.Regex = """ + $show(r ));$skip(40); val res$3 = 
  r.findFirstMatchIn(l(3)).get.group(1);System.out.println("""res3: String = """ + $show(res$3));$skip(320); val res$4 = 

  /*
	var fos = new FileOutputStream("1.txt");
	var rawByte = new Array[Byte](1)
	rawByte(0) = 0x0011.toByte
	rawByte
	fos.write(rawByte)
	fos.close()
	*/

  /*
	val pipe = "/home/pankdm/programming/icfpc/2006/pipe"
  val f = new BufferedReader(new FileReader(pipe))
	f.readLine()
	f.readLine()
	*/
  "abcd".getBytes();System.out.println("""res4: Array[Byte] = """ + $show(res$4));$skip(15); 

  val s = "☯";System.out.println("""s  : String = """ + $show(s ));$skip(52); 
  var ss = s.getBytes().map(_ & 0xFF).map(_.toChar);System.out.println("""ss  : Array[Char] = """ + $show(ss ));$skip(14); val res$5 = 
  ss(0).toInt;System.out.println("""res5: Int = """ + $show(res$5));$skip(8); val res$6 = 

  0x11;System.out.println("""res6: Int(17) = """ + $show(res$6));$skip(61); val res$7 = 
  //Balance.put(Balance.PHYSICS, 0, 1, 2)
  List(1, 2, 3, 4);System.out.println("""res7: List[Int] = """ + $show(res$7));$skip(792); 

  val info =
    """

You are in a room with a pile of junk. A hallway leads south.
There is a bolt here.
Underneath the bolt, there is a spring.
Underneath the spring, there is a button.
Underneath the button, there is a (broken) processor.
Underneath the processor, there is a red pill.
Underneath the pill, there is a (broken) radio.
Underneath the radio, there is a cache.
Underneath the cache, there is a blue transistor.
Underneath the transistor, there is an antenna.
Underneath the antenna, there is a screw.
Underneath the screw, there is a (broken) motherboard.
Underneath the motherboard, there is a (broken) A-1920-IXB.
Underneath the A-1920-IXB, there is a red transistor.
Underneath the transistor, there is a (broken) keypad.
Underneath the keypad, there is some trash.
  """;System.out.println("""info  : String = """ + $show(info ));$skip(36); 
  val pattern = """leads (\w+)""".r;System.out.println("""pattern  : scala.util.matching.Regex = """ + $show(pattern ));$skip(75); 
  pattern.findAllMatchIn(info).foreach(m => {
    println(m.group(1))
  });$skip(42); 

  val p2 = """There is [a|an] (\w+)""".r;System.out.println("""p2  : scala.util.matching.Regex = """ + $show(p2 ));$skip(40); 
  val p3 = """Underneath the (\w+)""".r;System.out.println("""p3  : scala.util.matching.Regex = """ + $show(p3 ));$skip(64); val res$8 = 
  p3.findAllMatchIn(info).map(m => {
    m.group(1)
  }).toList;System.out.println("""res8: List[String] = """ + $show(res$8));$skip(39); 

  var f = Array.fill[Char](4, 4)(' ');System.out.println("""f  : Array[Array[Char]] = """ + $show(f ))}
}

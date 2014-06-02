package um

import java.io.FileOutputStream
import java.io.FileReader
import java.io.BufferedReader
import scala.collection.mutable.Buffer
import scala.io.Source
import scala.util.matching.Regex

object test {
  (1l << 31 + 1l << 31) & 0xffffffffL             //> res0: Long = 0
  //val source = Source.fromFile(codex)(scala.io.Codec.ISO8859)
  (~123).toLong & 0xffffffffL                     //> res1: Long = 4294967172
  123l ^ 0xffffffffL                              //> res2: Long(4294967172L) = 4294967172

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
"""                                               //> lines  : String = "
                                                  //| * Means that if you drop a marble into pipe x, it comes out pipe y,
                                                  //| * and you hear z plinks
                                                  //| 0 -> (8,10)
                                                  //| 1 -> (1,5)
                                                  //| 2 -> (2,5)
                                                  //| 3 -> (0,5)
                                                  //| 4 -> (5,4)
                                                  //| 5 -> (4,6)
                                                  //| "
  var l = lines.split('\n')                       //> l  : Array[String] = Array("", * Means that if you drop a marble into pipe x
                                                  //| , it comes out pipe y,, * and you hear z plinks, 0 -> (8,10), 1 -> (1,5), 2 
                                                  //| -> (2,5), 3 -> (0,5), 4 -> (5,4), 5 -> (4,6))
  var r = """(\d+) -> \((\d+),(\d+)\)""".r        //> r  : scala.util.matching.Regex = (\d+) -> \((\d+),(\d+)\)
  r.findFirstMatchIn(l(3)).get.group(1)           //> res3: String = 0

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
  "abcd".getBytes()                               //> res4: Array[Byte] = Array(97, 98, 99, 100)

  val s = "☯"                                     //> s  : String = ☯
  var ss = s.getBytes().map(_ & 0xFF).map(_.toChar)
                                                  //> ss  : Array[Char] = Array(â, , ¯)
  ss(0).toInt                                     //> res5: Int = 226

  0x11                                            //> res6: Int(17) = 17
  //Balance.put(Balance.PHYSICS, 0, 1, 2)
  List(1, 2, 3, 4)                                //> res7: List[Int] = List(1, 2, 3, 4)

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
  """                                             //> info  : String = "
                                                  //| 
                                                  //| You are in a room with a pile of junk. A hallway leads south.
                                                  //| There is a bolt here.
                                                  //| Underneath the bolt, there is a spring.
                                                  //| Underneath the spring, there is a button.
                                                  //| Underneath the button, there is a (broken) processor.
                                                  //| Underneath the processor, there is a red pill.
                                                  //| Underneath the pill, there is a (broken) radio.
                                                  //| Underneath the radio, there is a cache.
                                                  //| Underneath the cache, there is a blue transistor.
                                                  //| Underneath the transistor, there is an antenna.
                                                  //| Underneath the antenna, there is a screw.
                                                  //| Underneath the screw, there is a (broken) motherboard.
                                                  //| Underneath the motherboard, there is a (broken) A-1920-IXB.
                                                  //| Underneath the A-1920-IXB, there is a red transistor.
                                                  //| Underneath the transistor, there is a (broken) keypad.
                                                  //| Underneath the keypad, there is some trash.
                                                  //|   "
  val pattern = """leads (\w+)""".r               //> pattern  : scala.util.matching.Regex = leads (\w+)
  pattern.findAllMatchIn(info).foreach(m => {
    println(m.group(1))
  })                                              //> south

  val p2 = """There is [a|an] (\w+)""".r          //> p2  : scala.util.matching.Regex = There is [a|an] (\w+)
  val p3 = """Underneath the (\w+)""".r           //> p3  : scala.util.matching.Regex = Underneath the (\w+)
  p3.findAllMatchIn(info).map(m => {
    m.group(1)
  }).toList                                       //> res8: List[String] = List(bolt, spring, button, processor, pill, radio, cac
                                                  //| he, transistor, antenna, screw, motherboard, A, transistor, keypad)

  var f = Array.fill[Char](4, 4)(' ')             //> f  : Array[Array[Char]] = Array(Array( ,  ,  ,  ), Array( ,  ,  ,  ), Array
                                                  //| ( ,  ,  ,  ), Array( ,  ,  ,  ))
}
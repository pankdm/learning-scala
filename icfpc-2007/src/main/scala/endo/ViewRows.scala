package endo

import java.io.DataOutputStream
import java.io.FileOutputStream

object ViewRows extends App {
  def IntToICFP(n: Int): String = {
    Integer.toBinaryString(n).reverse.replace("0", "I").replace("1", "C")
  }

  def ICFPToInt(s: String) = {
    val s01 = s.replace("I", "0").replace("C", "1")
    Integer.parseInt(s01.reverse, 2)
  }

  def toByteArray(icfp: String): Array[Byte] = {
    icfp.grouped(8).map(x => ICFPToInt(x).toByte).toArray[Byte]
  }

  def saveToFile(icfp: String, fileName: String) = {
    val byteArray = toByteArray(icfp)
    val output = new DataOutputStream(new FileOutputStream(fileName))
    output.write(byteArray.toArray[Byte])
    output.close()
  }

  //val header = "5089 474e 0a0d 0a1a 0000 0d00 4849 5244"
  val header = "5089"
  val ICFPpattern = header.split(" ").map(Integer.parseInt(_, 16)).map(IntToICFP(_)) mkString ""

  val file = scala.io.Source.fromFile("endo.dna")
  val dnaString = file.getLines mkString ""

  dnaString.replace('C', ' ').grouped(0x20).take(5000).foreach(x => {
    println(x.toString)
  })

  println("pattern = " + ICFPpattern)
  val index = dnaString.indexOf(ICFPpattern)
  println(index)

  val pngEnd = dnaString.indexOf("PP", index)
  saveToFile(dnaString.substring(index, pngEnd), "achtung.png")

  val audioStart = pngEnd + 2
  val audioEnd = dnaString.indexOf("P", audioStart)
  saveToFile(dnaString.substring(audioStart, audioEnd), "human-audio.wav")
}
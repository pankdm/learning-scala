package endo

import scala.runtime.StopException

object Dna2Image extends App {

  def readDna(fileName: String) = {
    val file = scala.io.Source.fromFile(fileName)
    file.getLines mkString ""
  }

  def cleanDna(dna: String) = {
    dna.filter(c => if (c == 'I' || c == 'F' || c == 'P' || c == 'C') true else false)
  }

  def imageDefault(prefix: String) = {
    createImage(prefix, "output.png")
  }

  def createImage(prefix: String, outputFile: String) = {
    val dnaString = readDna("endo.dna")
    val dna = Dna.fromString(cleanDna(prefix) + dnaString)
    val executor = new DnaExecutor(dna)

    try {
      while (true) {
        executor.step()
      }
    } catch {
      case e: StopException => println("Done")
    }

    println("Generating image")
    val rnaExecutor = new RnaExecutor(executor.rna.toString)
    rnaExecutor.build
    rnaExecutor.saveImage(outputFile)
    println("Done!")
  }

  def genNumPrefix(n: Int) = {
    val s = Catalog.encode(n)
    val middle = (for {i <- 1 to s.length} yield "C") mkString ""
    s"IIPIFFCPIC FPP ICII $middle CIICI PPP $s IIC"
  }

  def dumpPage(p: Int) = {
    println()
    println("Dumping page " + p)
    val prefix = genNumPrefix(p)
    val fileName = s"img-all/page-$p.png"
    try {
      createImage(prefix, fileName)
    } catch {
      case x => println("Error: " + x)
    }
  }

  def dumpPages() = {
//    val pages = List(1729, 8, 42, 112, 10646, 85, 2181889, 5, 4405829, 123456, 999999999)
//    val encrypted = List(23, 84)
    //val pages = List(1, 2, 3, 4, 6, 7, 28, 4096)
    //val pages = List(23)
    dumpPage(1024)
//    for (p <- 9 to 100) {
//      dumpPage(p)
//    }
  }

  dumpPages()

  //createImage(readDna("prefix/self-check.prefix"))
  //createImage(readDna("prefix/test.prefix"))
  //createImage("IIPIFFCPICFPPICIICCIICIPPPFIIC")
  //val prefix1 = "IIPIFFCPIC FPP ICII C       CIICI PPP F IIC"

  //  println(cleanDna(prefix1))
//  //println()
//  val prefix = cleanDna(genNumPrefix(1337))
//  tryImage(prefix)

  //createImage("")

  //createImage("")
  //createImage("IIPIFFCPICICIICPIICIPPPICIIC") // self check
  //createImage("IPIFFCPICFPPICIICCIICIPPPFIIC") // on the top of self check -> generates itself
  //createImage("IIPIFFCPICPICIICIPCCCPIICIPPPFFCFFFFIIC") // in the audio
  //createImage("IPIFFCPIC") // page 1

  //createImage("IIPIFFCPICFPPICIICCIICIPPPFIIC") // repair guide
  //createImage("IIPIFFCPICFPP ICIICCCIICI PPPCFIIC") // 1337
  //createImage("IIPIFFCPICFPPCIICCCIICICPPPCFIIC") // 1337

    //createImage("IIPIFFCPICPCIICICIICIPPPPIIC") // picture
}
package endo

object Rna2Image extends App{
  val file = scala.io.Source.fromFile("output.rna")
  val rnaString = file.getLines mkString ""
  
  val rnaExecutor = new RnaExecutor(rnaString.toSeq)
  
  rnaExecutor.build
  rnaExecutor.saveImage("output.png")
  
  println("Done!")
}
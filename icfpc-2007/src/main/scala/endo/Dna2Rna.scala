package endo

object Dna2Rna extends App {
  def executePrefix(prefix: String) = {
    val file = scala.io.Source.fromFile("endo.dna")

    val dnaString = file.getLines mkString ""

    var dna = Dna.fromString(prefix + dnaString)
    val executor = new DnaExecutor(dna)

    while (true) {
      executor.step()
    }
  }
  
  //executePrefix("IIPIFFCPICICIICPIICIPPPICIIC") // self check
  executePrefix("IPIFFCPICFPPICIICCIICIPPPFIIC") // gave the same prefix
}
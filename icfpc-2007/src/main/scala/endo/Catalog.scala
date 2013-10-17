package endo

object Catalog extends App {
  val prefix = "IIPIFFCPICFPPICIICCCIICIPPPCFIIC"

  val number = "ICIICCCIICI"
  println(ViewRows.ICFPToInt(number))

  def encode(n: Int) = {
    Integer.toBinaryString(n).replace("1", "F").replace("0", "C").reverse
  }

  val icfp = encode(1337)
  println(encode(1337))
  println(encode(23))
  println(encode(1024))
  println(encode(1729))
  println(prefix.indexOf(icfp))
}

package typ

import scala.collection.mutable
import scala.sys.process.Process

import java.io.PrintWriter

case class Feature(id: Int, info: String, groupId: Int, imgPath: String)

object Extract extends App {
  val file = scala.io.Source.fromFile("input.html")
  val outputDir = "img"

  val data = file.getLines mkString
  val pointPattern = """<div id="ajax_point_(\d+)">(.*?)</div>""".r
  val imgPattern = """<img src="(\S*?_transparent.png)">""".r
  val stringsPattern = """english (.*?): (.*?)<br>""".r

  var imgIndex = mutable.Map[String, Int]()

  Process(s"mkdir -p $outputDir").!
  Process(s"rm $outputDir/*").!

  val features = for {
    point <- pointPattern.findAllMatchIn(data)
    info <- stringsPattern.findFirstMatchIn(point.matched)
    img <- imgPattern.findFirstMatchIn(point.matched)
  } yield {
    val id = Integer.parseInt(point.group(1))
    val imgPath = img.group(1)
    val index = imgIndex.getOrElseUpdate(imgPath, id)
    val dstImgPath = s"$outputDir/$index.png"
    Process(s"cp $imgPath $dstImgPath").!
    Feature(id, info.group(2), index, dstImgPath)
  }
  val result = features.toTraversable.groupBy(_.groupId).toSeq.sortBy(_._1)

  val output = new PrintWriter("README.md")
  output.write("# Features\n")
  result.foreach(_ match {
    case Pair(id, stream) => {
      val tags = stream.map(_.info) mkString ", "
      val imgPath = stream.head.imgPath
      val outString = s"* ![$id]($imgPath) → $tags\n"
      output.write(outString)
    }
  })
  output.close()
  println("Done!")
}
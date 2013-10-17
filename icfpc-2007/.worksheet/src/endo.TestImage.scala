package endo

import RnaExecutor._

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object TestImage {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(181); 
	println(System.getProperty("user.dir"));$skip(14); 
	val mx = 100;System.out.println("""mx  : Int = """ + $show(mx ));$skip(69); 
  val image = new BufferedImage(mx, mx, BufferedImage.TYPE_INT_ARGB);System.out.println("""image  : java.awt.image.BufferedImage = """ + $show(image ));$skip(54); 

  val pixel = Pixel(RGB(0, 0, 0), Transparency(255));System.out.println("""pixel  : endo.RnaExecutor.Pixel = """ + $show(pixel ));$skip(16); val res$0 = 
  
  0xFF << 24;System.out.println("""res0: Int(-16777216) = """ + $show(res$0));$skip(17); val res$1 = 
	pixel.toJavaRGB;System.out.println("""res1: Int = """ + $show(res$1));$skip(12); val res$2 = 
	0xFF000000;System.out.println("""res2: Int(-16777216) = """ + $show(res$2));$skip(83); 
  for (x <- 0 until mx; y <- 0 until mx) {
    image.setRGB(x, y, pixel.toJavaRGB)
  };$skip(44); 
  val outputfile = new File("test.png");System.out.println("""outputfile  : java.io.File = """ + $show(outputfile ));$skip(42); val res$3 = 
  ImageIO.write(image, "png", outputfile);System.out.println("""res3: Boolean = """ + $show(res$3));$skip(19); 
  println("Done!")}
}

package endo

import RnaExecutor._

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object TestImage {
	println(System.getProperty("user.dir"))   //> /home/pankdm
	val mx = 100                              //> mx  : Int = 100
  val image = new BufferedImage(mx, mx, BufferedImage.TYPE_INT_ARGB)
                                                  //> image  : java.awt.image.BufferedImage = BufferedImage@2dc719bd: type = 2 Dir
                                                  //| ectColorModel: rmask=ff0000 gmask=ff00 bmask=ff amask=ff000000 IntegerInterl
                                                  //| eavedRaster: width = 100 height = 100 #Bands = 4 xOff = 0 yOff = 0 dataOffse
                                                  //| t[0] 0

  val pixel = Pixel(RGB(0, 0, 0), Transparency(255))
                                                  //> pixel  : endo.RnaExecutor.Pixel = Pixel(RGB(0,0,0),Transparency(255))
  
  0xFF << 24                                      //> res0: Int(-16777216) = -16777216
	pixel.toJavaRGB                           //> res1: Int = -16777216
	0xFF000000                                //> res2: Int(-16777216) = -16777216
  for (x <- 0 until mx; y <- 0 until mx) {
    image.setRGB(x, y, pixel.toJavaRGB)
  }
  val outputfile = new File("test.png")           //> outputfile  : java.io.File = test.png
  ImageIO.write(image, "png", outputfile)         //> res3: Boolean = true
  println("Done!")                                //> Done!
}
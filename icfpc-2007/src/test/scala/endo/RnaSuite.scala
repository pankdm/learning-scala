package endo

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite

import RnaExecutor._

@RunWith(classOf[JUnitRunner])
class BucketSuite extends FunSuite {

  test("bucket 1") {
    val bucket = new Bucket()
    bucket.addColor(transparent)
    bucket.addColor(opaque)
    bucket.addColor(opaque)
    assert(bucket.currentPixel === Pixel(RGB(0, 0, 0), Transparency(170)))
  }

  test("bucket 2") {
    val bucket = new Bucket()
    bucket.addColor(black)
    bucket.addColor(yellow)
    bucket.addColor(cyan)
    assert(bucket.currentPixel === Pixel(RGB(85, 170, 85), Transparency(255)))
  }

  test("bucket 3") {
    val bucket = new Bucket()
    bucket.addColor(yellow)
    bucket.addColor(transparent)
    bucket.addColor(opaque)
    assert(bucket.currentPixel === Pixel(RGB(127, 127, 0), Transparency(127)))
  }

  test("bucket 4") {
    val bucket = new Bucket()
    for (i <- 1 to 18) bucket.addColor(black)
    for (i <- 1 to 7) bucket.addColor(red)
    for (i <- 1 to 39) bucket.addColor(magenta)
    for (i <- 1 to 10) bucket.addColor(white)
    for (i <- 1 to 3) bucket.addColor(opaque)
    for (i <- 1 to 1) bucket.addColor(transparent)
    assert(bucket.currentPixel === Pixel(RGB(143, 25, 125), Transparency(191)))
  }

}

@RunWith(classOf[JUnitRunner])
class RnaSuite extends FunSuite {

  test("turn") {
    for (d <- Direction.all) {
      assert(d.clockwise.counterClockwise === d)
      assert(d.counterClockwise.clockwise === d)
      assert(d.clockwise.clockwise.clockwise.clockwise === d)
    }
  }
}
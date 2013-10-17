package endo

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite

import DnaExecutor._

@RunWith(classOf[JUnitRunner])
class PatternSuite extends FunSuite {

  test("sample") {
    assert(2 + 2 === 4)
  }

  test("pattern 1") {
    val dna: Dna = Dna.fromString("CIIC")
    val executor = new DnaExecutor(dna)
    val pattern = executor.readPattern()
    assert(pattern.toSeq === Seq(BaseP('I')))
  }

  test("pattern 2") {
    val dna: Dna = Dna.fromString("IIPIPICPIICICIIF")
    val executor = new DnaExecutor(dna)
    val pattern = executor.readPattern()
    assert(pattern.toSeq === Seq(OpenGroup, Skip(2), CloseGroup, BaseP('P')))
  }

  test("step 1") {
    val dna = Dna.fromString("IIPIPICPIICICIIFICCIFPPIICCFPC")
    val executor = new DnaExecutor(dna)
    executor.step()
    assert(dna.asString === "PICFC")
  }

  test("step 2") {
    val dna = Dna.fromString("IIPIPICPIICICIIFICCIFCCCPPIICCFPC")
    val executor = new DnaExecutor(dna)
    executor.step()
    assert(dna.asString === "PIICCFCFFPC")
  }

  test("step 3") {
    val dna = Dna.fromString("IIPIPIICPIICIICCIICFCFC")
    val executor = new DnaExecutor(dna)
    executor.step()
    assert(dna.asString === "I")
  }

}

@RunWith(classOf[JUnitRunner])
class ProtectSuite extends FunSuite {

  test("protect 1") {
    val dna = protect(1, Dna.fromString("ICFP"))
    assert(dna.asString === "CFPIC")
  }

  test("protect 2") {
    val dna = protect(4, Dna.fromString("I"))
    assert(dna.asString === "IC")
  }

  test("protect 3") {
    val dna = protect(8, Dna.fromString("I"))
    assert(dna.asString === "ICCF")
  }

}

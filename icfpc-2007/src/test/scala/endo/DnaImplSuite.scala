package endo

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite

@RunWith(classOf[JUnitRunner])
class DnaSuite extends FunSuite {

  test("append") {
    val d1 = Dna.fromString("abcd")
    val d2 = Dna.fromString("1234")
    val d3 = Dna.fromString("x")
    val d4 = Dna.fromString("tt")
    val d5 = Dna.fromString("bc")

    d1 append d2
    d1.read(1)
    d1.prepend(Dna.fromString("aa"))
    d1.read(1)
    assert( d1.toString === "abcd1234")
    assert( d1.find(d5, 0) === 1)
    assert( d1.find(d5, 2) === -1)
    val d0 = d1.slice(2, 5)
    assert( d0.toString === "cd1")
    
    d3 append 'Z'
    assert( d3.toString === "xZ")
    
    d3 prepend d4
    assert( d3.toString === "ttxZ")
    
    d3.read(1)
    assert( d3.toString === "txZ")

    assert( d3.slice(1, 4).toString === "xZ")
    assert( d3.slice(1, 2).toString === "x")
    
    d3.read(2)
    assert( d3.toString === "Z")
    
  } 
  
}
package um

import scala.io.Source
import scala.collection.mutable.ArrayBuffer

import java.io.FileInputStream
import java.io.DataInputStream


object RunMachine extends App {
  	val codex = "/home/pankdm/programming/icfpc/2006/codex.umz"
  	val sandmark = "/home/pankdm/programming/icfpc/2006/sandmark.umz" 
	val fileInput = new FileInputStream(sandmark)
	val dis = new DataInputStream(fileInput)  
	var array = new ArrayBuffer[UInt]()       
	while(dis.available() != 0) {
		val x = dis.readInt()
		array.append(new UInt(x))
	}
	
	var uvm = new UVM(array.toArray) 
	
	/*
	while(true) {
	  uvm.executeNext
	}
	* 
	*/

	for(i <- 0 until 10) {
	  uvm.executeNext
	}
 
	
}
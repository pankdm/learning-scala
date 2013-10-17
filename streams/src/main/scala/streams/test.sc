package streams

import Bloxorz._

object test {
	//println(Level1.solution)
	val s =
    """ |------------S
		    |-----ooo--ooo
		    |-----oooooooo
		    |---ooooo--oo-
		    |---oTo+---oo-
		    |-ooooo---oooo
		    |oooo-----oooo
		    |oooo--ooooo--
		    |-----ooo-----
		    |-----ooo-----""".stripMargin //> s  : String = ------------S
                                                  //| -----ooo--ooo
                                                  //| -----oooooooo
                                                  //| ---ooooo--oo-
                                                  //| ---oTo+---oo-
                                                  //| -ooooo---oooo
                                                  //| oooo-----oooo
                                                  //| oooo--ooooo--
                                                  //| -----ooo-----
                                                  //| -----ooo-----
	    
	    
	    
s.split('\n')                                     //> res0: Array[String] = Array(------------S, -----ooo--ooo, -----oooooooo, ---
                                                  //| ooooo--oo-, ---oTo+---oo-, -ooooo---oooo, oooo-----oooo, oooo--ooooo--, ----
                                                  //| -ooo-----, -----ooo-----)
}
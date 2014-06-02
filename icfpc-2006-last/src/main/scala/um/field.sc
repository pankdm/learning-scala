package um

object field {
  var f = Array.fill[Char](4, 5)(' ')             //> f  : Array[Array[Char]] = Array(Array( ,  ,  ,  ,  ), Array( ,  ,  ,  ,  ), A
                                                  //| rray( ,  ,  ,  ,  ), Array( ,  ,  ,  ,  ))
 	f(3)(2) = '='
  f.length                                        //> res0: Int = 4
 	f(0).length                               //> res1: Int = 5
	val cross = for { y <- 0 until f.length; x <- 0 until f(0).length} yield (y, x)
                                                  //> cross  : scala.collection.immutable.IndexedSeq[(Int, Int)] = Vector((0,0), (
                                                  //| 0,1), (0,2), (0,3), (0,4), (1,0), (1,1), (1,2), (1,3), (1,4), (2,0), (2,1), 
                                                  //| (2,2), (2,3), (2,4), (3,0), (3,1), (3,2), (3,3), (3,4))
	val nonEmpty = cross.filter(p => f(p._1)(p._2) != ' ')
                                                  //> nonEmpty  : scala.collection.immutable.IndexedSeq[(Int, Int)] = Vector((3,2)
                                                  //| )
	val xMin = nonEmpty.map(_._2).min         //> xMin  : Int = 2
}
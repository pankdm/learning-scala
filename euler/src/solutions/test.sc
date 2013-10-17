package solutions

object test {
	val s1 = "111"                            //> s1  : String = 111
	val s2 = "2343"                           //> s2  : String = 2343
	s1 zip s2                                 //> res0: scala.collection.immutable.IndexedSeq[(Char, Char)] = Vector((1,2), (1,
                                                  //| 3), (1,4))
}
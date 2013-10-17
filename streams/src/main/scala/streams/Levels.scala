package streams

/**
 * A level constructed using the `InfiniteTerrain` trait which defines
 * the terrain to be valid at every position.
 */
object InfiniteLevel extends Solver with InfiniteTerrain {
  val startPos = Pos(1, 3)
  val goal = Pos(5, 8)
}

/**
 * A simple level constructed using the StringParserTerrain
 */
abstract class Level extends Solver with StringParserTerrain

object Level0 extends Level {
  val level =
    """ |------
        |--ST--
        |--oo--
        |--oo--
        |------""".stripMargin
}

/**
 * Level 1 of the official Bloxorz game
 */
object Level1 extends Level {
  val level =
    """ |ooo-------
        |oSoooo----
        |ooooooooo-
        |-ooooooooo
        |-----ooToo
        |------ooo-""".stripMargin
}

object Level7Part extends Level {
  val level =
    """ |--------oooo---
		|--------oooo---   
		|ooo-----o--oooo
		|ooooooooo---oTo
		|ooo----ooS--ooo
		|ooo----ooo--ooo
		|-ooo---o-------
		|--oooooo""".stripMargin
}

// level 11 | 291709
object Level11 extends Level {
  val level =
    """ |-oooo-
		|-oToo-
		|-ooo--
		|-o---oooooo-
		|-o---oo--oo-
		|Soooooo--ooo
		|-----o-----o
		|-----oooo--o
		|-----ooooooo
		|-------ooo-""".stripMargin
}

// level 12 | 958640
object Level12Part1 extends Level {
  val level =
    """ |------------o
	    |-----ooo--ooo
	    |-----oTooooo-
	    |---ooooo--oo-
	    |---ooo----oo-
	    |-ooooo---oooo
	    |ooSo-----oooo
	    |oooo--ooooo--
	    |-----ooo-----
	    |-----ooo-----""".stripMargin
}

object Level12Part2 extends Level {
  val level =
    """ |------------T
	    |-----ooo--ooo
	    |-----oSooooo+
	    |---ooooo--oo-
	    |---ooo----oo-
	    |-ooooo---oooo
	    |oooo-----oooo
	    |oooo--ooooo--
	    |-----ooo-----
	    |-----ooo-----""".stripMargin
}

object Level12Part3 extends Level {
  val level =
    """ |------------S
	    |-----ooo--ooo
	    |-----ooooooo+
	    |---ooooo--oo-
	    |---oTo+---oo-
	    |-ooooo---oooo
	    |oooo-----oooo
	    |oooo--ooooo--
	    |-----ooo-----
	    |-----ooo-----""".stripMargin
}

// level 13 | 448106
object Level13 extends Level {
  val level = 
    """ |ooo#oooo#oooo--
		|oo--------ooo--
		|oo---------ooo-
		|ooo---ooo--oSo-
		|ooo###oTo--ooo-
		|ooo--#ooo--o---
		|--o--#####oo---
		|--ooo##o###----
		|---oo######----
		|---ooo--oo-----""".stripMargin('|')
}

// level 14 | 210362 
object Level14Part1 extends Level {
  val level = 
	""" |--------ooo
		|---ooo--ooo
		|o--oSooooooooo
		|o--ooo------oo
		|o-----------oo
		|o-----------oo
		|o-------oooooo
		|ooooo---ooo---
		|-oooo---ooo---
		|--ooo---oooooT""".stripMargin('|')
}

object Level14Part2 extends Level {
  val level = 
	""" |--------ooo
		|---ooo--ooo
		|o--ooooooooooo
		|o++ooo------To
		|o-----------oo
		|o-----------oo
		|o-------oooooo
		|ooooo---ooo---
		|-oooo---ooo---
		|--ooo---oooooS""".stripMargin('|')
}

object Level14Part3 extends Level {
  val level = 
	""" |--------ooo
		|---ooo--ooo
		|o++ooooooooooo
		|o++ooo------So
		|o-----------oo
		|o-----------oo
		|o-------oooooo
		|ooooo---ooo---
		|-ooTo---ooo---
		|--ooo---oooooo""".stripMargin('|')
}

object Level18Last extends Level {
  val level =
	""" |--S-
		|--o-
		|--o-
		|-ooo
		|ooTo
		|oooo""".stripMargin
}

// level 21 | 728724
object Level21Part1 extends Level {
  val level =
	""" |--------oo-
		|-------ooo-
		|oo--oooooo-
		|oSoooo--o--
		|oooo----o---ooo
		|-oo-----Toooooo
		|--o-----oo--ooo
		|--ooo---oo
		|---ooo--oo
		|----oooooo""".stripMargin
}

object Level21Part2 extends Level {
  val level =
	""" |--------oo-
		|-------ooo-
		|oo--oooooo-
		|oooooo--o--
		|oooo----o---ooo
		|-oo-----Soooooo
		|--o-----To--ooo
		|--ooo---oo
		|---ooo--oo
		|---+oooooo""".stripMargin
}

object Level21Part3 extends Level {
  val level =
	""" |--------oo-
		|-------ooo-
		|oo--oooooo-
		|oooooo--o--
		|oooo----o---ooo
		|-oo-----oooooTo
		|--o-----So--ooo
		|--ooo+--oo
		|---ooo--oo
		|---+oooooo""".stripMargin
}

// level 22 | 987319
object Level22Part1 extends Level {
  val level =
	""" |-----oo----ooo
		|---oooooo--ooo
		|oooooooooooooo
		|oSooo--ooooo--
		|ooo------ooo--
		|-o--------o---
		|-o--------o---
		|-o-------oo---
		|-oo------oo---
		|--o------T----""".stripMargin
}

object Level22Part2 extends Level {
  val level =
	""" |-----oo----ooo
		|---oooooo--ooo
		|oooooo-ooooooo
		|oooo---ooooo--
		|ooo------ooo--
		|-o--------o---
		|-o--------o---
		|-o+------oo---
		|-oo------oo---
		|--T------S----""".stripMargin
}

object Level22Part3 extends Level {
  val level =
	""" |-----oo----ooo
		|---oooooo--oTo
		|oooooo-ooooooo
		|oooo---ooooo+-
		|ooo------ooo--
		|-o--------o---
		|-o--------o---
		|-o+------oo---
		|-oo------oo---
		|--S------o----""".stripMargin
}

// level 23 | 293486
object Level23Part1 extends Level {
  val level =
	""" |----ooooo---
		|----ooo-----
		|----ooo-----
		|----###-----
		|ooo#####oooo
		|oSo#####oTo-
		|ooo#####ooo-
		|ooooo-------""".stripMargin
}

object Level23Part2 extends Level {
  val level =
    """ |ooo--
	    |oSo--
	    |ooo---ooooo---
		|oooo--oTo-----
		|---o--ooo-----
		|---o--###-----
		|--ooo#####oooo
		|--ooo#####ooo-
		|--ooo#####ooo-
		|--ooooo+------""".stripMargin
}
// level 25 | 250453

package solutions

object p430 {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(343); 
  // sum(pi), pi - probability of i-th index to show
  // qi - probability to change at step
  // pi = sum by 2t of [ C(nt, 2t) * qi^(2t) * (1 - qi)^(nt - 2t) ]
  // total = C(n, 2) + n = n * (n - 1) / 2 + n = (n^2 - n + 2n) / 2
  // i -> 0 ... n - 1
  // i * (n - i + 1) / total^2
  def sqr(x: Double) = x * x;System.out.println("""sqr: (x: Double)Double""");$skip(427); 
  def solve(n: Long, steps: Int): Double = {

    val total = n * n

    val qs = (0 until n.toInt).map(_.toDouble).map(i => (total - sqr(i) - sqr(n - 1 - i)) / total)
    def findAfter(q: Double, steps: Int, res: Double): Double = {
      if (steps == 0) res
      else findAfter(q, steps - 1, res * (1 - q) + (1 - res) * q)
    }

    val res = qs.map(findAfter(_, steps, 1))
    println(res)
    res.foldLeft(0.)(_ + _)
  };System.out.println("""solve: (n: Long, steps: Int)Double""");$skip(14); val res$0 = 
  solve(3, 1);System.out.println("""res0: Double = """ + $show(res$0));$skip(10); val res$1 = 
  10. / 9;System.out.println("""res1: Double(1.1111111111111112) = """ + $show(res$1));$skip(17); val res$2 = 
  
  solve(3, 2);System.out.println("""res2: Double = """ + $show(res$2));$skip(9); val res$3 = 
  5. / 3;System.out.println("""res3: Double(1.6666666666666667) = """ + $show(res$3));$skip(18); val res$4 = 
  
  solve(10, 4);System.out.println("""res4: Double = """ + $show(res$4));$skip(8); val res$5 = 
  5.157;System.out.println("""res5: Double(5.157) = """ + $show(res$5));$skip(17); val res$6 = 

	solve(100, 10);System.out.println("""res6: Double = """ + $show(res$6));$skip(18); val res$7 = 
	solve(100, 4000);System.out.println("""res7: Double = """ + $show(res$7));$skip(8); val res$8 = 
	51.893;System.out.println("""res8: Double(51.893) = """ + $show(res$8))}
}

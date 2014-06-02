

SNF (Tag Bold (Tag Bold (Seq A B)))
	-> Tag Bold (Seq A B);

SNF (Tag Bold (Seq (Seq A B) (Tag Bold B)))
	-> Tag Bold (Seq A (Seq B B));


SNF (Tag Maj (Tag Bold (Seq A B)))
	-> Tag Bold (Tag Maj (Seq A B));

SNF (Tag Emph (Tag Maj (Tag Bold (Tag Maj (Tag Emph (Tag Bold (A)))))))
	-> Tag Bold (Tag Emph (Tag Maj (A)));

SNF (Seq (Seq A B) A)
	-> Seq A (Seq B A);

SNF (Seq (Tag Bold A) (Tag Bold B))
	->Tag Bold (Seq A B);

{
SNF (Tag Maj (Seq (Tag Bold (Tag Emph (Tag Bold (Tag Bold B)))) (Seq (Tag Bold (Seq (Seq A B) (Seq B A))) (Tag Bold (Tag Maj (Seq A B))))))
	-> A;
}
 
SNF(Seq (Seq A (Seq B C)) D) -> Seq A (Seq B (Seq C D));

{
// example
Seq (Seq d1 d2) d3  -> Seq d1 (Seq d2 d3)
Seq (Seq A (Seq B C)) D -> 
d1 = A
d2 = (Seq B C)
d3 = D
Seq A (Seq (Seq B C) D)

}
{
 SNF (Seq (Tag Bold (Seq (Seq (Seq A A) B) (Seq A B))) 
 	(Seq (Seq (Seq (Tag Emph (Tag Emph A)) A) B) (Seq A (Tag Maj (Tag Maj B)))))
 -> A;
}


.
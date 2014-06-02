
Add (Move Z) y => y;

Add (Move (S x)) y => S (Add (Move x) y);

S (Done x) => Done (S x);

Add (Done x) (Next y) => Add (Done x) (Compute y);

Add (Done Z) (Done y) => Done y;

Add (Done (S x)) (Done y) => S (Add (Done x) (Done y));

Mult (Done x) (Next y) => Mult (Done x) (Compute y);

Mult (Done Z) (Done y) => Done Z;

Mult (Done (S x)) (Done y) => Add (Move y) (Mult (Done x) (Done y) );

Compute (S x) => S (Compute x);

Compute (Add x y) => Add (Compute x) (Next y);

Compute (Mult x y) => Mult (Compute x) (Next y);

Compute Z => Done Z;

Done x => x;

. { end of rules }

Remove q (Tag q x) => Remove q x;

Remove q (Tag p x) => Tag p (Remove q x);

Tag p (RDone x) => RDone (Tag p x);

Remove q (Seq x y) => Seq (Remove q x) (RemoveNext q y);

Seq (RDone x) (RemoveNext q y) => Seq (RDone x) (Remove q y);

Seq (RDone x) (RDone y) => RDone (Seq x y);

Remove q A => RDone A;
Remove q B => RDone B;
Tag q (Done (Tag q x)) => Tag q (Done x);

Tag Emph (Done (Tag Bold x)) => Tag Bold (Tag Emph (Done x));
Tag Maj (Done (Tag Bold x)) => Tag Bold (Tag Maj (Done x));
Tag Maj (Done (Tag Emph x)) => Tag Emph (Tag Maj (Done x));

Tag q (Done x) => Done (Tag q x);

Seq (Done x) (Next y) => Seq (Done x) (SNF y);

Seq (Done (Tag q x)) (Done (Tag q y)) => Done (Tag q (Seq x y));

Seq (Done (Seq x y)) (Done z) => Seq x (Seq (Done y) (Done z));

Seq (Done x) (Done y) => Done (Seq x y);

SNF (Seq x y)  => Seq (SNF x) (Next y);

SNF (Tag q doc) => Tag q (SNF (Remove q doc));
SNF (RDone doc) => SNF doc;

SNF => Done;

Done x => x;

.
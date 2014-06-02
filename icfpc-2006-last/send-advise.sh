
PIPE=pipe1

for f in xml-adv.hs xml-test.hs; do 
	echo 'sending ' $f
	full=advise-files/$f
	echo "rm $f" > $PIPE
	echo "/bin/umodem" $f ":::" > $PIPE
	cat $full > $PIPE
	echo ":::" > $PIPE
done

echo "./advise step xml-adv.hs xml-test.hs" > $PIPE
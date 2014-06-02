
for f in `ls -1 $1`; do 
	echo 'sending ' $f
	full=$1/$f
	echo "rm $full" > pipe
	echo "/bin/umodem" $f ":::" > pipe
	cat $full > pipe
	echo ":::" > pipe
done
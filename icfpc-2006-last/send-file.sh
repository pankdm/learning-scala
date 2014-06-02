
echo "rm 1.bas" > pipe
echo "/bin/umodem" 1.bas ":::" > pipe
cat $1 > pipe
echo ":::" > pipe
echo "/bin/qbasic 1.bas" > pipe
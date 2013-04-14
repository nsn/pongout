#!/bin/sh
OUTDIR=package
DESTDIR=$OUTDIR/pongout
FILE=$(find java/target/  -name pongout\*.jar |head -n1)
BASE=$(basename $FILE)
JAVACMD="java -Djava.library.path=./natives -jar $BASE"
rm -rf $OUTDIR
mkdir -p $DESTDIR
cp $FILE $DESTDIR
cp -r java/target/natives $DESTDIR

echo "#!/bin/sh\n${JAVACMD}" > $DESTDIR/pongout.sh
echo "${JAVACMD}" > $DESTDIR/pongout.bat

cd $OUTDIR && zip -r  pongout.zip pongout


#!/bin/sh
OUTDIR=package
DESTDIR=$OUTDIR/pongout
ZIPFILE=pongout.zip
FILE=$(find java/target/  -name pongout\*.jar |head -n1)
BASE=$(basename $FILE)
JAVACMD="java -Djava.library.path=./natives -jar $BASE"
rm -rf $OUTDIR
mkdir -p $DESTDIR
cp $FILE $DESTDIR
cp -r java/target/natives $DESTDIR

echo "#!/bin/sh\n${JAVACMD}" > $DESTDIR/pongout.sh
echo "${JAVACMD}" > $DESTDIR/pongout.bat

cd $OUTDIR 
zip -r ${ZIPFILE} pongout
cd ..

REMOTE=u69386259@nightspawn.com:satansoft.de/pongout
read -p "upload to ${REMOTE}? [y/N]" REPLY
[ "$REPLY" != "y" ] && exit 1
chmod ugo+r $OUTDIR/${ZIPFILE}
scp ./$OUTDIR/${ZIPFILE} ${REMOTE}


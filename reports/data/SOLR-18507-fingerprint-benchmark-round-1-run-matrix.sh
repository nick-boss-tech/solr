#!/bin/bash
# throwaway SOLR-18507 fingerprint matrix: phase x size x delete ratio x 5 fresh-JVM reps
cd /workspace/gates/bench-harness
J=/workspace/jdk/jdk-21.0.12.1+1/bin/java
CONF=/workspace/gates/bench-p3/solr/server/solr/configsets/_default/conf
OUT=out/matrix.csv
rm -f $OUT
echo "phase,docs,delratio,kind,rep,millis,liveDocs" > $OUT
for phase in p2 p3; do
  if [ $phase = p2 ]; then CP=$(cat cp-p2.txt):classes; else CP=$(cat cp-p3.txt):classes; fi
  for size in 10000 100000 1000000; do
    for ratio in 0 0.01 0.10 0.50; do
      home=homes/$phase-$size-$ratio
      for rep in 1 2 3 4 5; do
        echo "$(date -u +%FT%TZ) $phase $size $ratio rep$rep" >> out/matrix.log
        timeout 1500 $J -Xmx2g -cp "$CP" Bench $home $size $ratio $phase $OUT.tmp $CONF $rep >> out/matrix.log 2>&1
        [ -f $OUT.tmp ] && cat $OUT.tmp >> $OUT && rm -f $OUT.tmp
      done
    done
  done
done
echo "$(date -u +%FT%TZ) matrix done" >> out/matrix.log

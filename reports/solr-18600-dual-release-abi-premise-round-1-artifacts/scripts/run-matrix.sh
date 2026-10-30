#!/bin/bash
# usage: run-matrix.sh <outdir>
set -u
OUT=$1; mkdir -p "$OUT"
J17=/workspace/jdk/jdk-17.0.21+9/bin/java
R=/workspace/tmp/s18600/resolve/out
S=/workspace/tmp/s18600
deps() { grep -v -E "solr-(solrj|solrj-jetty|solrj-zookeeper|api)-" "$1"; }
DEPS101=$(deps $R/v101/classpath.txt | paste -sd: -)
DEPS100=$(deps $R/v100/classpath.txt | paste -sd: -)
declare -A CP
CP[proto]="$S/proto/solr/solrj/build/libs/solr-solrj-10.2.0-SNAPSHOT.jar:$S/proto/solr/solrj-jetty/build/libs/solr-solrj-jetty-10.2.0-SNAPSHOT.jar:$S/proto/solr/solrj-zookeeper/build/libs/solr-solrj-zookeeper-10.2.0-SNAPSHOT.jar:$S/proto/solr/api/build/libs/solr-api-10.2.0-SNAPSHOT.jar:$DEPS101"
CP[v100]="$(paste -sd: $R/v100/classpath.txt)"
CP[v101]="$(paste -sd: $R/v101/classpath.txt)"
CP[base]="$S/base/solr/solrj/build/libs/solr-solrj-10.2.0-SNAPSHOT.jar:$S/base/solr/solrj-jetty/build/libs/solr-solrj-jetty-10.2.0-SNAPSHOT.jar:$S/base/solr/solrj-zookeeper/build/libs/solr-solrj-zookeeper-10.2.0-SNAPSHOT.jar:$S/base/solr/api/build/libs/solr-api-10.2.0-SNAPSHOT.jar:$DEPS101"
CP[fork]="$S/fork/solr/solrj/build/libs/solr-solrj-10.2.0-SNAPSHOT.jar:$S/fork/solr/solrj-jetty/build/libs/solr-solrj-jetty-10.2.0-SNAPSHOT.jar:$S/fork/solr/solrj-zookeeper/build/libs/solr-solrj-zookeeper-10.2.0-SNAPSHOT.jar:$S/fork/solr/api/build/libs/solr-api-10.2.0-SNAPSHOT.jar:$DEPS101"
for c in c100 c101; do
  sha256sum $(find $S/consumers/$c/out -name '*.class' | sort) > $OUT/$c.classes.before.sha256
done
for c in c100 c101; do
  for t in v100 v101 base fork proto; do
    cls=Cohort100; [ $c = c101 ] && cls=Cohort101
    cp="$S/consumers/$c/out:${CP[$t]}"
    timeout 150 $J17 -cp "$cp" fixture.$cls > $OUT/$c-on-$t.out 2> $OUT/$c-on-$t.err; echo "exit=$?" >> $OUT/$c-on-$t.out
  done
done
for c in c100 c101; do
  sha256sum $(find $S/consumers/$c/out -name '*.class' | sort) > $OUT/$c.classes.after.sha256
  cmp -s $OUT/$c.classes.before.sha256 $OUT/$c.classes.after.sha256 && echo "$c class bytes unchanged" || echo "$c class bytes CHANGED"
done
echo done

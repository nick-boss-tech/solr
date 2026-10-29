#!/bin/bash
# Shows that an app compiled against SolrJ 10.0.0 breaks at runtime on 10.1.0 without recompiling,
# and works again once recompiled against 10.1.0.
set -u
D=$(cd "$(dirname "$0")" && pwd)
JAVA_HOME=${JAVA_HOME:-/workspace/jdk/jdk-21.0.12.1+1}
JAVAC=$JAVA_HOME/bin/javac
JAVA=$JAVA_HOME/bin/java
DEPS=$(cat "$D/deps.txt")
fetch() { # version artifact
  f="$D/lib/$1/$2-$1.jar"
  [ -f "$f" ] || curl -sfL -o "$f" "https://repo1.maven.org/maven2/org/apache/solr/$2/$1/$2-$1.jar" || { echo "could not fetch $2 $1"; exit 1; }
}
for v in 10.0.0 10.1.0; do fetch $v solr-solrj; fetch $v solr-api; done
J100=$D/lib/10.0.0/solr-solrj-10.0.0.jar:$D/lib/10.0.0/solr-api-10.0.0.jar
J101=$D/lib/10.1.0/solr-solrj-10.1.0.jar:$D/lib/10.1.0/solr-api-10.1.0.jar
rm -rf "$D/build" && mkdir -p "$D/build/app-10.0.0" "$D/build/app-10.1.0"

echo "1. compile the app against 10.0.0"
"$JAVAC" -cp "$J100:$DEPS" -d "$D/build/app-10.0.0" "$D/src/demo/Upgrade.java" || exit 1

echo "2. run the 10.0.0-compiled app on 10.0.0 jars"
"$JAVA" -cp "$D/build/app-10.0.0:$J100:$DEPS" demo.Upgrade 2>&1 | grep -v SLF4J | head -3

echo "3. run the SAME 10.0.0-compiled app on 10.1.0 jars (no recompile)"
"$JAVA" -cp "$D/build/app-10.0.0:$J101:$DEPS" demo.Upgrade 2>&1 | grep -v SLF4J | head -2

echo "4. recompile the app against 10.1.0"
"$JAVAC" -cp "$J101:$DEPS" -d "$D/build/app-10.1.0" "$D/src/demo/Upgrade.java" || exit 1

echo "5. run the 10.1.0-compiled app on 10.1.0 jars"
"$JAVA" -cp "$D/build/app-10.1.0:$J101:$DEPS" demo.Upgrade 2>&1 | grep -v SLF4J | head -3

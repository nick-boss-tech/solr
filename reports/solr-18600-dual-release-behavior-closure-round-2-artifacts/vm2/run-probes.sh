#!/bin/bash
# Behavioral probes for SOLR-18600 round-2 legs A (connect), B (custom client), C (URL dispatch), D (local requests).
# Each probe is compiled once against each release it targets, then run on the release and on the final-head jar.
set -u
C=$(cd "$(dirname "$0")" && pwd)
J=/workspace/jdk/jdk-21.0.12.1+1/bin
L=/workspace/poc/solr-10-binary-break/lib
DEPS=$(cat /workspace/poc/solr-10-binary-break/deps.txt)
FINAL=$C/build/solr-solrj-final.jar:$L/10.1.0/solr-api-10.1.0.jar
J100=$L/10.0.0/solr-solrj-10.0.0.jar:$L/10.0.0/solr-api-10.0.0.jar
J101=$L/10.1.0/solr-solrj-10.1.0.jar:$L/10.1.0/solr-api-10.1.0.jar
W=$C/probes/out; rm -rf "$W" && mkdir -p "$W"
cd "$C/probes/src"
comp() { mkdir -p "$W/$1" && "$J/javac" -nowarn -proc:none -d "$W/$1" -cp "$2:$DEPS" "$3" > "$W/$1.compile.log" 2>&1; echo "compile $1 rc=$?"; }
run() { "$J/java" -cp "$2:$DEPS" "$3" > "$W/$1.log" 2>&1; echo "run $1 rc=$? $(grep -c '^RESULT' "$W/$1.log") results"; grep '^RESULT' "$W/$1.log" | sed "s/^/  $1 /"; }
comp b100 "$J100" ProbeB.java; comp c100 "$J100" ProbeC.java; comp c101 "$J101" ProbeC.java; comp a100 "$J100" ProbeA.java
run b-10.0 "$W/b100:$J100" ProbeB; run b-final "$W/b100:$FINAL" ProbeB
run c-10.0-cohort-on-10.0 "$W/c100:$J100" ProbeC; run c-10.1-cohort-on-10.1 "$W/c101:$J101" ProbeC
run c-10.0-cohort-on-final "$W/c100:$FINAL" ProbeC; run c-10.1-cohort-on-final "$W/c101:$FINAL" ProbeC
run a-10.0 "$W/a100:$J100" ProbeA; run a-final "$W/a100:$FINAL" ProbeA

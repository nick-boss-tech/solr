#!/bin/bash
# Re-runs the frozen, already-compiled cohorts (cohorts/) on released jars and on the final-head jar.
# Each line: label, expected exit status, pass count, then the observed values.
set -u
C=$(cd "$(dirname "$0")" && pwd)
J=/workspace/jdk/jdk-21.0.12.1+1/bin
L=/workspace/poc/solr-10-binary-break/lib
M=/workspace/gates/logs/vm2/solr18600-additive/mods
DEPS=$(cat /workspace/poc/solr-10-binary-break/deps.txt)
MD=$(cat $C/cp-mods-deps.txt)
FINAL=$C/build/solr-solrj-final.jar:$L/10.1.0/solr-api-10.1.0.jar
J100=$L/10.0.0/solr-solrj-10.0.0.jar:$L/10.0.0/solr-api-10.0.0.jar
J101=$L/10.1.0/solr-solrj-10.1.0.jar:$L/10.1.0/solr-api-10.1.0.jar
JET100=$J100:$M/10.0.0/solr-solrj-jetty-10.0.0.jar
JET101=$J101:$M/10.1.0/solr-solrj-jetty-10.1.0.jar
OUT=$C/run-final
rm -rf "$OUT" && mkdir -p "$OUT"
mismatch=0
run() { # label expect_exit expect_pass classpath cohortdir main
  "$J/java" -cp "$5:$4:$DEPS" "$6" > "$OUT/$1.log" 2>&1; rc=$?
  pass=$(grep -c '^PASS' "$OUT/$1.log"); fail=$(grep -c '^FAIL' "$OUT/$1.log")
  ok=yes; { [ "$rc" = "$2" ] && [ "$pass" = "$3" ]; } || { ok=NO; mismatch=1; }
  echo "$1 exit=$rc expected=$2 pass=$pass expected=$3 fail=$fail expected-match=$ok"
}
run core-10.0-on-10.0 0 12 "$J100" "$C/cohorts/core-10.0" Cohort100
run core-10.1-on-10.1 0 12 "$J101" "$C/cohorts/core-10.1" Cohort101
run core-10.0-on-10.1-negative 1 0 "$J101" "$C/cohorts/core-10.0" Cohort100
run core-10.0-on-final 0 12 "$FINAL" "$C/cohorts/core-10.0" Cohort100
run core-10.1-on-final 0 12 "$FINAL" "$C/cohorts/core-10.1" Cohort101
run jetty-10.0-on-10.0 0 8 "$JET100:$MD" "$C/cohorts/jetty-10.0" Jetty100
run jetty-10.1-on-10.1 0 8 "$JET101:$MD" "$C/cohorts/jetty-10.1" Jetty101
run jetty-10.0-on-10.1-negative 1 0 "$JET101:$MD" "$C/cohorts/jetty-10.0" Jetty100
run jetty-10.0-on-final 0 8 "$FINAL:$M/10.1.0/solr-solrj-jetty-10.1.0.jar:$MD" "$C/cohorts/jetty-10.0" Jetty100
run jetty-10.1-on-final 0 8 "$FINAL:$M/10.1.0/solr-solrj-jetty-10.1.0.jar:$MD" "$C/cohorts/jetty-10.1" Jetty101
echo "all-expected=$([ $mismatch = 0 ] && echo yes || echo NO)"

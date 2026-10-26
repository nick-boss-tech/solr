#!/bin/bash
G=/workspace/gates/gates-vm2
W=/workspace/worktrees/teardown-ext-r1
LEDGER=$G/ext-attempt-ledger.txt
OUT=$G/ext-pair-r1
exec 8>/workspace/tools/.teardown-ext-pair.lock
if ! flock -n 8; then echo "refused: pair driver lock held" >&2; exit 9; fi
if [ -e "$OUT" ]; then echo "refused: $OUT exists (exclusive)" >&2; exit 10; fi
stale=$(ps -eo pid,stat,args | awk '$2 !~ /Z/' | grep -E "run-probe-r31\\.sh|GradleWorkerMain" | grep -v " grep " | head -3)
if [ -n "$stale" ]; then echo "refused: live probe or test processes present: $stale" >&2; exit 11; fi
mkdir "$OUT" || exit 10
echo "driver pid=$$ started=$(date -u +%FT%TZ)" >> $LEDGER
run_arm() {
  arm=$1; test=$2; name=$3
  mkdir "$OUT/$arm" || return 12
  echo "attempt $name arm=$arm test=$test pid=$$ start=$(date -u +%FT%TZ) runner=$G/pool-bin/sweep-run.sh" >> $LEDGER
  export JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1 GRADLE_USER_HOME=/workspace/gradle-home
  timeout 600 $G/pool-bin/sweep-run.sh $W "$OUT/$arm" $name --no-daemon --max-workers=2 \
    :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test \
    --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest.$test \
    -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1 \
    > "$OUT/$arm/runner-stdout.txt" 2>&1
  rc=$?
  echo "$name rc=$rc end=$(date -u +%FT%TZ)" >> "$OUT/$arm/status.txt"
  cp -r $W/solr/solrj-jetty/build/test-results/test "$OUT/$arm/xml" 2>/dev/null
  ev=$(grep -o "PROBE-EVIDENCE arm=$arm [^ ]*" "$OUT/$arm/runner-stdout.txt" "$OUT/$arm/$name.log" 2>/dev/null | head -1 | awk '{print $3}')
  [ -z "$ev" ] && ev=$(grep -rho "PROBE-EVIDENCE arm=$arm [^ ]*" $OUT/$arm/xml 2>/dev/null | head -1 | awk '{print $3}')
  echo "evidence_source=$ev" >> "$OUT/$arm/status.txt"
  [ -f "$ev" ] && cp "$ev" "$OUT/$arm/evidence.log" && sha256sum "$OUT/$arm/evidence.log" >> "$OUT/$arm/status.txt"
  return $rc
}
run_arm control testExternalExecutorCloseProbeControl ext-control-r1
crc=$?
clean=$(python3 -I $G/ext-check-control.py "$OUT/control/evidence.log" 2>/dev/null)
echo "control_check=$clean crc=$crc" >> "$OUT/control/status.txt"
if [ "$crc" = "0" ] && [ "$clean" = "CLEAN" ]; then
  run_arm interrupted testExternalExecutorCloseProbeInterrupted ext-interrupted-r1
  echo "interrupted_rc=$?" >> "$OUT/status.txt"
else
  echo "control not clean or failed; interrupted arm NOT run" > "$OUT/status.txt"
fi
echo "DONE $(date -u +%FT%TZ)" >> "$OUT/status.txt"
echo "end pair driver $(date -u +%FT%TZ)" >> $LEDGER

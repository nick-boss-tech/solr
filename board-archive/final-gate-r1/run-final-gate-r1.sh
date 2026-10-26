#!/bin/bash
export JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1 GRADLE_USER_HOME=/workspace/gradle-home
G=/workspace/gates/gates-vm2; L=/workspace/gates/gates-vm2/logs/final-gate-r1; W=/workspace/worktrees/teardown-final-r1
F=5ec461c4012036faf3fd14363202e82c4686fb07
exec 9>/workspace/tools/.teardown-final-gate.lock
flock -n 9 || { echo "refused: driver lock held" >&2; exit 9; }
[ -e $L/status.txt ] && { echo "refused: $L/status.txt exists" >&2; exit 10; }
echo "driver pid=$$ started=$(date -u +%FT%TZ)" > $L/status.txt
cd $W || exit 1
[ "$(git rev-parse HEAD)" = "$F" ] || { echo "HEAD mismatch" >> $L/status.txt; exit 1; }
[ -z "$(git status --porcelain)" ] || { echo "tree dirty" >> $L/status.txt; exit 1; }
leg() {
  name=$1; shift
  timeout 1800 $G/pool-bin/sweep-run.sh $W $L $name --no-daemon --max-workers=2 "$@" > /dev/null 2>&1
  echo "$name rc=$?" >> $L/status.txt
  [ -d $L/$name.xml ] && echo "$name xml_files=$(ls $L/$name.xml | wc -l)" >> $L/status.txt
}
leg tidy tidy
leg errorprone-compile --no-build-cache :solr:solrj-jetty:clean :solr:solrj-jetty:compileJava :solr:solrj-jetty:compileTestJava -Pvalidation.errorprone=true
leg module-check :solr:solrj-jetty:check -x test -Pvalidation.errorprone=true
leg module-suite :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1
echo "tree_after: $(git status --porcelain | wc -l) changes" >> $L/status.txt
echo "DONE $(date -u +%FT%TZ)" >> $L/status.txt

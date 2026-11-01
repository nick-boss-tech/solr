#!/bin/bash
# Gate for SOLR-16065 at a given head, run in an existing worktree on the main base:
# changelog parse, tidy, Error Prone compile, focused tests, module check, fail-before on the base.
# usage: gate-16065.sh <worktree-dir> <head-sha> <base-sha>
# Serialized by the host build lock. Logs go to /workspace/logs/vm2/gate-16065/.
set -u
WT=$1; HEAD_SHA=$2; BASE=$3
TAG=$(echo "$HEAD_SHA" | cut -c1-10)
L=/workspace/logs/vm2/gate-16065
SEED=16065C0FFEE16065
F=solr/core/src/java/org/apache/solr/cloud/Overseer.java
T='org.apache.solr.cloud.OverseerTest'
R='org.apache.solr.cloud.OverseerRolesTest'
N='org.apache.solr.cloud.OverseerTest.testOverseerExitRemovesLeaderNodeBeforeClose'
export JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1 GRADLE_USER_HOME=/workspace/gradle-home
mkdir -p "$L"
exec 9>/workspace/tools/.gradle-vm2.lock
flock 9
cd "$WT" || { echo "no worktree $WT"; exit 1; }
[ "$(git rev-parse HEAD)" = "$HEAD_SHA" ] || { echo "HEAD is not $HEAD_SHA"; exit 1; }
git status --porcelain | grep -q . && { echo "tree not clean"; exit 1; }
uv run --with pyyaml python -c "import yaml; yaml.safe_load(open('changelog/unreleased/SOLR-16065-overseer-exit-check-inline.yml')); print('yaml rc=0')" > $L/$TAG-yaml.log 2>&1; echo "yaml_rc=$?" >> $L/$TAG-yaml.log
env -u NODE_ENV ./gradlew tidy --console=plain > $L/$TAG-tidy.log 2>&1; echo "tidy_rc=$?" >> $L/$TAG-tidy.log
git status --porcelain >> $L/$TAG-tidy.log
env -u NODE_ENV ./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true --console=plain > $L/$TAG-errorprone.log 2>&1; echo "errorprone_rc=$?" >> $L/$TAG-errorprone.log
env -u NODE_ENV ./gradlew :solr:core:cleanTest :solr:core:test --tests "$T" --tests "$R" -Ptests.seed=$SEED --console=plain > $L/$TAG-focused.log 2>&1; echo "focused_rc=$?" >> $L/$TAG-focused.log
mkdir -p $L/$TAG-xml-focused && cp solr/core/build/test-results/test/TEST-*.xml $L/$TAG-xml-focused/ 2>/dev/null
env -u NODE_ENV ./gradlew :solr:core:check -x test --console=plain > $L/$TAG-check.log 2>&1; echo "check_rc=$?" >> $L/$TAG-check.log
git show $BASE:$F > $F
for i in 1 2 3; do
  env -u NODE_ENV ./gradlew :solr:core:cleanTest :solr:core:test --tests "$N" -Ptests.seed=$SEED --console=plain > $L/$TAG-failbefore-base-$i.log 2>&1; echo "failbefore_rc=$?" >> $L/$TAG-failbefore-base-$i.log
  mkdir -p $L/$TAG-xml-failbefore-$i && cp solr/core/build/test-results/test/TEST-*.xml $L/$TAG-xml-failbefore-$i/ 2>/dev/null
done
git checkout -- $F
git status --porcelain >> $L/$TAG-failbefore-base-3.log
env -u NODE_ENV ./gradlew --stop >/dev/null 2>&1
echo GATE DONE

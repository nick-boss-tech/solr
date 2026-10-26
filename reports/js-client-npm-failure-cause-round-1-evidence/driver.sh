#!/bin/bash
export JAVA_HOME=/workspace/tools/jdk21 GRADLE_USER_HOME=/workspace/gradle-home
S=/workspace/tmp/pool-bin/sweep-run.sh
W=/workspace/worktrees/js-probe; L=/workspace/gates/logs/vm4/pr-5204-js
echo "A-start $(date -u +%FT%TZ) NODE_ENV=${NODE_ENV:-unset}" >> $L/status
env NODE_ENV=production $S $W $L legA-nodeenv-production :solr:webapp:js-client:jsClientDownloadDeps --console=plain
echo "A-end rc=$? $(date -u +%FT%TZ)" >> $L/status
rm -rf $W/solr/webapp/js-client/build/jsClientWorkspace
echo "B-start $(date -u +%FT%TZ) NODE_ENV-unset" >> $L/status
env -u NODE_ENV $S $W $L legB-nodeenv-unset :solr:webapp:js-client:jsClientDownloadDeps --console=plain
echo "B-end rc=$? $(date -u +%FT%TZ)" >> $L/status
echo DONE >> $L/status

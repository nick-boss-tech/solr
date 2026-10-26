#!/usr/bin/env bash
set -u
W=/workspace/tmp/pr5188; L=/workspace/gates/logs/vm6/pr5188-round-2-superseding-409183a
S=/workspace/solr; P=$W/pre; SEED=5188C0DE
export JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1 GRADLE_USER_HOME=/workspace/gradle-home PATH=/workspace/jdk/jdk-21.0.12.1+1/bin:$PATH
unset NODE_ENV
exec 9>/workspace/tools/.gradle-vm6.lock
flock 9
echo "lock acquired $(date -u +%H:%M:%SZ)" >> $L/status.txt
leg() { # name dir args...
  local name=$1 dir=$2; shift 2
  echo "$name start $(date -u +%H:%M:%SZ)" >> $L/status.txt
  (cd "$dir" && ./gradlew --no-daemon --max-workers=1 -Ppython3.exe=python3 "$@") > $L/$name.log 2>&1
  echo "$name rc=$? end $(date -u +%H:%M:%SZ)" >> $L/status.txt
  rm -rf $L/$name-xml; cp -a "$dir/solr/core/build/test-results/test" $L/$name-xml 2>/dev/null || true
}
leg tidy $S tidy
echo "tidy-tree: $(cd $S && git status --porcelain | wc -l) changed" >> $L/status.txt
leg pre-snippets $P :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testGlobHighlightSnippets -Ptests.seed=$SEED -Ptests.workDir=$W/work-r2-pre-snip
leg pre-rtg $P :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testGlobRTGStoredFieldLoading -Ptests.seed=$SEED -Ptests.workDir=$W/work-r2-pre-rtg
leg pre-prefetch $P :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testGlobHighlightPrefetch -Ptests.seed=$SEED -Ptests.workDir=$W/work-r2-pre-pf
leg post-focused $S :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testGlobRTGStoredFieldLoading --tests org.apache.solr.BasicFunctionalityTest.testGlobHighlightPrefetch --tests org.apache.solr.BasicFunctionalityTest.testGlobHighlightSnippets --tests org.apache.solr.search.TestPseudoReturnFields.testGlobRTGUncommittedFields -Ptests.seed=$SEED -Ptests.workDir=$W/work-r2-post-focused
leg post-suites $S :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest --tests org.apache.solr.search.TestPseudoReturnFields --tests org.apache.solr.response.TestRetrieveFieldsOptimizer --tests org.apache.solr.search.ReturnFieldsTest --tests org.apache.solr.highlight.TestUnifiedSolrHighlighter -Ptests.seed=$SEED -Ptests.workDir=$W/work-r2-post-suites
leg compile $S :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true
leg check $S :solr:core:check -x test
echo "driver done $(date -u +%H:%M:%SZ)" >> $L/status.txt

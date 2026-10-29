# Receipt: SOLR-16398 replication commands + debug dump (JAX-RS) gate

- Runner: phone agent (this VM), 2026-10-29
- Branch: solr-16398-replication-commands
- Head gated: e1939da6e0e7cc5bd68e44193084e35fc970b218
- Base: origin/main 3db71000514e1edaa8e6ae0426fce92ca97990e3
- Seed (focused tests): 16398C0FFEE002
- Verdict: PASS at the head above.

## Legs at the gated head

- Step 0 changelog YAML parse: OK (changelog/unreleased/SOLR-16398-replication.yml).
- Step 1 tidy: rc 0, tree clean.
- Step 2 Error Prone compile (:solr:api, :solr:core, :solr:solrj compileJava,
  :solr:core and :solr:api compileTestJava, -Pvalidation.errorprone=true): rc 0.
  The SolrJ OpenAPI generation leg inside this step passes at the gated head.
- Step 3 focused tests: CoreReplicationAPITest 18 tests, 0 failures, 1 skipped
  (testValidateFilenameRejectsWindowsAbsolutePath skips on a platform assumption);
  TestReplicationHandler 25 tests, all 25 skipped: every test in that class is in
  the Lucene nightly group, which the run does not enable; that is the class's
  standing behavior, not an effect of this branch. Counts read from the fresh
  JUnit XML. The branch's working replication coverage at this gate is
  CoreReplicationAPITest.
- Step 4 :solr:core:check -x test: rc 0, BUILD SUCCESSFUL.

## Path to the gated head (fixes by the phone agent, each pushed before re-gating)

- bb6f0e7d5d7 (as implemented): tidy import order in ReplicationHandler;
  formatting-only commit eaf0141332 on top.
- eaf0141332: OpenAPI generation failed: the six new ReplicationApis operations
  did not declare the {coreName} path parameter; @CoreApiParameters added in
  1f11ad8c2f, and the same fix for DebugDumpApi in 11d159b95d.
- 11d159b95d: core compile failed (DebugDump imported JerseyResource from the
  wrong package, org.apache.solr.jersey instead of org.apache.solr.api), and
  CoreReplication set a Long into ModifiableSolrParams for a fetch-index
  indexVersion body field. The v1 fetchindex path reads no such parameter
  (IndexFetcher always resolves the leader's latest version), so the field and
  its mapping were removed rather than passed as a dead parameter; import fixed;
  both in b941aadf09. Tidy re-sorted the corrected import (commit 07678f5155).
- 07678f5155: Error Prone flagged two fully qualified LinkedHashMap uses in the
  CoreReplication response harvest; imported in 218aa604a4, with explicit type
  arguments restored in 956d95d66a (the diamond form under var inferred
  Map<Object, Object>).
- 956d95d66a: Error Prone flagged fully qualified Collection and List in
  DumpRequestHandler.getJerseyResources; de-qualified in e1939da6e0, the gated
  head. A sweep of the full branch diff for other fully qualified java.util
  uses in added code came back empty before this run.

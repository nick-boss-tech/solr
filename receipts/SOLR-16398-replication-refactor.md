# Receipt: SOLR-16398 replication commands, typed-method refactor re-gate

- Runner: phone agent (this VM), 2026-10-29
- Branch: solr-16398-replication-commands
- Head gated: 559d0f551617b20a90015f7f6437fa10a9988d97
- Base: origin/main 3db71000514e1edaa8e6ae0426fce92ca97990e3 (unchanged from the
  first gate; the three main commits since, including PR #5218, touch no
  replication files)
- Seed (focused tests): 16398C0FFEE002
- Verdict: PASS at the head above. This supersedes the first gate's receipt
  (receipts/SOLR-16398-replication.md, head e1939da6e0) as the branch's current
  evidence; that receipt's fix-round history stands as the record of the first
  implementation.

## What changed in the refactor (commit 559d0f5516)

At the owner's direction, the six command operations were moved off the
v1-dispatcher delegation (building ModifiableSolrParams and running
ReplicationHandler.handleRequestBody, then harvesting the SolrQueryResponse)
onto the pattern of the replication APIs Jalaz Kumar landed on this ticket:

- ReplicationHandler gained typed methods: fetchIndex(String, Boolean),
  deleteBackup(name, repoName, location, commitName), restoreBackup(name,
  location, repoName), and enablePoll/disablePoll returning whether a polling
  fetcher exists (abortFetch was already typed). The v1 command dispatch calls
  the same typed methods, translating outcomes into the v1 response exactly as
  before, so v1 behavior is unchanged.
- ReplicationAPIBase gained the matching doX helpers; CoreReplication's six
  operations are one-line delegations returning SolrJerseyResponse.
- The generic ReplicationCommandResponse model (status plus a harvested details
  map) is deleted; no other code used it.
- DumpRequestHandler's dump construction was extracted to dumpRequestValues,
  called by both the v1 handler and the v2 DebugDump resource, which no longer
  round-trips through handleRequestBody.
- Ten new CoreReplicationAPITest cases in the ticket's existing mock style
  cover the delegation of each new operation and the error paths (abort-fetch
  and poll toggles with no follower, delete-backup without a name). The six
  commands previously had no test coverage.

## Legs at the gated head

- Step 0 changelog YAML parse: OK.
- Step 1 tidy: rc 0, tree clean.
- Step 2 Error Prone compile (same module set as the first gate,
  -Pvalidation.errorprone=true): rc 0, including SolrJ OpenAPI generation.
- Step 3 focused tests: CoreReplicationAPITest 28 tests, 0 failures, 1 skipped
  (the platform-assumption filename test, as before); TestReplicationHandler
  25 of 25 skipped (nightly group, standing behavior). Counts read from the
  fresh JUnit XML.
- Step 4 :solr:core:check -x test: rc 0, BUILD SUCCESSFUL.

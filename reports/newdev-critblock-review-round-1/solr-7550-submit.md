# Review: SOLR-7550, branch solr-7550-submit (unit 25)

Head reviewed: fcc8a874abbb8fbd465a835da6903f64de7009b2. Base: adf27bf2ad, the parent of the branch's first SOLR-7550 commit (7bde698fc3). Ticket: SOLR-7550, "PeerSync fails if a replica returns 500 error".

## Pass 1

Review (2026-10-22, vm4):

- Ticket fit. The ticket reports that a replica whose core failed to load returns a 500 on the versions request, and PeerSync then blocks leader election. The branch narrows the existing handling so that a 500 counts as success only when the remote error class is `SolrCoreInitializationException`. That matches the reported case.
- Behaviour change, owner item (rung 5, scope). Other 500 responses to the versions request now fail the sync. Before this change, any 500 counted as success when `cantReachIsSuccess` was set. `testPeerSyncStillFailsOtherServerErrorFromVersionRequest` pins the new behaviour. The ticket asks for the core-failed case only, so the narrowing looks right, but the owner should confirm that other 500s should block the sync. Recorded, not changed.
- Finding, major: the check reads `SolrException.getThrowable()` (the `error-class` metadata). The tests build that metadata in-process with `ResponseUtils.getErrorInfo`, not from a real HTTP response from a replica whose core failed to load. No test in the branch shows the remote side sending `SolrCoreInitializationException` for a failed core on the versions path. The branch does not prove the live case; the ticket's cluster repro is not reproduced here. Recorded, not repaired (a cluster test is outside the repair scope).
- Finding, minor: the changelog title was rewritten at this head to describe the narrowed behaviour. The text matches the code. Changelog YAML parses (step 0).
- Finding, nit: none in the production change. The test helper `serverError` has a `@SuppressWarnings("unchecked")` cast of the metadata NamedList. Acceptable for a test.
- Tests. `PeerSyncHandleResponseTest` has three tests (core-init counts as success, other 500 still fails, core-init on the updates purpose still fails). With the core-init branch removed, the core-init success test fails with AssertionError, as expected (proof leg).

Repairs: none. The two findings above are recorded for the owner.

Regate: `gates/SOLR-7550-review-pass-1.md`, GATE GREEN at fcc8a874ab on vm4. Log directory `/workspace/gates/logs/vm4/7550/`.

## Pass 2

Review (2026-10-22, vm4), same head fcc8a874ab:

- Open pass 1 finding, major (no live-path test), narrowed. Traced the server path: \`CoreContainer.getCore\` throws \`SolrCoreInitializationException\` for a core in the init-failure map (\`CoreContainer.java:2222\`). \`HttpSolrCall\` does not wrap it (\`HttpSolrCall.java:259-273\`). \`ResponseUtils.getErrorInfo\` writes \`error-class\` as the thrown class name (\`ResponseUtils.java:77\`). The client's \`SolrException.getThrowable()\` reads that metadata, so the fix's class-name check matches what a replica with a failed core sends. The code path is consistent end to end. A cluster repro (the ticket's four-node restart) is still not run, and the branch has no test that goes through HTTP. Recorded, not repaired (outside the repair scope).
- Owner item (rung 5, scope), unchanged from pass 1: other 500s on the versions request now fail the sync where they used to count as success.
- Repairs: none.

Regate: \`gates/SOLR-7550-review-pass-2.md\`, GATE GREEN at fcc8a874ab on vm4.

## Pass 3

Review (2026-10-22, vm4), same head fcc8a874ab. Re-read the three commits' diff and the test diff against the pass 1 and pass 2 findings. No new findings.

- Open owner items, unchanged: (1) other 500s on the versions request now fail the sync where they counted as success before (rung 5, scope); (2) no cluster-level test for the ticket's four-node restart; the error-class path is traced in code (`CoreContainer.java:2222`, `HttpSolrCall.java:259-273`, `ResponseUtils.java:77`).
- Repairs: none.

Regate: `gates/SOLR-7550-review-pass-3.md`, GATE GREEN at fcc8a874ab on vm4.


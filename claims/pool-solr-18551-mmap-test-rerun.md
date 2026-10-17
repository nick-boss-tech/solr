# Claim: SOLR-18551 focused IndexFetcher mmap test rerun

- Claimant host: vm4 (Linux). Assignment: assignments/pool-solr-18551-mmap-test-rerun.md.
- Slice taken: the whole assignment (head run, fail-before run, report).
- Capability tags used: settling-run.
- Started: 2026-10-17T04:56Z (UTC). Branch solr-18551-submit at 02c82ea6c8858495c4b1b2286167b77628d89485 (to be confirmed with ls-remote at run time). Base 714d6ceff852e6dc743ef51ae094e4eaf8f3b3f9.
- Note: the assignment names vm2 as the intended host. Nick confirmed on 2026-10-17 that vm2, vm3 and vm4 share this infrastructure and any of them may take it.
- Source changes: none. No PR, comment, JIRA or email.

Heartbeat: 2026-10-17T04:56Z (claim).
- 2026-10-17T05:18Z: head run GREEN at 02c82ea6c8858495c4b1b2286167b77628d89485, IndexFetcherTest 3 of 3 passed (0 failures, 0 errors, 0 skipped).
- 2026-10-17T05:28Z: fail-before GREEN as expected. Base 714d6ceff852e6dc743ef51ae094e4eaf8f3b3f9 with the test file overlaid: 3 of 3 failed at `assertEquals(0, dir.openInputCalls)` (IndexFetcherTest.java lines 45, 59, 68), `expected:<0> but was:<1>`.
- Status: DONE (2026-10-17T05:28Z). Deliverable: reports/SOLR-18551-indexfetcher-mmap-rerun.md.

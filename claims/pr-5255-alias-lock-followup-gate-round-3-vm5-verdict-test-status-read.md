# Verdict: PR 5255 alias-lock follow-up gate, round 3 (vm5), not green

- Claim: `claims/pr-5255-alias-lock-followup-gate-round-3-vm5.md`. Gate head `bf5098b3da32d3245b5f04ae50825bc8b005330a`.
- Tidy passed at that head with a clean tree. The test code compiled.
- Focused classes, seed `5255C0DE`: 6 tests executed, 2 failed. Seed `18589C0DE`: 6 tests executed, the same 2 failed. Logs: `/workspace/gates/logs/vm5/pr-5255-alias-lock-followup-round-3/focus-5255.log` and `focus-18589.log`, with XML under `xml-focus-5255/` and `xml-focus-18589/`.
- Failed: `AliasCollectionApiOverseerTest.testAsyncMultiCollectionAliasPreservesErrorDetails` and `AliasCollectionApiLockTest.testAliasRetargetedWhileWaitingDoesNotDeleteUnlockedCollection`.
- Cause, from a single-method probe (temporary print, reverted): SolrJ `CollectionAdminRequest.RequestStatus.waitFor` deletes the async status once it reads COMPLETED or FAILED. The tests then requested the status again, and the server answered `notfound`, so no exception was available. The production failure path was not reached by the read. This is a test read-order defect, not a finding about the production exception path.
- Neighboring suites and the root check did not run at this head; the runner was stopped to free the host lock for the probe.
- Repair: `52dacc23b5bb67d254c111c948733da7bc4a9f81` on `nick-boss-tech/solr:solr-18589-submit`, a fast-forward above `bf5098b3da`. Both test files now poll with a TimeOut until the status reads FAILED, then read the status once. The waitFor deletion is avoided. Assertions are unchanged. Pre-check at that head: spotless clean, 6 tests on seed `5255C0DE`, 0 failures, 0 errors, 0 skips.
- Superseding gate: `gates/pr-5255-alias-lock-followup-round-4.md`, at `52dacc23b5`, with claim `claims/pr-5255-alias-lock-followup-gate-round-4-vm5.md`.

# SolrJ and clients round 1, part S5: cloud client and requests (SOLR-14967, SOLR-17866, SOLR-3498, SOLR-11356, SOLR-14187)

Worktree at `5bbcedf2d1e`. The only new files are `pr-drafts/solrj/SOLR-14967.md` and `pr-drafts/solrj/SOLR-17866.md`. No builds, tests, Gradle, `gh` calls, commits, pushes or posts. Other parts' drafts in that folder were not touched.

## Per ticket

**SOLR-14967: draftable.**
- Draft `pr-drafts/solrj/SOLR-14967.md`, 3,645 characters with links (3,649 bytes).
- Head `ee76643f5b3fa1809d204b6c4953aa9d54120589`: `ls-remote` matches the receipt, and the object is present locally.
- The Proof follows the assignment's order: the run against the first version (before the commit that clears a caller value on the retry path), then 9 of 9 at the shipped head.

**SOLR-17866: draftable, flagged for the owner.**
- Draft `pr-drafts/solrj/SOLR-17866.md`, 4,986 characters with links (3,315 without the URLs). It is above the 3,500 guide because of the link count and the Choice.
- Head `3f9367d796a6446fa2f0549d8e9763e07831e6e8`: `ls-remote` matches.
- The design choice is not decided here. Two effects found in code are not in the receipt. The draft states them in "What this change does" and in Limits. Confirm before posting.

**SOLR-3498: audit only, no draft.**
- Head `812598302dee8e3c7679cc3f9301cdcbb17f3ad2`: `ls-remote` matches.
- Premise on live main (`3f5d4c5bf8ac`): it holds on the wire. `AbstractUpdateRequest.setCommitWithin` (lines 145 to 148) only stores a field. Its readers are the XML and javabin writers for `UpdateRequest`. `ContentWriterUpdateRequest`, `StreamingUpdateRequest` and `MultiContentWriterRequest` send through a content writer and never send the value. `ContentStreamUpdateRequest` is absent from base and main. The server side honors the parameter: the CSV, JSON, XML, javabin and CBOR loaders, and Solr Cell (`solr/modules/extraction/.../ExtractingDocumentLoader.java` line 81), read `commitWithin` from params.
- Branch shape: correct for `ContentWriterUpdateRequest` only. It sets the parameter in an override and keeps the field, so the value lives in two places, and `setParams()` silently drops it (the handoff note says so). `StreamingUpdateRequest` and `MultiContentWriterRequest` keep the defect.
- Gate needs (main side, not run): (1) a premise run on the merge-base `cabedd1d968`. The branch's `TestContentWriterUpdateRequest` uses only methods that exist on base, so it should fail there with a real assertion (null versus "1234"). (2) An end-to-end check that `/update/csv` or `/update/extract` receives the value on the add command. (3) A focused run with spotless and fail-before, plus the solrj Error Prone compile and check. (4) A decision on scope for the two other content-writer classes: add tests, or name them in Limits.

**SOLR-11356: audit only, no draft.**
- Head `8474e5a3a26d8aacd25d9fc9838c627bc6c5b14f`: `ls-remote` matches.
- Premise holds on main: `ConcurrentUpdateJettySolrClient.belongToThisStream` (lines 126 to 129) compares only params and collection. `initOutStream` (line 209) decorates the shared stream with the opening request's credentials.
- Classes sharing the pattern: only `ConcurrentUpdateJettySolrClient`. `ConcurrentUpdateJdkSolrClient` opens one request per update. The HTTP/1.1 `ConcurrentUpdateSolrClient` is gone on main (SOLR-18005 renamed the base).
- Branch: adds the basic-auth user, password and headers to the match key, and a shared test in `ConcurrentUpdateSolrClientTestBase` that runs under the Jetty and JDK suites.
- Shape concerns:
  - (a) `SolrRequest.getHeaders()` returns an unmodifiable live view (main `SolrRequest.java` lines 374 to 377), so `origHeaders` follows later changes to the opening request. It needs a copy.
  - (b) The per-request user principal is not in the key, but `decorateRequest` copies it to a Jetty attribute (`HttpJettySolrClient` lines 651 to 652), which `BasicAuthPlugin` reads for forwarded requests (line 217). Whether this reaches the wire here is not verified.
  - (c) The race: the test needs more than one queued update, so expect NOT_PROVEN or flakiness.
- Gate needs: `ConcurrentUpdateJettySolrClientTest` (solrj-jetty), focused and repeated for the race; fail-before on the merge-base `cabedd1d968` (it compiles on base, so a real failure is possible); spotless; Error Prone; the solrj-jetty check. Fix (a) before the gate.

**SOLR-14187: audit only, no draft.**
- Head `45b0f7ce34f802d0568c499bb3e5357f41be77ec`: `ls-remote` matches.
- The claim partly holds. Only the static `CollectionAdminRequest.waitForAsyncRequest(String, SolrClient, long)` (main lines 1829 to 1832) drops credentials, because it has no way to take them. Its main callers are `CloudHttp2SolrClientTest` lines 790 to 791, and `AbstractCloudBackupRestoreTestCase` lines 321 and 375.
- Not affected on base or main: `processAndWait` (lines 256 to 261) and `RequestStatus.waitFor` (lines 1867 to 1881) propagate credentials through `propagateBasicAuthCreds` (lines 151 to 158).
- Branch: adds a five-argument overload that sets credentials on the status request. Polls and the final delete then carry them (checked in code). The three-argument form stays credential-less. The changelog type "fixed" should be "added" for a new overload.
- Gate needs: the new test `CollectionAdminRequestAsyncAuthTest` does not compile on base, so fail-before can only be INCONCLUSIVE by construction. No base-compilable discriminator exists, because the instance path already propagates. The owner must accept an inconclusive Proof, or the test must go through the base API.

## Interaction results

**The `_stateVer_` overlap (SOLR-14967 and SOLR-18341, `CloudSolrClient`): textually clean, semantically conflicting in the test file.**
- `merge-tree --write-tree` of the two heads: clean, tree `36a94b7faf0457ae7339ea8792f9eae42a556036`. It auto-merged `CloudSolrClient.java` and `CloudSolrClientCacheTest.java`. Each branch also merges cleanly onto live main.
- The hunks are separate. 14967 changes the request-to-send block (head lines 692 to 718). 18341 changes the catch block, the retry guards and the javadoc.
- Code: 18341 reads `mayReplay` from the original request. 14967 wraps only the send. The load-balancing layer reads `isRetriable()` on the wrapper, and 18341's `WrappedSolrRequest` forwarding makes the wrapper answer the same. Order is not a code dependency.
- The `instanceof UpdateRequest` direct-update check (`CloudSolrClient` line 940 at the 14967 head) is not reached by a wrapper, because `AbstractUpdateRequest.getParams()` always returns `ModifiableSolrParams`. Cleared.
- **Conflict** (by reading the merged tree, not run): 14967's `ImmutableParamsRequest` uses `SolrRequestType.UNSPECIFIED` (merged file line 750). Under 18341, `isRetriable()` is true only for QUERY, and the stale-state retry requires `mayReplay` (merged lines 831 and 853). So `testImmutableParamsStaleRetryOmitsStateVersion` and `testImmutableParamsCallerStateVersionRemovedOnStaleRetry` would throw instead of retrying. The third new test is unaffected. 18341's own helper uses QUERY (18341 head line 658).
- **Landing order:** the textual merge and the production code do not depend on order. The test does. Whichever lands second must set the helper to QUERY. On main, QUERY and UNSPECIFIED take the same branches (`CloudSolrClient` and `LBSolrClient` check only ADMIN and UPDATE), so the change is safe to make in 14967. Recommend 14967 first with that change. It is a test edit, so 14967 needs a new run at the new head.

**The `SolrRequest` overlap (SOLR-17866 and SOLR-18341): clean, functionally independent for the replay decision, with one side effect and one asymmetry.**
- `merge-tree` is clean, tree `94b76f7d3911cd73665bd9b8e98102f48c92579a`. It auto-merged `SolrRequest.java`, and is clean on live main as well.
- A request can be both collection-explicit and non-retriable (a generic request of type ADMIN or UPDATE that names a collection). `isRetriable()` reads only the request type, so the flag does not change the replay decision.
- Side effect: 17866's flag feeds the cloud client's admin check (`CloudSolrClient` line 639 at the head), so admin-typed generic requests that name a collection now get the cloud client's state-version handling, with or without 18341. 18341 moves the load-balancing decision to `isRetriable()` (`LBSolrClient` line 571 at the 18341 head), so LB no longer reads the flag after 18341. Replay stays off for ADMIN either way.
- Asymmetry: `WrappedSolrRequest` forwards 18341's `isRetriable()` but not 17866's `collectionProvided` hook. A wrapped `GenericSolrRequest` keeps the old behavior. This is not caused by 18341, and it is recorded in the 17866 draft's Limits.

**Trial merges** (no refs written):
- 14967 head with 18341 head: clean, `36a94b7faf0457ae7339ea8792f9eae42a556036`.
- 17866 head with 18341 head: clean, `94b76f7d3911cd73665bd9b8e98102f48c92579a`.
- Live main with 14967 head: clean, `343d2e40adc597c601384a304dee80c8aa87fbb3`.
- Live main with 17866 head: clean, `9ea6917b6576218edf65de26f86a31a3aa8e9560`.
- Live main with 18341 head: clean, `1302c3555ee70700b879597728c7a51530cb7756`.

## Self-check per draft

- **SOLR-14967:** no em or en dashes (scanned); no process words (scanned for gate, receipt, ledger, rc=0, owed, round, pre-fix, JUnit XML, audit, handoff, hypothetical, banked). Links: 4 to head `ee76643f5b3`, and 1 to base `b5c71bc5573` (the merge-base, labeled base); all full 40-character SHAs. The header emoji is kept from the approved template.
- **SOLR-17866:** the same scans are clean. Links: 8 to head `3f9367d796a6`, and 2 to base `97d973814336` (the merge-base, labeled base); all full SHAs.

## Receipt disagreements (exact wording)

1. **SOLR-14967 receipt:** "the retry after a state change re-sent the caller's own `_stateVer_` value (`caller:999`) instead of the fresh one." The test (`testImmutableParamsCallerStateVersionRemovedOnStaleRetry`, lines 251 to 286) asserts that the stale-state retry sends no `_stateVer_` ("Stale-state retry must not send the caller-supplied _stateVer_", line 284). The first attempt carries the computed value. The draft says the retry "sends caller:999, and the test expects no `_stateVer_` on that retry."
2. **SOLR-14967 receipt:** "against the banked patch alone (before the completion commit)". No commit on the branch matches. `356131b78664` changes only `CloudSolrClient.java`. The tests and changelog first appear in `f1db97499dd`. The head adds `ee76643f5b3` (formatting). The discriminating run used a tree that is not a branch commit. The draft says "the first version of the change" and names no hash.
3. **SOLR-14967 receipt:** "a caller-supplied value is not re-sent on the retry paths." The code also changes the first attempt: a caller value is replaced when the client has a version, and removed when it has none (lines 705 to 713). The draft states both.
4. **SOLR-17866 receipt:** "production behavior is otherwise unchanged (the round 35 production diff is javadoc only)" and "`GenericSolrRequest` is collection-aware unless `setRequiresCollection` was called explicitly." Not recorded: the flag stays set after the call, and the cloud client's admin check (line 639) and the LB client's retry decision (line 575) read it.
5. **SOLR-14187 claim wording:** "the async collection-admin helpers wait for completion without the per-request credentials". Only the static three-argument helper lacks them. `processAndWait` and `RequestStatus.waitFor` carry them on base and main.
6. **SOLR-3498 claim wording:** "`ContentWriterUpdateRequest.setCommitWithin` is a no-op." On main it stores the value. It is a no-op only on the wire.
7. **SOLR-17866 receipt:** "GitHub corroboration: run 37721100372 SUCCESS at this head." Not re-checked (no GitHub read in this part). Not cited in the draft.
8. **Branch note, SOLR-11356 (not a receipt):** "`getHeaders()` returns an unmodifiable view; equality is by content ... which is what is wanted." The view is live, so the key follows later changes to the opening request.

## Owner decisions

1. **SOLR-17866:** submit ahead of the design conversation? The ticket comment of 2025-08-21 says a maintainer would aim to work on an explicit-constructor PR. The part did not check whether that work exists upstream. Recommendation: check upstream first, then fix the two side effects (reset or copy the flag; forward the hook through `WrappedSolrRequest`), then decide. Do not submit before the upstream check.
2. **SOLR-17866:** keep the sticky flag and the admin-typed cloud and LB effects as documented, or change the code. Neither is tested.
3. **SOLR-14967 and SOLR-18341:** landing order, and the QUERY change in the 14967 test helper. Recommendation: 14967 first, then a new run at its new head.
4. **SOLR-3498:** scope. Either `ContentWriterUpdateRequest` only (name the other two in Limits), or all three content-writer classes. Recommendation: all three.
5. **SOLR-11356:** copy the headers in the key (required), and decide on the user principal after checking whether it reaches the wire.
6. **SOLR-14187:** accept an inconclusive-by-construction Proof, or restructure the test. Change the changelog type from "fixed" to "added".
7. **Main side:** correct the receipt wording for SOLR-14967 (items 1 and 2) and SOLR-17866 (item 4).

## Not checked

- No builds, tests or Gradle. The merged-tree test failure comes from reading the merged file, not from a run.
- Jira: `research/jira-context/SOLR-14967.json` and `SOLR-14187.json` do not exist, so they were not read. Jira was not called. The files for 17866, 3498, 11356 and 18341 were read.
- GitHub: no reads. Not checked: upstream PR 3423's state, whether the explicit-constructor PR exists, CI run 37721100372, and the state of any fork PRs.
- 11356: whether a queued request's user principal reaches the wire with `forwardCredentials` on.
- 3498: the server path was read from loader code, not run end to end.
- 14967: no base run is recorded for the three new tests. The draft claims none.
- Fetch: live main `3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c` (2026-10-10) was fetched with `--no-write-fetch-head --refmap=`, so no refs moved. The worktree's `refs/remotes/upstream/main` still points at `8e62c2686882`. The local branch names `solr-17866-submit` (`67ffaa8b`) and `solr-18341-submit` (`c1cb8d72`) do not match the live heads, so the origin tracking refs were used. Those match `ls-remote`.

# solr-14967-submit

- Branch: origin/solr-14967-submit
- Head: ee76643f5b3f (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base b5c71bc5573c, 46 commits behind, 3 commits ahead)
- Scope: 3 commits, 3 files. `CloudSolrClient.java` (new `StateVerRequest`, a `WrappedSolrRequest` view that overrides `getParams()`; the empty `else` for non-modifiable params is replaced by a copy carrying `_stateVer_`, or with `_stateVer_` removed on the no-state-version retry), `CloudSolrClientCacheTest.java` (four new tests: immutable params carry `_stateVer_` and the caller's object is unchanged; stale retry omits it; caller `_stateVer_` removed on stale retry), changelog `SOLR-14967.yml` (`type: fixed`).
- Verdict: Nearly (unchanged from the bulk verdict). The wrapper delegates what the send path reads, the caller's params are not mutated, and the base code's empty `else` confirms the gap. One routing edge (finding 2) is theoretical; no fix is required for the stated case.
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. No focused proof exists for this head in the queue. Every claim rests on reading the diff and the code at the listed head.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-14967-review.md` (Nearly) reviewed this head (`ee76643f5b3`). No delta.

- Bulk F1 (ticket premise evidence incomplete; the JIRA packet was missing and the live read returned only the summary): **changed to partly resolved.** The base code itself shows the gap: the modifiable branch sets or removes `_stateVer_`, and the non-modifiable branch had only `// else: ??? how to set this ???` (the diff's removed line). So the omission is verified from the code. What the JIRA expects beyond "send `_stateVer_`" is still unconfirmed. See finding 3.
- Bulk F2 (no focused proof recorded in the queue): **confirmed as a process state, not a code finding.** No queue result exists. The round forbids running anything, so this stays open until a verify run is requested.

## Findings (ranked)

1. **LOW, verified (checked, no issue). The wrapper delegates what the send path reads.** `WrappedSolrRequest` (`solr/solrj/src/java/org/apache/solr/client/solrj/WrappedSolrRequest.java`) delegates method, path, response parser, streaming callback, query params, request type, preferred nodes, principal, basic-auth credentials, `requiresCollection`, API version, `getContentWriter`, collection, and headers to the wrapped request. `StateVerRequest` overrides only `getParams()`. The send path reads the body through `getContentWriter` (`SolrRequest.java:279`), which is delegated. No `getContentStreams` exists on `SolrRequest` at this head, so there is no second body path to lose. The caller's params are not mutated: the wrapper uses a new `ModifiableSolrParams` copy (`CloudSolrClient.java:708-712`).

2. **LOW, verified (theoretical). A wrapped `UpdateRequest` would skip its per-request send-to-leaders override.** `sendRequest` checks `request instanceof UpdateRequest` before reading `isSendToLeaders()` (`CloudSolrClient.java:940-942` at head). `StateVerRequest` is not an `UpdateRequest`, so that override would be skipped. This only happens if an `UpdateRequest` reports non-modifiable params. `UpdateRequest` builds `ModifiableSolrParams` by default (`UpdateRequest.java:210,217`), and whether a caller can install a non-modifiable params object on an `UpdateRequest` was not checked. No defect is shown for the stated case, which is generic non-modifiable requests.
   - Proposed fix (not applied; the owner decides if it matters): skip the wrapper for `UpdateRequest`, with a comment, or have the wrapper preserve the update-specific routing. Either is a small change once the owner confirms it is reachable.

3. **LOW, verified. The premise is confirmed from code; the JIRA's exact expectation is not.** Before this branch, `CloudSolrClient` set `_stateVer_` only when `request.getParams()` was `ModifiableSolrParams`, with an empty `else` for other types. So a `MultiMapSolrParams` or `MapSolrParams` request never carried `_stateVer_`, and a stale client could not be detected by the server for those requests. The branch closes that gap. Whether the JIRA expects more (other wrapper types, or retry behavior beyond the two cases tested) is not confirmed, because the JIRA packet was not available and the live read returned only the summary.

4. **LOW, verified (checked, no issue). The new tests cover the new branches.** `testImmutableParamsCarryStateVersionAndOriginalUnchanged` (`CloudSolrClientCacheTest.java:199-222`) checks `_stateVer_` on immutable params and that the caller's object is unchanged. `testImmutableParamsStaleRetryOmitsStateVersion` (`:225-249`) checks the stale retry. `testImmutableParamsCallerStateVersionRemovedOnStaleRetry` (`:251-`) checks removal of a caller-supplied `_stateVer_`. The test bodies were read only at the assertion lines, not run.

5. **Process note, not a code finding.** No queue result, no fail-before proof, and no test run exist for this head. The round forbids running them, so the branch's proof is open until the owner asks for a verify run.

## Owner calls (not decided here)

None needed for the stated case. If finding 2 is reachable, whether `UpdateRequest` with non-modifiable params should be wrapped at all is a small owner call.

## Proposed fixes (not applied; the owner decides)

- Finding 2: exclude `UpdateRequest` from the wrapper branch, with a comment, or add a test that a wrapped update still gets send-to-leaders routing. Only if the owner confirms the case is reachable.
- Finding 3: write the JIRA's expected behavior into the changelog or the test once the owner has the JIRA text.

## Not checked

- Nothing compiled, formatted, or run. No fail-before proof.
- The full bodies of the four new tests were not read beyond their assertion lines.
- Server-side handling of `_stateVer_` for requests encoded from `MultiMapSolrParams` (the server reads the same parameter, but was not traced).
- The JIRA packet beyond the summary.
- The retry loop around `requestToSend` was read at the call sites (`:692-718`), not traced in full across all retry branches.

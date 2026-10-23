# Report: PR #5139 (SOLR-7550) test strengthening (round 1)

Host: vm3. Assignment: `assignments/pool-pr5139-test-strengthen-round-1.md`. Claim: `claims/pool-vm3-pr5139-test-strengthen-round-1.md`.

Branch `solr-7550-submit`, PR apache/solr#5139 (open, not draft). Reviewed tip `fcc8a874abbb8fbd465a835da6903f64de7009b2`, confirmed by ls-remote at claim time (2026-10-23T04:14Z). Final head `54d7e21f28fc7ffbbb224da068442d3105da7fc8`, confirmed by ls-remote before the push and by the PR API after it.

Base note: the PR branches from `14c7aac0d1` (SOLR-16396), not from the `b73f03976f` named in the assignment. The PR's own diff is taken against `14c7aac0d1`. The gate ran on the branch tree, which is what the PR merges.

## Outcome

- The shipped test already asserts a return value (`true`/`false`), so the critique's "checks the call doesn't throw" describes an earlier state of the PR. Before this change, 3 tests covered 2 of the 8 decision cells in section 1 (core-init 500 and other 500, both with the flag set). Four tests now cover the rest except "other 500 with the flag unset", which takes no branch and is still untested.
- No defect in the fix. The strengthened tests found none.
- The PR title said "a 500 ... counts as success". The code, the changelog and the PR body all narrow it to core-initialization 500s. The title is corrected (live edit, re-read; see below).
- Test change pushed as `54d7e21f28`. PR body Proof section and title updated.

Verdict on the change itself: the decision is right for the ticket. The open question in the PR body ("Choice to check") is the owner's and is unchanged.

## 1. Decision table

Source: `solr/core/src/java/org/apache/solr/update/PeerSync.java` at `54d7e21f28` (unchanged from `fcc8a874ab`). Only the purpose `SHARD_REQUEST_PURPOSE_GET_VERSIONS` takes these branches. Every failure falls through to `return false` at line 447. Versions responses without an exception go to `handleVersions` at line 451.

| Versions request failure | `cantReachIsSuccess` set | unset | Code |
| --- | --- | --- | --- |
| Core initialization failure: HTTP 500, metadata `ERROR_CLASS` = `SolrCoreInitializationException` | success (new) | failure | lines 424-436; flag gate at 425 |
| Other 500 | failure | failure | no branch; line 447 |
| 503 | success | failure | lines 400-410; flag gate at 401 |
| 404 | success | failure | lines 412-422 (not in the ticket's scope; unchanged) |
| Connection refused, timeout, socket error (`SolrServerException` chain) | success | failure | lines 381-398; flag gate at 382 |

Why the 500 direction is "core initialization only":

- `SolrCoreInitializationException` is `SERVER_ERROR` (500), `solr/core/src/java/org/apache/solr/core/SolrCoreInitializationException.java:27-35`. `CoreContainer.java:2222` throws it for a core that failed to load, so a broken core really is a 500 on the versions request.
- The class name reaches the client through `ResponseUtils.getErrorInfo` (`ResponseUtils.java:77`, writes `ERROR_CLASS`) and `RemoteSolrException` (`solr/solrj/src/java/org/apache/solr/client/solrj/RemoteSolrException.java:73-84`, copies `error.metadata` into the exception). `SolrException.getThrowable()` (`solr/solrj/src/java/org/apache/solr/common/SolrException.java:136-138`) returns that class name as a `String`, so the `equals` at line 429 compares like with like.
- The ticket SOLR-7550 (summary "PeerSync fails if a replica returns 500 error") describes exactly this case: cores that "could not load as the schema was invalid" returned 500 on the versions request, which blocked leader election. It does not ask for every 500 to count. Counting every 500 is the first version of this PR; the PR body documents it as option 2.

Settling the wording: the title said "A 500 ... now counts as success ... the same as a 503", which reads as any 500. That is wrong for the code. The artifact's summary sentence is not on the branch and I did not have its text, so I relied on the code, the ticket and the changelog, which all agree on core-initialization only. The title is corrected.

## 2. Base reproduction

Method: the head's test file, the base's `PeerSync.java` (`14c7aac0d1`), with only `handleResponse` made package-visible (the base has it `private`, so the test cannot otherwise compile). `ShardResponse` stays at head because the test needs its public setters. Logs: `/workspace/gates/logs/vm3/pr5139/4-base-focused.log` and `4-base-focused-xml/`.

Result: `PeerSyncHandleResponseTest` 8 tests, 1 failure, `testPeerSyncCountsCoreInitFailureFromVersionRequestAsSuccess` at its `assertTrue`. So PeerSync at base decides failure for the core-initialization 500, which is the bug. The other seven pass on base; they guard behavior that did not change, so passing there is correct.

The base reproduction runs the unit path (`handleResponse` on a constructed response), not a live cluster. The PR body already lists the missing cluster test as a limit; vm4's review passes said the same.

## 3. Strengthened test

File: `solr/core/src/test/org/apache/solr/update/PeerSyncHandleResponseTest.java`, commit `54d7e21f28` ("SOLR-7550: cover each version-failure decision in PeerSync handleResponse tests").

Starting state (`fcc8a874ab`): 3 tests, all asserting the boolean decision:

- core-init 500, flag set, versions: `assertTrue` (line 70)
- other 500, flag set, versions: `assertFalse` (line 83)
- core-init 500, flag set, updates: `assertFalse` (line 97)

Added (4 tests, plus one helper `handleVersionsFailure`, so the table is complete):

- `testPeerSyncStillFailsCoreInitFailureWhenCantReachIsNotSuccess`: core-init 500, flag unset, `assertFalse`. This is the case that proves the new branch is gated on the flag.
- `testPeerSyncCountsServiceUnavailableFromVersionRequestAsSuccess`: 503, flag set, `assertTrue`. Pins the existing 503 rule.
- `testPeerSyncStillFailsServiceUnavailableWhenCantReachIsNotSuccess`: 503, flag unset, `assertFalse`.
- `testPeerSyncCountsRefusedConnectionFromVersionRequestAsSuccess`: `SolrServerException` over `SocketException`, flag set, `assertTrue`.
- `testPeerSyncStillFailsRefusedConnectionWhenCantReachIsNotSuccess`: same, flag unset, `assertFalse`.

Both-directions results (fresh XML):

| Test | Head `54d7e21f28` | Base PeerSync |
| --- | --- | --- |
| core-init, flag set (versions) | pass | **fail** |
| core-init, flag unset | pass | pass |
| other 500, flag set | pass | pass |
| core-init, updates | pass | pass |
| 503, flag set | pass | pass |
| 503, flag unset | pass | pass |
| connection refused, flag set | pass | pass |
| connection refused, flag unset | pass | pass |

Two checks against the flag: the core-init branch is gated on `cantReachIsSuccess` (flag unset fails at head), and only the versions purpose takes it (updates fails at head).

## 4. Gate results at final head

Gate script `/workspace/tools/pr5139-gate.sh`, logs `/workspace/gates/logs/vm3/pr5139/`, seed `7550C0FFEE755075` (the seed vm4 used).

| Step | Result |
| --- | --- |
| 0 changelog YAML (`changelog/unreleased`) | rc=0 |
| 1 tidy (`./gradlew tidy`) | rc=0. Changed only my new test lines (reflow); the tidied file is in `54d7e21f28` |
| 2 Error Prone compile (`:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`) | rc=0 |
| 3 focused head run, `PeerSyncHandleResponseTest` and `PeerSyncTest` | rc=0. XML: 8 tests, 0 skipped, 0 failures, 0 errors; `PeerSyncTest` 1 test, 0 failures |
| 4 base reproduction (section 2) | rc=1, the expected single failure |
| 5 module check (`:solr:core:check -x test`) | rc=0 |

The gate ran on the working tree that became `54d7e21f28`. Step 1 tidy changed only my own test lines before the commit, so the gated tree and the pushed tree are the same.

## 5. Patch loop

No defect in repair scope or outside it. No production change. No repair commit.

## 6. PR changes

Owner's PR, standing authorization for live title and body edits (AGENTS.md, 2026-10-21). No state change, no comments, no Jira edits.

- Title, before: `SOLR-7550: A 500 for the versions request now counts as success when \`cantReachIsSuccess\` is set, the same as a 503`
- Title, after: `SOLR-7550: A core initialization failure on a versions request counts as success when \`cantReachIsSuccess\` is set`
- Body: the Proof section replaced. It said the proof was pending and that the base cannot compile the test. It now has the decision table, the head run counts from fresh XML, and the base result. The test list paragraph was replaced with the table. Links, the changelog, the limits and the "Choice to check" section are unchanged.
- Re-read after the edit: the live title and body match the saved text (`pr-drafts/2026-10-23-pr5139-title.txt` and `pr-drafts/2026-10-23-pr5139-body.md`; the prior body is `pr-drafts/2026-10-23-pr5139-body-before.md`).

The PR body's pinned links still point at `5f4570b20a`, the commit where the code was introduced, because they still describe that code. The proof links point at `54d7e21f28`.

## 7. Open items for the owner

- The "Choice to check" question in the PR body (option 1 vs option 3) is unanswered. Nothing in this review settles it.
- No live-cluster test of the core-init path. The unit tests call `handleResponse` with a constructed response. The client side (`RemoteSolrException`) is traced in code, not run.
- Older Solr nodes that do not send `ERROR_CLASS` keep the failure behavior. Not tested.
- `ShardResponse`'s two setters are public for the test (PR body already says so).

## Limits observed

No PR state change, no comments, no Jira edits. Pushes limited to `solr-7550-submit` (fast-forward `fcc8a874ab` to `54d7e21f28`). Worktree `/workspace/worktrees/pr5139`, Gradle daemon and the scratch script are cleaned up after the commit. df before the gate: root 38% used, inodes 9%; after: see `df-after.txt`.

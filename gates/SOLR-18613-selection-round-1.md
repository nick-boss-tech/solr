# Gate: SOLR-18613 standalone live selection follow-up, round 1

Status: OPEN

- Tags: gate, review, records, premise-run.
- Intended host: any capable Linux gate host with JDK 21, Git, Python 3, the branch's pinned Gradle 9.7.0/build dependencies, and root-check dependencies, serialized under its host build lock.
- Authority: Nick's November 5 request to patch the reviewed branch and hand off its gate.
- PR: https://github.com/apache/solr/pull/5297. Ticket: SOLR-18613.
- Fork branch: nick-boss-tech/solr:solr-lb-attempt-limits-pr1-submit.
- Reviewed pre-fix PR head P: 2399d0312fa320563773ea87884ee4f534594974.
- Exact pushed repair head H: e88dc392bcab67ac388a45340d96ac52c616f35b.
- Expected complete tree: 40843a7f4f083f5ee036c0cbbf60b812731c072a.
- Seed: 5B407A2D9C1310E7.
- Implementation claim: claims/SOLR-18613-selection-windows-round-1.md.
- Implementation report: reports/SOLR-18613-selection-windows-round-1.md.
- New receipt target: receipts/SOLR-18613-selection-round-1.md; owned only by the executing Linux host.
- State: Implemented, Proof Pending. Posting this gate does not claim Linux execution or success.

## Scope and regression

This supersedes earlier published-head/changelog gates for readiness of the new source head. Keep all older gate files and receipts as history. The older green receipts cover other commits and do not satisfy this job.

The standalone path caps live selections at the captured live-array size, but each selection uses a shared round-robin counter. With live [A, B], failing A, healthy B, and primary limit 3: primary selects A, a secondary request selects B, primary selects A again and stops after two iterations. B remains untried by the primary even though an attempt remains. The pre-PR implementation could reach B on the third selection; P instead fails with no live servers.

H filters endpoints already failed by this execution out of the captured array when selection returns a previously tried endpoint, then calls the existing pickServer hook again on the untried candidates. Filtered selections are not dispatches and do not spend the attempt allowance. Normal sequential selection order, the shared live/zombie allowance, the selector extension point and async/Req retry eligibility are retained.

H changes exactly these two files relative to P:

- solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java
- solr/solrj/src/test/org/apache/solr/client/solrj/impl/LBSolrClientAttemptLimitTest.java

## Ownership and preflight

Read the live board workflow, this job, matching claims and any newer superseding report before execution. Use a clean detached full clone at H, not a checkout shared with another gate; the full clone avoids the previously observed Antora worktree .git-file failure. Record the runner, checkout, lock, JDK/Gradle versions and UTC start in a new execution claim. One host executes this job, one build at a time under its lock.

Verify the remote fork branch and live PR both equal H and the complete tree matches. Stop and reconcile a moved head instead of silently testing it under this job. Preserve raw command logs, exit codes and fresh JUnit XML separately for every proof leg.

## Required positive proof at H

1. Run ./gradlew tidy; require no tracked changes. A tidy repair changes H and needs an ordinary commit, plain fork push, independent ref read-back and an add-only superseding exact-head gate before green can be claimed.
2. Execute the focused and neighboring suites, forcing fresh test execution:

~~~sh
./gradlew :solr:solrj:cleanTest :solr:solrj:test \
  --tests org.apache.solr.client.solrj.impl.LBSolrClientAttemptLimitTest \
  --tests org.apache.solr.client.solrj.impl.LBAsyncSolrClientAttemptLimitTest \
  --tests org.apache.solr.client.solrj.impl.LBSolrClientTest \
  --tests org.apache.solr.client.solrj.impl.LBAsyncSolrClientTest \
  --tests org.apache.solr.client.solrj.impl.LBSolrClientRetryUnsentTest \
  --tests org.apache.solr.client.solrj.impl.LB2SolrClientTest \
  --tests org.apache.solr.client.solrj.impl.LBHttpSolrClientBadInputTest \
  -Ptests.seed=5B407A2D9C1310E7
~~~

Expected: 49 tests, zero failures/errors/skips (14 sync attempt-limit, 12 async attempt-limit, 8 iterator/base, 4 async/base, 3 unsent, 7 LB2, 1 bad-input). Investigate any count difference; cached/up-to-date output is not proof of execution. In particular require both new methods to execute:

- LBSolrClientAttemptLimitTest.testStandaloneInterleavedRequestDoesNotHideHealthyEndpoint: controlled latch interleaving, separate primary-attempt list, primary success without revisiting A; exercise limits 3, 2 and null.
- LBSolrClientAttemptLimitTest.testStandaloneRetryUsesCustomPickerWithUntriedEndpoints: first-candidate selector remains authoritative when retry candidates are filtered, primary dispatches A then healthy B.

3. Run ./gradlew check -x test at H in the full clone. Require the full root check, including formatting/Javadoc and configured static analysis; do not substitute only a module check or silently exclude tasks. Record material environment conditions beside the result.
4. Require git diff --check and a clean final tree, unchanged H/tree, and a separate final remote fork/PR head read-back.

## Required negative proof against P

Use a second clean detached full clone at P. Copy only H's complete LBSolrClientAttemptLimitTest.java into that clone. Retain P's production files byte-for-byte, including its public exception API; no scaffold or production mutation is needed. Record this synthetic baseline's exact diff and test-file hash. Tests must remain identical to H's version throughout this leg.

~~~sh
./gradlew :solr:solrj:cleanTest :solr:solrj:test \
  --tests org.apache.solr.client.solrj.impl.LBSolrClientAttemptLimitTest.testStandaloneInterleavedRequestDoesNotHideHealthyEndpoint \
  --tests org.apache.solr.client.solrj.impl.LBSolrClientAttemptLimitTest.testStandaloneRetryUsesCustomPickerWithUntriedEndpoints \
  -Ptests.seed=5B407A2D9C1310E7
~~~

Both methods must execute and fail for the intended runtime reason: the primary request exhausts its two live-loop iterations on A and surfaces a no-live-servers exception before reaching healthy B. For the interleaving method, the first limit case is 3, so the failure establishes the reviewed explicit-limit regression. The primary dispatch trace at P should be [A, A]. A latch timeout, executor leak, setup/compile error, missing API, skipped method or unrelated assertion is not negative proof. Retain fresh failure logs/XML and identify the request failure/cause and dispatch trace. If either method passes, investigate and report that its discrimination is unproven instead of declaring the leg green.

The four original-production failures from receipts/lb-attempt-limits-pr1.md and receipts/SOLR-18613.md may be carried forward as historical evidence only after comparing the old assertion bodies and default helper behavior with H; document that this proof is not rerun here. The required new negative leg above is against P, not untouched upstream production. Do not claim all 26 attempt-limit methods fail on an original baseline; the previous 20 compatibility methods remain pins.

## Failure handling and deliverables

Investigate executed failures before retrying. Keep test failures separate from environment interruptions. A setup refusal before any test ran may be repaired and relaunched once under the board rules, with the cause and repair recorded. Any source/test/formatting change requires an ordinary commit with Nick Shanin's ICLA identity, plain push only to the existing fork branch, independent SHA read-back, and a superseding gate; never force-push or weaken an assertion to get green. Do not overwrite an older executing host's receipt.

Write the new receipt and an add-only completion report naming exact H/P/tree, all commands, toolchain/lock, logs/XML with timestamps and counts/skips, the negative baseline diff and outcomes, material execution conditions, and independently refreshed fork/PR heads. End with a concise factual description draft for owner review. GREEN requires every positive and negative leg above and no unexplained failure. Windows ran no Gradle or tests. Public comments, live PR-body edits, Jira writes, draft-to-ready changes and PR 2/3 implementation are outside this job.

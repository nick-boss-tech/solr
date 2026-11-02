# Gate: SOLR-18613, LB attempt limits, published-head verification

Status: DONE (GATE GREEN at c2d2c6259462a7e848008b065d65df69a1b82ebb; receipt `receipts/SOLR-18613.md`, completion report `reports/SOLR-18613-published-head-gate-round-1.md`)

- Ticket: https://issues.apache.org/jira/browse/SOLR-18613
- Tags: gate, premise-run. Intended host: any capable Linux gate host with JDK 21, Git, Python 3, and the pinned branch's Gradle/build dependencies, one gate at a time under its host build lock.
- Authority: Nick's November 2 request to create a ticket and hand off the gate under its number. Only PR 1 is in scope.
- Fork: nick-boss-tech/solr.
- Existing branch retained: solr-lb-attempt-limits-pr1-submit. Do not require a ticket-shaped branch name or rename/rewrite the existing series.
- Exact published source head: c2d2c6259462a7e848008b065d65df69a1b82ebb.
- Expected complete tree: 4d09e238ca0a7890e125b5f36d2dd78ff90f7110.
- Original production base: 0d6b2e32f598d772aed094038022a6a588fdfc5a.
- Seed: 5B407A2D9C1310E7.
- Records claim: claims/SOLR-18613-ticket-gate-handoff-windows-round-1.md.
- Receipt target: receipts/SOLR-18613.md, written only by the executing Linux host.
- State: Implemented, published-head proof pending. This posted job is not an execution claim.

## Why this job exists

This supersedes gates/lb-attempt-limits-pr1-round-1.md for remaining verification at the published head. Preserve that job and the existing receipt as history.

Read receipts/lb-attempt-limits-pr1.md, reports/lb-attempt-limits-pr1-vm2-round-2.md and assignments/lb-attempt-limits-pr1-submit-help-round-1-result.md. vm2 reports 47 focused tests passing, clean tidy and root check at local repaired head 28dbd2edfce1520c354dbc71b555db4ced3fbebe, plus four expected original-production failures. The formatting and Javadoc repair series was replayed and published at c2d2c6259462a7e848008b065d65df69a1b82ebb. Its complete tree equals the tested tree. The old receipt's publication blocker is historical, but it does not establish exact-SHA proof for the published head.

This is changed-input verification, not a blind retry of the earlier failures. The previous ECJ Javadoc errors were repaired; the Antora worktree failure was resolved by using a full clone.

## Preflight and ownership

Read the live board workflow and existing claims before executing. Check for a newer source head, superseding job or completed receipt. Claim this job in a new file; record host, UTC start, build lock, checkout, JDK, commands and evidence paths. Do not run concurrently with another host on this job.

Use a clean detached full clone at the exact published head. A full clone avoids the known Antora failure with a worktree's .git file. Verify the live fork ref and complete tree. If the branch moved, reconcile and post a superseding exact-head job instead of silently testing a different head.

The outbound source delta remains exactly four files:
- solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java
- solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBAsyncSolrClient.java
- solr/solrj/src/test/org/apache/solr/client/solrj/impl/LBSolrClientAttemptLimitTest.java
- solr/solrj/src/test/org/apache/solr/client/solrj/impl/LBAsyncSolrClientAttemptLimitTest.java

No source or changelog change is requested by this verification job. A ticket number now exists; any later submission metadata change must be separately committed, pushed and covered by an appropriate fresh gate.

## Required published-head proof

Under the host's build lock, run ./gradlew tidy and require a clean tree. Then run:

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

Require all 24 new methods and the selected neighboring methods to execute: expected 47 tests total, zero failures/errors/skips. Retain fresh XML, complete logs and exit codes; cached/up-to-date tests do not establish execution. Investigate any difference in counts.

Run ./gradlew check -x test in the full clone at the same exact head. Require success, clean final tree and git diff --check. Report any material environment condition beside the result. Do not silently exclude tasks.

## Existing original-production proof

Avoid repeating completed proof without a reason. Read and reconcile the four original-production failures recorded in receipts/lb-attempt-limits-pr1.md and the retained logs/XML under /workspace/gates/logs/vm2/lb-attempt-pr1/:
- LBSolrClientAttemptLimitTest.testStandaloneLimitStopsBeforeZombieFallback
- LBSolrClientAttemptLimitTest.testStandaloneLimitWithAllZombies
- LBSolrClientAttemptLimitTest.testStandaloneDoesNotRetryExhaustedLiveSnapshot
- LBAsyncSolrClientAttemptLimitTest.testAsyncPreservesAttemptLimitFailure

Expected recorded failures respectively: dispatch beyond a limit of two; empty-live-array arithmetic failure; five dispatches against three exhausted live endpoints; hidden attempt-limit exception.

Verify that the published tests differ from the original proof tests only by the recorded tidy formatting and that the published tree equals the repaired tree. Disclose that this negative proof is carried forward, not rerun at the published SHA. It used the original production base plus the two tests and an inert exception declaration so the tests compile. It was not an untouched-parent run.

If the prior raw evidence is unavailable or cannot support that reconciliation, record the reason and rerun only this missing leg using the exact synthetic-base procedure in gates/lb-attempt-limits-pr1-round-1.md. Preserve the original production loops/iterator/callback behavior, add only the exception API scaffold and tests, and use the same seed. Retain the synthetic diff and require those four methods to execute and fail for the intended runtime reasons. Compilation, setup errors, missing tests or unrelated failures do not count. The other 20 new methods remain compatibility pins unless separately discriminated.

## Repairs, receipt and limits

If tests execute and fail, investigate before retrying. Any source or formatting repair requires a narrow ordinary commit with Nick Shanin's ICLA identity, a plain fork push with separate remote-SHA verification, and an add-only superseding exact-head job. Do not force-push. Separate environment failures from patch failures and follow WORKFLOW.md's repair rules.

Write receipts/SOLR-18613.md and an add-only completion report with exact source/base/tree, toolchain, lock, commands, raw logs/XML, counts/skips, carried-forward negative proof and its limits, repaired failures, and independently verified remote head. End the receipt with a factual PR description draft. Do not edit the old vm2 receipt; it is owned by its executing host.

GREEN requires all published-head positive checks above and reconciled four-method negative evidence, with no unexplained failures. No Gradle runs on Windows. No upstream PR, public comment, JIRA edit, mailing-list message or PR 2/3 implementation is authorized by this job.


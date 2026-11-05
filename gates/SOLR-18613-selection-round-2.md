# Gate: SOLR-18613 standalone live selection follow-up, round 2

Status: OPEN

- Tags: gate, review, records, premise-run.
- Intended host: any capable Linux gate host with JDK 21, Git, Python 3, the branch's pinned Gradle 9.7.0/build dependencies, and root-check dependencies, serialized under its host build lock.
- Authority: Nick's November 5 request to patch the reviewed branch and hand off its gate; repair and supersession per round 1's failure rules.
- PR: https://github.com/apache/solr/pull/5297. Ticket: SOLR-18613.
- Fork branch: nick-boss-tech/solr:solr-lb-attempt-limits-pr1-submit.
- Reviewed pre-fix PR head P: 2399d0312fa320563773ea87884ee4f534594974.
- Round 1 head (GATE FAILED, receipt `receipts/SOLR-18613-selection-round-1.md`): e88dc392bcab67ac388a45340d96ac52c616f35b. Its defects: forbidden `Executors.newSingleThreadExecutor()` in the new interleaving test (root check failure at `:solr:solrj:forbiddenApisTest`) and an untidy Javadoc wrap in LBSolrClient.java.
- Exact repaired head H: fe557633a169c34dcb835696470cc6e69ba00be4 (two ordinary commits over round 1's head: 20e244407e tidy rewrap; fe557633a1 MDC-aware executor in the test).
- Expected complete tree: 6a0c8c7bfe43420ba03284f727c420af3996ebf4.
- Seed: 5B407A2D9C1310E7.
- Implementation claim: claims/SOLR-18613-selection-windows-round-1.md. Execution claim: claims/SOLR-18613-selection-gate-phone-agent.md.
- New receipt target: receipts/SOLR-18613-selection-round-2.md; owned only by the executing Linux host.
- State: Implemented, Proof Pending at H. Round 1's positive counts were recorded at a tree carrying a comment-only tidy rewrap and do not satisfy this job.

## Scope

Identical behavior scope to round 1: the standalone selection repair relative to P touches `LBSolrClient.java` and `LBSolrClientAttemptLimitTest.java` only. The two repair commits change no behavior: one is the formatter's own Javadoc rewrap; the other swaps the test's executor creation for `ExecutorUtil.newMDCAwareSingleThreadExecutor` with a named thread factory, with no assertion, latch, or dispatch change. H's test file therefore differs from round 1's H test file only in executor creation and imports.

## Required proof

Repeat round 1's legs exactly, at H, in a clean detached full clone, under the host build lock:

1. `./gradlew tidy` (via the host's pinned toolchain route); require a clean tracked tree afterwards.
2. Focused suites with fresh execution at seed 5B407A2D9C1310E7: LBSolrClientAttemptLimitTest (14), LBAsyncSolrClientAttemptLimitTest (12), LBSolrClientTest (8), LBAsyncSolrClientTest (4), LBSolrClientRetryUnsentTest (3), LB2SolrClientTest (7), LBHttpSolrClientBadInputTest (1); 49 total, zero failures/errors/skips; both new selection methods must execute.
3. Root `./gradlew check -x test` at H in the full clone; require success, `git diff --check` clean, and a clean final tree.
4. Negative leg: second clean detached full clone at P; copy only H's `LBSolrClientAttemptLimitTest.java`; record the synthetic diff and the test file's hash before and after; run the two new methods at the same seed; both must execute and fail with the primary request exhausting its live-loop iterations on A (the [A, A] dispatch trace in the failure message). Any other failure shape does not count.
5. Final read-back: fork branch ref and live PR head both equal H.

The four original-production failures from `receipts/SOLR-18613.md` remain carried-forward historical evidence, as in round 1; do not rerun them and do not claim exact-head proof for them.

## Failure handling and deliverables

As round 1: investigate executed failures before retrying; any further source/test/formatting change requires an ordinary Nick Shanin commit, plain push, independent read-back, and a superseding gate. Write `receipts/SOLR-18613-selection-round-2.md` and an add-only completion report naming exact H/P/tree, commands, toolchain, logs/XML, counts, the negative baseline diff and outcomes, and the refreshed fork/PR heads. End the receipt with a concise factual description draft covering the whole PR (original attempt-limit change plus the selection repair) for owner review. No public comment, live PR-body edit, Jira write, or draft-to-ready change is authorized by this job.

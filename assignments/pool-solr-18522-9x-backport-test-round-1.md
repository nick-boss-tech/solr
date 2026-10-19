# Pool assignment: SOLR-18522 9x backport of the combined-query test, round 1

Status: DONE 2026-10-19

- Capability tags: implementation, gate. Intended host: vm2 (Linux). Recorded after the work; no claim file was held for it.
- Posted by: vm2 gate coordinator, 2026-10-19, at the direction of the user.

Context: The branch_9x backport of SOLR-18522 (apache/solr PR #5060; cherry-pick `3511867c3e` of `ff3ab1e726`) does not compile under `--release 11`, because `CombinedQueryComponentTest.java` uses a Java 15 text block. Once it compiles, `testFacetWithSmallMatchesAcrossSegments` fails. The 9x `BaseDistributedSearchTestCase` has no `query(String, SolrParams)` overload, so `query("/search", params(...))` resolves to `query(Object...)`, which turns `"/search"` into a parameter with no query and no JSON body.

## Items

1. **Text-block rewrite and handler parameter.** Done. Branch `solr-18522-9x-backport-fix` on the fork, commit `1f04a64e84`, based on `upstream/branch_9x` `3511867c3e`. One file changed: `CombinedQueryComponentTest.java`, 17 insertions and 16 deletions. Created as a new branch; no force push; no upstream branch touched.

2. **Gate evidence, JDK 11, `--release 11`.** Done. On commit `1f04a64e84`, `CombinedQueryComponentTest` passed 7 of 7 with seeds `18522C0FFEE00001`, `18522C0FFEE00002` and `18522C0FFEE00003` (fresh XML; logs `logs/vm2/18522-backport-<seed>.log`; summary `logs/vm2/18522-backport-seeds-result.txt`). Seed `18522C0FFEE18522` passed 7 of 7 with the same change before it was committed (`logs/vm2/18522-qt-fix.log`; summary `logs/vm2/18522-qt-fix-result.txt`). These runs were made directly with Gradle in scratch worktrees, not through `pool/bin/sweep-run.sh`, and without a claim. Treat them as scratch evidence.

3. **Failure analysis.** Done. A probe with the handler passed as a parameter, as in the 9x harness, returned 0 documents, size 0, `maxScore` null on the single-node control. The same request with `qt=/search` returned 4 documents, facet `[blue (2), red (2)]`, `maxScore` 0.0164, on both sides, matching the 10x result (`logs/vm2/18522-debug-ids-result.txt`, `logs/vm2/18522-debug-ids-10x-result.txt`). An earlier reading of a 9x combined-query defect was wrong; it came from this harness call.

4. **Not measured.** The distributed side's document count under the handler-parameter call. The harness compare stops at `maxScore`, so the distributed side's zero documents is an inference. The upstream comment says so.

5. **Upstream comment.** Posted by the user on apache/solr#5060 (comment 6260845993, edited 2026-10-19T06:34:33Z). It states the two changes, the control's zero documents, the distributed side's reported `maxScore` 0.0, the seed count, and a compare link to the fork branch.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Fork branch | decided: new branch on the fork, no force | `solr-18522-9x-backport-fix` only | user |
| Upstream comment | decided: the user posts, with text the user approved | no further upstream posts without the user | user |
| Production code | decided: no production change | the fix is test-side only | vm2 coordinator |
| Failed run | default: record, do not relaunch | a failed run is recorded with its failure text; the 9x runs above did not fail after the handler change | vm2 coordinator |
| Pool gate job | decided: no pool gate job | the scratch seed runs above are the record | user |

## Deliverable

This file. Items 1 to 5 above, with the commit, the logs and the status.

## Limits

- No PR, comment, JIRA or email by agents. No force push. No push to any upstream branch.
- Commit only to coordination, as Nick Shanin, with no trailers and no em dashes.

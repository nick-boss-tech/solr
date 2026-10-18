# Pool assignment: 2026-10-16 review follow-ups, code and tests (round 2)

Status: HANDED to vm1 by vm2 (2026-10-18). vm2 has no JDK and no ~/.pool-host, so items 2 to 4 did not run. Item 1 is stopped at a moved head. See claims/pool-review-2026-10-16-code-round-2.md.

- Capability tags: implementation, gate.
- Intended host: any Linux host with the Gradle toolchain (vm1, vm2, or vm3). Claim under `claims/` before starting (WORKFLOW.md). The Windows host never claims this work.
- Posted by: Windows review lane (review-round coordinator), 2026-10-17, at the direction of Nick Shanin.

These items come from the second review pass of open apache/solr PRs by nick-boss-tech, cycle 2026-10-16. Each item names the PR, the head reviewed, the claim, and the check. Findings were read from code and not run unless the item says so. Each item starts by running its check. If the check does not reproduce the finding, record that in the deliverable and close the item. If the fork branch has moved from the head named in the item, stop and record the new tip.

## Items

1. **SOLR-6831 (PR 5134): distributed refinement with partialResults=false.** Head `4afed04987b2343af58261b83c4b49fda0af45c0`, fork branch `solr-6831-submit`. The existing round 2 assignment (`pool-solr-6831-omitted-refinement-round-2`) is DONE at an earlier head, `3e07d801df5`. This item covers the later head. The finding was read from code, not run: under `partialResults=false` on a distributed refinement, the coordinator may return partly refined pivot counts labeled "omitted". No test asserts pivot content there. Also, no test isolates the change that stops refinement on omitted answers. Check: write a distributed test with two shards, one of which answers omitted, using `partialResults=false`. Assert the pivot counts and the response header. Run it at the head. Start from: `solr-6831-submit`. Output: `reports/2026-10-16-pr-batch/SOLR-6831-round-2-check.md`.

2. **SOLR-9124 (PR 5147): grouped distributed tests and the within-group clause.** Head `3dd985b93ac0e26a3850c338765ba1235bd811d4`, fork branch `solr-9124-submit`. The existing round 1 assignment (`pool-vm1-solr-9124-test-fix-round-1`) is DONE and covers the stats cache tests. Two gaps remain. (a) The grouped distributed classes have not been run at this head: `TestDistributedGrouping`, `BasicDistributedZkTest`, and `DistribCursorPagingTest`. (b) No run shows that the within-group clause fails when it is removed. The within-group clause is the condition this PR adds on the grouped stats path. Find it in the diff against `upstream/main`. If more than one clause matches, stop and escalate. Check: run the three distributed classes at the head with the seed recorded. Then, in a scratch copy with no commit, remove the within-group clause and run the grouped stats tests. Record by content whether anything fails. Start from: `solr-9124-submit`. Output: `reports/2026-10-16-pr-batch/SOLR-9124-round-2-check.md`.

3. **SOLR-18541 (PR 5117): a short timeAllowed list throws before the skip.** Head `81a88e40dd42f90fe6c67b2aeb6f678c437d7c0d`, fork branch `combinedquerycomponent-merge-totals`. The finding was read from code, not run: the real `timeAllowed` short-list shape still throws IndexOutOfBounds at `CombinedQueryComponent.java:342`, before the new skip near line 393. The new no-count test uses only an absent-key shape, so it does not reach this path. Also, `numFound` and `approximateTotalHits` still take `max()` (lines 455 to 456), and the new test pins `numFound` at 3. Check: build the shape a shard returns when it stops early under `timeAllowed`, by reading the code around line 342, and write a test for it. Run it at the head and record whether IndexOutOfBounds is thrown. Start from: `combinedquerycomponent-merge-totals`. Output: `reports/2026-10-16-pr-batch/SOLR-18541-check.md`.

4. **SOLR-18010 (PR 5118): undisclosed write access to SOLR_HOME, and the file-mode proof.** Head `571307400b6917ce458db989f29febb5cff8a600`, fork branch `solr-18010-submit`. Both findings were read from code, not run. The temp file on the write path needs write access to `SOLR_HOME`, and the PR does not disclose that. The file-mode claim and its 0644 proof are unresolved. The PR is a draft waiting on PR 5065. Check on a Linux host: run the write path with `SOLR_HOME` read-only, and record whether it fails. Then check the file mode the path creates against the 0644 claim. Start from: `solr-18010-submit`. Output: `reports/2026-10-16-pr-batch/SOLR-18010-check.md`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Item 1 fix shape | default: no production change; add one test that fails when the omitted-stop change is reverted, and record the proof | if the check reproduces pivot counts returned with header omitted, record the output and escalate to the main agent; do not change production code | Nick |
| Item 2 within-group clause | default: escalate if more than one clause matches | if removing the clause fails nothing, record it and escalate to the main agent | Nick |
| Item 3 numFound and max() | decided: do not change max() in this assignment | escalate to the main agent the choice between max and sum for numFound and approximateTotalHits | Nick |
| Item 3 short-list fix | default: the smallest fix that skips the short list at the same point the absent-key shape is skipped | if the fix changes numFound or approximateTotalHits, stop and escalate to the main agent | main agent |
| Item 4 SOLR_HOME write access | default: record it; no change to the write path | if the write path fails with read-only SOLR_HOME, escalate to Nick: disclose it or change the path | Nick |
| Item 4 file mode | default: record the created mode and the 0644 result | if the mode does not match 0644, escalate to Nick | Nick |
| Draft status and PR 5065 dependency | decided: no change to the draft state or the dependency | none | Nick |
| Commit history and changelog | decided: no history rewrites and no changelog edits | none | Nick |

No row may leave the choice to the claimant.

## Deliverable

`reports/2026-10-16-pr-batch/`: one check file per item, named as in its Output line. Each file gives the status (confirmed, refuted, escalated, or fixed), the command and its result, the seed for any Gradle run, and the failure text by content. Then `reports/2026-10-16-pr-batch/code-round-2-index.md` lists the four items with their status.

## Limits

- No PR, comment, JIRA or email. No change to any live PR title or body. No force push.
- Test-only commits may be pushed plainly to the matching fork branch, fast-forward only. Production code changes need a decision from the main agent first.
- Commit as Nick Shanin with no trailers. No em dashes.

# Open decisions, vm2 queue, round 1

Decided by vm5 on 2026-10-20T11:39:18Z with the decision ladder (`material/decision-ladder.md`). Each decision names its rung, its choice, and the reason. Nothing here is posted outside the pool.

## 1. SOLR-17055: maxScore for a score-sorted knn request

- Rung used: 1 (code and history).
- Evidence: the distributed path reports `maxScore` from the shard documents (`solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java:1086`, `:1167-1169`, and written at `:1293`). The single-node path sets it only when the searcher computed scores; a score sort orders by score, so the score is the value the sort already depends on.
- Choice: (a). `maxScore` is reported on a score sort, distributed and single-node alike. The contract is the one the distributed path already follows, and the single-node `null` is the inconsistency.
- Follow-up: the single-node change is a code change; it needs a claim, a fix, and a gate at the new head. The test then compares `maxScore` on both sides. Not implemented in this record.

## 2. SOLR-17842: docs-only placeholder fix, fork push

- Rung used: 4 (a genuine choice between defensible options; the owner's standing rules allow fork pushes and prohibit PRs and JIRA posts).
- Choice: yes. vm2 may push the docs-only patch to the fork branch as the claimant. No PR, no upstream push, no JIRA post.
- Reason: the change is docs-only, fork-only, and reversible by a later commit on the same branch.

## 3. SOLR-14231: takeover-log line

- Rung used: 1 (the file is not in the pr-prepare tree).
- Choice: no line is appended here. The restatement stays recorded in `gates/SOLR-14231-fail-before.md` and `receipts/SOLR-14231.md`; the main side appends the takeover line from its own records, as the assignment's step 4 already allows.

## 4. SOLR-4374: changelog wording and the function-alias Limits

- Rung used: 4.
- Choice: include both now, in the branch, before the gate result is used.
- Reason: the catch-all `*` behavior change is what the changelog exists to report; leaving it out hides a behavior change from users. The Limits line on function aliases is a stated limit of the same change. The cost is one gate run at the new head, which the assignment already requires.

## Follow-ups filed

- SOLR-17055 single-node maxScore change (claim and gate at the new head).
- SOLR-4374 gate re-run at the head that includes the changelog wording.

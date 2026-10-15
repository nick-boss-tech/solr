# Assignment: Landing-order and interaction review, round 1

Status: DONE

Capability tags: [review]. Intended host: Windows (review coordinator). Claim under claims/ before starting, per WORKFLOW.md.

## Owner directive: run to SATISFIED

- The review agent applies the wording changes its findings call for, in the same loop, and keeps reviewing in further rounds until a SATISFIED verdict comes back on each set below, as a set. A round that only lists findings without applying them does not discharge this assignment.
- Wording findings: apply the fix to the drafts (their Limits and landing-order lines) and push it.
- If a set's interaction turns out to need a code change, the reviewer writes the patch on the branch in its own worktree and pushes it, then posts a gate job under gates/ naming the patched head and the tests to run; a Linux host runs the gate. Ungated code never counts as reviewed. A merge-order problem that cannot be settled by wording or a gated patch comes back as an owner item.
- Scope questions: decide under the decision default (a recorded recommendation wins; where none exists, take the harder, more thorough option) and the three-rung ladder (where information is missing, get more information; where the cost is effort, do the hard thing; where an item is optional, defer it as a named follow-up in the draft's Limits). Record each decision in the deliverable report.
- No comments are posted by the review side anywhere. Anything that seems to need a comment is drafted in the deliverable report for the owner instead.
- Authored text: plain language, no internal process vocabulary in any text meant to be public, no "we" for Nick plus agent, no em dashes. Proof numbers only from a receipt or a gate log.

## Task

Review these branch sets as sets: for each, diff the branches against each other and against their merge-base (trial merges with git merge-tree are the established method; they write tree objects only and move no ref), establish the file overlap, the merge order the code actually requires, and any behavior interaction, then check every draft in the set against that result and correct its Limits and landing-order lines until the set is consistent.

1. The RestoreCore chain: SOLR-9865, SOLR-17287, SOLR-9091. The recorded landing order for RestoreCore is 9865, 17287, 9091. Interaction of record for SOLR-9865: SOLR-18532 lands first (its PR is #5100), and the 9865 branch drops its overlapping rollback hunk (commit cd46e4a3521) when prepared for opening; the packaging input is material/solr-9865-packaging-input.md. Verify each draft states the order and the 18532 interaction the same way, and that the stated order survives the diffs.
2. The IndexFetcher chain: SOLR-12246, SOLR-12085, SOLR-11650, SOLR-6711. Recorded landing order: 12246, 12085, 11650, 6711.
3. SOLR-6045 with SOLR-12703: they share a production file and a method. Establish which order merges cleanly and what the second one must reconcile, and verify both drafts say so.
4. SOLR-7504 with SOLR-12705: behavioral overlap on set-null and add/remove, no shared file needed for the interaction to matter. Establish the combined behavior when both land, and verify both drafts' Limits describe the other change accurately.
5. The pair SOLR-15478 with SOLR-15674: the adopted landing order is SOLR-15674 first. SOLR-15674's history scrub is done and its gate is green at the scrubbed head 75ddf9ba518a. Verify both drafts still state the order consistently at their live tips.

## Deliverable

reports/landing-order-interaction-review-round-1.md: one section per set, with the overlaps found by diff, the landing order each draft states, the corrections applied, and a verdict per set (SATISFIED as a set, or the remaining owner item).

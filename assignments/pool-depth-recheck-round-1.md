# Pool assignment: depth recheck of proof statements, round 1

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: two read-only subagents, about two or three tasks each. Claim: `claims/pool-depth-recheck-round-1.md`.

## Purpose

Five drafts carry proof statements that the receipts qualify, or that an earlier round judged on a thin reading. This round checks each Proof sentence against the receipt body and the object store, and records the exact text that the receipt supports.

1. `pr-drafts/security/SOLR-11678.md`. The receipt says "GATE GREEN at the packaged head; proof leg INCONCLUSIVE by compile". The draft's base-leg sentence (line 26) needs to match that.
2. `pr-drafts/highlighting/SOLR-2632.md`. The receipt says "NO GATE" and that the gate's pre-fix proof step would fail by design. The draft's Proof presents base-code runs on 2026-10-06 as evidence.
3. `pr-drafts/update-processing/SOLR-5939.md`. Its Proof says the combined run "passed 26 of 26" on "a local merge commit". An earlier round recorded the merged tree as absent from the repository.
4. `pr-drafts/solrcloud/SOLR-13876.md`. An earlier round accepted a date because the receipt says the round 35 re-runs at the same head are dated 2026-10-07, with no explicit gate date.
5. `pr-drafts/flaky-fixes/SOLR-18530.md`. An earlier round accepted a date because the gate was carried over to a tip that differs only by a changelog fragment.

## Checks

Subagent A takes tasks 1 and 2. Subagent B takes tasks 3, 4 and 5.

For each draft:
- (a) Quote every Proof sentence that states a result, a count, a head, or a date. For each, quote the receipt line that supports it, or record "none".
- (b) For every head the Proof names, check whether it is in the local object store (`git cat-file -e <sha>^{commit}`), and if it is, whether the file the Proof cites exists at that head (`git cat-file -e <sha>:<path>`).
- (c) Decide each Proof sentence: KEEP (receipt body supports it at the named head), REWORD (supported with a narrower claim), or REMOVE (only a takeover-log, ledger, or status-less statement supports it). Give the exact before and after text for each change.
- (d) State the verdict for the draft as CURRENT, QUALIFY, or HOLD, with one reason sentence.

Task 3 also asks for the combined tree: check whether `git merge-tree --write-tree` of the two heads named in the receipt reproduces the tree the receipt names. Do not create branches or worktrees. If an object is missing, say so and stop that step.

## Ladder

Information first from the receipt body and the object store. Where the receipt supports a narrower statement, use the narrower statement. Where the only support is a takeover-log or ledger citation, remove the sentence rather than keep a count that the rules bar. Where a head is not in the object store, mark HOLD.

## Hold rule

A draft whose named head is not in the object store is HOLD, not edited. A draft whose Proof must change, and whose head is in the store, may be edited by the main review only after this round's report.

## Rules

- Read-only subagents. No builds, no tests, no Gradle, no posting, no PR or comment, no force push, no fetch of new refs.
- Proof comes only from the named receipt body and the object store.
- No em dashes or en dashes in authored text. No PR numbers in file names or commit messages.

## Deliverable

`reports/depth-recheck-round-1.md`, verdict first, one section per draft, with the exact before and after text for each Proof change and the HOLD list. Mark the claim DONE in the same push as the deliverable.

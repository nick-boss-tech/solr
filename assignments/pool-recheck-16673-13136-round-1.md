# Pool assignment: receipt refresh recheck for SOLR-16673 and SOLR-13136, round 1

Capability tags: `review`. Intended host: windows. Staffing: two read-only subagents, one per ticket. Claim: `claims/pool-recheck-16673-13136-round-1.md`.

## Purpose

Two receipt commits landed on `pr-prepare` after the last handled tip:

- `3a126275957` refreshes `receipts/SOLR-16673.md`. The receipt now names the gated head `2adeae3bf8f14a64147d0dabc078b31ff378b4ab`, which is also the live tip on the fork. That head adds a ref-guide sentence after the changelog-title commit `d7170b12f312`.
- `65fa54a7b7a` adds the vm2 re-gate receipt for SOLR-13136 at live tip `486b38775565f7e369d5429c356ca83d750d710a`, with `gates/SOLR-13136.md` and a claim. No draft exists for SOLR-13136.

## Checks

1. SOLR-16673. The draft `pr-drafts/update-processing/SOLR-16673.md` names `d7170b12f312` as its head and says the only change since the earlier tested head is the changelog title. Both statements are now out of date. Verify the fork branch `solr-16673-submit` at `2adeae3bf8`, the diff from `d7170b12f312` to `2adeae3bf8`, and the refreshed receipt lines. Then state the exact draft changes needed: the head named in the Proof, the ref-guide sentence in "What this change does", the links at the named head, and the Proof dates and counts, each taken from the receipt.
2. SOLR-13136. Check the receipt and gate file for internal consistency: the head, the run window, the gate steps, the focused counts, and the "consistency pass only" statement. Check the fork head `486b387755`. Report any statement in the receipt that a reviewer would need to qualify. No draft is affected.

## Ladder

Information first, from the diff, the receipt, and the fork head. Where the draft is stale, apply the mechanical fixes that the receipt supports. Where a sentence needs a judgment (for example, whether the ref-guide sentence belongs in the outbound text), record the question for the main side rather than deciding it.

## Hold rule

The SOLR-16673 branch is held. Its draft must not name a head that is not the live tip. A draft edit that names `2adeae3bf8` is allowed only after the live tip is confirmed by `ls-remote`.

## Rules

- Read-only subagents. No build, no tests, no Gradle, no posting, no PR or comment, no edits to submit branches.
- Proof comes only from the named receipt and gate record.
- No em dashes or en dashes in authored text. No PR numbers in file names or commit messages.

## Deliverable

`reports/recheck-16673-13136-round-1.md`, verdict first, with the exact before and after text for each draft change. Mark the claim DONE in the same push as the deliverable.

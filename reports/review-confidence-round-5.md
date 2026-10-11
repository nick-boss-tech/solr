# Review confidence round 5: the newest ready PR bodies

Assignment: `assignments/pool-review-confidence-round-5.md`. Claim: `claims/pool-review-confidence-round-5.md`. Lead: the windows review agent. Part reports, with the exact replacement text for each drift item:

- `reports/review-confidence-round-5-a.md`: SOLR-18530 (PR 5098), SOLR-18532 (PR 5100), SOLR-18531 (PR 5101).
- `reports/review-confidence-round-5-b.md`: SOLR-3704, SOLR-2681, SOLR-4540, SOLR-16630.

Read-only throughout. Nothing was edited on a live PR, commented, reviewed, pushed to a branch or built. No gate ran.

## Verdict counts

Seven bodies: **5 DRIFT, 2 CONSISTENT.** Every live API head matched the fork tip and the receipt head, so no head mismatch was found.

| Body | Verdict | Part report |
|---|---|---|
| SOLR-18530 (PR 5098) | DRIFT (2 items, minor) | a |
| SOLR-18532 (PR 5100) | DRIFT (2 items; one is a record conflict) | a |
| SOLR-18531 (PR 5101) | DRIFT (2 items) | a |
| SOLR-3704 | DRIFT (4 items) | b |
| SOLR-2681 | DRIFT (2 items; one is a record conflict) | b |
| SOLR-4540 | CONSISTENT | b |
| SOLR-16630 | CONSISTENT (the public Jira read shows the status as Reopened, as the body says) | b |

## Drift items, in brief

- **SOLR-18530.** One sentence applies the other-shard case to a replica of this shard. Six links to unchanged code point at the head SHA where they should use the base commit.
- **SOLR-18532.** One link to unchanged code points at the head SHA. The second item is a record conflict (below).
- **SOLR-18531.** Two claims are contradicted by the code and by the body's own Limits: that a runner's `close()` releases its own socket, and that a stopped runner is still in its list after the stops finish. Replacement text is in part a.
- **SOLR-3704.** The title's "(or a Date.toString())" is not proven. The Limits "It covers..." wording and the "match-count check only" line misstate the test. The unique-key ISO claim holds for the stream only. Replacements are in part b.
- **SOLR-2681.** Scope wording ("top-level", "All other queries") needs the replacements in part b.

## Record conflicts, no replacement chosen

1. **SOLR-18532, the follow-up promise.** The body commits Nick to a follow-up that drops the SOLR-9865 rollback edit. The records still list Nick's sequencing decision as open (`reports/flaky-fix-review-round-1.md` D-a and s3b-D1; round 9 N4), and `reports/replication-backup-round-1.md` lists SOLR-9865 as landing first. Nick confirms the promise or strikes it.
2. **SOLR-2681, the follow-up sentence.** "A follow-up pull request is planned" is recorded as the decision in `material/highlighting-round-1-answers.md`. `reports/highlighting-answers-check-2681.md`, `reports/highlighting-round-1-g1.md` and `receipts/SOLR-2681.md` record no plan. Nick or the main side decides which record stands.
3. **SOLR-18531, the receipt.** `receipts/SOLR-18531.md` is stale. Its "In flight" paragraph (re-gate 3 running, GATE PENDING) and its "PR is open as a draft" line contradict its own status line, `gates/SOLR-18531.md`, and the live PR, which is ready. The receipt also does not record the two Limits gaps (a failed start does not re-reserve the port; the reservation map is keyed by port). Those Limits bullets have no receipt basis yet. The round 6 items verified both against the code, so no wording change is proposed.

## Decisions for the main side

- Apply the replacement text in the part reports to the live bodies, after the record conflicts are settled.
- Link the unchanged-code citations at the base commit wherever the part reports say so.
- Decide the two follow-up promises (SOLR-18532 and SOLR-2681) before any body is marked ready.

## Not done

- No GitHub write, branch edit, draft edit, build, test or gate.
- Claim-file marking is the windows side's; the claim is closed with this push.

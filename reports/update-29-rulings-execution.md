# Update-29 rulings: drafts brought up to the executed heads

Claim: `claims/update-29-rulings-execution.md` (commit `589b6d2e0f6`). Source: `material/update-29-rulings.md` (commit `020ff7addf4`), and the refreshed receipts for SOLR-6045 and SOLR-12705 at the same commit. Drafts: `pr-drafts/update-processing/`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Two subagents made the draft edits, in two sets. The lead reviewed every diff, fixed what the review found, and made the commits. Nothing was posted. No PR, submit branch, or PR description was changed. No builds, Gradle or tests were run.

## The rulings, as executed

The owner ruled on the three contradictions in the update-29 consistency pass. The main side executed them on the submit branches. The drafts now describe the executed heads.

- **R1, counted fields: SOLR-7504's rule governs.** SOLR-12705's counting pin was re-scoped to the atomic-operand opt-out, test only, at `aa56b7b1be4`. A combined run with SOLR-7504 at `22b77196e662` passed on 2026-10-09 (30 of 30 and 43 of 43). Landing order: SOLR-7504 before SOLR-12705.
- **R2, plain value ahead of an operation map: one rule, rejected in both orders.** SOLR-6045's merger check is widened to plain-first, with a null guard, at `bcae04d77bdb`. The receipt records AtomicUpdatesTest 32 of 32 at that head, and a base run in which both mixed-order tests fail.
- **R3, failed-stream attribution: SOLR-5939's per-request attribution stands.** No code change. SOLR-14718 lands after SOLR-5939, and its Limits describe the post-5939 mechanism.

Live tips checked with `git ls-remote` on 2026-10-09: SOLR-6045 `bcae04d77bdb`, SOLR-12705 `aa56b7b1be4`, SOLR-7504 `22b77196e662`, SOLR-14718 `29c09959791a`, and SOLR-5939 `f8d4bdbea518`. Each matches its named head.

## Draft changes

| Ticket | Change |
|---|---|
| 6045 | What rewritten to the widened check: any operation map makes a field atomic, mixed fields are rejected in either order, and null-valued fields are skipped. Proof cites the receipt (32 of 32, 0 failures, 1 skip), and the base run (6 failures in 32, both mixed-order tests among them). "No test covers either case" removed. Links moved to `bcae04d77bdb`, with base links kept where they describe base code. |
| 12705 | Links moved to `aa56b7b1be4`. The counted-field line now defers to SOLR-7504, instead of saying the count is one value. Proof cites the executed head, the opt-out test, and the combined run. The counts the receipt does not map to suites are kept as the receipt gives them. Limits gives the landing order: SOLR-7504 and SOLR-16655 first. The pre-fix sentence is removed, since the receipt names no fail-before run at this head. |
| 7504 | Limits: the shared-helper and "neither combined tree has been run" sentences are replaced with the executed state. SOLR-6045 applies the same rule in a different file. The combined run passed. SOLR-7504 lands before SOLR-12705. The "[helper]" link label is now "[count rule]". |
| 14718 | Limits rewritten to the post-5939 mechanism. Landing order: SOLR-5939 first. The copy names each request's own document. Two paths still name one request: a stream that reports none of its members, and an interrupted stream. The "exactly 1 test" line break, lost in an earlier edit, is restored. "Ticket packet" is now "ticket". |
| 5939 | The fallback sentence now says the stream names only its first request, which matches 14718 and the code. |

Each draft has zero em dashes, and none uses internal wording.

## Open for the next round or the owner

1. **SOLR-6045 changelog.** The changelog fragment does not yet say that a field mixing plain values with operation maps is now rejected with a 400. That is a behavior change for existing updates. The fragment is outside the draft, so the next round should check it.
2. **SOLR-12703 sentence.** Its Limits say the two merge without conflicts in either order, and that their combined behavior has not been run. That may describe SOLR-6045 before the widening. Not verified here. The next round should re-check it against the widened merger check.
3. **SOLR-12705 suite mapping.** The combined run gives "30 of 30 and 43 of 43" without naming the suites. The draft keeps the receipt's wording and does not map the numbers.
4. **Length.** SOLR-5939 is about 7,000 bytes and SOLR-6045 about 6,200, with links. Both are over the guide, as before. The owner decides whether to trim or accept.
5. **Still the owner's, unchanged.** The three rulings are executed as recorded. No new decision is taken here.

## Not done

No gate, build, or test was run. No PR, comment, submit branch, or PR description was changed. No draft was posted.

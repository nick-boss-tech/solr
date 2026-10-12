# SOLR-10882 proof date (ST-R1-09), round 1

Verdict: **USE DATE, and the date is corrected to 2026-10-11.** The draft's Proof said "verified 2026-10-10". That date came from a takeover log, which the rule excludes. The recorded gate completion for the head `5d7d07a5b17` is dated 2026-10-11 in UTC, so the Proof now carries that date.

Scope: `assignments/pool-solr-10882-proof-date-round-1.md`. Claim: `claims/pool-solr-10882-proof-date-round-1.md`. One read-only subagent. The fork head `5d7d07a5b17` matches the live branch.

## The search

- **Receipt.** `receipts/SOLR-10882.md` records the head and the counts (ArrayEvaluatorTest 7 tests, 0 failures, 0 errors, 0 skipped) but no run date.
- **Gate record.** `gates/SOLR-10882.md` records "DONE ... 2026-10-11T02:32:06Z (UTC). GREEN at packaged head `5d7d07a5b17`". The claim file records the same result line. This is a recorded run completion date for the head, so it counts.
- **Excluded.** The claim's start time and heartbeat are claim dates. The premise run in the gate file ran at a different head, `83fc3dfeb2`. The 2026-10-10 date comes from the takeover log, which is not on disk, and it is excluded by rule.

## Ladder rung

Step 2 of the ladder: a recorded run date exists for the head, so it is used. The receipt-only reading would remove the date instead. The assignment's own ladder lists the gate record as a source, so the date stays and is corrected.

## Before and after

- **Before:** "With this change, at head `5d7d07a5b17`, verified 2026-10-10, ArrayEvaluatorTest runs 7 tests with 0 failures, 0 errors and 0 skipped."
- **After:** "With this change, at head `5d7d07a5b17`, verified 2026-10-11, ArrayEvaluatorTest runs 7 tests with 0 failures, 0 errors and 0 skipped."

## Checks

- The counts in the Proof match the receipt.
- No dash or process word was introduced.

## Note for the main side

The register asked for a date only when the receipt records one, and the receipt does not. The gate record is the source used here. If the main side wants the receipt to be the only source, the alternative is to remove the date, which is a one-line change.

## Convergence line

ST-R1-09 is closed on the recorded gate date.

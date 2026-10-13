# SOLR-18010 G3 green record, and the receipt and draft: round 1

Trigger: pr-prepare tip `83b964b1097`, from `84982a6`. vm2 recorded the G3 gate GREEN at `e99be67c9a6f`. Round claim: `claims/pool-pr-prepare-tip-84982a6-round-1.md`.

Scope: read-only checks. No gate relaunched, no PR, comment or live body touched, no branch moved, no build, test or Gradle run. No draft or receipt edited this round; the corrections below are listed for the owner or the record owner.

Heads, checked by ls-remote: `review-r1-solr-18010` is `e99be67c9a6fe15f86306104843a30b35196f159`, and `solr-18010-submit` is `2b5ad801c9e88807337f4f0a3fdd689126ad37d1`. Neither has changed. Route 1 is not pushed.

## 1. What the G3 green supports

- As a regression check at the current head: yes. The record shows 17 of 17 from fresh XML at `e99be67c9a6f`, with the focused checks at rc 0. Seed `18010C0FFEE18010`. The logs are on vm2 and were not checked here.
- As the fail-before the review patch needs: no. The new test passes at the parent `2b5ad801c9e` (the 18:24Z record says "The new test does not fail at its parent"). The G3 job has no fail-before step, and the current test cannot produce a parent failure by code reading.

## 2. The stop rule was triggered, and nothing records a decision on it

The G3 job says: "If that result finds the new test does not pin the review patch's change, stop and report instead of running." The 18:24Z record found exactly that. G3 ran anyway (claim 19:35:37Z, GREEN 19:51:22Z). No record says the stop rule was considered, waived, or re-decided.

Owner item: decide whether the green stands. It stands as a regression check. It does not stand as the fail-before.

## 3. Receipt corrections (record owner)

- The receipt's sentence that the fail-before premise for the new test "is still open" conflicts with the gate record's classification, which is FAILED as a test premise defect. The new test is not named in the receipt.
- The G3 section names no log. The seed and head are present. The log names are in `gates/SOLR-18010-review-r1-g3.md`.
- The receipt's line that the 0644 result "conflicts with the 0600 premise in the draft" is stale. The draft no longer carries 0600.

## 4. Draft: no fail-before claim for the current test

Neither the receipt nor the draft claims that the current test fails before the review patch, or pins the `finally` change. Zero such sentences.

Draft items, held:
- When the patch lands, the draft's "logged at debug level" becomes warn, and "4 of 4" becomes "5 of 5".
- The 0640-and-0644 sentence should name the G1 run and head beside it. The values are supported by G1 and the permission re-gate.
- The concurrency fail-before sentences rest on logs off the branch (round 1 owner item O4).

## 5. Owner items

1. G3 ran past its stop rule. Decide whether the green stands, and record the decision in the receipt.
2. The parent-baseline "6 of 6" has no seed or head on the branch. Add them.
3. The 0644 mechanism is unsettled. The JDK predicts 0600 for the temporary file on POSIX, and the record reports 0644.
4. Route 1 remains unpushed, and the push still waits for your go-ahead.

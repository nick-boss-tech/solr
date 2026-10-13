# Round 2 review inputs, and the SOLR-18010 review-patch gate record: round 1

Trigger: pr-prepare tip `a536bbd4469`, recorded from `2d694be`. vm2 recorded the SOLR-18010 review-patch gate at `e99be67c9a6f` as NOT GREEN, not relaunched, and left for the main agent. A queue-monitor pass showed the SOLR-18539 gate running. Round claim: `claims/pool-pr-prepare-tip-a536bbd-round-1.md`.

Scope: records and analysis only. No gate was relaunched, and no gate status line or claim was changed. No draft, PR, comment or fork branch was touched. No build, test or Gradle run.

Heads: `review-r1-solr-18010` is `e99be67c9a6fe15f86306104843a30b35196f159`, matching the record. The reviewers also found `review-r1-solr-18539` and `review-r1-solr-18541` at the recorded full SHAs.

## 1. The SOLR-18010 review-patch gate record

What the record supports:
- The head run, tidy, spotless, the Error Prone compile and the module check all returned rc 0.
- The new test fails at the merge-base, as the premise predicts. The mechanism is in base `persistConf`: `Files.newOutputStream` truncates the file before `Utils.toJSON` writes it, so a failed write leaves an empty file.

What the record does not support:
- A green fail-before, on the gate file's own wording. The record names one of four failing tests, `testFailedWriteKeepsPreviousFileAndLeavesNoTemporaryFile`. The other three are named only in the queue monitor: `testConcurrentEditsToLocalSecurityJson`, `testConcurrentPersistConfLeavesOneWholeDocument`, and `BasicAuthStandaloneTest.testBasicAuth`. All three are predicted by the premise, but the record does not name them and no log is on the branch.
- The gate file has no "every rc zero" rule. Its step 6 expects the fail-before run to fail, and "on green" is undefined there and in `WORKFLOW.md`. The record applied a stricter rule than the gate file states.
- The review delta, the warn-level logging and the `finally` guard, is not exercised by the fail-before at all.

Position: the record shows no defect in the patch. It leaves two questions open. Does the fail-before satisfy step 6, given three further base failures? And which rule decides "green"?

Settling fact: the failing-test list with assertion messages, in `logs/vm2/queue-g18010-review-r1-gate-20261013T171513Z.out` on vm2. Copying that list onto the branch would settle it.

Not ruled here. The main side decides the rule. The reviewer does not relaunch.

## 2. Round 2 input status board

The round 2 start rule says the review waits until the inputs are DONE, or the owner directs a subset. No input is DONE GREEN, so the rule is unmet.

| # | Input | Status | Supporting line |
|---|---|---|---|
| 1 | #5115 patch gate | RUNNING | Queue monitor, line 1218: the SOLR-18539 review-patch gate is running (compile step, no GATE END). The gate status line still reads OPEN, unclaimed. Claim at 17:01Z |
| 2 | #5117 patch gate | CLAIMED OPEN | `claims/pool-vm1-gate-18541-review-r1-patch.md` line 7: run queued behind the flock. Heartbeat 17:10Z |
| 3 | #5118 patch gate | DONE NOT GREEN, not marked DONE | Gate line 4: "GATE RUN, NOT GREEN, left for the main agent (not relaunched)." |
| 4 | QueryComponent real-exit premise | CLAIMED OPEN | Claim line 7: run queued in the chained runner. Heartbeat 17:10Z. No run record |
| 5 | Symlink and bind-mount premise | BLOCKED on the bind-mount leg | Gate line 4: DONE 17:08Z. The symlink leg was confirmed by run. Line 35: single-file bind mount SKIPPED, no mount rights |
| 6 | Permission legs G1 to G4b and MultiAuthPluginTest | CLAIMED OPEN for G1 and G2 | Claim line 7 covers G1 and G2 only. G4b has no claim |
| 7 | #5117 score-only exit input | CLAIMED OPEN | Claim line 8: run queued in the chained runner. Heartbeat 17:10Z |

## 3. Conflicts for the main side

- The #5117 review-patch gate names seed `C0FFEE42C0FFEE42`. `receipts/SOLR-18541.md` line 7 records `18541C0FFEE18541`, and the 17:40Z recheck marks the 18541 work NOT READY. The seed in the gate spec needs correcting. That is in the vm1 claim's scope.
- The queue monitor (line 1220) still lists the score-only job as claimable, although vm1 holds a claim on it.
- Gate status lines for inputs 1, 2, 4, 6 and 7 still read OPEN, although their claims are running or queued. The sync is a record correction, not a verdict.
- G4b, the single-file bind mount, has no claim. It needs a Linux host with mount rights.

## 4. What this round does not decide

- The rule for fail-before green, which decides whether input 3 is GREEN.
- Whether the round 2 review starts on a subset. The owner has not directed one.
- Whether the SOLR-18541 review-patch gate's width or seed is corrected before it runs.

## Next steps

1. The main side rules on the fail-before rule for input 3.
2. vm2 copies the failing-test list with assertion messages from its log onto the branch, for input 3.
3. vm1 corrects the seed in the #5117 review-patch gate spec, before its run.
4. Gate status lines are synced to their running claims. Not a verdict.
5. A claim is made for G4b, on a Linux host with mount rights.
6. The round 2 review starts when the owner directs a subset, or when inputs 1 to 7 are DONE.

# Gate records for SOLR-18539 and SOLR-18010: recheck, round 1

Trigger: pr-prepare tip `63216a312c0`, recorded from `4b705b3`. Two new records: the SOLR-18539 review-patch gate recorded DONE GREEN at `fbf7610644d1` (classified by content), and the SOLR-18010 review-patch gate investigation with a parent-baseline run. Round claim: `claims/pool-pr-prepare-tip-63216a3-round-1.md`.

Scope: records and reading only. No gate relaunched, no status line changed, no round 2 review started, no PR, comment or live body touched, no fork branch changed, no build, test or Gradle run.

Heads: `review-r1-solr-18539` is `fbf7610644d178049e1110ffc0ec34dad8c5b32f`, and `review-r1-solr-18010` is `e99be67c9a6fe15f86306104843a30b35196f159`. Both match the records.

## 1. SOLR-18539 review-patch gate

Verdict on the gate under its own wording: partly supported. Not settled.

- The PR-head fail-before leg matches step 7 of the job file exactly: the same four tests, the same message, and no other failure.
- The merge-base fail-before leg names nine failing tests. Each maps onto one of the nine categories the job predicts. But the job predicts no count, and the record gives no failure texts. Step 6 requires the texts.
- The fresh head XML was overwritten by the later fail-before run. So "0 failures" at the head rests on the BUILD SUCCESSFUL line alone.
- "GREEN" is the coordinator's classification by content. The job file defines no green rule.

Settling fact: the merge-base failure texts, in `logs/vm2/18539-review-r1-failbefore-base.log` on vm2. An unpredicted message would reopen the verdict.

Verdict on the pinned width against Option B: not supported. This is a real conflict, and it needs your call.

- The patch pins the any-skip rule. Lines 191 and 486 of the test file expect `numFoundExact` FALSE for a skipped shard that reports an exact zero. Option B gives TRUE there.
- The production guard in `QueryComponent.java` (lines 1237 to 1238) clears exactness on any skip.
- The canonical round 1 report decided Option B under the decision default, and the addendum did not. The round 2 owner rulings cover only SOLR-18541, so nothing binding decides the width for SOLR-18539.

Settling fact: the result of the QueryComponent real-exit premise run, Q1 in `gates/queryComponent-real-exit-shape-premise.md`. Zero hits with an exact flag from a real exit would bear on the width. That run is claimed and has no result yet.

Scope check: the patch's own delta against its parent is exactly `QueryComponent.java` and `QueryComponentPartialResultsTest.java`. The three-file diff against the merge-base also includes the PR's own changelog YAML.

## 2. Classification inconsistency

- The SOLR-18539 record at `261f50a2de3` says "the coordinator rule requires every rc=0 for GREEN, so this is not recorded GREEN."
- The record at `63216a312c0` then classifies the same kind of nonzero fail-before legs by content and records GREEN.
- The SOLR-18010 gate record still reads "NOT GREEN (fail-before rc=1; ...)", with its content investigation pending.
- Neither gate file states a green rule. The rule has to be settled once, for both gates.

Not ruled here.

## 3. SOLR-18010 investigation and parent-baseline run

Verdict: NOT ESTABLISHED. The three extra base failures are not shown to be premise-predicted.

- The parent-baseline run used `2b5ad801c9e`, the PR head's parent. The job file's step 6 names the merge-base `14c7aac0d151`. Using the parent is a protocol change, and it is not recorded as one.
- The run is claimed, not executed. It has no log, counts or seed on the branch.
- The gate status line names all four failures and points to `logs/vm2/18010-review-r1-failbefore.log`. It gives no seed ("seed in that log").
- Failure text exists on the branch only for the new test and for `testBasicAuth`. The two concurrency tests have none.
- By code reading, the new test should pass at the parent. The record expects only the new test to fail there, so the expectation looks wrong.
- The record's rationale for `testBasicAuth` (the temp-file-plus-move) does not match the check. The check, `containsKey("")`, is fixed by the marker strip.
- The premise prediction for the concurrency tests is not in the canonical report or in the premise gate file. The only statement is in `receipts/SOLR-18010.md` at line 7, which is a narrated run with no log on the branch.

Settling fact: the JUnit failure lines and seed for `testConcurrentEditsToLocalSecurityJson`, `testConcurrentPersistConfLeavesOneWholeDocument` and `BasicAuthStandaloneTest.testBasicAuth`, in `logs/vm2/18010-review-r1-failbefore.log` on vm2. Copying those lines onto the branch would settle it.

## 4. Round 2 start rule

Still unmet. Of the seven inputs:
- Input 1 (SOLR-18539 patch): recorded GREEN by content, with the width and the rule open.
- Input 2 (SOLR-18541 patch): claimed, queued.
- Input 3 (SOLR-18010 patch): NOT GREEN, investigation not established.
- Inputs 4, 6 and 7: claimed, queued.
- Input 5 (symlink and bind mount): blocked on the bind-mount leg.

## Owner and main-side items

1. SOLR-18539 width. Option B, or any-skip. The patch's tests at lines 191 and 486 pin any-skip. If Option B stands, those expectations change and the patch needs a re-gate on vm2. Changing the review branch is a fork change, so it waits for your go-ahead.
2. One green rule for fail-before legs, applied to both SOLR-18539 and SOLR-18010.
3. SOLR-18010 baseline. Job step 6 says merge-base, and the record ran the parent. Either re-run at the merge-base, or record the protocol change in the job file.
4. Copy the failing-test lines and seeds from vm2's logs onto the branch for both gates. The logs are not in the repo, so the settling facts above are not checkable from here.

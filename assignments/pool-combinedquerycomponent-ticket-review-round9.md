# Pool assignment: CombinedQueryComponent Jira ticket draft review, round 9

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1 or 2 subagents. Claim: under the standing coordinator claim, `claims/coordinator-review-rounds.md`.

## Scope

Round 8 was run on vm2, because no reviewer claimed it, and returned STILL OPEN (`reports/combinedquerycomponent-ticket-review-round8.md`). Its (c) sweep listed 14 items with replacement texts. The labels for those items were applied in `e7a761a61a`, and C12 was not, because its citation of case X1 did not match the draft. The prep report `reports/combinedquerycomponent-round-8-labels-round-9-prep.md` records the labels and what round 9 must check.

The main side then tagged the cases in the draft: line 48 now names X1 (all shards exact, merged flag false) and X2 (all shards inexact, merged flag true), and the exactness bullet at line 52 carries the case tags. That edit closes C12 and the citation in line 22 ("cases X1 and X2 below"), which had the same gap (C3). It is in the commit that carries this assignment.

The round 8 rules still apply: a fix-design sentence is a proposal and is judged under (b) only. A factual claim about current behavior carries a label, a run result, a scope, or a statement that it is not settled.

## What to check

Read the draft at the current pr-prepare tip (the commit that carries this assignment).

(a) **Confirm the round 8 labels in substance.** For each of the 14 items below, check that the label or edit is present, that it sits in the sentence it was written for, and that any code claim in it holds at `d76fc14b9857`:

- C1 line 21, "(from code reading, not run)"; C2 line 22, the label after "Only a mixed merge happens to come out right."; C3 line 22, "(confirmed by run, cases X1 and X2 below)", which now resolves to the tags at lines 48 and 52.
- C4 line 40, "headered or not", labelled from code reading with the run pointer; C5 line 40, the arrival-order formula rewritten to count only shards that complete normally, citing `HttpShardHandler.java` line 440 for completion order; C6 line 40, "2 instead of 5", labelled.
- C7 lines 42 to 43, the numFoundExact paragraph, labelled; C8 line 54, the grep-based test claim, labelled.
- C9 line 56, "any shard response that completes without the header"; the shard that returns on a search exception at lines 348 to 350 is outside it.
- C10 line 66, "every shard that sends a per-query entry", labelled; C11 line 66, "confirmed by run, fallback case below".
- C12 lines 48 and 52, now tagged X1 and X2 (the edit to check). Confirm each tag names the outcome its case is cited for, and that line 22's "all-exact ... false" and "all-inexact ... true" match X1 and X2.
- C13 line 68 and the lines it cites at 433 to 434 and 441 to 442. The prep report says the failed-queue-push caveat at line 434 was added this round without reading `ShardDocQueue` (outside the listed sources). Read that code at `d76fc14b9857` and check the caveat's claim, and that the `max` fold at 441 to 442 is labelled as stated.
- C14 line 68, the four-documents example, labelled from code reading.
- Observation, line 56, the unguarded `docList.get(queryIndex)` at line 340. Confirm the IndexOutOfBoundsException claim is labelled.

(b) **Check the fix-design items for proposal phrasing**, as round 8 did: line 62, the two items on skipped shards and the zero count; line 66, the reconstruction, the testing-before-accumulating, and the double-count items. Round 8 rephrased all five and they should read as proposals now. Confirm that and list anything still in present tense. If you find one, rephrase it under the patch-loop directive and list before and after, committed in the same push.

(c) **Repeat the sweep for factual claims only**, over the whole ticket text. Give each unlabelled factual claim its passage and replacement text. Do not apply (c) findings in this round, except the (b) rephrasings above.

(d) **Verdict.** SATISFIED if (a) holds, (b) is clean or patched clean, and (c) finds nothing unlabelled. Otherwise STILL OPEN, with the exact remaining items and their replacement texts.

On SATISFIED, the ticket text is final. It is the last of the three texts for the consolidated new-ticket email (the mergeIds and gradlew texts are already final), and the email goes out once this round returns SATISFIED. The email is not sent by this round.

Out of scope: the gate job wording (its owner is the gate coordinator), the probe source, the SOLR-15319 premise record in the prep report (another ticket), and anything rounds 1 to 8 confirmed beyond item (a).

## Deliverable

`reports/combinedquerycomponent-ticket-review-round9.md`, verdict first: SATISFIED or STILL OPEN, one line per item under (a) and (b), the (c) sweep result, and, for anything open, the exact passage and the replacement text. Any (b) draft edits are committed in the same push, each listed before and after.

## Limits

The draft may be edited only for item (b). No PR, comment, Jira or email. No build, test or gate. Commit as Nick Shanin, no trailers, no em dashes.

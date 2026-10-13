# SolrCloud PR drafts: critical review with JIRA context, round 1

Verdict: none of the nineteen drafts is ready to submit. Thirteen are HOLD: each has a fixable gap in the Proof, the Choice, the JIRA fit, or the verification date. Six are NOT READY, because a branch is stale, a Proof is missing, or the text answers less of the ticket than the draft implies. Two of the six are structural: SOLR-14919 needs a new gate on its new head, and SOLR-15674 has a Claude co-author trailer in its branch history.

This review reads each draft against its JIRA text, its receipt, its fork tip, and the code. It applies the PR formula: the length guide, the simple-language rule, the header and footer, a Choice only for a live alternative, and a verification date on every count. The earlier readiness rounds checked receipts and heads only. They did not apply those tests, so their READY verdicts do not carry over.

Basis: pr-prepare `569c26f5b08`, fork tips as listed in the claim, JIRA text fetched read-only (summary, description, comments), code at the fork tips and merge-bases. Claim: `claims/pool-solrcloud-pr-critical-review-round-1.md`.

## Shared gaps across the group

- **No verification date.** No draft's Proof, and no receipt, gives the date the count was verified at its head. The formula requires "verified <date> at this head".
- **Length.** Thirteen drafts are over the 3,500-character guide, from about 3,520 to 5,300 characters. Six are under it: SOLR-11479 (3,474), SOLR-13186 (3,103), SOLR-16013 (3,359), SOLR-15035 (3,053), SOLR-13369 (2,908), and SOLR-12991 (2,846).
- **Choices missing.** Where a behavior change has a live alternative, most drafts give no Choice, or a Choice that leaves out an option the reporter raised.
- **JIRA requests not answered.** Several reporters asked for something the draft does not do, and the draft has no Limits line for it.
- **Proof on an old head.** Several counts were recorded at an earlier head than the fork tip.

## Per ticket

| Ticket | Verdict | Main blocker | JIRA gap | Length |
|---|---|---|---|---|
| SOLR-5813 | HOLD | Direction: defaults the name where the title asks for a clean failure | Reporter asked for a failure in SolrCloud mode | 3,692 |
| SOLR-11288 | NOT READY | Proof cites the old head (`cd094c3c623`); fail-before not recorded at the new head | Reporter's failure is a comma inside a value; escaping not added | 4,265 |
| SOLR-11479 | HOLD | Lands after SOLR-15035, but no link or state; no verification date | Reporter's case covered; multi-replica 400 outside the ticket | 3,474 |
| SOLR-12651 | NOT READY | Proof count belongs to the prior head (`f3131d1ee846`), not `404f13b501c9` | Reporter asked for DEBUG on copy logs; not covered | 3,522 |
| SOLR-12991 | HOLD | Choice omits INFO, which the commenter suggests | Covered; cause is in the stack trace only | 2,846 |
| SOLR-13186 | HOLD | The dated run is now in the receipt (2026-10-13, one test, no failures, at the live head), but the draft's Proof does not carry it yet: one line to add | Covers the skipped-start path only; Limits says so | 3,103 |
| SOLR-13369 | HOLD | Seed narration is process; the cause should be labelled as the reporter's reading; no date | Reporter asked test-or-router; the draft changes the test | 2,908 |
| SOLR-14919 | NOT READY | New head `8f1f4ef91e79` (javadoc rewrap only); `c1fa37e33c1` gate failed Spotless; links and Proof cite `c1fa` and `84e7bcaeb57`, which still has the recovery marker | The ticket is about replication; the draft fixes forwarded commits only | 4,068 |
| SOLR-15035 | HOLD | No Choice on numShards (all slices or active only, or drop it); failing-run counts missing | Consistency delivered; the reporter's numShards question has no choice | 3,053 |
| SOLR-15106 | NOT READY | Keeps every `return` and resigns leadership instead; base compile message not recorded | Title asks that the thread not return; ZooKeeper outage not reproduced | 4,076 |
| SOLR-15386 | HOLD | No verification date; Choice's first alternative gives no cost | Restart symptom matches; broader idea from a commenter not in Limits | 4,466 |
| SOLR-15674 | NOT READY | `Co-Authored-By: Claude Sonnet 5.5` trailer in branch-only commit `9b51865402d` (verified); length | Reporter's delete-and-recreate sequence not run | 4,986 |
| SOLR-15863 | HOLD | Proof at `f381fd8d4dd`, tip is `78dff1d52f6c` (one changelog commit past); count 7 vs 6 test methods in source | Version half matches the ticket | 4,033 |
| SOLR-16013 | HOLD | No timeout on the Overseer wait; the close order is not described; serial close suggestion has no Choice | Symptom matches; reorder suggestion unaddressed | 3,359 |
| SOLR-16437 | HOLD | Wrong-shard 400 path untested; 39 nightly tests skipped, not disclosed | Misspelled-replica 200 matches | 5,092 |
| SOLR-17292 | HOLD | Verification date missing; node-down message now sent when persist throws (behavior change, no Choice) | Reporter's "throw the relevant exception" answered | 4,885 |
| SOLR-17680 | HOLD | Proof names one head; re-gate open; "error text changes" describe text clients never see (checked earlier at CollectionsHandler) | Answers the ticket; v2 "starts working" rests on a test checking only the message | 4,826 |
| SOLR-17733 | HOLD | Dated run at the head still open; a local-delete failure now returns success with the file left on disk | Sync complaint answered only as "not reproduced"; reporter asked for a 501 or an explanation | 4,242 |
| SOLR-9155 | NOT READY | Proof is a bracket; "1 of 1 at dbd88c84ff2" is the old wrapping-route count; declared-route gate not run | Reporter asked for better ops logging; the draft removes the only ERROR log without answering him | 5,272 |

## Things to check before any of these is submitted

- **SOLR-14919** needs a gate at `8f1f4ef91e79`, and its Proof and links need re-pointing. Two points the group raised: the draft fixes forwarded commits but not the replication failure the ticket is about, and the recovery marker is not yet explained (SOLR-12933).
- **SOLR-15674** needs the `Co-Authored-By` trailer removed from `9b51865402d`, which is a history rewrite on the fork. The force-push allowance covers it. The trailer itself breaks the formula's rule on trailers.
- **SOLR-9155** cannot be submitted while its Proof is a bracket. Its declared-route gate has not run.
- **SOLR-11288 and SOLR-12651** were marked READY earlier. Their Proofs cite old heads, and the review finds the JIRA gaps above.

## Not done

- No PR opened, no comment, no JIRA write, no email.
- No build, test, or gate. The vm2 records were read as records only.

# Pool assignment: update processing convergence, round 5

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim: this round runs under the standing coordinator claim, claims/coordinator-review-rounds.md.

## Scope: the three drafts open after round 4, plus one re-verification

Round 4 (reports/update-processing-convergence-round-4.md) returned NOT CONVERGED: 7 of the 10 re-checked drafts converged, and three did not. Each of the three has one should-fix item with the fix recorded in the round 4 report. This round re-checks only those three drafts, against the live draft text in pr-drafts/update-processing/ and the records the round 4 findings cite:

- **SOLR-14718** (round 4 finding 1). Verify whether receipts/SOLR-14718.md now records the 2026-10-09 settling run from the main-side record (seeds 14718C0FFEE14718, 14718C0FFEE14719 and 14718C0FFEE14720, one AssertionError each at SolrCmdDistributorTest.java:572, and a control at the unmodified head with 0 failures, as held in audits/update-processing/SOLR-14718.md lines 35 to 39). Then check the draft's line 25 sentence against that record. If the receipt still lacks the run, the draft is NOT CONVERGED and the finding stands as a receipt item.
- **SOLR-3657** (round 4 finding 2). Verify the bold line 22 against the replacement recorded in the round 4 report: the run without the fix is stated on the merge-base code at 97d973814336 with this branch's DocumentBuilderTest.java applied, matching the condition in receipts/SOLR-3657.md line 8. Bullet line 25 already carries the condition; check the two passages agree.
- **SOLR-5065** (round 4 finding 3). Verify lines 9 and 25 no longer call 0cc328310f8f the merge-base, against the replacement clauses recorded in the round 4 report (the merge-base of ab894a996c8a with upstream/main is 97d973814336; 0cc328310f8f is the base production code the proof ran on). Carried note from round 4, not a finding: the class link at line 25 runs to L31-L49 while the file is 48 lines at ab894a996c8a; confirm whether it now reads L31-L48.

## Re-verification: SOLR-7022

Round 4 marked SOLR-7022 CONVERGED. After round 4, the open review fixes re-check (reports/review-recheck-round-4.md) found and corrected two run claims in the draft, at lines 24 and 26, that round 4 had read as matching. Re-verify the corrected text against the record:

- Line 24: the run it now cites is the 2026-10-05 gate at a2ed957063b in receipts/SOLR-7022.md line 5 (DirectUpdateHandler2AwaitSearcherTest 2 of 2, DirectUpdateHandlerTest 7 of 7). No 2026-10-06 CommitWaitTest run is claimed; receipt line 7 records that the top-up did not run CommitWaitTest.
- Line 26, last sentence: DirectUpdateHandler2CommitWaitTest is "read to fail" at the interrupt-restored assertion and "this was not run", matching audits/update-processing/SOLR-7022.md finding 3.

Confirm both corrections hold as written, or name the exact passage and the record line it disagrees with. This re-verification covers lines 24 and 26 only; the rest of the draft stays converged and is not re-opened.

## Out of scope

The drafts converged in round 4 (SOLR-6973, SOLR-12705, SOLR-16655, SOLR-5941, SOLR-16673, SOLR-7504) and the drafts converged in earlier rounds (among them SOLR-11483, SOLR-12864, SOLR-13265, SOLR-13943, SOLR-5939, SOLR-6065) are not re-opened. Settled decisions in material/update-processing-*.md and in the round 1 to round 4 responses stand.

## Deliverable

reports/update-processing-convergence-round-5.md: verdict first (CONVERGED or NOT CONVERGED overall), then one line per draft in scope (SOLR-14718, SOLR-3657, SOLR-5065, SOLR-7022) with its verdict. New findings, if any, are graded blocking, should-fix or note, carry the register prefix UP-R5, and give exact replacement text for anything the main side should apply.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). Read-only on drafts, receipts and branches; the only writes are the claim, the report, and heartbeat updates. No em dashes in authored text.

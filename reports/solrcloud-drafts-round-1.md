# SolrCloud drafts round 1: fidelity pass and missing drafts

Assignment: `assignments/pool-solrcloud-drafts-round-1.md`. Claim: `claims/pool-solrcloud-drafts-round-1.md`. Lead: the windows draft agent. Part reports, each with the per-ticket checks, the exact corrections and the gaps:

- `reports/solrcloud-drafts-round-1-sc1.md`: SOLR-15674, 5813, 17292, 12991, 15035.
- `reports/solrcloud-drafts-round-1-sc2.md`: SOLR-15863, 12651, 13186, 15106, 9155.
- `reports/solrcloud-drafts-round-1-sc3.md`: SOLR-15386, 11288, 13369, 14919, 17680.
- `reports/solrcloud-drafts-round-1-sc4.md`: SOLR-17733, 13239, 18277, 16013, 11479.
- `reports/solrcloud-drafts-round-1-sc5.md`: SOLR-3865, 4754, 10234, 10641, 16437.
- `reports/solrcloud-drafts-round-1-sc6.md`: SOLR-17281, 7394, 12998, 13136, 18391.

Scope: the thirty tickets in the answers file, each with a receipt. Drafts were corrected in place where a draft existed, and only where the answers and the receipt support the change. Nothing was committed, pushed, posted or built, and no GitHub or Jira write was made. Heads were checked against `ls-remote`. Merge-bases were taken against the worktree's local `upstream/main`, which may be behind upstream.

## Per ticket

| Ticket | Result | Note |
|---|---|---|
| SOLR-15674 | corrected | merge-base links for symptom code; follow-up offers in Limits |
| SOLR-5813 | corrected | merge-base link; receipt base-run count; follow-up offer |
| SOLR-17292 | corrected | merge-base link; Proof dated 2026-10-10 per the receipt, at the post-remedy head |
| SOLR-12991 | corrected | merge-base link; receipt base-run outcome in Proof |
| SOLR-15035 | corrected | Proof reworded to the receipt outcome; test link to the method's lines |
| SOLR-15863 | corrected | Proof names the method; head, counts, date and citations checked |
| SOLR-12651 | corrected, **held** | the receipt now records a live-tip green (2026-10-10); the hold stays until the lead decides |
| SOLR-13186 | corrected | pre-change citation moved to the merge-base |
| SOLR-15106 | corrected | test link to the method's end; base premise gap flagged, wording unchanged |
| SOLR-9155 | corrected | pre-change citation moved to the merge-base; Proof premise qualified per the receipt |
| SOLR-15386 | checked, no edit | already conforms; six citations verified at the gated head |
| SOLR-11288 | corrected | symptom links relinked to the merge-base; DISCUSS (blank-only) posed, not settled |
| SOLR-13369 | corrected | process words removed; otherwise verified |
| SOLR-14919 | corrected, **held** | symptom links relinked; opening held on the DISCUSS item (RecoveryStrategy marker) |
| SOLR-17680 | corrected | symptom link to the merge-base; field-check paragraph fixed; two unsupported error-text claims replaced or removed |
| SOLR-17733 | corrected | "What happens today" links moved to the merge-base, with one sentence saying so |
| SOLR-16437 | corrected | Proof carries the receipt counts; base gives one failure; pre-change citations moved |
| SOLR-13239 | not drafted | submission-held; gated green at the live tip |
| SOLR-18277 | not drafted | merged; retire item (DISCUSS on branch deletion) |
| SOLR-16013 | not drafted | held (DISCUSS on the history rewrite) |
| SOLR-11479 | not drafted | audit only; branch corrections owed before the first gate |
| SOLR-3865 | not drafted | audit only, not ready (DISCUSS item 8) |
| SOLR-4754 | not drafted | audit only, not ready (DISCUSS item 9) |
| SOLR-10234 | not drafted | audit only, plausible (DISCUSS item 10) |
| SOLR-10641 | not drafted | audit only, no draft (DISCUSS item 11) |
| SOLR-17281 | not drafted | parked, not ready (DISCUSS: revert or replace) |
| SOLR-7394 | not drafted | live PR, consistency only; DISCUSS 12 held |
| SOLR-12998 | not drafted | live PR; the receipt records a green gate at its head |
| SOLR-13136 | not drafted | live PR; DISCUSS on behavior, held |
| SOLR-18391 | not drafted | two live PRs; DISCUSS on closing the submit branch |

Newly written: none. Every ticket with a draft was corrected in place, and every ticket without one has a stated reason.

## Holds

1. **SOLR-12651.** The hold comment is an HTML comment at the top of the file. It says the hold was for a run at the live tip, which is now recorded (2026-10-10). The branch changelog title still overstates the cleanup, and its replacement commit moves the head. The lead decides whether to lift the hold. Remove the comment before posting.
2. **SOLR-14919.** The opening stays held on the DISCUSS item (the RecoveryStrategy marker). The draft keeps its full text, and the hold is recorded here and in the part report, not in the draft.
3. **SOLR-13136, SOLR-18391 (submit), SOLR-17281, SOLR-7394.** Each is held on a DISCUSS item in the answers. The owner's calls decide them.

## Gaps for the lead

- **Branch corrections owed** (not draft edits; each one moves a head): SOLR-12651 changelog title; SOLR-15863 changelog title, which starts in lower case; SOLR-11288 changelog title, which omits the blank-only change; SOLR-14919 test comment at `IgnoreCommitOptimizeUpdateProcessorFactoryTest` lines 63 to 66, which misstates the hop; SOLR-11479 changelog title and the several-replica rejection test; SOLR-16013 changelog title with the history rewrite.
- **Head discrepancy.** SOLR-16013 is gated at `e1bd21fd11a`, while the live tip is `ba26b702891`, so there is no honest Proof at the tip.
- **Answers out of date.** The answers' gate lines for SOLR-12998 and SOLR-16437 are stale: the receipts record green gates that the answers call owed. The receipts win in both cases. The SOLR-17292 hold the answers describe is superseded by the receipt at the fork tip (`f614a42fbc8`); the lead should confirm that the hold is lifted before opening.
- **SOLR-15035.** The ticket summary was not verified: no Jira packet is on disk. The answers say to confirm before opening.
- **SOLR-17680.** The category routed alias without `router.field` was not traced. The owner should confirm the error-text bullets before opening.
- **SOLR-15106.** The test calls `getCollectionProcessorThread()`, which the change adds. A base run therefore needs a premise tree that carries it, and the receipt does not record one.
- **SOLR-18391 graceful-create.** The answers' commit `3a0ff1262bf` is not in the receipt; the receipt gates `adcda10b501`. The base run has two failures in `CreateCollectionCleanupTest`, so "exactly one failure" does not hold.
- **SOLR-18391 submit.** The receipt does have a 2026-10-04 verification record at `3000eeede7a`, which the answers call absent.
- **Length.** Drafts run from about 3,100 to 4,900 characters with link URLs, and about 2,300 to 3,500 without them. SOLR-15674 and SOLR-17292 are the longest (about 4.9 KB). Neither was trimmed.
- **Symptom wording.** The SOLR-15386 symptom wording was taken from the draft and is not verified against the ticket packet. The SOLR-15386 mixed-version Limits line is not verified.

## Not done

- No build, test, Gradle or gate run. No PR, comment, review or Jira write.
- Ticket text was not re-read from Jira. Merge-bases came from the local `upstream/main`.
- Receipt-named gate logs and JUnit XML are not on disk. Counts are the receipts' recorded numbers.
- Live PR titles and bodies were not re-read. The approving review on SOLR-18391's graceful-create PR was not read.

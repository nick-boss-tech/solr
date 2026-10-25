# Work ledger, 2026-10-24

Compiled by the phone agent at the owner's direction ("keep track of what we worked on"). Pointers are board paths unless a URL is given. Everything posted publicly went out on the owner's explicit go.

## Posted publicly

- PR #5211: the owner's review comment posted by vm6 (19:08 UTC); text of record `drafts/pr-5211-review-posted-round-1.md`.
- PR #5222 opened (SOLR-18119 RAT exclusion); reply to Smiley posted 19:24 UTC; tarball follow-up posted 2026-10-25T00:21Z, https://github.com/apache/solr/pull/5222#issuecomment-6391351733
- Jira: SOLR-18586 and SOLR-18587 filed (RC3 repeating failures); SOLR-18588 filed and closed the same day as a duplicate of SOLR-18564, with the duplicate link and both comments, after the owner caught the overlap.
- PR #5098 and PR #5106 descriptions edited: RC3 section naming the five smoke-loop items, later updated with the SOLR-18586, SOLR-18587, and SOLR-18564 links.

## Gates, checks, and reports landed

- SOLR-18295 gate (vm6): head `b273b245115b` green, 20 of 20 runs; baseline 4 of 20 with a different failure mode than the ticket's premise, recorded plainly in `receipts/SOLR-18295.md`.
- vm5 release-tarball check for PR #5222 plus addendum: dev-tools export-ignore verdict (no release impact); exposed the pre-existing `:allHelpFilesExit` gap against export-ignored `dev-docs`.
- vm5 RC3 smoke loop final report (5 completed runs, all BUILD FAILED) and the Smiley failure comparison.
- RC3 root-cause unit 1 (TestTlogReplica) reproduction report landed (commit `34c9c35151d`); unread at ledger time.
- cuvs-lucene: two independent green CPU gates (vm2), rounds 2 and 3 receipts.
- SOLR-18552 9.x gate receipt at the top of `receipts/SOLR-18552.md`.
- Misc benchmark (vm6): authorization vs cache-validation vs response-generation medians delivered; the report was also sent to the owner directly at his request.

## Drafts prepared, awaiting the owner's go

- The slate of five: SOLR-18549 staged ask, SOLR-18295 round-2 note (rewritten with gate results), SOLR-18556 round-2 note, SOLR-17861 closing note, SOLR-17988 comment edit (applicable by API).
- Eight UI staged asks (17849, 17850, 17887, 17909, 17911, 17913, 17914, 17660).
- Status notes: SOLR-17976, SOLR-10928. Eric Pugh offers: SOLR-18546, SOLR-17931.
- Backlog EOL ping batch 1 (10 verified texts); comment family examples, 6 in round 1 and 3 spot-checked in round 2.
- RC3 vote follow-up email draft (`drafts/rc3-fix-set-email-draft.md`); the owner sends it himself.

## Stores and assignments stocked

- Comment caps and streams settled (`assignments/scan-streams-and-comment-cap-round-1.md`): 100 posted per day cap, active sub-cap 10, no per-opener cap, drafts uncapped.
- Harmonized backlog INDEX built (1,526 keys, 16 batches); H1 batches 1 to 4 queued as claimable units with QA before drafting.
- EOL ping continuation claimed through batch 5. Warm-ticket evidence units rounds 1 and 2 (eight tickets). Warm-and-bench units: SOLR-18571, 18473, 18369, 18460.
- BATS follow-up to PR #5225 (accurate allowPaths assertion) assigned; a gate job for it was created by a host.
- Gate jobs posted: PR #5106 final head, SOLR-10928 live-head re-gate, PR #5192 exact head.
- Critical coverage store: census of 86 open priority-Critical tickets in 9 batches, comment-coverage assignment, implementation-triage assignment; drafting routed to phone-agent-2 with closure-ready drafts first, plus a locator addendum after a visibility check.
- Scouting: tarball help-files premise run; day-scan closeout for 2026-10-24.
- cuvs round-4 set (nine assignments) plus the owner's sequencing decision recorded (`reports/cuvs-lucene-round-4-decision-round-1.md`).

## Corrections recorded today

- Jira writes via the API work in full (creation, comments, links, transitions); the older partial-access record is superseded. Per-text approval still governs.
- SOLR-18564 already has its PR: apache/solr#5192, open and ready since 2026-10-22, head `dd65b21b41dc` with the changelog commit on top of the gated fix. Earlier ledger entries saying no PR existed were wrong; the exact-head gate job above is the remaining evidence step.
- SOLR-18588 was a duplicate of the owner's own SOLR-18564; a pre-filing self-search rule was added to the agent's standing instructions.

## Owner approvals outstanding at ledger time

- The postable draft pile (above). The RC3 tickets discussion (TestTaskManagement one-off; SOLR-9433 overlap on 18587; whether 18586/18587 stand). The vote email send, before Tuesday 2026-10-27 11:00 UTC. The family-examples verdict. The bulk-notice category 2 and its post date. The PR-stream accounting question.

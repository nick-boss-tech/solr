# Assignment: two backlog-pack decisions for the phone agent (round 1)

Status: DONE. Posted by vm4, 2026-10-26, at Nick Shanin's direction. For phone-agent. Tags: records. Nothing here posts anything.

## Why

vm4 worked Unit 4 of the backlog first-100 pack (`assignments/backlog-first-100-pack-slices-round-1.md`) and found two questions it could not settle with the decision ladder (`material/decision-ladder.md`). Both sit with the store you built (`material/harmonized-backlog/`). Report: `reports/backlog-first-100-unit-4-round-1.md`; revision: `reports/backlog-first-100-unit-4-round-1-revision-1.md`.

## Question 1: the conflict flags on Bug records in the closure families

Every closure-not-a-bug record (127), 77 closure-fixed, 67 closure-obsolete and 7 closure-duplicate records carry `bug in a closure disposition: barred from closure families; flagged for severity screen` in rev4. Under the pack's rule 2, conflict-flagged records are excluded, so these flags block the pack.

The flag's first clause is the round-1 "no Bugs" setting that the owner superseded on 2026-10-24 (`assignments/backlog-first-100-pack-round-1.md`). The second clause, "flagged for severity screen", is not explained anywhere on the board. Nothing in the pack file says a severity screen is a prerequisite.

What the ladder gives:
- Rung 3 (the owner's written intent) settles that the Bug barrier is gone for this pack.
- Rung 3 does not settle whether a severity screen is still required before a Bug closure. That is a rung-4 choice, or possibly an owner question.

Please decide:
1. Is the severity screen a prerequisite for pack inclusion? Cite where it is defined.
2. If it is not, write a new revision (rev5) of the affected batch records that clears only the Bug-barrier clause, and keeps the other flags: `verify-first` with no crosswalk row (2 closure-not-a-bug, 1 closure-duplicate), the Critical flag (1 closure-fixed, 1 closure-obsolete), and the `fixing version citation to be checked in phase H2 QA` note (77 closure-fixed). Keep rev4 unchanged. Record the rung used.
3. If it is a genuine owner call, set this item to NEEDS-OWNER with that one question, as the ladder requires.

Do not clear flags by editing rev4 in place. Records are add-only.

## Question 2: duplicate targets for the closure-duplicate family

Eight closure-duplicate records (SOLR-3765, SOLR-5772, SOLR-7721, SOLR-9089, SOLR-11034, SOLR-11787, SOLR-13167, SOLR-13470) name no surviving ticket. The template needs one, and the target has to be open or closed-as-fixed and checked. The skip-queue file the records cite (`queue-current.json`) is not on `coordination-ui`.

Evidence already on the board: the draft for SOLR-11787 on branch `solr-11787-draft-close` names SOLR-6213 as its target and waits on Nick's decision (`jira/COMMENTS.md`). That is one key, not a list.

Please:
1. Say where the skip-queue file is, or add it to the board. Name the archive path if it is on the archived board.
2. Give the surviving ticket for each of the eight keys, with the citation. Where the source is a live Jira link, say so.

vm4 can check the live status of each target once the names are known.

## What vm4 has settled, for your review

Settled inside the pool with the ladder, so you do not need to decide them:

- **Ping-stale wording** (rung 1). The template's sentence "found no steps that could be tested" is false for most of the 11 ping-stale tickets; the ticket text shows reproducible scenarios. The replacement sentence is accurate because no comment on those tickets records a confirmed reproduction on a supported release. Eleven drafts are in `jira/jira-comment-<KEY>-draft.md`, not posted. The family file `drafts/jira-comments/ping-stale.md` still has the old sentence. If you accept the change, update it there.
- **Metadata source** (rung 4). Reporter and last-updated come from live Jira, read at fill time, because the metadata CSV is not on the board. Both are the same Jira fields the CSV was built from.

If you disagree with either, reply on the board and vm4 will revise. Nothing is posted and no PR or Jira write is authorized by this assignment.

## Deliverable

`reports/backlog-first-100-decisions-round-1.md`: for each question, the rung used, the decision, and the citations. Mark this assignment DONE in the same push.

Completed 2026-10-25: decisions and citations in `reports/backlog-first-100-decisions-round-1.md`; rev5 batches posted under `material/harmonized-backlog/batches/`; ping-stale family file updated in the same batch.

# SOLR-18444 closeout record

Verdict: keep-open (draft branch `solr-18444-draft-open`).

- Pinned head: fork `origin/main` `498a09aea97aac86c2cc7ac114113b946d249942` (2026-10-19 fetch). Upstream history is not cited.
- JIRA checked 2026-10-19: status Open, no resolution, no fix versions, updated 2026-09-22. Description is 525 characters and was read in full. Two comments (2026-09-13 and 2026-09-22, both by dsmiley) were read in full; the closeout evidence skill cut them at 240 characters before this record, so the earlier attempt missed the second comment's NRT and leader-only query points.
- Audit verdict on record: `confirmed-skip` (audit-1, 2026-10-04): "Feature proposal (coordinated autoCommit), not a bug."

## Code paths read at the pinned head

- `solr/core/src/java/org/apache/solr/update/CommitTracker.java:136-165`: `_scheduleCommitWithin` schedules the next autoCommit with `scheduler.schedule(this, commitMaxTime, ...)` (line 165). The delay is measured from the moment the update path asks for a commit, not from a shared clock. Two cores that receive their first update at different times therefore commit at different phases, which is the mechanism the ticket describes.
- `CommitTracker.java:78-97`: the tracker is constructed per core. No code aligns the phase across cores or shards.

## Reproduction

None attempted. The ticket asks for a behavior change (aligned commit times), not a defect report. A reproduction would show that phases differ, which the code path already shows, and would not decide whether the project wants aligned commits.

## What the verdict does not cover

- Whether aligned autoCommit is a wanted feature. That is a design decision for the project and has no evidence on the ticket beyond the reporter and one maintainer comment.
- Whether the proposed epoch anchor interacts with the NRT and TLOG replica types the second comment raises. Not checked.

## What discharges keep-open

A project decision that the proposal is wanted, or a decision to close it as not planned. Either is the owner's call. The rubric has no closeout verdict for a declined feature request, so the owner may want to add one.

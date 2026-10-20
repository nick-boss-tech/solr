# Skip record: SOLR-10751

- Ticket: SOLR-10751, Master/Slave IndexVersion conflict. JIRA read in this pass: Open, resolution none, fix versions none, 6 comments (2017-05-25 to 2019-02-22); updated 2019-10-25.
- Verdict: keep-open (unchanged status; this record replaces the earlier record on this branch, whose cross-audit reading named missing items J, C and L).
- Draft branch: solr-10751-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: after a master's index is deleted and re-created, a slave can see the same version number as the master but a different generation (two commits in the same millisecond get the same version). The reporter proposes also comparing generations, and the 2017 comments explain that version 0 means "no replicable index" and that a forced replication is the only path that deletes a follower's index. The 2019 comments discuss a change for PULL and TLOG replicas, which the author wrote as a work-in-progress PR.

## Code reading

- solr/core/src/java/org/apache/solr/handler/IndexFetcher.java:528-545: a leader with version 0 and generation 0 is "not open for replication" (LEADER_IS_NOT_ACTIVE); a leader with version 0 and a follower commit that has a commit timestamp causes the follower to delete its index ("New index in Leader. Deleting mine..."). This branch applies to all replica types; there is no PULL or TLOG condition at the pin.
- IndexFetcher.java:585-590: the full copy is decided when the follower commit timestamp is at or past the leader version, or the follower generation is at or past the leader generation, or the fetch is forced. The generation comparison is the check the reporter proposes; it was added by de381d2816 (SOLR-6804, 2015), which predates this ticket.
- The version comparison that decides whether to fetch at all is not re-traced line by line in this pass; the same-version case from the ticket is not shown to be handled by a fix commit.

So the generation-aware full-copy decision exists at the pin, but the ticket's same-version case is not tied to any fix, and the replica-type change discussed in 2019 is not in the pinned code.

## History

- No commit at the pin names SOLR-10751 (git log --grep at the pin: none).
- de381d2816 (SOLR-6804, 2015): the generation condition, predates the ticket.
- The 2019 PR for PULL and TLOG version-0 handling is not identified by a commit at the pin.

## Reproduction attempts

Not run. A reproduction needs a master and slave with the delete-and-recreate sequence from the ticket, a commit in the same millisecond on each, and a check whether the slave keeps a different generation with the same version. Not built or run in this pass.

## Gap analysis

Two pieces are missing: a fix commit for the same-version case (the generation check that exists predates the ticket and is not shown to cover this), and the replica-type-specific version-0 change discussed in 2019 (not present at the pin). Whether the same-version divergence still occurs needs a run.

## Verdict justification

keep-open: no fix commit names or covers the ticket, the 2019 change is not in the pin, and the divergence has not been reproduced. close-fixed and close-obsolete are not supported; close-cannot-reproduce needs a run attempt, and none was made.

Missing for a closing verdict: a replication run at the pin with the ticket's delete-and-recreate sequence and same-millisecond commits, recording whether master and slave stay on different generations with the same version; and a decision on the PULL and TLOG version-0 change.

## Limits

This record does not run replication, does not trace the version comparison line by line, and does not post anything to JIRA or GitHub.

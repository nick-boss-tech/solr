# Skip record: SOLR-11815

- Ticket: SOLR-11815, "TLOG leaders going down and rejoining as a replica do fullCopy when not needed". JIRA status read 2026-10-20T07:33:10Z (UTC): Open, resolution None, updated 2019-12-02. Linked: SOLR-11920 (Closed), which the last comment asks about.
- Verdict: close-fixed. This replaces the recorded keep-open: the fix that the last comment asks about is on main and is an ancestor of the pinned head.
- Draft branch: solr-11815-draft-close.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T07:33:10Z (UTC).

## Ticket reading

After a TLOG leader restarts and rejoins as a replica, the replica runs a full copy of the index even when the difference is small. The reporter calls the full copy unnecessary, because it moves a large index. The first comments trace the cause to `isIndexStale`, which forces a full copy when a file's size or checksum differs. The last comment (2019-12-02) asks whether SOLR-11920 fixed it.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/handler/IndexFetcher.java:617`: `if (!isFullCopyNeeded && isIndexStale(indexDir)) { isFullCopyNeeded = true; }`. The stale check still sets the full-copy flag.
- `IndexFetcher.java:1132-1164`: `doDifferentialCopy` is applied during the copy. A file that matches the leader's name and checksum is reused, not downloaded again, so the bytes moved are only the differing files. This is the change SOLR-11920 added.
- `IndexFetcher.java:1306`: `isIndexStale` is still present.

## History

- `fbdf7fa736` (2018-03-15), SOLR-11920: "Differential file copy for IndexFetcher". Ancestor of the pinned head (checked with `git merge-base --is-ancestor`). Changed `IndexFetcher.java` (52 lines) and added the CHANGES entry.
- `72ee0f64d4` (2018-04-17), SOLR-11920: "Adding a file exists check before skipping a file for replication". Ancestor of the pinned head.
- evidence.py at the pinned head: no commits naming SOLR-11815 directly.

## Reproduction attempts

Not run. The ticket's repro needs a leader kill, a hard commit, and a pull, which the chaos tests in the repo can approximate. The verdict does not rest on a run: it rests on the fix commit and the code that it leaves in place.

## Gap analysis

The harm in the ticket is the bytes transferred and the disk contention on a large index. The differential copy addresses the bytes: matching files are reused. The full-copy flag is still set by `isIndexStale`, so the name "fullCopy" still appears in the log. Whether that flag should be removed is the design question in the ticket's comments, and it is not a bug in the bytes moved.

## Verdict justification

close-fixed meets the rubric: the fix commit is named (fbdf7fa736, with 72ee0f64d4), it is an ancestor of the pinned head, and the changed code (doDifferentialCopy at IndexFetcher.java:1132-1164) is still present. Requirement not fully met: the full-copy flag itself stays, so a reader who expects no full-copy log line would not see it removed. The limit is stated below.

## Limits

This record does not show the disk-space case is solved, since a differential copy still uses temporary space. It does not remove the isIndexStale check. If the owner wants the flag removed, that is a new ticket.

# SOLR-17920 closeout record

Verdict: keep-open (draft branch `solr-17920-draft-open`).

- Pinned head: fork `origin/main` `498a09aea97aac86c2cc7ac114113b946d249942`. Upstream history is not cited.
- JIRA checked 2026-10-19: status Open, no resolution, no fix versions, created and updated 2025-09-16. Description read in full (about 600 characters, with `df` output before and after `systemctl stop solr`). No comments.
- Audit verdict on record: `confirmed-skip` (audit-1, 2026-10-04): disk space held until stop, no deleted-file evidence, no code pointer, zero comments; an ops report.

## Code paths and references read at the pinned head

- `solr/core/src/java/org/apache/solr/core/SolrDeletionPolicy.java`: the Lucene deletion policy that decides when old commit points are removed. Files from old commits stay on disk until the policy releases them.
- `solr/core/src/java/org/apache/solr/core/SolrCore.java:865, 1235, 1800-1802`: the IndexWriter and core-state reference counts that a running core holds. A core that still holds an old writer or searcher keeps the files it reads open, so deleted segment files remain allocated until those references close. This is the mechanism that would explain space freed on stop; it is not confirmed for this report.
- `solr/solr-ref-guide`: searched for disk space, deleted files, pending deletion and file handles. No page explains the behavior, so the closeout-answered verdict does not apply.

## Attempts

- Read the ticket's `df` output: about 150 GB is released when the service stops, which fits space held by deleted files.
- No reproduction. The ticket's own check (`lsof | grep deleted`) found nothing, and the reporter did not say whether it was run while the disk was full or on which process.

## What the verdict does not cover

- Whether the held space is deleted index files, logs, or something else.
- Whether this happens on current main. The reporter's Solr version is not on the ticket.

## What discharges keep-open

`lsof -p <solr pid>` (or `ls -l /proc/<pid>/fd | grep deleted`) captured while usage is high, the Solr version, and the commit and merge activity on that node around the time. With those, the held files can be identified and matched to a deletion-policy or reader-reference cause, or the ticket can be closed as expected behavior with a link.

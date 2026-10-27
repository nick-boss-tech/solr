# SOLR-18369 sub-task census, round 1

Ticket: SOLR-18369, "Remove deprecated code from Solr 11" (Open, Minor, reporter Eric Pugh, created 2026-08-18). The umbrella ticket for removals targeting Solr 11, a follow-up to SOLR-17931. Its work is in sub-tasks. This replaces the BulkV2 audit that unit 3 first described; see `assignments/eric-stream-stock-round-1-addendum.md`.
Pinned main: `e59ce3c97ddb` (apache/solr main, committed 2026-10-26 19:44 -0400).
Source of truth for sub-task state: the ASF Jira REST API, read 2026-10-27, and the apache/solr GitHub pull request API. Coverage is judged from the main tree at the pinned SHA, not from PR titles.

## Sub-tasks

The parent lists 23 sub-tasks. Four are not Resolved in Jira. The 19 Resolved sub-tasks are not in this census; the parent's own status is Open.

| Sub-task | Jira status | Summary | Covered by | Coverage at pinned main |
|---|---|---|---|---|
| SOLR-18374 | Open | Remove NamedList.NamedListEntry and MapWriterMap | PR #4763, merged 2026-08-23 | Partly covered. `NamedListEntry` is gone. `MapWriterMap` still exists at `solr/solrj/src/java/org/apache/solr/common/MapWriterMap.java:25`, now package-private and scoped, not removed. The remaining removal is uncovered. |
| SOLR-18379 | Open | Remove DocsStreamer.KNOWN_TYPES | PR #4758, open | Covered by an open PR. `KNOWN_TYPES` is still at `solr/core/src/java/org/apache/solr/response/DocsStreamer.java:85`. |
| SOLR-18387 | Open | Remove ZkStateReader.getCollectionPathRoot/getCollectionPath | PR #4767 under SOLR-18388, merged 2026-08-20 | Covered by merged work under a sibling. `ZkStateReader` has no such methods at the pinned SHA. The same-named statics on `DocCollection` remain (`DocCollection.java:190`, `:194`) and are called widely; their removal is not in this sub-task's title as written, so a Jira clarification is needed to say whether it was meant. Jira's sub-task 18388 has the same summary and is Resolved. |
| SOLR-18358 | Open | Remove CollectionAdminRequest.Backup.setIncremental(boolean) | PR #4808, merged 2026-09-09 | Covered by merged work. The string `setIncremental(` has zero hits at the pinned SHA. Jira is stale: the sub-task is still Open. |

## Uncovered

- The remainder of SOLR-18374: `MapWriterMap` is scoped to its package but not removed. Whether the ticket wants the full removal depends on whether a package-private `MapWriterMap` still counts as deprecated; the ticket text is "Remove".
- The `DocCollection` static pair in SOLR-18387, if the ticket meant those. Its title names `ZkStateReader`, so this is a question for the ticket owner, not a finding.

## Stale Jira state

- SOLR-18374, SOLR-18387, and SOLR-18358 are Open in Jira but their code is merged on main. SOLR-18374 is partly done. The other two are covered, so their status is stale.

## Notes for selection

- SOLR-18379 needs review of open PR #4758, not new code.
- Nothing in this census is a draft. The addendum asks that any draft for an uncovered sub-task be a separate unit after Nick picks.

## Reproduce

`curl -s https://issues.apache.org/jira/rest/api/2/issue/SOLR-18369?fields=subtasks` (sub-task statuses)
`curl -s https://api.github.com/repos/apache/solr/pulls/4763` (and 4758, 4808, 4767 for `state`, `merged_at`)
`git grep -n -F 'setIncremental(' e59ce3c97d -- solr` (no hits)
`git grep -n 'class MapWriterMap' e59ce3c97d -- solr`
`git grep -n 'KNOWN_TYPES' e59ce3c97d -- solr/core/src/java/org/apache/solr/response/DocsStreamer.java`
`git grep -n 'getCollectionPath(' e59ce3c97d -- solr/solrj/src/java/org/apache/solr/common/cloud/DocCollection.java`

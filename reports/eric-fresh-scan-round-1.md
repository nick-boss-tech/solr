# Fresh Eric scan, round 1

Scope: SOLR tickets Eric Pugh reported or commented on, updated since 2026-10-24, excluding the tickets already named in the stream assignment (SOLR-18553, SOLR-18449, SOLR-18369, SOLR-18460) and the five existing drafts (SOLR-18549, SOLR-18546, SOLR-17931, SOLR-18439, SOLR-18500).
Pinned main: `e59ce3c97ddb` (apache/solr main, 2026-10-26 19:44 -0400).
Population: Jira search, 2026-10-27, `project = SOLR AND (reporter = epugh OR assignee = epugh OR comment ~ "epugh") AND updated >= "2026-10-24"`, returned 5 issues. Jira does not allow `commentedBy` anonymously, so "commented on" is approximated by the comment text match. That match also catches mentions, so each candidate below is checked for whether Eric wrote the comment.

## Summary

| Ticket | Relation to Eric | Result |
|---|---|---|
| SOLR-18351 Remove ContentStream | Reporter; commented 2026-08-21 | Draft: `jira/jira-comment-SOLR-18351-draft.md` |
| SOLR-17953 .DS_store breaks localfilesystem restore | Reporter; commented 2026-10-25 | No draft: resolved, fix on main |
| SOLR-17988 Solr 10 property names | Assignee and reporter | No draft: board already covers it (ST1 to ST5) |
| SOLR-18234 core-cli build module | Eric mentioned by others, did not comment | Excluded: not in scope by the rule |
| SOLR-18500 curl quoting | Existing draft | Excluded by the stream rule |

Three tickets were taken for a trace or a verdict; one produced a draft. The population is small, so fewer than three is the honest result.

## SOLR-18351, Remove ContentStream in favour of ContentWriter

Facts at the pinned SHA:
- The gap Eric and Serhiy raised on 2026-08-21 (a `ContentWriter` writes one stream, so multi-part bodies cannot migrate) was addressed by `78862e7f54`, "SOLR-18351: remove ContentStream from SolrJ's client write path (#4811)", merged 2026-09-02. It added `RequestWriter.MultipartContentWriter` with named parts (`solr/solrj/src/java/org/apache/solr/client/solrj/request/RequestWriter.java:47`).
- `ContentStream` is still declared `@Deprecated(since = "10.0")` at `solr/solrj/src/java/org/apache/solr/common/util/ContentStream.java:28`.
- SolrJ main code (outside the two `ContentStream` definition files) has zero references.
- Remaining main references: 34 files. Core 29 files; `solr/modules/cross-dc` 3 files; `solr/modules/extraction` 1; `solr/modules/scripting` 1. The core count includes `solr/core/src/java/org/apache/solr/client/solrj/embedded/EmbeddedSolrServer.java:310`, which builds `List<ContentStream>` for request parts.
- Test references: 14 files.
- Eric's 2026-08-21 figure ("17 places could migrate, about 50 could not") predates #4811 and should not be quoted as current.

Open point: the ticket says "Remove". Removing the type needs the 34 main sites moved. The SolrJ client path is done.

Verdict: concrete. Draft asks Eric whether the remaining list is what he expected for the ticket, and recommends splitting the core handlers into their own sub-task.

## SOLR-17953, .DS_store breaks localfilesystem restore

Resolved. The fix is on main as `b73f03976f`, "SOLR-17953: Ignore hidden files when locating an incremental backup (#5066)", 2026-10-22. Backports merged: `d9f86bd17e` on branch_10x (#5197) and `dae5ac66f9` on branch_9x (#5198), both 2026-10-25. Eric's last comment is thanks. No open point.

Verdict: no draft. Nothing to ask.

## SOLR-17988, Ensure Solr 10 release conveys property changes

Assignee and reporter is Eric. Nick's 2026-10-24 status comment says the property transitions are covered by the Solr 10 upgrade notes. The board already has the sub-task units (`claims/solr-17988-subtasks-round-1-ST1.md` through ST5, all DONE), so this is not new work.

Verdict: no draft; covered by board records.

## SOLR-18234, core-cli build module (excluded)

Jira's comment match returned this ticket because jaykay12 wrote `[~epugh]` in two comments (2026-07-25 and 2026-10-16). Eric did not comment, so it is outside the rule. Recorded for the owner: `org.apache.solr.cli` (76 files) has no importers in other core main code or test-framework after #5018 (`8e62c26886`). Its own imports reach back into core in about 35 lines. Excluded from the scan; not drafted.

## Not drafted or excluded

- SOLR-18500: existing draft, excluded by the stream rule.

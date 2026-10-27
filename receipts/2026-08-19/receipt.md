# Jira day scan receipt: 2026-08-19 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-08-20. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-08-19%22+AND+updated+%3C+%222026-08-20%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-08-19" AND updated < "2026-08-20"`. One search page returned 2 SOLR tickets updated on 2026-08-19 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; both ticket histories were fetched serially.
- Scope totals: 2 tickets, 1 comment across their complete histories, 1 commenter account, and 1 fresh watch-list comment by Eric Pugh. No `nickshanin` account comments appear; the full comment history is retained in the totals.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| Eric Pugh | `epugh` | 1 | 1 | Commenter account |
## Fresh watch-list comment
| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| Eric Pugh | [SOLR-18369](https://issues.apache.org/jira/browse/SOLR-18369?focusedCommentId=18105912), `2026-08-19T13:03:40.383+0000` | “We got the first green ‘RESOLVED’ in the subtask list!” |
## Candidate work from fresh comments
Candidate only — not claimed: offer help with one of the remaining deprecated-code cleanup subtasks under [SOLR-18369](https://issues.apache.org/jira/browse/SOLR-18369?focusedCommentId=18105912), asking Eric Pugh which should be next. His comment marks early progress, not completion of the parent; the current Jira page still shows the parent Open / Unresolved and several open subtasks, including [SOLR-18387](https://issues.apache.org/jira/browse/SOLR-18387) (“Remove ZkStateReader.getCollectionPathRoot/getCollectionPath”), which is unassigned. No task was claimed.
## Per-ticket dispositions
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18387](https://issues.apache.org/jira/browse/SOLR-18387) - Remove ZkStateReader.getCollectionPathRoot/getCollectionPath | `2026-08-19T18:09:15.000+0000` | 0 | None | Open in the current parent subtask list; no fresh watch-list comment on this ticket. |
| [SOLR-18369](https://issues.apache.org/jira/browse/SOLR-18369?focusedCommentId=18105912) - Remove deprecated code from Solr 11 | `2026-08-19T13:03:40.000+0000` | 1 | Eric Pugh: first subtask resolved | Open / Unresolved; ongoing cleanup with an optional candidate-only offer of help. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

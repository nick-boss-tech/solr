# Jira day scan receipt: 2026-08-28 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-08-29. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-08-28%22+AND+updated+%3C+%222026-08-29%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-08-28" AND updated < "2026-08-29"`. One search page returned 3 SOLR tickets updated on 2026-08-28 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; all 3 histories were fetched serially.
- Scope totals: 3 tickets, 8 comments across their complete histories, 3 commenter accounts, and 0 fresh watch-list comments. No `nickshanin` account comments appear; all commenter accounts and comments are included in the full-history totals.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 6 | 3 | Automation |
| David Smiley | `dsmiley` | 1 | 1 | Commenter account |
| Eric Pugh | `epugh` | 1 | 1 | Commenter account |
## Fresh watch-list comments
No fresh watch-list comments were returned.
## Candidate work from fresh comments
No candidate: no watch-list comments were created during this UTC day.
## Per-ticket dispositions
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18356](https://issues.apache.org/jira/browse/SOLR-18356) - Remove DocsStreamer 2-arg convertLuceneDocToSolrDoc overload | `2026-08-28T14:42:56.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18319](https://issues.apache.org/jira/browse/SOLR-18319) - Simplify 404 handling | `2026-08-28T14:36:13.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18294](https://issues.apache.org/jira/browse/SOLR-18294) - IndexFetcherPacketProtocolTest fails intermittently (~5%) | `2026-08-28T14:36:39.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

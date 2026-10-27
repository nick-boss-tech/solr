# Jira day scan receipt: 2026-08-31 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-01. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-08-31%22+AND+updated+%3C+%222026-09-01%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-08-31" AND updated < "2026-09-01"`. One search page returned 4 SOLR tickets updated on 2026-08-31 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; all 4 histories were fetched serially.
- Scope totals: 4 tickets, 10 comments across their complete histories, 5 commenter accounts, and 0 fresh watch-list comments. No `nickshanin` account comments appear; all commenter accounts and comments are included in the full-history totals.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 4 | 2 | Automation |
| David Smiley | `dsmiley` | 3 | 2 | Commenter account |
| Eric Pugh | `epugh` | 1 | 1 | Commenter account |
| Mike Drob | `mdrob` | 1 | 1 | Commenter account |
| Tomas Eduardo Fernandez Lobbe | `tflobbe` | 1 | 1 | Commenter account |
## Fresh watch-list comments
No fresh watch-list comments were returned.
## Candidate work from fresh comments
No candidate: no watch-list comments were created during this UTC day.
## Per-ticket dispositions
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18401](https://issues.apache.org/jira/browse/SOLR-18401) - HttpJettySolrClient: detect if request wasn't sent; means retry-able | `2026-08-31T02:41:44.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18295](https://issues.apache.org/jira/browse/SOLR-18295) - ReplicationFactorTest is flaky | `2026-08-31T00:19:37.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18111](https://issues.apache.org/jira/browse/SOLR-18111) - Remove org.apache.solr.legacy.Legacy* Field Types | `2026-08-31T17:38:06.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-15390](https://issues.apache.org/jira/browse/SOLR-15390) - Deprecate "segmentTerminateEarly" in favor of "minExactCount" | `2026-08-31T09:56:58.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

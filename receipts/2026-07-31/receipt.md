# Jira day scan receipt: 2026-07-31 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-08-01. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-07-31%22+AND+updated+%3C+%222026-08-01%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-07-31" AND updated < "2026-08-01"`. One search page returned 1 SOLR ticket updated on 2026-07-31 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/SOLR-15036/comment`, paginated at `maxResults=100`.
- Scope totals: 1 ticket, 48 comments across its complete history, 6 commenter accounts, and 0 fresh watch-list comments. No `nickshanin` account comments appear; all 48 comments remain in the full-history totals. The `jira-bot` account contributed 2 comments and is marked as automation.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| Joel Bernstein | `jbernste` | 23 | 1 | Person |
| Timothy Potter | `thelabdude` | 13 | 1 | Person |
| Michael Gibney | `magibney` | 8 | 1 | Person |
| ASF subversion and git services | `jira-bot` | 2 | 1 | Automation account |
| Atri Sharma | `atri` | 1 | 1 | Person |
| Jan Høydahl | `janhoy` | 1 | 1 | Person |
## Fresh watch-list comments
No fresh watch-list comments were returned. Jan Høydahl's older comment remains included in the full-history totals but was not created on this scan day.
## Candidate work from fresh comments
No candidate: there was no fresh watch-list activity.
## Per-ticket dispositions
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-15036](https://issues.apache.org/jira/browse/SOLR-15036) - Use plist automatically for executing a facet expression against a collection alias backed by multiple collections | `2026-07-31T18:44:40.000+0000` | 48 | None | No fresh watch-list comment; no candidate. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

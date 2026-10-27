# Jira day scan receipt: 2026-09-01 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-02. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-01%22+AND+updated+%3C+%222026-09-02%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-01" AND updated < "2026-09-02"`. One search page returned 3 SOLR tickets updated on 2026-09-01 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; all 3 histories were fetched serially.
- Scope totals: 3 tickets, 33 comments across their complete histories, 7 commenter accounts, and 0 fresh watch-list comments. No `nickshanin` account comments appear; all commenter accounts and comments are included in the full-history totals.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 11 | 3 | Automation |
| Dawid Weiss | `dweiss` | 8 | 1 | Commenter account |
| Jan Høydahl | `janhoy` | 7 | 1 | Commenter account |
| David Smiley | `dsmiley` | 3 | 2 | Commenter account |
| Chris M. Hostetter | `hossman` | 2 | 1 | Commenter account |
| Andrzej Bialecki | `ab` | 1 | 1 | Commenter account |
| Pierre Salagnac | `psalagnac` | 1 | 1 | Commenter account |
## Fresh watch-list comments
No fresh watch-list comments were returned.
## Candidate work from fresh comments
No candidate: no watch-list comments were created during this UTC day.
## Per-ticket dispositions
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18156](https://issues.apache.org/jira/browse/SOLR-18156) - Various problems using QueryLimits (aka: timeAllowed) in Solr 10.0.0 due to anonymous IndexSearcher | `2026-09-01T21:06:47.000+0000` | 8 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18062](https://issues.apache.org/jira/browse/SOLR-18062) - CrossDC - support arbitrary Kafka properties | `2026-09-01T04:15:12.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-15465](https://issues.apache.org/jira/browse/SOLR-15465) - Do not require LICENSE and NOTICE files for test-dependencies | `2026-09-01T14:05:32.000+0000` | 21 | None | No fresh watch-list comment; no candidate from this scan. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

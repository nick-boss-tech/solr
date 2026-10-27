# Jira day scan receipt: 2026-08-14 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-08-15. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-08-14%22+AND+updated+%3C+%222026-08-15%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-08-14" AND updated < "2026-08-15"`. One search page returned 1 SOLR ticket updated on 2026-08-14 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; the ticket history was fetched serially.
- Scope totals: 1 ticket, 2 comments across its complete history, 2 commenter accounts, and 0 fresh watch-list comments. No `nickshanin` comments appear; all full-history comments are included in the totals.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| David Smiley | `dsmiley` | 1 | 1 | Commenter account |
| Michael Braun | `mbraun688` | 1 | 1 | Commenter account |
## Fresh watch-list comments
No fresh watch-list comments were returned. David Smiley appears in the full-history commenter totals, but his comment was not created on the scan day and is not a fresh-comment highlight.
## Candidate work from fresh comments
No candidate: the scan returned no fresh watch-list comments.
## Per-ticket disposition
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-11213](https://issues.apache.org/jira/browse/SOLR-11213) - Update Jetty version to deal with CVE-2017-9735 | `2026-08-14T16:13:28.000+0000` | 2 | None from the watch list | No fresh watch-list comment; no candidate. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

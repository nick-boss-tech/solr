# Jira day scan receipt: 2026-08-20 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-08-21. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-08-20%22+AND+updated+%3C+%222026-08-21%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-08-20" AND updated < "2026-08-21"`. One search page returned 2 SOLR tickets updated on 2026-08-20 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; both ticket histories were fetched serially.
- Scope totals: 2 tickets, 23 comments across their complete histories, 6 commenter accounts, and 0 fresh watch-list comments. No `nickshanin` account comments appear; all commenter accounts and comments are included in full-history totals.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| Mark Robert Miller | `markrmiller` | 9 | 1 | Commenter account |
| Chris M. Hostetter | `hossman` | 4 | 1 | Commenter account |
| Serhiy Bzhezytskyy | `serhiybzhezytskyy` | 4 | 1 | Commenter account |
| ASF subversion and git services | `jira-bot` | 3 | 1 | Automation |
| David Smiley | `dsmiley` | 2 | 1 | Commenter account |
| Houston Putman | `houston` | 1 | 1 | Commenter account |
## Fresh watch-list comments
No fresh watch-list comments were returned.
## Candidate work from fresh comments
No candidate: there was no fresh watch-list activity on this day.
## Per-ticket dispositions
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18393](https://issues.apache.org/jira/browse/SOLR-18393) - Deprecate and remove Solr's “default request handler” concept | `2026-08-20T11:35:20.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17764](https://issues.apache.org/jira/browse/SOLR-17764) - “graceful” jetty shutdown causes ChaosMonkeySafeLeaderWithPullReplicasTest failures | `2026-08-20T18:03:57.000+0000` | 23 | None | No fresh watch-list comment; no candidate from this scan. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

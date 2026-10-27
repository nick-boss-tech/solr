# Jira day scan receipt: 2026-09-24 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-25. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-24%22+AND+updated+%3C+%222026-09-25%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-24" AND updated < "2026-09-25"`. One search page returned all 4 tickets; every `updated` timestamp falls on 2026-09-24 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 4 histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 4 SOLR tickets; 10 comments across their complete histories; 2 commenter accounts. There were no fresh watch-list comments on 2026-09-24. Nick Shanin did not appear in these histories.

## Commenter table

`jira-bot` is marked as automation, not a person.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| Andrzej Bialecki | `ab` | 5 | 3 | Commenter account |
| ASF subversion and git services | `jira-bot` | 5 | 2 | Automation |

## Fresh watch-list comments

None. The scanner found no watch-list comments created on 2026-09-24.

## Candidate work from fresh comments

No candidate: there was no fresh watch-list activity on this date.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. None had a fresh watch-list comment on this date.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18478](https://issues.apache.org/jira/browse/SOLR-18478) - Solr Ref Guide checkSiteLinks task throws NPE | `2026-09-24T15:14:46.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18439](https://issues.apache.org/jira/browse/SOLR-18439) - Some v2 "command" APIs report failures via HTTP 200 instead of a proper 4xx/5xx status | `2026-09-24T21:40:33.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18185](https://issues.apache.org/jira/browse/SOLR-18185) - Published POM have missing versions | `2026-09-24T12:38:23.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-16560](https://issues.apache.org/jira/browse/SOLR-16560) - Pull replication appears to consume excessive CPU as it does not use Segment Reader pooling | `2026-09-24T13:37:25.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

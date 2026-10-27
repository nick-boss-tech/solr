# Jira day scan receipt: 2026-09-16 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-17. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-16%22+AND+updated+%3C+%222026-09-17%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-16" AND updated < "2026-09-17"`. One search page returned all 3 tickets; each `updated` timestamp falls on 2026-09-16 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 3 histories were fetched serially.
- Scope totals: 3 SOLR tickets; 2 comments across their complete histories; 2 commenter accounts. There were no fresh watch-list comments on 2026-09-16. Nick Shanin did not appear in these histories.

## Commenter table

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| David Smiley | `dsmiley` | 1 | 1 | Commenter account |
| Eric Pugh | `epugh` | 1 | 1 | Commenter account |

## Fresh watch-list comments

None. The scanner found no watch-list comments created on 2026-09-16.

## Candidate work from fresh comments

No candidate: there was no fresh watch-list activity on this date.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. No ticket had a fresh watch-list comment on this date.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18454](https://issues.apache.org/jira/browse/SOLR-18454) - Remove deprecated /update/json request param json.command | `2026-09-16T21:01:01.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18453](https://issues.apache.org/jira/browse/SOLR-18453) - Deprecate /update/json request param json.command | `2026-09-16T21:01:10.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18367](https://issues.apache.org/jira/browse/SOLR-18367) - waitForFinalState removal | `2026-09-16T18:44:07.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

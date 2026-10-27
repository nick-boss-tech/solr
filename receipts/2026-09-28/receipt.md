# Jira day scan receipt: 2026-09-28 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-29. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-28%22+AND+updated+%3C+%222026-09-29%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-28" AND updated < "2026-09-29"`. One search page returned all 2 tickets; every `updated` timestamp falls on 2026-09-28 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. Both histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 2 SOLR tickets; 9 comments across their complete histories; 3 commenter accounts. One fresh watch-list comment was created that day, by David Smiley on SOLR-18395. Nick Shanin did not appear in these histories.

## Commenter table

`jira-bot` is marked as automation, not a person.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 4 | 1 | Automation |
| Chris M. Hostetter | `hossman` | 4 | 1 | Commenter account |
| David Smiley | `dsmiley` | 1 | 1 | Commenter account |

## Fresh watch-list comments

This comment was created on 2026-09-28 UTC.

| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| David Smiley | [SOLR-18395](https://issues.apache.org/jira/browse/SOLR-18395?focusedCommentId=18120329), `2026-09-28T19:17:21.551+0000` | “Thanks for being on top of this Hoss!” |

## Candidate work from fresh comments

No candidate. David Smiley’s “Thanks for being on top of this Hoss!” is an acknowledgment on the Lucene 10.5 upgrade ticket, not a request for follow-up work.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18486](https://issues.apache.org/jira/browse/SOLR-18486) - Complete Major changes and Upgrade Notes in RefGuide for 10.1.0 | `2026-09-28T19:11:17.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18395](https://issues.apache.org/jira/browse/SOLR-18395) - Upgrade to lucene 10.5 | `2026-09-28T19:17:21.000+0000` | 9 | David Smiley | David Smiley thanks Chris M. Hostetter; acknowledgment only, no candidate. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

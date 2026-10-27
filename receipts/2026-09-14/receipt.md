# Jira day scan receipt: 2026-09-14 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-15. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-14%22+AND+updated+%3C+%222026-09-15%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-14" AND updated < "2026-09-15"`. One search page returned all 4 tickets; each `updated` timestamp falls on 2026-09-14 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 4 histories were fetched serially.
- Scope totals: 4 SOLR tickets; 9 comments across their complete histories; 4 commenter accounts. There were no fresh watch-list comments on 2026-09-14. Nick Shanin did not appear in these histories.

## Commenter table

`jira-bot` is marked as automation, not a person.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 6 | 2 | Automation |
| Andrzej Bialecki | `ab` | 1 | 1 | Commenter account |
| David Smiley | `dsmiley` | 1 | 1 | Commenter account |
| Paul McArthur | `pmcarthur` | 1 | 1 | Commenter account |

## Fresh watch-list comments

None. The scanner found no watch-list comments created on 2026-09-14.

## Candidate work from fresh comments

No candidate: there was no fresh watch-list activity on this date.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. No ticket had a fresh watch-list comment on this date.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18443](https://issues.apache.org/jira/browse/SOLR-18443) - Prepare CLI tools separating arg parsing from tool execution | `2026-09-14T01:19:53.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18427](https://issues.apache.org/jira/browse/SOLR-18427) - Add v2 API equivalent for "List Replicas" | `2026-09-14T21:13:32.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18426](https://issues.apache.org/jira/browse/SOLR-18426) - Add v2 API equivalent for "List Shards" | `2026-09-14T20:54:18.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17894](https://issues.apache.org/jira/browse/SOLR-17894) - state.json compression isn't happening with Overseer disabled | `2026-09-14T01:20:09.000+0000` | 6 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

# Jira day scan receipt: 2026-09-19 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-20. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-19%22+AND+updated+%3C+%222026-09-20%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-19" AND updated < "2026-09-20"`. One search page returned both tickets; each `updated` timestamp falls on 2026-09-19 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. Both histories were fetched serially.
- Scope totals: 2 SOLR tickets; 27 comments across their complete histories; 4 commenter accounts. There were no fresh watch-list comments on 2026-09-19. Nick Shanin did not appear in these histories.

## Commenter table

`jira-bot` is marked as automation, not a person.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 21 | 2 | Automation |
| Eric Pugh | `epugh` | 4 | 1 | Commenter account |
| Utsav | `utsav00` | 1 | 1 | Commenter account |
| Vishnu Priya | `vchandsekar` | 1 | 1 | Commenter account |

## Fresh watch-list comments

None. The scanner found no watch-list comments created on 2026-09-19.

## Candidate work from fresh comments

No candidate: there was no fresh watch-list activity on this date.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. Neither ticket had a fresh watch-list comment on this date.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18366](https://issues.apache.org/jira/browse/SOLR-18366) - Remove AbstractFullDistribZkTestBase.getTotalReplicas | `2026-09-19T21:17:33.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17931](https://issues.apache.org/jira/browse/SOLR-17931) - Remove deprecated code from Solr 10 | `2026-09-19T21:17:00.000+0000` | 25 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

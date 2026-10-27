# Jira day scan receipt: 2026-09-23 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-24. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-23%22+AND+updated+%3C+%222026-09-24%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-23" AND updated < "2026-09-24"`. One search page returned all 5 tickets; every `updated` timestamp falls on 2026-09-23 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 5 histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 5 SOLR tickets; 15 comments across their complete histories; 6 commenter accounts. There were no fresh watch-list comments on 2026-09-23. Nick Shanin did not appear in these histories.

## Commenter table

`jira-bot` is marked as automation, not a person.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 9 | 4 | Automation |
| Chris M. Hostetter | `hossman` | 2 | 2 | Commenter account |
| Andrzej Bialecki | `ab` | 1 | 1 | Commenter account |
| Anshum Gupta | `anshum` | 1 | 1 | Commenter account |
| David Smiley | `dsmiley` | 1 | 1 | Commenter account |
| Jan Høydahl | `janhoy` | 1 | 1 | Commenter account |

## Fresh watch-list comments

None. The scanner found no watch-list comments created on 2026-09-23.

## Candidate work from fresh comments

No candidate: there was no fresh watch-list activity on this date.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. None had a fresh watch-list comment on this date.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18473](https://issues.apache.org/jira/browse/SOLR-18473) - CrossDC Consumer NPE on shutdown | `2026-09-23T14:01:53.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18464](https://issues.apache.org/jira/browse/SOLR-18464) - Fix up various antora linking errors | `2026-09-23T14:02:58.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18403](https://issues.apache.org/jira/browse/SOLR-18403) - bin/solr snapshot-export silently backs up the live index instead of the named snapshot | `2026-09-23T03:26:10.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18203](https://issues.apache.org/jira/browse/SOLR-18203) - Frequently failing ChaosMonkeySafeLeaderWithPullReplicasTest | `2026-09-23T16:42:55.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17067](https://issues.apache.org/jira/browse/SOLR-17067) - SolrCloudTestCase activeClusterShape() should only consider active shards | `2026-09-23T03:28:29.000+0000` | 6 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

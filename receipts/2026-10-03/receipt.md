# Jira day scan receipt: 2026-10-03 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-10-04. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-10-03%22+AND+updated+%3C+%222026-10-04%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-10-03" AND updated < "2026-10-04"`. One search page returned both tickets; all returned `updated` timestamps fall on 2026-10-03 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. Both histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 2 SOLR tickets; 13 comments across their complete histories; 8 commenter accounts. Two fresh watch-list comments were authored by David Smiley. Nick Shanin’s two historical comments across two tickets remain in the aggregate only and are excluded from candidate highlights.

## Commenter table

Counts cover all comments in the histories of the two tickets. `jira-bot` is marked as automation, not a person. Nick Shanin is labeled self and included for aggregate accounting only.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| David Smiley | `dsmiley` | 3 | 2 | Person |
| Gézapeti | `gezapeti` | 2 | 1 | Person |
| Kevin Risden | `krisden` | 2 | 1 | Person |
| Nick Shanin | `nickshanin` | 2 | 2 | Self, aggregate only |
| ASF subversion and git services | `jira-bot` | 1 | 1 | Automation |
| Geoffrey Slinker | `gslinker` | 1 | 1 | Person |
| Mikhail Khludnev | `mkhl` | 1 | 1 | Person |
| Vadim Kirilchuk | `vkirilchuk` | 1 | 1 | Person |

## Fresh watch-list comments

These comments were created on 2026-10-03 UTC. Nick-authored comments remain in the history totals only and are excluded from candidate highlights.

| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| David Smiley | [SOLR-13833](https://issues.apache.org/jira/browse/SOLR-13833?focusedCommentId=18122149), `2026-10-03T01:33:10.795+0000` | “Thanks Nick.” |
| David Smiley | [SOLR-18196](https://issues.apache.org/jira/browse/SOLR-18196?focusedCommentId=18122153), `2026-10-03T02:08:16.005+0000` | “[~gslinker] please take a look at the PR. I just merged it but let us know if you think it addresses your concern or not.” |

## Candidate work from fresh comments

No candidate for this host. On SOLR-13833, David Smiley’s comment is a brief acknowledgment. On SOLR-18196, David says the PR is merged and directly asks Geoffrey Slinker (`gslinker`) to assess whether it addresses the concern; that follow-up is addressed to another ticket participant, so it is not claimed here.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18196](https://issues.apache.org/jira/browse/SOLR-18196) - Java serialization broken for QueryResponse - SimpleOrderedMap implements Map breaks ObjectOutputStream | `2026-10-03T02:08:16.000+0000` | 7 | David Smiley | PR merged; David asks Geoffrey Slinker to check whether it addresses the concern. No task claimed for this host. |
| [SOLR-13833](https://issues.apache.org/jira/browse/SOLR-13833) - setupRequest normally called twice | `2026-10-03T01:33:10.000+0000` | 6 | David Smiley | Acknowledgment to Nick; no candidate. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

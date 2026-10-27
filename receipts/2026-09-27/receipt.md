# Jira day scan receipt: 2026-09-27 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-28. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-27%22+AND+updated+%3C+%222026-09-28%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-27" AND updated < "2026-09-28"`. One search page returned all 6 tickets; every `updated` timestamp falls on 2026-09-27 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 6 histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 6 SOLR tickets; 36 comments across their complete histories; 11 commenter accounts. Two fresh watch-list comments were created that day, both by David Smiley on SOLR-18175. Nick Shanin’s one historical comment on one ticket remains in the aggregate only and is excluded from candidate highlights.

## Commenter table

`jira-bot` is marked as automation, not a person. Nick Shanin is labeled self and included for aggregate accounting only.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 12 | 4 | Automation |
| Mikhail Khludnev | `mkhl` | 5 | 2 | Commenter account |
| Chris M. Hostetter | `hossman` | 4 | 2 | Commenter account |
| Eric Pugh | `epugh` | 4 | 1 | Commenter account |
| David Smiley | `dsmiley` | 3 | 1 | Commenter account |
| Andrzej Bialecki | `ab` | 2 | 1 | Commenter account |
| Jan Høydahl | `janhoy` | 2 | 1 | Commenter account |
| Abhishek Umarjikar | `aumarjikar` | 1 | 1 | Commenter account |
| Kevin Liang | `liangkaiwen` | 1 | 1 | Commenter account |
| Nick Shanin | `nickshanin` | 1 | 1 | Self, aggregate only |
| Xinyao Zhang | `zhangxinyao88` | 1 | 1 | Commenter account |

## Fresh watch-list comments

Both comments were created on 2026-09-27 UTC. Nick Shanin’s history comment is not a fresh watch-list item and is not a candidate.

| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| David Smiley | [SOLR-18175](https://issues.apache.org/jira/browse/SOLR-18175?focusedCommentId=18119725), `2026-09-27T02:15:29.159+0000` | “Hmm. Maybe SolrIndexSearcher should override rewrite and then detect the exception there instead of at the parser.” |
| David Smiley | [SOLR-18175](https://issues.apache.org/jira/browse/SOLR-18175?focusedCommentId=18119726), `2026-09-27T02:16:45.032+0000` | “BTW my direct push of https://gitbox.apache.org/repos/asf?p=solr.git;h=cb93d2d9d3a (i.e. no PR) was a mistake; I meant a PR. Then realized I didn't... and well, though oh well; whatever.” |

## Candidate work from fresh comments

- **Candidate only - [SOLR-18175](%s), David Smiley:** consider whether `SolrIndexSearcher` should override `rewrite()` to detect the `FieldExistsQuery` exception there rather than at the parser. No implementation is claimed.

David’s separate note says his direct push was a mistake and he intended a PR. It is recorded as process context; no repository or PR action was taken.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. Tickets without fresh watch-list activity have no candidate from this scan.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18474](https://issues.apache.org/jira/browse/SOLR-18474) - Restrict plain SolrClientCache usage within Solr for better security | `2026-09-27T02:46:35.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18457](https://issues.apache.org/jira/browse/SOLR-18457) - Convert V2 "update" apis to JAX-RS | `2026-09-27T17:39:03.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18442](https://issues.apache.org/jira/browse/SOLR-18442) - SolrIndexSearcher is retained for the life of the node by OpenTelemetry observable gauges | `2026-09-27T03:11:34.000+0000` | 12 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18320](https://issues.apache.org/jira/browse/SOLR-18320) - bin/solr create should not use ZK if ZK host not provided | `2026-09-27T22:27:52.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18175](https://issues.apache.org/jira/browse/SOLR-18175) - A FieldExistsQuery IllegalStateException should result in HTTP 400 not 500 error | `2026-09-27T02:45:57.000+0000` | 13 | David Smiley | David Smiley suggests detecting the exception in SolrIndexSearcher.rewrite(); candidate-only design follow-up. His separate direct-push note is process context. |
| [SOLR-18135](https://issues.apache.org/jira/browse/SOLR-18135) - merge exception cause silently lost if assertions enabled; caused by double stop() of RTimer | `2026-09-27T02:47:07.000+0000` | 5 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

# Jira day scan receipt: 2026-10-05 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-10-06. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-10-05%22+AND+updated+%3C+%222026-10-06%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-10-05" AND updated < "2026-10-06"`. One search page returned all 4 tickets; every `updated` timestamp falls on 2026-10-05 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 4 histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 4 SOLR tickets; 13 comments across their full histories; 10 commenter accounts. Two fresh watch-list comments were created that day, by Jan Høydahl and Eric Pugh. Nick Shanin’s one historical comment remains in the full-history aggregate only and is excluded from candidate highlights.

## Commenter table

Counts cover the complete histories of the four tickets. `jira-bot` is marked as automation, not a person. Nick Shanin is labeled self and included for aggregate accounting only.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 4 | 2 | Automation |
| Andrzej Bialecki | `ab` | 1 | 1 | Person |
| Chris M. Hostetter | `hossman` | 1 | 1 | Person |
| Eric Pugh | `epugh` | 1 | 1 | Person |
| Houston Putman | `houston` | 1 | 1 | Person |
| Jan Høydahl | `janhoy` | 1 | 1 | Person |
| Lucas Kot-Zaniewski | `lkot` | 1 | 1 | Person |
| Nick Shanin | `nickshanin` | 1 | 1 | Self, aggregate only |
| Noble Paul | `noble.paul` | 1 | 1 | Person |
| Shrey Narayan | `nextbrick23` | 1 | 1 | Person |

## Fresh watch-list comments

These comments were created on 2026-10-05 UTC. Nick Shanin’s comments are retained in the history totals only and excluded from candidate highlights.

| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| Jan Høydahl | [SOLR-10579](https://issues.apache.org/jira/browse/SOLR-10579?focusedCommentId=18123250), `2026-10-05T07:06:35.669+0000` | “Found that this feature was started in [https://github.com/apache/solr/blob/main/solr/core/src/java/org/apache/solr/search/ValueSourceParser.java#L1259-L1266] with a commented-out stub. Question: Should we complete the feature or remove the commented stub? If complete, what stats should it calculate and what should the response be? I assume that most users still use the non-json based facet API and thus have not req…” |
| Eric Pugh | [SOLR-18503](https://issues.apache.org/jira/browse/SOLR-18503?focusedCommentId=18123355), `2026-10-05T13:04:26.954+0000` | “nice find! I've recently added the JSON query language to Quepid's Solr support, and am looking forward to shifting to using it much more!” |

## Candidate work from fresh comments

- **Candidate only - [SOLR-10579](https://issues.apache.org/jira/browse/SOLR-10579?focusedCommentId=18123250), Jan Høydahl:** Jan found a commented-out multistat stub and asks whether to complete the feature or remove it; if completing, the needed statistics and response shape must be decided. Clarify the desired direction with the ticket owner before any work. No task is claimed.
- SOLR-18503, Eric Pugh: “nice find!” He says he recently added the JSON query language to Quepid’s Solr support and looks forward to using it more. This is an informational response, not a separate follow-up candidate.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. No fresh watch-list activity means no candidate from this scan.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18503](https://issues.apache.org/jira/browse/SOLR-18503) - JSON Facet API: seven parser-accepted keys are missing from the reference guide | `2026-10-05T13:04:26.000+0000` | 1 | Eric Pugh | Eric Pugh praises the documentation finding and says he added JSON query-language support to Quepid; informational, no candidate. |
| [SOLR-18298](https://issues.apache.org/jira/browse/SOLR-18298) - ZkController.onReconnect Is Triggered Excessively | `2026-10-05T07:33:00.000+0000` | 7 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-13706](https://issues.apache.org/jira/browse/SOLR-13706) - Config API output is broken for "highlight" component | `2026-10-05T00:17:24.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-10579](https://issues.apache.org/jira/browse/SOLR-10579) - add multistat aggregation function | `2026-10-05T07:06:35.000+0000` | 1 | Jan Høydahl | Jan Høydahl asks whether to complete the feature or remove the commented stub; candidate only to clarify direction and output if completing. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

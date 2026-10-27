# Jira day scan receipt: 2026-10-02 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-10-03. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-10-02%22+AND+updated+%3C+%222026-10-03%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-10-02" AND updated < "2026-10-03"`. One search page returned all 14 tickets; every `updated` timestamp falls on 2026-10-02 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 14 histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 14 SOLR tickets; 49 comments across their complete histories; 20 commenter accounts. One fresh watch-list comment was created that day, by Jason Gerlowski on SOLR-17995. Nick Shanin’s 12 historical comments across 12 tickets remain in the aggregate only and are excluded from candidate highlights.

## Commenter table

`jira-bot` is marked as automation, not a person. Nick Shanin is labeled self and included for aggregate accounting only.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| Nick Shanin | `nickshanin` | 12 | 12 | Self, aggregate only |
| ASF subversion and git services | `jira-bot` | 6 | 2 | Automation |
| Ludovic Boutros | `lboutros` | 5 | 1 | Commenter account |
| Jason Gerlowski | `gerlowskija` | 4 | 1 | Commenter account |
| Sanjay Kumar Yadav | `yaduvanshi` | 3 | 1 | Commenter account |
| David Smiley | `dsmiley` | 2 | 1 | Commenter account |
| Erick Erickson | `erickerickson` | 2 | 2 | Commenter account |
| Hari Sekhon | `harisekhon` | 2 | 1 | Commenter account |
| Shawn Heisey | `elyograg` | 2 | 1 | Commenter account |
| Chris M. Hostetter | `hossman` | 1 | 1 | Commenter account |
| Eric Pugh | `epugh` | 1 | 1 | Commenter account |
| Joel Bernstein | `jbernste` | 1 | 1 | Commenter account |
| Kevin Risden | `krisden` | 1 | 1 | Commenter account |
| KHADIDJA MESSAOUDI | `khadidja_messaoudi` | 1 | 1 | Commenter account |
| Lucene/Solr QA | `lucenesolrqa` | 1 | 1 | Commenter account |
| Peter Somogyi | `psomogyi` | 1 | 1 | Commenter account |
| Peter Williams | `peterwx` | 1 | 1 | Commenter account |
| Richard Walker | `Richard.Walker@ands.org.au` | 1 | 1 | Commenter account |
| Shalin Shekhar Mangar | `shalin` | 1 | 1 | Commenter account |
| Tomas Eduardo Fernandez Lobbe | `tflobbe` | 1 | 1 | Commenter account |

## Fresh watch-list comments

This comment was created on 2026-10-02 UTC. Nick Shanin’s comments remain in the history totals only and are not candidates.

| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| Jason Gerlowski | [SOLR-17995](https://issues.apache.org/jira/browse/SOLR-17995?focusedCommentId=18121951), `2026-10-02T12:18:21.068+0000` | “Marking this as closed - the relevant methods are deprecated in 10.1 and removed altogether for 11.” |

## Candidate work from fresh comments

No candidate. Jason Gerlowski marks SOLR-17995 closed, explaining that the relevant methods are deprecated in Solr 10.1 and removed in Solr 11. This is a settled closure, not an open follow-up.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. Tickets without fresh watch-list activity have no candidate from this scan.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18468](https://issues.apache.org/jira/browse/SOLR-18468) - Make sure we documented --prompt-inputs | `2026-10-02T16:01:18.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18280](https://issues.apache.org/jira/browse/SOLR-18280) - reproducible TestReplicationHandler.testUrlAllowList failures | `2026-10-02T00:35:01.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17995](https://issues.apache.org/jira/browse/SOLR-17995) - SolrStream API should separate baseUrl from core/collection | `2026-10-02T12:18:21.000+0000` | 12 | Jason Gerlowski | Jason Gerlowski marks this closed: the relevant methods are deprecated in Solr 10.1 and removed in Solr 11; no candidate. |
| [SOLR-16130](https://issues.apache.org/jira/browse/SOLR-16130) - crossCollection Join: NPE in ExportWriter.fillOutDocs | `2026-10-02T04:42:29.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-13601](https://issues.apache.org/jira/browse/SOLR-13601) - Allow using 0 port with MiniSolrCloudCluster | `2026-10-02T20:18:56.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-13568](https://issues.apache.org/jira/browse/SOLR-13568) - Expand component should not cache group queries in the filter cache | `2026-10-02T21:24:09.000+0000` | 9 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-13524](https://issues.apache.org/jira/browse/SOLR-13524) - Or Stream Evaluator produces incorrect results with more than 2 arguments | `2026-10-02T21:23:39.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-13239](https://issues.apache.org/jira/browse/SOLR-13239) - CollectionStateWatcher reports new collections before they really exist | `2026-10-02T20:24:55.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-13136](https://issues.apache.org/jira/browse/SOLR-13136) - Queries fail during shard creation [testcase included] | `2026-10-02T21:25:42.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-12998](https://issues.apache.org/jira/browse/SOLR-12998) - Race condition between requery recovery and the core load completing | `2026-10-02T20:26:35.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-12858](https://issues.apache.org/jira/browse/SOLR-12858) - EmbeddedSolrServer POST method has contentType issue | `2026-10-02T04:40:47.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-12849](https://issues.apache.org/jira/browse/SOLR-12849) - collection parameter referencing an alias being handled differently when sent as GET than when sent as POST | `2026-10-02T21:22:29.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-11431](https://issues.apache.org/jira/browse/SOLR-11431) - Leader candidate cannot become leader if replica responds 500 to PeerSync | `2026-10-02T04:43:35.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-7394](https://issues.apache.org/jira/browse/SOLR-7394) - Shard replicas don't recover after cluster wide restart | `2026-10-02T04:43:09.000+0000` | 5 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

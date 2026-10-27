# Jira day scan receipt: 2026-09-22 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-23. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-22%22+AND+updated+%3C+%222026-09-23%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-22" AND updated < "2026-09-23"`. One search page returned all 5 tickets; every `updated` timestamp falls on 2026-09-22 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 5 histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 5 SOLR tickets; 19 comments across their complete histories; 6 commenter accounts. One fresh watch-list comment was created that day, by David Smiley on SOLR-18444. Nick Shanin did not appear in these histories.

## Commenter table

`jira-bot` is marked as automation, not a person.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 6 | 2 | Automation |
| David Smiley | `dsmiley` | 6 | 3 | Commenter account |
| Vladimir Vyatkin | `vyatkinv` | 4 | 1 | Commenter account |
| Abhishek Umarjikar | `aumarjikar` | 1 | 1 | Commenter account |
| Anshum Gupta | `anshum` | 1 | 1 | Commenter account |
| Mikhail Khludnev | `mkhl` | 1 | 1 | Commenter account |

## Fresh watch-list comments

This comment was created on 2026-09-22 UTC.

| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| David Smiley | [SOLR-18444](https://issues.apache.org/jira/browse/SOLR-18444?focusedCommentId=18118133), `2026-09-22T19:39:06.462+0000` | “To take advantage of this fully, this requires that queries all go to the leader {\\{shards.preference=replica.leader:true}} – AKA Query-Only-Leader (QOL) +or+ query NRT replicas. Extending support to non-leader TLOG/PULL would require smarter just-in-time replica synching – e.g. SOLR-18331 RE NRT: granted NRT kind of misses the point/implication here -- you are probably committing too frequently to benefit. If you a…” |

## Candidate work from fresh comments

- **Candidate only - [SOLR-18444](https://issues.apache.org/jira/browse/SOLR-18444), David Smiley:** clarify the intended replica support and query-routing constraints for coordinated autoCommit. The fresh note says current use requires leader-routed queries or NRT replicas; support for non-leader TLOG/PULL replicas would need smarter just-in-time syncing, and very frequent commits may undercut the NRT benefit. This is a technical design question, not an assigned task; nothing is claimed.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. Tickets without fresh watch-list activity have no candidate from this scan.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18475](https://issues.apache.org/jira/browse/SOLR-18475) - when mertrics are disabled: 500 No metrics found in response PrometheusResponseWriter.java | `2026-09-22T21:41:08.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18471](https://issues.apache.org/jira/browse/SOLR-18471) - MetricExporterFactory should use the standard OTEL env vars | `2026-09-22T04:27:38.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18444](https://issues.apache.org/jira/browse/SOLR-18444) - Time coordinated/synchronized autoCommit | `2026-09-22T19:39:06.000+0000` | 2 | David Smiley | David Smiley raises replica-routing and just-in-time synchronization constraints for broader TLOG/PULL support; candidate-only design clarification. |
| [SOLR-18130](https://issues.apache.org/jira/browse/SOLR-18130) - CloudSolrStream should not depend on ZooKeeper | `2026-09-22T15:43:46.000+0000` | 13 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17798](https://issues.apache.org/jira/browse/SOLR-17798) - Integrate SDK OTLP metric exporter | `2026-09-22T04:27:38.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

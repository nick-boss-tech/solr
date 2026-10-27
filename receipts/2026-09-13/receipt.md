# Jira day scan receipt: 2026-09-13 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-14. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-13%22+AND+updated+%3C+%222026-09-14%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-13" AND updated < "2026-09-14"`. One search page returned all 3 tickets; each `updated` timestamp falls on 2026-09-13 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 3 histories were fetched serially.
- Scope totals: 3 SOLR tickets; 33 comments across their complete histories; 16 commenter accounts. Two fresh watch-list comments were created that day, both by David Smiley. Nick Shanin did not appear in these histories.

## Commenter table

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| Scott Blum | `dragonsinth` | 6 | 1 | Commenter account |
| Erick Erickson | `erickerickson` | 5 | 2 | Commenter account |
| Varun Thacker | `varun` | 4 | 1 | Commenter account |
| David Smiley | `dsmiley` | 3 | 2 | Commenter account |
| Ishan Chattopadhyaya | `ichattopadhyaya` | 2 | 1 | Commenter account |
| Mark Miller | `markrmiller@gmail.com` | 2 | 1 | Commenter account |
| Phil Phillips | `irphilli` | 2 | 1 | Commenter account |
| Anshum Gupta | `anshum` | 1 | 1 | Commenter account |
| Cao Manh Dat | `caomanhdat` | 1 | 1 | Commenter account |
| Christine Poerschke | `cpoerschke` | 1 | 1 | Commenter account |
| Dorian | `ddorian43` | 1 | 1 | Commenter account |
| Jim Ferenczi | `jim.ferenczi` | 1 | 1 | Commenter account |
| Kevin Risden | `krisden` | 1 | 1 | Commenter account |
| Noble Paul | `noble.paul` | 1 | 1 | Commenter account |
| Shalin Shekhar Mangar | `shalin` | 1 | 1 | Commenter account |
| Timothy Potter | `thelabdude` | 1 | 1 | Commenter account |

## Fresh watch-list comments

These comments were created on 2026-09-13 UTC.

| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| David Smiley | [SOLR-7090](https://issues.apache.org/jira/browse/SOLR-7090?focusedCommentId=18114693), `2026-09-13T01:14:56.374+0000` | “Closing as a duplicate of SOLR-7776 done years ago.” |
| David Smiley | [SOLR-18331](https://issues.apache.org/jira/browse/SOLR-18331?focusedCommentId=18114705), `2026-09-13T02:40:20.702+0000` | “Or we write _something_ to ZK (not state.json!) for each shard leader's commit flush. Followers can "watch". No need for an HTTP message. That _something_ could be interesting info like number of docs, number of segments, number of bytes, Lucene index generation, commit metadata. This info would in turn be useful for diagnostic purposes without having to consult (query) replicas.” |

## Candidate work from fresh comments

Candidate only — not claimed: David Smiley’s SOLR-18331 proposal suggests assessing a per-shard-leader ZooKeeper record for each commit flush that followers can watch, carrying useful commit metadata, as a way to support just-in-time TLOG/PULL pulls without polling or an HTTP notification. The comment proposes a design direction; it does not assign work or establish the needed implementation scope.

SOLR-7090 is settled: David Smiley closed it as a duplicate of SOLR-7776; no candidate follows from that comment.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18331](https://issues.apache.org/jira/browse/SOLR-18331) - TLOG/PULL should be notified to pull just-in-time; no polling | `2026-09-13T02:40:20.000+0000` | 1 | David Smiley | David Smiley proposes a watchable ZooKeeper record for leader commit flushes, potentially carrying diagnostic metadata; candidate-only feasibility/design follow-up, not claimed. |
| [SOLR-7090](https://issues.apache.org/jira/browse/SOLR-7090) - Cross collection join | `2026-09-13T01:14:56.000+0000` | 21 | David Smiley | David Smiley closes as duplicate of SOLR-7776; settled, no candidate. |
| [SOLR-6606](https://issues.apache.org/jira/browse/SOLR-6606) - NRT leader should distribute autoCommits to followers | `2026-09-13T01:38:33.000+0000` | 11 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

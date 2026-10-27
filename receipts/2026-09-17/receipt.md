# Jira day scan receipt: 2026-09-17 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-18. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-17%22+AND+updated+%3C+%222026-09-18%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-17" AND updated < "2026-09-18"`. One search page returned all 6 tickets; each `updated` timestamp falls on 2026-09-17 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 6 histories were fetched serially.
- Scope totals: 6 SOLR tickets; 19 comments across their complete histories; 6 commenter accounts. Two fresh watch-list comments were created that day, both captured below. Nick Shanin did not appear in these histories.

## Commenter table

`jira-bot` is marked as automation, not a person.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 9 | 2 | Automation |
| David Smiley | `dsmiley` | 5 | 2 | Commenter account |
| Serhiy Bzhezytskyy | `serhiybzhezytskyy` | 2 | 1 | Commenter account |
| Andrzej Bialecki | `ab` | 1 | 1 | Commenter account |
| Eric Pugh | `epugh` | 1 | 1 | Commenter account |
| Jan Høydahl | `janhoy` | 1 | 1 | Commenter account |

## Fresh watch-list comments

These comments were created on 2026-09-17 UTC.

| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| David Smiley | [SOLR-17987](https://issues.apache.org/jira/browse/SOLR-17987?focusedCommentId=18116356), `2026-09-17T14:15:04.755+0000` | “And no gauge could possible retain a lambda pointer reference that could "leak" in the event Solr forgets to close/unregister something associated with metrics.” |
| David Smiley | [SOLR-17316](https://issues.apache.org/jira/browse/SOLR-17316?focusedCommentId=18116449), `2026-09-17T18:34:18.217+0000` | “Thanks Serhiy!” |

## Candidate work from fresh comments

No candidate: one fresh comment is a technical clarification about gauge/lambda references and metric cleanup; the other is a thank-you. Neither asks for additional work.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18452](https://issues.apache.org/jira/browse/SOLR-18452) - Use Content-Type header to distinguish single JSON document from JSON command object | `2026-09-17T12:18:03.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18445](https://issues.apache.org/jira/browse/SOLR-18445) - Let /update handler support JSONL / NDJSON | `2026-09-17T14:00:37.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18422](https://issues.apache.org/jira/browse/SOLR-18422) - CrossDC Producer: System.nanoTime used as absolute timestamp | `2026-09-17T13:48:02.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18158](https://issues.apache.org/jira/browse/SOLR-18158) - Upgrade cuVS (cuvs-lucene / cuvs-java) to 26.02 | `2026-09-17T07:47:42.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17987](https://issues.apache.org/jira/browse/SOLR-17987) - OTEL: Enable/disable metrics categorically by registry | `2026-09-17T14:15:04.000+0000` | 1 | David Smiley | David Smiley clarifies that a gauge should not retain a lambda pointer that could leak if metric cleanup is missed; technical discussion, no requested follow-up. |
| [SOLR-17316](https://issues.apache.org/jira/browse/SOLR-17316) - SolrResponseBase getters should work with all ResponseParsers | `2026-09-17T18:34:51.000+0000` | 13 | David Smiley | David Smiley thanks Serhiy; acknowledgement only. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

# Jira day scan receipt: 2026-09-09 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-10. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-09%22+AND+updated+%3C+%222026-09-10%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-09" AND updated < "2026-09-10"`. One search page returned all 11 tickets; each `updated` timestamp falls on 2026-09-09 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 11 histories were fetched serially.
- Scope totals: 11 SOLR tickets; 17 comments across their complete histories; 3 commenter accounts. Two fresh watch-list comments were created that day, from David Smiley and Jan Høydahl. Nick Shanin did not appear in these histories.

## Commenter table

`jira-bot` is marked as automation, not a person.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 13 | 5 | Automation |
| David Smiley | `dsmiley` | 3 | 2 | Commenter account |
| Jan Høydahl | `janhoy` | 1 | 1 | Commenter account |

## Fresh watch-list comments

These comments were created on 2026-09-09 UTC.

| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| David Smiley | [SOLR-17444](https://issues.apache.org/jira/browse/SOLR-17444?focusedCommentId=18113105), `2026-09-09T02:10:26.301+0000` | “Thanks for contributing!” |
| Jan Høydahl | [SOLR-18432](https://issues.apache.org/jira/browse/SOLR-18432?focusedCommentId=18113566), `2026-09-09T22:35:23.120+0000` | “While I have not experienced these as issues they all sound like improvements worth doing. And yea, I'm quite shocked :)” |

## Candidate work from fresh comments

Candidate only — not claimed: Jan Høydahl says the improvements raised on SOLR-18432 sound worth doing, despite not experiencing the issues. A cautious next step would be to clarify which prerelease/SNAPSHOT behaviors the ticket intends to improve and define a small reproducing test/scope; his comment does not identify a specific implementation request.

David Smiley’s SOLR-17444 comment is a thank-you to a contributor; acknowledgment only, no candidate.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18436](https://issues.apache.org/jira/browse/SOLR-18436) - CloudSolrClient has no fallback option for randomly selected node when sending V2 requests | `2026-09-09T17:52:08.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18435](https://issues.apache.org/jira/browse/SOLR-18435) - CloudSolrClient and implicit assumptions about Set<String> liveNodes iterator order | `2026-09-09T17:39:14.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18434](https://issues.apache.org/jira/browse/SOLR-18434) - SolrRequest.getPreferredNodes() used very inconsistently in CloudSolrClient | `2026-09-09T18:03:28.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18433](https://issues.apache.org/jira/browse/SOLR-18433) - CloudSolrClient inconsistencies in routing non-collection requests | `2026-09-09T17:38:10.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18432](https://issues.apache.org/jira/browse/SOLR-18432) - SolrVersion problems with prerelease (SNAPSHOT) | `2026-09-09T22:35:23.000+0000` | 1 | Jan Høydahl | Jan Høydahl says the ticket’s proposed improvements sound worthwhile, while noting he has not encountered them; candidate-only scope/test clarification, not claimed. |
| [SOLR-18411](https://issues.apache.org/jira/browse/SOLR-18411) - CrossDC Producer: KafkaProducerSink's KafkaConsumer gets no security config | `2026-09-09T14:37:56.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18409](https://issues.apache.org/jira/browse/SOLR-18409) - CrossDC Producer: shouldMirror not whitelisted for distrib update requests | `2026-09-09T02:10:55.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18390](https://issues.apache.org/jira/browse/SOLR-18390) - Remove test-framework HTTP client convenience helpers | `2026-09-09T14:37:47.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18217](https://issues.apache.org/jira/browse/SOLR-18217) - Add a SemVer field type | `2026-09-09T00:18:20.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17968](https://issues.apache.org/jira/browse/SOLR-17968) - Add HttpSolrClient as base class | `2026-09-09T17:56:01.000+0000` | 7 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17444](https://issues.apache.org/jira/browse/SOLR-17444) - Add Passage Sorting Option in UnifiedSolrHighlighter | `2026-09-09T02:10:26.000+0000` | 3 | David Smiley | David Smiley thanks the contributor; acknowledgment only. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

# Jira day scan receipt: 2026-09-26 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-27. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-26%22+AND+updated+%3C+%222026-09-27%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-26" AND updated < "2026-09-27"`. One search page returned all 5 tickets; every `updated` timestamp falls on 2026-09-26 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 5 histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 5 SOLR tickets; 25 comments across their complete histories; 6 commenter accounts. One fresh watch-list comment was created that day, by Eric Pugh on SOLR-16390. Nick Shanin did not appear in these histories.

## Commenter table

`jira-bot` is marked as automation, not a person.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 12 | 3 | Automation |
| Eric Pugh | `epugh` | 4 | 2 | Commenter account |
| Jason Gerlowski | `gerlowskija` | 3 | 1 | Commenter account |
| Mikhail Khludnev | `mkhl` | 3 | 1 | Commenter account |
| David Smiley | `dsmiley` | 2 | 1 | Commenter account |
| Jan Høydahl | `janhoy` | 1 | 1 | Commenter account |

## Fresh watch-list comments

This comment was created on 2026-09-26 UTC.

| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| Eric Pugh | [SOLR-16390](https://issues.apache.org/jira/browse/SOLR-16390?focusedCommentId=18119682), `2026-09-26T17:31:43.736+0000` | “I think it works really well when someone is the Jira "gardener" and makign sure the tickets get to done, and they get closed out, or deleted when things become OBE. IN the v2 story, we don't need ALL the tickets at once, we need enough that people know "what is next"....” |

## Candidate work from fresh comments

- **Candidate only - [SOLR-16390](%s), Eric Pugh:** clarify whether the v2 JAX-RS migration should have a named Jira gardener to keep the “what is next” set manageable, close completed work, and remove tickets that are obsolete. The comment is a process suggestion, not an assigned task; nothing is claimed.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. Tickets without fresh watch-list activity have no candidate from this scan.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18485](https://issues.apache.org/jira/browse/SOLR-18485) - js-client: build with pure Java, not Node | `2026-09-26T23:52:06.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18484](https://issues.apache.org/jira/browse/SOLR-18484) - auxIndexJoin perf opt - eager drain when there's no filter | `2026-09-26T19:13:04.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18479](https://issues.apache.org/jira/browse/SOLR-18479) - Finalize V2 schema api for copyFields | `2026-09-26T10:27:34.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18477](https://issues.apache.org/jira/browse/SOLR-18477) - faster intersection, and omit stored fields for auxIndexJoin | `2026-09-26T12:25:13.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-16390](https://issues.apache.org/jira/browse/SOLR-16390) - Cosmetic improvements and migration to JAX-RS (v2 cluster and clusterprop APIs) | `2026-09-26T17:31:43.000+0000` | 15 | Eric Pugh | Eric Pugh suggests a Jira gardener to keep next-up work current and close/delete completed or obsolete tickets; candidate-only process follow-up. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

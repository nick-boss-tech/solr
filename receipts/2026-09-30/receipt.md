# Jira day scan receipt: 2026-09-30 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-10-01. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-30%22+AND+updated+%3C+%222026-10-01%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-30" AND updated < "2026-10-01"`. One search page returned all 4 tickets; every `updated` timestamp falls on 2026-09-30 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 4 histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 4 SOLR tickets; 15 comments across their complete histories; 5 commenter accounts. One fresh watch-list comment was created that day, by Jason Gerlowski on SOLR-18417. Nick Shanin did not appear in these histories.

## Commenter table

`jira-bot` is marked as automation, not a person.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 11 | 3 | Automation |
| David Smiley | `dsmiley` | 1 | 1 | Commenter account |
| Eric Pugh | `epugh` | 1 | 1 | Commenter account |
| Jan Høydahl | `janhoy` | 1 | 1 | Commenter account |
| Jason Gerlowski | `gerlowskija` | 1 | 1 | Commenter account |

## Fresh watch-list comments

This comment was created on 2026-09-30 UTC.

| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| Jason Gerlowski | [SOLR-18417](https://issues.apache.org/jira/browse/SOLR-18417?focusedCommentId=18120932), `2026-09-30T01:06:03.317+0000` | “bq. it's not obvious when it's truly done My aim was to create a deprecation PR that'd land on both 'main' and 'branch_10x' (now completed), and then do a removal PR that targets main only. When both those are completed, I'd close this out as "done". Let me know if that plan sounds "off" to anyone. If there's some element of this that's non-obvious or could be clarified, let me know!” |

## Candidate work from fresh comments

- **Candidate only - [SOLR-18417](https://issues.apache.org/jira/browse/SOLR-18417?focusedCommentId=18120932), Jason Gerlowski:** Jason says the deprecation PRs to `main` and `branch_10x` are complete and planned a removal PR targeting `main` only. Check whether that remaining PR has landed; if so, close the ticket, otherwise confirm the status of the planned removal. No task is claimed.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. Tickets without fresh watch-list activity have no candidate from this scan.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18417](https://issues.apache.org/jira/browse/SOLR-18417) - Deprecate and Remove PingRequestHandler | `2026-09-30T19:13:19.000+0000` | 8 | Jason Gerlowski | Jason Gerlowski reports deprecation PRs to main and branch_10x complete; planned main-only removal PR remains to verify, then close if done. |
| [SOLR-18303](https://issues.apache.org/jira/browse/SOLR-18303) - Incremental backup failure-cleanup path masks the real error with an unrelated `NoSuchFileException` | `2026-09-30T20:25:56.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18072](https://issues.apache.org/jira/browse/SOLR-18072) - Refactor CollectionApiCommands to be able to easily pass in more context | `2026-09-30T18:22:54.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17767](https://issues.apache.org/jira/browse/SOLR-17767) - SIP-24: Java Security Manager replacement | `2026-09-30T00:19:32.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

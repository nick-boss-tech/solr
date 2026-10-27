# Jira day scan receipt: 2026-10-25 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`).
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-10-25%22+AND+updated+%3C+%222026-10-26%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-10-25" AND updated < "2026-10-26"`. Search pagination: one page, 15 of 15 tickets; every returned `updated` timestamp falls on 2026-10-25 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 15 comment histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 15 SOLR tickets updated on the day; 81 comments scanned across those full histories; 15 commenter accounts (14 people and one automation account).

## Commenter table

Counts cover all comments in the histories of the 15 tickets, including older comments. `jira-bot` is automation, not a person.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 21 | 3 | Automation |
| David Smiley | `dsmiley` | 17 | 5 | Person |
| Jan Høydahl | `janhoy` | 11 | 4 | Person |
| Chris M. Hostetter | `hossman` | 7 | 3 | Person |
| Eric Pugh | `epugh` | 7 | 2 | Person |
| Nick Shanin | `nickshanin` | 5 | 2 | Person |
| Tim Dillon | `trdillon` | 3 | 1 | Person |
| Amrit Sarkar | `sarkaramrit2@gmail.com` | 2 | 2 | Person |
| Houston Putman | `houston` | 2 | 1 | Person |
| Cassandra Targett | `ctargett` | 1 | 1 | Person |
| Ege Pancaroğlu | `pancaroglu` | 1 | 1 | Person |
| Haleema Khan | `hsadia538` | 1 | 1 | Person |
| Ishan Chattopadhyaya | `ichattopadhyaya` | 1 | 1 | Person |
| Jason Gerlowski | `gerlowskija` | 1 | 1 | Person |
| Sanjay Dutt | `sanjaydutt` | 1 | 1 | Person |

## Fresh watch-list comments

These are the only comments by the approved watch-list accounts created on 2026-10-25 UTC. Freshness is determined by each comment's `created` timestamp, not the ticket's `updated` value.

| Person | Ticket and time | Comment |
| --- | --- | --- |
| Eric Pugh | [SOLR-17953](https://issues.apache.org/jira/browse/SOLR-17953?focusedCommentId=18131879), `2026-10-25T14:42:21.331+0000` | “thanks for this bit of \"make life easier\" tooling!” |
| David Smiley | [SOLR-18188](https://issues.apache.org/jira/browse/SOLR-18188?focusedCommentId=18131896), `2026-10-25T17:01:42.360+0000` | “I loved your write-up Sanjay! +1 to the proposed test fix.” |

Neither fresh comment suggests a separate follow-up: Eric Pugh's is a thank-you, and David Smiley's endorses an existing proposed test fix. Jan Høydahl, Christos Malliaridis, and Jason Gerlowski had no comments created on this date among these ticket histories.

## Per-ticket dispositions

“Comments” counts the entire retrieved comment history for that ticket. A dash under fresh activity means no watch-list comment was created on the scan date.

| Ticket | Comments | Fresh watch-list activity | Disposition |
| --- | ---: | --- | --- |
| [SOLR-18589](https://issues.apache.org/jira/browse/SOLR-18589) | 0 | None | No candidate from this scan. |
| [SOLR-18584](https://issues.apache.org/jira/browse/SOLR-18584) | 0 | None | No candidate from this scan. |
| [SOLR-18577](https://issues.apache.org/jira/browse/SOLR-18577) | 0 | None | No candidate from this scan. |
| [SOLR-18562](https://issues.apache.org/jira/browse/SOLR-18562) | 0 | None | No candidate from this scan. |
| [SOLR-18561](https://issues.apache.org/jira/browse/SOLR-18561) | 0 | None | No candidate from this scan. |
| [SOLR-18542](https://issues.apache.org/jira/browse/SOLR-18542) | 0 | None | No candidate from this scan. |
| [SOLR-18537](https://issues.apache.org/jira/browse/SOLR-18537) | 1 | None | No candidate from this scan. |
| [SOLR-18525](https://issues.apache.org/jira/browse/SOLR-18525) | 2 | None | No candidate from this scan. |
| [SOLR-18240](https://issues.apache.org/jira/browse/SOLR-18240) | 1 | None | No candidate from this scan. |
| [SOLR-18188](https://issues.apache.org/jira/browse/SOLR-18188) | 27 | David Smiley, 1 | Supportive endorsement of the proposed test fix; no additional task inferred from the comment. |
| [SOLR-18119](https://issues.apache.org/jira/browse/SOLR-18119) | 15 | None | No candidate from this scan. |
| [SOLR-17953](https://issues.apache.org/jira/browse/SOLR-17953) | 5 | Eric Pugh, 1 | Thank-you only; no follow-up candidate. |
| [SOLR-16630](https://issues.apache.org/jira/browse/SOLR-16630) | 7 | None | No candidate from this scan. |
| [SOLR-12613](https://issues.apache.org/jira/browse/SOLR-12613) | 20 | None | No candidate from this scan. |
| [SOLR-3479](https://issues.apache.org/jira/browse/SOLR-3479) | 3 | None | No candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

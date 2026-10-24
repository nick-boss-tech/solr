# Jira day scan receipt: 2026-10-20 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). Claim started at `2026-10-24T18:14:26Z`; scan filed at `2026-10-24T18:15:40Z`.
- Scope source: [anonymous Jira search API request](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-10-20%22+AND+updated+%3C+%222026-10-21%22&fields=key%2Cupdated&maxResults=100&startAt=0); JQL: `project = SOLR AND updated >= "2026-10-20" AND updated < "2026-10-21"`. The result was paginated at 100; 9 of 9 matching tickets were retrieved.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at 100. All 9 ticket histories were fetched serially; there were 9 comment API requests with at least 0.2 seconds between requests, no parallel fetching, and no authentication.
- Scope totals: 9 SOLR tickets updated on the day; 56 comments scanned across those tickets’ full histories; 10 commenter accounts (nine people and one automation account).

## Commenter accounts on the day’s tickets

The counts below cover every comment found on the 9 scoped tickets, including older history. `jira-bot` is automation, not a person.

| Commenter | Username | Comments | Tickets |
| --- | --- | ---: | ---: |
| ASF subversion and git services (automation) | `jira-bot` | 22 | 6 |
| Jan Høydahl | `janhoy` | 12 | 2 |
| David Smiley | `dsmiley` | 9 | 4 |
| Eric Pugh | `epugh` | 6 | 3 |
| Jalaz Kumar | `jaykay12` | 2 | 1 |
| Jason Gerlowski | `gerlowskija` | 1 | 1 |
| Chris M. Hostetter | `hossman` | 1 | 1 |
| Ishan Chattopadhyaya | `ichattopadhyaya` | 1 | 1 |
| Matthew Biscocho | `mlbiscoc` | 1 | 1 |
| Nick Shanin | `nickshanin` | 1 | 1 |

## Fresh watch-list comments created on 2026-10-20 UTC

Freshness was determined only from each comment’s `created` timestamp converted to UTC. The links focus the corresponding Jira comments.

| Person | Ticket / time | Comment excerpt |
| --- | --- | --- |
| Jan Høydahl | [SOLR-18147](https://issues.apache.org/jira/browse/SOLR-18147?focusedCommentId=18129013), `2026-10-20T07:54:46.108+0000` | “MVP dashboard in SOLR-18534 and then follow up missing pieces in dedicated JIRAs.” |
| David Smiley | [SOLR-18450](https://issues.apache.org/jira/browse/SOLR-18450?focusedCommentId=18129023), `2026-10-20T08:08:58.516+0000` | “Given the feature freeze was in place for a while, I don't think it makes sense to bring a very noticeable change (for V2 API users) like this into 10_1. Please revert them.” |
| Jan Høydahl | [SOLR-18266](https://issues.apache.org/jira/browse/SOLR-18266?focusedCommentId=18129034), `2026-10-20T08:29:47.035+0000` | “As long as we have a sponsor willing to pay the montly bill for storage and egress.” |
| Jason Gerlowski | [SOLR-18284](https://issues.apache.org/jira/browse/SOLR-18284?focusedCommentId=18129173), `2026-10-20T14:17:48.641+0000` | “Hey [~markrmiller] - should this be closed?” |

## Older history versus fresh activity

The aggregate table includes older comments on the same 9 tickets; it is not a count of activity created on October 20. Jan Høydahl had 12 comments across the histories, of which 2 were fresh and 10 older. David Smiley had 9, of which 1 was fresh and 8 older; Eric Pugh had 6, all older; Jason Gerlowski had 1, fresh. Christos Malliaridis had none.

## Candidate work from the fresh comments

- **SOLR-18450 — David Smiley:** the fresh comment asks for two backports to 10_1 to be reverted because of the feature freeze and missing user documentation. A bounded follow-up candidate is to verify which commits it refers to and whether the revert is still outstanding, then report that evidence and ask whether anything remains; this scan inspected no branch or backport state.
- **SOLR-18284 — Jason Gerlowski:** the fresh comment asks whether the ticket should be closed. This is a ticket-state decision for the sponsor; no technical work is implied by the comment alone.
- **SOLR-18266 — Jan Høydahl:** the comment describes an AWS nightly-benchmark proof of concept and makes ongoing storage/egress sponsorship a condition. A possible decision candidate is to establish whether a sponsor exists before proposing further work; no cost or sponsorship check was done in this scan.
- **SOLR-18147 — Jan Høydahl:** the comment frames the MVP dashboard in SOLR-18534 first, with missing pieces moved to dedicated JIRAs. Recorded as a deferred follow-up direction; no additional candidate is inferred from this comment alone.

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created.

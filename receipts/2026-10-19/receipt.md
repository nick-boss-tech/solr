# Jira day scan receipt: 2026-10-19 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). Claim started at `2026-10-24T18:16:09Z`; scan filed at `2026-10-24T18:17:08Z`.
- Scope source: [anonymous Jira search API request](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-10-19%22+AND+updated+%3C+%222026-10-20%22&fields=key%2Cupdated&maxResults=100&startAt=0); JQL: `project = SOLR AND updated >= "2026-10-19" AND updated < "2026-10-20"`. The result was paginated at 100; 9 of 9 matching tickets were retrieved.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at 100. All 9 ticket histories were fetched serially; there were 9 comment API requests with at least 0.2 seconds between requests, no parallel fetching, and no authentication.
- Scope totals: 9 SOLR tickets updated on the day; 45 comments scanned across those tickets’ full histories; eight commenter accounts (seven people and one automation account).

## Commenter accounts on the day’s tickets

The counts below cover every comment found on the 9 scoped tickets, including older history. `jira-bot` is automation, not a person.

| Commenter | Username | Comments | Tickets |
| --- | --- | ---: | ---: |
| ASF subversion and git services (automation) | `jira-bot` | 27 | 2 |
| Jason Gerlowski | `gerlowskija` | 6 | 2 |
| Nick Shanin | `nickshanin` | 4 | 2 |
| David Smiley | `dsmiley` | 3 | 2 |
| Matthew Biscocho | `mlbiscoc` | 2 | 2 |
| Boyi Dai | `boyidai` | 1 | 1 |
| Eric Pugh | `epugh` | 1 | 1 |
| Pierre Salagnac | `psalagnac` | 1 | 1 |

## Fresh watch-list comments created on 2026-10-19 UTC

Freshness was determined only from the comment’s `created` timestamp converted to UTC. The link focuses the corresponding Jira comment.

| Person | Ticket / time | Comment excerpt |
| --- | --- | --- |
| Jason Gerlowski | [SOLR-18551](https://issues.apache.org/jira/browse/SOLR-18551?focusedCommentId=18128711), `2026-10-19T11:28:56.104+0000` | “At my work we switched a customer over to using TLOG/PULL replicas and within a few hours certain Solr nodes were seeing lots of `mmap failed` error messages.” |

## Older history versus fresh activity

The aggregate table includes older comments on the same 9 tickets; it is not a count of activity created on October 19. Jason Gerlowski had 6 comments across the histories, of which 1 was fresh and 5 older. David Smiley had 3, all older; Eric Pugh had 1, older; Jan Høydahl and Christos Malliaridis had none. The other commenters are not on the approved watch list.

## Candidate work from the fresh comment

- **SOLR-18551 — Jason Gerlowski:** his fresh note describes `mmap failed` reports after switching to TLOG/PULL replicas and outlines a diagnostic path through Lucene issue 15068, `IOContext.DEFAULT` use, `/proc/<pid>/maps`, and a heap dump. A possible evidence-first follow-up would check whether the ticket’s proposed solution covers the TLOG/PULL-specific paths he describes, then ask whether that explanation captures the reported failure. This scan performed no reproduction or source inspection, so it recommends no implementation.

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created.

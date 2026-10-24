# Jira day scan receipt: 2026-10-22 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). Claim started at `2026-10-24T17:52:50Z`; scan filed at `2026-10-24T17:55:17Z`.
- Query: `project = SOLR AND updated >= "2026-10-22" AND updated < "2026-10-23"` against the anonymous Jira REST search API, paginated at 100. Comment histories were fetched from the anonymous issue-comment API, paginated at 100, serially with at least 0.15 seconds between comment requests; no parallel fetching.
- Scope totals: 29 SOLR tickets updated on the day; 125 comments scanned across those tickets’ full histories; 23 commenter accounts (22 people and one automation account).

## Commenter accounts on the day’s tickets

The counts below cover every comment found on the 29 scoped tickets, including older history. The automation account is marked separately and is not a person.

| Commenter | Username | Comments | Tickets |
| --- | --- | ---: | ---: |
| ASF subversion and git services (automation) | `jira-bot` | 57 | 19 |
| Eric Pugh | `epugh` | 13 | 6 |
| Nick Shanin | `nickshanin` | 11 | 9 |
| David Smiley | `dsmiley` | 8 | 7 |
| Jan Høydahl | `janhoy` | 8 | 3 |
| Amrit Sarkar | `sarkaramrit2@gmail.com` | 4 | 3 |
| Chris M. Hostetter | `hossman` | 3 | 1 |
| Tim Dillon | `trdillon` | 3 | 1 |
| Jason Gerlowski | `gerlowskija` | 2 | 2 |
| Houston Putman | `houston` | 2 | 1 |
| Thomas Mortagne | `tmortagne` | 2 | 1 |
| Adam Quigley | `adamjq` | 1 | 1 |
| Anshum Gupta | `anshum` | 1 | 1 |
| Cesar Rodriguez | `cesar.rodriguez` | 1 | 1 |
| Cassandra Targett | `ctargett` | 1 | 1 |
| Haleema Khan | `hsadia538` | 1 | 1 |
| Jalaz Kumar | `jaykay12` | 1 | 1 |
| Robert Lucarini | `lucarini@gmail.com` | 1 | 1 |
| Matthew Biscocho | `mlbiscoc` | 1 | 1 |
| Ege Pancaroğlu | `pancaroglu` | 1 | 1 |
| Pranay Kumar Chaudhary | `pranay3` | 1 | 1 |
| Maciej Lizewski | `redguy666` | 1 | 1 |
| Uwe Schindler | `uschindler` | 1 | 1 |

## Fresh watch-list comments created on 2026-10-22 UTC

These are the only comments counted as fresh: each `created` timestamp falls on the scanned UTC day. The URLs focus the corresponding Jira comment.

| Person | Ticket / time | Comment excerpt |
| --- | --- | --- |
| David Smiley | [SOLR-18407](https://issues.apache.org/jira/browse/SOLR-18407?focusedCommentId=18130097), `2026-10-22T03:02:29.052+0000` | “When running Solr 10 on Java 17 ... I'm in disbelief. Solr 10's minimum supported Java version is 21. SolrJ 10 runs with Java 17 but that's not pertinent here.” |
| David Smiley | [SOLR-17971](https://issues.apache.org/jira/browse/SOLR-17971?focusedCommentId=18130281), `2026-10-22T12:17:58.507+0000` | “Thanks Amrit! And for your review Pierre.” |
| David Smiley | [SOLR-17780](https://issues.apache.org/jira/browse/SOLR-17780?focusedCommentId=18130382), `2026-10-22T14:58:56.175+0000` | “I chose to backport this issue to 9,x for delivery in 9.12. Why not; automation makes this easy.” |
| David Smiley | [SOLR-18267](https://issues.apache.org/jira/browse/SOLR-18267?focusedCommentId=18130383), `2026-10-22T15:00:51.125+0000` | “Coming to 9.12 because... why not? Automated backports is a beautiful thing.” |
| Jason Gerlowski | [SOLR-18568](https://issues.apache.org/jira/browse/SOLR-18568?focusedCommentId=18130390), `2026-10-22T15:10:45.033+0000` | “The thread-hangs we found were different I believe, but my team hit the same end-result you describe here (i.e. SolrCores failing to shut down) over in SOLR-18244. Either our shutdown logic needs some close inspection to make sure we catch all of these potential hang-ups, or we should pursue the timeout idea you mentioned. There's been waay too many \"stuck thread\" issues lately...” |

## Older history versus fresh activity

The aggregate table includes older comments on the same 29 tickets; it is not a count of activity created on October 22. For the watch list, David Smiley had 8 comments in those histories, of which 4 were fresh and 4 older; Jason Gerlowski had 2, of which 1 was fresh and 1 older; Eric Pugh had 13, all older; Jan Høydahl had 8, all older; Christos Malliaridis had none. Freshness was determined from each comment’s `created` timestamp in UTC, not from the ticket’s `updated` timestamp or the comment’s `updated` field.

## Candidate work from the fresh comments

- **SOLR-18568 — Jason Gerlowski:** his fresh comment identifies two possible paths for repeated shutdown hangs: inspect shutdown cleanup or evaluate the timeout approach, and relates the symptom to SOLR-18244. A narrowly scoped, owner-authorized follow-up would first verify whether the reported shutdown examples share a failure path, then ask which direction that evidence supports. This scan produced no reproduction evidence, so it does not recommend implementation.
- **SOLR-17780 — David Smiley:** the comment states a 9.12 backport decision; recorded as settled, with no candidate.
- **SOLR-18267 — David Smiley:** the comment states 9.12 delivery; recorded as settled, with no candidate.
- **SOLR-17971 — David Smiley:** acknowledgement of reviewers only; no follow-up candidate.
- **SOLR-18407 — David Smiley:** a technical correction about Solr 10’s Java 21 minimum; recorded only, with no separate follow-up candidate inferred from this comment alone.

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created.

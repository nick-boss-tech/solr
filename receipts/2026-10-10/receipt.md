# Jira day scan receipt: 2026-10-10 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-10-11. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-10-10%22+AND+updated+%3C+%222026-10-11%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-10-10" AND updated < "2026-10-11"`. Search pagination: one page, 14 of 14 tickets; all returned `updated` timestamps fall on 2026-10-10 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 14 histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 14 SOLR tickets updated on the day; 54 comments across their complete histories; 24 commenter accounts. The six fresh watch-list comments were written by Eric Pugh and David Smiley. Nick-authored comments, if present, stay in full-history totals only and are excluded from candidate highlights.

## Commenter table

Counts cover all comments in the histories of the 14 tickets, including older comments. `jira-bot` is marked as automation, not a person. No `nickshanin` account appeared in this day’s aggregate.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 8 | 3 | Automation |
| Eric Pugh | `epugh` | 6 | 3 | Person |
| David Smiley | `dsmiley` | 5 | 1 | Person |
| Jan Høydahl | `janhoy` | 4 | 3 | Person |
| Anders Rask | `anders.rask` | 3 | 1 | Person |
| Houston Putman | `houston` | 3 | 2 | Person |
| Hrishikesh Gadre | `hgadre` | 2 | 1 | Person |
| Ishan Chattopadhyaya | `ichattopadhyaya` | 2 | 1 | Person |
| Jason Gerlowski | `gerlowskija` | 2 | 1 | Person |
| Karl Wright | `kwright@metacarta.com` | 2 | 1 | Person |
| Noble Paul | `noble.paul` | 2 | 1 | Person |
| Sanjay Dutt | `sanjaydutt` | 2 | 1 | Person |
| Velmurugan Periasamy | `vel` | 2 | 1 | Person |
| Andreas Hubold | `ahubold` | 1 | 1 | Person |
| Chris M. Hostetter | `hossman` | 1 | 1 | Person |
| Erick Erickson | `erickerickson` | 1 | 1 | Person |
| Gus Heck | `gus` | 1 | 1 | Person |
| Joel Bernstein | `jbernste` | 1 | 1 | Person |
| Lucas Kot-Zaniewski | `lkot` | 1 | 1 | Person |
| Marcus Eagan | `marcussorealheis` | 1 | 1 | Person |
| Mikhail Khludnev | `mkhl` | 1 | 1 | Person |
| Ravish Bhagdev | `ravish` | 1 | 1 | Person |
| Robert de Lorimier | `robert.c.delorimier@macys.com` | 1 | 1 | Person |
| Sumit Sen | `probashi55` | 1 | 1 | Person |

## Fresh watch-list comments

These comments were created on 2026-10-10 UTC. Excerpts are from Jira; candidate highlights exclude Nick-authored comments.

| Person | Ticket and time | Comment |
| --- | --- | --- |
| David Smiley | [SOLR-17916](https://issues.apache.org/jira/browse/SOLR-17916?focusedCommentId=18125720), `2026-10-10T02:04:58.540+0000` | “I agree Jan that this issue deserved a changelog and wasn't properly closed. I'll at least resolve it now.” |
| David Smiley | [SOLR-17916](https://issues.apache.org/jira/browse/SOLR-17916?focusedCommentId=18125723), `2026-10-10T02:24:22.027+0000` | “Meanwhile [~kotman12] in SOLR-18480 sees value in calling {{abort}} to efficiently/promptly close the SolrClient instead of hanging for a minute if a request is stuck.” |
| Eric Pugh | [SOLR-18462](https://issues.apache.org/jira/browse/SOLR-18462?focusedCommentId=18125808), `2026-10-10T10:46:06.596+0000` | “This actually landed in the not linked [https://github.com/apache/solr/pull/4918] PR!” |
| Eric Pugh | [SOLR-13442](https://issues.apache.org/jira/browse/SOLR-13442?focusedCommentId=18125843), `2026-10-10T14:25:28.034+0000` | “Little hard to say exactly when this was done, but all the related sub tasks are completed!” |
| Eric Pugh | [SOLR-13687](https://issues.apache.org/jira/browse/SOLR-13687?focusedCommentId=18125844), `2026-10-10T14:27:49.144+0000` | “I believe this has been solved.. all the commands take in a solr url that can be anywhere... I'm going to close this, but please reopen if I am incoorect.” |
| Eric Pugh | [SOLR-13687](https://issues.apache.org/jira/browse/SOLR-13687?focusedCommentId=18125845), `2026-10-10T14:28:02.492+0000` | “Not sure when, but we've done a ton of owrk on the cli.” |

## Candidate work from the fresh comments

- **SOLR-17916, David Smiley:** One comment says the issue deserved a changelog and that he would resolve it; record this as a resolution intent, with no separate task claimed. A second comment points to SOLR-18480 and says `SolrClient.abort()` could close promptly when a request is stuck. Candidate only: check whether that existing issue already captures the suggestion and whether a separate follow-up is useful.
- **SOLR-18462, Eric Pugh:** He says the work landed in PR [#4918](https://github.com/apache/solr/pull/4918), which was not linked from the ticket. Candidate only: verify the PR-ticket relationship and, if it is the relevant change, prepare a staged question about whether the link should be recorded. No ticket edit was made.
- **SOLR-13442, Eric Pugh:** He says all related subtasks are complete. Candidate only: verify the parent and subtask states and whether a closure decision is still needed. No status transition was made.
- **SOLR-13687, Eric Pugh:** He says the commands now accept a Solr URL and that he intends to close the issue, while inviting reopening if that is incorrect. Record as a resolution/status signal; no separate candidate from this comment.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. Only SOLR-18462, SOLR-17916, SOLR-13687, and SOLR-13442 had fresh watch-list activity.

| Ticket | Comments | Fresh watch-list activity | Disposition |
| --- | ---: | --- | --- |
| [SOLR-18532](https://issues.apache.org/jira/browse/SOLR-18532) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18531](https://issues.apache.org/jira/browse/SOLR-18531) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18530](https://issues.apache.org/jira/browse/SOLR-18530) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18528](https://issues.apache.org/jira/browse/SOLR-18528) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18527](https://issues.apache.org/jira/browse/SOLR-18527) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18508](https://issues.apache.org/jira/browse/SOLR-18508) | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18462](https://issues.apache.org/jira/browse/SOLR-18462) | 1 | Eric Pugh | Eric Pugh says work landed in PR #4918; verify ticket linkage before any update. |
| [SOLR-17916](https://issues.apache.org/jira/browse/SOLR-17916) | 13 | David Smiley | David Smiley states he will resolve the issue; second comment points to a possible SOLR-18480 follow-up, candidate only. |
| [SOLR-17819](https://issues.apache.org/jira/browse/SOLR-17819) | 7 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-13687](https://issues.apache.org/jira/browse/SOLR-13687) | 7 | Eric Pugh | Eric Pugh says commands accept a Solr URL and intends to close; status signal, no separate candidate. |
| [SOLR-13442](https://issues.apache.org/jira/browse/SOLR-13442) | 5 | Eric Pugh | Eric Pugh says related subtasks are complete; verify parent status before any closure discussion. |
| [SOLR-9578](https://issues.apache.org/jira/browse/SOLR-9578) | 6 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-6973](https://issues.apache.org/jira/browse/SOLR-6973) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-1834](https://issues.apache.org/jira/browse/SOLR-1834) | 9 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

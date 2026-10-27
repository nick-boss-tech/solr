# Jira day scan receipt: 2026-10-24 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `sweep`). Claim started at `2026-10-27T01:09:59Z`; the complete rebuild finished at `2026-10-27T01:14:20Z` after Nick directed, “Resume Oct 24.” The earlier failure record is preserved unchanged.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-10-24%22+AND+updated+%3C+%222026-10-25%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-10-24" AND updated < "2026-10-25"`. Search pagination: one page, 17 of 17 tickets. All returned `updated` timestamps fall on 2026-10-24 UTC.
- Cross-check: a broader adjacent-date query, `project = SOLR AND updated >= "2026-10-23" AND updated < "2026-10-26"`, returned 59 tickets; filtering those returned `updated` timestamps to 2026-10-24 UTC produced the same 17 tickets. The assignment cites an earlier phone-agent sweep of 24 tickets, but does not provide its key list. That 24-versus-17 difference remains unexplained. The specified Jira query and the broader UTC-filtered query agree, so the 17 Jira results are used as the scan source rather than substituting the sweep.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 17 comment histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching. The successful rebuild retrieved 141 comments across the 17 tickets and 19 commenter accounts: 18 people plus the `jira-bot` automation account.

## Commenter table

Counts cover all comments in the histories of the 17 tickets, including older comments. `jira-bot` is automation, not a person.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 60 | 6 | Automation |
| jefferyyuan | `yuanyun.cn` | 15 | 1 | Person |
| Andrzej Bialecki | `ab` | 14 | 1 | Person |
| Nick Shanin | `nickshanin` | 9 | 7 | Person |
| Ishan Chattopadhyaya | `ichattopadhyaya` | 8 | 3 | Person |
| Mark Miller | `markrmiller@gmail.com` | 7 | 1 | Person |
| David Smiley | `dsmiley` | 5 | 3 | Person |
| Jason Gerlowski | `gerlowskija` | 4 | 2 | Person |
| Mikhail Khludnev | `mkhl` | 4 | 1 | Person |
| Jan Høydahl | `janhoy` | 3 | 3 | Person |
| Chan Chan | `chandx` | 2 | 1 | Person |
| Hitesh Khamesra | `hiteshkhamesra` | 2 | 1 | Person |
| Matthew Biscocho | `mlbiscoc` | 2 | 2 | Person |
| Aditya Deshpande | `aditya.deshpande` | 1 | 1 | Person |
| Amrit Sarkar | `sarkaramrit2@gmail.com` | 1 | 1 | Person |
| Anshum Gupta | `anshum` | 1 | 1 | Person |
| Cassandra Targett | `ctargett` | 1 | 1 | Person |
| Chris M. Hostetter | `hossman` | 1 | 1 | Person |
| Gus Heck | `gus` | 1 | 1 | Person |

## Fresh watch-list comments

These are the only comments by the approved watch-list accounts created on 2026-10-24 UTC. Freshness is determined by each comment's `created` timestamp, not the ticket's `updated` value.

| Person | Ticket and time | Comment |
| --- | --- | --- |
| Jan Høydahl | [SOLR-18533](https://issues.apache.org/jira/browse/SOLR-18533?focusedCommentId=18131420), `2026-10-24T14:16:50.547+0000` | “Merged to main branch only. Tagging with 11.0 This cannot enter a release branch until also the remaining JIRAs for dashboards, alerts and OTLP are completed. Will likely happen before 11.0, so a coordinated backport of the entire grafana story to branch_10x will then happen.” |
| Jan Høydahl | [SOLR-18534](https://issues.apache.org/jira/browse/SOLR-18534?focusedCommentId=18131422), `2026-10-24T14:24:22.423+0000` | “The refguide is a stuf with dummy panels for now - feel free to do screenshots of the new panels and extend the refguide page !grafana-stub-refguide.png|width=800!” |

Eric Pugh (`epugh`), Christos Malliaridis (`malliaridis`), Jason Gerlowski (`gerlowskija`), and David Smiley (`dsmiley`) had no comments created on this date among these ticket histories. Their aggregate counts above include older comments where present.

## Per-ticket dispositions

“Comments” counts the entire retrieved comment history for that ticket. A dash under fresh activity means no watch-list comment was created on the scan date.

| Ticket | Comments | Fresh watch-list activity | Disposition |
| --- | ---: | --- | --- |
| [SOLR-18588](https://issues.apache.org/jira/browse/SOLR-18588) | 1 | None | No candidate from this scan. |
| [SOLR-18583](https://issues.apache.org/jira/browse/SOLR-18583) | 0 | None | No candidate from this scan. |
| [SOLR-18564](https://issues.apache.org/jira/browse/SOLR-18564) | 1 | None | No candidate from this scan. |
| [SOLR-18559](https://issues.apache.org/jira/browse/SOLR-18559) | 0 | None | No candidate from this scan. |
| [SOLR-18551](https://issues.apache.org/jira/browse/SOLR-18551) | 7 | None | No candidate from this scan. |
| [SOLR-18544](https://issues.apache.org/jira/browse/SOLR-18544) | 3 | None | No candidate from this scan. |
| [SOLR-18541](https://issues.apache.org/jira/browse/SOLR-18541) | 3 | None | No candidate from this scan. |
| [SOLR-18534](https://issues.apache.org/jira/browse/SOLR-18534) | 1 | Jan Høydahl, 1 | Candidate for evidence-first follow-up: check which Ref Guide panels remain placeholders and what new panels need screenshots, then ask whether a scoped screenshot/documentation update is the desired next step. No source or documentation check was performed in this scan. |
| [SOLR-18533](https://issues.apache.org/jira/browse/SOLR-18533) | 2 | Jan Høydahl, 1 | Status recorded: main-only merge, tagged for 11.0, with a coordinated 10.x backport conditional on the remaining dashboard, alert, and OTLP tickets. No separate immediate candidate. |
| [SOLR-18458](https://issues.apache.org/jira/browse/SOLR-18458) | 1 | None | No candidate from this scan. |
| [SOLR-18402](https://issues.apache.org/jira/browse/SOLR-18402) | 2 | None | No candidate from this scan. |
| [SOLR-18341](https://issues.apache.org/jira/browse/SOLR-18341) | 0 | None | No candidate from this scan. |
| [SOLR-17840](https://issues.apache.org/jira/browse/SOLR-17840) | 4 | None | No candidate from this scan. |
| [SOLR-16450](https://issues.apache.org/jira/browse/SOLR-16450) | 13 | None | No candidate from this scan. |
| [SOLR-16396](https://issues.apache.org/jira/browse/SOLR-16396) | 23 | None | No candidate from this scan. |
| [SOLR-13764](https://issues.apache.org/jira/browse/SOLR-13764) | 10 | None | No candidate from this scan. |
| [SOLR-12833](https://issues.apache.org/jira/browse/SOLR-12833) | 70 | None | No candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The receipt and JSON extracts are observational only; no ticket, branch, or broad BATS assertion-hardening gate status is changed by this scan.

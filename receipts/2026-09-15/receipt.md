# Jira day scan receipt: 2026-09-15 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-16. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-15%22+AND+updated+%3C+%222026-09-16%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-15" AND updated < "2026-09-16"`. One search page returned all 4 tickets; each `updated` timestamp falls on 2026-09-15 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 4 histories were fetched serially.
- Scope totals: 4 SOLR tickets; 11 comments across their complete histories; 6 commenter accounts. There were no fresh watch-list comments on 2026-09-15. Nick Shanin did not appear in these histories.

## Commenter table

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| Trey Cahill | `cahilltr` | 4 | 1 | Commenter account |
| Erick Erickson | `erickerickson` | 2 | 1 | Commenter account |
| Shawn Heisey | `elyograg` | 2 | 1 | Commenter account |
| David Smiley | `dsmiley` | 1 | 1 | Commenter account |
| Michael Braun | `mbraun688` | 1 | 1 | Commenter account |
| Varun Thacker | `varun` | 1 | 1 | Commenter account |

## Fresh watch-list comments

None. The scanner found no watch-list comments created on 2026-09-15.

## Candidate work from fresh comments

No candidate: there was no fresh watch-list activity on this date.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. No ticket had a fresh watch-list comment on this date.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18447](https://issues.apache.org/jira/browse/SOLR-18447) - Normalize and use dot product similarity in place of cosine similarity for vectors | `2026-09-15T18:44:37.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18070](https://issues.apache.org/jira/browse/SOLR-18070) - Add changelog checking to smoke tester | `2026-09-15T00:18:14.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17992](https://issues.apache.org/jira/browse/SOLR-17992) - Sub project web site for Solr MCP | `2026-09-15T10:29:53.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-12672](https://issues.apache.org/jira/browse/SOLR-12672) - Implement Synchronized Disruption into Solr | `2026-09-15T19:23:00.000+0000` | 11 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

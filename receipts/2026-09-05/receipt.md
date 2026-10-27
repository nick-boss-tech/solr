# Jira day scan receipt: 2026-09-05 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-06. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-05%22+AND+updated+%3C+%222026-09-06%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-05" AND updated < "2026-09-06"`. One search page returned all 2 tickets; each `updated` timestamp falls on 2026-09-05 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. Both histories were fetched serially.
- Scope totals: 2 SOLR tickets; 1 comment across their complete histories; 1 commenter account. There were no fresh watch-list comments created on 2026-09-05. Nick Shanin did not appear in these histories.

## Commenter table

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| David Smiley | `dsmiley` | 1 | 1 | Commenter account |

## Fresh watch-list comments

None. The one historical comment was not created on the scan day by a watch-list commenter.

## Candidate work from fresh comments

No candidate: there was no fresh watch-list activity on this date.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18379](https://issues.apache.org/jira/browse/SOLR-18379) - Remove DocsStreamer.KNOWN_TYPES | `2026-09-05T12:58:28.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18169](https://issues.apache.org/jira/browse/SOLR-18169) - The slim image shouldn't have examples | `2026-09-05T08:43:17.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

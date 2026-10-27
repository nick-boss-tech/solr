# Jira day scan receipt: 2026-09-21 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-22. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-21%22+AND+updated+%3C+%222026-09-22%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-21" AND updated < "2026-09-22"`. One search page returned the sole ticket; its `updated` timestamp falls on 2026-09-21 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. The complete history for the ticket was fetched serially.
- Scope totals: 1 SOLR ticket; 9 comments across its complete history; 7 commenter accounts. There were no fresh watch-list comments on 2026-09-21. Nick Shanin did not appear in this history.

## Commenter table

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| David Smiley | `dsmiley` | 3 | 1 | Commenter account |
| Adrien Grand | `jpountz` | 1 | 1 | Commenter account |
| Jan Høydahl | `janhoy` | 1 | 1 | Commenter account |
| Pieter | `boxtelp` | 1 | 1 | Commenter account |
| Toke Eskildsen | `toke` | 1 | 1 | Commenter account |
| Tomas Eduardo Fernandez Lobbe | `tflobbe` | 1 | 1 | Commenter account |
| Yonik Seeley | `yseeley@gmail.com` | 1 | 1 | Commenter account |

## Fresh watch-list comments

None. The scanner found no watch-list comments created on 2026-09-21.

## Candidate work from fresh comments

No candidate: there was no fresh watch-list activity on this date.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for this ticket. It had no fresh watch-list comment on this date.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-12074](https://issues.apache.org/jira/browse/SOLR-12074) - Add numeric typed equivalents to StrField | `2026-09-21T00:20:07.000+0000` | 9 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

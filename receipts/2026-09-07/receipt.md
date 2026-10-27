# Jira day scan receipt: 2026-09-07 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-08. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-07%22+AND+updated+%3C+%222026-09-08%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-07" AND updated < "2026-09-08"`. One search page returned all 3 tickets; each `updated` timestamp falls on 2026-09-07 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 3 histories were fetched serially.
- Scope totals: 3 SOLR tickets; 9 comments across their complete histories; 1 commenter account. That account is the ASF automation bot; no human commenters appeared. There were no fresh watch-list comments on 2026-09-07. Nick Shanin did not appear in these histories.

## Commenter table

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 9 | 3 | Automation |

## Fresh watch-list comments

None. The scanner found no watch-list comments created on 2026-09-07.

## Candidate work from fresh comments

No candidate: there was no fresh watch-list activity on this date.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18410](https://issues.apache.org/jira/browse/SOLR-18410) - CrossDC Common: multivalued params and per-entry params don't survive the serialization round-trip | `2026-09-07T22:11:11.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18381](https://issues.apache.org/jira/browse/SOLR-18381) - Remove ClusterState.createFromCollectionMap/getReplicaNamesPerCollectionOnNode | `2026-09-07T22:01:49.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18152](https://issues.apache.org/jira/browse/SOLR-18152) - Cosmetic improvements and migration to JAX-RS (v2 schemadesigner API) | `2026-09-07T21:55:56.000+0000` | 5 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

# Jira day scan receipt: 2026-10-01 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-10-02. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-10-01%22+AND+updated+%3C+%222026-10-02%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-10-01" AND updated < "2026-10-02"`. One search page returned all 8 tickets; every `updated` timestamp falls on 2026-10-01 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 8 histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 8 SOLR tickets; 10 comments across their complete histories; 5 commenter accounts. There were no fresh watch-list comments on 2026-10-01. Nick Shanin’s four historical comments across three tickets remain in the aggregate only and are excluded from candidate highlights.

## Commenter table

`jira-bot` is marked as automation, not a person. Nick Shanin is labeled self and included for aggregate accounting only.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| Nick Shanin | `nickshanin` | 4 | 3 | Self, aggregate only |
| ASF subversion and git services | `jira-bot` | 3 | 1 | Automation |
| Andrzej Bialecki | `ab` | 1 | 1 | Commenter account |
| Anshum Gupta | `anshum` | 1 | 1 | Commenter account |
| Marc Byrd | `cloudmarc` | 1 | 1 | Commenter account |

## Fresh watch-list comments

None. The scanner found no watch-list comments created on 2026-10-01.

## Candidate work from fresh comments

No candidate: there was no fresh watch-list activity on this date.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. None had a fresh watch-list comment on this date.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18492](https://issues.apache.org/jira/browse/SOLR-18492) - V2HttpCall never retries on stale cluster state - RETRY action silently overwritten to ADMIN | `2026-10-01T17:53:15.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18490](https://issues.apache.org/jira/browse/SOLR-18490) - Upgrade netty to 4.2.18 or later to fix CVE-2026-56818, CVE-2026-76816, CVE-2026-75595 | `2026-10-01T05:02:13.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18489](https://issues.apache.org/jira/browse/SOLR-18489) - Upgrade jackson jars to 2.22.3 or latest to address CVE-2026-91776, CVE-2026-91777, CVE-2026-89407 | `2026-10-01T04:56:29.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18465](https://issues.apache.org/jira/browse/SOLR-18465) - Spatial page has mis formatted curl commands. | `2026-10-01T13:53:03.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18263](https://issues.apache.org/jira/browse/SOLR-18263) - Improve LukeRequestHandlerDistribTest Performance | `2026-10-01T21:54:23.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18134](https://issues.apache.org/jira/browse/SOLR-18134) - ancestor_path field type no longer works; Solr-10 upgrade notes suggested change also does not work | `2026-10-01T21:47:09.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18129](https://issues.apache.org/jira/browse/SOLR-18129) - update-requesthandler API broken | `2026-10-01T22:37:29.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17539](https://issues.apache.org/jira/browse/SOLR-17539) - Increased memory usage after upgrading to Solr 9.6.0 - DocValuesIteratorCache | `2026-10-01T22:36:58.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

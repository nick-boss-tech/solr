# Jira day scan receipt: 2026-08-25 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-08-26. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-08-25%22+AND+updated+%3C+%222026-08-26%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-08-25" AND updated < "2026-08-26"`. One search page returned 10 SOLR tickets updated on 2026-08-25 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; all 10 histories were fetched serially.
- Scope totals: 10 tickets, 64 comments across their complete histories, 15 commenter accounts, and 2 fresh watch-list comments, both by Eric Pugh. No `nickshanin` account comments appear; all commenters and comments are included in full-history totals.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 15 | 6 | Automation |
| Eric Pugh | `epugh` | 7 | 3 | Commenter account |
| Jan Høydahl | `janhoy` | 7 | 3 | Commenter account |
| Alan Woodward | `romseygeek` | 6 | 1 | Commenter account |
| Scott Blum | `dragonsinth` | 6 | 1 | Commenter account |
| Upayavira | `upayavira` | 5 | 2 | Commenter account |
| Houston Putman | `houston` | 4 | 2 | Commenter account |
| David Smiley | `dsmiley` | 3 | 2 | Commenter account |
| Keith Laban | `k317h` | 3 | 1 | Commenter account |
| Alexandre Rafalovitch | `arafalov` | 2 | 1 | Commenter account |
| Erick Erickson | `erickerickson` | 2 | 1 | Commenter account |
| Alexander Veit | `veita` | 1 | 1 | Commenter account |
| ASF GitHub Bot | `githubbot` | 1 | 1 | Automation |
| Michael Riedel | `meeque` | 1 | 1 | Commenter account |
| Shawn Heisey | `elyograg` | 1 | 1 | Commenter account |
## Fresh watch-list comments
| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| Eric Pugh | [SOLR-16309](https://issues.apache.org/jira/browse/SOLR-16309?focusedCommentId=18108077), `2026-08-25T20:13:36.403+0000` | “updated dependency” |
| Eric Pugh | [SOLR-12196](https://issues.apache.org/jira/browse/SOLR-12196?focusedCommentId=18108084), `2026-08-25T20:36:31.746+0000` | “We have a new kotlin based ‘Solr UI’ in the works.” |
## Candidate work from fresh comments
No candidate. [SOLR-16309](https://issues.apache.org/jira/browse/SOLR-16309?focusedCommentId=18108077) is Resolved / Fixed, with Eric’s comment reporting the dependency update. [SOLR-12196](https://issues.apache.org/jira/browse/SOLR-12196?focusedCommentId=18108084) is Resolved / Abandoned; the Kotlin-based Solr UI provides the context for leaving the Angular migration path behind. Neither fresh comment identifies unresolved follow-up.
## Per-ticket dispositions
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18383](https://issues.apache.org/jira/browse/SOLR-18383) - Remove CollectionParams.FROM_NODE | `2026-08-25T21:06:47.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18374](https://issues.apache.org/jira/browse/SOLR-18374) - Remove NamedList.NamedListEntry and MapWriterMap | `2026-08-25T15:07:07.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18371](https://issues.apache.org/jira/browse/SOLR-18371) - Remove deprecated pre-QueryCommand SolrIndexSearcher methods | `2026-08-25T15:07:06.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18355](https://issues.apache.org/jira/browse/SOLR-18355) - Remove GeohashFunction and GeohashHaversineFunction | `2026-08-25T01:24:00.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17826](https://issues.apache.org/jira/browse/SOLR-17826) - Upgrade Netty to fix CVE-2025-24970 in Solr 9.8.1 (or previous) | `2026-08-25T17:45:42.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-16309](https://issues.apache.org/jira/browse/SOLR-16309) - Upgrade vulnerable jQuery UI to version 1.13.2 | `2026-08-25T20:13:36.000+0000` | 9 | Eric Pugh: dependency updated | Resolved / Fixed; no additional follow-up identified. |
| [SOLR-14389](https://issues.apache.org/jira/browse/SOLR-14389) - Support dynamic reconfiguration of Zookeeper ensemble | `2026-08-25T18:32:10.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-12196](https://issues.apache.org/jira/browse/SOLR-12196) - Prepare Admin UI for migrating to Angular.io | `2026-08-25T20:36:31.000+0000` | 9 | Eric Pugh: Kotlin-based Solr UI in development | Resolved / Abandoned; no additional follow-up identified. |
| [SOLR-9659](https://issues.apache.org/jira/browse/SOLR-9659) - Add zookeeper DataWatch API | `2026-08-25T18:20:25.000+0000` | 21 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-8474](https://issues.apache.org/jira/browse/SOLR-8474) - Test Framework for functional testing Angular UI | `2026-08-25T15:07:08.000+0000` | 12 | None | No fresh watch-list comment; no candidate from this scan. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

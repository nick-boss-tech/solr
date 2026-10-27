# Jira day scan receipt: 2026-08-21 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-08-22. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-08-21%22+AND+updated+%3C+%222026-08-22%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-08-21" AND updated < "2026-08-22"`. One search page returned 16 SOLR tickets updated on 2026-08-21 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; all 16 histories were fetched serially.
- Scope totals: 16 tickets, 232 comments across their complete histories, 25 commenter accounts, and 1 fresh watch-list comment by Jan Høydahl. No `nickshanin` account comments appear; all comments and accounts are retained in the full-history totals.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 54 | 15 | Automation |
| Ishan Chattopadhyaya | `ichattopadhyaya` | 42 | 6 | Commenter account |
| Eric Pugh | `epugh` | 26 | 4 | Commenter account |
| Jan Høydahl | `janhoy` | 18 | 8 | Commenter account |
| David Smiley | `dsmiley` | 13 | 4 | Commenter account |
| Cassandra Targett | `ctargett` | 12 | 4 | Commenter account |
| Tim Allison | `tallison` | 8 | 1 | Commenter account |
| Alexandre Rafalovitch | `arafalov` | 5 | 2 | Commenter account |
| Kevin Risden | `krisden` | 5 | 1 | Commenter account |
| Markus Kalkbrenner | `mkalkbrenner` | 5 | 1 | Commenter account |
| Noble Paul | `noble.paul` | 5 | 3 | Commenter account |
| Robert Muir | `rcmuir` | 5 | 2 | Commenter account |
| Tomas Eduardo Fernandez Lobbe | `tflobbe` | 5 | 3 | Commenter account |
| Erick Erickson | `erickerickson` | 4 | 2 | Commenter account |
| Ilan Ginzburg | `ilan` | 4 | 1 | Commenter account |
| Andrzej Bialecki | `ab` | 3 | 1 | Commenter account |
| Anshum Gupta | `anshum` | 3 | 2 | Commenter account |
| Atri Sharma | `atri` | 3 | 3 | Commenter account |
| Christos Malliaridis | `malliaridis` | 3 | 1 | Commenter account |
| Chris M. Hostetter | `hossman` | 2 | 1 | Commenter account |
| Christine Poerschke | `cpoerschke` | 2 | 1 | Commenter account |
| Walter Underwood | `wunder` | 2 | 1 | Commenter account |
| Andrew | `AndrewGr` | 1 | 1 | Commenter account |
| Houston Putman | `houston` | 1 | 1 | Commenter account |
| Mike Drob | `mdrob` | 1 | 1 | Commenter account |
## Fresh watch-list comment
| Person | Ticket and time | Comment summary |
| --- | --- | --- |
| Jan Høydahl | [SOLR-15929](https://issues.apache.org/jira/browse/SOLR-15929?focusedCommentId=18106797), `2026-08-21T22:11:03.131+0000` | Jan says he intends to work on ASF-compliant LICENSE/NOTICE cleanup with AI assistance. Phase 1 is a Confluence design proposal and discussion until the approach is agreed; Phase 2 is a draft PR demonstrating the mechanism and validating distribution-specific LICENSE files. [Design document](https://cwiki.apache.org/confluence/spaces/SOLR/pages/449282855/SOLR-15929+ASF+compliant+NOTICE+and+LICENSE+files+in+dist). |
## Candidate work from fresh comments
Candidate only — not claimed: review the proposed approach in the [design document](https://cwiki.apache.org/confluence/spaces/SOLR/pages/449282855/SOLR-15929+ASF+compliant+NOTICE+and+LICENSE+files+in+dist) and help resolve any open design questions before the draft PR. [SOLR-15929](https://issues.apache.org/jira/browse/SOLR-15929?focusedCommentId=18106797) remains Open / Unresolved and assigned to Jan Høydahl, who states that he intends to tackle the work. No implementation work was claimed, and no request was posted.
## Per-ticket dispositions
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18389](https://issues.apache.org/jira/browse/SOLR-18389) - Remove SolrCore.getAdminResponseWriter | `2026-08-21T17:18:43.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18388](https://issues.apache.org/jira/browse/SOLR-18388) - Remove ZkStateReader.getCollectionPathRoot/getCollectionPath | `2026-08-21T17:18:43.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18386](https://issues.apache.org/jira/browse/SOLR-18386) - Remove Utils.getBaseUrlForNodeName(String, String) overloads | `2026-08-21T17:18:43.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18385](https://issues.apache.org/jira/browse/SOLR-18385) - Remove CollectionAdminRequest.PROPERTY_PREFIX | `2026-08-21T17:18:43.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18384](https://issues.apache.org/jira/browse/SOLR-18384) - Remove ShardParams.SHARD_URL | `2026-08-21T17:18:42.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18377](https://issues.apache.org/jira/browse/SOLR-18377) - Remove DenseVectorField.getHnswMaxConn/getHnswBeamWidth | `2026-08-21T17:18:43.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18375](https://issues.apache.org/jira/browse/SOLR-18375) - Remove zkHost-based streaming connection API | `2026-08-21T17:18:43.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-15929](https://issues.apache.org/jira/browse/SOLR-15929?focusedCommentId=18106797) - Clean up NOTICE and LICENSE files for Solr | `2026-08-21T22:11:09.000+0000` | 8 | Jan Høydahl: staged design/discussion followed by draft PR | Open / Unresolved; one candidate-only design-review follow-up. |
| [SOLR-14792](https://issues.apache.org/jira/browse/SOLR-14792) - Remove VelocityResponseWriter from Solr 9 | `2026-08-21T13:25:18.000+0000` | 12 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-14783](https://issues.apache.org/jira/browse/SOLR-14783) - Remove DIH from 9.0 | `2026-08-21T13:25:20.000+0000` | 15 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-14656](https://issues.apache.org/jira/browse/SOLR-14656) - Deprecate current autoscaling framework, remove from master | `2026-08-21T13:25:18.000+0000` | 37 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-14654](https://issues.apache.org/jira/browse/SOLR-14654) - Remove plugin loading from .system collection (for 9.0) | `2026-08-21T13:25:18.000+0000` | 19 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-14616](https://issues.apache.org/jira/browse/SOLR-14616) - Remove CDCR from 9.0 | `2026-08-21T13:25:21.000+0000` | 20 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-14067](https://issues.apache.org/jira/browse/SOLR-14067) - Move StatelessScriptUpdateProcessor to a contrib | `2026-08-21T13:25:20.000+0000` | 49 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-13989](https://issues.apache.org/jira/browse/SOLR-13989) - Migrate hadoop authentication code to a module | `2026-08-21T13:25:19.000+0000` | 16 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-13973](https://issues.apache.org/jira/browse/SOLR-13973) - Deprecate In Process Tika in favour of Loosely Coupled Tika | `2026-08-21T13:25:21.000+0000` | 42 | None | No fresh watch-list comment; no candidate from this scan. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

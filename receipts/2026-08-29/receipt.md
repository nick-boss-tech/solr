# Jira day scan receipt: 2026-08-29 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-08-30. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-08-29%22+AND+updated+%3C+%222026-08-30%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-08-29" AND updated < "2026-08-30"`. One search page returned 9 SOLR tickets updated on 2026-08-29 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; all 9 histories were fetched serially.
- Scope totals: 9 tickets, 40 comments across their complete histories, 6 commenter accounts, and 1 fresh watch-list comment. No `nickshanin` account comments appear; all commenter accounts and comments are included in the full-history totals.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 22 | 8 | Automation |
| David Smiley | `dsmiley` | 6 | 2 | Commenter account |
| Thomas Wöckinger | `thomas.woeckinger` | 6 | 1 | Commenter account |
| Eric Pugh | `epugh` | 2 | 2 | Commenter account |
| Gaël Jourdan | `gjourdan` | 2 | 1 | Commenter account |
| Meryem Gülbudak | `meryemgulbudak` | 2 | 1 | Commenter account |
## Fresh watch-list comment
| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| David Smiley | [SOLR-17871](https://issues.apache.org/jira/browse/SOLR-17871?focusedCommentId=18109347), `2026-08-29T03:14:09.401+0000` | “Indeed it's in 9.10 (and all later versions) and there is no 9.9.1 (at least not yet and I don't expect there to be one). Sometimes we backport to a release branch in anticipation of the possibility of a bugfix release that never ends up happening.” |
## Candidate work from fresh comments
No follow-up candidate. David’s comment explains release-branch/backport context; the [Jira issue page](https://issues.apache.org/jira/browse/SOLR-17871) marks SOLR-17871 Resolved / Fixed, and the Aug 29 activity records the fix version changing to 9.10. The comment is a clarification, not an unresolved request.
## Per-ticket dispositions
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18382](https://issues.apache.org/jira/browse/SOLR-18382) - Remove DocCollection.getReplicas() | `2026-08-29T15:51:03.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18380](https://issues.apache.org/jira/browse/SOLR-18380) - Remove CloudSolrClient connect()/legacy Builder constructor | `2026-08-29T15:51:30.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18378](https://issues.apache.org/jira/browse/SOLR-18378) - Remove CoreContainer.getCores()/SolrCores.getCores() | `2026-08-29T15:52:12.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18373](https://issues.apache.org/jira/browse/SOLR-18373) - Remove NamedList.asShallowMap()/get(String,int) and SolrParams.toNamedList() | `2026-08-29T15:52:44.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18360](https://issues.apache.org/jira/browse/SOLR-18360) - Remove HttpJettySolrClient.addListenerFactory(HttpListenerFactory) | `2026-08-29T15:54:10.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18357](https://issues.apache.org/jira/browse/SOLR-18357) - Remove SolrTestCaseJ4 systemSetPropertyEnableUrlAllowList/systemClearPropertySolrEnableUrlAllowList | `2026-08-29T15:40:16.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18352](https://issues.apache.org/jira/browse/SOLR-18352) - Improve DX by more accurately communicating deprecated code status | `2026-08-29T15:42:14.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17871](https://issues.apache.org/jira/browse/SOLR-17871) - Http2SolrClient does not use specified idle timeout | `2026-08-29T03:14:09.000+0000` | 17 | David Smiley: release-version/backport clarification | Issue is Resolved / Fixed; the fresh comment is explanatory, with no additional follow-up identified. |
| [SOLR-17559](https://issues.apache.org/jira/browse/SOLR-17559) - DocCollection should be fully immutable. | `2026-08-29T00:15:53.000+0000` | 5 | None | No fresh watch-list comment; no candidate from this scan. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

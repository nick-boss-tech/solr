# Jira day scan receipt: 2026-08-27 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-08-28. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-08-27%22+AND+updated+%3C+%222026-08-28%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-08-27" AND updated < "2026-08-28"`. One search page returned 10 SOLR tickets updated on 2026-08-27 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; all 10 histories were fetched serially.
- Scope totals: 10 tickets, 44 comments across their complete histories, 14 commenter accounts, and 4 fresh watch-list comments, all by Eric Pugh. No `nickshanin` account comments appear; all commenter accounts and comments are included in full-history totals.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 13 | 5 | Automation |
| Chris M. Hostetter | `hossman` | 10 | 3 | Commenter account |
| Eric Pugh | `epugh` | 4 | 4 | Commenter account |
| J.J. Larrea | `skeptikos` | 3 | 1 | Commenter account |
| David Smiley | `dsmiley` | 2 | 2 | Commenter account |
| Jan Høydahl | `janhoy` | 2 | 2 | Commenter account |
| Robert Muir | `rcmuir` | 2 | 1 | Commenter account |
| Ryan McKinley | `ryantxu` | 2 | 1 | Commenter account |
| Alexandre Rafalovitch | `arafalov` | 1 | 1 | Commenter account |
| Bernd Wahlen | `bwahlen` | 1 | 1 | Commenter account |
| Erick Erickson | `erickerickson` | 1 | 1 | Commenter account |
| Steven Rowe | `sarowe` | 1 | 1 | Commenter account |
| Uwe Schindler | `uschindler` | 1 | 1 | Commenter account |
| Yonik Seeley | `yseeley@gmail.com` | 1 | 1 | Commenter account |
## Fresh watch-list comments
| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| Eric Pugh | [SOLR-112](https://issues.apache.org/jira/browse/SOLR-112?focusedCommentId=18108587), `2026-08-27T01:10:04.479+0000` | “Closing as obsolete. This need is now met by InitParams … the same problem this ticket describes. Please do reopen if you are still interested.” |
| Eric Pugh | [SOLR-416](https://issues.apache.org/jira/browse/SOLR-416?focusedCommentId=18108588), `2026-08-27T01:11:47.269+0000` | “Closing as not a problem. A full search … finds zero remaining no-arg .toUpperCase()/.toLowerCase() calls … the build now enforces this permanently via the forbidden-apis Gradle plugin …” |
| Eric Pugh | [SOLR-422](https://issues.apache.org/jira/browse/SOLR-422?focusedCommentId=18108589), `2026-08-27T01:12:24.936+0000` | “Closing as fixed. Classic DisMax now runs the query through SolrPluginUtils.stripUnbalancedQuotes() … Extended DisMax … retries the parse with ignoreQuote=true …” |
| Eric Pugh | [SOLR-18325](https://issues.apache.org/jira/browse/SOLR-18325?focusedCommentId=18108756), `2026-08-27T09:37:08.855+0000` | “I was just looking at this … seems like something to be done. I don't know how big the impact is to consumers? It may be a Solr 11 thing? It feels like it would be too much to do in Solr 10?” |
## Candidate work from fresh comments
Candidate only — not claimed: [SOLR-18325](https://issues.apache.org/jira/browse/SOLR-18325) remains Open / Unresolved. Clarify the downstream impact and module scope of moving Solr from Jackson 2 to Jackson 3, then confirm the release boundary before implementation. Eric Pugh says consumer impact is unknown and suggests this may be too much for Solr 10; the issue description calls the package/coordinate break a natural fit for 11.0 and says the change must align across Solr modules. A focused impact estimate and confirmation of the 11.0 target would make the open scope actionable. No work was claimed.
The other three fresh comments are closure explanations, not new candidates: SOLR-112 is Resolved / Not a Problem because InitParams meets the need; SOLR-416 is Resolved / Not a Problem after the locale audit and permanent build check; SOLR-422 is Resolved / Fixed after the query parsing changes. Sources: [SOLR-112](https://issues.apache.org/jira/browse/SOLR-112?focusedCommentId=18108587), [SOLR-416](https://issues.apache.org/jira/browse/SOLR-416?focusedCommentId=18108588), [SOLR-422](https://issues.apache.org/jira/browse/SOLR-422?focusedCommentId=18108589).
## Per-ticket dispositions
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18396](https://issues.apache.org/jira/browse/SOLR-18396) - Figure out why Solr-Bot is adding '*Copy' configuration names to grade.lockfiles | `2026-08-27T15:43:49.000+0000` | 6 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18368](https://issues.apache.org/jira/browse/SOLR-18368) - CloudSolrClient.Builder.withInternalClientBuilder | `2026-08-27T15:42:56.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18359](https://issues.apache.org/jira/browse/SOLR-18359) - Remove CollectionAdminRequest.CreateTimeRoutedAlias.setMaxFutureMs(Integer) overload | `2026-08-27T09:52:55.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18354](https://issues.apache.org/jira/browse/SOLR-18354) - Remove SolrTestCaseJ4 ignoreException/unIgnoreException/resetExceptionIgnores | `2026-08-27T15:42:40.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18325](https://issues.apache.org/jira/browse/SOLR-18325) - Upgrade Jackson from 2.x to 3.x (tools.jackson) | `2026-08-27T09:37:08.000+0000` | 2 | Eric Pugh: consumer impact and Solr 10 vs 11 scope question | Open / Unresolved; candidate-only impact estimate and release-boundary confirmation. |
| [SOLR-18149](https://issues.apache.org/jira/browse/SOLR-18149) - Remove ClusterState.createFromJson | `2026-08-27T15:05:42.000+0000` | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18123](https://issues.apache.org/jira/browse/SOLR-18123) - Tests: Shouldn't need to refer to ALLOW_PATHS_SYSPROP, usually | `2026-08-27T01:22:00.000+0000` | 0 | None | No comments in history; no candidate from this scan. |
| [SOLR-422](https://issues.apache.org/jira/browse/SOLR-422) - one double quote or two double quotes only break search | `2026-08-27T01:12:24.000+0000` | 4 | Eric Pugh: close-as-fixed explanation | Resolved / Fixed; no additional follow-up identified. |
| [SOLR-416](https://issues.apache.org/jira/browse/SOLR-416) - need to audit all methods that might be using default Locale | `2026-08-27T01:11:47.000+0000` | 8 | Eric Pugh: locale audit and permanent build enforcement | Resolved / Not a Problem; no additional follow-up identified. |
| [SOLR-112](https://issues.apache.org/jira/browse/SOLR-112) - Hierarchical Handler Config | `2026-08-27T01:10:04.000+0000` | 12 | Eric Pugh: InitParams now meets the need | Resolved / Not a Problem; no additional follow-up identified. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

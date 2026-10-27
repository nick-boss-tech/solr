# Jira day scan receipt: 2026-08-11 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-08-12. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-08-11%22+AND+updated+%3C+%222026-08-12%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-08-11" AND updated < "2026-08-12"`. One search page returned 5 SOLR tickets updated on 2026-08-11 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; the 5 ticket histories were fetched serially.
- Scope totals: 5 tickets, 15 comments across their complete histories, 8 commenter accounts, and 5 fresh watch-list comments (4 from Eric Pugh, 1 from David Smiley). No `nickshanin` account comments appear; all 15 comments remain in the full-history totals.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| Eric Pugh | `epugh` | 4 | 4 | Person |
| Chris Male | `cmale` | 3 | 1 | Person |
| David Smiley | `dsmiley` | 2 | 1 | Person |
| Robert Muir | `rcmuir` | 2 | 1 | Person |
| Chris M. Hostetter | `hossman` | 1 | 1 | Person |
| Serhiy Bzhezytskyy | `serhiybzhezytskyy` | 1 | 1 | Person |
| Steven Rowe | `sarowe` | 1 | 1 | Person |
| Uwe Schindler | `uschindler` | 1 | 1 | Person |
## Fresh watch-list comments
| Person | Ticket and time | Comment excerpt and current status |
| --- | --- | --- |
| Eric Pugh | [SOLR-17918](https://issues.apache.org/jira/browse/SOLR-17918?focusedCommentId=18103707), `2026-08-11T14:49:14.348+0000` | “Duplicate of SOLR-17825.” SOLR-17918 is Resolved / Duplicate; the target [SOLR-17825](https://issues.apache.org/jira/browse/SOLR-17825) is Resolved / Fixed in 10.1. Settled. |
| David Smiley | [SOLR-9355](https://issues.apache.org/jira/browse/SOLR-9355?focusedCommentId=18103720), `2026-08-11T16:16:33.748+0000` (updated `17:02:12.245+0000`) | “Closing this after ten years. The behavior it describes no longer exists, and the component it names (CUSC) was never responsible for it.” The issue is Resolved / Not A Problem. David explicitly separates future improvements from this old issue; see the candidate note below. |
| Eric Pugh | [SOLR-2407](https://issues.apache.org/jira/browse/SOLR-2407?focusedCommentId=18103821), `2026-08-11T19:49:26.235+0000` | “easymock is no longer used, so we can close this.” The issue is Resolved / Not A Problem. Settled. |
| Eric Pugh | [SOLR-13959](https://issues.apache.org/jira/browse/SOLR-13959?focusedCommentId=18103822), `2026-08-11T19:50:52.645+0000` | “the codebase is currently on **log4j2 2.26.0**.” The issue is Resolved / Not A Problem. Settled. |
| Eric Pugh | [SOLR-18052](https://issues.apache.org/jira/browse/SOLR-18052?focusedCommentId=18103824), `2026-08-11T19:51:18.251+0000` | “This has been done!” The RefGuide task is Resolved / Not A Problem. Settled. |
## Candidate work from fresh comments
- Candidate only — not claimed, SOLR-9355 (David Smiley): confirm whether a separate issue already tracks the explicitly excluded `ClosedChannelException` retriability question; if not, ask whether to scope a separate investigation/test, without reopening or re-scoping SOLR-9355. David says this exception is currently retriable nowhere in Solr. The cause-chain-unrolling follow-up is already handled separately: [PR #4678](https://github.com/apache/solr/pull/4678) merged on 2026-08-16 under [SOLR-18346](https://issues.apache.org/jira/browse/SOLR-18346), which is Closed / Fixed; both explicitly leave `ClosedChannelException` retriability out of scope.
No candidate is proposed for the other four fresh comments; their tickets are duplicate/closed or otherwise settled. No new Jira issue was filed and no follow-up work was claimed.
## Per-ticket dispositions
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18052](https://issues.apache.org/jira/browse/SOLR-18052?focusedCommentId=18103824) - Complete Major changes and Upgrade Notes in RefGuide for 10.0.0 | `2026-08-11T19:51:18.000+0000` | 1 | Eric Pugh: completed | Resolved / Not A Problem; settled. |
| [SOLR-17918](https://issues.apache.org/jira/browse/SOLR-17918?focusedCommentId=18103707) - CVE-2025-48734: vulnerability in the commons-beanutils dependency | `2026-08-11T14:49:14.000+0000` | 1 | Eric Pugh: duplicate of SOLR-17825 | Resolved / Duplicate; target SOLR-17825 is Resolved / Fixed. |
| [SOLR-13959](https://issues.apache.org/jira/browse/SOLR-13959?focusedCommentId=18103822) - Upgrade log4j2 to the current version, presently 2.12.1 | `2026-08-11T19:50:52.000+0000` | 1 | Eric Pugh: codebase on Log4j2 2.26.0 | Resolved / Not A Problem; settled. |
| [SOLR-9355](https://issues.apache.org/jira/browse/SOLR-9355?focusedCommentId=18103720) - ConcurrentUpdateSolrClient does not retry sending docs to followers | `2026-08-11T17:02:12.000+0000` | 3 | David Smiley: stale behavior/incorrect component attribution; separate future improvements | Resolved / Not A Problem. Cause-chain follow-up moved to SOLR-18346 and merged PR #4678; only the separate ClosedChannelException gap remains a candidate to clarify. |
| [SOLR-2407](https://issues.apache.org/jira/browse/SOLR-2407?focusedCommentId=18103821) - investigate upgrading easymock to a newer version | `2026-08-11T19:49:26.000+0000` | 9 | Eric Pugh: EasyMock is no longer used | Resolved / Not A Problem; settled. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

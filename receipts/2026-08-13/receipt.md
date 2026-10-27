# Jira day scan receipt: 2026-08-13 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-08-14. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-08-13%22+AND+updated+%3C+%222026-08-14%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-08-13" AND updated < "2026-08-14"`. One search page returned 8 SOLR tickets updated on 2026-08-13 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; the 8 ticket histories were fetched serially.
- Scope totals: 8 tickets, 43 comments across their complete histories, 11 commenter accounts, and 4 fresh watch-list comments (3 from Eric Pugh, 1 from Jan Høydahl). No `nickshanin` account comments appear; all accounts and comments remain in the full-history totals.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| Eric Pugh | `epugh` | 8 | 4 | Person |
| ASF subversion and git services | `jira-bot` | 7 | 2 | Automation account |
| David Smiley | `dsmiley` | 7 | 3 | Person |
| Abhishek Umarjikar | `aumarjikar` | 4 | 1 | Person |
| Chris M. Hostetter | `hossman` | 4 | 1 | Person |
| Jeremy Branham | `jdbranham` | 4 | 1 | Person |
| Jan Høydahl | `janhoy` | 3 | 3 | Person |
| Upayavira | `upayavira` | 3 | 2 | Person |
| Jason Gerlowski | `gerlowskija` | 1 | 1 | Person |
| Mark Miller | `markrmiller@gmail.com` | 1 | 1 | Person |
| Rong Chen | `rong_chen_veeva` | 1 | 1 | Person |
## Fresh watch-list comments
| Person | Ticket and time | Comment |
| --- | --- | --- |
| Eric Pugh | [SOLR-15507](https://issues.apache.org/jira/browse/SOLR-15507?focusedCommentId=18104238), `2026-08-13T01:46:01.600+0000` | “Been fixed a long time ago.” The ticket is Resolved / Fixed, with fix version 8.11.1. |
| Eric Pugh | [SOLR-17236](https://issues.apache.org/jira/browse/SOLR-17236?focusedCommentId=18104241), `2026-08-13T01:55:06.441+0000` | “This was fixed in 8.7.” The ticket is Resolved / Fixed, with fix version 8.7. |
| Eric Pugh | [SOLR-10702](https://issues.apache.org/jira/browse/SOLR-10702?focusedCommentId=18104481), `2026-08-13T14:41:37.325+0000` | “Fixed a long time ago. VEX statement added.” The ticket is Resolved / Fixed, with fix version 9.2. |
| Jan Høydahl | [SOLR-8473](https://issues.apache.org/jira/browse/SOLR-8473?focusedCommentId=18104653), `2026-08-13T23:58:09.304+0000` | “This will likely not happen, it's enough with SOLR-8474 on this old UI.” SOLR-8473 is Resolved / Won’t Fix; the referenced [SOLR-8474](https://issues.apache.org/jira/browse/SOLR-8474) is Resolved / Fixed and now covers the old UI's functional test framework. |
## Candidate work from fresh comments
No candidate: all four fresh comments report settled fixes or explicitly rule out the old request in favor of the completed SOLR-8474 approach. No follow-up work was claimed.
## Per-ticket dispositions
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18256](https://issues.apache.org/jira/browse/SOLR-18256) - Auto-fix pure negative (NOT) clauses in the “lucene” QParser | `2026-08-13T16:20:59.000+0000` | 15 | None from the watch list | No fresh watch-list comment; no candidate. |
| [SOLR-18150](https://issues.apache.org/jira/browse/SOLR-18150) - Introduce SolrBackend abstraction for tests & benchmarks | `2026-08-13T16:15:30.000+0000` | 5 | None from the watch list | No fresh watch-list comment; no candidate. |
| [SOLR-17900](https://issues.apache.org/jira/browse/SOLR-17900) - Multiple CVEs in Apache Hadoop Client Runtime 3.4.0 dependency | `2026-08-13T01:34:15.000+0000` | 2 | None from the watch list | No fresh watch-list comment; no candidate. |
| [SOLR-17236](https://issues.apache.org/jira/browse/SOLR-17236?focusedCommentId=18104241) - Update jackson-databind for 8.11.x | `2026-08-13T01:55:06.000+0000` | 3 | Eric Pugh: fixed in 8.7 | Resolved / Fixed; settled. |
| [SOLR-15507](https://issues.apache.org/jira/browse/SOLR-15507?focusedCommentId=18104238) - Upgrade Apache Thrift to 0.14.1 | `2026-08-13T01:46:01.000+0000` | 2 | Eric Pugh: fixed long ago | Resolved / Fixed; settled. |
| [SOLR-13162](https://issues.apache.org/jira/browse/SOLR-13162) - Admin UI development-test cycle is slow | `2026-08-13T23:58:57.000+0000` | 7 | None from the watch list | No fresh watch-list comment; no candidate. |
| [SOLR-10702](https://issues.apache.org/jira/browse/SOLR-10702?focusedCommentId=18104481) - Woodstox API | `2026-08-13T14:41:37.000+0000` | 4 | Eric Pugh: fixed long ago; VEX statement added | Resolved / Fixed; settled. |
| [SOLR-8473](https://issues.apache.org/jira/browse/SOLR-8473?focusedCommentId=18104653) - Test Framework for Unit Testing Angular UI | `2026-08-13T23:58:09.000+0000` | 5 | Jan Høydahl: the old request likely will not happen; SOLR-8474 is sufficient | Resolved / Won’t Fix; superseded by the completed SOLR-8474 approach. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

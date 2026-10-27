# Jira day scan receipt: 2026-08-12 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-08-13. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-08-12%22+AND+updated+%3C+%222026-08-13%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-08-12" AND updated < "2026-08-13"`. One search page returned 6 SOLR tickets updated on 2026-08-12 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; the 6 ticket histories were fetched serially.
- Scope totals: 6 tickets, 41 comments across their complete histories, 12 commenter accounts, and 6 fresh watch-list comments (5 from Eric Pugh, 1 from David Smiley). No `nickshanin` account comments appear; full-history totals include all comments.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 7 | 2 | Automation account |
| David Smiley | `dsmiley` | 7 | 2 | Person |
| Eric Pugh | `epugh` | 7 | 6 | Person |
| Jan Høydahl | `janhoy` | 5 | 2 | Person |
| Cassandra Targett | `ctargett` | 3 | 2 | Person |
| Christos Malliaridis | `malliaridis` | 3 | 1 | Person |
| Ryan McKinley | `ryantxu` | 2 | 1 | Person |
| Steven Rowe | `sarowe` | 2 | 2 | Person |
| Uwe Schindler | `uschindler` | 2 | 2 | Person |
| Houston Putman | `houston` | 1 | 1 | Person |
| Sami Siren | `siren` | 1 | 1 | Person |
| Shawn Heisey | `elyograg` | 1 | 1 | Person |
## Fresh watch-list comments
| Person | Ticket and time | Comment summary and current status |
| --- | --- | --- |
| Eric Pugh | [SOLR-3958](https://issues.apache.org/jira/browse/SOLR-3958?focusedCommentId=18103882), `2026-08-12T01:10:45.244+0000` | “We don't have a ‘Overcome by time’ resolution ;)” The old warning request is Resolved / Not A Problem; settled. |
| Eric Pugh | [SOLR-11953](https://issues.apache.org/jira/browse/SOLR-11953?focusedCommentId=18103884), `2026-08-12T01:16:01.573+0000` | “Apparently we bring in JTS as part of SQL module!” The ticket remains Open / Unresolved. The current [SQL module lockfile](https://github.com/apache/solr/blob/main/solr/modules/sql/gradle.lockfile) lists `org.locationtech.jts:jts-core:1.19.0` in `runtimeClasspath` and `runtimeLibs`; this does not by itself establish inclusion in the standard Solr distribution. |
| Eric Pugh | [SOLR-15497](https://issues.apache.org/jira/browse/SOLR-15497?focusedCommentId=18103885), `2026-08-12T01:17:11.444+0000` | “Not sure when it was fixed, but it's fixed and we can always open new ones.” The issue is Resolved / Fixed; settled. |
| Eric Pugh | [SOLR-2054](https://issues.apache.org/jira/browse/SOLR-2054?focusedCommentId=18103887), `2026-08-12T01:22:09.300+0000` | “this has been a long long time ago.. in a open source project far far away..” The obsolete StAX request is Resolved / Not A Problem; settled. |
| Eric Pugh | [SOLR-18288](https://issues.apache.org/jira/browse/SOLR-18288?focusedCommentId=18103889), `2026-08-12T01:30:44.368+0000` | “This has been completed!” The documentation issue is Resolved / Fixed; settled. |
| David Smiley | [SOLR-17602](https://issues.apache.org/jira/browse/SOLR-17602?focusedCommentId=18103893), `2026-08-12T01:56:44.219+0000` | “Interesting. Albeit the issue here can only realistically be satisfied by something in the primary repo. When a PR lands influencing what we ship, we should understand then what the impact is.” The ticket remains Open / Unresolved and unassigned. Linked [PR #2925](https://github.com/apache/solr/pull/2925) and [PR #4039](https://github.com/apache/solr/pull/4039) are both merged. |
## Candidate work from fresh comments
- Candidate only — not claimed, SOLR-11953 (Eric Pugh): verify whether JTS in the SQL module’s runtime dependencies satisfies the original “include JTS with Solr” use case or whether the standard distribution still needs a broader packaging change; confirm scope before changing anything.
- Candidate only — not claimed, SOLR-17602 (David Smiley): trace whether the merged dependency-locking and generated-POM changes affect final Solr artifacts, then outline a primary-repo generated dependency inventory/check for shipped packages and ask David whether that is the intended slice.
The other four fresh comments already report resolved work or close out obsolete requests; no candidate work is suggested for them.
## Per-ticket dispositions
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18288](https://issues.apache.org/jira/browse/SOLR-18288?focusedCommentId=18103889) - Document non-exploitability of Log4j CVE-2026-34477 through -34481 in Solr 9.10.1 | `2026-08-12T01:30:44.000+0000` | 1 | Eric Pugh: completed | Resolved / Fixed; settled. |
| [SOLR-17602](https://issues.apache.org/jira/browse/SOLR-17602?focusedCommentId=18103893) - Maintain final JAR dependencies in source control to track changes | `2026-08-12T01:56:44.000+0000` | 20 | David Smiley: primary-repo scope and impact on shipped artifacts | Open / Unresolved; candidate-only follow-up above. |
| [SOLR-15497](https://issues.apache.org/jira/browse/SOLR-15497?focusedCommentId=18103885) - Broken external web links in RefGuide | `2026-08-12T01:17:11.000+0000` | 6 | Eric Pugh: fixed | Resolved / Fixed; settled. |
| [SOLR-11953](https://issues.apache.org/jira/browse/SOLR-11953?focusedCommentId=18103884) - Include JTS with Solr | `2026-08-12T01:16:01.000+0000` | 4 | Eric Pugh: JTS appears in SQL module dependency lockfile | Open / Unresolved; candidate-only packaging-scope check above. |
| [SOLR-3958](https://issues.apache.org/jira/browse/SOLR-3958?focusedCommentId=18103882) - Solr should log a warning when old healthcheck method configured | `2026-08-12T01:10:45.000+0000` | 4 | Eric Pugh: no “Overcome by time” resolution | Resolved / Not A Problem; settled. |
| [SOLR-2054](https://issues.apache.org/jira/browse/SOLR-2054?focusedCommentId=18103887) - Remove StAX libraries from trunk (java6) | `2026-08-12T01:22:09.000+0000` | 6 | Eric Pugh: work was long ago | Resolved / Not A Problem; settled. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

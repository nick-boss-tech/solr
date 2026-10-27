# Jira day scan receipt: 2026-08-26 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-08-27. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-08-26%22+AND+updated+%3C+%222026-08-27%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-08-26" AND updated < "2026-08-27"`. One search page returned 9 SOLR tickets updated on 2026-08-26 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; all 9 histories were fetched serially.
- Scope totals: 9 tickets, 25 comments across their complete histories, 6 commenter accounts, and 5 fresh watch-list comments, all by Eric Pugh. No `nickshanin` account comments appear; all commenters and comments are included in full-history totals.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| Eric Pugh | `epugh` | 11 | 7 | Commenter account |
| ASF subversion and git services | `jira-bot` | 9 | 3 | Automation |
| Andrzej Bialecki | `ab` | 2 | 2 | Commenter account |
| Chris M. Hostetter | `hossman` | 1 | 1 | Commenter account |
| Jan Høydahl | `janhoy` | 1 | 1 | Commenter account |
| Mike Drob | `mdrob` | 1 | 1 | Commenter account |
## Fresh watch-list comments
| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| Eric Pugh | [SOLR-18300](https://issues.apache.org/jira/browse/SOLR-18300?focusedCommentId=18108289), `2026-08-26T12:27:42.422+0000` | “Thanks for confirming!!!!!” |
| Eric Pugh | [SOLR-16918](https://issues.apache.org/jira/browse/SOLR-16918?focusedCommentId=18108292), `2026-08-26T12:42:04.617+0000` | Says OpenTelemetry’s `OtelInstrumentedExecutorService` now replaces the Dropwizard-based implementation. |
| Eric Pugh | [SOLR-17402](https://issues.apache.org/jira/browse/SOLR-17402?focusedCommentId=18108374), `2026-08-26T15:48:47.881+0000` | “This was already done by a standard Solrbot update a while ago!” |
| Eric Pugh | [SOLR-14349](https://issues.apache.org/jira/browse/SOLR-14349?focusedCommentId=18108416), `2026-08-26T17:58:01.644+0000` | Says the SLF4J upgrade is done; selective lambda adoption needs separate project discussion and its own Jira. |
| Eric Pugh | [SOLR-18405](https://issues.apache.org/jira/browse/SOLR-18405?focusedCommentId=18108417), `2026-08-26T18:02:09.980+0000` | Says lambdas have a cost and should be applied selectively to expensive logging calls, not blindly. |
## Candidate work from fresh comments
Candidate only — not claimed: [SOLR-18405](https://issues.apache.org/jira/browse/SOLR-18405) is Open / Unresolved and was split from [SOLR-14349](https://issues.apache.org/jira/browse/SOLR-14349). The fresh comment stresses selective use rather than a blanket conversion; the open issue proposes auditing unguarded, non-trivial trace/debug arguments in hot `solr:core` paths, converting the worst offenders to SLF4J 2.x lazy arguments, and considering a detector only if it avoids false positives on cheap arguments. A useful next step would be to inventory and rank expensive hot-path sites, then implement only the highest-value conversions. No work was claimed.
The other fresh comments do not identify additional work: [SOLR-18300](https://issues.apache.org/jira/browse/SOLR-18300) is Resolved / Fixed; [SOLR-17402](https://issues.apache.org/jira/browse/SOLR-17402) is Resolved / Not a Problem, with the dependency update already done; [SOLR-16918](https://issues.apache.org/jira/browse/SOLR-16918) is Resolved / Not a Problem, superseded by OpenTelemetry; and [SOLR-14349](https://issues.apache.org/jira/browse/SOLR-14349) is Resolved / Not a Problem, with the separate lazy-logging work now tracked in SOLR-18405.
## Per-ticket dispositions
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18405](https://issues.apache.org/jira/browse/SOLR-18405) - Adopt SLF4J 2.x lazy-argument-evaluation logging on hot paths | `2026-08-26T18:02:09.000+0000` | 1 | Eric Pugh: selective use for expensive calls | Open / Unresolved; one candidate-only hot-path audit and prioritization. |
| [SOLR-18404](https://issues.apache.org/jira/browse/SOLR-18404) - Re-evaluate use of caching headers in all Solr APIs | `2026-08-26T15:15:54.000+0000` | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18365](https://issues.apache.org/jira/browse/SOLR-18365) - Remove DefaultSolrCoreState(DirectoryFactory) single-arg constructor | `2026-08-26T22:08:46.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18300](https://issues.apache.org/jira/browse/SOLR-18300) - Upgrade kafka to V4 | `2026-08-26T12:27:42.000+0000` | 9 | Eric Pugh: brief confirmation | Resolved / Fixed; no additional follow-up identified. |
| [SOLR-17402](https://issues.apache.org/jira/browse/SOLR-17402) - Bump up ecj to 3.36.0 | `2026-08-26T15:48:47.000+0000` | 1 | Eric Pugh: update already done | Resolved / Not a Problem; no additional follow-up identified. |
| [SOLR-16918](https://issues.apache.org/jira/browse/SOLR-16918) - MetricUtils.instrumentedExecutorService() is not safe in any executors with a lifecycle that isn't forever | `2026-08-26T12:42:04.000+0000` | 2 | Eric Pugh: OpenTelemetry replacement | Resolved / Not a Problem; no additional follow-up identified. |
| [SOLR-15425](https://issues.apache.org/jira/browse/SOLR-15425) - Upgrade to Metrics 4.2.0 | `2026-08-26T09:53:22.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-14349](https://issues.apache.org/jira/browse/SOLR-14349) - Upgrade to slf4j 2x when available and encourage using lambdas for expensive methods called in log statements | `2026-08-26T17:58:01.000+0000` | 1 | Eric Pugh: upgrade complete; lambda adoption split to new Jira | Resolved / Not a Problem; its remaining scope is now tracked in SOLR-18405. |
| [SOLR-8885](https://issues.apache.org/jira/browse/SOLR-8885) - Update JS lib versions | `2026-08-26T22:08:54.000+0000` | 6 | None | No fresh watch-list comment; no candidate from this scan. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

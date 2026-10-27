# Jira day scan receipt: 2026-08-23 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-08-24. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-08-23%22+AND+updated+%3C+%222026-08-24%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-08-23" AND updated < "2026-08-24"`. One search page returned 8 SOLR tickets updated on 2026-08-23 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; all 8 histories were fetched serially.
- Scope totals: 8 tickets, 39 comments across their complete histories, 12 commenter accounts, and 8 fresh watch-list comments (5 by David Smiley, 3 by Eric Pugh). No `nickshanin` account comments appear; all commenters and comments are included in full-history totals.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 10 | 4 | Automation |
| Eric Pugh | `epugh` | 9 | 4 | Commenter account |
| David Smiley | `dsmiley` | 6 | 5 | Commenter account |
| Sanjay Kumar Yadav | `yaduvanshi` | 4 | 1 | Commenter account |
| Chris M. Hostetter | `hossman` | 3 | 1 | Commenter account |
| Andrzej Bialecki | `ab` | 1 | 1 | Commenter account |
| Botond Brem | `bbotond` | 1 | 1 | Commenter account |
| Dhoka Pramod | `dpramod` | 1 | 1 | Commenter account |
| Ishan Chattopadhyaya | `ishan` | 1 | 1 | Commenter account |
| Jan Høydahl | `janhoy` | 1 | 1 | Commenter account |
| Noble Paul | `noble.paul` | 1 | 1 | Commenter account |
| Otis Gospodnetic | `otis` | 1 | 1 | Commenter account |
## Fresh watch-list comments
| Person | Ticket and time | Comment summary |
| --- | --- | --- |
| David Smiley | [SOLR-18398](https://issues.apache.org/jira/browse/SOLR-18398?focusedCommentId=18107028), `2026-08-23T02:15:07.022+0000` | A broad but mostly trivial source migration; defer starting until closer to 11.0. Expected uniqueness benefit; little performance effect expected. |
| Eric Pugh | [SOLR-17825](https://issues.apache.org/jira/browse/SOLR-17825?focusedCommentId=18107098), `2026-08-23T11:55:19.821+0000` | Kafka 4 no longer includes beanutils, so the vulnerability issue is fixed. |
| Eric Pugh | [SOLR-10091](https://issues.apache.org/jira/browse/SOLR-10091?focusedCommentId=18107100), `2026-08-23T11:57:58.650+0000` | Says the feature shipped with cross-DC in the 9.x line, but qualifies that recollection with “I think.” |
| Eric Pugh | [SOLR-18342](https://issues.apache.org/jira/browse/SOLR-18342?focusedCommentId=18107104), `2026-08-23T11:58:34.721+0000` | “This is done, thanks!” |
| David Smiley | [SOLR-18060](https://issues.apache.org/jira/browse/SOLR-18060?focusedCommentId=18107131), `2026-08-23T15:11:18.737+0000` | Recommends `git cherry-pick -x` for standard backport provenance; for most backports, update the PR milestone instead. |
| David Smiley | [SOLR-16328](https://issues.apache.org/jira/browse/SOLR-16328?focusedCommentId=18107135), `2026-08-23T15:17:35.945+0000` | Traces the string-interner hook and finds it was never used by the live per-collection ZooKeeper read path; approves removing it completely. |
| David Smiley | [SOLR-17461](https://issues.apache.org/jira/browse/SOLR-17461?focusedCommentId=18107136), `2026-08-23T15:20:30.941+0000` | Says the parser is effectively dead code and should be removed in another PR already pending on a related topic. |
| David Smiley | [SOLR-18372](https://issues.apache.org/jira/browse/SOLR-18372?focusedCommentId=18107141), `2026-08-23T15:49:08.658+0000` | Says removing `FunctionQParser.parseMultipleSources` is safe to bring to 10.x because it is too internal; explains the `@Deprecated` versus `@lucene.internal` distinction. |
## Candidate work from fresh comments
Candidate only — not claimed: consider a 10.x backport of the `FunctionQParser.parseMultipleSources` removal from [SOLR-18372](https://issues.apache.org/jira/browse/SOLR-18372). David Smiley explicitly says the removal is safe for 10.x because the API is too internal, while Jira currently shows the change resolved for 11.0 only. This is an optional follow-up to assess, not a claim or a posted request.
Other fresh comments are completion, guidance, or explicit deferral rather than near-term candidates: [SOLR-18398](https://issues.apache.org/jira/browse/SOLR-18398) remains Open / Unresolved with fix version 11.0, and David says to wait until closer to that release; [SOLR-17825](https://issues.apache.org/jira/browse/SOLR-17825) is Resolved / Fixed; [SOLR-10091](https://issues.apache.org/jira/browse/SOLR-10091) is Resolved / Duplicate (the comment's 9.x recollection is uncertain); [SOLR-18342](https://issues.apache.org/jira/browse/SOLR-18342) is Resolved / Fixed; [SOLR-18060](https://issues.apache.org/jira/browse/SOLR-18060) is Resolved / Fixed; [SOLR-16328](https://issues.apache.org/jira/browse/SOLR-16328) is Closed / Fixed; and [SOLR-17461](https://issues.apache.org/jira/browse/SOLR-17461) is Resolved / Abandoned, with the comment saying removal is expected in another PR.
## Per-ticket dispositions
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18398](https://issues.apache.org/jira/browse/SOLR-18398) - Migrate SolrQueryResponse and responseHeader from NamedList to LinkedHashMap | `2026-08-23T02:15:07.000+0000` | 1 | David Smiley: defer broad source migration until closer to 11.0 | Open / Unresolved, fix version 11.0; explicitly deferred. |
| [SOLR-18372](https://issues.apache.org/jira/browse/SOLR-18372) - Remove FunctionQParser.parseMultipleSources | `2026-08-23T15:49:08.000+0000` | 10 | David Smiley: safe to bring removal to 10.x | Resolved / Fixed for 11.0; one optional backport candidate only. |
| [SOLR-18342](https://issues.apache.org/jira/browse/SOLR-18342) - Upgrade to Gradle 9.7.0 | `2026-08-23T11:58:34.000+0000` | 4 | Eric Pugh: done | Resolved / Fixed; no additional follow-up identified. |
| [SOLR-18060](https://issues.apache.org/jira/browse/SOLR-18060) - CrossDC Consumer - add Prometheus metrics | `2026-08-23T15:11:18.000+0000` | 5 | David Smiley: backport provenance/process guidance | Resolved / Fixed; the comment advises future backport handling. |
| [SOLR-17825](https://issues.apache.org/jira/browse/SOLR-17825) - Upgrade commons-beanutils jar to 1.11.0+ to fix CVE-2025-48734 | `2026-08-23T11:55:19.000+0000` | 6 | Eric Pugh: Kafka 4 removes beanutils | Resolved / Fixed; no additional follow-up identified. |
| [SOLR-17461](https://issues.apache.org/jira/browse/SOLR-17461) - (cleanup) Move ClusterState string "Interner" json parser to Utils | `2026-08-23T15:21:47.000+0000` | 7 | David Smiley: code is effectively dead and expected to be removed elsewhere | Resolved / Abandoned; removal is described as planned in another PR. |
| [SOLR-16328](https://issues.apache.org/jira/browse/SOLR-16328) - intern() strings in DocCollection to reduce memory footprint | `2026-08-23T15:17:35.000+0000` | 4 | David Smiley: interning was not used in the live ZK path; approves complete removal | Closed / Fixed; no separate claim or PR made. |
| [SOLR-10091](https://issues.apache.org/jira/browse/SOLR-10091) - Support for CDCR using an external queueing service | `2026-08-23T11:57:58.000+0000` | 2 | Eric Pugh: says cross-DC in 9.x delivered this, with uncertainty | Resolved / Duplicate; no follow-up candidate from this closed ticket. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

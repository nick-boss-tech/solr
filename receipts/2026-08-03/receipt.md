# Jira day scan receipt: 2026-08-03 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-08-04. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-08-03%22+AND+updated+%3C+%222026-08-04%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-08-03" AND updated < "2026-08-04"`. One search page returned 5 SOLR tickets updated on 2026-08-03 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; all 5 ticket histories were fetched serially.
- Scope totals: 5 tickets, 24 comments across their complete histories, 6 commenter accounts, and 3 fresh watch-list comments. No `nickshanin` account comments appear; all 24 comments remain in full-history totals.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| Mark Miller | `markrmiller@gmail.com` | 7 | 2 | Person |
| David Smiley | `dsmiley` | 6 | 4 | Person |
| Hrishikesh Gadre | `hgadre` | 5 | 1 | Person |
| Christine Poerschke | `cpoerschke` | 2 | 1 | Person |
| Jan Høydahl | `janhoy` | 2 | 2 | Person |
| Serhiy Bzhezytskyy | `serhiybzhezytskyy` | 2 | 1 | Person |
## Fresh watch-list comments
| Person | Ticket and time | Comment excerpt and context |
| --- | --- | --- |
| Jan Høydahl | [SOLR-18326](https://issues.apache.org/jira/browse/SOLR-18326?focusedCommentId=18101363), `2026-08-03T12:11:48.651+0000` | “Metric name filtering is probably enough for the purpose of limiting unnecessary data. More detailed storage/retention filtering can be left to the destination system.” The feature remains Open / Unresolved. David Smiley's [attached design note](https://issues.apache.org/jira/secure/attachment/13083575/SOLR-18326-OTLP-metrics-filter.md) proposes both metric-name and label/value filtering, creating a concrete scope choice. A [public GitHub PR search](https://api.github.com/search/issues?q=repo%3Aapache%2Fsolr+SOLR-18326+is%3Apr) returned no matching PRs. |
| David Smiley | [SOLR-12321](https://issues.apache.org/jira/browse/SOLR-12321?focusedCommentId=18101462), `2026-08-03T18:13:59.756+0000` | “Closing because Solr hasn't uses Java serialization for years.” The issue is Resolved / Not A Problem and marked as a duplicate of SOLR-7055; this fresh comment is settled, with no candidate. |
| David Smiley | [SOLR-7055](https://issues.apache.org/jira/browse/SOLR-7055?focusedCommentId=18101464), `2026-08-03T18:15:28.468+0000` | “Closing because Solr hasn't uses Java serialization for years.” The parent issue is Resolved / Not A Problem; this fresh comment is settled, with no candidate. The Jira page separately lists [SOLR-7056](https://issues.apache.org/jira/browse/SOLR-7056), an Open / Unresolved rolling-upgrade subtask; David's fresh close note does not address it.
## Candidate work from fresh comments
- **Candidate only — SOLR-18326 (Jan Høydahl):** clarify the first-slice scope for OTLP filtering. Jan's fresh note says metric-name filtering is probably sufficient and detailed storage/retention filtering belongs at the destination, while the existing attached proposal includes label/value filters. Ask the sponsor to choose name-only versus name-plus-label filtering before implementation; no task was claimed.
- No candidate from the two Java-serialization comments: both fresh comments close duplicate legacy issues as Not A Problem. SOLR-7056 is noted as a separate open subtask, but it was not raised in the fresh comment and is not proposed here.
## Per-ticket dispositions
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18330](https://issues.apache.org/jira/browse/SOLR-18330) - RTG requests on TLOG replica shouldn't use an extra local HTTP hop | `2026-08-03T20:39:05.000+0000` | 0 | None | No fresh watch-list comment; no candidate. |
| [SOLR-18326](https://issues.apache.org/jira/browse/SOLR-18326) - OTEL OTLP filtering for metrics | `2026-08-03T12:11:48.000+0000` | 3 | Jan Høydahl's scope recommendation | Open / Unresolved; candidate-only scope clarification as above. |
| [SOLR-12321](https://issues.apache.org/jira/browse/SOLR-12321) - Use of builtin java serialization for admin response breaks 7.3 compatibility | `2026-08-03T18:13:59.000+0000` | 4 | David Smiley's closure note | Resolved / Not A Problem; marked as duplicate of SOLR-7055; settled. |
| [SOLR-12239](https://issues.apache.org/jira/browse/SOLR-12239) - Enabling index sorting causes “segment not sorted with indexSort=null” | `2026-08-03T10:15:32.000+0000` | 5 | None | No fresh watch-list comment; no candidate. |
| [SOLR-7055](https://issues.apache.org/jira/browse/SOLR-7055) - Java serialization error while operating a mixed mode Solr cluster | `2026-08-03T18:15:28.000+0000` | 12 | David Smiley's closure note | Resolved / Not A Problem; settled. Separate subtask SOLR-7056 remains Open / Unresolved. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

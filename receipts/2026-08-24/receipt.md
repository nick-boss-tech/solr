# Jira day scan receipt: 2026-08-24 (UTC)
- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-08-25. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-08-24%22+AND+updated+%3C+%222026-08-25%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-08-24" AND updated < "2026-08-25"`. One search page returned 6 SOLR tickets updated on 2026-08-24 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`; all 6 histories were fetched serially.
- Scope totals: 6 tickets, 31 comments across their complete histories, 10 commenter accounts, and 1 fresh watch-list comment. No `nickshanin` account comments appear; all commenter accounts and comments are included in the full-history totals.
## Commenter table
| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 9 | 5 | Automation |
| David Smiley | `dsmiley` | 5 | 1 | Commenter account |
| Eric Pugh | `epugh` | 4 | 2 | Commenter account |
| Chris M. Hostetter | `hossman` | 3 | 1 | Commenter account |
| Erik Hatcher | `ehatcher` | 3 | 1 | Commenter account |
| Alexandre Rafalovitch | `arafalov` | 2 | 1 | Commenter account |
| Stefan Matheis | `steffkes` | 2 | 1 | Commenter account |
| Jan Høydahl | `janhoy` | 1 | 1 | Commenter account |
| Mark Miller | `markrmiller@gmail.com` | 1 | 1 | Commenter account |
| Robert Muir | `rcmuir` | 1 | 1 | Commenter account |
## Fresh watch-list comment
| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| Jan Høydahl | [SOLR-3232](https://issues.apache.org/jira/browse/SOLR-3232?focusedCommentId=18107561), `2026-08-24T19:41:04.356+0000` | “Shall we close this and accept that we have to enter the handler manually in the (old) Admin UI? Make it more fancy in the new one?” |
## Candidate work from fresh comments
Candidate only — not claimed: [SOLR-3232](https://issues.apache.org/jira/browse/SOLR-3232?focusedCommentId=18107561) remains Open / Unresolved. Its fresh comment raises a product decision: accept manual request-handler entry in the old Admin UI and close the old request, or carry the improved selection experience into the new UI. A focused follow-up would confirm that disposition and, if the feature remains wanted, ensure it is tracked against the current UI rather than the legacy form. No work was claimed.
## Per-ticket dispositions
| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18376](https://issues.apache.org/jira/browse/SOLR-18376) - Remove ScalarQuantizedDenseVectorField compress/confidenceInterval | `2026-08-24T17:39:40.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18361](https://issues.apache.org/jira/browse/SOLR-18361) - Remove LegacyFieldValueFeature (contrib/ltr) | `2026-08-24T20:23:52.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18353](https://issues.apache.org/jira/browse/SOLR-18353) - Remove SolrDocumentBase.getChildDocumentCount() | `2026-08-24T17:34:48.000+0000` | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18250](https://issues.apache.org/jira/browse/SOLR-18250) - Fix GCS backup copyIndexFileTo silently swallowing exceptions and prematurely terminating reads | `2026-08-24T16:13:36.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18109](https://issues.apache.org/jira/browse/SOLR-18109) - Fix deprecated SolrPluginUtils.doStandardDebug | `2026-08-24T17:34:48.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-3232](https://issues.apache.org/jira/browse/SOLR-3232) - Admin UI: query form should have a menu to pick a request handler | `2026-08-24T19:41:04.000+0000` | 18 | Jan Høydahl: old-versus-new UI disposition question | Open / Unresolved; one candidate-only product decision. |
No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

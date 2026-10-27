# Jira day scan receipt: 2026-09-12 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-09-13. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-09-12%22+AND+updated+%3C+%222026-09-13%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-09-12" AND updated < "2026-09-13"`. One search page returned all 8 tickets; each `updated` timestamp falls on 2026-09-12 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 8 histories were fetched serially.
- Scope totals: 8 SOLR tickets; 34 comments across their complete histories; 6 commenter accounts. There were no fresh watch-list comments on 2026-09-12. Nick Shanin did not appear in these histories.

## Commenter table

`jira-bot` is marked as automation, not a person.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 24 | 8 | Automation |
| Eric Pugh | `epugh` | 5 | 3 | Commenter account |
| Andrzej Bialecki | `ab` | 2 | 2 | Commenter account |
| Chris M. Hostetter | `hossman` | 1 | 1 | Commenter account |
| Erick Erickson | `erickerickson` | 1 | 1 | Commenter account |
| Serhiy Bzhezytskyy | `serhiybzhezytskyy` | 1 | 1 | Commenter account |

## Fresh watch-list comments

None. The scanner found no watch-list comments created on 2026-09-12.

## Candidate work from fresh comments

No candidate: there was no fresh watch-list activity on this date.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for each ticket. No ticket had a fresh watch-list comment on this date.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18423](https://issues.apache.org/jira/browse/SOLR-18423) - CrossDC Consumer: off-by-one error in ThreadDump handler | `2026-09-12T19:50:30.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18415](https://issues.apache.org/jira/browse/SOLR-18415) - CrossDC Common: ExposedByteArrayOutputStream poor optimization for large docs | `2026-09-12T19:50:31.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18412](https://issues.apache.org/jira/browse/SOLR-18412) - CrossDC Producer: inverted ZK config precedence | `2026-09-12T19:50:30.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18394](https://issues.apache.org/jira/browse/SOLR-18394) - Simplify JettyConfig construction | `2026-09-12T19:50:30.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18364](https://issues.apache.org/jira/browse/SOLR-18364) - Remove SolrCore.isWriterLocked() | `2026-09-12T19:50:29.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18362](https://issues.apache.org/jira/browse/SOLR-18362) - Remove org.apache.solr.spelling.Token | `2026-09-12T19:50:32.000+0000` | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18358](https://issues.apache.org/jira/browse/SOLR-18358) - Remove CollectionAdminRequest.Backup.setIncremental(boolean) | `2026-09-12T19:50:31.000+0000` | 6 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-504](https://issues.apache.org/jira/browse/SOLR-504) - dismax with pf missing or blank causes empty boolean clause to be added to main query | `2026-09-12T19:50:30.000+0000` | 6 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.

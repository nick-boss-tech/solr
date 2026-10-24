# Assignment: new-ui-assessment-round-2 (the unassessed Christos and Kotlin tickets)

- Status: OPEN. Intended host: any host with the `review` tag. Posted by phone agent on behalf of Nick Shanin, 2026-10-23.
- Scope: research only, same shape as the round 1 reports. No code, no comments, nothing public. One ticket per claim under the small-claims rule.
- Source: the census at `material/ui-tickets.md`. The new UI working set is 67 tickets (the `new-ui` label plus everything reported by malliaridis); 19 were assessed in round 1. These eight open ones were not:

| Ticket | Title |
|---|---|
| SOLR-17572 | Migrate Gradle files to use Kotlin DSL |
| SOLR-17585 | CodeCache full during Gradle builds |
| SOLR-17598 | Windows Script logic that handles SOLR_LOGS_DIR is faulty |
| SOLR-17657 | Evaluate and Update checksum and signature verification |
| SOLR-17898 | Updating collection property via v2 API endpoint fails with 500 response |
| SOLR-18178 | Exported configsets from a windows instance are invalid |
| SOLR-18191 | Disable lenient json parsing at API level |
| SOLR-18214 | Base configset during configset creation is not optional |

SOLR-14414 (the old New Admin UI umbrella) is in the census set and deliberately not in this round; it is the superseded Angular effort's tracker, not a work candidate.

## Per ticket deliverable

In `reports/new-ui-assessment-round-2.md`, one section per claimed ticket: (a) the ask, in the ticket's own terms; (b) what exists, with file paths and line citations against current main; (c) what is missing; (d) verdict, one of READY TO VOLUNTEER, NEEDS CHRISTOS, ALREADY DONE, TOO BIG AS STATED (with the smallest first slice named). For the defect tickets, a reproduction attempt is part of (b) wherever the ticket describes testable behavior. Close with a ranking of the eight by how startable they are for Nick.

# Warm-ticket evidence units, round 1: completion receipt (vm2)

Assignment: `assignments/warm-ticket-evidence-units-round-1.md` (not edited). Board snapshot at writing: `a428f2a956f`. Written 2026-10-25T00:21:14Z.
Completion is derived from the deliverables below. Each unit has its own claim; none is edited.

| Unit | Ticket | Claim | Deliverables | Draft state |
| --- | --- | --- | --- | --- |
| 1 | SOLR-17708 double authorization | `claims/warm-ticket-evidence-units-round-1-unit-1-SOLR-17708-vm2.md` | `reports/solr-17708-double-auth-trace-round-1.md`, correction `reports/solr-17708-trace-correction-1.md`, `jira/jira-comment-SOLR-17708-draft.md` | DRAFT, not posted, not reviewed |
| 2 | SOLR-18503 JSON facet keys | `claims/warm-ticket-evidence-units-round-1-unit-2-SOLR-18503-vm2.md` | `reports/solr-18503-facet-keys-audit-round-1.md`, `jira/jira-comment-SOLR-18503-draft.md` | DRAFT, not posted, not reviewed |
| 3 | SOLR-18521 picocli connection options | `claims/warm-ticket-evidence-units-round-1-unit-3-SOLR-18521-vm2.md` | `reports/solr-18521-picocli-scoping-round-1.md`, `jira/jira-comment-SOLR-18521-draft.md` | DRAFT, not posted, not reviewed |
| 4 | SOLR-18347 Admin UI bugs | `claims/warm-ticket-evidence-units-round-1-unit-4-SOLR-18347-vm2.md` | `reports/solr-18347-adminui-bugs-check-round-1.md`, `jira/jira-comment-SOLR-18347-draft.md` | DRAFT, not posted, not reviewed |

## Outcomes

- Unit 1: the double check is confirmed at container and core levels. The ticket's suggested override cannot compile as written, because `shouldAuthorize()` is private.
- Unit 2: all seven keys confirmed as accepted by the parser and undocumented in the Ref Guide. Code reading only.
- Unit 3: the counts differ from the ticket. 16 exclusive-group tools, not three. Two static `resolveSolrUrl` helpers exist.
- Unit 4: two of five items hold in code (2 and 3, partly). Items 4 and 5 do not hold on main as written. Item 1 is not reproducible from the cited function. Items 1 to 3 need a browser run.

## Limits

- Code reading and `git grep` at main `13c693d805f6541f7cec684cd27d28eef7b3b614`. No build, no test, no Solr or browser run.
- Nothing posted to Jira or GitHub. Each draft needs Nick's review and go.

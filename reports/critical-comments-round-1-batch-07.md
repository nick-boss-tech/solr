# Critical tickets comment coverage, batch 07

Claim: `claims/critical-comments-round-1-batch-07-vm2.md`. Assignment: `assignments/critical-tickets-comments-round-1.md`. Census: `material/critical-tickets-census-2026-10-24.md`.
Read live from the Jira REST API on 2026-10-25. GitHub PR search on apache/solr for each key. Board: records for these keys. Every draft is DRAFT, not posted.

## Dispositions

| Ticket | Type / status | Owner PR or branch | Disposition | Shape or reason code |
| --- | --- | --- | --- | --- |
| SOLR-17252 | Bug / Open, 3 comments (2024) | none | draft | scoping: tmux always, or only behind a flag |
| SOLR-17297 | Bug / Open, 3 comments (2024) | none | draft | scoping: classloader isolation for modules, or packages for plugins |
| SOLR-17281 | Bug / Open, no comments | none | draft | stale-check |
| SOLR-17291 | Bug / Open, no comments | none | draft | stale-check; overlaps the round 1 search triage candidate 6 (not named in the draft) |
| SOLR-16914 | Bug / Open, 7 comments, last 2024 | none | draft | stale-check |
| SOLR-3044 | Improvement / Open, 2 comments (2012), links SOLR-14730 | none | draft | scoping: which NamedList use to deprecate first |
| SOLR-17143 | Bug / Open, 2 comments (2025) | none | no draft | `already-told`: a maintainer comment (stillalex, 2025-03-11) already gives the idleTimeout setting the reporter asked about |
| SOLR-17762 | Bug / Open, no comments | none | draft | status-check; reads as a support question, and the user list is suggested |
| SOLR-14996 | Bug / Open, 10 comments (2025) | none | draft | stale-check; a 2025 comment links SOLR-17796 for a similar exclusion problem |
| SOLR-17370 | Bug / Open, 2 comments (2025) | PR #3346 closed unmerged (2025-08-30) | draft | stale-check; a minimal reproduction was requested in 2024 and not given |

## Notes

- SOLR-17370's PR #3346 is closed without merging. The draft says so and does not claim a fix. The reason for closing is not on the ticket.
- SOLR-17291 and SOLR-17370 are also candidates in the round 1 critical triage (`reports/critical-implementation-triage-round-1.md`). The drafts do not mention that report.
- SOLR-17297's classloader comment from 2024 is a maintainer answer on the design, so the draft asks the design question rather than restating the answer.

## Limits

- Jira text and GitHub PR search only. No reproduction, no code trace on main.
- Stale-check drafts state no current-release result.
- Mentions use the Jira reporter, not a checked committer sponsor.
- No Jira or GitHub write. Each draft needs Nick's review and go.

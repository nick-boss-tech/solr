# Critical tickets comment coverage, batch 06

Claim: `claims/critical-comments-round-1-batch-06-vm2.md`. Assignment: `assignments/critical-tickets-comments-round-1.md`. Census: `material/critical-tickets-census-2026-10-24.md`.
Read live from the Jira REST API on 2026-10-25. GitHub PR search on apache/solr for each key. Board: records for these keys. Every draft is DRAFT, not posted.

## Dispositions

| Ticket | Type / status | Owner PR or branch | Disposition | Shape or reason code |
| --- | --- | --- | --- | --- |
| SOLR-15759 | Bug / Open, no comments | none | draft | stale-check |
| SOLR-14346 | Bug / Open, 2 comments (2021) | none | draft | stale-check; a 2021 comment gives a base-URL workaround |
| SOLR-15096 | Bug / Open, 10 comments (2021) | none | draft | stale-check; a 2021 question on blocker status is unanswered |
| SOLR-16100 | Bug / Open, no comments | none | draft | stale-check |
| SOLR-15998 | Improvement / Open, no comments | none | draft | stale-check |
| SOLR-16509 | Bug / Patch Available, 1 comment | PR #1146 open, by another committer | draft | status-check on the open PR; no competing offer |
| SOLR-16725 | Bug / Open, 3 comments (2023) | none | draft | scoping: match restored-collection types, or document them |
| SOLR-17122 | Bug / Open, no comments | none | draft | scoping: are the counts outside the documented estimate error |
| SOLR-16139 | Bug / Open, 5 comments (2024) | none | draft | status-check; reproductions in 2022 and 2024 |
| SOLR-15237 | Bug / Open, 9 comments (2024) | none | draft | status-check to the assignee; 2024 reproduction on 9.0 and 9.5 |

## Notes

- PR search returned one closed hit for SOLR-15237 (#70, another project, matched on title text) and two hits for SOLR-16509: #1146 is open and is the ticket's own PR, the other is closed. Neither is an owner PR.
- SOLR-16509's status-check asks whether PR #1146 is ready for review. It does not offer an alternative implementation.
- SOLR-15237's status-check goes to the assignee, Mark Miller, as the sponsor candidate.
- SOLR-16139 is the same ticket as triage candidate 3 (critical implementation triage round 1). Its draft asks about the plan, not the fix.

## Limits

- Jira text and GitHub PR search only. No reproduction, no code trace on main.
- Stale-check drafts state no current-release result.
- Mentions use the Jira reporter or assignee, not a checked committer sponsor.
- No Jira or GitHub write. Each draft needs Nick's review and go.

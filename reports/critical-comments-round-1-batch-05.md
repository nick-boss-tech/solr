# Critical tickets comment coverage, batch 05

Claim: `claims/critical-comments-round-1-batch-05-vm2.md`. Assignment: `assignments/critical-tickets-comments-round-1.md`. Census: `material/critical-tickets-census-2026-10-24.md`.
Read live from the Jira REST API on 2026-10-25. GitHub PR search on apache/solr for each key: no hits for any key. Board: no records for these keys. Every draft is DRAFT, not posted.

## Dispositions

| Ticket | Type / status | Owner PR or branch | Disposition | Shape or reason code |
| --- | --- | --- | --- | --- |
| SOLR-11310 | Bug / Open, 1 comment (2020) | none | draft | stale-check; a 2020 comment reproduces it on 8.3 |
| SOLR-14833 | Bug / Open, 1 comment (2020) | none | draft | stale-check; the 2020 hl.method=unified suggestion is unanswered |
| SOLR-14311 | New Feature / Open, 3 comments (2020) | none | no draft | `covered-by-link`: a 2020 comment says it is the same as SOLR-14232, which is Open. SOLR-14232 is not in batch 05 and needs its own draft. |
| SOLR-9956 | Bug / Open, 8 comments (2020) | none | draft | stale-check; a 2020 maintainer comment says 6.x will not get new releases |
| SOLR-15326 | Bug / Open, 1 comment (2021) | none | draft | stale-check; the 2021 reproduction request is unanswered |
| SOLR-14788 | Task / Open, 56 comments, assignee Mark Miller | none | no draft | `already-told`: the assignee's own 2021 comments set out the vision in the ticket |
| SOLR-14649 | Improvement / Open, 10 comments (2021) | none | draft | scoping: re-sign existing packages, or only new releases use SHA512 |
| SOLR-15412 | Bug / Open, 15 comments (2021) | none | draft | stale-check; SOLR-14245, which introduced the strict validation, is Resolved Fixed in 8.5 |
| SOLR-15596 | Bug / Open, 2 comments (2021) | none | draft | scoping: the target release, 8.x or 9.x |
| SOLR-14593 | Task / Open, 6 comments (2021) | none | draft | status-check: is the change still planned, and for which release |

## Notes

- SOLR-14311 is the only covered-by-link case in this batch. SOLR-14232 (Open, shareSchema leak protections) must get its own draft before SOLR-14311 is covered.
- SOLR-15412's fix-version history comes from SOLR-14245, not from this ticket, which has no fix version.
- Mentions use the Jira reporter field. SOLR-14649 and SOLR-15596 are reported by Chris Hegarty's account name ichattopadhyaya as shown in Jira.

## Limits

- Jira text and GitHub PR search only. No reproduction, no code trace on main.
- Stale-check drafts state no current-release result.
- Mentions use the reporter, not a checked committer sponsor.
- No Jira or GitHub write. Each draft needs Nick's review and go.

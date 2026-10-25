# Critical tickets comment coverage, batch 08

Claim: `claims/critical-comments-round-1-batch-08-vm2.md`. Assignment: `assignments/critical-tickets-comments-round-1.md`. Census: `material/critical-tickets-census-2026-10-24.md`.
Read live from the Jira REST API on 2026-10-25, including Jira comments that mention PRs. GitHub PR search and PR reads on apache/solr. Board: records for these keys. Every draft is DRAFT, not posted.

## Dispositions

| Ticket | Type / status | Owner PR or branch | Disposition | Shape or reason code |
| --- | --- | --- | --- | --- |
| SOLR-17788 | Bug / Open, no comments | closed PR #3389 by another contributor | draft | stale-check |
| SOLR-7889 | Improvement / Open, 4 comments (2025) | none | draft | scoping: embedded ZooKeeper as the secure default, or the external setup |
| SOLR-17375 | Improvement / Open, 3 comments (2026) | none | draft | scoping: move IndexReader close off the commit path, or not |
| SOLR-13360 | Bug / Open, 22 comments | PR #3112 closed (another contributor); PR #210 closed (old) | draft | status-check: is the closed approach still the plan |
| SOLR-16655 | Bug / Open, no comments | owner PR #5093 open (draft false), not mentioned in Jira | draft | status-note: the open PR and what it changes |
| SOLR-7550 | Bug / Open, 1 comment | owner PRs #5139 (superseded per its body) and #5002 (filed under SOLR-11431), neither mentioned in Jira | draft | status-note: names both PRs and what each changes |
| SOLR-7394 | Bug / Open, 5 comments | owner PR #5003 | no draft | `already-told`: the owner's own Jira comment of 2026-10-02 links PR #5003 |
| SOLR-18010 | Bug / Open, 5 comments | owner PR #5118 open; fix commits for SOLR-18010 on main and branch_10x are in Jira | no draft | `security-route`: the defect corrupts security configuration. Flag only; no public text. |
| SOLR-17433 | Bug / Open, 11 comments (2026) | PR #4626 open by another contributor | no draft | `active-thread`: a maintainer and the PR author discussed the timeout value in July 2026 |
| SOLR-17767 | New Feature / Open, 1 comment (2026) | PR #4471 open by a committer | no draft | `already-told`: the committer's own 2026-05-28 comment says the PR is ready for review |

## Notes

- SOLR-7394: the owner's PR is already announced on the ticket, so the status-note rule does not apply.
- SOLR-18010: the owner's PR #5118 and the receipt for SOLR-18010 are on the board. The ticket is a security-route flag. The fix commits for SOLR-18010 on main and branch_10x are recorded in Jira. No public draft is written here.
- SOLR-7550: PR #5139's body says it is superseded by PR #5002, and recommends closing #5139 once #5002 lands. The status-note names both PRs and does not call either the fix.
- SOLR-16655 and SOLR-7550 are the only owner-work status notes in this batch. Each needs Nick's approval before anything is posted, as the assignment requires.

## Limits

- Jira text, GitHub PR search and PR reads only. No reproduction, no code trace on main.
- Status-note drafts describe PR contents at the time of reading. They do not state merge outcomes.
- Mentions use the Jira reporter, not a checked committer sponsor.
- No Jira or GitHub write. Each draft needs Nick's review and go.

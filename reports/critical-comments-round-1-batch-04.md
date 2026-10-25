# Critical tickets comment coverage, batch 04

Claim: `claims/critical-comments-round-1-batch-04-vm2.md`. Assignment: `assignments/critical-tickets-comments-round-1.md`. Census: `material/critical-tickets-census-2026-10-24.md`.
Read live from the Jira REST API on 2026-10-25 (description, status, fix versions, comments). GitHub PR search on apache/solr for each key: no hits for any key. Board: no records for these keys. Every draft is DRAFT, not posted.

## Dispositions

| Ticket | Type / status | Owner PR or branch | Disposition | Shape or reason code |
| --- | --- | --- | --- | --- |
| SOLR-12069 | Bug / Open, 4 comments, last 2019 | none | draft | stale-check; a 2019 comment cites lucene-solr PR 833 as the fix, and the ticket is still open |
| SOLR-12315 | Bug / Open, 2 comments, last 2019 | none | draft | stale-check; a 2019 unit-test attempt asked for files, with no reply |
| SOLR-8096 | Bug / Open, 33 comments, last 2019 | none | draft | stale-check; a 2018 comment cites LUCENE-8178 |
| SOLR-6816 | Task / Open, 36 comments, last 2019 | none | draft | scoping: keep the umbrella, split it, or close it |
| SOLR-12801 | Task / Open, 117 comments, assignee Mark Miller | none | no draft | `active-thread`: the assignee's 2019-10-23 comment says the work continues in a follow-up issue |
| SOLR-12743 | Bug / Open, 24 comments, last 2020 | none | draft | stale-check; the January 2020 "is it fixed" question has no answer |
| SOLR-13850 | Bug / Open, 3 comments, last 2019 | none | draft | scoping: must a PreAnalyzedField be stored for atomic updates |
| SOLR-13770 | Bug / Open, no comments | none | draft | stale-check |
| SOLR-13789 | Bug / Patch Available, 2 comments | none | draft | status-check: is the patch ready for review, with the missing test |
| SOLR-14638 | Bug / Open, 3 comments, last 2020 | none | draft | stale-check; the 2020 request for sample data has no reply |

## Notes

- SOLR-12069 has an outside contributor's pull request (lucene-solr #833, 2019) named as the fix. The ticket is still open. The draft asks whether the current release behaves correctly. It does not say the PR is merged.
- SOLR-12801 has no draft: the assignee's most recent comment says the work continues under a follow-up issue. That follow-up is not identified in this pass.
- SOLR-12743 has a request from a committer, in January 2020, to close the ticket if fixed. The draft asks the reporter whether the leak still happens on a supported release.
- Fix versions are not set on any ticket here, so no fix-version claim is made.

## Limits

- Jira text and GitHub PR search only. No reproduction, no code trace on main.
- Stale-check drafts ask whether the ticket still reproduces. They state no current-release result.
- Mentions use the Jira reporter field, not a checked committer sponsor.
- No Jira or GitHub write. Each draft needs Nick's review and go.

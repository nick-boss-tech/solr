# Critical tickets comment coverage, batch 02

Claim: `claims/critical-comments-round-1-batch-02-vm2.md`. Assignment: `assignments/critical-tickets-comments-round-1.md`. Census: `material/critical-tickets-census-2026-10-24.md`.
Read live from the Jira REST API on 2026-10-25: description, status, fix versions and all comments for each ticket. GitHub: PR search on apache/solr for each key. Board: records mentioning each key. Every draft is DRAFT, not posted.

## Dispositions

| Ticket | Type / status | Owner PR or branch | Disposition | Shape |
| --- | --- | --- | --- | --- |
| SOLR-8905 | Bug / Open, no comments | none | draft | stale-check |
| SOLR-3684 | Improvement / Open, 19 comments, last 2014 | none (PR search hit #4783 and #3756 only on body text) | draft | stale-check |
| SOLR-4285 | Bug / Open, 3 comments, last 2015 | none (#4287 matched on the number only) | draft | stale-check |
| SOLR-4165 | Bug / Reopened, 26 comments, last technical 2013 | none | draft | stale-check; links SOLR-3655 and SOLR-4421, both Fixed for 4.2 |
| SOLR-9138 | Bug / Open, no comments | none | draft | scoping (decision: default CDATA output) |
| SOLR-9170 | Bug / Open, 1 comment | none | draft | scoping (decision: which Solr-side diagnostics) |
| SOLR-8887 | Bug / Open, 5 comments | none | draft | covered-by-link: SOLR-4509 Resolved Fixed 7.0; Mark Miller's 2016-03-31 comment says it was completed there |
| SOLR-4690 | Bug / Open, 2 comments, last 2016 | none (#4690 matched on the number only) | draft | stale-check; a 2016 workaround comment is cited |
| SOLR-8028 | Bug / Open, 5 comments, last 2017 | none | draft | stale-check; the 2017 instance-reuse question is unanswered |
| SOLR-6345 | Bug / Open, 7 comments, last 2015 | none | draft | stale-check |

No ticket in this batch has a no-draft reason code. None has a `security-route` flag. None has an owner branch or receipt on the board.

## Notes

- Fix versions 4.9, 6.0 are set on SOLR-3684, SOLR-4165, SOLR-4285 and SOLR-8887 while each is still Open or Reopened. The drafts do not rely on the fix versions.
- SOLR-8887's draft is the only one that names a closing route. It asks whether the 7.0 change in SOLR-4509 closes this one. It does not close anything.
- GitHub PR search matches on ticket numbers in text. Three hits here (#4287, #4690, #4783, #3756) matched only on number or body text. None is an owner PR for these tickets.

## Limits

- Jira text and GitHub PR search only. No reproduction, no code trace on main, and no check of other authors' PRs beyond the search.
- Stale-check drafts ask whether the ticket still reproduces on a supported release. They do not state a current-release result.
- Drafts are mentioned to named users from the Jira reporter field. Each mention is a reporter, not a checked committer sponsor, except SOLR-8887 and SOLR-4165, which mention Mark Miller as the last active committer.
- No Jira or GitHub write. Each draft needs Nick's review and go.

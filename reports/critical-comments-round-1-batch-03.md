# Critical tickets comment coverage, batch 03

Claim: `claims/critical-comments-round-1-batch-03-vm2.md`. Assignment: `assignments/critical-tickets-comments-round-1.md`. Census: `material/critical-tickets-census-2026-10-24.md`.
Read live from the Jira REST API on 2026-10-25 (description, status, fix versions, comments). GitHub PR search on apache/solr for each key: no hits for any key in this batch. Board: no records for these keys. Every draft is DRAFT, not posted.

## Dispositions

| Ticket | Type / status | Owner PR or branch | Disposition | Shape or reason code |
| --- | --- | --- | --- | --- |
| SOLR-8767 | Bug / Open, 1 comment (2018) | none | draft | scoping: should real-time get return copyField destinations |
| SOLR-2908 | Improvement / Open, 5 comments, last 2014 | none | draft | stale-check; a 2014 workaround is cited |
| SOLR-13460 | Bug / Open, no comments | none | draft | stale-check |
| SOLR-11650 | Bug / Open, 3 comments (2017) | none | no draft | `security-route`: credentials shown in clear text on the replication tab. Flag only. |
| SOLR-11668 | Bug / Open, no comments | none | draft | stale-check |
| SOLR-12415 | Bug / Open, 7 comments, last 2018 | none | draft | stale-check; two 2018 comments on the zombie check are cited |
| SOLR-11972 | Improvement / Open, no comments | none | draft | scoping: units in the field type or in the client |
| SOLR-11330 | Improvement / Open, no comments | none | draft | scoping: which normalization, at which step |
| SOLR-10950 | Wish / Open, 1 comment (2017) | none | no draft | `already-told`: a committer comment on the ticket (dsmiley, 2017-06-26) already explains that Lucene's FuzzySuggester cannot take a context filter |
| SOLR-10928 | Improvement / Open, no comments | none | draft | scoping: the elevate.xml match key |

## Notes

- SOLR-2908 and SOLR-10928 are Improvements. A design-sized Improvement gets a scoping note, so SOLR-10928 is a scoping note and SOLR-2908 is a stale-check.
- SOLR-11650 carries credentials. The ticket's own comment says the hashed value can be recovered. No draft and no exploit detail is written here.
- Fix versions 4.9 and 6.0 on SOLR-2908 are not relied on.

## Limits

- Jira text and GitHub PR search only. No reproduction, no code trace on main.
- Stale-check drafts ask whether the ticket still reproduces on a supported release. They state no current-release result.
- Mentions use the Jira reporter field, not a checked committer sponsor.
- No Jira or GitHub write. Each draft needs Nick's review and go.

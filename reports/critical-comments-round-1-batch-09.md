# Critical tickets comment coverage, batch 09 (last batch)

Claim: `claims/critical-comments-round-1-batch-09-vm2.md`. Assignment: `assignments/critical-tickets-comments-round-1.md`. Census: `material/critical-tickets-census-2026-10-24.md`.
Read live from the Jira REST API on 2026-10-25, including Jira comments that mention PRs. GitHub PR search and PR reads on apache/solr. Board: records for these keys. Every draft is DRAFT, not posted.

## Dispositions

| Ticket | Type / status | Owner PR or branch | Disposition | Shape or reason code |
| --- | --- | --- | --- | --- |
| SOLR-14200 | Bug / Patch Available, 2 comments (2020; 2026 updated) | owner PR #5199 open, not mentioned in Jira | draft | status-note: the PR and what its description says the bug is |
| SOLR-16155 | Bug / Patch Available, 5 comments (2022 to 2024) | owner PR #5200 open; a second open PR #1151 by another committer | no draft | `security-route`: the defect puts field values, which may be sensitive, into logs and responses. Flag only. Both PRs are on the board's records only through this report. |
| SOLR-5735 | Task / Open, 11 comments (2014), assignee Mark Miller | owner PR #5208 open, not mentioned in Jira | draft | status-note: the PR and what it changes in the safe-leader chaos tests |
| SOLR-6213 | Bug / Open, 14 comments | owner PRs #5209 and #5210 open | no draft | `already-told`: the owner's own Jira comment of 2026-10-23 names both PRs |
| SOLR-18402 | Improvement / Open, 2 comments (2026) | PR #4829 open by another contributor, ready for review | no draft | `active-thread`: the PR author's most recent comments are about the follow-up work |
| SOLR-18341 | Improvement / Open, no comments | none | draft | scoping: declaration on the request type or the call, and its default |

## Notes

- SOLR-16155 is about sensitive field values reaching logs and responses. Per the assignment, the security-sensitive flag is recorded here with no public draft and no description of the exposure. Its open owner PR #5200 is public on GitHub. Whether to mention it in Jira is a question for Nick, so no draft is written.
- SOLR-16155 also has an open PR #1151 by another committer. The owner's PR is not a duplicate of that one, and no comparison is made here.
- SOLR-6213's owner comment of 2026-10-23 is already on the ticket, so no status note is written.
- SOLR-14200 and SOLR-5735 are status notes for owner PRs not mentioned in Jira. Each draft needs Nick's approval before anything is posted.
- SOLR-18341's link to SOLR-18402 is named in the draft. SOLR-18402 is an active thread, so it has no draft of its own here.

## Limits

- Jira text, GitHub PR search and PR reads only. No reproduction, no code trace on main.
- Status-note drafts describe PR descriptions at the time of reading. They do not state merge or test outcomes.
- Mentions use the Jira reporter or assignee, not a checked committer sponsor.
- No Jira or GitHub write. Each draft needs Nick's review and go.

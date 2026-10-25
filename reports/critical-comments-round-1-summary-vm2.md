# Critical tickets comment coverage, round 1: completion summary (vm2)

Assignment: `assignments/critical-tickets-comments-round-1.md`. Census: `material/critical-tickets-census-2026-10-24.md` (86 tickets, 9 batches of 10 and one of 6). Written 2026-10-25 from the batch reports, not from memory.

## Coverage

| Batch | Claim / report | Drafts | No-draft reason codes |
| --- | --- | --- | --- |
| 01 | phone-agent-2 (claimed, not vm2's work) | not counted | not counted |
| 02 | `reports/critical-comments-round-1-batch-02.md` | 10 | 0 |
| 03 | `reports/critical-comments-round-1-batch-03.md` | 8 | 2 |
| 04 | `reports/critical-comments-round-1-batch-04.md` | 9 | 1 |
| 05 | `reports/critical-comments-round-1-batch-05.md` (+ correction 1) | 8 | 2 |
| 06 | `reports/critical-comments-round-1-batch-06.md` (+ correction 1) | 10 | 0 |
| 07 | `reports/critical-comments-round-1-batch-07.md` | 9 | 1 |
| 08 | `reports/critical-comments-round-1-batch-08.md` | 6 | 4 |
| 09 | `reports/critical-comments-round-1-batch-09.md` | 3 | 3 |

Batches 02 to 09 cover 76 tickets: 63 drafts and 13 no-draft reason codes. With batch 01 (10 tickets, claimed by phone-agent-2), the census of 86 is accounted for.

## Drafts by shape (63 in total)

| Shape | Count |
| --- | --- |
| stale-check | 33 |
| scoping | 18 |
| status-check | 7 |
| status-note | 4 |
| covered-by-link | 1 |

## No-draft reason codes (13 in total)

| Code | Count | Tickets |
| --- | --- | --- |
| already-told | 6 | SOLR-10950, SOLR-17143, SOLR-7394, SOLR-17767, SOLR-14788, SOLR-6213 |
| security-route | 3 | SOLR-11650, SOLR-18010, SOLR-16155 |
| active-thread | 3 | SOLR-12801, SOLR-17433, SOLR-18402 |
| covered-by-link | 1 | SOLR-14311 (to SOLR-14232) |

## Needs Nick before anything posts

- Four status notes name owner PRs (SOLR-16655 #5093, SOLR-7550 #5139 and #5002, SOLR-14200 #5199, SOLR-5735 #5208). Each is an owner-work note for Nick's go.
- Three security-route tickets (SOLR-11650, SOLR-18010, SOLR-16155) carry no public draft. SOLR-18010's fix commits are already on main and branch_10x, and the owner's PR #5118 is open. SOLR-16155's owner PR #5200 is public on GitHub. Whether to mention either in Jira is Nick's call.

## Follow-ups

- SOLR-14232 needs its own draft. SOLR-14311 is covered-by-link to it, and SOLR-14232 is Open and not drafted anywhere.
- SOLR-16139 is round 1 critical implementation triage candidate 3. Its batch 06 draft is a status-check, not a fix.
- SOLR-17291 and SOLR-17370 are also triage candidates. Their batch 07 drafts are stale-checks.
- Batch 01 is claimed by phone-agent-2. This summary does not count it.

## Limits

- Counts come from the batch reports as published. Two correction files (batch 05 and batch 06) fix wording only, and do not change counts.
- Jira text and GitHub PR search only. No reproduction, no code trace on main.
- No Jira or GitHub write.

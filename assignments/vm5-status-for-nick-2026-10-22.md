# Status for Nick from vm5: newdev and Blocker/Critical review round 1

Status: OPEN

- Intended host: any pool host with a chat channel to Nick. Read this file and tell Nick its contents in plain language. No pool work is needed from the reader.
- Posted by: vm5, 2026-10-22, because Nick could not see vm5's replies in the chat.
- Source: `assignments/pool-newdev-critblock-review-round-1.md` and the claim files named below.

## Why this file exists

vm5's replies in the chat were not visible to Nick. This file is the record instead. Everything below is also on `coordination` with the commit that added it.

## What the review round did

The round reviews 45 units (Blocker, Critical and newdev implementations). Each unit gets three review passes, each with a regate on the head it reviewed.

| Unit | Ticket | Outcome | Who | Where to look |
| --- | --- | --- | --- | --- |
| 1 | SOLR-17842 | DONE, three green passes, no repairs | vm5 | `claims/pool-newdev-critblock-review-round-1-solr-17842-submit.md` |
| 2 | SOLR-18297 | vm5 stood down; vm3 holds it (vm3 claimed first) | vm3 | `claims/pool-newdev-critblock-review-round-1-solr-18297.md` |
| 3 | SOLR-18458 | claimed by vm4 | vm4 | `claims/pool-newdev-critblock-review-round-1-solr-18458-submit.md` |
| 4 | SOLR-18552 main | NEARLY: passes 1 to 3 partial; launcher and bats leg waits on vm1 | vm5 | `gates/SOLR-18552-launcher-bats.md` (OPEN) |
| 5 | SOLR-18552 9.x | NEARLY: passes 1 to 3 partial; owner question on a `solr.cmd` default; no changelog; vm1 leg | vm5 | `claims/pool-newdev-critblock-review-round-1-solr-18552-sharedarena-9x.md` |
| 6 | SOLR-11310 | DONE, three green passes; proof leg rerun red at base | vm5 | `receipts/SOLR-11310.md` |
| 7 | SOLR-11650 | HANDED BACK for implementation: a raw `/` or `?` in a password is not redacted in the URL log and error text | vm5 | `gates/SOLR-11650-review-pass-1.md` |
| 8 | SOLR-12743 | ENDED: the patched class is absent on the fork pin and upstream main | vm5 | `reports/newdev-critblock-review-round-1/solr-12743-submit.md` |
| 9 | SOLR-14200 | HANDED BACK for implementation: silent empty result in SignificantTermsStream; divide by zero in DeepRandomStream | vm5 | `gates/SOLR-14200-review-pass-1.md` |
| 10 | SOLR-14638 | ENDED: branch parked under the owner's review; no gate owed | vm5 | `claims/pool-newdev-critblock-review-round-1-solr-14638-submit.md` |
| 11 | SOLR-15326 | DONE, three green passes with minor notes | vm5 | `receipts/SOLR-15326.md` |
| 12 | SOLR-16155 | DONE, finished by vm3 (takeover) | vm3 | `receipts/SOLR-16155.md` |
| 13 to 25 | various | claimed by other hosts (see the claim directory) | others | `claims/pool-newdev-critblock-review-round-1-*.md` |
| 26 | SOLR-8767 | no claim found | none | unclaimed |

## Decisions that need Nick

1. Delete the banked branch `solr-12743-submit`. Agents do not delete branches. Owner OK needed.
2. Unit 5 (SOLR-18552, 9.x): add the matching `solr.cmd` default on the 9.x line? vm5 recommends yes. No answer is recorded yet.
3. Retire or keep the branches flagged as retire candidates: `solr-14200-submit`, `solr-15326-submit`, and `solr-17433-17143-submit`.
4. SOLR-16155: whether to open a PR, given upstream PR #1151 covers the ticket. The receipt says opening is Nick's call.

## Implementation handbacks waiting on a host

- SOLR-11650: fix `URLUtil.redactUserInfo` so a raw `/`, `?` or `#` in a password is redacted (`solr/solrj`).
- SOLR-14200: decide the failure for an empty shard list in `SignificantTermsStream` and `DeepRandomStream`.

## Things to check

- The coordination commit `554e008b8e` ("vm5: claim SOLR-13943 lost-alias regression test and re-gate") is not part of vm5's review work. The reader should confirm which session made it before relying on it.
- vm5's scheduled wake for unit 12 pass 3 did not produce that pass; vm3 finished it. Nothing was lost, but vm5's report of unit 12 should be read from the receipt.

## Limits

- Read only. Do not post to GitHub, JIRA or email. Do not change any branch or claim.

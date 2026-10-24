# Reply: vm6 loose ends, round 1

From the phone agent, 2026-10-24. Item numbers follow `assignments/vm6-loose-ends-round-1.md`.

1. SOLR-17861 LICENSE and NOTICE scope: with the owner. The question (option A: close the ticket on the checksum slice at `92aed103a0` and move LICENSE and NOTICE presence to SOLR-17970) has been put to Nick and is on his open list. When he answers, the phone agent records the answer in a new file carrying a `Closes:` line for `assignments/phone-agent-17861-license-scope-and-handover-round-1.md`.
2. Index generator change: reviewed and accepted. Skipping a file named in a DONE file's `Closes:` line, and skipping a CLAIMED file whose claim carries a `DONE:` line, matches the derived-state design (state comes from the records, not from hand-edited summaries). No different rule requested.
3. SOLR-18569 gate file wording: decided. Leave the sentence as it is. Gate job files are never edited, and the sentence is inert: the results live in `reports/pr-5211-test-review-gate-round-1.md` and `receipts/SOLR-18569.md`, so nothing is missing and nothing needs correcting in the gate file.
4. SOLR-18556 option choice: with the owner. Options (a) document now, (b) a format-selection guard in `CuVSCodec`, (c) a pin, plus approval or rejection of the Jira draft at `jira/SOLR-18556-comment-draft-round-1.md`, are on Nick's open list with vm6's recommendation of (a). The closure file stays NEEDS-OWNER until he answers.
5. PR 5211 drafts housekeeping: noted. The posted text of record is `drafts/pr-5211-review-posted-round-1.md`. The three earlier drafts are superseded; nothing may be posted from them.
6. Standing work: noted. Misc box item 1 (the cache-versus-authorization benchmark) and the day scan remain available standing work; no action needed now.

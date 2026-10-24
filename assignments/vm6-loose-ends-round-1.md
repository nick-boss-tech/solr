# Assignment: loose ends from vm6 for the phone agent

Status: OPEN. Posted by vm6, 2026-10-24, at Nick Shanin's direction. For the phone agent to carry. Nothing here asks a worker to change a shared file.

## 1. SOLR-17861 LICENSE and NOTICE scope (owner decision)

- The checksum slice is done on `solr-17861-ui-license-check` (`022acd4ec9`, tip `92aed103a0`). Receipt: `receipts/SOLR-17861.md`.
- Still open: whether per-jar LICENSE and NOTICE presence belongs in this ticket. Recommendation (option A): close the checksum slice, and move LICENSE and NOTICE to the desktop distribution work in SOLR-17970. Reasons are in `assignments/phone-agent-17861-license-scope-and-handover-round-1.md`.
- The index lists `assignments/phone-agent-17861-license-scope-and-handover-round-1.md` as an open assignment, not as waiting on an owner. Please take the question to Nick and, once he answers, record the answer with a new file that carries `Closes:`.

## 2. Change to the index generator (review)

- `pool/bin/open-work.py` now skips a file named in the `Closes:` line of a DONE file, and skips a CLAIMED file whose claim has a `DONE:` line (commits `e70768acf9` and `613072e799`). The index was regenerated with it.
- Please review the change and the regenerated `OPEN-WORK.md`. If you want a different rule, say so in a new file; the script is shared.

## 3. SOLR-18569 gate file wording (decision)

- `gates/SOLR-18569-pr5211-test-review-f00c7bde83bf.md` says "Filled in by the gating host after the run". WORKFLOW.md says gate job files are never edited. The results are in `reports/pr-5211-test-review-gate-round-1.md` and `receipts/SOLR-18569.md`, so nothing is missing.
- Decision for the phone agent: leave the sentence as it is, or record a correction in a new file. Do not edit the gate file.

## 4. SOLR-18556 option choice (owner decision)

- Report `reports/solr-18556-round-1.md` and JIRA draft `jira/SOLR-18556-comment-draft-round-1.md` (not posted). Closure `assignments/solr-18556-closure-round-1.md` is `NEEDS-OWNER`.
- Nick needs to choose among (a) document now, (b) a format-selection guard in `CuVSCodec`, (c) a pin, and to approve or reject posting the JIRA draft. The recommendation is (a) now.

## 5. Drafts for PR 5211 (housekeeping)

- Posted text: `drafts/pr-5211-review-posted-round-1.md`. Earlier drafts `drafts/pr-5211-test-review-comment-round-1.md` (round 1, wrong filename), `drafts/pr-5211-review-with-diff-round-2.md` and `drafts/pr-5211-review-with-diff-round-3.md` are superseded. Nothing else may be posted from them.

## 6. Available standing work (no action needed now)

- Misc box item 1, the cache-versus-authorization benchmark: `assignments/misc-box-round-1.md`. Tags `jvm-tests`, `records`.
- Jira day scan: `assignments/work-store-jira-day-scan-round-1.md`, next date is the day before the oldest folder on the board.

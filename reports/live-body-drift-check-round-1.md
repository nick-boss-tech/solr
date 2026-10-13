# Live PR bodies against their drafts: drift check, round 1

Date: 2026-10-13, at pr-prepare tip `bb3c2a3cbc0`. Read-only: the live bodies were read through the API and compared with the drafts on this branch. Nothing was edited on GitHub.

Method: each draft was cut at its PR text (from the AI header line onward, so staging notes are excluded). Each live body was compared with the draft line by line. Byte-order marks and a trailing newline were ignored.

## Results

| Draft | Live PR | Differing lines | Verdict |
|---|---|---|---|
| `pr-drafts/search-components/mergeids-skipped-shard-totals.md` | 5115 | none of substance | matches |
| `pr-drafts/build-docs/SOLR-18540.md` | 5116 | none of substance | matches |
| `pr-drafts/core-admin/SOLR-18010.md` | 5118 | 7 lines | DRIFTED |

The 5115 and 5116 live bodies already carry the edits made to their drafts. The round 1 addendum synced them.

## The 5118 drift

The live body still has the earlier text. The draft has the edits from the 18010 recheck rounds.

1. Merge-base sentence (the paragraph on the non-atomic write). The draft names the full merge-base SHA and says what the links point to. The live body still says "These links point to merge-base commit 14c7aac0d15."
2. Permission Proof line. The draft says the test expects 0640 to survive an edit, and that no failure value is stated until the reverted check is complete. The live body says "With the permission step reverted, the new test fails with 0644 observed where 0640 was expected." The 0644 value is the one the gate review could not reconcile with the code reading (0600). The live body states it as a result.
3. CoreContainer link in Limits. The draft says "at the merge-base and unchanged by this PR". The live body has the earlier wording.
4. File replacement Limits line. The draft carries the one-off Linux symlink result and says the bind mount was not tested. The live body still says "A symbolic link would be replaced by a regular file ... Neither case was run."

## Why this is held

Syncing 5118 would change a public PR body. The Proof line (item 2) is a claim the records do not yet support, so the sync is more than wording. The owner decisions on the file-replacement behaviour, the permission-copy failure path and the length of the body are still open. The sync needs Nick's go-ahead, and the body should be read back after the edit.

## Proposed next step

When Nick approves: replace the live body of 5118 with the draft text (the AI header through the end, staging notes excluded), then read the body back and compare it with the draft. Do not change the title or any comment.

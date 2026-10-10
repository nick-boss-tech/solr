# SOLR-12703 at the re-gated head: check of the draft and its citations

Claim: `claims/update-12703-regate.md` (commit `b3d3e914126`). Update: commit `4e3af9620c5`, which moved the draft and the receipt to the re-gated head `bdd29ba19900`. The branch change is one changelog line, in `bdd29ba1990`, which replaces the RunUpdateProcessor symptom with "instead of being stored as a field value".

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Two read-only subagents checked the draft against the receipt, and every code citation at the new head. The lead wrote this roll-up. Nothing was applied to the draft, committed to the branch, posted, or opened.

## Result

- **Code citations: clean.** Every link resolves at `bdd29ba19900` or at the base commit where it describes pre-change behavior. The SOLR-6045 link at `bcae04d77bdb` is correct for that branch. The changelog no longer carries the RunUpdateProcessor claim.
- **Draft: fix first.** Three sentences need replacing, as the draft check lists them.
  1. Proof, the pre-fix sentence: "Without the message change, the updated test fails on the earlier head". The receipt names no message change at `ed95d555e62`. Proposed: "The updated test fails on the earlier head."
  2. Proof, the module-check sentence: "The module checks pass". The receipt names one module check, `rc=0`. Proposed: "The module check passes."
  3. Limits, the merge sentence: "The two merge without conflicts in either order". The receipt does not support it. Proposed: "Their combined behavior has not been run." This needs the owner's confirmation.

## Decision for you

Confirm the third replacement, and whether the first two may be applied to the draft as factual corrections.

## Not done

Nothing was applied to a draft. No PR, comment, title, or branch was touched. No build, Gradle run or test was run.

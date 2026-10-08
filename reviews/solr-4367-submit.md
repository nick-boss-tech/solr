# solr-4367-submit

- Branch: origin/solr-4367-submit
- Head: 0af6087f43fa (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind, 2 commits ahead)
- Scope: 2 commits, 3 files. `SpellCheckComponent.java` (+10, a `classname` check at the top of the init loop, `:695-706`), `SpellCheckComponentTest.java` (+13, `testSpellcheckerOutsideSpellcheckerListIsRejected`), changelog `SOLR-4367-spellcheck-misplaced-config.yml` (`type: fixed`). No `SOLR-4367-TESTING.md` on the tip.
- Verdict: Needs work (the check covers one of the misplaced keys; the others are still silently ignored)
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-4367-review.md` (verdict Needs work) was written at the same head (0af6087f43fa). No delta.

- Bulk F1 (validation only catches a misplaced `classname`): **confirmed.** See finding 1.

## Findings (ranked)

1. **MEDIUM, verified. Only `classname` is validated; other misplaced keys are silently ignored.** The new check is `if ("classname".equals(initEntry.getKey()))` (`SpellCheckComponent.java`, the init loop starting near `:698`). The loop's only other branch is `if ("spellchecker".equals(...))`, with no `else`, so any other top-level key is dropped without a message. A component with only a misplaced `name` or `sourceLocation` therefore still starts with no spellchecker and no diagnostic. The test (`SpellCheckComponentTest.java`, diff lines 44-55) supplies `name`, `classname`, and `sourceLocation` together, so it proves rejection of `classname` only. The bulk review's finding holds.
   - Proposed fix (not applied): enumerate the top-level keys the component legitimately reads and throw for every other key. Before writing that list, check which keys the component reads from `initParams` (for example `queryAnalyzerFieldType`, which is read with `initParams.get`, and the `queryConverter` plugins loaded by `core.initPlugins`). Add one test per misplaced key, each alone.

2. **LOW, verified. The diagnostic fails component init, not just this key.** The new check throws `SolrException(SERVER_ERROR)` from `inform`, which runs at core load. A misplaced config therefore stops the core from starting. That is the stated intent in the changelog ("fails with a clear message"), but it is a behavior change. See owner call 1.

## Owner calls (not decided here)

1. **Error or warning for misplaced settings.** The JIRA request, as quoted in the bulk review, supports an error. The pipeline handoff records the open question of failing core startup versus logging a warning. Pose it: should a misplaced spellchecker setting stop core load (current head) or log a warning and skip?

## Checked and not a finding

- The `spellchecker` list form (`<lst name="spellchecker">`) is unchanged. The check only fires on a top-level `classname` key, so it does not affect properly wrapped configs.

## Proposed fixes (not applied; the owner decides)

- Finding 1: allow-list the top-level keys, throw for the rest, and add one test per misplaced key.
- Owner call 1: depends on the answer.

## Not checked

- Not compiled, formatted, or run. No Gradle, no `drain`, no test runs.
- Whether any shipped or test `solrconfig` puts `name`, `sourceLocation`, or `classname` directly under the component (a grep of `solr/core/src/test-files` was not done for this branch). This matters for the startup change in owner call 1.
- The full list of top-level keys the component reads from `initParams` (finding 1's proposed fix depends on it).
- The JIRA ticket text was not re-read here; the quoted request comes from the bulk review.
- No GitHub or JIRA writes were made.

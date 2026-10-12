# Docs-only commit rule, review round 1

Verdict. The premise about where the handoff notes sit holds. The premise that the build never reads them does not hold as written. The rule is restated to cover pure deletions only, which is what the packaging commits do, and that restatement stands on the evidence below.

Inputs: `assignments/pool-docs-only-gate-rule.md` (commit `b4ea175a030`), claim `claims/pool-docs-only-gate-rule-round-1.md`. Two read-only subagents did the checks. Nothing was pushed, fetched beyond ls-remote, or posted.

## Premise 1: the notes are root-level Markdown outside src/. Holds.

- 328 fork branches were listed with ls-remote. 322 tips are in the local object store.
- Every TESTING-named path on those tips is a root-level `.md` file. No match is under any `src/` directory, and no match is a non-Markdown file.
- Net diff against the merge-base: 19 branches still hold their note at the tip. Commit level: 323 entries on 163 branches, all root-level Markdown.
- Six tips could not be checked and were not fetched: solr-10234, solr-13097, solr-13568, solr-1877, solr-5589 and solr-8554.
- Note names do not always match the ticket. solr-9637 carries `SOLR-17393-TESTING.md`. Each of these is still root-level Markdown.

## Premise 2: no build or tool reads the notes. Does not hold as written.

The subagent searched upstream/main and the four fork tips for the note names and for `TESTING` and `handoff`. No build, test or changelog script names a handoff note. Three things do read them, though:

1. **Root-level lint.** The root `rat` task scans every tracked root file, and `validateSourcePatterns` reads root `.md` files. Under default settings a note with no license header would fail RAT. `gradle/validation/rat-sources.gradle` and `gradle/validation/validate-source-patterns.gradle`.
2. **CI changelog classifier.** `.github/workflows/validate-changelog.yml` matches any changed root `.md` name, so a note counts as docs-only.
3. **Gate scope.** Every receipt records its module check as `:solr:core:check -x test`. That is a module-scoped task, and the root tasks above are not part of it. Confirmed from the receipts, not from running anything.

Consequence. A note's presence can affect root lint and the CI classifier, so the rule must not cover anything except deletion. Deleting a file cannot add a finding, and the module check does not read it.

## Restated rule

> Build and tests passed at gated head X. Then we deleted `SOLR-NNNN-TESTING.md`, a handoff note that nothing compiles or tests.

Scope: pure deletions of root-level handoff notes only. Additions and edits keep the current rule.

## Decision

Accepted, as the owner directed. The one-sentence rule replaces the longer draft in `assignments/pool-docs-only-gate-rule.md`.

## Pushes still pending

Four packaging commits would delete notes from submit branches that have open PRs: SOLR-10364, SOLR-11356, SOLR-11678 and SOLR-12161. Each would change a PR head. Under the rule the deletion inherits the gate at the named head, and each receipt names that gated head. Pushing them is a PR action, so it waits for the owner's yes.

Current state: the four branches still hold their notes at the remote tip. None of the deleting commits exists on the remote.

## Limits

- Premise 2 was checked by reading the scripts and workflows, not by running them.
- The receipt heads `efb1e6717f`, `6120dae28d`, `611eae2464` and `cdd265168a` are absent from the object store, so the receipts' pairing with the four tickets was taken as written.
- The 57 branches that delete their note in an earlier commit were not examined.

# Build, docs and misc round 1, part G3: SOLR-17252 and SOLR-17842

Audit only. Nothing was posted, pushed, edited or run. No builds, tests, gates or Antora runs. Evidence is the branch diffs, the shipped files on main, the Jira JSON and the receipts. Condensed by the lead from the subagent's final report. The SOLR-17252 draft is in `pr-drafts/build-docs/SOLR-17252.md`; the SOLR-17842 draft is in `pr-drafts/build-docs/SOLR-17842.md`.

Shared finding: the logs the receipts name are not in the dev workspace. `receipts/SOLR-17252.md:5` names `g17252r35-harness.log` (the 21 checks); `receipts/SOLR-17842.md:5` names `g17842-lightgate.log` (the tidy result). Both Proofs rest on the receipts' wording alone.

## SOLR-17252 (`solr-17252-submit`, head `d730a266a042`): draftable on the recorded state, HOLD

Refs: head matches the live tip and `receipts/SOLR-17252.md:4`. Merge-base `14c7aac0d151`; the receipt records no base. Main read `3f5d4c5bf8ac`, 70 commits past the base. The cited lines in `releaseWizard.py` (136, 193-199) are the same at base. Commits `f6a0b44d8ca` and `d730a266a04`. Jira read: Open, Critical, component release-scripts, reporter Gus Heck, created 2024-04-23, three comments.

Verdict: the premise holds on main (EDITOR set to nano passes through with at most a warning, `releaseWizard.py:196-199`). Both receipt findings hold on reading the shipped script. Scope is three files, two commits. The evidence is the local check script from the receipt, not a repository test.

Evidence:
- Scope: 3 files, +23 and -2 (`dev-docs/releasing.adoc`, `dev-tools/scripts/README.md`, `dev-tools/scripts/releaseWizard.py`).
- Name set: `releaseWizard.py:76` `TERMINAL_EDITORS = {"vi", "vim", "nano", "pico", "emacs"}`.
- Matching at `:194-199`: `shlex.split` (ValueError falls back to split), then `os.path.basename` of the first word. `emacs`, `/usr/bin/emacs` and `emacs -nw` match; `emacsclient`, `code --wait` and `nvim` do not. `env nano` does not, because the first word is `env`.
- Exit at `:202-219` (the sys.exit at `:207-211`). Main's `get_editor` (`:193-199`) warns for the exact five strings and continues.
- Eager call (Finding 2 holds): `expand_jinja` (`:98`) builds its globals with `'editor': get_editor()` at `:137`, so every `expand_jinja` call runs the check.
- First call on the branch: `check_prerequisites()` at `:1380`; `store_rc` at `:1378`; `ReleaseState` at `:1386`; `state.save()` at `:1392`; the checklist loop at `:1414` reaches `get_editor` through `expand_jinja`. **The first `get_editor` call is after `state.save()`.**
- Docs (`releasing.adoc:56-58`, `README.md:52-54`) name the five editors as terminal-attached. Neither says GUI emacs is refused too, which the code does.
- Why it matters: `releaseWizard.yaml:450` runs `{{ editor }} .asf.yaml`; `:1534` runs `{{ editor }} {{ solr_news_file }}`.
- No unit test for `releaseWizard.py` exists on main. Both commits are by Nick Shanin, with no trailers.

Changelog: none. The branch adds no changelog fragment. The assignment's no-changelog convention covers ref-guide-only docs; this is dev tooling, and whether Solr wants a fragment for it was not confirmed.

Receipt disagreements:
1. `receipts/SOLR-17252.md:8` says "Q3 the check stays at startup, before release state exists." The shipped check runs after `state.save()` (`:1392`). The receipt implies the shipped placement meets Q3; the code does not.
2. `receipts/SOLR-17252.md:6` says "Finding 3 dispositioned: the docs match the implemented behavior." The docs list the same five names but do not say GUI emacs is refused. A wording gap, not a list mismatch.
3. `receipts/SOLR-17252.md:5` says "21 checks, all pass." The log is not in the workspace; the count is from the receipt only.
4. `receipts/SOLR-17252.md:4` says "unchanged since the 2026-10-04 DONE record." Only the tip was checked.
5. The receipt's mechanism claims for Finding 1 and Finding 2 agree with the code.

Owner decisions:
1. Placement (Q3). (a) Keep the check after `state.save()` at `:1392`, and correct receipt line 8. (b) Move the check into `check_prerequisites()` (called at `:1380`, before `ReleaseState` at `:1386`), so no release state is written before a refused EDITOR; the rc file from `store_rc` is still written first. Recommendation: (b). It matches the recorded position and is a few lines. It needs a new head and a rewritten "What this change does".
2. Emacs in the list (Q2). (a) Keep emacs in the list, and add one docs sentence saying GUI emacs is refused too. (b) Narrow to terminal mode. Recommendation: (a) for this PR, with the narrowing posed as the Choice.
3. Exit or warning, and the ticket's own options (Q1). Jira comment 17840001 (Jan Høydahl, 2024-04-23) suggests a warning. Comments 17841653 (Gus Heck) and 17842853 (Jan Høydahl) propose and test a tmux wrapper. The draft now names warn-only and tmux as Choice options, since both are live alternatives a maintainer could pick. Confirm the wording.

Not checked: the script, the check, the wizard and a terminal session were not run. Findings 1 and 2 are confirmed by reading only. `g17252r35-harness.log` was not found (searched the dev workspace, pruning `source`, `wt`, `.git`, `node_modules` and `env/patched-source`). The round 35 draft `goal files/reviews-2026-10-07-round35-bulk/17252.md` was not found, so the draft could not be compared with it; it is written from the branch. Windows behavior of the check (backslash paths) and nvim and wrapper behavior were read, not run.

## SOLR-17842 (`solr-17842-submit`, head `008973f63133`): draftable as instructions only, WAITING on the owner's scope call

Refs: head matches the live tip and `receipts/SOLR-17842.md:4`. Merge-base `14c7aac0d151` matches the receipt. Main read `3f5d4c5bf8ac`. The diff from base to main is empty for `solr/benchmark`, `dev-docs` and the tools README, so the existence checks hold at base too. Commits `188b4bd10ce` and `008973f6313`. Jira read: "Publish benchmarks for important features", Blocker, Open, reporter Ishan Chattopadhyaya, created 2025-08-07, no comments. The ticket asks for per-release benchmarks, comparisons across releases and against other engines, and vector search numbers on external leaderboards.

Verdict: the existence claims hold on main. **One fix belongs on the branch before posting: the example's `<build>` placeholder is an unquoted `<`, which bash reads as a redirect.** The guide's example line (line 69) as written does not run. This round does not commit to submit branches, so the fix is the branch owner's to make.

Evidence:
- Scope: 2 files, +91 (`solr/benchmark/README.md` +2 at lines 201-202; `solr/benchmark/docs/release-benchmarking.md` new, 89 lines). No Java, no changelog.
- The guide's scope statement (lines 20-22): "The remaining release work is choosing a small, repeatable benchmark set and publishing the results." The branch does not publish results.
- Benchmark table (lines 41-48): eight classes, all public on main under `solr/benchmark/src/java/org/apache/solr/bench/`, with packages matching the guide.
- Example at line 69: `./jmh.sh SimpleSearch JsonFaceting FilterCache -f 1 -wi 5 -i 5 -rf json -rff work/jmh-<build>.json`. `solr/benchmark/jmh.sh` exists on main (mode 100755). It must run from `solr/benchmark` (lines 19-26) and passes its arguments to JMH (line 63). Without a lib directory it runs the Gradle jar task (lines 29-40).
- Placeholder defect: an unquoted `<` is a redirect in bash and zsh. Fix: a plain placeholder such as `work/jmh-BUILD.json`, or a quoted name.
- Lines 55-61 (settings to keep stable) and 63-73 (JSON output and `primaryMetric.score`): content read, not run.
- README pointer (line 201) resolves to the new file.
- On main, `solr/benchmark/README.md` has no "release" text, and `docs/` holds only the two profiler notes, so the guide does not repeat existing text.
- Both commits are by Nick Shanin, with no trailers.

Changelog: none. Docs-only, so the no-changelog convention applies.

Receipt disagreements: none found on head, base, file list or the existence claims. `receipts/SOLR-17842.md:7` matches the Jira text. The light-gate log is not in the workspace, so the tidy result is from the receipt only.

Owner decisions:
1. Scope of SOLR-17842. (a) Keep the ticket open; this PR refers to it and does not close it. (b) Narrow the ticket to a release-benchmarking guide and close it on merge. (c) Hold the PR until published results exist. The round 35 recommendation, as `receipts/SOLR-17842.md:7` records it: keep the issue open unless the owner narrows the outcome. Recommendation: (a), with the title as drafted so the PR does not claim the ticket's results.
2. Placeholder fix (a new commit on the submit branch, which the owner or the branch's author must make). Recommendation: a plain placeholder such as `work/jmh-BUILD.json` with one sentence telling the reader to replace BUILD. Then rewrite the draft against the new head.

Not checked: the light-gate log; `jmh.sh`, the example and any benchmark were not run; the `primaryMetric.score` key was not checked against a real run; whether JMH creates `work/` when `-rff` points into it; whether the bare class-name patterns select only the intended benchmarks; the Jira "blocker for 10.0" status against release state.

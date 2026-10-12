# CLI convergence, round 2

Verdict: **NOT CONVERGED.** SOLR-10667 is the only draft in scope. Its blocking item (CLI-R1-04, the `solrclient/` exclusion) and its changelog title (CLI-R1-05) have not landed on the branch. The other three drafts are checked for their branch-side titles only, and those titles have not landed either.

Counts: items re-checked 8 (CLI-R1-03, 04, 05, 14, 17, 06, 07, and the OWNER NOTE hold line). Heads checked 4, all matching their drafts. Dispositions confirmed 2 (CLI-R1-06 and 07) and the hold line. Dispositions disputed 0. Items still open 5. New findings CLI-R2-nn: 1 (note).

Scope: `assignments/pool-cli-convergence-round-2.md`, checked against `reports/cli-convergence-round-1.md`. One read-only subagent did the review. Nothing was edited, posted or built.

## Verdict per draft

- **SOLR-10667: NOT CONVERGED.** The head `1e3f022552be` matches the draft, the OWNER NOTE and the changelog link. The tip has not moved since the draft was written, so no re-gate is owed yet. The receipt records GATE GREEN at that head on 2026-10-10.
- **SOLR-9342, SOLR-16272, SOLR-16813: out of convergence scope.** Their branch-side titles are checked below. Each draft's changelog link already names the live head, so no link re-point is owed yet.

## State lines

- **CLI-R1-04 (SOLR-10667, blocking): OPEN.** At `1e3f022`, `gradle/solr/packaging.gradle` lines 83 to 85 copy the example directory with no exclude, and "solrclient" does not appear in the file. The copy-all Change sentence and the missing Limit are the text the OWNER NOTE holds, which matches the main side's response. Owner decision 10 reads "exclude solrclient/** (recommended)" and is not reopened. The change needs a branch edit, a new head and a re-gate.
- **CLI-R1-05 (SOLR-10667, branch-side title): OPEN.** The live title is "The Solr distribution now includes the Learning To Rank example files (modules/ltr/example)." The round 1 replacement adds "full" after "The".
- **CLI-R1-03 (SOLR-9342, branch-side title): OPEN.** The live title at `833e11192a7f` still says "bin/solr now exports TZ from SOLR_TIMEZONE ... unless TZ is already set." It contradicts the draft's Change text, which says the script does not export TZ for itself.
- **CLI-R1-14 (SOLR-16272, branch-side title): OPEN.** The live title at `d2cf8173917` still ends "...uninstall no longer refuses".
- **CLI-R1-17 (SOLR-16813, branch-side title): OPEN.** The live title at `1b288170e8aa` still begins "A node that joins a cluster with installed packages now also fetches...".

## Head check

- `solr-10667-submit` at `1e3f022552be4d281a020e015f4c8c05ce4eec6c`: matches.
- `solr-9342-submit` at `833e11192a7ff918f7e401bde920cf63c89df063`: matches.
- `solr-16272-submit` at `d2cf817391692b29c3d221ec4ebed65c3833bb91`: matches.
- `solr-16813-submit` at `1b288170e8aa9c3b1ea9bce418e26bd61f08498e`: matches.

## Dispositions verified

- **CLI-R1-06:** the draft's line 11 matches the replacement word for word. The base holds seven tracked files under `solr/modules/ltr/example`.
- **CLI-R1-07:** the draft's line 42 matches the replacement word for word. The base reference page at line 788 still says the example folder is not shipped in the binary release.
- **OWNER NOTE hold (line 1):** matches the main side's recorded text word for word.

## Register

**CLI-R2-01 | SOLR-10667 | note.** Owner decision 10 also lists "dotfiles" and "the target branches beyond main" as owner items. The response adopts only the `solrclient/` exclusion and records no disposition for the other two. The draft's Change text states the `.gitignore` copy openly, so this is a record gap, not a text error. No replacement text is needed at note grade.

## Not checked

- The receipts ledger, the takeover log and security material.
- The branch-side title commits are not on the branch, so their landed state is checked by reading the title only.

## Convergence line

NOT CONVERGED for SOLR-10667. CLI-R1-04 (blocking) and CLI-R1-05 (should-fix) are open. The three title items wait on branch commits.

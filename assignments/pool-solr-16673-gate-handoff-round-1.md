# Pool assignment: SOLR-16673 gate handoff, round 1

Status: DONE (2026-10-19, vm5): both gates green on apache/main. uniqueKey head a494962d79, rootfield head 72e505cc20. Deliverables: receipts/SOLR-16673.md (round 2 sections), gates/SOLR-16673-uniquekey.md and gates/SOLR-16673-rootfield.md (DONE).

Round 2 (vm5): both heads were re-tested. Both gates are blocked at step 2 (Error Prone) on a base-file warning, and vm1 has a claim on a superseded rootfield head. See reports/SOLR-16673-handoff-review-round-2.md and the gate files Result r2. The HOLD is lifted for the uniqueKey gate only after the step 2 decision.

Picks up the two Linux hardening gates that stopped at step 1 (tidy) on 2026-10-19. See `gates/SOLR-16673-uniquekey.md` and `gates/SOLR-16673-rootfield.md`, each with a Result section. Neither branch has been changed. Both gates run in this round, one after the other, on one host.

- Capability tags: gate.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md). Name the claim `claims/pool-<host>-gate-handoff-16673-round-1.md`.
- Posted by: vm5, 2026-10-19, at the direction of the main agent.

Context: tidy on each branch removes `oshai-logging = "8.0.4"` from `gradle/libs.versions.toml`. The same removal happens on the merge-base `e044bf20b405b8ca985730be3553d77d1fc8ff31`, and the build never uses that entry, so the drift is in the shared base and not in either branch. The Decisions table below says how step 1 treats it.

## Items

1. **Claim and confirm heads.** Write the claim file. Confirm with `git ls-remote origin refs/heads/solr-16673-uniquekey` and `refs/heads/solr-16673-rootfield` that the heads are `70c6eb0c49008cf9518c456dedd6c4981642d52c` and `5fb792482fc3d8fef851513bb91eb3231409a033`. If either has moved, record the new tip and stop that gate.
2. **Gate solr-16673-uniquekey.** Follow `gates/SOLR-16673-uniquekey.md` from step 0 through step 5, with step 1 handled by the Decisions table below. Start from a worktree at the branch head. Output: the gate file's Result section for this round, and the receipt section in item 4.
3. **Gate solr-16673-rootfield.** Same as item 2 for `gates/SOLR-16673-rootfield.md`, starting after item 2 is finished and its worktree is removed.
4. **Receipt and job marks.** On green, add a new top section to `receipts/SOLR-16673.md` for each branch that went green: head, gate date and host, log path, every step result, proof-leg test name and failure text, and focused counts from the fresh XML. Preserve the existing sections. Mark each gate file DONE, and mark the claim DONE, in the same fast-forward push as its receipt. A gate that stops does not get a receipt; it records its result in its gate file and its claim ends with the reason.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Step 1 tidy drift in gradle/libs.versions.toml | decided: restore the one line | If tidy's only change is deleting the line `oshai-logging = "8.0.4"` from `gradle/libs.versions.toml` (confirm with `git status --porcelain` and `git diff`: one file, one deletion), run `git checkout -- gradle/libs.versions.toml`, record the diff in the gate file, and continue with step 2. The worktree must be clean before step 2. If tidy changes any other file or line, mark GATE INVALID, record the tidy diff, and stop. | main agent (vm5 proposal, awaiting Nick's confirmation) |
| Tip moved at claim time | decided: stop | Record the new tip and stop that gate. Do not retarget the job. | main agent |
| Proof-leg failure shape | decided: exact failure only | Uniquekey: `testNumericUniqueKeyDocIds` fails with `ClassCastException` at base `SchemaDesigner.java`. Rootfield: `testAnalyzeNestedDocsWithNumericUniqueKey` fails with the nested `_root_` and uniqueKey type mismatch, or the loaded `_root_` type assertion reports expected `plong` but actual `string`. Any other failure shape: record it and stop. Do not edit the test or rerun. | main agent |
| Focused head run fails after execution | decided: record and stop | Record the failing test, failure text, seed, and XML counts. Do not relaunch. | main agent |
| Receipt push races the other gate | decided: serialize | Fetch and rebase before the receipt push, preserving every existing section. | main agent |
| Public text edits | decided: none | No PR opening, PR edit, PR comment, or Jira comment is part of this assignment. | Nick |

## Deliverable

- `receipts/SOLR-16673.md` with a new top section for each green branch.
- Gate files `gates/SOLR-16673-uniquekey.md` and `gates/SOLR-16673-rootfield.md` with their Result sections and final Status line (DONE if green; otherwise the stop reason).
- Claim files marked DONE or ended with the reason.

## Limits

- One gate per host at a time. Build through `pool/bin/sweep-run.sh` under the host lock. Host id, JAVA_HOME and GRADLE_USER_HOME come from `/workspace/AGENTS.md`.
- No PR, PR edit, comment, JIRA or email. No force push. Do not push the ticket branches.
- Commit as Nick Shanin with no trailers. No em dashes in authored text.

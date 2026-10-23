# Gate job: SOLR-5821 (review round 1, pass 3)
Status: DONE
- Branch: solr-16914-submit. Head: `327547d693e098447ff70c536b794aa364508670`, fork tip confirmed 2026-10-23.
- Prior gated head: `327547d693e098447ff70c536b794aa364508670`. Review base: `14c7aac0d151`.
- Intended host: phone-agent-2. Capability tags: gate, npm.
- Module: `:solr:solr-ref-guide`. Focused class and seed: not applicable, docs-only.
- Diff scope: one file, `solr/solr-ref-guide/modules/query-guide/pages/common-query-parameters.adoc`, 3 insertions.
- Why: regate pass 3 after pass 2 GREEN at the unchanged branch tip. The prior receipt specifies a light gate plus Antora, not a Java test gate.
Links: Ticket: SOLR-5821. Category: search. Siblings: none in this unit. Supersedes: pass 2 green regate at the same head.

## What to run
Runner: `/workspace/tools/phone-agent-2-5821-docs-gate.py`, a docs-only adapter using the host standard gate runner's locks, disk guard, clean-tree check, durable step logs and result JSON.
Logs: `/workspace/phone-agent-2-logs/solr-5821-review/pass-3/`.
- Step 0: confirm one-page docs-only scope. Changelog YAML not applicable.
- Step 1: tidy, with only the documented unrelated oshai-logging catalog pruning restored if encountered. Any other change follows Gate repairs.
- Step 2: `:solr:solr-ref-guide:buildLocalAntoraSite --rerun` using the source wrapper, JDK 21 and npm.
- Step 3: inspect the fresh rendered three sentences, each present once, obsolete wording absent, and unique-sort advice retained.
- Steps 2 to 5 of the production gate (Error Prone, fail-before proof, focused Java tests, Java module check): not applicable to this docs-only change; no test count is claimed.
- Require the tracked worktree to remain clean at the exact head.

## Decisions
| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Evidence shape | decided: tidy plus Antora and rendered text check | No Java or changelog in diff; no production test gate | Existing receipts/SOLR-5821.md |
| Ticket fit | decided: retain major ticket-fit finding; do not implement a replica-consistency fix in this round | Docs explain tied sorts, not a production fix to the reopened Critical bug | Prior pass 3 report and assignment repair scope |
| Build failure | default: follow WORKFLOW.md Gate repairs | An environment refusal without executed tests allows one relaunch; an out-of-scope repair is handed back | WORKFLOW.md |
| Public actions | decided: none | Do not open or change PR state, post comments or email | Owner standing limits |

## On green
Write a new receipt section with exact head, host, commands and log paths. Mark this job DONE. Mark the unit DONE only after this regate is green. The coordinator will atomically publish the completed report, receipts, gates and terminal claim.

## Result
- GREEN at `327547d693e098447ff70c536b794aa364508670`, phone-agent-2, 2026-10-23T06:21:49.761946+00:00. No product repair or commit.
- Standalone checkout `/workspace/solr-5821-antora`; tracked tree clean.
- Tidy rc=0; forced Antora rc=0; rendered text checks rc=0.
- Final state `/workspace/phone-agent-2-logs/solr-5821-review/pass-3/state.json`. No Java tests or full checkSiteLinks claimed.
- Receipt top section written. Unit has three GREEN regates; coordinator publishes the atomic closeout.

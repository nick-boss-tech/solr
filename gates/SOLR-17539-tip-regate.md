# Gate job: SOLR-17539 docs-shaped re-gate at the live tip

- Branch: solr-17539-submit. Live tip: f9d201a278b9418ae71d8123ff9269edccd7776a (one ref-guide wording commit, 2026-10-06, on top of the gated head). Gated head: bce505f45acd20f80686cb448e17e7c5717d6f25 (receipts/SOLR-17539.md).
- Why: the live tip has no local gate. The commit is docs-only, but the receipt records no green run at the tip.
- Intended host: any capable Linux host (vm2 or vm1), under the WORKFLOW.md claim rules. Windows never claims build work.
- Capability tags: gate.
- Module: the module the gated run used, as named in logs/g17539-gate.log. The claimant reads it from the log before composing the run.
- Job type: docs-shaped re-gate, the same shape as SOLR-6430's docs gate: the reference guide builds at the tip and the changelog lints. No focused Java tests apply to a docs-only commit.
- Gate steps: changelog YAML parse and changelog lint; tidy with a clean tree afterward; reference guide local build at the tip; module check for the module named in the gate log, `-x test`.
- On green: refresh receipts/SOLR-17539.md with a live-tip section (tip sha, steps, the module named); mark this job DONE and the claim DONE. No fork push: the tip is already on the fork.
- On failure: record the failing step and message in this file; do not relaunch unchanged.
Status: DONE (GATE GREEN at f9d201a278)

## Failure record

- Run: gates/g17539-gate.sh, GATE START 2026-10-13T11:14:30Z, head f9d201a278b9418ae71d8123ff9269edccd7776a, worktree /workspace/gates/solr-17539-gate.
- Passed at the tip: delta is one file (configuration-guide caches-warming.adoc, 1 insertion, 1 deletion); changelog YAML parse rc=0; changelog lint rc=0; tidy rc=0; tidy-clean status empty.
- Failed: reference guide build `:solr:solr-ref-guide:buildLocalSite` rc=1 in `buildLocalAntoraSite`, message: "Local content source must be a git repository: /workspace/gates/solr-17539-gate". The gate worktree's `.git` is a gitfile, which Antora rejects. Environment cause, not a content cause, as far as the log shows; the page itself was not checked (page-check found no built caches-warming.html).
- Not run: module check `:solr:core:check -x test`. The log stops at its start (logs/vm2/17539-check.log, no rc line, no GATE END); no tests executed.
- No green at the tip. Receipt not refreshed.

## Step checklist

- [x] changelog parse and lint at the tip
- [x] tidy on a clean tree
- [x] reference guide build at the tip (rerun in a clone, rc=0)
- [x] module check
- [x] receipt refreshed; job and claim DONE

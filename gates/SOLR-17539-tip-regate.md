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

## Step checklist

- [ ] changelog parse and lint at the tip
- [ ] tidy on a clean tree
- [ ] reference guide build at the tip
- [ ] module check
- [ ] receipt refreshed; job and claim DONE

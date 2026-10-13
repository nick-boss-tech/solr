# Gate job: SOLR-10322 re-gate at the live tip (changelog title only)

- Branch: solr-10322-submit. Live tip: 80ce9d7a3c8a61dfb5c5169a371890f11d9e17ff (verified by ls-remote 2026-10-13). Gated head: 0523a10639dd9a74dfca559503324ebf74103464 (receipts/SOLR-10322.md).
- Why: the one commit past the gate, 80ce9d7a3c8, changes only the changelog fragment title (changelog/unreleased/SOLR-10322-topic-stream-client-cache.yml, one line). No code or test file changes, but the receipt records no green run at the tip.
- Intended host: any capable Linux host (vm2 or vm1), under the WORKFLOW.md claim rules. Windows never claims build work.
- Capability tags: gate.
- Module: :solr:solrj-streaming (the module the gated run used, per receipts/SOLR-10322.md).
- Job type: docs-shaped re-gate. No focused Java test is required for a changelog-only change; the gated count (StreamExpressionTest 34 of 34 at 0523a10639dd) stands for the code, which the tip does not touch.
- Gate steps: changelog YAML parse of the branch fragment; tidy with a clean tree afterward (restore gradle/libs.versions.toml if the build touches it); module check `:solr:solrj-streaming:check -x test`, which compiles main and test sources.
- On green: refresh receipts/SOLR-10322.md with a live-tip section (tip sha, steps, the module named, and the title-only commit named as the only change past the gated head); mark this job DONE and the claim DONE. No fork push: the tip is already on the fork.
- On failure: record the failing step and message in this file; do not relaunch unchanged.

## Step checklist

- [ ] changelog parse at the tip
- [ ] tidy on a clean tree
- [ ] module check :solr:solrj-streaming
- [ ] receipt refreshed; job and claim DONE

- Status: CLAIMED by vm2 (gate backlog coordinator), 2026-10-13T11:17Z at 80ce9d7a3c8; claim claims/pool-vm2-gate-backlog-10322-tip-regate.md.

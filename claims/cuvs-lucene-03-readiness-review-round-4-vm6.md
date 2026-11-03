# Claim: cuvs-lucene readiness review, round 4, step 03 (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE (report delivered, disposition CHANGES NEEDED).
- Job or assignment: `assignments/cuvs-lucene-03-readiness-review-round-4.md`, whole assignment.
- Snapshot: coordination-ui `ab64a120f326`. Assignment Status line reads HANDED (waiting for steps 1 and 2); both prerequisites are evidenced by published outputs: step 01 package `material/cuvs-lucene-contribution-round-4/` (commit `6ed612c8ca`), step 02 receipt `receipts/cuvs-lucene-02-submission-package-round-4.md` and revision 1 drafts (commit `3a082c89ec`). No other step 03 claim exists at this snapshot.
- Capability tags used: review, records.
- Independence: vm6 did not prepare steps 01, 02 or the review kit (all vm2). Disclosure: vm6 wrote the earlier SOLR-18556 options report (`8c642f0fa6`), which concerns the Solr docs route (step 07), not the reviewed patch or drafts.
- Started: 2026-11-03T02:34:50Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: cuvs-lucene-03-readiness-review-round-4@ab64a120f326.
- Deliverable: `reports/cuvs-lucene-readiness-review-round-4.md` (identity table, code assessment, proof mapping, description findings, disposition READY or CHANGES NEEDED).
- Method: source, evidence and public-text review. Independent reconstruction of the four test files from the PR 2702 head in an isolated directory (not a repo). No build, no test run, no Gradle, no upstream write, no Jira write, no push to any source branch. GitHub API read-only.
- Workspace: /workspace/tmp/cuvs-03/.
- Links: Category: review. Parent: SOLR-18556, NVIDIA/cuvs PR 2702. Inputs: reports/cuvs-lucene-review-kit-round-4.md, material/cuvs-lucene-next-steps-round-4.md, reports/cuvs-lucene-round-4-decision-round-1.md. Next: assignments/cuvs-lucene-04-owner-handoff-round-4.md (owner only). Supersedes: none.

- Decision ladder: rung 1 for identities, reconstruction, upstream state and test behavior (sources and blobs cited in the report); rung 3 for the review scope (assignment step 03 Work items 1 to 6); rung 4 for the target options (recorded in the report, recommendation PR 2970). The target is bound at step 04 by the owner.

Result: 2026-11-03T02:38:54Z: DONE. `reports/cuvs-lucene-readiness-review-round-4.md`. Disposition CHANGES NEEDED: every identity matches and the tests and proof are sound at PR 2702 `23004d0f`, but upstream moved after the package was built. PR 2867 merged 2026-10-29 (`90661fd0`), and PR 2970 (Lucene 10.3, compat modules, two approvals, head `3602ef0a`) rewrote `TestCompatKnnFloatVectorQuery` and `TestBackCompatIndices`, so the combined patch no longer applies where the work is landing and the first-contact text is stale. Bounded repair: (a) port plus exact-input CPU job on the chosen base, (b) step 02 revision 2 drafts, (c) step 03 re-review. Owner decision: target (PR 2970 recommended). Nothing posted.

Heartbeat: 2026-11-03T02:38:54Z (final; claim done)

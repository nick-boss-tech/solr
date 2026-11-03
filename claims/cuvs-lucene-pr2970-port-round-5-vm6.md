# Claim: cuvs-lucene test port to NVIDIA/cuvs PR 2970, round 5 (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE (implemented; proof handed to gates/cuvs-lucene-pr2970-port-round-5.md).
- Job or assignment: owner direction, 2026-11-03 (Nick, through the vm6 session): take option 1 of `reports/cuvs-lucene-readiness-review-round-4.md` (port to PR 2970), implement, and hand the CPU proof to another host as a gate. This is repair item (a) of that report, implementation half only.
- Snapshot: coordination-ui `65e63e1bc69a`. No other port claim or package exists at this snapshot.
- Target base: NVIDIA/cuvs PR 2970 head `3602ef0a8047ef222ad994a316d69ffec26e98bd` (open, two approvals; rechecked at claim time).
- Capability tags used: implementation, records.
- Started: 2026-11-03T02:46:38Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: cuvs-lucene-pr2970-port-round-5@3602ef0a8047.
- Deliverables: `material/cuvs-lucene-pr2970-port-round-5/` (port patch, manifest, README), `gates/cuvs-lucene-pr2970-port-round-5.md` (CPU proof gate for another host), `reports/cuvs-lucene-pr2970-port-round-5.md`.
- Method: edit test files only, in an isolated directory built from the PR head through the GitHub contents API. A javac compile check against Lucene jars may run here (no Gradle, no Maven build, no GPU). The positive runs and mutation and data-fault controls are left to the gate.
- Limits: no upstream push, PR, comment, or Jira write. Drafts (step 02 revision 2) are not part of this claim.
- Links: Parent: SOLR-18556, NVIDIA/cuvs PR 2970 and PR 2702. Siblings: `assignments/cuvs-lucene-03-readiness-review-round-4.md`, `claims/cuvs-lucene-03-readiness-review-round-4-vm6.md`. Supersedes: none (round 2 and round 3 evidence stands for PR 2702 `23004d0f`).

- Decision ladder: rung 1 for the fixture data contract (GenerateReleasedIndices.writeIndex, blob 81182548) and the compat counting hook; rung 3 for the base and hand-off (owner direction above); rung 2 smoke and dry runs to check the harness, labelled as implementer evidence, not a gate.

Result: 2026-11-03T02:57:23Z: DONE. Package `material/cuvs-lucene-pr2970-port-round-5/` at `57e8a6c989ec63c306d571cd85435d38403cfdce` (port.patch SHA-256 `3185ea28...c36d`, 214 added lines in 4 test files). Implementer dry run on vm6: 18 positive query executions, 6 expected mask-drop failures, 10 archived-index method executions over 70 fixture cases, 2 expected data-fault failures, on Lucene 10.2.0 and 10.3.2. Gate `gates/cuvs-lucene-pr2970-port-round-5.md` OPEN for another host. Report `reports/cuvs-lucene-pr2970-port-round-5.md`. Nothing posted.

Heartbeat: 2026-11-03T02:57:23Z (final; claim done)

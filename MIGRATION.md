# Migration plan for review

## Goals

Make live work and its ownership easy to find, separate it from historical evidence, preserve exact-head proof and links, and give agents concise, discoverable coordination instructions. Avoid treating an index, old receipt, or branch name as authoritative without checking the underlying records.

## Proposed shape

- Keep assignments, claims, gate requests, receipts and reports as traceable records, with ownership and status derived from the relevant records. An index is a convenience, not authority.
- Keep large raw evidence and completed history at stable, pinned locations on the old branch or a separately decided archive; carry explicit links and source revisions. Do not discard, rewrite or assume availability of evidence.
- Use a short coordination skill as the entry point; follow current host, public-write and gate requirements. Integrate with the separately reviewed Solr skills rather than copying their domain workflows.
- Identify a single live board explicitly. A preview is never a second claim queue.

## Before cutover

1. At a fresh source SHA inventory assignments and gates, their claims, terminal records, host activity, receipts, reports and external branch heads. Reconcile ambiguities rather than deriving closure from `OPEN-WORK.md` alone. Preserve source paths, blob hashes and exact source commit in a machine-readable manifest. Include paused, handed, owner-decision and active work as well as open work; carry dependencies and evidence links.
2. Produce a reviewable migration set and a mapping from every carried record to its old path. Keep active claims owned by their existing holders; do not create duplicate claims or rerun gates. Explicitly identify unclassified records and missing dependencies. Verify links and blob hashes.
3. Audit all board readers, writers, host bootstrap instructions, scheduled jobs, wrappers and skills for hard-coded `coordination-ui`, including the Windows poster and Linux `pool/bin` scripts. Update and test each against an isolated test target; do not redirect existing production writers prematurely. Account for every host, including agents that are offline.
4. Reconcile source changes since the inventory snapshot. Arrange a coordinated write freeze or another verified atomic handover with all active hosts; preserve in-flight work and ensure old writers cannot create split ownership. A timed-out agent or an empty generated index is not evidence of quiescence.
5. Publish the verified migration and instructions; redirect the designated bootstraps and tools only at the agreed cutover. Verify live reads, add-only claims, compare-and-swap edits, ancestry and evidence access on the new board. Publish a clear pointer on the old board and retain its historical access. Record exact source and destination heads and the time.
6. Monitor for late writes to the old branch, reconcile them without losing work, and keep a documented rollback path. Never delete or force-push the old branch as part of this plan.

The migration is not complete until the manifest, reconciled live state, host readiness, tested tooling and cutover checks exist. This preview performs none of those actions.

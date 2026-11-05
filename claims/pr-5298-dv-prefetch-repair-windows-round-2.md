# Claim: SOLR-18608 prefetch gate repair, round 2

Status: CLAIMED (Windows implementation; Linux proof pending)
Started: 2026-11-05 22:11 UTC
Host: windows (Codex desktop)
Tags: implementation, review, records
Authority: continuing Nick's request to patch PR 5298 and hand it to the gate, under the original gate's scoped repair rule; Nick reported the resulting receipt in this chat.
Board snapshot: 36cef337d9cc2e7d352cb8c95e26cdc108541299

Input H: 4d547451e3914de441fc897ea6a8c0652a48736a, separately verified as the current fork and PR head. Failed execution receipt: receipts/PR-5298-dv-prefetch-round-1.md; report: reports/pr-5298-dv-prefetch-gate-round-1.md. The execution claim is terminal. This new claim supersedes the completed Windows implementation claim for this repair slice; no other repair claim was found.

Diagnosis and scope (decision ladder rung 1):
- The new helper omits wt=xml but uses XPath. Add the explicit response format.
- LargeFieldTest fails at line 96, the initial lazyField wrapper assertion, before its lazyField-only query at line 99. Its schema version 1.7 str field has DocValues by default. The new selector skips prefetch for its explicit str request, leaving no cached document; the test's subsequent unfiltered doc(0) call then fills the cache eagerly. Preserve the established explicit-only prefetch path and restrict the new substitution/empty-set bypass to pattern selections, which is this PR follow-up's intended scope. Leave LargeFieldTest's existing assertions and schema unchanged.

Deliverables:
- One scoped ordinary repair commit pushed to the same fork branch and separately read back.
- reports/pr-5298-dv-prefetch-repair-round-2.md
- gates/pr-5298-dv-prefetch-round-2.md, preserving the original mandatory proof and explicitly requiring LargeFieldTest at the repaired head.

No Windows Gradle/tests. Original failed receipt/job remain immutable. Status remains Gate FAILED at H until the repair is pushed, then Implemented, Proof Pending at the new head; no green claim until Linux proof completes.

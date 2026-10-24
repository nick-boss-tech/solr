# Claim: cuvs-lucene PR 2702 CPU review and implementation, round 2

- Host: windows, NCBIPC0140, Codex desktop.
- Started: 2026-10-24T20:34:19.791Z.
- Authority: Nick's current chat instruction, 2026-10-24: review the existing upstream compatibility work, implement substantiated gaps, and hand off a CPU-only gate if needed.
- Prior assignment: `assignments/cuvs-lucene-lucene104-upstream-fix-round-1.md`; prior package delivered at `1ea03b601b9a5560086d487a2e622b4a137ed08d`. This claim is new work, not a takeover of the completed round.
- Board snapshot: `a2ac42d3ae16036cc2033ef3b03a55af34771724`; no overlapping cuvs-lucene follow-up claim found.
- Upstream review base: NVIDIA/cuvs PR 2702 head `23004d0f0de2014f11b09313f04fcf0de7643c3b`, pinned before source work.
- Slice: verify the three remaining Lucene linkage failures against the PR's versioned implementation, implement a focused correctness or regression-coverage gap supported by source/probes, and preserve CPU-only evidence. Do not invent a defect if the three failures are already fixed.
- Capabilities: implementation, review, records.
- Deliverables: `reports/cuvs-lucene-pr2702-cpu-fix-round-2.md`, `material/cuvs-lucene-pr2702-round-2/` patch and reproducible proof inputs/results. A gate assignment will be added if broader CPU proof is necessary.
- Decision ladder: rung 1 for upstream code/contracts; rung 2 for CPU probes and fail-before checks; rung 3 for the owner's approved scope. Record later decisions in the report because this claim is immutable.
- Limits: no GPU validation, no Gradle or local Solr queue, no Solr code changes, no public PR/comment or upstream/fork code push. Deliver source changes durably as a board package. Keep Nick Shanin identity and no commit trailers.

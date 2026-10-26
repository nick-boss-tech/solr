# Claim: PR 5188 glob field-loading follow-up, Windows round 1

Claimant: windows (Codex desktop)
Started: 2026-10-26T14:39:56Z
Tags: implementation, review, records
Source: Nick's direct request on 2026-10-26 and research/glob-field-loading-followup-research-note.md.
Board snapshot reviewed: 1053370a993cea8c6779f0e4bbf79276453daecd.
No existing assignment or claim for this slice was found in assignments/, claims/, gates/, receipts/ or reports/ at that snapshot. Direct owner instruction authorizes this implementation slice.

Scope: trace RTG stored-field selection and lazy prefetch/highlighting; implement the smallest supported follow-up and focused loading assertions. Preserve transformer source fields, aliases, pseudo-fields, explicit docValues and fl=*. Exclude term-vector selection. No Gradle on Windows; Linux matched pre-fix/post-fix proof remains pending.

Code base: Apache PR 5188 e07455a0f953e702da27f677c51ea894b7bafc3a.
Separate fork branch: pr-5188-glob-field-loading-followup. No change to the public PR or its author's branch.
Decision ladder: rung 1 source/history trace first; rung 2 executable discrimination delegated to the Linux gate. Unrun hypotheses remain labeled unproven.

Deliverables:
- Pushed implementation and regression tests on pr-5188-glob-field-loading-followup.
- reports/pr-5188-glob-field-loading-implementation-round-1.md
- gates/pr-5188-glob-field-loading-round-1.md

Completion is derived from those deliverables. Implemented, Proof Pending until the exact-head Linux gate receipt is green.

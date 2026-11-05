# Claim: SOLR-18613 standalone selection follow-up, Windows round 1

- Status: CLAIMED for implementation and gate handoff; no Linux execution claim.
- Host: windows, Codex desktop; tags: implementation, review, records.
- Started: 2026-11-05T18:44:04Z.
- Authority: Nick's direct request in the review chat: "Try to patch this branch and hand off the gate task".
- Board snapshot inspected: ad0b35d8adb77ad08143de9b32847791f9f599f9. No existing claim covers this follow-up.
- Target: apache/solr PR 5297, SOLR-18613; fork branch solr-lb-attempt-limits-pr1-submit at 2399d0312fa320563773ea87884ee4f534594974.
- Scope: repair standalone live-candidate exhaustion under shared round-robin interleaving; add deterministic regression coverage, ordinary commit and plain fork push, then an exact-head Linux gate.
- Decision ladder: rung 1, confirmed source regression. Capping picks by snapshot size does not exhaust distinct candidates when other requests advance the shared selector. Preserve the existing pickServer hook and request attempt allowance.
- Isolation: new task-specific worktree and local branch at the verified live head; the older checkout of the rebased submit branch is preserved.
- Deliverables: reports/SOLR-18613-selection-windows-round-1.md and gates/SOLR-18613-selection-round-1.md, with separately verified pushed SHA, expected proof failures, checks, logs/XML and receipt path.
- Windows runs no Gradle, compilation, formatting tasks or tests. State remains Implemented, Proof Pending after handoff.

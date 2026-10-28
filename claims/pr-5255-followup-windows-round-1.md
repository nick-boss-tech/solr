# Claim: SOLR-18589 alias lock follow-up implementation

Status: CLAIMED

- Host: windows (implementation, review, records).
- Started: 2026-10-28 05:20 UTC.
- Source: Nick's explicit request in chat to produce patches and tests for both PR 5255 review findings and hand them to the Linux gate.
- Reviewed board: `9ee9785f3c354e5f6a5075b6f88f1d9e6625ba6b`; no competing claim or gate for this slice found.
- Parent: PR 5255 at `1edd6a113b2a9e1b029f509c9279f4fb58683bba`, confirmed live and without discussion changes.
- Fork branch: `nick-boss-tech/solr:solr-18589-submit` (new, derived from the contributor's head; never write their branch).
- Scope: prevent a command from acting on a newly resolved collection under a different collection's lock; preserve structured BAD_REQUEST errors from Overseer lock acquisition; add behavioral regressions.
- Decision ladder: rung 1 establishes the locking invariant and structured error contract. A changed alias will cause a retryable conflict before execution rather than operate without the matching lock. Keep the original alias message for existing DELETE/RENAME bookkeeping. The Linux gate must prove the behavior and fail-before regressions; no build or test runs on Windows.
- Deliverables: fork implementation commit; `reports/pr-5255-alias-lock-followup-windows-round-1.md`; `gates/pr-5255-alias-lock-followup-round-1.md`, with exact head, positive/negative legs, neighboring suites, tidy and root check; receipt target `receipts/PR-5255-alias-lock-followup.md` owned by the gate host.
- No public PR/JIRA action is part of this claim.

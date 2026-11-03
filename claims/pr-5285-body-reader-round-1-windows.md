# Claim: SOLR-16398 request-body error scope, Windows round 1

Status: CLAIMED

- Host: windows; tags: implementation, review, records
- Started: 2026-11-03T18:58:22Z
- Authority: Nick's explicit request to patch PR 5285 and hand it off for gating.
- Board reviewed: 2d798474b3d944973d8677625f2d49e642b3b1ec; no matching live claim or gate found.
- Parent: e6fbade6773b3bf78e95f50ecc2335276daca651
- Fork target: nick-boss-tech/solr, solr-16398-malformed-body-400; ordinary commit/push, preserving PR ancestry.
- Worktree: wt/PR-5285-body-reader; local branch solr-16398-body-reader-windows.
- Slice: replace global Jackson exception mapping with conversion of client syntax/input-mismatch failures at the JSON reader boundary; preserve server exceptions; add focused coverage.
- Deliverables: pushed branch, reports/pr-5285-body-reader-round-1-windows.md, gates/pr-5285-body-reader-round-1.md. Linux execution receipt reserved as receipts/pr-5285-body-reader-round-1.md.
- No Windows Gradle. Linux host must claim and execute the gate. No public comments or body edits are authorized by this claim.
- Decision ladder: source scope and test fixture choices resolved by inspecting the existing reader and error-response path; no owner decision required.

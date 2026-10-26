# Claim: BATS assertion hardening implementation and Linux handoff, round 1

Status: ACTIVE; implementation remains proof pending until the exact-head Linux gate succeeds.
Host: windows, Codex desktop; tags: implementation, review, records.
Started: 2026-10-26T18:10:47Z.
Board snapshot: 7e80ec74d4ac1fb0ed7bab008f80de085cc54c59.
Source base: apache main 0d6b2e32f598d772aed094038022a6a588fdfc5a.
Evidence: reports/bats-scripts-assertion-review-round-1.md and its three unit reports.

Owner direction in this chat: "See if you can patch these in a branch and then hand it off for gate". This authorizes the implementation follow-up to the review-only assignment, a normal fork push, and a new Linux gate job. No public PR/Jira action is authorized. No overlapping assertion-hardening implementation or gate claim was found at the board snapshot.

Scope: harden the nine new confirmed assertion patterns and the four straightforward suspected checks with positive state, specific failure causes, fresh output, and checked helper failures. Keep production behavior unchanged. Do not touch test_security_manager.bats or the separate PR 5225 gate/candidate. Where TLS diagnostics need Linux evidence, require that proof in the new gate rather than claiming an observed result on Windows.

Branch: solr-bats-assertion-hardening-submit (descriptive branch; no Jira ticket has been invented or filed).
Worktree: wt/BATS-assertion-hardening.
Decision ladder: rung 1 source contracts; rung 3 explicit owner follow-up direction supersedes the review-only implementation limit.
Deliverables: pushed fork commit, reports/bats-assertion-hardening-implementation-round-1.md, and gates/bats-assertion-hardening-round-1.md with exact head, focused BATS matrix, formatting/check requirements, and discriminating controls. No Gradle or BATS on Windows.

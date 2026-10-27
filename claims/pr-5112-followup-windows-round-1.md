# Claim: PR 5112 routing review follow-up, Windows, round 1

- Host: windows, Codex desktop. Tags: implementation, review, records.
- Started: 2026-10-27T16:23:39Z.
- Source: Nick's direct October 27 request to implement the review, push an adoptable branch, and hand it to Linux proof. Hold public comment drafting/posting until Nick brings back results.
- Board snapshot: `f1ee7180bcd24d0dfd0054f7f7201d7627c3ad0e`. No matching implementation assignment, claim, gate, or receipt; the sole ticket match is a daily scan entry saying no candidate.
- Original PR head: `b4fa46f1ecb11a60cc2cfd4783492639bb4e3a85`, apache/solr PR 5112, dsmiley's `SOLR-18537-proxyRequestsByHeader`.
- Slice: fix F1 header loss on unrelated query text, add discriminating regressions and header/query conflict coverage, correct F2 guide/Javadoc wording. Existing certificate-identity discussion is a separate unresolved concern, outside this bounded routing fix.
- Fork target: `solr-18537-submit`, starting exactly at the PR head; absent at claim time. Local worktree: `wt/SOLR-18537`.
- Deliverables: pushed implementation commit; `reports/pr-5112-target-header-followup-windows-round-1.md`; `gates/pr-5112-target-header-followup-round-1.md`.
- Proof: no Gradle/tests on Windows. Linux must run exact-head focused suites, tidy, required checks, and original-production/new-test fail-before proof.
- Decision: bounded implementation supported by source and existing public review. No public write or contributor-branch change needed.

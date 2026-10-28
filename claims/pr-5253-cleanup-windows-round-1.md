# Claim: SOLR-18601 migration failure cleanup follow-up

Status: CLAIMED (implementation only; Linux gate pending).

- Owner direction: Nick requested building the reviewed fixes and handing them to the gate in this chat on October 27, 2026 Eastern.
- Host: windows; tags implementation, review, records.
- Started: 2026-10-28 01:15 UTC.
- Board snapshot reviewed: b9989c6a17cd7d5baa0554b06847fa305a6c7f97. No SOLR-18601 assignment, claim, gate, or receipt exists in that snapshot.
- Original PR 5253 head: 32b91612edde057c19bf1a7963e8276b816551df, unchanged on live refresh.
- Worktree: C:/Users/shaninna/dev/Solr-issues/wt/SOLR-18601; fork destination solr-18601-submit. Original contributor branch remains unchanged.
- Scope: preserve buffered acknowledged writes by replaying after routing restoration; preserve any routing rule that existed before the failed range transfer; do not resume target processing if routing restoration fails. Remove the now-unneeded new buffer-drop action. Add focused regressions and a failure contract note.
- Decision ladder: rungs 1 and 3. UpdateLog has one core-wide buffer and cannot identify migration ownership. Replaying preserves existing forwarding streams. MIGRATE is not transactional; failed operations can already leave a merged target index. The patch will document possible partial target data rather than claim a full rollback.
- Deliverables: pushed fork fix head; reports/pr-5253-cleanup-implementation-round-1.md; gates/pr-5253-cleanup-round-1.md, with exact-head positive, original-production negative, tidy, and root-check requirements. Implemented, Proof Pending until Linux proof succeeds.
- No Gradle or local test queue on Windows. No upstream public write is authorized by this claim.

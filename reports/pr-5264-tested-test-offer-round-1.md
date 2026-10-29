# SOLR-18610: verified regression test offered to PR 5264, round 1

Status: VERIFIED TEST OFFER POSTED; AWAITING CONTRIBUTOR RESPONSE

- Host: windows (Codex desktop), 2026-10-29T17:10:10Z
- Board snapshot reviewed: 07b48fb5fc4dac389a1be25a895690d95a829ff8
- Gate: gates/pr-5264-web-identity-test-round-1.md
- Receipt reviewed: receipts/SOLR-18610.md, authored by vm5
- Tested and live fork head: f93d00367d8a9861fd8edbfb51b6ab83ae59c156, nick-boss-tech/solr:solr-18610-submit
- Live contributor head: dbb3d85d72dd27889d8b93f10d1f4c2f54aa5e0e, unchanged through post/read-back
- Public comment: https://github.com/apache/solr/pull/5264#issuecomment-6503323789
- Posted: 2026-10-29T17:09:29Z; separately read back and compared with the local draft, exact match after line-ending/trailing-newline normalization
- Authorization: Nick's standing PR review follow-up authorization after a clean Linux gate at the exact offered head, root AGENTS.md, 2026-10-26

## Correction to gate-agent framing

The contributor's head ALREADY contains all three production NetworkPermission grants at solr/server/etc/security.policy:110-112. The diff of that policy between dbb3d85d72 and f93d00367d is empty. The merge base of those heads is dbb3d85d72. The complete offered delta is one new test file, 181 lines. No production grant or changelog change was introduced by Windows or the gate repairs.

The receipt's head-chain row describing dbacd6951e as introducing the grants, and its sentence attributing the changelog/grants to the Windows commit, are inaccurate as provenance. dbacd6951e added only the test. The unpatched policy used for negative proof is from pre-PR main, 3db71000514e1edaa8e6ae0426fce92ca97990e3, NOT from the contributor's PR head. This is an add-only correction; the vm5-owned receipt was not edited. The factual correction does not invalidate the proof comparison recorded in that receipt.

## Assessment of repairs and proof

The final source was reviewed from the fetched fork ref, including c47026394b, 41400946a3 and f93d00367d. The classpath policy extension adds only java.io.FilePermission read grants for the child JVM's own classpath entries. No network grant, alternate HTTP transport, disabled Security Manager, or weaker credential assertion was introduced. The JDK HTTP server suppression is local to the fixture method; the deprecated Security Manager check retains its own targeted suppression. No new source defect was found in these repairs. git diff --check is clean.

The actual receipt records Java 21.0.12.1, seed 18610C0DE, final-head execution: one passing test with zero failures/errors/skips. The same test with only the policy reverted to pre-PR main records one failure, caused by Apache5HttpClient's permission check and an AccessControlException for setOption.TCP_KEEPIDLE. The exception names all three required TCP options; this is not evidence of three independently executed denial tests.

Tidy passed and left the tree clean. Root check -x test passed in a plain clone at the final head after NODE_ENV was unset so npm could install Antora's tabs devDependency. The initial production-mode Antora failure is documented, not counted as a pass. The receipt explains that forbiddenApisTest was up-to-date in the plain clone and had already passed with matching inputs in the runner worktree at the same head.

This Windows follow-up inspected the receipt, gate requirements, source, history, live heads and discussion. It did not rerun Gradle or directly inspect vm5's raw log/XML files. The proof covers the direct web identity provider against a local STS stub under the shipped policy plus test classpath read grants; it does not claim an EKS deployment or full backup/restore run.

All fork-only follow-up commits are authored and committed as Nick Shanin, without attribution trailers, consistent with Nick's explicit workspace instructions. The public comment carries the required AI disclosure. No history rewrite was made or needed, and the original contributor commits were preserved.

## Public action and next state

Before posting, the live PR still had no conversation comments, inline comments or reviews, and its head was unchanged. The remote fork head matched the receipt. The public comment offers only the tested test file, links the pinned original-head-to-tested-head diff, reports the exact proof and NODE_ENV condition, and gives git switch/fetch plus a pinned merge --ff-only command. No approval review, PR-body edit, JIRA action, PR creation or contributor-branch update was made.

The contribution is ready for the contributor's consideration. Wait for their response; do not characterize the public PR as merged, accepted or updated. The local SOLR-18610 worktree was advanced by a clean fast-forward to the tested fork head.

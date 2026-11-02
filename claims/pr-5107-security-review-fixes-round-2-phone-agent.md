# Claim: PR 5107 security-review fixes round 2 Linux gate (phone agent)

- Host: phone agent (Linux gate host, JDK 21.0.12.1+1, Gradle 9.7.0 via
  ~/workspace/tools/solr-gradle.sh, serialized through the test-queue flock).
- Status: CLAIMED.
- Unit: Linux gate for `gates/pr-5107-security-review-fixes-round-2.md`.
  Fork branch `nick-boss-tech/solr:solr-8442-security-review-r2-submit` at
  pinned head H `795b821a36e7d395b80ee3473bea4de82875e397`, one commit above
  contributor C `25c88fb1c432120b98f7af668cf58be0052d13d0` (verified by
  ls-remote, ancestry, and live PR head = C, no movement, at claim time).
  Implementation report `reports/pr-5107-security-review-fixes-round-2.md`;
  Windows implementation claim only; no Linux executor claim or receipt
  existed at claim time.
- Preflight (before this claim, per the gate): JDK 21.0.12.1+1, OpenSSL
  3.0.13, keytool in the vendored JDK, curl present; no `bats` binary on
  PATH, so the runner seeds the four bats tarballs before packaging legs
  (the established local fix for the Gradle bats-download PKIX failure).
- Gate runner: ~/workspace/tools/5107-gate-runner.sh, worktree
  ~/workspace/solr-worktrees/5107-gate, seed 8442C0DE.
- No PR, no Jira/GitHub post. No production fix inside the gate; a
  formatting-only tidy diff stops the gate as GATE INVALID for the
  standing repair rule, it is not folded in silently.
- Tidy repair 2026-11-01 ~21:25 MST: the first runner run stopped GATE
  INVALID at the tidy guard (tidy reflowed SSLTool.java and SSLToolTest.java,
  proven formatting-only by identical whitespace-stripped token streams).
  Repair committed as Nick Shanin, no trailers, as d264f75664 and
  plain-pushed fast-forward to the fork branch (verified by ls-remote).
  Superseding gate gates/pr-5107-security-review-fixes-round-2-
  superseding-d264f75664.md pins H2; the original gate file is untouched.
  Runner retargeted to H2 and relaunched; no test at H counts as proof.

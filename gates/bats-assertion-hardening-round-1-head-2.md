# Superseding head record — BATS assertion hardening round 1

- Host: `phone-agent-2`.
- Supersedes the original candidate head record at `0d7c6f4e6cb6d2211b59ed9eed36db90182b122e`.
- Candidate: `nick-boss-tech/solr:solr-bats-assertion-hardening-submit` at exact head `de4cf59ec35c66b8fec1bd16da0ce94eef0eab0b`; parent `0d7c6f4e6cb6d2211b59ed9eed36db90182b122e`; pinned Apache base `0d6b2e32f598d772aed094038022a6a588fdfc5a`.
- The preserved gzip patch artifact was verified (`gzip -t`; SHA-256 `2d9c2b9c3c2a1ec143b3e6246059c992a951b4dd2b1700f946f119e1d7354f11`) and its three-file contents matched the 2026-10-26 follow-up report. It was applied on a separate worktree, leaving `/dev/shm/bats-assertion-hardening-0d7c6f4` and prior logs untouched.
- The new commit changes only `solr/packaging/test/bats_helper.bash`, `solr/packaging/test/test_ssl.bats`, and `solr/packaging/test/test_stream.bats`. `git diff --check` passed before commit. Commit author and committer are both exactly `Nick Shanin <nick.boss.us@gmail.com>`; the message has no trailers. The fork branch was pushed fast-forward and the remote head verified at `de4cf59ec35c66b8fec1bd16da0ce94eef0eab0b`.
- No BATS or Gradle test has run on this new head. Root inode headroom is 135,963 of 837,408 free (16.2%), below Nick's requested ~20% launch threshold; all test/build launches remain paused pending an exact cleanup target approval. No additional cleanup was performed.
- All earlier-head runs remain non-final proof. The pinned-base mTLS hostname discrimination, eight control groups, final four-leg matrix, and final checks are pending.
- Per the Docker amendment, `test_rolling_upgrade.bats` is unrunnable by any current pool host. No subleg handoff will be created; no GREEN receipt is authorized on this record.

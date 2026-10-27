# Superseding candidate head record — BATS assertion hardening round 1

Status: final-head proof pending. This record supersedes the earlier head records; it does not claim gate completion.

## Candidate

- Fork branch: `nick-boss-tech/solr:solr-bats-assertion-hardening-submit`
- Exact current head: `4d5d876d946a4f5ac174a7f68e16f4dd9b5f9f68`
- Parent: `db22aabe997c9af6d6a8f1ef0184c9778d5705b5`
- Commit subject: `Require successful no-compression BATS response`
- Author and committer: `Nick Shanin <nick.boss.us@gmail.com>`
- Changed path: `solr/packaging/test/test_compression.bats` only; test-only change, no production code.
- The no-`Accept-Encoding` case now positively requires an HTTP 200 status line before checking that no `Content-Encoding` header is present. This prevents a successful-but-empty curl response from satisfying only the negative header assertion.

The commit was created from the verified parent, pushed with a normal fast-forward, and the remote branch was verified at the exact head above. The candidate worktree is clean. BATS parser count for `test_compression.bats` is 3 and `git diff --check` passed. No BATS or Gradle tests have run on this new head yet; the parser count is not test execution.

## Preserved evidence and next proof

Earlier evidence remains preserved under `/workspace/gates/logs/phone-agent-2/bats-assertion-hardening-round-1/finish-2026-10-27/`. Evidence produced on prior head `db22aabe997c9af6d6a8f1ef0184c9778d5705b5` is not final-head proof. All remaining controls and matrix legs must identify and run against this exact new head.

At record time, root had 141,766 free of 837,408 inodes, `/dev/shm` had 2.2 GiB available, no active Gradle/BATS build process was found, and the documented phone-agent-2 Gradle lock was available. Recheck resources, processes, and lock immediately before every Gradle launch; do not launch at or below the user-approved 15% root-inode floor.

The preserved one-time host check at `docker-and-jdk25-host-check.txt` reports JDK 25.0.4.1 installed, Docker CLI unavailable, and Docker daemon unavailable. Per the owner's Docker amendment, do not post a subleg handoff for `test_rolling_upgrade.bats`; report it as unrunnable by any current host. Do not write a GREEN receipt if Docker is the only unmet criterion; stop incomplete and ask Nick to decide.

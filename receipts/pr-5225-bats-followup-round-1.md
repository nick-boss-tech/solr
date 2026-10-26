# Receipt: PR 5225 BATS follow-up gate, round 1

**Verdict: GREEN.** All gate requirements are satisfied at the exact candidate head `35378004f1797c5b9ae3ba68d2503e4edb3840e8` on `nick-boss-tech/solr:solr-5225-bats-followup` (parent `096da6567e3db6d2bd141e3d2bf556ad1b3d2509`). This receipt is the add-only close-out; the gate definition, prior claims, and candidate source remain unchanged.

## Final-head evidence

All four exact-head runtime legs passed one test each, with zero failures, errors, or skips: JDK 21 Commons CLI; JDK 21 Picocli; JDK 25 Commons CLI; and JDK 25 Picocli. The completed JDK 21 Picocli continuation report is `reports/pr-5225-bats-followup-round-1-jdk21-picocli.md`; it names SHA `35378004f1797c5b9ae3ba68d2503e4edb3840e8` and was not rerun. The other exact-head results are recorded in `reports/pr-5225-bats-followup-round-1-evidence.md` and `reports/pr-5225-jdk25-regex-fix-final-head-follow-up.md`.

At the pinned head in a clean detached worktree, `tidy`, `git diff --check`, and `:solr:packaging:check -x test` all passed. The packaging log records `errorProneSkipped`; this receipt does not claim Error Prone validation.

## Mutation-control evidence

- **Unrelated-error mutation:** the JDK 25 Commons CLI run executed one test and failed at the path-specific `assert_file_contains` assertion, as required. The log contained the unrelated synthetic backup error and generic wrapper but not the path-specific denial phrase.
- **Pre-test stale-log seed:** a matching line was verified in `solr.log` during setup before Solr start. Startup rotated it into `solr.log.1`; the current `solr.log` held the unrelated synthetic failure. The one-test BATS run still failed at the path-specific assertion, as required. The ineffective post-start injection attempt is not counted.
- **Original PR #5225 comparison:** the original test, with Security Manager explicitly disabled and its generic alternative unchanged, passed against the unrelated synthetic failure. This records that the original generic alternative accepts that failure; it is the comparison specified by the gate, not a production-risk or failure-rate estimate.

The detailed report is `reports/pr-5225-bats-followup-round-1-phone-agent-2-takeover.md`. Full wrapper logs, fresh XML/transcripts, complete Solr logs, scratch diffs, and headroom records are preserved at `/workspace/gates/logs/phone-agent-2/pr5225-followup-takeover/`.

## Scope and resource safety

The live candidate ref remains at the pinned SHA; this gate run changed no candidate-source file and made no PR, Jira, or upstream write. Every build used the prescribed host lock and inode guard. After the final run, 191,538 of 837,408 root inodes were free (22.9%, above the 15% floor); `/dev/shm` had 4.6 GiB free. No existing path or cache was deleted. The stale prior full claim and its incomplete verdict were left intact; the takeover claim identifies the handoff.

## PR description draft (not posted)

This follow-up tightens the PR #5225 BATS assertion so the backup test requires the path-specific `must be relative to SOLR_HOME` diagnostic, not merely a generic backup failure. At candidate head `35378004f1797c5b9ae3ba68d2503e4edb3840e8`, the focused BATS case passed under JDK 21 and JDK 25 with both Commons CLI and Picocli. Final `tidy`, `git diff --check`, and `:solr:packaging:check -x test` checks passed. Isolated controls confirmed the updated assertion rejects an unrelated backup error and remains failing with the prescribed pre-test log seed; the original PR test's generic alternative accepted the unrelated error. The local packaging check skipped Error Prone, and no upstream CI result is claimed.

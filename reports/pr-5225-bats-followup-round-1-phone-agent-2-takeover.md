# PR 5225 BATS follow-up gate, round 1 — phone-agent-2 takeover completion

**Verdict: GREEN at candidate head `35378004f1797c5b9ae3ba68d2503e4edb3840e8`.** The authoritative gate definition and candidate source were not edited. This add-only report supersedes the earlier incomplete evidence report for the resumed gate slice; completion is recorded by the receipt.

## Candidate and takeover

The candidate is `nick-boss-tech/solr:solr-5225-bats-followup`, exact implementation head `35378004f1797c5b9ae3ba68d2503e4edb3840e8`, parent `096da6567e3db6d2bd141e3d2bf556ad1b3d2509`, based on `07fdebdda54a24085086281da1cde701e122564d`. The live candidate ref was rechecked at that SHA after the gate runs. Its outbound change remains the single BATS assertion-line change; no candidate-source file was changed during this takeover.

The takeover claim is `claims/pr-5225-bats-followup-round-1-phone-agent-2-takeover-20261026.md`. It names the stale prior full claim and holder, the stale interval, and the reason for resumption. The JDK 21 Picocli continuation was already complete at the pinned SHA and was not rerun.

## Exact-head runtime matrix

All four final-head legs have fresh one-test XML/log evidence and the expected assertions:

| Runtime / parser | Result | Existing exact-head report |
|---|---|---|
| JDK 21 / Commons CLI (`SOLR_PICOCLI=false`) | 1 passed; 0 failures, errors, or skips; expected access-denied evidence | `reports/pr-5225-bats-followup-round-1-evidence.md` |
| JDK 21 / Picocli (`SOLR_PICOCLI=true`) | 1 passed; 0 failures, errors, or skips | `reports/pr-5225-bats-followup-round-1-jdk21-picocli.md` |
| JDK 25 / Commons CLI (`SOLR_PICOCLI=false`) | 1 passed; 0 failures, errors, or skips; generic wrapper plus path-specific log evidence | `reports/pr-5225-jdk25-regex-fix-final-head-follow-up.md` |
| JDK 25 / Picocli (`SOLR_PICOCLI=true`) | 1 passed; 0 failures, errors, or skips; generic wrapper plus path-specific log evidence | `reports/pr-5225-jdk25-regex-fix-final-head-follow-up.md` |

The JDK 21 Picocli report is on the live coordination branch and records the pinned SHA `35378004f1797c5b9ae3ba68d2503e4edb3840e8`; the older cross-host Picocli report is for `096da656...` and was not used for this final-head leg.

## Final checks at the pinned head

On a clean detached scratch worktree at `35378004f1797c5b9ae3ba68d2503e4edb3840e8`, `tidy`, `git diff --check`, and `:solr:packaging:check -x test` passed. The packaging log records `errorProneSkipped`; no Error Prone validation is claimed. The final-check worktree remained clean.

Logs and headroom records:

- `/workspace/gates/logs/phone-agent-2/pr5225-followup-takeover/tidy-final.log`
- `/workspace/gates/logs/phone-agent-2/pr5225-followup-takeover/packaging-check-final.log`
- `/workspace/gates/logs/phone-agent-2/pr5225-followup-takeover/tidy-final.headroom.log`
- `/workspace/gates/logs/phone-agent-2/pr5225-followup-takeover/packaging-check-final.headroom.log`

## Discriminating controls

Every mutation was confined to a detached scratch tree and was never pushed.

1. **Unrelated-error mutation:** `SolrPaths` returned the generic backup failure while its path-specific denial text was replaced with `Synthetic unrelated backup failure: cannot complete this backup.` The allowed backup still succeeded. The valid JDK 25 Commons CLI run executed exactly one test and failed at the candidate's `assert_file_contains` path-specific diagnostic assertion; the Solr log shows the unrelated message and generic `Could not backup all shards` wrapper, not the required denial phrase. This is the expected rejection. The counted run is `control-1-mutated-commons-cli-jdk25`; an earlier wrong-child-JDK attempt is preserved but excluded.

2. **Pre-seeded stale-log control:** the same mutation was combined with a matching path-denial line written and verified in `solr.log` during BATS setup, before Solr started. The test executed exactly one case and still failed at the path-specific assertion. Solr's `OnStartupTriggeringPolicy` rotated the planted line to `solr.log.1`; the active `solr.log` contains the unrelated synthetic failure. This is the prescribed pre-test seed; it did not make the current-run assertion pass. The counted run is `control-2-preseed-before-start-jdk25`. The ineffective post-start seed attempt is preserved separately and is not counted.

3. **Original PR #5225 comparison:** the original test file from Apache PR #5225 head `53ddbf0e857a74b8475b299b0907ac1582be8248` was used in a separate scratch copy of the pinned candidate, with `SOLR_SECURITY_MANAGER_ENABLED=false` and the generic assertion `access denied|Could not backup all shards` left unchanged. With the same unrelated-error mutation, the one test passed even though the log records the synthetic unrelated failure and generic wrapper, with no path-specific denial phrase. This records that the original generic alternative accepts the unrelated failure; it is a synthetic comparison, not a production-risk or failure-rate claim.

Control evidence is preserved under `/workspace/gates/logs/phone-agent-2/pr5225-followup-takeover/`, including the exact scratch diffs, BATS XML/transcripts, full Solr logs, wrapper logs, and headroom records.

## Resource and scope close-out

All builds ran under the prescribed host lock and inode guard. Final root free inodes were 191,538 of 837,408 (22.9%), above the 15% stop floor of 125,611; `/dev/shm` had 4.6 GiB free. No existing path or cache was deleted. The candidate ref remains at the pinned SHA. No PR, Jira, or upstream action was taken, and the original gate file was left untouched.

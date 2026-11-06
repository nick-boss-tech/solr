# PR #5098 direct leader-only gate — evidence index

**Verdict: GATE GREEN at F `d822b88b1bd58c197e4ad09c53ffff0920b76190`.**

This evidence is distinct from the RC3 matched-pair assignment. The archive contains the raw logs, fresh JUnit XML, statuses, guard/driver/headroom records, mutation diff and hashes, and supporting metadata. `SHA256SUMS.txt` covers every file under `raw/`.

## Execution summary

- Host: `phone-agent-2`; JDK 21.0.12.1; Gradle wrapper 9.7.0; runner `/workspace/gates/scripts/solr-gate-step.sh` plus `/workspace/solr/pool/bin/sweep-run.sh`.
- Exact commit: F `d822b88b1bd58c197e4ad09c53ffff0920b76190`, direct child of O `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`; the source diff is two Java files, 27 insertions and 6 deletions; `git diff --check O F` passes.
- Tidy at F: rc=0; tracked worktree clean.
- Root `./gradlew check -x test`: rc=0, `BUILD SUCCESSFUL`, 558 actionable tasks; run in standalone F clone `/workspace/tmp/pr-5098-root-check` because the live runner refuses repository check from a Git worktree. No additional test exclusion.
- Final pin: fork branch and public PR head both F; PR #5098 open and non-draft, target `main` at `3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c`.
- Final resource check: 39 GiB free on `/`, 528,474 free inodes (28.29%); stop thresholds were not reached. Serialized host lock was free. Three Java Gradle daemon processes remained; no gate/build held the lock.
- Nonfatal locale-provider/security-policy warnings appear in test logs; these did not prevent compilation or the passing outcomes.
- Board note: the live gate file was explicit, open, and unclaimed, although the generated `OPEN-WORK.md` snapshot omitted it. Category is `gate`, consistent with existing gate receipts; the current branch has no `pool/work-map.md`.

## Focused test runs

### O baseline — head `O`, seed `A57A22E346C237C6`

- Result: rc=0; 1 tests, 0 failures, 0 errors, 0 skipped.
- Log: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/baseline/o-A57A22E346C237C6.log` (SHA-256 `e19fe9adaf92da684844eb3f88c393dc86beb7da32cf8c1d87f28173fe48109c`); archived as `raw/baseline/o-A57A22E346C237C6.log`.
- Fresh JUnit XML: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/baseline/o-A57A22E346C237C6.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml` (SHA-256 `edc8756cad3e71a838c71243648a6603074a583b2afc4c3aff7e587c0b7da7a7`); archived as `raw/baseline/o-A57A22E346C237C6.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml`.

### F positive — head `F`, seed `A57A22E346C237C6`

- Result: rc=0; 1 tests, 0 failures, 0 errors, 0 skipped.
- Log: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/positive/f-A57A22E346C237C6.log` (SHA-256 `c0841de6347a523642bc258aa8a4852eaec04730f46039418afb549aff75c33b`); archived as `raw/positive/f-A57A22E346C237C6.log`.
- Fresh JUnit XML: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/positive/f-A57A22E346C237C6.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml` (SHA-256 `40e37d1b9b3e2116ec3788b2f740d0b5665a16b46d1e19ed8c00df189c1a37a0`); archived as `raw/positive/f-A57A22E346C237C6.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml`.

### F positive — head `F`, seed `18530C0DE0000001`

- Result: rc=0; 1 tests, 0 failures, 0 errors, 0 skipped.
- Log: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/positive/f-18530C0DE0000001.log` (SHA-256 `eae4697182057c59e3540232ec2b1bd02f57af4c4e3d8c8d7355d8f911916dfc`); archived as `raw/positive/f-18530C0DE0000001.log`.
- Fresh JUnit XML: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/positive/f-18530C0DE0000001.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml` (SHA-256 `b260f603dffbc5a89c97cb319d84f4659130362b5faca51d7469bdcfc315c328`); archived as `raw/positive/f-18530C0DE0000001.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml`.

### F positive — head `F`, seed `18530C0DE0000002`

- Result: rc=0; 1 tests, 0 failures, 0 errors, 0 skipped.
- Log: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/positive/f-18530C0DE0000002.log` (SHA-256 `7fd959bca5b39923be8d2d832e9c1b6dba7837ea304ab279230b96a9cd0ce868`); archived as `raw/positive/f-18530C0DE0000002.log`.
- Fresh JUnit XML: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/positive/f-18530C0DE0000002.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml` (SHA-256 `d66dcaaa66dce30a376b93506b9e74ecbccd4df05189a1adc3dddd8c0ae7b744`); archived as `raw/positive/f-18530C0DE0000002.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml`.

### F positive — head `F`, seed `18530C0DE0000003`

- Result: rc=0; 1 tests, 0 failures, 0 errors, 0 skipped.
- Log: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/positive/f-18530C0DE0000003.log` (SHA-256 `0894103c930df49a59dfb7c7a1001c2e245f2af1adc6d3d08c43548bbc2b8d2e`); archived as `raw/positive/f-18530C0DE0000003.log`.
- Fresh JUnit XML: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/positive/f-18530C0DE0000003.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml` (SHA-256 `cf5af70ec47aed74bbaea1c38b5c1b263ce65d6427bddfa76f902684419f65c9`); archived as `raw/positive/f-18530C0DE0000003.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml`.

### F positive — head `F`, seed `18530C0DE0000004`

- Result: rc=0; 1 tests, 0 failures, 0 errors, 0 skipped.
- Log: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/positive/f-18530C0DE0000004.log` (SHA-256 `7a29e94f7945eb136cceb55e6beb9f092d8643d18b2a925909a160c1fd0c76dd`); archived as `raw/positive/f-18530C0DE0000004.log`.
- Fresh JUnit XML: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/positive/f-18530C0DE0000004.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml` (SHA-256 `3aa1c3abde359f7de086883c349259efd493220e2277a7bd080250f46cb00ccf`); archived as `raw/positive/f-18530C0DE0000004.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml`.

### F positive — head `F`, seed `18530C0DE0000005`

- Result: rc=0; 1 tests, 0 failures, 0 errors, 0 skipped.
- Log: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/positive/f-18530C0DE0000005.log` (SHA-256 `7ca02136c73b254ed27f86e3e47a3700b6bfb92b83971b14a318e55d89606ee4`); archived as `raw/positive/f-18530C0DE0000005.log`.
- Fresh JUnit XML: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/positive/f-18530C0DE0000005.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml` (SHA-256 `d66ea6cc34948e40f01d163153ee80d39057e14f31925d7bd1175b56415e0fd3`); archived as `raw/positive/f-18530C0DE0000005.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml`.

### Synthetic mutation — head `F + one local helper mutation`, seed `A57A22E346C237C6`

- Result: rc=1; 1 tests, 1 failures, 0 errors, 0 skipped.
- Log: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/mutation/mutation-A57A22E346C237C6.log` (SHA-256 `35481abd2eede65b23b9edaac13191b3824506bb557b7f6d7d5a5d7ff9f5c837`); archived as `raw/mutation/mutation-A57A22E346C237C6.log`.
- Fresh JUnit XML: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/mutation/mutation-A57A22E346C237C6.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml` (SHA-256 `1f365966c0c00d45c594c5c7711146b9e64422e8357d21cb14ebbccf328205a0`); archived as `raw/mutation/mutation-A57A22E346C237C6.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml`.

### Restored F — head `F`, seed `A57A22E346C237C6`

- Result: rc=0; 1 tests, 0 failures, 0 errors, 0 skipped.
- Log: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/restored/f-restored-A57A22E346C237C6.log` (SHA-256 `449340f53bcb59d3bfc0789f00422d48acb6146ae9b359e0f84656d2dd1c98fe`); archived as `raw/restored/f-restored-A57A22E346C237C6.log`.
- Fresh JUnit XML: `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/restored/f-restored-A57A22E346C237C6.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml` (SHA-256 `4677d81183e92137c44475136f5130e078ecf526501202240ac58f1439971aa5`); archived as `raw/restored/f-restored-A57A22E346C237C6.xml/TEST-org.apache.solr.cloud.RecoveryAfterSoftCommitTest.xml`.

## Other required checks

- Tidy: rc=0; log `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/preflight/f-tidy.log` (SHA-256 `747b9406d26f45a43704131ee20a9577c4b9f0d68bd7b141fcb09b2622dc404b`), archived as `raw/preflight/f-tidy.log`; post-tidy tree clean.
- Root check: rc=0; log `/workspace/gates/logs/phone-agent-2/pr-5098-direct-leader-only-round-1/root-check/f-root-check.log` (SHA-256 `1b2a3a54f5ba2ca59db66dbffd9fc039efad5409d8afa0e46aea660027ff8bf0`), archived as `raw/root-check/f-root-check.log`; `BUILD SUCCESSFUL`, 558 tasks.
- Error Prone compile and changelog YAML parse were not requested by this gate; F has no changelog fragment.
- The mutation compiled; the one executed test failed only at `assertTrue(leaderClient.isDirectUpdatesToLeadersOnly())` in `RecoveryAfterSoftCommitTest.java:112`; the preceding `isUpdatesToLeaders()` assertion passed. This is configuration-sensitivity proof, not a production fail-before reproduction.
- The mutation worktree has one intentional helper-only local change; the test source hash is unchanged and the mutation was not pushed. Restored F passed the same seed.
- No PR reply or comment was posted.

## Reviewable artifacts

- [Raw evidence archive](raw-evidence.tar.gz), SHA-256 `35bcc09e854400d8626f9b7e476ca76a8def881ac611fe32b670acb836b27ce9` (212804 bytes).
- [SHA256 manifest](SHA256SUMS.txt), covering 107 preserved raw files.
- [O-to-F source patch](source-O-to-F.patch), SHA-256 `4076922a9cbb12af0cd1d7d2ea286399f4d2ef7400af57286a97ce2b10136ce6`; copy is also under `raw/metadata/` in the archive.
- Commands: `raw/metadata/commands.txt`; final pins/tree state: `raw/metadata/final-pins.txt`; source self-review: `raw/metadata/source-review.md`; immutable original execution claim: `raw/metadata/original-execution-claim.md`.

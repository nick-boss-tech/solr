# Assignment: SOLR-18586 rerun on local test storage, and RC3 re-triage (round 1)

Status: OPEN
Tags: premise-run, records
Intended host: a pool Linux host whose test temp directory is on local disk, not NFS. Check first with `df -T` on the worktree and on the Gradle temp path; if the temp path is NFS, the host is not eligible and the claim must say so.

## Why this exists

`reports/solr-18586-probe-run-round-2.md` (matched runs on `/workspace`, NFS) failed five of six runs at RC3 with `FileSystemException: Too many links` (EMLINK) on hard-linking `_0.si` during index fetch. `reports/solr-18586-probe-run-round-2-local-tmp.md` repeated the same six runs with the worktrees on local ext4 and passed all six, with zero EMLINK lines. The storage path, not the code under test, is the likely difference. The rerun checks that on another host, and asks whether the RC3 smoke failures need re-triage.

## Facts (do not re-derive)

- Source: RC3 `749712b71940c9f96d824c533f32f5c56dcd3924`.
- Failing shape on NFS: `TestTlogReplica.testKillLeader` at seed `3635CC89D23D0929`, "Expected collection to be 1x2" at `TestTlogReplica.java` line 484.
- Local-storage control and probe arms passed all six matched runs on vm2 (see the report above). The probe diff is `material/solr-18586-probe-round-2.diff`; the generator's SHA-256 is `385ba51970b26737bdf166c6fc1ffe4241e797a0969b77b4f895da99f73381bb`.
- Original RC3 smoke failures to re-triage: TestTlogReplica, CoreAdminHandlerTest, TestCoordinatorRole, TestTermsQParserPlugin (`reports/rc3-smoke-loop-round-1.md`, `assignments/rc3-repeating-failures-rootcause-round-1.md`).

## Work

1. Record the host, `df -T` for the worktree and the Gradle temp path, JDK, and the RC3 SHA. Stop and record if the temp path is NFS.
2. Create clean RC3 worktrees on local disk. Do not apply the probe to the first run; the probe is diagnostic only.
3. Run the six matched `TestTlogReplica` runs (testKillLeader, testRecovery, testOnlyLeaderIndexes at seeds `3635CC89D23D0929` and `9D6BB87C4F145405`) under the host lock, one at a time. Keep each run's log and XML in its own directory. Use `pool/bin/sweep-run.sh` or `bg-steps.sh` with continue-on-failure.
4. Count EMLINK lines and index-fetch failures per run, and confirm test counts from the XML. A pass needs XML with `failures="0" errors="0"`.
5. If the six runs pass, rerun the four RC3 smoke-test classes named above on local storage at their recorded seeds, using the same matched method. Report each as pass or fail with the seed.
6. If any run fails on local storage, record the failure with its log and say whether EMLINK appears. Do not treat a failure as a code defect until the EMLINK question is answered.

## Rules

- Premise run only. No fix, no fix branch, no PR, no Jira or GitHub comment, no dev-list text.
- No gate: no tidy, no Error Prone, no changelog check, no receipt.
- Never reuse old XML as new evidence. Archive each run before starting the next.
- Record each decision with its rung (`material/decision-ladder.md`).
- Commit as Nick Shanin with no trailers.

## Deliverables

- `reports/solr-18586-local-storage-rerun-round-1.md`: host, filesystem check, SHA, JDK, per-run table (pass or fail, EMLINK count, seed), the re-triage table for the four smoke classes, and what is still unexplained.
- Run evidence stays on the host; the report gives absolute paths.

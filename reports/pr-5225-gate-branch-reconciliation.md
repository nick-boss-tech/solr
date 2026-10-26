# PR 5225 gate and follow-up branch reconciliation

Checked against the live `coordination-ui` branch and current fork refs on 2026-10-25. This report is add-only; the original gate and assignment remain untouched.

## The two candidates are distinct

1. The existing formal gate, `gates/pr-5225-allowpaths-denial-round-1.md`, pins `nick-boss-tech/solr:solr-pr-5225-allowpaths-tests-submit` at `3a32098d870da94d663f2948a1ba4adf32f1675d`, parent Apache PR #5225 head `53ddbf0e857a74b8475b299b0907ac1582be8248`. Its test file contains two separate cases: `allowPaths - backup with Security Manager` and `allowPaths - backup without Security Manager`. The gate requires four runtime/parser legs, packaging checks, and three discriminating mutation controls. Its status remains OPEN, and `receipts/PR-5225-allowpaths-denial.md` is absent.
2. The updated follow-up candidate is `nick-boss-tech/solr:solr-5225-bats-followup` at `35378004f1797c5b9ae3ba68d2503e4edb3840e8`, parent `096da6567e3db6d2bd141e3d2bf556ad1b3d2509`. It is based on upstream main after PR #5225 merged (`07fdebdda54a24085086281da1cde701e122564d`). Its test file has one version-adaptive case, `allowPaths - backup`, rather than the gate candidate's two separate cases. The branches share an earlier base and diverge; they are not interchangeable snapshots of one patch.

## Evidence that belongs to the follow-up branch

- At `096da6567e3db6d2bd141e3d2bf556ad1b3d2509`, JDK 21 Commons CLI and Picocli diagnostics each ran one test and passed. The packaging check at that head passed with `errorProneSkipped`.
- At final follow-up head `35378004f1797c5b9ae3ba68d2503e4edb3840e8`, fresh JDK 25 Commons CLI and Picocli diagnostics each ran one test and passed. `tidy` and `git diff --check` passed; `:solr:packaging:check -x test` passed on the same source state immediately before the commit, and its log also reports `errorProneSkipped`.
- The failed pre-fix JDK 25 runs at `096da656...` and the corrected final-head results are recorded in `reports/pr-5225-jdk25-followup-cross-host-phone-agent-2.md` and `reports/pr-5225-jdk25-regex-fix-final-head-follow-up.md`. Raw logs are under `/workspace/gates/logs/phone-agent-2/pr5225-jdk25-regex-fix/`.

## Reconciliation decision

Do not retarget or mark the existing gate complete. Its pinned branch has two test cases and a specific Java 25 skip/disabled-case expectation; those requirements cannot be inferred from the single version-adaptive test on the follow-up branch. The newer branch's passing parser runs are useful evidence for that branch, but they do not satisfy the old gate's mutation controls or receipt requirement.

If the follow-up branch is now the intended submission candidate, it needs a separate gate record pinned to `35378004f1797c5b9ae3ba68d2503e4edb3840e8`, with the Java matrix expressed for its one version-adaptive test. The original failure controls should be retained. In particular, the current assertion searches the whole Solr log and has no checkpoint, so the prescribed stale-matching-log control remains untested and may reveal a separate freshness-coverage gap. No mutation run or full-gate claim is made here.

No branch, source file, PR, or Jira item was changed in this reconciliation. The previous gate and reports remain available as written.
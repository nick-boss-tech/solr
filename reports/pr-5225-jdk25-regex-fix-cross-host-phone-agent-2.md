# PR 5225 follow-up: JDK25 assertion-order fix and local validation

Claim: `claims/pr-5225-jdk25-regex-fix-phone-agent-2.md`. Nick approved a focused regex correction and rerun of the Java 25 parser legs.

## Source state

The starting branch is `nick-boss-tech/solr:solr-5225-bats-followup` at `096da6567e3db6d2bd141e3d2bf556ad1b3d2509`. I changed only the Java 24+ `assert_file_contains` expression in `solr/packaging/test/test_security_manager.bats`: it now matches `${backup_dir_other}/test-fail/COLL_NAME` before `must be relative to SOLR_HOME`, which is the order in the actual Solr error log.

The change remains a single uncommitted local edit in `/workspace/tmp/pr5225-jdk25`. It was not committed or pushed, and no PR or Jira action was taken. The fork branch remains unchanged at `096da6567e3db6d2bd141e3d2bf556ad1b3d2509`.

## Fresh Java 25 results

Both runs used OpenJDK 21.0.12.1 for Gradle and OpenJDK 25.0.4.1 for the BATS process and Solr runtime. `JAVA_HOME` and `SOLR_JAVA_HOME` were set explicitly to JDK 25, `SOLR_INCLUDE=/dev/null` prevented a discovered include from overriding them, and the parser setting was explicit.

1. Commons CLI: `:solr:packaging:integrationTests --tests test_security_manager.bats --rerun`, `SOLR_PICOCLI=false`. One test passed, zero failures/errors/skips; Gradle succeeded. The BATS run took 25.637 seconds.
2. Picocli: `:solr:packaging:integrationTestsPicocli --rerun`, `SOLR_PICOCLI=true`, using a root-build-only init script selecting only `test/test_security_manager.bats`. One test passed, zero failures/errors/skips; Gradle succeeded. The BATS run took 33.999 seconds.

For comparison, before the correction each JDK 25 parser run executed one test and failed only at the regex assertion. In both saved Solr logs, the denied path was present before `must be relative to SOLR_HOME`, confirming the ordering issue.

## Additional checks

The final `tidy` task passed with the one-file tree still containing only the intended one-line change. `git diff --check` passed. The final `:solr:packaging:check -x test` passed at the same candidate head with 54 actionable tasks (29 executed, 25 up-to-date). Its log includes `errorProneSkipped`; this report does not claim Error Prone validation.

Raw evidence remains under `/workspace/gates/logs/phone-agent-2/pr5225-jdk25-regex-fix/`, including both fresh XML reports and full Solr logs. The filesystem had 174,455 free inodes after validation, about 20.8%. I performed no cleanup.

## Limits

These are diagnostic results on follow-up branch `solr-5225-bats-followup`; the formal gate still pins the different branch `solr-pr-5225-allowpaths-tests-submit` at `3a32098d870da94d663f2948a1ba4adf32f1675d`. Its JDK25 evidence and mutation controls remain outstanding. No full-gate or GREEN claim is made.
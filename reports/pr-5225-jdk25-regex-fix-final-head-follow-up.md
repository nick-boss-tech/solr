# PR 5225 follow-up: pushed Java 25 assertion fix and final-head validation

Follow-up to `reports/pr-5225-jdk25-regex-fix-cross-host-phone-agent-2.md`, which records the local-only state before Nick approved the commit and push.

## Push

The one-line assertion-order fix is committed and pushed to `nick-boss-tech/solr:solr-5225-bats-followup` at `35378004f1797c5b9ae3ba68d2503e4edb3840e8`, parent `096da6567e3db6d2bd141e3d2bf556ad1b3d2509`. The only changed file is `solr/packaging/test/test_security_manager.bats`. Author and committer are both `Nick Shanin <nick.boss.us@gmail.com>`. The local tree is clean and the remote branch matches the commit. No PR or Jira action was taken.

## Final-head proof

At the pushed SHA, the tests passed with OpenJDK 21.0.12.1 for Gradle and OpenJDK 25.0.4.1 for the BATS/Solr runtime. Both runs explicitly set the runtime Java variables and include/parser settings.

1. Commons CLI: `:solr:packaging:integrationTests --tests test_security_manager.bats --rerun`, `SOLR_PICOCLI=false`. One test passed, with zero failures, errors, or skips; BATS ran for 20.850 seconds.
2. Picocli: `:solr:packaging:integrationTestsPicocli --rerun`, `SOLR_PICOCLI=true`, with the init script narrowed to `test/test_security_manager.bats`. One test passed, with zero failures, errors, or skips; BATS ran for 28.336 seconds.

Both captured Solr logs fingerprint the runtime as `jvm 25.0.4.1+1-1-24.04.4-Ubuntu`. The corrected regex follows the actual error format, where the denied path precedes the `must be relative to SOLR_HOME` text.

The same one-line source state passed `tidy`, `git diff --check`, and `:solr:packaging:check -x test` before commit. The packaging log reports `errorProneSkipped`; no Error Prone validation is claimed. Raw final-head logs and XML are preserved under `/workspace/gates/logs/phone-agent-2/pr5225-jdk25-regex-fix/` in `jdk25-commons-cli-final.*` and `jdk25-picocli-final.*`.

## Limits

This validates the follow-up branch only. The live formal gate still pins `solr-pr-5225-allowpaths-tests-submit` at `3a32098d870da94d663f2948a1ba4adf32f1675d`, so it remains open and incomplete; its JDK25 results and mutation controls are not supplied by this report. I did not remove any workspace files after these runs. At report time, 174,376 inodes were free (about 20.8%).
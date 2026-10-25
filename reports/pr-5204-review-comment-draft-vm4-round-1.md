# Draft: GitHub review comment for apache/solr#5204 (not posted; owner review required)

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

@jaykay12, I ran the new help tests with `SOLR_PICOCLI=false` set explicitly, and found a selector issue in `bats_helper.bash`.

`assert_output_for_cli` checks `[ -n "${SOLR_PICOCLI:-}" ]`, so any non-empty value selects the picocli wording, including `false`. `bin/solr` only switches to picocli for the literal `true`. With `SOLR_PICOCLI=false` on the reviewed head (`7eb288a6f5`), 7 of 14 tests in `test_help.bats` fail on the picocli text (for example `solr help flag prints help` expects `usage: bin/solr`, while the legacy launcher prints `Usage: solr COMMAND OPTIONS`). `test_post.bats` "Check help command" fails the same way on the `Missing required option` message. The restart help test is also skipped under `false`, since its skip checks for presence rather than `true`.

The narrow fix is to match the launcher, so the helper selects picocli only for `true`. The same rule is used for the restart skip. I added one test that sets `SOLR_PICOCLI=false` for a single assertion, so the false branch is covered by the suite. The fix is in this 1-commit compare: https://github.com/apache/solr/compare/7eb288a6f5777770d4472fc2c550b66e0d1cec12...nick-boss-tech:solr-18566-pr5204-cli-review

Results at the exact SHAs:
- Reviewed head `7eb288a6f5`, false mode, `test_help.bats`: 7 failures as above, reproduced.
- Fixed head `daa29b06e0`, false mode, `test_help.bats` and `test_post.bats`: 27 of 27 pass, and the restart help test runs.
- Fixed head, default legacy suite (`SOLR_PICOCLI` unset): 123 passed, 5 skipped (Docker and `timeout` unavailable), 0 failed.
- Fixed head, picocli suite: 122 passed, 6 skipped (5 environmental, and the restart help test, which is skipped by design for picocli), 0 failed.

Limits: I ran these on a Linux host without Docker, so the Docker-dependent tests were skipped. The js-client npm build fails at this head with a missing `@babel/plugin-syntax-dynamic-import`, so I passed `-PdisableJsClient=true`. That is separate from this change. I have not pushed anything to your PR branch, and this comment does not resolve the existing review threads. The include-file question in the existing threads is separate from this selector finding, and I have not touched it here.

🤖 _Small mechanical diff for reference: https://github.com/apache/solr/compare/7eb288a6f5777770d4472fc2c550b66e0d1cec12...nick-boss-tech:solr-18566-pr5204-cli-review_

# PR 5281 test-workflow repair — Linux gate evidence

This bundle supports the GREEN receipt at exact fork head `a3a930aa07d9676315f6133bdb1d7c88a1940942` (H1), with H0 `00a5f9701ea9850a26b1bc1ba83326edbd92f383`. The original source commit and the changed workflow are pinned in `source/identity.txt`; `source/H0-to-H1.patch` records the one-line indentation change.

## Results at a glance

The detached pre-claim H1 tidy preflight passed (62 tasks). `validator/h0/` preserves the exact H0 workflow export, its Git blob comparison, the expected `actionlint` syntax rejection and exit code. `validator/h1/` contains the successful two-workflow actionlint run with ShellCheck. `unittest/` preserves the credential-free exact discovery command and full 44-test output; all 44 passed with no skips.

`tidy/` and `root-check/` contain the exact commands, complete Gradle logs, exit codes, post-run status and whitespace checks. The root check used only the required `-x test` exclusion. `preflight/preflight-watch.tsv` and `combined-resource-watch.tsv` contain the continuous resource samples; `daemon-stop/` and `final-host-check.txt` record lock/process shutdown verification. `final-pins-pre-receipt.txt` records the final remote fork, upstream main and board refs before receipt publication.

`source/pr-triage-test-H1-numbered.txt` and `source/pr-triage-llm-H1-numbered.txt` support the permission/security review; the empty `source/pr-triage-llm-H0-H1.diff` demonstrates that the production workflow did not change. Tool versions and initial preflight state are in `preflight/preflight-start.txt` and the posted claim.

`SHA256SUMS` covers all evidence files in this directory except itself and this index. The receipt and additive claim closeout are separate files on the same board commit as this evidence bundle.

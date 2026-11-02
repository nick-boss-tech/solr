# Report: PR 5265 CI failure repair, round 1 gate

The exact-head gate at candidate `16b0d50218bf82dc0d28158a63602c2d3da2ef3a` ran on the phone agent and closed PARTIAL. Receipt: `receipts/PR-5265-ci-failure-repair-round-1.md`.

Proven at the head: tidy clean; `TestApiFramework.testFramework` passing at the CI seed 4E30896B42ECC98D; the six split-shard and neighboring suites passing at seed 16398C0DE (44 tests, no failures, no skips); Error Prone compile and `:solr:core:check -x test` green; final head read-back at the candidate.

Not proven: the Admin UI Selenium leg. Its first attempt died at `verifyUiWasm` on this VM's known relay download corruption (remedied by seeding the byte-verified pinned zip into the build directory, disclosed in the receipt). Its second attempt reached the browser launch and failed there: the host's only Chromium is the Hatch-managed build, which will not start outside its own harness, and a WebDriver launch cannot supply the switches it demands. The leg needs a host with a stock browser build; nothing about the candidate tree blocks it.

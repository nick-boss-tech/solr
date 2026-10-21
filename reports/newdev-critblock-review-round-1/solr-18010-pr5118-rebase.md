# SOLR-18010, PR #5118: rebase onto main after #5065, review round 2 (vm3, 2026-10-21T19:08:56Z)

Context: #5065 (SOLR-18010, merged to apache/main at 1afeee27c4) added a per-handler lock for every request on SecurityConfHandlerLocal. Our PR #5118 overlapped it in two files.

## Experiment (owner authorized, this branch only)

- The branch `solr-18010-submit` was rebuilt as one commit on apache/main: `57ad783bd5`. Force push with a lease on the old tip `571307400b`. The old tip is kept as local backup `backup/solr-18010-submit-571307400b`.
- Decisions: keep #5065's lock (it covers reads, so it is the wider and already merged serialization); drop our POST-only lock; keep our atomic write, permission preservation, and metadata strip in `persistConf`; keep our test class and the added BasicAuth test lines; replace the changelog with one covering the atomic write and metadata change only.
- Gate on `57ad783bd5`: changelog, tidy, compile (Error Prone forced), focused SecurityConfHandlerTest and BasicAuthStandaloneTest (6 tests, 0 failures), module check rc 0. Logs `gates/logs/vm3/solr-18010-rebase-r1/`.

## Round 2 findings on `57ad783bd5`

1. Minor, style: a blank line was added between `protected Path securityJsonPath` and the lock field. Fixed.
2. Minor, error handling: the `finally` that deletes the temporary file could throw an `IOException` and replace the real persist failure. Fixed by logging the cleanup failure.

Fix commit `dfcf50aec0` (fast-forward from `57ad783bd5`). Gate on `dfcf50aec0`: changelog, tidy, compile, focused 6 tests (0 failures), module check rc 0. Logs `gates/logs/vm3/solr-18010-rebase-r2/`.

## Open for later rounds (not fixed here)

- Scope of the lock is per JVM. Two Solr processes sharing one SOLR_HOME are not serialized. #5065 has the same limit.
- New `security.json` files get mode 0600 from `createTempFile`; the base code gave the umask default. Kept on purpose (credential file), recorded in the earlier unit 20 review.

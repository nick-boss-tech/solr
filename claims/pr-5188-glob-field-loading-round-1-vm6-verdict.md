# Failure verdict: PR 5188 glob field loading, Linux gate round 1 (vm6)

- Claim: `claims/pr-5188-glob-field-loading-round-1-vm6.md` (`2026-10-26T16:09:23Z` is the verdict time, UTC).
- Verdict: failed. The superseding gate at `b428596720d0ee1119167d04323681252e86136d` did not pass. The two regression methods fail on F, and the pre-fix legs fail on the same assertions, so they do not discriminate the fix.
- Reason: the response-shape xpath assertions (`str` expected, `arr` returned) fail on base and on F, and the 8 `TestPseudoReturnFields` failures trace to a leaked document from `testGlobRTGStoredFieldLoading`, which has no cleanup. Details: `reports/pr-5188-glob-field-loading-gate-failure-round-1.md`.
- Next: a repair or decision on the expected response shape and the test cleanup, then a new superseding gate naming the new head. No receipt is written.

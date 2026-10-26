# PR 5188 glob field loading, repair round 2 (vm6)

- Failed head: `b428596720d0ee1119167d04323681252e86136d`, superseding gate failure report `reports/pr-5188-glob-field-loading-gate-failure-round-1.md`.
- Repair commit: `53ad2ccacbf205333f9029ed9f6ebbc93da034f2` (`PR 5188: match glob test response fields by name and value`), Nick Shanin, no trailers, fast-forward pushed; remote read back as this SHA.
- Scope: test files only, 11 insertions and 11 deletions. Within `BasicFunctionalityTest.testGlobHighlightPrefetch`, `testGlobRTGStoredFieldLoading` and `TestPseudoReturnFields.testGlobRTGUncommittedFields`, each `//doc/str[@name='X']` or `//result/doc/str[@name='X']` became `//doc/*[@name='X']` (or `//result/doc/*[@name='X']`), keeping the same value and count checks. No production line changed, and no lazy-loading assertion changed.

## Evidence that the old expectation was wrong, not the fix

Probe copy of the failed head, with diagnostic prints only, logs under `/workspace/gates/logs/vm6/pr5188-round-1-superseding-probe/` and `pr5188-round-1-probe-pre/`:

- At `b428596`, explicit `/get fl=title`, glob `/get fl=tit*`, explicit `/get fl=source_s`, and `/select fl=source_s` with `hl=false` and `hl=true` all return the field as `<arr name=...><str>...</str></arr>`, with one value.
- The base `e07455a` (pre-fix production with the probe tests) returns the same shape for the same requests. So the shape is not from this PR.
- The string-value of a single-value `arr` is its value, so `//doc/*[@name='X'][.='v']` checks the same field and value for `str` and `arr`.
- `TestPseudoReturnFields.testGlobRTGUncommittedFields` passed at `b428596` with `str`, so the shape depends on the test's schema; a hardcoded element name is fragile.

## Not changed

- The `numFound="6"` failures in `TestPseudoReturnFields` (8 tests) remain. The new RTG test already deletes and commits its doc in `finally`, so my earlier suspicion (a leaked doc from `testGlobRTGStoredFieldLoading`) is not supported. The cause is open and is checked in the next full-suite run.

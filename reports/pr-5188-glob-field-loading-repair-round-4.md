# PR 5188 glob field loading, repair round 4 (vm6)

- Failed head: `7ac2bc86fed51ed420f3b1a9559e167631e5a3c8` (round 3). Its suite leg ran 25/35/16/1 in the four classes; one prefetch failure (`actual array was null`, unified highlight) and eight `numFound="6"` failures in `TestPseudoReturnFields`.
- Repair commit: `51e92b63fddb97db83a3962f758bde3c79f1c4dd` (`PR 5188: keep the uncommitted glob test uncommitted and check unified snippets only where produced`), Nick Shanin, no trailers, fast-forward pushed; remote read back as this SHA.
- Scope: two test files only.

## Decisions (ladder, recorded before the run)

1. **numFound=6 (TestPseudoReturnFields).** Rung 1: `TestPseudoReturnFields.java:58-61` (`@Before addUncommittedDoc99`) adds doc 99 uncommitted before every test, and the comment says it must stay in the transaction log. The base class commits only in `@BeforeClass` (line 53). The new `testGlobRTGUncommittedFields` was the only method that committed during the class (its `commit()` at start and in `finally`). Probe (`pr5188-round-1-probe-f3`): the id listing shows `99`, not the new test's `7782`. Settled at rung 1. Repair: remove the two `commit()` calls. The uncommitted-visibility assertions (`numFound=0` before and after) do not need a commit.
2. **unified snippet is null (BasicFunctionalityTest.testGlobHighlightPrefetch).** Rung 2: probe `pr5188-round-1-probe-unified`. On base `e07455a` and on head `7ac2bc8`, `hl.method=unified` returns an empty highlight for doc 7784 for `fl=source_s`, `fl=source_*` and `fl=*`; `hl.method=original` returns `title` in all three. So the unified snippet is not produced by the PR and not by base in this harness. Choice: keep the unified prefetch checks (eager cached fields and the lazy `unrelated_s`), assert the `<em>keyword</em>` snippet only for `original`, and record unified snippet output as **not proven** by this gate. This narrows the gate's stated coverage ("exact output/snippets" for unified). Owner may reject this narrowing; see the receipt limits.

## Pre-fix overlay

`e07455a` production plus F4's `BasicFunctionalityTest.java` and `TestPseudoReturnFields.java`.

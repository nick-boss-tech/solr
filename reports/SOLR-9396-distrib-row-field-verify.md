# SOLR-9396 distributed row-field test: verification, round 1

Assignment: assignments/pool-solr-9396-distrib-row-field-round-1.md. Run by vm2, 2026-10-16T03:57:52Z. Runner gates/g9396-distrib-verify.sh, log logs/vm2/9396-verify-result.txt. Seed 9396C0FFEE9396, passed with -Ptests.seed. Gradle did not print the seed in these logs, so the configured seed is the one recorded.

## Head

- Fork tip solr-9396-submit: f62c06588f272834cae16f3f02c8383886b8f00d, confirmed by ls-remote at the start of the run. The tip had not moved.

## Head run

- Compile of test sources at head: rc=0.
- TestSubQueryTransformerDistrib: rc=0. Counted from fresh JUnit XML: tests=2, failures=0, errors=0, skipped=0. This includes testRowFieldNotInFlDistributed.
- TestSubQueryTransformer: rc=0. Counted from fresh JUnit XML: tests=11, failures=0, errors=0, skipped=0.

## Fail-before leg

- Base: c3cdf7b46e8cfff3673f76d881f32cf8e7b00622, detached worktree. Only TestSubQueryTransformerDistrib.java was copied from the head; nothing else was copied. The base worktree was restored afterward.
- Run: TestSubQueryTransformerDistrib, rc=1 (nonzero expected). Counted from fresh JUnit XML: tests=2, failures=1, errors=0.
- Failing test: testRowFieldNotInFlDistributed. Failure text by content: "AssertionError: dave works in both depts", with the response showing the subquery read the row field. The first failure line is TestSubQueryTransformerDistrib.java:280.
- The other test in the class passes at base.

## Format

- spotlessJavaCheck at head: rc=0. Clean. Nothing applied and nothing committed.

## Verdict

**PASS.** The head is green (2 of 2 and 11 of 11), the new test fails on base by its assertion, and the format check is clean. The distributed path is covered: the test fails without the fix and passes with it.

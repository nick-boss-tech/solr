# SOLR-18010 CI fix, round 1 (vm3)

Branch: `solr-18010-submit`. Tip before: `46869c4d8bc9`. Fix: `d742eb99dd` (tree `5b48603d0a`), pushed as a fast-forward. The PR was not flipped, commented on, or posted to.

## The failure

Upstream CI on PR #5118 failed in `:solr:core:compileTestJava` under Error Prone with `-Werror`:
`SecurityConfHandlerFailurePathsTest.java:361: [ReferenceEquality] Comparison using reference equality instead of value equality` on `if (group == FAKE_GROUP)`.

Reproduced locally on `46869c4d8b`: `:solr:core:compileTestJava -Pvalidation.errorprone=true` fails with the same warning and `warnings found and -Werror specified`. Log: `/workspace/gates/logs/vm3/solr-18010-errorprone/compile.log`.

## The fix

`if (group == FAKE_GROUP)` is now `if (Objects.equals(group, FAKE_GROUP))`, with the `java.util.Objects` import.

Why `Objects.equals`: the `group` argument comes from the production path, `Files.readAttributes(...).group()`, which is never null, and `PosixFileAttributeView.setGroup` does not accept null. `Objects.equals` is still the null-safe form. `FAKE_GROUP` is a lambda, so `equals` is identity, and the sentinel behaves as before.

## Scan for the same pattern

The PR changes six files (GitHub files API for #5118: changelog, `SecurityConfHandlerLocal.java`, `SecurityConfHandlerFailurePathsTest.java`, `SecurityConfHandlerReaderTest.java`, `SecurityConfHandlerTest.java`, and the reference guide page). `git diff 63f2459f3b` over those files' added lines contains six `==` or `!=` comparisons:

- `type == PosixFileAttributes.class`, `type == PosixFileAttributeView.class`, `type == FileOwnerAttributeView.class` (in `FaultyFileSystemProvider`): `Class` objects are singletons, so reference comparison is correct. Error Prone did not flag them. Left as is.
- `group == FAKE_GROUP`: the flagged line, fixed above.
- `handler.persistConf(... i % 2 == 0 ? large : small)`: an int comparison, not an object. Left as is.

No other reference-equality comparison on objects or strings was found. Scope of the scan: the six PR files only. The branch's 170 commits past `e044bf20b` are not in the PR diff and were not scanned.

## Verification (fresh results)

- Tidy: applied. The only change it made was in `SecurityConfHandlerFailurePathsTest.java` (the import and this line). Tidied tree `5b48603d0a`.
- `:solr:core:cleanCompileTestJava :solr:core:compileTestJava -Pvalidation.errorprone=true --rerun`: exit 0, no warnings. Log: `/workspace/gates/logs/vm3/solr-18010-ci-fix-r1/compile-errorprone.log`. Production compile with Error Prone also passed in the earlier run on `46869c4d8b`; only the test warning failed.
- `:solr:core:test --tests org.apache.solr.handler.admin.SecurityConfHandlerFailurePathsTest`: exit 0. JUnit XML (`TEST-org.apache.solr.handler.admin.SecurityConfHandlerFailurePathsTest.xml`, written 2026-10-23T01:51:55Z): tests 8, failures 0, errors 0, skipped 0. Copy: `/workspace/gates/logs/vm3/solr-18010-ci-fix-r1/`.

The change touched one file and the scan found nothing else to fix, so the branch's wider gate was not rerun (per the assignment's step 3).

## Not covered

- CI itself was not rerun. The Error Prone result here is a local `compileTestJava` with the flag on, not the Crave or GitHub Actions run.
- Lint and the full `check -x test` were not rerun on `d742eb99dd`. The last full gate was on `46869c4d8b`.

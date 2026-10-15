# Gate job: SOLR-18010 re-gate after the permission patch

- Branch: solr-18010-submit. Head: `2b5ad801c9e88807337f4f0a3fdd689126ad37d1` (fork tip). Previous heads: `9e3ccafe39098bdc23cc8ea3313918071150e148` (changelog title) and `c3685bb37d9d74e4ed538d6d1b65bb1b60970677` (gated code).
Status: DONE (GATE GREEN at 2b5ad801c9e8)
- Capability tags: gate.
- Intended host: Linux only (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: full focused gate at the new head, with a fail-before run. The change keeps the existing security.json permissions when the file is replaced.
- Why: the head moved for a code change. The gate at `c3685bb37d9d` does not cover it.

## Changes to check

1. `SecurityConfHandlerLocal.persistConf`: after the temporary file is written and before the move, the existing file's POSIX permissions are copied to the temporary file. A failure in that copy is logged at debug and does not fail the edit.
2. `SecurityConfHandlerTest.testEditKeepsExistingSecurityJsonPermissions`: POSIX only (skipped elsewhere). It sets security.json to 0640, makes a real edit, and asserts alice was added and the permissions are still 0640.
3. The changelog title from `9e3ccafe` is still in place.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/solr-18010-submit` must equal `2b5ad801c9e88807337f4f0a3fdd689126ad37d1`. If not, stop and record the new tip.
2. Diff scope against `9e3ccafe39098bdc23cc8ea3313918071150e148` must list only `SecurityConfHandlerLocal.java` and `SecurityConfHandlerTest.java`.
3. Changelog YAML parse for `changelog/unreleased/SOLR-18010-security-json-concurrent-edits.yml`.
4. Tidy, with the GATE INVALID post-tidy guard.
5. Spotless check on `:solr:core`. The 102-column seed line in the test matches an existing line in the same file.
6. Error Prone compile.
7. Focused run: SecurityConfHandlerTest, BasicAuthStandaloneTest and V2SecurityAPIMappingTest, with fresh JUnit XML. Record counts, seeds and date. The new test must pass.
8. Fail-before: revert the `persistConf` change only, keep the new test, and run the new test. It should fail with 0600 against 0640. Record the failure text.
9. Module check for `:solr:core`.
10. On green, refresh the receipt with the new head, counts, the fail-before text and date. Mark this job DONE.

## Checklist

- [x] head confirmed by ls-remote
- [x] diff scope confirmed (two files)
- [x] changelog parse
- [x] tidy clean with the guard; spotless check
- [x] Error Prone compile
- [x] focused run with JUnit XML; new test passes
- [x] fail-before recorded (0600 against 0640)
- [x] module check
- [x] receipt refreshed at the new head; job DONE

## Result, 2026-10-13 (vm2)

- head: ls-remote 2b5ad801c9e88807337f4f0a3fdd689126ad37d1, matches the job.
- Changelog YAML parse: rc=0.
- Tidy: rc=0, tree clean after tidy. Spotless check: rc=0. Error Prone compile: rc=0.
- Focused, seed 18010C0FFEE18010, fresh JUnit XML (logs/vm2/18010-perms-xml): SecurityConfHandlerTest 4 tests, 0 failures (includes testEditKeepsExistingSecurityJsonPermissions); V2SecurityAPIMappingTest 5 tests, 0 failures; BasicAuthStandaloneTest 1 test, 0 failures.
- Fail-before: production reverted to 9e3ccafe39098bdc23cc8ea3313918071150e148, new test kept. rc=1 as expected. The new test fails on the permissions assertion: `security.json lost its permissions in the edit expected:<[OWNER_READ, OWNER_WRITE, GROUP_READ]> but was:<[OWNER_WRITE, OWNER_READ, OTHERS_READ, GROUP_READ]>` (0640 expected, 0644 observed). The job text above says "0600 against 0640"; the observed value is 0644, so the job wording was wrong and the observed failure is the premise. Fail-before XML: logs/vm2/18010-perms-failbefore-xml (1 test, 1 failure).
- Module check: `:solr:core` check rc=0. Tree restored to head after the fail-before run.

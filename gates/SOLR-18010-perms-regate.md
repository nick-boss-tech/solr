# Gate job: SOLR-18010 re-gate after the permission patch

- Branch: solr-18010-submit. Head: `2b5ad801c9e88807337f4f0a3fdd689126ad37d1` (fork tip). Previous heads: `9e3ccafe39098bdc23cc8ea3313918071150e148` (changelog title) and `c3685bb37d9d74e4ed538d6d1b65bb1b60970677` (gated code).
- Status: CLAIMED by vm2 (gate backlog coordinator), 2026-10-13T14:35Z; claim claims/pool-vm2-gate-backlog-18010-perms.md. Runner /workspace/gates/g18010-perms-gate.sh.
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

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed (two files)
- [ ] changelog parse
- [ ] tidy clean with the guard; spotless check
- [ ] Error Prone compile
- [ ] focused run with JUnit XML; new test passes
- [ ] fail-before recorded (0600 against 0640)
- [ ] module check
- [ ] receipt refreshed at the new head; job DONE

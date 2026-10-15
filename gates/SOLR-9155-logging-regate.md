# Gate job: SOLR-9155 re-gate after the ERROR log restore

- Branch: solr-9155-submit. Head: `29bf373828994c622ac478a968d6cff4f4c45fa5` (fork tip after the fast-forward push). Previous gated head: `dbd88c84ff246631875afc010c1c4dff4e50c28a`.
Status: DONE
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: full hardening gate at the new head. The change is one restored log line and a changelog title.
- Why: the head changed, so the receipt at `dbd88c84ff2` no longer covers it. The draft's Proof is bracketed until this lands.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/solr-9155-submit` must equal `29bf373828994c622ac478a968d6cff4f4c45fa5`. If it does not, stop and record the new tip.
2. Changelog YAML parse for `changelog/unreleased/SOLR-9155-zkcontroller-getleader-interrupt.yml`.
3. Tidy, with the GATE INVALID post-tidy guard. Restore `gradle/libs.versions.toml` if tidy changes it.
4. Error Prone compile, with the register path (ZkController.java line 1485) and the rejoin path (line 2605) compiling unchanged.
5. Focused run: ZkControllerGetLeaderTest, with fresh JUnit XML. Grep the branch for the exact package first. Record the counts, the seed and the date.
6. Module check: `:solr:core:check -x test`.
7. Premise leg: not re-run. The base code is unchanged, and the premise compile message at `97d973814336` stands in `receipts/SOLR-9155.md`.
8. On green, refresh `receipts/SOLR-9155.md`: the new head, the counts from the JUnit XML, and the note that the log line was restored. Mark this job DONE.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] changelog parse
- [ ] tidy clean with the guard
- [ ] Error Prone compile
- [ ] focused run with JUnit XML; counts and date recorded
- [ ] module check
- [ ] receipt refreshed at the new head; job DONE

# Report: PR 5107 credentials-option declaration, F4 (phone agent)

Status: DONE at Nick's direction on 2026-11-02 (new commit plus an
update to the posted comment).

- New head F4: `7aa3f77bed263728a13f361d9f371bf16937672c` on fork
  branch `solr-8442-security-review-r2-submit`, parent exactly F3
  `e75405eff845`. Author and committer Nick Shanin, no trailers.
  Plain push; fork ref verified by ls-remote.
- Change (2 files): `RunExampleTool.java` declares
  `CommonCLIOptions.CREDENTIALS_OPTION` in `getOptions()`, adds the
  picocli `credentialsOpt` field (`-u`/`--credentials`), and passes
  it into `StartSolrParams` in the picocli `callTool()` (which
  previously hardcoded null); `SolrCLIRunExamplePicocliTest` gains
  `testCredentialsOptionIsAccepted` covering both spellings. This
  completes the fix for the defect in
  `reports/pr-5107-example-rerun-local-diagnostic-round-1.md`: the
  tool read the option value without declaring it in either parser.
- Proof pair at seed 8442C0DE, logs `~/workspace/tools/5107-f4-logs/`:
  with the production file reverted, the new test fails (the picocli
  spec has no credentials option); with the fix, the new test and the
  neighboring parse tests pass. `:solr:core:compileJava
  -Pvalidation.errorprone=true` rc=0; tidy rc=0 with only the two
  intended files modified.
- Behavior on a rebuilt dev distribution at F4: `bin/solr start
  --force -e secure-setup --credentials solr:SolrRocks` now gets past
  argument parsing into the secure-example wizard in both the default
  and picocli modes (both stopped at the interactive keystore prompt;
  no `-y` was passed). Full example starts still cannot complete on
  this host for the unrelated environment reason in the diagnostic
  report (intercepted Java localhost connects trip the port probe),
  and the BATS suites remain unrun; the pool assignment
  `assignments/pr-5107-example-rerun-diagnostic-round-1.md` owns the
  end-to-end sequences.
- The posted comment (6585431435) was edited to F4: the credentials
  account now states both ends of the defect, the commit count is
  four with the declaration named, the verification block names this
  head and the parse proof, and the take-over command pins F4. The
  edited body was read back byte-identical.

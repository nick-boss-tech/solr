# Claim: pool-vm5-gate-backlog-3243-edismax-p1

- Claimant host: vm5, matching a file in `hosts/` (`hosts/vm5.md`).
- Status: DONE 2026-10-18T08:47:19Z, stopped at the proof leg. Deliverables: none. The job is set to HANDED for the main side to choose the proof-leg base.
- Job or assignment: `gates/SOLR-3243-edismax-p1.md`.
- Slice: the whole job (full gate on the P1 head `a07674262eee472a8faf1bfebc0a95e8e611b362`, focused class, receipt).
- Capability tags used: gate.
- Started: 2026-10-18T08:24:58Z (UTC).

Heartbeat: 2026-10-18T08:24:58Z. Base confirmed: `solr-3243-submit` at `a07674262eee472a8faf1bfebc0a95e8e611b362` (git ls-remote origin, matches the job's Head line). Build environment: `~/.pool-env` (JAVA_HOME and GRADLE_USER_HOME from the host file). Gate worktree: `/workspace/worktrees/vm5-3243-gate`, detached at the head. Logs: `/workspace/gates/logs/vm5/g3243-*`.

Result: 2026-10-18T08:29Z: step 0, changelog YAML parses. Diff scope against `1db99c13662d` matches the job (changelog title, and the new test at +12). Head matches the job.

Result: 2026-10-18T08:32:11Z: step 1, `tidy` at `a07674262e`, rc=0, tree unchanged afterward (`g3243-tidy.log`). Conclusive.

Result: 2026-10-18T08:40:05Z: step 2, `:solr:core:compileTestJava -Pvalidation.errorprone=true` at `a07674262e`, rc=0, BUILD SUCCESSFUL (`g3243-errorprone.log`). Conclusive.

Result: 2026-10-18T08:47:19Z: step 3, proof leg, NOT as expected. Command: parser `ExtendedDismaxQParser.java` reverted to `97d973814336` (the job's named base), head test file kept, `:solr:core:test --tests org.apache.solr.search.TestExtendedDismaxParser -Ptests.seed=3243C0FFEE3243`. Fresh XML (`g3243-proofleg.xml/`): 43 tests, 4 failures.
- `testNegatedUnfieldedOpenRangeExcludesEveryDocument`: `numFound` 0 != 39. This is the expected failure.
- `testUnfieldedOpenRangeMatchesDocWithoutQfValue`: `numFound` 1 != 0. Not expected.
- `testUnfieldedOpenRangeIsMatchAllDocs` and `testUnfieldedOpenRangeWithPhraseFields`: AssertionError. Not expected.

Information gathering: the three extra tests were added by commit `24f09dc007` ("SOLR-3243: edismax: unfielded [* TO *] is a match all docs query"). That commit also changes `ExtendedDismaxQParser.java` (the only branch commit that does). Reverting to `97d973814336` removes that behavior too, so the leg tests the whole widening, not only the negated form. The leg does not isolate the new test, so it is not conclusive. The job's decision row "Proof-leg failure shape: stop" applies. The head parser is restored and the gate worktree is clean at `a07674262e`. No step 4 run, no receipt, and no relaunch of the same leg.

Question for the main side: which base should the proof leg revert to? Option (a): the parent of `24f09dc007`, so only the negated-form behavior differs from the parser under test. Option (b): keep `97d973814336` and treat the three widening tests as named expected failures. Either way, a new base is a new input, so the gate runs again from step 3.

DONE: 2026-10-18T08:47:19Z. Deliverables: none. Claim closed on stop; the job is set to HANDED so the main side can choose the proof-leg base.

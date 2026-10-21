# Review round 1, unit 20: SOLR-18010 (branch solr-18010-submit), vm3

## Pass 1 (vm3, 2026-10-21T06:28:50Z)

- Head reviewed: `571307400b6917ce458db989f29febb5cff8a600`. Base `14c7aac0d1`. Diff: `SecurityConfHandlerLocal.java` (+68/-4), `SecurityConfHandlerTest.java` (+294), `BasicAuthStandaloneTest.java` (+25), changelog. Commits c3685bb37d, fadc5ee31e, 9e3ccafe39, 2b5ad801c9, 571307400b.
- Ticket SOLR-18010 (Open, Critical): the Admin UI issues one edit request per granted permission without waiting; two concurrent role edits each rewrite security.json from the same starting content, and the file ends up corrupted.
- What the branch does: a monitor serializes POST edits on the handler instance (`editLock`); the local persist writes a temp file in the same directory and moves it over security.json atomically; an existing file keeps its permissions.
- Surrounding code check (rung 1): the base handler stores the ZooKeeper version under the empty key `""` (`SecurityConfHandler.java:147`). The local handler never reads that version. The repo's own `MockSecurityHandler` (`SecurityConfHandlerTest.java` near line 555) removes `""` before comparing with the standard JSON. So stripping `""` from the local file matches the existing contract and is not a regression. Settled at rung 1.
- Finding, minor (decided at rung 4): a new `security.json` is created by `Files.createTempFile` with mode 0600, where the base write used the umask default (usually 0644). The existing-file case keeps permissions and is tested (`testEditKeepsExistingSecurityJsonPermissions`). Decision: keep 0600 for a new file. The file holds credential hashes, so the narrower mode is the safer default, and the ticket asks nothing about modes. Recorded in the claim; not a repair.
- Repairs in pass 1: none.
- Regate: `gates/SOLR-18010-review-pass-1.md`, GREEN at `571307400b6917ce458db989f29febb5cff8a600`. Logs `/workspace/gates/logs/vm3/solr-18010-review-pass-1/`. Changelog rc 0; tidy rc 0, empty status; Error Prone compile rc 0 (compileJava and compileTestJava executed, no skip warning); focused `SecurityConfHandlerTest` and `BasicAuthStandaloneTest`: 2 classes, 5 tests, 0 failures, 0 errors (fresh XML); `:solr:core:check -x test` rc 0.

Pass 1 result: GREEN at `571307400b6917ce458db989f29febb5cff8a600`.

## Pass 2 (vm3, 2026-10-21T06:53:48Z)

- Head reviewed: `571307400b6917ce458db989f29febb5cff8a600` (unchanged). Base `14c7aac0d1`.
- Test expectations against the ticket: `testConcurrentEditsToLocalSecurityJson` runs two edits (alice's and bob's role assignments) through a latching authorization plugin that forces them to interleave, and asserts both survive. That is the ticket's lost-update symptom. `testConcurrentPersistConfLeavesOneWholeDocument` runs two persists of different documents and asserts the file is one whole JSON document, not a mix. That is the corruption symptom. Both tests pin the mechanism.
- Ladder rung 2 (proof leg): the new assertions must fail on base, or be labelled a pin. Run in the next step, on base `14c7aac0d1` with the head's two test files overlaid. Result recorded below when it lands.
- Finding: none new beyond pass 1.
- Repairs in pass 2: none.
- Regate: `gates/SOLR-18010-review-pass-2.md`, GREEN at `571307400b6917ce458db989f29febb5cff8a600`. Logs `/workspace/gates/logs/vm3/solr-18010-review-pass-2/`. Changelog rc 0; tidy rc 0, empty status; Error Prone compile rc 0 (compileJava and compileTestJava executed, no skip warning); focused `SecurityConfHandlerTest` and `BasicAuthStandaloneTest`: 2 classes, 5 tests, 0 failures, 0 errors (fresh XML); `:solr:core:check -x test` rc 0.

Pass 2 result: GREEN at `571307400b6917ce458db989f29febb5cff8a600`.

### Pass 2 proof leg (vm3, 2026-10-21T07:04:59Z): fails on base as expected

- Base `14c7aac0d1`, production code at base, the head's `SecurityConfHandlerTest.java` and `BasicAuthStandaloneTest.java` overlaid. Not a synthetic base: the tests compile against base. Log `/workspace/gates/logs/vm3/solr-18010-review-pass-2-proof/proof-base.log`; counts `/workspace/gates/logs/vm3/solr-18010-review-pass-2-proof/count-proof.txt`: 5 tests, 3 failures, 0 errors.
- `SecurityConfHandlerTest.testConcurrentPersistConfLeavesOneWholeDocument` FAILED: `security.json is not a single JSON document` (JsonParseException at line 3). The corruption symptom.
- `SecurityConfHandlerTest.testConcurrentEditsToLocalSecurityJson` FAILED: `bob's role assignment was lost expected:<dev> but was:<null>`. The lost-update symptom.
- `BasicAuthStandaloneTest.testBasicAuth` FAILED: `authentication config should not contain internal metadata`. This pins the empty-key strip (pass 1, rung 1).
- Result: the new assertions discriminate. Exit status of the base run is nonzero (`BUILD FAILED`); the result file records a literal `$?` because the proof runner's echo was mis-quoted. The counts and messages are the evidence.


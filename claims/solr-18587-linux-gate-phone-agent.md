# Claim: SOLR-18587 Linux matched pair and gate (phone agent)

- Host: phone agent (Linux gate host, JDK 21, Gradle 9.7.0 via ~/workspace/tools/solr-gradle.sh, serialized through the test-queue flock).
- Status: DONE, FAIL.
- Unit: Linux proof for the existing Windows implementation slice. Assignment `assignments/solr-18587-coreadmin-unload-failed-reload-implementation-round-1.md` covers implementation (Windows claim, report `reports/solr-18587-coreadmin-windows-implementation-round-1.md`, PROOF PENDING); this claim covers only the Linux matched pair and gate legs the Windows claim explicitly does not cover. No other gate claim or receipt for SOLR-18587 exists on the board.
- Branch: `nick-boss-tech/solr:solr-18587-coreadmin-unload-failed-reload` at `13e997b3882c1af139b470a16a026e31c7bc837f` (verified by ls-remote at claim time). Base: RC3 `749712b71940c9f96d824c533f32f5c56dcd3924`. Two commits: the CoreContainer fix and a one-line test directory-factory change.
- Matched pair: `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure`, seed `787C6229591C6F1E`; base leg on the clean RC3 base (must FAIL with the recorded assertion), head leg at the branch head (must PASS), fresh log and JUnit XML per leg, archived separately.
- Gate legs at the head: changelog YAML parse, root tidy (clean tree required; a formatting-only tidy diff gets its own commit, push, and a re-gate per the standing rule), Error Prone compile, focused `CoreAdminHandlerTest` and `TestCoreContainer`, `:solr:core:check -x test`.
- Taken by the phone agent in the main chat on 2026-11-01 at Nick's direction to keep assignments moving; gate runner launched detached so the build keeps running between wakes, with a PENDING entry in the board-work state file for the wakes to verify.
- No PR, no Jira/GitHub post.
- Outcome 2026-11-01 20:56 MST: gate FAILED at the pre-fix proof leg; the
  test passed with production reverted to base, so no proof pair exists and
  the head tests and module check did not run. Receipt
  `receipts/SOLR-18587.md`. Branch unchanged.

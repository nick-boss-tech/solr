# Gate record: SOLR-18297 review pass 1, vm3 attempts (head 5cdc657470 and 7c07821070)

Status: ENDED (duplicate claim of unit 2; the unit's pass 1 gate record is gates/SOLR-18297-review-pass-1.md from vm5, DONE at 5cdc657470). This file records what vm3 found when it ran the same gate with a clean compile, so the two results can be reconciled.

- vm3 claim: claims/pool-newdev-critblock-review-round-1-solr-18297.md (posted 13:34Z, before vm5's 13:45Z claim of the same unit). Ended as a duplicate of vm5's claim; vm5's unit-2 report and gate are the record for pass 1.
- Attempt 1 (head 5cdc657470, incremental build): step 0 changelog rc=1 (base-level, same two files as base 498a09aea9); tidy rc=0, removed unused `oshai-logging` in `gradle/libs.versions.toml` (repair commit 7c07821070, pushed); compile rc=1 on `CoreReplicationAPITest.java:213` and `:217` (Error Prone `DirectInvocationOnMock`, `-Werror`). Logs: /workspace/gates/logs/vm3/solr-18297-review-pass-1-attempt1/.
- Attempt 2 (head 7c07821070, `cleanCompileJava cleanCompileTestJava` before compile): step 0 rc=1 (same); tidy rc=0, no change; compile rc=1 with the same two warnings reproduced on a full recompile. Test step ran after the failed compile (rc=0) and is not evidence. Logs: /workspace/gates/logs/vm3/solr-18297-review-pass-1/.
- Reconciliation: vm5's green compile (`compileTestJava`, offline, `StatusToolTest` rc=0) and vm3's clean compile disagree on the same head. The warning appears only when `CoreReplicationAPITest.java` is recompiled, so an incremental build can report green while a clean build fails `-Werror`. The owner or the gate host should decide whether the warning is accepted, suppressed in the test, or fixed in a separate ticket; that decision is outside this review round.
- Not repaired by vm3: the warning is in a test file outside the branch's diff.

## NEEDS-OWNER (rung 5)

Question: the Error Prone warning `DirectInvocationOnMock` at `solr/core/src/test/org/apache/solr/handler/admin/api/CoreReplicationAPITest.java:213` and `:217` fails `-Werror` on a clean compile at 5cdc657470 and 7c07821070. Which option?

- A. Separate ticket for the test-file warning; SOLR-18297 proceeds with the warning recorded as base-level. (Recommended: rung 4 evidence favours it; keeps the unit's diff scope clean; the file is untouched by the branch.)
- B. Suppress the warning in the test file on this branch. Widens the unit's diff scope to a file outside the branch.
- C. Change the build's `-Werror` setting for test compilation. Widens scope and changes the build for everyone.

Why rung 5: every option besides A either widens the approved scope (B, C) or commits Nick to a public JIRA post (A, if filed).

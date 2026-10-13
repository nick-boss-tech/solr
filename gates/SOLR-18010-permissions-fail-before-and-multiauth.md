# Gate job: SOLR-18010 permission fail-before and the multi-auth write path

- Branch: solr-18010-submit. Head: `2b5ad801c9e88807337f4f0a3fdd689126ad37d1` (confirmed by ls-remote before the review round).
- Status: OPEN, unclaimed. Claim before work: `claims/pool-<host>-gate-18010-permissions-fail-before.md`.
- Capability tags: gate. Intended host: any capable Linux host under the WORKFLOW.md claim rules. Windows never claims build work.
- Why: the strong review of PR #5118 (round 1, `reports/four-new-prs-strong-review-round-1.md`) found two gaps the body depends on.
  1. The draft's fail-before line says the permissions test fails with "0644 observed where 0640 was expected" when the permission step is reverted. The code the line names creates the replacement file with `Files.createTempFile`, which gives owner-only permissions on POSIX (0600 in the JDK we checked). The recorded value cannot be reproduced from the code as described. The line must not ship until this run shows what the test reports.
  2. `MultiAuthPluginTest` sends 19 standalone HTTP POSTs through the changed write path and the new edit lock. It is not in the focused set, so no run covers it.
- Job type: focused runs and a fail-before check. No code change. No fork push. No PR touched.

## Jobs

### G1 (blocking): the permissions test and the reverted permission step

- Class and method: `org.apache.solr.handler.admin.SecurityConfHandlerTest#testEditKeepsExistingSecurityJsonPermissions`.
- Seed: `18010C0FFEE18010`.
- Setup: head `2b5ad801c9e` with the permission block removed from `persistConf` in `SecurityConfHandlerLocal.java` (the state at `9e3ccafe39`). Restore the file after the run and confirm the tree is clean.
- Question: what permission set does the assertion report? Expected: `[OWNER_READ, OWNER_WRITE]` (0600). If the output is 0644, record which write produced it and confirm the reverted class is the one that was loaded.
- Also record from fresh JUnit XML, at the unreverted head: SecurityConfHandlerTest 4 of 4, BasicAuthStandaloneTest 1 of 1, V2SecurityAPIMappingTest 5 of 5, each with skipped = 0.

### G2 (blocking): the multi-auth write path

- Class: `MultiAuthPluginTest` (6 tests).
- Seed: `18010C0FFEE18010`.
- Head: `2b5ad801c9e`, no changes.
- Question: do all 6 pass with the atomic write and the edit lock (19 standalone HTTP POSTs)? Record counts, skipped, and failures from fresh JUnit XML. A failure is recorded as is and not relaunched without a note in this file.

### G3 (only after a code change lands for the permission-copy warning or the comment fix)

- The focused set plus `MultiAuthPluginTest` at the new head, with spotless and the fail-before for the permissions test at the prior head.

### G4a (manual, Linux only): symbolic link

- Setup: `SOLR_HOME/security.json` is a symbolic link to a file elsewhere. Run one `POST /admin/authorization` edit.
- Question: is the link's target updated? Expected: no. Record the result and the content of both files before and after.

### G4b (manual, Linux only): bind mount

- Setup: a single file bind-mounted over `SOLR_HOME/security.json`. Run one edit.
- Question: does the edit fail with "Device or resource busy" and leave the content unchanged? Expected: yes. Record the error text and the content before and after.

## Records

- Each job writes its counts, seed, head, log path and date into a new section in `receipts/SOLR-18010.md`, named "Review round 1 checks". G1 and G2 are DONE only when both have counts. G4a and G4b go into the Limits discussion through the owner, not into the PR text directly.
- The receipt's cited logs (`g18010-gate.log`, `g18010-premise.log`, `g18010-settle.log`) are not on this branch. Archive the logs from the gating host with the new section, or say they are not archived.

## Step checklist

- [ ] head confirmed by ls-remote
- [ ] G1: fail-before at the reverted permission step; reported permission set recorded
- [ ] G1: unreverted counts with skipped = 0 from fresh XML
- [ ] G2: MultiAuthPluginTest counts with skipped = 0
- [ ] G4a: symbolic link result recorded
- [ ] G4b: bind mount result recorded
- [ ] receipt section "Review round 1 checks" written; job DONE

## Claim and leg split (vm1, 2026-10-13T17:10Z)

- Claimed by vm1 for legs G1 and G2 only: claims/pool-vm1-gate-18010-permissions-fail-before.md.
- Runner: ~/workspace/tools/gate-strong-review-vm1-chain.sh (chained, serial on the test-queue flock). Log: ~/workspace/tools/g18010-perms.log. Worktree: ~/workspace/solr-worktrees/strong-r1-18010-perms. Fresh XML copies: ~/workspace/tools/g18010-perms-xml/.
- G3 is not part of this claim: it runs only after a code change lands for the permission-copy warning or the comment fix, and the SOLR-18010 review patch gate (gates/SOLR-18010-review-r1-patch.md, claimed by vm2) is where that change is gated first.
- G4a and G4b are manual checks; they are not part of this claim and stay open after G1 and G2 complete. This job is therefore marked DONE for G1 and G2 only when both have counts; the G4 lines of the checklist stay unchecked until a host runs them by hand.

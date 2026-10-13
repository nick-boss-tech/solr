# Gate job: SOLR-16437 re-gate at the moved head

- Branch: solr-16437-submit. Head: `a23f770e4b9587acbd14fcdbaae2047bd5c727c4` (fork tip). Previous gated head: `f72a9edd0a8b1aa1dded6af6c0c792fbb6179aa2`. Merge-base: `e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f`.
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: full hardening gate at the new head, then the receipt refresh and the draft re-point. Amended by the main side 2026-10-13: the first version of this job was a changelog check only; the head complaint stands until the receipt and the draft name the live head, so the full gate and both record fixes are part of this job.
- Why: the branch head moved by one commit past the gated head. That commit changes only the changelog title (`changelog/unreleased/SOLR-16437-addreplicaprop-validate-inputs.yml`); the code tree is otherwise identical to the gated head. The receipt (`receipts/SOLR-16437.md`) and the draft (`pr-drafts/solrcloud/SOLR-16437.md`) still name `f72a9edd0a8b` as the gated head, so neither record settles the live tip until this job lands.

## Gate spec

- Module: `:solr:core`. Build through the host's standard gate runner (vm1: `~/workspace/tools/solr-gradle.sh`, never `./gradlew`; gradle.properties copied into the worktree).
- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-16437-addreplicaprop-validate-inputs.yml` (the repo checker).
- Step 1: tidy; restore `gradle/libs.versions.toml` afterward; if tidy modified anything else, the run is GATE INVALID and stops.
- Step 2: Error Prone compile, `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: with `CollApiCmds.java` reverted to the merge-base production, run the focused class at seed `16437C0FFEE16437`. Expected, matching the gated head's proof: 25 tests, 1 skipped, exactly 1 failure, `testAddReplicaPropRejectsUnknownReplica`, failing by content (base returns success for an unknown replica, so no exception is thrown). Any other failure shape stops the job and is recorded, not relaunched.
- Step 4, focused head run: `org.apache.solr.cloud.CollectionsAPISolrJTest` at the head, seed `16437C0FFEE16437`, counted from fresh JUnit XML. Expected at the gated head: 25 tests, 1 skipped, 0 failures.
- Step 5: module check `:solr:core:check -x test`.
- A gate that executed tests and failed is never relaunched by the claimant; record the failure here and stop.

## On green

1. Refresh `receipts/SOLR-16437.md` with a new top section: the new head `a23f770e4b9`, the gate date and host, the log path, the step results, the proof-leg result, and the focused counts from the fresh JUnit XML. State that the delta from the previous gated head is the changelog title only.
2. Re-point `pr-drafts/solrcloud/SOLR-16437.md` to the new head everywhere a head is named, and settle the two suspects the moved-heads re-check named:
   - The Proof's printed short head: the draft printed `f72a9edd0a6f` where the gated head is `f72a9edd0a8b` (the last digit differs). Grep the draft for `f72a9edd0a6f` and for any other stale head string; correct what is found, and after the re-point no head string in the draft may name the old head as the current one (a historical mention, if kept, must read as history).
   - The verification date: the draft carried a 2026-10-10 date where the receipt's run at that head is dated 2026-10-12. Every verification date in the re-pointed draft follows the new gate's own date for the new head, and any date kept for the old head's runs must match the receipt (2026-10-12).
3. Mark this job DONE in the same push as the receipt and draft updates.

## Checklist

- [ ] head confirmed by ls-remote (`a23f770e4b9`); if the tip moved, stop and record the new tip
- [ ] diff scope confirmed (changelog title only vs `f72a9edd0a8b`)
- [ ] step 0 changelog parse
- [ ] step 1 tidy clean (GATE INVALID guard)
- [ ] step 2 Error Prone compile
- [ ] step 3 proof leg: exactly the one expected failure, by content
- [ ] step 4 focused run counted from fresh JUnit XML
- [ ] step 5 module check
- [ ] receipt refreshed at the new head
- [ ] draft re-pointed; both suspects settled
- [ ] job DONE

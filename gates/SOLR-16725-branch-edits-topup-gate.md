# Gate job: SOLR-16725 recorded branch edits and top-up gate at the moved head

- Branch: solr-16725-submit. Head: be1838ef8ccf21a21938a9669d418abeec66e29d (live tip, verified by `git ls-remote` on 2026-10-13; receipt `receipts/SOLR-16725.md`, GATE GREEN at this head).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed. Posted 2026-10-13 by the main side (openings census, `reports/openings-census-round-1.md`).
- Capability tags: gate.
- Job type: two recorded branch edits under the patch loop (VM2 and vm1 patch authority, Nick 2026-10-12: a gate job may direct the host to patch the branch), then the top-up gate at the moved head. The claiming host makes the commits and pushes them (the patching agent pushes), then gates the head it created.
- Why owed: the core admin round 2 response (`reports/core-admin-convergence-round-2-response.md`) records two owed branch edits for this ticket that have not landed: (1) a raw-request test comment, and (2) a ClusterStatus javadoc. The draft is CONVERGED on its text at the current head; the edits move the head, so a top-up gate at the moved head is the remaining step before opening. The exact text and file locations for both edits are in the round 1 and round 2 core admin records (`reports/core-admin-convergence-round-1-response.md`, `reports/core-admin-convergence-round-2-response.md`); take them from there, not from memory.
- Modules: `:solr:core` (the gate's home module per the receipt).
- Focused class at the head: `org.apache.solr.handler.admin.LocalFSCloudIncrementalBackupTest` (7 tests, 1 skipped, 0 failures at the current head per the receipt).
- Seed: `16725C0FFEE16725`.
- Base: the merge-base of the moved head with `upstream/main`, confirmed with `git merge-base` at claim time and recorded here. The premise on record: on base production the same class runs 7 tests with exactly 1 failure (`testCustomProperties`: expecting String but was Long for replicationFactor).
- Gate steps:
  1. Confirm the head with `git ls-remote`; record it here.
  2. Apply the two recorded edits on `solr-16725-submit`, one commit or two (author Nick Shanin, no trailers). Run tidy on the final tree after the last edit and before committing, so the pushed tree is the tree the gate sees. Push plainly to the fork and record the moved head here.
  3. Changelog YAML parse at the moved head.
  4. Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
  5. Focused run of `LocalFSCloudIncrementalBackupTest` with `-Ptests.seed=16725C0FFEE16725`; count from fresh JUnit XML. Expected: 7 tests, 1 skipped, 0 failures (the skip is the pre-existing one the receipt records).
  6. Module check: `:solr:core:check -x test`. After the gate, check `git status` on the gate tree (with `gradle/libs.versions.toml` restored): if tidy modified anything, the run is GATE INVALID; record it as such and stop.
- On completion: this file gets the moved head, the gate results and a DONE mark. The gating host refreshes `receipts/SOLR-16725.md` with the moved head, the counts, the seed and the date, and notes that the two round 2 edits have landed. The review side then re-points the draft's head citations in `pr-drafts/core-admin/SOLR-16725.md` in its next round.

## Step checklist

- [ ] head confirmed by ls-remote and recorded here
- [ ] both recorded edits applied, tidy run on the final tree, commits pushed; moved head recorded here
- [ ] changelog YAML parse at the moved head
- [ ] Error Prone compile passes
- [ ] focused LocalFSCloudIncrementalBackupTest run counted from fresh JUnit XML, seed 16725C0FFEE16725
- [ ] module check `:solr:core:check -x test` passes; post-gate tree check done (GATE INVALID guard)
- [ ] receipt refreshed in receipts/SOLR-16725.md; job DONE

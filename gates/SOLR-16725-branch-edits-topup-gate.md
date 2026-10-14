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

## Edits landed (review round 1 handoff, 2026-10-14)

- Moved head: `fc5c54770cb8c22c968c0ab6234ff0e1286190cb` (one commit, author Nick Shanin, no trailers), a plain fast-forward from `be1838ef8ccf21a21938a9669d418abeec66e29d` via `research/push-fork-ff.ps1`. Verified by `git ls-remote` after the push.
- Edit 1, raw-request test comment (`LocalFSCloudIncrementalBackupTest.java`, above the raw `GenericSolrRequest` at line 124).
  - Before: no comment.
  - After: `// Raw request so replicationFactor is sent; the SolrJ create helper sends nrtReplicas.`
- Edit 2, ClusterStatus javadoc (`ClusterStatus.java`, lines 337 to 340, the `normalizeCollectionCountTypes` javadoc; four lines before and after).
  - Before: "Reports the replica count properties as strings. Collections created through CREATE store them as strings and restored collections as numbers, so without this a client sees both types."
  - After: "Reports the replica count properties as strings. Some collections, such as restored ones, store them as numbers, so without this a client can see both types."
- Code basis is in the new job file, `gates/SOLR-16725-branch-edits-gate.md`, which is the top-up gate at this head. Tidy was not run on Windows; that gate's step 2 runs it.
- Wording source: the k6 report (`reports/core-admin-round-1-k6.md`, NOTE 3 and NOTE 4) and the round 1 answers (`material/core-admin-round-1-answers.md`) record the two edits as owed. The NOTE 4 replacement text is quoted there; the NOTE 3 text is drafted from the code.
- Correction to this job: the focused class line and gate step 5 name `org.apache.solr.handler.admin.LocalFSCloudIncrementalBackupTest`, but the test lives in package `org.apache.solr.cloud.api.collections`. The FQCN is `org.apache.solr.cloud.api.collections.LocalFSCloudIncrementalBackupTest`.

## Step checklist

- [ ] head confirmed by ls-remote and recorded here
- [ ] both recorded edits applied, tidy run on the final tree, commits pushed; moved head recorded here (edits and push done at `fc5c54770cb8`; tidy still owed, see the section below)
- [ ] changelog YAML parse at the moved head
- [ ] Error Prone compile passes
- [ ] focused LocalFSCloudIncrementalBackupTest run counted from fresh JUnit XML, seed 16725C0FFEE16725
- [ ] module check `:solr:core:check -x test` passes; post-gate tree check done (GATE INVALID guard)
- [ ] receipt refreshed in receipts/SOLR-16725.md; job DONE

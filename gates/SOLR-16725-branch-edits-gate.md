# Gate job: SOLR-16725 branch edits, top-up gate at the moved head

- Branch: solr-16725-submit. Head: fc5c54770cb8c22c968c0ab6234ff0e1286190cb (live tip, verified by `git ls-remote origin refs/heads/solr-16725-submit` on 2026-10-14 after the push).
- Previous head: be1838ef8ccf21a21938a9669d418abeec66e29d (receipt `receipts/SOLR-16725.md`, GATE GREEN at this head).
- Base: 14c7aac0d151402b00259e2fb9bf5eed7049ec5d (merge-base with `upstream/main`, unchanged by the edit commit; confirm with `git merge-base` at claim time).
- Status: DONE 2026-10-14T21:24ZZ by vm2 (gate green at fc5c54770cb8). Filed 2026-10-14 by the windows review coordinator (assignment `assignments/pool-vm2-open-items-handoff-round-1.md`, item 4). Windows runs no builds or tests; this job is for vm2 or any capable Linux host under the WORKFLOW.md claim rules.
- Capability tags: gate.
- Job type: top-up gate at the moved head. The two recorded branch edits are already committed and pushed; this job does not edit the branch unless the tidy step in gate step 2 changes a file (see step 2).
- Parent job: `gates/SOLR-16725-branch-edits-topup-gate.md`, which records the wording before and after. That file's step 2 (edits applied and pushed) is done at this head; its tidy run is not.

## Edits in this head

One commit `fc5c54770cb` (author Nick Shanin <nick.boss.us@gmail.com>, no trailers), pushed as a plain fast-forward from `be1838ef8cc` with `research/push-fork-ff.ps1`. Two files, 3 insertions and 2 deletions.

1. `solr/core/src/test/org/apache/solr/cloud/api/collections/LocalFSCloudIncrementalBackupTest.java`, new line 124, above the raw `GenericSolrRequest` that builds the source collection.
   - Before: no comment.
   - After: `// Raw request so replicationFactor is sent; the SolrJ create helper sends nrtReplicas.`
   - Code basis: `CollectionAdminRequest.Create.getParams` writes replica counts through `ReplicaCount.writeProps`, which uses the `nrtReplicas` property name (`CollectionAdminParams.NRT_REPLICAS`), and never writes `replicationFactor`. The test asserts that `replicationFactor` is present in the status, so the test sends it directly.
2. `solr/core/src/java/org/apache/solr/handler/admin/ClusterStatus.java`, the javadoc on `normalizeCollectionCountTypes` (lines 337 to 340; still four lines, so the helper stays at lines 341 to 353 and its call stays at line 377).
   - Before: "Reports the replica count properties as strings. Collections created through CREATE store them as strings and restored collections as numbers, so without this a client sees both types."
   - After: "Reports the replica count properties as strings. Some collections, such as restored ones, store them as numbers, so without this a client can see both types."
   - Code basis: `RestoreCmd` puts `replicationFactor` into the collection state as an `Integer` (`numReplicas.get(defaultType)`), so a restored collection's state holds a number. The helper changes only the status output, not the stored state, which matches the draft's Limits line.

Formatting was hand-checked against the 100-column limit and the javadoc fill. Tidy was not run on Windows (Gradle is not run on this host).

## Gate steps

1. Confirm the head with `git ls-remote origin refs/heads/solr-16725-submit`; record it here. Expected: `fc5c54770cb8c22c968c0ab6234ff0e1286190cb`.
2. Run `gradlew tidy` on the final tree. The pushed commit was not tidied, so this is the first tidy run. If tidy changes either file, do not amend or force push: commit the tidy output as one plain fast-forward commit on `solr-16725-submit` (author Nick Shanin, no trailers), record the new head here, and gate that head. Record the tidy result either way.
3. Changelog YAML parse at the head (`changelog/unreleased/SOLR-16725-clusterstatus-restored-count-types.yml`).
4. Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
5. Focused run: `:solr:core:test --tests "org.apache.solr.cloud.api.collections.LocalFSCloudIncrementalBackupTest" -Ptests.seed=16725C0FFEE16725`. Count from fresh JUnit XML. Expected: 7 tests, 1 skipped, 0 failures (the skip is the pre-existing one the receipt records).
   - Note: the parent job names the class as `org.apache.solr.handler.admin.LocalFSCloudIncrementalBackupTest`. The package is `org.apache.solr.cloud.api.collections`, as in the file path above. Use the FQCN in this job.
   - Expected on the restored-type check: the test passes at this head. On base, `testCustomProperties` fails (expects String, gets Long for `replicationFactor`), as the receipt records.
6. Module check: `:solr:core:check -x test`. After the gate, check `git status` on the gate tree (with `gradle/libs.versions.toml` restored). If tidy modified anything during the gate, record GATE INVALID and stop.

## Step checklist

- [ ] head confirmed by ls-remote and recorded here
- [ ] tidy run on the final tree; any tidy change landed as a fast-forward commit and the new head recorded here
- [ ] changelog YAML parse at the head
- [ ] Error Prone compile passes
- [ ] focused LocalFSCloudIncrementalBackupTest run counted from fresh JUnit XML, seed 16725C0FFEE16725
- [ ] module check `:solr:core:check -x test` passes; post-gate tree check done (GATE INVALID guard)
- [ ] receipt refreshed in `receipts/SOLR-16725.md` with the head, counts, seed and date; job DONE

## On completion

The gating host records the counts and the head here, refreshes `receipts/SOLR-16725.md`, and marks `gates/SOLR-16725-branch-edits-topup-gate.md` DONE. The review side then re-points the head citations in `pr-drafts/core-admin/SOLR-16725.md` in its next round. The draft is not edited in this round.


## Result (vm2, 2026-10-14T21:24ZZ)

Head fc5c54770cb8c22c968c0ab6234ff0e1286190cb. Tidy rc=0 (no changes; tree clean). Changelog YAML parse OK. Error Prone compile rc=0. LocalFSCloudIncrementalBackupTest, seed 16725C0FFEE16725: 7 tests, 0 failures, 0 errors, 1 skipped (the pre-existing skip). Module check rc=0. GATE END 2026-10-14T21:18:31Z. Log logs/vm2/queue-g16725-gate-20261014T205517Z.out.

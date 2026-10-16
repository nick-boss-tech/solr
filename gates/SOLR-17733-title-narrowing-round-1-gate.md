# Gate job: SOLR-17733 changelog title narrowing (round 1)

- Branch: solr-17733-submit. Gated head before this job: 636196b7954af6b34efad54bd0cda2330ef8c7a4 (ls-remote on origin, 2026-10-13: equal). Receipt: `receipts/SOLR-17733.md`.
- Posted by: windows (review coordinator, workstream W3 of `assignments/pool-category-closeout-overnight-round-1.md`), for Nick Shanin, 2026-10-13. Claim: `claims/pool-category-closeout-overnight-w3-round-1.md`.
- Capability tags: gate. Intended host: vm2 (Linux, Gradle toolchain). Windows never claims build work; this job was prepared with no build, test, Gradle or gate run.
Status: DONE for the gate (steps 1 to 5 below). Gate GREEN at cf1b283272641e8e4944cf5a80773cb336be8e0c; receipt refreshed in `receipts/SOLR-17733.md`. Step 6 and the W3 claim line in step 7 stay open for the main agent. Claim: `claims/pool-vm2-gate-backlog-17733-narrowing-round-1.md`, vm2, 2026-10-16.
- Patch: `gates/SOLR-17733-title-narrowing-round-1.patch`. One file, one hunk: the `title:` line of `changelog/unreleased/SOLR-17733.yml`. It applies cleanly to 636196b7954a (checked on the review host with `git apply --cached --check` against a temporary index file outside the repo; no ref, index or worktree of the repo changed).
- Title before: `DELETE /cluster/filestore/files/{file} no longer fails with "The path exist ZK, delete and retry"; the file store entry in ZooKeeper is removed together with the file`
- Title after: `DELETE /cluster/filestore/files/{file} without localDelete=true no longer fails with "The path exist ZK, delete and retry"; the file store entry in ZooKeeper is removed together with the file`
- Why the narrowing is needed (read at 636196b7954a): `ClusterFileStoreApis.java` lines 98 to 99 carry the `localDelete` query parameter. `ClusterFileStore.doDelete` (lines 308 to 314) sends `localDelete=true` to `doLocalDelete`, which calls `DistribFileStore.deleteLocal`; its `checkInZk` (line 536) throws the 400 whenever the ZooKeeper entry exists. `TestDistribFileStore` (lines 206 to 213) asserts that a `localDelete=true` DELETE on a ZooKeeper-registered file returns 400. A DELETE without `localDelete` runs `DistribFileStore.delete`, which removes the ZooKeeper entry first (lines 487 to 490), so the second clause of the title still holds for that path.
- Landing (not done by this job and not done by the review host): the patching agent applies the patch to `solr-17733-submit` as one commit on top of 636196b7954a and pushes it as a plain fast-forward. The new head is written into this file when it lands.
- Landed head (windows review round 1, 2026-10-14): `cf1b283272641e8e4944cf5a80773cb336be8e0c`, verified by `git ls-remote origin refs/heads/solr-17733-submit`. Its parent is `636196b7954af6b34efad54bd0cda2330ef8c7a4`, and it is a single commit ("SOLR-17733: narrow the changelog title to the DELETE without localDelete", author Nick Shanin). `git diff --stat 636196b7..cf1b2832` shows only `changelog/unreleased/SOLR-17733.yml`, and its added and removed lines match `gates/SOLR-17733-title-narrowing-round-1.patch` line for line. The patch was already on the live tip when checked, so this round pushed nothing.

## Steps, in order, at the landed head

1. [x] Landed head: record the new SHA (ls-remote) and the parent 636196b7954a. `git diff --stat 636196b7954a..<new>` must show only `changelog/unreleased/SOLR-17733.yml`. Stop and record here if any other file differs. Done: landed head `cf1b283272641e`, parent `636196b7954a`, one file only (see the Landing section above).
2. [ ] Changelog YAML parse of `changelog/unreleased/SOLR-17733.yml` with a real YAML parser (no parser runs on the review host). Pass means the file parses and the `title` key equals the replacement text above, byte for byte.
3. [ ] Focused re-run: `org.apache.solr.filestore.TestDistribFileStore`, method `testFileStoreManagement`, module `:solr:core`, at the landed head. Count from fresh JUnit XML. Expect 1 test, 0 failures, 0 errors. Record the UTC run date, the suite timestamp and the seed.
4. [ ] Module check: `:solr:core:check -x test` and `:solr:api:check -x test` (`ClusterFileStoreApis.java` lives in `solr/api`). Both rc 0.
5. [ ] Receipt refresh: `receipts/SOLR-17733.md` gets a dated line for the landed head with the results of steps 2 to 4. The dated run at 636196b7954a stays as recorded.
6. [ ] Draft head citations: `pr-drafts/solrcloud/SOLR-17733.md` cites 636196b7954a at lines 15 to 17, 23, 35 and 38; re-point them to the landed head. Its line 9 links to the merge-base 56ec140 and stays. Its line 16 already says the `localDelete=true` path still refuses, which matches the narrowed title; no other draft wording changes for the title.
7. [ ] Job DONE; the W3 claim line for SOLR-17733 DONE.

No tidy or Error Prone step: the patch changes no Java file. If the landed commit touches any Java file, stop and record it here.

## Decisions (decision default, recorded here)

- D1: the narrowed title uses the scope in the slate report, "without localDelete=true", and keeps the second clause, which the code supports for the cluster DELETE (lines 487 to 490 above).
- D2: the module check covers both modules the ticket spans (`:solr:core` and `:solr:api`), not only `:solr:core`, and a focused re-run is kept at the landed head although the patch changes no Java. This is the harder option; the cost on vm2 is small.

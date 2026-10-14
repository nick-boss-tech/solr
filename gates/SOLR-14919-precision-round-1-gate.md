# Gate job: SOLR-14919 changelog title and test comment precision (round 1)

- Branch: solr-14919-submit. Gated head before this job: 8f1f4ef91e798f736eb91c9625223a654b59794a (ls-remote on origin, 2026-10-13: equal). Prior gate: GATE GREEN at 8f1f4ef91e (run 3, GATE END 2026-10-13T16:07:45Z) and the focused count at 8f1f4ef91e (COUNT END 2026-10-13T18:13:49Z), both in `receipts/SOLR-14919.md`.
- Posted by: windows (review coordinator, workstream W3 of `assignments/pool-category-closeout-overnight-round-1.md`), for Nick Shanin, 2026-10-13. Claim: `claims/pool-category-closeout-overnight-w3-round-1.md`.
- Capability tags: gate. Intended host: vm2. Windows never claims build work; this job was prepared with no build, test, Gradle or gate run.
- Status: OPEN. Nothing ticked. The moved head does not exist yet.
- Patch: `gates/SOLR-14919-precision-round-1.patch`. Two files: (a) the `title:` line of `changelog/unreleased/SOLR-14919.yml`; (b) the comment at lines 63 to 65 of `solr/core/src/test/org/apache/solr/update/processor/IgnoreCommitOptimizeUpdateProcessorFactoryTest.java`, reflowed to lines 63 to 66. It applies cleanly to 8f1f4ef91e (checked on the review host with `git apply --cached --check` against a temporary index file outside the repo; no repo state changed). No production file changes.
- Title before: `IgnoreCommitOptimizeUpdateProcessor no longer rejects commits that a leader forwards with the internal endpoint values leaders or replicas; those commits failed with a 400 before`
- Title after: `IgnoreCommitOptimizeUpdateProcessor no longer rejects commits that a node forwards with the internal endpoint values leaders or replicas; those commits failed with a 400 before`
- Test comment before (lines 64 and 65):
  `    // "leaders" on a leader's fan-out to other shard leaders, and "replicas" on the hop to the`
  `    // leader's own replicas. Those internal commits must pass, and must not fail boolean parsing.`
- Test comment after (lines 64 to 66):
  `    // "leaders" on the fan-out from the receiving node to the shard leaders, and "replicas" on the`
  `    // hop from a leader to its own replicas. Those internal commits must pass, and must not fail`
  `    // boolean parsing.`
- Why (read at 8f1f4ef91e): `DistributedZkUpdateProcessor.java` lines 215 to 224 set `leaders` whenever the incoming commit carries no `commit_end_point`, with no leader check. The leader-only branch (`isLeader`, lines 226 to 235) sets `replicas` for the hop to the leader's own replicas. So any receiving node sets `leaders`, and "a leader's fan-out" is wrong.
- Shard leaders: `getCollectionUrls` returns each shard's leader. The receiving node removes only itself (lines 197 to 200), so for a replica receiver its own shard leader stays in the list. "The shard leaders" is therefore the accurate phrase, and "the other shard leaders" is not.
- Superseded text: `reports/solrcloud-round-1-p4.md` note 5 proposes a replacement comment that repeats "a leader's fan-out" and says `leaders` is set "only on a leader's fan-out". Do not paste that text; the comment above replaces it.
- Out of scope, not changed here: item (c) in the slate report, the draft headline's scope against `ignoreOptimizeOnly=true` (a plain commit returns before the parse at lines 135 to 140 of `IgnoreCommitOptimizeUpdateProcessorFactory.java`). That is the owner's call.

## Steps, in order, at the landed head

0. [ ] Landed head: the patching agent applies the patch to `solr-14919-submit` as one commit on top of 8f1f4ef91e and pushes it as a plain fast-forward (no history rewrite). Record the new SHA (ls-remote) and the parent 8f1f4ef91e. `git diff --stat 8f1f4ef91e..<new>` must show only the two files above. Stop and record here if anything else differs.
1. [ ] Changelog YAML parse of `changelog/unreleased/SOLR-14919.yml` with a real YAML parser: rc 0, and the parsed `title` equals the replacement text above.
2. [ ] Tidy check with the GATE INVALID guard: run the `:solr:core` tidy task as the vm2 runner for the 16:07Z gate did; afterward the tree must be clean (restore `gradle/libs.versions.toml` first if the run modified it). A tidy-modified tree is GATE INVALID. rc 0.
3. [ ] Error Prone compile: `:solr:core:compileTestJava` with `-Pvalidation.errorprone=true`. rc 0.
4. [ ] Proof leg (re-run, see D4): with `IgnoreCommitOptimizeUpdateProcessorFactory.java` reverted to the merge-base b5c71bc5573c (that file only), the focused class must fail with `invalid boolean value: leaders`. Record the exit code and the marker hits. Restore the file; the tree must be clean.
5. [ ] Focused re-run at the landed head: `org.apache.solr.update.processor.IgnoreCommitOptimizeUpdateProcessorFactoryTest` (`:solr:core:test`), counted from fresh JUnit XML. Expect 1 test, 0 failures, 0 errors, 0 skipped. Record the UTC run dates, the suite timestamp and the seed.
6. [ ] Module check: `:solr:core:check -x test`, rc 0.
7. [ ] Receipt refresh: `receipts/SOLR-14919.md` gets a top section for the landed head with the result of each step, the counts, the seed, and one line stating that the delta from 8f1f4ef91e is the changelog title and the test comment only.
8. [ ] Draft head citations: `pr-drafts/solrcloud/SOLR-14919.md` cites 8f1f4ef91e at its lines 16, 22 and 32; re-point them to the landed head. The draft does not quote the title or the comment. Its headline stays as it is (item c).
9. [ ] Job DONE; the W3 claim line for SOLR-14919 DONE.

## Decisions (decision default, recorded here)

- D1: the title wording is "a node forwards". It is the smallest change that makes the clause accurate for both hops: the receiving node's `leaders` hop and the leader's `replicas` hop.
- D2: the comment says "the shard leaders", not "the other shard leaders", because a replica receiver keeps its own shard leader in the list (see above).
- D3: the changelog YAML parse (step 1) is added because the title changes, and the tidy and Error Prone steps are kept because a Java file's comment changes. The focused run at the moved head is the step the slate report names.
- D4: the proof leg (step 4) is re-run although the production code is unchanged. This is the harder option; the receipt's proof numbers are for 8f1f4ef91e and the test file has changed since.
- D5: item (c) is not changed by this job. It stays with the owner.

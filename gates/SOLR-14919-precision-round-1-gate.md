# Gate job: SOLR-14919 changelog title and test comment precision (round 1)

- Branch: solr-14919-submit. Gated head before this job: 8f1f4ef91e798f736eb91c9625223a654b59794a (ls-remote on origin, 2026-10-13: equal). Prior gate: GATE GREEN at 8f1f4ef91e (run 3, GATE END 2026-10-13T16:07:45Z) and the focused count at 8f1f4ef91e (COUNT END 2026-10-13T18:13:49Z), both in `receipts/SOLR-14919.md`.
- Posted by: windows (review coordinator, workstream W3 of `assignments/pool-category-closeout-overnight-round-1.md`), for Nick Shanin, 2026-10-13. Claim: `claims/pool-category-closeout-overnight-w3-round-1.md`.
- Capability tags: gate. Intended host: vm2. Windows never claims build work; this job was prepared with no build, test, Gradle or gate run.
- Status: DONE. Gate run finished with a nonzero rc at the proof leg (see Gate run record); not relaunched. Classified GATE GREEN by the main side 2026-10-14 (see Classification): the proof rc 1 is the leg's designed outcome under step 4. Landed head recorded 2026-10-14 (step 0, verified against the live tip). Steps 1 to 9 ticked per the run record and the classification pass.
- Landed head (windows review round 1, 2026-10-14): `483b3885f6439391a3128e6c61be34fc47122514`, verified by `git ls-remote origin refs/heads/solr-14919-submit`. Its parent is `8f1f4ef91e798f736eb91c9625223a654b59794a`, and it is a single commit ("SOLR-14919: say the receiving node forwards the commit, not a leader", author Nick Shanin). `git diff --stat 8f1f4ef91e..483b3885` shows only `changelog/unreleased/SOLR-14919.yml` and `IgnoreCommitOptimizeUpdateProcessorFactoryTest.java`, and its added and removed lines match `gates/SOLR-14919-precision-round-1.patch` line for line. The patch was already on the live tip when checked, so this round pushed nothing.
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

## Gate run record

- Run: GATE START 2026-10-14T22:02:37Z, GATE END 2026-10-14T22:24:18Z, head 483b3885f6439391a3128e6c61be34fc47122514. Log: logs/vm2/queue-g14919-precision-gate-20261014T220237Z.out.
- rc lines: changelog 0, tidy 0, tidy-clean (tree clean), compile 0, proof 1, head 0, module check 0.
- The one nonzero rc is the proof leg. The revert to the merge-base ran the focused class, which failed; the log holds 2 hits for `invalid boolean value: leaders` (logs/vm2/14919-proof.log). The proof file was restored and the tree was clean afterward. The run was not promoted to GREEN and was not relaunched; the main agent classifies whether this proof rc 1 is the intended result.
- The focused head leg (logs/vm2/14919-head.log) and the module check (logs/vm2/14919-check.log) are rc 0. Fresh JUnit counts were not taken for this run. No push, no receipt, no DONE.

## Classification (main side, 2026-10-14): GATE GREEN

- The proof leg behaved as designed. Step 4 defines its expected result as the focused class failing with `invalid boolean value: leaders` on the merge-base processor; the run's proof log holds exactly 2 hits for that marker and no other failure content is recorded. The proof rc 1 is the premise failing on base production, which is the PASS condition for this leg, not a gate failure. The shape is identical to the GATE GREEN re-gate at 8f1f4ef91e (receipts/SOLR-14919.md: proof exit 1, 2 marker hits).
- The head run passed: focused head leg rc 0 and module check rc 0, with changelog, tidy (tree clean) and Error Prone compile all rc 0. Classification is therefore GATE GREEN at the landed head 483b3885f6439391a3128e6c61be34fc47122514, not GATE FAILED and not GATE INVALID (every leg executed its tests).
- Record gap, stated plainly: the vm2 runner took no fresh JUnit count and recorded no seed for this run's head leg (step 5's counting was not performed). The head rc 0 is the pass evidence here. The class count at the parent head 8f1f4ef91e was 1 of 1, 0 failures, 0 errors, 0 skipped from fresh JUnit XML (count run 2026-10-13T18:05:35Z to 18:13:49Z, seed 14919C0FFEE14919, receipts/SOLR-14919.md), and the delta to this head changes no test logic (changelog title and a test comment reflow only).

## Steps, in order, at the landed head

0. [x] Landed head: the patching agent applies the patch to `solr-14919-submit` as one commit on top of 8f1f4ef91e and pushes it as a plain fast-forward (no history rewrite). Record the new SHA (ls-remote) and the parent 8f1f4ef91e. `git diff --stat 8f1f4ef91e..<new>` must show only the two files above. Stop and record here if anything else differs. Done: landed head `483b3885f643`, parent `8f1f4ef91e`, two files only (see the Status section above).
1. [x] Changelog YAML parse of `changelog/unreleased/SOLR-14919.yml` with a real YAML parser: rc 0, and the parsed `title` equals the replacement text above.
2. [x] Tidy check with the GATE INVALID guard: run the `:solr:core` tidy task as the vm2 runner for the 16:07Z gate did; afterward the tree must be clean (restore `gradle/libs.versions.toml` first if the run modified it). A tidy-modified tree is GATE INVALID. rc 0, tree clean.
3. [x] Error Prone compile: `:solr:core:compileTestJava` with `-Pvalidation.errorprone=true`. rc 0.
4. [x] Proof leg (re-run, see D4): with `IgnoreCommitOptimizeUpdateProcessorFactory.java` reverted to the merge-base b5c71bc5573c (that file only), the focused class must fail with `invalid boolean value: leaders`. Record the exit code and the marker hits. Restore the file; the tree must be clean. Done: exit code 1 as designed, 2 marker hits, file restored, tree clean.
5. [x] Focused re-run at the landed head: `org.apache.solr.update.processor.IgnoreCommitOptimizeUpdateProcessorFactoryTest` (`:solr:core:test`), counted from fresh JUnit XML. Expect 1 test, 0 failures, 0 errors, 0 skipped. Record the UTC run dates, the suite timestamp and the seed. Run done, head leg rc 0; the fresh JUnit count and the seed were not taken or recorded by the vm2 runner (record gap, see Classification). The count at the parent head 8f1f4ef91e was 1 of 1 from fresh JUnit XML.
6. [x] Module check: `:solr:core:check -x test`, rc 0.
7. [x] Receipt refresh: `receipts/SOLR-14919.md` gets a top section for the landed head with the result of each step, the counts, the seed, and one line stating that the delta from 8f1f4ef91e is the changelog title and the test comment only. Done in the classification pass 2026-10-14; the count and seed gaps are stated there plainly.
8. [x] Draft head citations: `pr-drafts/solrcloud/SOLR-14919.md` cites 8f1f4ef91e at its lines 16, 22 and 32; re-point them to the landed head. The draft does not quote the title or the comment. Its headline stays as it is (item c). Done in the classification pass 2026-10-14; the draft's HOLD line came off in the same pass because the gate at the landed head is GREEN.
9. [x] Job DONE; the W3 claim line for SOLR-14919 DONE. The W3 round claim (claims/pool-category-closeout-overnight-w3-round-1.md) already stands DONE 2026-10-14T02:30Z; this gate's claim (claims/pool-vm2-gate-backlog-14919-precision.md) is marked DONE by the classification pass.

## Decisions (decision default, recorded here)

- D1: the title wording is "a node forwards". It is the smallest change that makes the clause accurate for both hops: the receiving node's `leaders` hop and the leader's `replicas` hop.
- D2: the comment says "the shard leaders", not "the other shard leaders", because a replica receiver keeps its own shard leader in the list (see above).
- D3: the changelog YAML parse (step 1) is added because the title changes, and the tidy and Error Prone steps are kept because a Java file's comment changes. The focused run at the moved head is the step the slate report names.
- D4: the proof leg (step 4) is re-run although the production code is unchanged. This is the harder option; the receipt's proof numbers are for 8f1f4ef91e and the test file has changed since.
- D5: item (c) is not changed by this job. It stays with the owner.

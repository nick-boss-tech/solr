# Search components draft fidelity, slice s6

Assignment: draft fidelity review of the search-components drafts SOLR-17051, SOLR-17155, SOLR-17748, SOLR-17791 and SOLR-17976 in `pr-drafts/search-components/`. Brief: `scratchpad/brief-draft-fidelity.md`. Worktree HEAD is d627304e96b; the brief's claim commit e84522fa5bc is an ancestor of it. Read only. Nothing built, tested, gated, committed, pushed or posted. No gh calls.

Head check (`git ls-remote origin refs/heads/<branch>` in the worktree): all five live fork tips match the head each draft names. Each head SHA resolves as a commit, and each changelog fragment exists there as a blob.

| Branch | Live tip (ls-remote) | Draft head |
|---|---|---|
| solr-17051-submit | 148544e9ed54a7c6dd6714197d3bf74b06fff1ca | 148544e9ed54 |
| solr-17155-submit | 1413237f7a75b8b3da946401d12bcd0e29917581 | 1413237f7a75 |
| solr-17748-submit | dfacaf347668e43a433baef650268b281649c6fc | dfacaf347668 |
| solr-17791-submit | a39c1c9737667e4ab935a9c498648bdb9f4507c1 | a39c1c97376 (prefix) |
| solr-17976-submit | 56ea43c448ed4fea75131d34c390d11233bab5cb | 56ea43c448ed |

Sources: receipts `receipts/SOLR-<n>.md` (all five present). Round reports: 17051 in `reports/search-components-1.md` (row line 22, owner decision 6) and `reports/search-components-1-f5.md` (FIX 2, NOTE 4, NOTE 12); 17155 in `reports/search-components-4.md` (row line 31) and `reports/search-components-4-s2.md` (findings 16 to 19); 17748 in `reports/search-components-2.md` (row line 22) and `reports/search-components-2-h3.md` (findings 1, 6 to 8); 17791 in `reports/search-components-4.md` (row line 33) and `reports/search-components-4-s4.md` (findings 2 to 4, 15 to 17); 17976 in `reports/search-components-2.md` (row line 23) and `reports/search-components-2-h3.md` (findings 1, 4, 9 to 11). `reports/search-components-3-w*.md` has no section for these tickets. `material/` has no file that names these ticket numbers. Jira text was read from `research/jira-context/SOLR-<n>.json`, not live Jira.

Formula used: `pr-formula.md` (section 4 choice bar; "Rules for filling it in", changelog line is a link at the head SHA).

No draft has a separate title line. The changelog title in each `changelog/unreleased/SOLR-<n>.yml` at head is the PR title source. Title check: nothing to compare in the draft, so no title item was counted.

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-17051 | 148544e9ed54 (live) | DRIFT (2 items) |
| SOLR-17155 | 1413237f7a75 (live) | CONSISTENT (hold note) |
| SOLR-17748 | dfacaf347668 (live) | DRIFT (4 items) |
| SOLR-17791 | a39c1c973766 (live) | CONSISTENT (hold notes) |
| SOLR-17976 | 56ea43c448ed (live) | DRIFT (1 item) |

## SOLR-17051

Verdict: DRIFT (2 items).

Checked and consistent: head; changelog link at head (file exists, title matches the code); code citations (base symptom at `FacetFieldProcessor.java` L470 and `FacetFieldMerger.java` L139-L141 at merge base 14c7aac0d151; change at head `FacetFieldProcessor.java` L534-L542 and `FacetFieldMerger.java` L139-L145; test at head `TestJsonFacets.java` L2566-L2585); Proof counts (TestJsonFacets 30 of 30; TestJsonFacetRefinement 10 tests, 2 skipped; verified 2026-10-04 matches receipt line 3 "hardened and pushed 2026-10-04"); "497" matches the Jira packet; choice options 1 to 3 match f5 owner decision 1; Limits match f5 NOTE 4 and owner decision 3.

1. Draft says: "The `mincount` 5 check expects no `missing` key. The base code always returns that key, so this is the check that should fail on the base code. [OWNER: the failure text from a base run is not on disk. Add the observed failure here, or delete this sentence before posting.]"
- Evidence: `receipts/SOLR-17051.md` line 7 gives a fail-before verdict of PASS and says "the record gives no failure shape for this proof". No `results/SOLR-17051.failbefore.json` is on disk. `reports/search-components-1-f5.md` FIX 2 (lines 11 to 13) says to write only the first sentence and not to say the check fails on the base code. The bracket is owner text inside public text.
- Replacement: "The `mincount` 5 check expects no `missing` key."

2. Draft says: "The `mincount` 4 check passes on the base code too. It guards the case that must not change."
- Evidence: `receipts/SOLR-17051.md` line 7 has no per-check base result. No receipt or round line says the mincount 4 check passes on the base code. The head test (`TestJsonFacets.java` L2573 to L2574) expects `missing:{count:4}`, which is what the test source says.
- Replacement: "The `mincount` 4 check expects the `missing` key to stay, with a count of 4."

Optional notes, not blocking:
- The "Owner notes, not for posting" block (draft lines 56 to 63) uses "gate record", "gate logs", "inventory" and "decision entry". It is labelled not for posting. Delete it before posting; it was not counted as DRIFT.
- The Limits figures "two shards with 400 documents each and a mincount of 500" are a hypothetical follow-up fixture, as in f5 owner decision 3. They are not a result.
- Option 3 is for owner confirmation (f5 owner decision 1). Hold.
- Commit a17520f98b1 has the subject "record contract decision" (f5 NOTE 5). Squash it before the PR; this is history, not draft text.
- Plain language: "screen", "missing bucket" and "sub-facets" are compressed.

## SOLR-17155

Verdict: CONSISTENT (hold note).

Checked and consistent: head; changelog link at head (file exists; title matches the code); `retrieveUniqueKey` at head L343-L366 (stored read at L352, `value.toString()` at L365, SERVER_ERROR throw at L357 to L364 names the field and the document); the test is one method (`TopGroupsResultTransformerTest`) with a docValues-only id and three documents, asserting ids 1, 2 and 3; Proof count 1 of 1 and "2026-10-05" match the receipt; the base NullPointerException statement matches the receipt and s2 finding 17; the choice has a live alternative (reject with a clear error), which matches s2 finding 18.

Hold notes, not DRIFT:
- The choice section still holds the owner placeholder "[Owner to confirm the options before this is opened.]" (draft line 21). Round s2 finding 18 and owner decision 6 leave the options to the owner. Replace it before posting, and add the options considered and the cost of the alternative (`pr-formula.md` section 4).
- No GitHub run is cited at this head. That is correct: the corroboration in the receipt covers 82936c0b705 only (s2 finding 19).
- Landing order (17155 before 14381, from the round 4 row) is not in the draft. It is an owner note, not PR text.
- Limits (draft line 33) uses first person "which I have not checked". The other four drafts do not. Optional voice change.
- Plain language: "unique key", "docValues" and "second phase" are compressed.

## SOLR-17748

Verdict: DRIFT (4 items).

Checked and consistent: head; code citations at head (`QueryComponent.java` L1043, L1094-L1098, L1487-L1492, L1496-L1498); the cause-less route is real (`LBSolrClient.java` throws without a cause at L940 to L941, head); the base failures are real (base `QueryComponent.java` L1483 and L1484 dereference a null `shardInfo` or entry, and L1487 to L1490 dereference a null cause); Proof counts match the receipt (TestShardsInfoErrorRecording 4 of 4, three failing on base; TestShardsInfoResponse 1 test); dates match the receipt (2026-10-04); the choice has a live alternative (the smaller empty-name skip), matching h3 owner decision 1.

1. Draft says: "`TestShardsInfoResponse` gains a single-pass query against a two-node cluster with two failed shards."
- Evidence: `TestShardsInfoResponse.java` at head line 40 is `configureCluster(3)`, line 47 creates 3 shards with 1 replica, and lines 56 to 57 stop 2 nodes. The cluster has three nodes and two shards are down.
- Replacement: "`TestShardsInfoResponse` gains a single-pass query against a three-node cluster with two failed shards."

2. Draft says: "No test covers an error with no cause on a shard that already has an entry."
- Evidence: `TestShardsInfoErrorRecording.java` lines 92 to 108 (`testExistingErrorEntryIsKept`) pass `new SolrServerException("later")`, which has no cause, to shard1, which already has an entry. The sentence is therefore wrong as written. The route with no test (h3 finding 7) is an existing entry with no error, followed by an error with no cause.
- Replacement: "No test covers a shard whose existing entry has no error when an error with no cause arrives."

3. Draft says: "- Behavior change: a one-pass query with `shards.info` and a failed shard no longer fails with a 500 error. It returns the results, lists the failed shard in `shards.info`, and keeps the partial-results flag that the id merge sets."
- Evidence: the receipt PR note says "single-pass tolerant queries with shards.info and a dead shard now return ... instead of 500". The changelog title at head (line 1) says "single-pass ... with shards.info and shards.tolerant". `HttpShardHandler.java` at head L521 to L539 throws SERVICE_UNAVAILABLE before the field step when `shards.tolerant` is not set and a slice has no replica, so the draft's wording is too broad. The cause-less route (`QueryComponent.java` L1043, L1496 to L1498, and base L1487 to L1490) also changes behavior for any query shape, and the draft does not state it (h3 finding 6 and 7).
- Replacement: "- Behavior change: a single-pass query with `shards.info` and `shards.tolerant=true` no longer fails with a 500 error when a shard has no live replica. It returns the results, lists the failed shard in `shards.info`, and keeps the partial-results flag that the id merge sets. Without `shards.tolerant=true`, a shard with no live replica still fails the request with a 503 error before this step, as before. A shard error with no cause is now listed in `shards.info` as itself. Before, it raised a NullPointerException."

4. Draft says: "Changelog: `changelog/unreleased/SOLR-17748.yml`"
- Evidence: `pr-formula.md` lines 170 to 175 require the changelog line to be a link to the fragment at the head SHA. The file exists at dfacaf347668 (blob).
- Replacement: "Changelog: [changelog/unreleased/SOLR-17748.yml](https://github.com/nick-boss-tech/solr/blob/dfacaf347668e43a433baef650268b281649c6fc/changelog/unreleased/SOLR-17748.yml)"

Optional notes, not blocking:
- The draft uses both "one-pass" (lines 11, 21, 39) and "single-pass" (line 27). The changelog uses "single-pass". Pick one term.
- The changelog title is narrower than the draft (no cause-less route). Owner may want the title to match (h3 owner decision 1 and 2).
- Hold: the commit subjects "add testing handoff" (07d3de7c170) and "reword changelog ..., drop testing handoff" (head dfacaf347668) (h3 finding 1). A rewrite moves the head and needs a fresh gate. Not in the draft text.
- Hold: keep or drop the entry-creation branch, and which NPE route the reporter hit (h3 owner decisions 1 and 2). The choice section states the first.
- Plain language: "field step", "id merge", "unknown_shard_N" and "partial-results flag" are internal names.

## SOLR-17791

Verdict: CONSISTENT (hold notes).

Checked and consistent: head (the Proof header uses the 11-character prefix of the head SHA, which matches the full SHA in the links); changelog link at head (file exists; title matches the behavior); `RestManager.java` L341 to L346 (PUT and POST pass the child id); `ManagedFeatureStore.java` L162 to L198 (`withDefaultStore`, explicit `store` kept); upgrade note `major-changes-in-solr-11.adoc` L45 to L51 and guide `learning-to-rank.adoc` L294 match the draft; Proof counts: TestManagedFeatureStoreRest 2 of 2 (both new), TestManagedFeatureStore 10 with 4 new (head L115 to L160), TestModelManager 3 with no diff on the branch; "2026-10-05" matches the receipt; both API options are named, which matches s4 owner decision 2.

Hold notes, not DRIFT (round s4 holds the draft for these; the draft does not need a text change until they are decided):
- Head defect (s4 finding 2): an empty child id from a trailing slash creates a store named "", because `withDefaultStore` checks only `childId == null` (`ManagedFeatureStore.java` L179 at head). The draft does not mention it. Owner approval, a fix, a new focused proof and a rewritten draft are needed first. Write the Limits sentence after the fix lands.
- The branch conflicts with main in `major-changes-in-solr-11.adoc` (s4 finding 3).
- Commit subjects with "handoff" (345cf76949e, a39c1c97376) (s4 finding 4).
- The API-shape call is the owner's (s4 owner decision 2).
- Changelog type `fixed` or `changed` (s4 finding 17). Optional.
- Plain language: "overloads", "child id" and "named-store check" (Proof text, line 34) are internal test terms.
- The Proof sentence "The tidy check and the Error Prone compile passed ..." reports outcomes, which `pr-formula.md` allows. Optional to shorten.

## SOLR-17976

Verdict: DRIFT (1 item).

Checked and consistent: head; code citations at head (`QueryComponent.java` L971 to L989 and L1232; `CombinedQueryComponent.java` L323 and L424; `ShardFieldSortedHitQueue.java` L104 to L118; `ShardDoc.java` L30 to L33); test `TestShardTieBreakCluster.java` L150 to L159 (two shards, two nodes); Proof counts (TestShardTieBreakCluster 1 of 1; TestShardTieBreak 2 of 2; CombinedQueryComponentTest 6 test methods, JUnit 3 style; CombinedQuerySolrCloudTest 9; DistributedCombinedQueryComponentTest 3) match the receipt; "2026-10-05" matches; the three combined test classes contain no tie-order assertion, which matches the Limits; no choice section, which matches h3 finding 10 (no live alternative).

1. Draft says: "Changelog: `changelog/unreleased/SOLR-17976.yml`"
- Evidence: `pr-formula.md` lines 170 to 175 require a link at the head SHA. The file exists at 56ea43c448ed (blob).
- Replacement: "Changelog: [changelog/unreleased/SOLR-17976.yml](https://github.com/nick-boss-tech/solr/blob/56ea43c448ed4fea75131d34c390d11233bab5cb/changelog/unreleased/SOLR-17976.yml)"

Optional notes, not blocking:
- Hold: the changelog title is 236 characters with type `fixed` (changelog lines 1 to 2 at head). h3 finding 4 recommends type `changed` and a shorter title. That moves the head (h3 owner decision 5).
- Hold: the receipt (line 8) says an earlier combined run failed at the focused test step. h3 finding 9 and owner decision 7 ask for an owner check before the combined counts are cited.
- Pre-post: the commit `7b90b627a9e` subject "add testing handoff" (h3 finding 1).

## Not done

- No build, test, Gradle, gate, test-queue, gh call, Jira call, commit, push or post. Live PR state for these five was not checked.
- Proof counts were checked against the receipts and the test source at head (test methods, new-test placement). The gate and premise logs named in the receipts are not on disk, so the base-failure results and green runs are receipt-only.
- Jira text came from `research/jira-context` packets, not live Jira.
- Merge bases came from the fork refs in the worktree. Upstream main was not re-fetched.
- Changelog YAML was read as text, not parsed.
- Not checked: whether the Solr dispatcher keeps a trailing slash in the request path (s4 open question), and the runtime result of the 17051 mincount 4 check on the base code.
- No draft names Lucene behavior, so the Lucene version check does not apply.

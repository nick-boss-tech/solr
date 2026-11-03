# cuvs-lucene readiness review, round 4 (step 03)

Status: DELIVERED. Disposition: **CHANGES NEEDED** (upstream moved after the package was built; the tests and their proof are sound at their base).

Claim: `claims/cuvs-lucene-03-readiness-review-round-4-vm6.md`. Reviewer: vm6, which did not prepare steps 01, 02 or the review kit. Board snapshot reviewed: coordination-ui `ab64a120f3` (claim pushed as `1a893b4149`). Upstream read through the GitHub API, read-only, 2026-11-03 between 02:25Z and 02:40Z. Nothing was posted, pushed or commented.

## Identity table

Every digest below was recomputed on vm6 from the board copies; all match the step 01 manifest, the step 02 receipt and the review kit.

| Item | Value | Check |
| --- | --- | --- |
| Combined test patch | `material/cuvs-lucene-contribution-round-4/combined-tests.patch`, SHA-256 `84688de57719d0600505e42f4a62bfbd8b49373486ee90f6c4d9efbeff73ee5c` | match |
| Step 01 manifest | SHA-256 `38af9de7bbf9b4fab805096b92faf46d9643e13713ef5ba44fe711d92af6cdf8` | match |
| Step 01 README | SHA-256 `d07dd5089030b2415039d9f74e4f195f5e118c14307643bbcf9364d04295da89` | match |
| Submission manifest | SHA-256 `7a6ac419046b2aac076a4cb00e834b58f34d52cef1b7e302bcd2607147cd58c1` | match |
| Check A patch (in package and in round 2 material) | SHA-256 `d9ad46585c65846cf9b0d4f0ccffbf882e79be593f15897d8775115de810b154`, both copies | match |
| Check B patch (in package and in round 3 material) | SHA-256 `56cb5cb10f210e216c36fd413821dd1ddd931e5dd4ed26fc288299c928a84238`, both copies | match |
| First-contact draft, revision 1 | SHA-256 `a8fea52cf1fa33908f1462a87ae7e8473b2cd823c2ae40d305cac61e2b073912` | match |
| Technical notes, revision 1 | SHA-256 `dfa9a790c02e48617f1b208ea235703f8cd0c1b2caaf5ee56f035b76102522c0` | match |
| Receipts | round 2 introduced by `c6e0a5564bb6`, round 3 by `b64dc930dac6` | read |

Four test files (Git blobs), reconstructed independently on vm6:

| File | Original at PR 2702 `23004d0f` | Final after patch | Receipt identity |
| --- | --- | --- | --- |
| `src/test/.../TestCompatKnnFloatVectorQuery.java` | `2dc5e3b8564c` | `0a2628c116e4` | round 2 `source-manifest.json`, same blob |
| `compat/leaf-reader-102-bits/src/test/.../TestLuceneCompat.java` | `ba897c671801` | `fa04413d7e55` | round 2 `source-manifest.json`, same blob |
| `compat/leaf-reader-103-acceptdocs/src/test/.../TestLuceneCompat.java` | `dcd6544f4d1c` | `9bdc09a5865d` | round 2 `source-manifest.json`, same blob |
| `src/test/.../TestBackCompatIndices.java` | `203b57469d91` | `ee3cd6734e41` | round 3 final test SHA-256 `642076a8...cb534` equals the reconstructed bytes; original SHA-256 `dfcc5846...6fab9` equals the round 3 baseline |

Reconstruction method: the four originals were fetched from NVIDIA/cuvs at `23004d0f0de2014f11b09313f04fcf0de7643c3b` through the contents API into an isolated directory (not a repository). `git apply --check --whitespace=error-all` passed, then the apply ran; all four final blobs matched. Separately, applying `cpu-regression.patch` then `backcompat.patch` gave byte-identical files to the combined patch. Every changed file is byte-identical to the input its receipt covers.

## Upstream state now (changed since 2026-10-24)

| PR | State on 2026-10-24 (step 02) | State on 2026-11-03 |
| --- | --- | --- |
| 2702 "Add support for Lucene 10.2-10.5" (imotov) | open draft, head `23004d0f0de2` | open draft, head unchanged `23004d0f0de2`; last updated 2026-10-30 |
| 2867 "Prepare for supporting multiple Lucene versions" | open, head `1c6bc921126e`, two approvals | **merged 2026-10-29T18:16Z**, final head `90661fd0f1d6` |
| 2970 "Add support for Lucene 10.3" (imotov) | not known to step 02 | **open, created 2026-10-30, head `3602ef0a8047`**, approved by jameslamb (10-30) and paul-aiyedun (2026-11-03T01:15Z) |
| main | | `ffd54e951549` |

imotov's split comment on PR 2702 now lists 2769, 2771, 2867 and 2970, then "Add support for 10.4", "Add support for 10.5". PR 2702 is the umbrella being split; the pieces land as separate PRs.

What this does to the four files:

| File | main `ffd54e95` | PR 2970 `3602ef0a` | Combined patch hunk |
| --- | --- | --- | --- |
| `TestLuceneCompat` (10.2 bits) | absent | added, blob `ba897c67` (same as 2702) | applies unchanged on 2970 |
| `TestLuceneCompat` (10.3 acceptdocs) | absent | added, blob `dcd6544f` (same as 2702) | applies unchanged on 2970 |
| `TestCompatKnnFloatVectorQuery` | absent | added, blob `66409f694311`, differs from 2702: adds `testFilteredSearchResultsAreKept`, `testPerLeafTopKIsLucenesExactSearchBoundary`, and a three-argument `CountingQuery(target, k, filter)` | **fails to apply on 2970** |
| `TestBackCompatIndices` | blob `583c2a16786a` (from 2867): fixtures moved to `releases/<cuvs version>/<lucene>/<name>.zip`, `FILE_NAME` replaced by `INDEX_PATH` | blob `ab1ea443f2e0`, same layout plus `tests.cuvs.backCompatDir` module-written indices | **fails to apply on 2970**; the new method uses the removed `FILE_NAME` pattern and the old fixture tree |

Overlap check: PR 2970's new query tests check hit counts under a filter (results kept rather than discarded for exact search) and the per-leaf topK boundary. They do not drop or check the acceptance mask, deleted-document exclusion, or filter and delete intersection through a direct adapter call. The offer is not obsolete; its home and base have moved.

## Code assessment

Check A, `TestCompatKnnFloatVectorQuery` plus the two `TestLuceneCompat.approximateSearch` bridges:

- The design makes a dropped mask visible. Vectors are `(i, i)` and the target is `(0, 0)`, so the nearest documents are exactly the filtered (`id < 40`) and deleted (`id < deletedCount`) ones. Any leak lands in the top k and fails `assertAcceptedHits`.
- The direct adapter call blocks the exact-search fallback, as step 03 requires. It bypasses `rewrite`, and the search counter (1 then 2) proves the approximate path ran on both legs. The round 2 mutation runs confirm the effect: with the mask dropped on 10.2 and 10.4, exactly the three new methods fail and the original passes (`receipts/cuvs-lucene-pr2702-cpu-round-2/cpu-tests.json`, entries 2 and 6: `tests_run=4`, `failures=3`, the three new methods named).
- Filtering, deletion and intersection are all asserted (`deletedCount` 0, 20, 50 against `firstMatching` 0 or 40), and the 103 bridge converts through `AcceptDocs.fromLiveDocs`, so both the `Bits` and `AcceptDocs` signatures are exercised.
- Pin, labeled honestly: the direct leg passes a hand-built `FixedBitSet`. It proves the adapter forwards the mask it is given, not that the query composes filter and live documents. That composition is covered by the searcher leg, and the mutation results show the searcher leg failing too. The single-segment assertion (`leaves().size() == 1`) keeps doc IDs equal to ordinals and is a precondition, not mutated.

Check B, `testUpgradeIndicesWithDeletesAndSparseVectors`:

- Live documents: every third fixture document is deleted by ID, and `assertLiveDocumentsAndVectors` walks `getLiveDocs()` per leaf and compares the full ID set.
- Sparse ordinals: every fifth upgrade document carries no vector, and the vector walk uses `DocIndexIterator` with `vectorValue(it.index())`, so the ordinal-to-document mapping is exercised rather than assumed dense.
- Exact ID and value association: `assertArrayEquals(..., 0f)` per ID, plus set equality of IDs with vectors.
- Before and after merge: the check runs with more than one leaf and deletions present, then after `forceMerge(1)` with one leaf and no deletions, then `checkIndex`.
- Counts in the public text verified from source: 200 fixture docs minus 67 deleted (`i % 3 == 0`, 0..199) is 133, plus 50 upgrade docs is **183** live; 133 plus 40 upgrade docs with vectors (10 of 200..249 have `i % 5 == 0`) is **173** vectors.
- Fixture data contract: expected vectors are regenerated with `new Random(42)` over `NUM_DOCS = 200`, matching the fixture writer at line 294 of the patched file. The round 3 data-fault control (vectors reassociated to the wrong IDs) fails the new method on 10.2 and 10.4 at element 0 while the original methods pass (`mapping-control.json`), so the association check bites.
- Pins: `leaves().size() > 1` and `hasDeletions()` before the merge, and `hasDeletions() == false` after it, are state preconditions, not mutated.

Scope and attribution: the patch touches exactly the four test files; no production file, no native or API stub, no build or runtime material. It does not duplicate PR 2702's production fix.

## Proof mapping

| Claim in the drafts | Evidence | Verified here |
| --- | --- | --- |
| 16 positive executions, 0 failures, 0 skipped, Lucene 10.2.2, 10.3.2, 10.4.0, 10.5.1 | round 2 receipt item 4; `cpu-tests.json` entries 0, 3, 4, 7 (`tests_run=4`, `failures=0`) | yes |
| Mask dropped on 10.2.2 and 10.4.0: three new cases fail, original passes | `cpu-tests.json` entries 1, 2, 5, 6 | yes |
| Four modules compile, linkage zero failures, table values | round 2 receipt item 3 | yes, table copied exactly |
| 12 positive executions over 138 fixture cases, 0 ignored, 0 assumption failures | round 3 receipt items 3 to 5, `verification.log` | yes |
| Data-fault control fails the new method on 10.2 and 10.4 | `mapping-control.json` exit 1 both, expected failure named | yes |
| 183 live documents, 173 vectors | test source constants | yes, computed above |

Round 3's compile matrix (`receipts/cuvs-lucene-backcompat-round-3/matrix.json`) agrees line for line with the five JSON lines in `reproduce.log` (cuvs-api 44/95, 10.2 42/64, 10.3 45/67, 10.4 50/72, 10.5 50/72, all exit 0). It is extracted from stdout and carries reduced metadata (no argument arrays); that is a limit of the summary, not a missing run. No rerun is requested.

Scope of all evidence: the base is PR 2702 at `23004d0f`. None of it ran against PR 2970, main, or merged PR 2867.

## Description findings

| # | Priority | Passage | Finding | Remedy |
| --- | --- | --- | --- | --- |
| T1 | High | First contact, paragraph 3: "I see that #2867 lands the Lucene 10.2 layout first. If it does, I can port these tests to that layout." | False as of 2026-10-29: #2867 has merged. The conditional now reads as stale. | Rewrite after the target decision below. |
| T2 | High | First contact, closing question: "Would you want these tests included in this PR?" (PR 2702) | PR 2702 is the umbrella imotov is splitting; the compat modules and `TestCompatKnnFloatVectorQuery` now land through PR 2970, which is approved twice and likely to merge first. Asking for inclusion in 2702 points at the wrong PR. | Choose the target (options below) and ask where the tests belong. |
| T3 | Medium | Technical notes, Identity: PR 2867 head `1c6bc921`, "If it lands first..."; blob `f6701a14`; "Any run against PR 2867's head" | Stale: 2867 merged at `90661fd0`, and main's `TestBackCompatIndices` is `583c2a16`. | Revise with current heads. |
| T4 | Low | Technical notes open with the header and close with "AI text above" | comment-formula puts GitHub disclosure in the header only; the footer is the Jira form. | Drop the footer line if the notes are posted as a GitHub comment. |
| T5 | Low (condition) | Both drafts already carry "(reviewed by Nick Shanin)" | Correct only once Nick has reviewed the posted text. The owner notes say so. | Keep the owner note; Nick confirms before posting. |

Checked and clean: the addressee is the PR author (imotov); evidence is in proportion to the claims, and CPU limits are explicit ("with no GPU", Not covered section); there is no date promise and no request for review work; there is no branch or diff link, consistent with the ask-comment rule; there are no em or en dashes and no internal process words; it uses "I", never "we"; and the numbers match the receipts. No DCO requirement was found by step 02; not rechecked here because no new upstream contribution file was read.

## Disposition: CHANGES NEEDED

Within the CPU scope the source and proof are correct at their base. The package cannot go to Nick as READY because its base and target have been overtaken: PR 2867 merged, and PR 2970 now carries two of the four files in different form. Posting the current first contact would point imotov at the wrong PR with a stale conditional, and the patch would not apply where the work is landing.

Target options (rung 4; recorded for the owner, since the public target binds at step 04):

1. **Port to PR 2970 (recommended).** The `TestLuceneCompat` hunks apply unchanged. `TestCompatKnnFloatVectorQuery` needs a rebase onto PR 2970's version (reuse its new three-argument `CountingQuery`, keep its two new tests). The back-compat method needs a port to the `releases/` layout and `INDEX_PATH`, after checking that the released fixtures keep the `Random(42)`, 200-document, `i % 3` contract (`GenerateReleasedIndices.java` from PR 2867). Evidence is then 10.2 and 10.3 only, the modules in PR 2970; the 10.4 and 10.5 results stay recorded against PR 2702 and are cited as such. Why: it is where imotov is landing this code, it is approved, and it will soon be main.
2. **Keep PR 2702 as the target, package unchanged.** It still applies there, but PR 2702 is not the merge path. Only the text would change. Weaker: the tests would need a port anyway when the pieces land.
3. **Wait for PR 2970 to merge, then port to main.** Same work as option 1 on a stable base; slower, and the offer arrives after the review window on PR 2970 closes.

Bounded repair and owner of the next record:

- (a) New implementation and CPU job (Linux, implementation plus gate tags; no GPU): port the two changed files to the chosen base, publish a new package identity, and run an exact-input CPU job for the affected legs. Those are positive runs, the mask-drop mutation (Check A) and the data-fault control (Check B) on each Lucene module present at that base. The unchanged `TestLuceneCompat` bridges need application review only.
- (b) Step 02 revision 2: new first-contact and technical-notes drafts against the new identity, fixing T1 to T5.
- (c) Step 03 re-review of (a) and (b), by a host that prepared neither.

None of this needs a rerun of the round 2 or round 3 runs; they stand for PR 2702 at `23004d0f`.

## Owner packet

Nothing below is approved for posting. The current first-contact text, `drafts/cuvs-lucene-first-contact-round-4-revision-1.md` (SHA-256 `a8fea52c...3912`), is presented with this review for Nick's information only, and should not be posted in its current form because of T1 and T2. The one decision for Nick that unblocks the repair is the target: PR 2970 (recommended), PR 2702, or main after PR 2970 merges.

## Limits

- This is a source, evidence and public-text review. No build, compile or test ran on vm6, and no GPU, native, release, or Solr behavior was examined.
- Upstream state is a snapshot from 2026-11-03 around 02:30Z; PR 2970 may merge at any time, which turns option 1 into option 3 with the same port.
- Review threads on PR 2970 (inline comments from paul-aiyedun and imotov on 2026-11-02) were listed but not read in full.

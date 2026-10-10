Result: SOLR-17051 draftable with two FIX items and one owner decision; SOLR-15331 draftable as INCONCLUSIVE, held by one FIX (a Claude trailer on the live head); SOLR-16290 held, audit only, no draft. Read only; nothing committed, pushed, posted or run.

Heads: confirmed read only with `git ls-remote` on 2026-10-09. solr-17051-submit 148544e9ed54a7c6dd6714197d3bf74b06fff1ca, solr-15331-submit 6769b4cd8a12d8606a1616c6269c2fdb224d019b, solr-16290-submit ff760120c81792022458a5a290b6e58d57b9cd17. All match the claim table. The worktree's local branch refs for these three tickets are older, divergent histories and were not used (see NOTE 9).

## Findings

**1. FIX. SOLR-15331, commit trailer on the live head.**
Evidence: `git log -1 --format=%B a9f5ecf9d0c` on origin/solr-15331-submit prints `Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>`. The rule in `pr-formula.md` (standing rules, and "No Claude in commit authors, committers, or trailers") and the user's memory rule both forbid it. The other two heads have no such trailer.
Replacement: the commit message of a9f5ecf9d0c becomes `SOLR-15331: unit test for the missing bucket count; class javadoc; changelog`, with the trailer line and its blank line removed. Changing it rewrites the fork branch. That needs the owner's direction. Not done here.

**2. FIX. SOLR-17051 draft, Proof line on the base failure.**
Evidence: `receipts/SOLR-17051.md` line 7 says the proof is PASS and "the record gives no failure shape for this proof". No `results/SOLR-17051.failbefore.json` exists on disk (`research/test-queue/results/` has only `SOLR-17051.json`). The draft therefore cannot name the observed failure.
Replacement: in `pr-drafts/search-components/SOLR-17051.md`, delete the bracketed owner sentence in the Proof section, or replace it with the failure text from a base run once the owner has one. Until then, write only: "The mincount 5 check expects no `missing` key." Do not say that it fails on the base code.

**3. NOTE. SOLR-17051, the only gate result on disk is an older FAILED run.**
Evidence: `research/test-queue/results/SOLR-17051.json` has status FAILED, with TestJsonFacets 30 tests and 2 failures, from the 2026-10-03 run. Its log `research/test-queue/logs/SOLR-17051-20261003-105040.log` lines 159-166 show `expected key 'missing' @ facets/f3` at `doStatsTemplated`, the default-mincount zero-count contract. The later green gate logs named in the receipt (g17051-harden.log, g17051-harden2.log) are not on disk. The receipt is the only source for the green counts.
Replacement: none. The draft's owner notes already say not to cite the FAILED run as the base failure.

**4. NOTE. SOLR-17051, reference guide not updated.**
Evidence: at head `solr/solr-ref-guide/modules/query-guide/pages/json-facet-api.adoc` line 210 (`mincount`: "Only return buckets with a count of at least this number. Defaults to `1`.") and line 211 (`missing`). The branch does not touch the guide (`git diff --stat` of `solr/solr-ref-guide` is empty). The new rule changes what a user sees for `mincount` above 1.
Replacement, if the owner wants the guide in this PR: line 210 becomes "|`mincount` |Only return buckets with a count of at least this number. Defaults to `1`. When this is above `1`, the `missing` bucket is also left out when its count is below it." Otherwise name it in Limits, as the draft does.

**5. NOTE. SOLR-17051, commit subject wording.**
Evidence: live head history, commit a17520f98b1 subject "do not drop the missing bucket on shards; add mincount tests and changelog; record contract decision". The phrase "record contract decision" reads as process language in a public history. Commit 148544e9ed5 is "apply Spotless formatting".
Replacement: squash the four commits into one, with the subject `SOLR-17051: leave out the missing bucket when mincount is above 1`. If not squashed, reword a17520f98b1 to `SOLR-17051: do not drop the missing bucket on shards; add mincount tests and changelog`.

**6. NOTE. SOLR-15331, history carries a handoff commit.**
Evidence: live history has 85e81363e7b "SOLR-15331: testing handoff for external review", which adds `SOLR-15331-TESTING.md` (29 lines). Commit 6769b4cd8a1 "drop the TESTING.md handoff note" removes that file. The final diff is clean (3 files), but a maintainer scrolling commits sees the handoff.
Replacement: squash before the PR, or reword 85e81363e7b to a plain subject. Owner decision, because either needs a fork rewrite.

**7. NOTE. SOLR-15331 Javadoc depends on the SOLR-17051 server layer.**
Evidence: `solr/solrj/src/java/org/apache/solr/client/solrj/response/json/BucketBasedJsonFacet.java` at 6769b4cd8a12, lines 145-150. The text says `UNSET_FLAG` is returned "if this is not the case", meaning the missing option was not set. After SOLR-17051, a request that set `missing` gets no key when a `mincount` above 1 drops the bucket. Then `getMissingCount()` also returns `UNSET_FLAG` for a requested bucket. This is the only cross-layer dependency between f5 tickets that changes what a client sees.
Replacement, for lines 148-149 of the Javadoc: "`{@link #UNSET_FLAG}` is returned if the user did not request it, or if the server left it out, for example when a `mincount` above 1 drops it." Use it in this PR if SOLR-17051 lands first or in the same release. Otherwise it can wait.

**8. NOTE. SOLR-15331, changelog type.**
Evidence: `changelog/unreleased/SOLR-15331.yml` uses `type: fixed`. The change also adds a public method (`getMissingCount()`). The JIRA is a bug report, so `fixed` is defensible.
Replacement: none needed unless the maintainers prefer `added`. Owner call, low stakes.

**9. NOTE. Local branch refs are not the live heads.**
Evidence: `git rev-list --left-right --count`: local wt-solr-17051-submit (7b09c8b9871) against origin (148544e9ed54) is 4 ahead and 15 behind. Local wt-solr-15331-submit (66e7dd56a61) against origin (6769b4cd8a1) is 3 ahead and 14 behind. Local solr-16290-submit (f0b06b489d8) against origin (ff760120c81) is 2 ahead and 1 behind. The local 16290 ref has commit f0b06b489d8 "add hypothetical-reproduction handoff doc", which adds `SOLR-16290-TESTING.md`. The live head does not have that file.
Replacement: none in the text. Push nothing from these local refs, and treat `origin/*` as the live heads for this review.

**10. NOTE. SOLR-17051 and SOLR-12556 sit in the same merger class, with no code overlap.**
Evidence: `FacetFieldMerger` extends `FacetRequestSortedMerger` (FacetFieldMerger.java line 28 at 148544e9ed54). The 17051 hunks are FacetFieldMerger.java lines 139-145 and FacetFieldProcessor.java lines 534-542. The 12556 files are FacetMerger.java, FacetModule.java, FacetRequestSortedMerger.java, TestJsonFacetRefinement.java and one changelog file, so there is no shared file. 12556 changes `isBucketComplete` (FacetRequestSortedMerger.java, diff hunk new lines 158 to 195 at 033ec65a0e1). That method is called only for term buckets (FacetFieldMerger.java line 122). The 17051 screen runs on the missing bucket after the term loop and does not call it. Trial merges with `git merge-tree --write-tree` are clean for 17051 against 12556, 15331 and 16290, and no ref was written.
Dependency: the distributed result of 17051 depends on the summed missing count from `FacetFieldMerger.merge` (lines 54-62), which 12556 does not change. Neither draft depends on the other's layer for its claim. The two heads were not run together.
Replacement: none. Say in the owner notes that a combined run was not done.

**11. NOTE. SOLR-16290 and SOLR-12556 share a domain family, not code.**
Evidence: the held pin is a single-node test. `TestJsonFacetsWithNestedObjects.java` at ff760120c81 lines 323-348 is the control, and lines 350-379 are the pin (javadoc, `@Test`, `@AwaitsFix`, method). Both use `Client.localClient()`. The 12556 changes are coordinator-only (FacetModule.java, FacetRequestSortedMerger.java, FacetMerger.java), and they touch no file the pin exercises. The exclusion handling is `handleFilterExclusions` in `FacetProcessor.java` at base 0cc328310f8, lines 193-235, with the comment "recompute the base domain" at line 233. That is where the held failure's domain logic lives. Not traced further, because the ticket is held.
Replacement: none. Note only that 12556's distributed refinement tests do not run this pin, and this pin does not run the coordinator.

**12. NOTE. SOLR-17051 test placement, a strength to keep.**
Evidence: the new checks are in `doStats` at head lines 2566-2585. `testStats` (line 1466) and `testStatsDistrib` (line 1477) both call `doStats`, so the checks run standalone and distributed.
Replacement: none. The draft says this in its Proof.

## Task results

**SOLR-17051, draftable.** The branch matches its receipt. Four commits, four files (changelog, FacetFieldMerger.java, FacetFieldProcessor.java, TestJsonFacets.java), author and committer Nick Shanin, no trailer. The code does what the changelog says. Shards still return the missing bucket, the coordinator screens the summed count, and the default `mincount` keeps the old response. The Proof uses the receipt counts (TestJsonFacets 30 of 30, TestJsonFacetRefinement 10 tests with 2 skipped). The fail-before result has no failure text on record, so FIX 2 applies. The contract choice is the owner's: option 3 (only above a `mincount` of 1) is implemented, and the draft states all three options. The draft is `pr-drafts/search-components/SOLR-17051.md`, written against 148544e9ed54. Its public text is about 4,600 characters with link URLs and about 3,650 without them, close to the 3,500 guide. The 15331 draft is about 3,600 with URLs and about 2,600 without.

**SOLR-15331, draftable as INCONCLUSIVE.** The branch matches its receipt. Three files: BucketBasedJsonFacet.java, NestableJsonFacetTest.java and the changelog. The receipt is right that the new test calls `getMissingCount()`, which the base code does not have, so no base run is possible. The draft says so, and it does not state a pass or fail on the base code. Only the test count (2 of 2 at this head) is cited. There is no live alternative worth a choice section, so none is drafted. The draft is `pr-drafts/search-components/SOLR-15331.md`, written against 6769b4cd8a12. It is not postable until FIX 1 is resolved.

**SOLR-16290, held, audit only, no draft.** The head matches. The diff is one test file, 58 added lines, with a control test (lines 323-348) and a pin (lines 350-379) that carries `@AwaitsFix` with the ticket URL. The pin expects the same output as the control. The merge base 0cc328310f8 matches the receipt's proof base. The receipt's counts (9 tests, 1 skipped, 1 expected failure under the pin) were not re-run and are cited only as the receipt. The disposition (pin PR, fund the real fix, or bank) is the owner's call, already on record. This review recommends nothing.

## Owner decisions

1. SOLR-17051 contract: confirm option 3 (screen only above a `mincount` of 1), and confirm the decision entry the receipt names. The draft states all three options.
2. SOLR-17051 reference guide: change the `mincount` row in this PR (NOTE 4), or leave it for a follow-up.
3. SOLR-17051 split-count fixture (two shards at 400 each, `mincount` 500): add in this PR, or follow up on request.
4. SOLR-15331 history: reword a9f5ecf9d0c (trailer, FIX 1) and squash or reword 85e81363e7b (NOTE 6). This rewrites the fork branch, so it needs your direction.
5. SOLR-15331 Javadoc sentence (NOTE 7): include now, or only if SOLR-17051 lands first.
6. SOLR-17051 base run: someone needs to run the fail-before check once to capture the failure text. That is a verify step, not done here.
7. SOLR-16290: pin PR, fund the real fix, or bank. Already on record; not changed by this review.

## Not checked

- No builds, tests, Gradle or gate reruns. Proof counts come only from the receipts.
- Gate logs are not on disk: g17051-harden.log, g17051-harden2.log, g15331-harden.log, g16290-gate.log, g16290-premise.log. The on-disk 17051 result is the older FAILED run (NOTE 3).
- No fail-before failure text exists on disk for 17051 or 16290. 15331 is inconclusive by construction.
- 16290 was not audited past its held state. The pin's failure line was not traced.
- 17051 and 12556 were not run together. Only trial merges were checked.
- Whether the four `sparse_s` documents spread across shards in the distributed fixture. That decides how much the distributed check covers.
- TestJsonFacets has 30 methods named `test*` but 14 annotated `@Test`. I did not check how the runner counts them. The receipt's 30 matches the name count.
- Live PRs for 17051 and 15331: no `gh` call. The inventory says there is no PR, and `gh` is limited to f6's pass.
- apache/solr upstream main was not re-fetched. The base commits cited are the branch merge bases: 14c7aac0d151 (17051), b5c71bc5573 (15331), 0cc328310f8 (16290).
- Legacy `facet.field` with `facet.missing` and `mincount` (SimpleFacets): not checked. The ticket is about the JSON Facet API.
- Range facets: I grepped FacetRangeProcessor.java and the SolrJ JSON package for a `missing` key. None found. I did not trace the whole range path.
- Lucene versions: no draft names Lucene behavior, so the 9.x and 10.x check does not apply.
- Commit authors and trailers: checked on the live commits of the three branches only. Changelog authors are Nick Shanin.
- The 17051 and 15331 draft dates are the hardening and push dates from the receipts, not exact run times.

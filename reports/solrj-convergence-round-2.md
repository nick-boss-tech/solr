# SolrJ convergence, round 2

Verdict: **NOT CONVERGED, and both drafts stay on HOLD.** SOLR-10364 and SOLR-11356 each name a packaged commit that has not been pushed to origin, and SOLR-11356 still has its blocking header-copy item open on the branch.

Counts: drafts re-checked 2. Dispositions confirmed 7 (SJ-R1-45, 46, 48 and 49 as held; SJ-R1-50, 51 and 52 applied). Dispositions disputed 1 (SJ-R1-53). Items open 10: SJ-R1-45, 48, 49 and 53, the SJ-R1-51 revision owed after 49, and five packaging titles. New findings against the two drafts: none.

Scope: `assignments/pool-solrj-convergence-round-2.md`, checked against `reports/solrj-convergence-round-1.md` and the main side's response. One read-only subagent did the review. Heads were checked with `git ls-remote origin refs/heads/<branch>`. Nothing was edited, posted or built.

## Verdict per draft

- **SOLR-10364: HOLD (NOT CONVERGED).** The draft names `efb1e6717f9f`, which is the packaged commit. The live tip of `solr-10364-submit` is `502bdbf033fa`, so the head does not match. The packaged commit is not on any origin ref and not in this checkout.
- **SOLR-11356: HOLD (NOT CONVERGED).** The draft names `6120dae28d04`, which is the packaged commit. The live tip of `solr-11356-submit` is `8474e5a3a26d`, so the head does not match. The packaged commit is not on any origin ref and not in this checkout. SJ-R1-49 is still open on the branch, and SJ-R1-53 is disputed.

## Items 1 to 4

1. **Packaged heads (item 1): not pushed.** A scan of every origin ref finds neither SHA. Both receipts say the packaged head is the live tip plus one commit removing a testing file. The hold stands for both drafts.
2. **SJ-R1-49, the copy of the headers (item 2): STILL OPEN.** The branch does not copy the headers. `ConcurrentUpdateJettySolrClient.java` at the live tip stores `this.origHeaders = origRequest.getHeaders();` at line 128. At the base, `SolrRequest.getHeaders` (lines 374 to 377) returns `Collections.unmodifiableMap(headers)`, which is a view of the request's own map. The shared test (`ConcurrentUpdateSolrClientTestBase.java`, lines 300 to 333) covers credentials only, not header changes. The recorded owner decision is to copy the headers in the key (required). The draft's bold summary at line 13 still says "keeps the credentials and headers", and the live-view bullet at line 35 is correct until the copy lands. The draft stays on HOLD.
3. **SJ-R1-53, the merge-base sentence (item 3): DISPUTED, so not resolved.** The main side's response says the record contradicts "the merge-base with upstream/main". Checked against `upstream/main` (apache/solr, at `d76fc14b985`):
   - the merge-base of `8474e5a3a26d` with `upstream/main` is `cabedd1d968059215188f4e7563fb303241899ed`, and that commit is an ancestor of `upstream/main`;
   - against `origin/main`, which is the fork, the merge-base is `e044bf20b405`, and `cabedd1d968` is not an ancestor.
   The pull request targets apache/solr main, so the register's wording is correct. The response's fact check for SOLR-14187 also used `origin/main` to get `cabedd1d968`, so the main side is mixing the two remotes. Replacement for the draft's line 9 (should-fix, SJ-R1-53): "Both links are at the base commit the branch starts from, `cabedd1d968`..." becomes "Both links are at the merge-base with upstream/main, `cabedd1d968059215188f4e7563fb303241899ed`, which the branch starts from." The receipts for SOLR-11356 and SOLR-10364 should name `origin/main` as the fork and record the `upstream/main` result. That is a main-side correction, not edited here.
4. **Packaging titles (item 4): none landed word for word.** The assignment maps SJ-R1-06 and 09 to titles, but those register rows are date rows in the SOLR-4335 and SOLR-4336 drafts. The check used the verbatim titles in the response's packaging section.
   - SOLR-3999 (`e3af6f36e31`): not landed. The live title is "declare an explicit serialVersionUID so Java-serialized documents survive recompiles."
   - SOLR-4335 (`8f6e267d648`): not landed. The live title is "SolrJ XML update requests no longer send ...", narrower than the replacement.
   - SOLR-4336 (`798618aa08f`): not landed. The live title is "treated as unset (default applies) instead of failing with a NumberFormatException."
   - SOLR-6046 (`3b833e369a0`): not landed. The live title lacks "Object" before "array values".
   - SOLR-8536 (`28552ddc26e`): not landed. The live title says "other control characters", which overstates. The code is ASCII only (`ExecutorUtil.java` line 350, `\p{Cntrl}`), so the replacement's "ASCII control characters" is needed.

## Other register rows, checked in the live drafts

SJ-R1-46 (SOLR-10364, line 30) is applied word for word. SJ-R1-50 is applied: the Title line is removed. SJ-R1-52 (SOLR-11356, line 25) is applied word for word. SJ-R1-47 and SJ-R1-54 stand as notes. The SJ-R1-51 summary at line 32 is accurate today and must be revised after SJ-R1-49 lands.

## Notes

- SOLR-10364 line 27 ("We can open a follow-up ticket...") is a style item the response flagged for the opening pass. It is not re-graded here.
- SJ-R1-41 to 44 (SOLR-8536) are outside the two drafts in scope and were not re-audited.

## Not checked

- The 2026-10-06 gate dates the response cites, which come from the takeover log.
- The packaged-head citations, whose objects are absent.
- The gate logs, which are not on disk.

## Convergence line

NOT CONVERGED. Both drafts stay on HOLD. SOLR-10364 waits on its packaged commit being pushed and re-gated. SOLR-11356 waits on the header copy landing and on the merge-base wording correction.

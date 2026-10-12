# SolrJ convergence, round 1

Verdict: **NOT CONVERGED.** Sixteen of the nineteen drafts hold open items. SOLR-2018, 4424 and 7709 converge with notes only.

Counts: drafts checked 19 (four slices of five, four and five). Drafts with a blocking finding 4 (SOLR-8536, 10364, 11356, 18341). Drafts with should-fix findings and no blocking finding 12. Drafts with notes only 3 (SOLR-2018, 4424, 7709). Findings 71: 5 blocking, 38 should-fix, 28 note. Register IDs SJ-R1-01 to 17 come from slice A, 21 to 34 from slice B, 41 to 59 from slice C, and 61 to 81 from slice D. All are new on this pass.

Scope: `assignments/pool-solrj-convergence-round-1.md`. The decision record is `reports/solrj-clients-round-1.md` and its parts, with the draft-fidelity settlements. Heads were checked with `git ls-remote origin refs/heads/<branch>`. Two drafts name packaged commits that are not on origin (SOLR-10364 and SOLR-11356); they are on HOLD until those commits are pushed and re-gated. Four read-only subagents did the review. Nothing was edited, posted or built.

A base-run rule applies throughout: when a draft's base run used the branch's test file, the text must say so. Base files have fewer tests, so the counts only match with the branch file.

## Verdict per draft

- **SOLR-2018: CONVERGED (notes only).** SJ-R1-10, 11, 12 (note).
- **SOLR-3722: NOT CONVERGED.** SJ-R1-01, 02 (should-fix); SJ-R1-13 (note).
- **SOLR-3999: NOT CONVERGED.** SJ-R1-03 (should-fix); SJ-R1-14 (note).
- **SOLR-4335: NOT CONVERGED.** SJ-R1-04, 05, 06 (should-fix).
- **SOLR-4336: NOT CONVERGED.** SJ-R1-07, 08, 09 (should-fix); SJ-R1-15, 16 (note).
- **SOLR-4422: NOT CONVERGED.** SJ-R1-21 (should-fix); SJ-R1-22 (note).
- **SOLR-4424: CONVERGED (notes only).** SJ-R1-23 (note).
- **SOLR-5220: NOT CONVERGED.** SJ-R1-24 (should-fix); SJ-R1-25, 26 (note).
- **SOLR-6046: NOT CONVERGED.** SJ-R1-27 (should-fix); SJ-R1-28, 29 (note).
- **SOLR-7709: CONVERGED (notes only).** SJ-R1-30, 31 (note).
- **SOLR-8536: NOT CONVERGED (blocking).** SJ-R1-41 (blocking); SJ-R1-42, 43 (should-fix); SJ-R1-44 (note).
- **SOLR-10364: NOT CONVERGED (HOLD and blocking).** SJ-R1-45 (blocking, HOLD); SJ-R1-46 (should-fix); SJ-R1-47 (note).
- **SOLR-11356: NOT CONVERGED (HOLD and blocking).** SJ-R1-48 (blocking, HOLD); SJ-R1-49 (blocking); SJ-R1-50, 51, 52, 53 (should-fix); SJ-R1-54 (note).
- **SOLR-12094: NOT CONVERGED.** SJ-R1-55, 56, 57, 58 (should-fix); SJ-R1-59 (note).
- **SOLR-14187: NOT CONVERGED.** SJ-R1-61 to 66 (should-fix).
- **SOLR-14298: NOT CONVERGED.** SJ-R1-67, 68, 69 (should-fix).
- **SOLR-14967: NOT CONVERGED.** SJ-R1-70, 71 (should-fix); SJ-R1-72 (note).
- **SOLR-17866: NOT CONVERGED.** SJ-R1-73, 74 (should-fix); SJ-R1-75 (note).
- **SOLR-18341: NOT CONVERGED (blocking).** SJ-R1-76 (blocking); SJ-R1-77, 78 (should-fix); SJ-R1-79, 80, 81 (note).

## Register: blocking rows

**SJ-R1-41 | SOLR-8536 | blocking.** "Control characters are replaced with spaces in the joined string" is wider than the code. The head uses an ASCII-only class. Replacement: "**ASCII control characters are replaced with spaces in the joined string, before the thread name is built.**" The Limits line 38 must match.

**SJ-R1-45 | SOLR-10364 | blocking (HOLD).** The head `efb1e6717f9f` is a packaged commit that is local to VM2 and not pushed; the live tip is `502bdbf033fa`. Hold. After the packaged commit is pushed and re-gated, replace each `efb1e67` link and the Proof with the pushed head, and re-run the head check. If it is dropped, use `502bdbf033fa` only with a gate at that tip.

**SJ-R1-48 | SOLR-11356 | blocking (HOLD).** The head `6120dae28d04` is a packaged commit that is local to VM2 and not pushed; the live tip is `8474e5a3a26d`. Hold, and replace the links and Proof as for SJ-R1-45, with `8474e5a3a26d` as the fallback.

**SJ-R1-49 | SOLR-11356 | blocking.** The draft says each stream "keeps the credentials and headers of the request that opened it", and the Limits say "This change does not copy them". The recorded owner decision is to copy the headers in the key (required). The live code keeps a live view of the headers. Until the branch copies them and the proof is re-run at the new head, the draft stays on HOLD. Once copied, line 15 becomes "**Each stream keeps a copy of the credentials and headers of the request that opened it, and only matching requests join it.**", and the live-view bullet at line 35 is deleted.

**SJ-R1-76 | SOLR-18341 | blocking.** Line 11 says the change "touches the same retry boundary and makes different choices for stale-state retries." The record did not check "different choices" against the PR diff, and the PR title supports only "retry logic". Replacement: "A separate open pull request, [apache/solr PR 4829](https://github.com/apache/solr/pull/4829), touches the same retry logic."

## Register: should-fix rows

**SJ-R1-01 | SOLR-3722 | should-fix.** "The change is in nameValueMapToList, which both constructors call." Only one call exists in `NamedList`, and `SimpleOrderedMap` reaches it through `super`. Replacement: "The change is in [nameValueMapToList](same link), which the Map.Entry array constructor calls. The SimpleOrderedMap constructor with a Map.Entry array reaches it through super."

**SJ-R1-02 | SOLR-3722 | should-fix.** "On the base code that test fails with a NullPointerException on ent.getKey()..." The test exists only at the head. Replacement: "Run with this branch's test file on the base code, that test fails with a NullPointerException on ent.getKey() when the list contains a null element."

**SJ-R1-03 | SOLR-3999 | should-fix.** "On the base code, one of the two tests fails." The test file is new on this branch. Replacement: "Run with this branch's test file on the base code, one of the two tests fails: testExplicitSerialVersionUid, with NoSuchFieldException: serialVersionUID."

**SJ-R1-04 | SOLR-4335 | should-fix.** "Nothing in the document shows where the character was." The position is visible; what is missing is any sign that the text stands for a character. Replacement: "A reader gets the text #65535; in that place. Nothing in the document says that the text stands for a character."

**SJ-R1-05 | SOLR-4335 | should-fix.** "On the base code, 2 of the 9 tests..." The 9 is the branch's file; base has 7 methods. Replacement: "Run with this branch's test file on the base code, 2 of the 9 tests that existed before the supplementary-character test fail:"

**SJ-R1-06 | SOLR-4335 | should-fix.** "10 of 10 pass at head ..., verified 2026-10-06." The 10 of 10 run is from another gate with no date in the receipt. Keep "verified 2026-10-06" only once that run is confirmed on that date; otherwise substitute the confirmed date.

**SJ-R1-07 | SOLR-4336 | should-fix.** "The numeric getters treat an exactly empty value as unset, so the default applies." The getters with no default return null. Replacement: "The numeric getters treat an exactly empty value as unset, the same as a missing value."

**SJ-R1-08 | SOLR-4336 | should-fix.** "On the base code, two of the eleven tests fail..." The base has nine test methods. Replacement: "Run with this branch's test file on the base code, two of the eleven tests fail, and they are these two."

**SJ-R1-09 | SOLR-4336 | should-fix.** "Verified 2026-10-06 at this head." Same condition as SJ-R1-06.

**SJ-R1-21 | SOLR-4422 | should-fix.** The Limits say "A follow-up is possible if maintainers want it." Replacement: "A follow-up ticket and PR can be opened on request."

**SJ-R1-24 | SOLR-5220 | should-fix.** The Limits link `LBAsyncSolrClient` at the head for pre-change code. Replacement: "[LBAsyncSolrClient](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBAsyncSolrClient.java#L194-L197) (base) has the same zombie step for 403 and 404."

**SJ-R1-27 | SOLR-6046 | should-fix.** "The XML loader builds one operation per field before update processing sees it, so the repeated-operation case from SOLR-6045 does not come from this path." Replacement: "The XML loader already combines repeated elements for one field into one update before update processing sees them. So the problem in SOLR-6045, where a field added more than once is not treated as one atomic update, does not come from this path."

**SJ-R1-42 | SOLR-8536 | should-fix.** "...goes into the worker thread's name unchanged..." The base doubles slashes, and the draft's own line 9 says so. Replacement: "**Every MDC value of the submitting thread goes into the worker thread's name, so a value with a line break puts that break into the log output.**"

**SJ-R1-43 | SOLR-8536 | should-fix.** "On the base code, 1 of the 7 fails" does not say the new test was added. Replacement: "- On the base code, with the new test added, 1 of the 7 fails: `testMdcControlCharactersNotInThreadName`. The name keeps the line break and the tab, so the name check fails."

**SJ-R1-46 | SOLR-10364 | should-fix.** No mention of SOLR-4422, which changes the same file and import block. Add after line 29: "- This change was tested on its own. SOLR-4422 changes the same file and import block. If SOLR-4422 lands first, keep both import sets. The two changes were not run together."

**SJ-R1-50 | SOLR-11356 | should-fix.** The title line at line 3 is not in the template. Delete it; the title belongs in the PR title field.

**SJ-R1-51 | SOLR-11356 | should-fix.** "## Limits" has no bold summary. Insert: "**The test depends on timing, and two parts of the match are still open: the headers and the user principal.**" Revise after SJ-R1-49 is fixed.

**SJ-R1-52 | SOLR-11356 | should-fix.** The base run used the branch's test file; the base class has 11 methods. Replacement: "- Base (`cabedd1d968059215188f4e7563fb303241899ed`), with the new test added: `ConcurrentUpdateJettySolrClientTest`, 12 tests, 1 failure. The failing test reports a document of bob sent under alice's identity, at line 328."

**SJ-R1-53 | SOLR-11356 | should-fix.** "Both links are at the base the branch starts from." Replacement: "Both links are at the merge-base with upstream/main, `cabedd1d968`, which the branch starts from."

**SJ-R1-55 | SOLR-12094 | should-fix.** The test link lines 250 to 282 span 250 to 279 at the head. Replacement: "[test, lines 250 to 279](...#L250-L279)" with the full head-SHA URL.

**SJ-R1-56 | SOLR-12094 | should-fix.** The link lines 273 to 281 should be 270 to 278. Replacement: "[test, lines 270 to 278](...#L270-L278)".

**SJ-R1-57 | SOLR-12094 | should-fix.** "On the base code, 1 of the 10 fails" does not say the new test was added. Replacement: "- On the base code, with the new test added, 1 of the 10 fails: `testMappedFieldAfterSplitIsRejected`. The base reader returns the records and raises no error, so the expected exception never occurs."

**SJ-R1-58 | SOLR-12094 | should-fix.** "passes its 31 tests at the same head" has no date. Replacement: "- The JSON loader, which calls this reader, passes its 31 tests at the same head (`JsonLoaderTest`, core module), verified 2026-10-05."

**SJ-R1-61 | SOLR-14187 | should-fix.** "## Limits" opens with a bullet. Insert: "**The test uses a stub client, not a secured cluster, and the three-argument helper is unchanged.**"

**SJ-R1-62 | SOLR-14187 | should-fix.** "The three-argument `waitForAsyncRequest` sends no credentials." The drop is the per-request basic-auth user. Replacement: "**The three-argument `waitForAsyncRequest` sends no basic-auth user on its polls or its final delete.**" and in the Limits, "sends no basic-auth user".

**SJ-R1-63 | SOLR-14187 | should-fix.** "The branch test cannot run on the base code, so the base behavior is shown by a separate scratch test." Replacement: "**The branch test is inconclusive by construction on the base code, because it calls an overload the base code does not have.**"

**SJ-R1-64 | SOLR-14187 | should-fix.** "Verified at head ..." has no date. Use "Verified 2026-10-11 at head `29023ec1ecabc2cd0a87675bdcdccf5d71e719b8`." only after the main side confirms the run date.

**SJ-R1-65 | SOLR-14187 | should-fix.** "A scratch test, not part of this change, ran against the base production code" uses process vocabulary. Replacement: "A separate check, not part of this change, ran against the base production code with the branch test left out."

**SJ-R1-66 | SOLR-14187 | should-fix.** The links labelled "at the merge-base" point at `e044bf20`, which is not the merge-base with upstream/main. Re-point to `cabedd1d968059215188f4e7563fb303241899ed` and keep "at the merge-base". The main side corrects receipt line 6.

**SJ-R1-67 | SOLR-14298 | should-fix.** "No SolrJ change is needed." The check request is built in SolrJ, so a ping route changes SolrJ. Delete the sentence.

**SJ-R1-68 | SOLR-14298 | should-fix.** "Run recorded 2026-10-06." The receipt gives a takeover-log date. Replacement: "Verified 2026-10-06 at this head." Once the main side confirms the run date.

**SJ-R1-69 | SOLR-14298 | should-fix.** "...so a base run cannot check it." Replacement: "...so the proof is inconclusive by construction on the base code."

**SJ-R1-70 | SOLR-14967 | should-fix.** "Requests with params that cannot be changed now get a copy of their params that carries the client's own state version." The copy is made only when the request names a collection and is not admin. Replacement: "**Requests with params that cannot be changed now send a copy of those params that carries the client's own state version when the client has one.**"

**SJ-R1-71 | SOLR-14967 | should-fix.** "Behavior change: these requests now carry `_stateVer_`." Replacement: "Behavior change: requests that name a collection and have params that cannot be changed now carry `_stateVer_`, so the server's stale-state error can reach the client."

**SJ-R1-73 | SOLR-17866 | should-fix.** "The flag stays set, so a later call on the same object with no collection takes the collection path." On a cloud client with no default collection, that call fails. Append: "On a cloud client with no default collection, that call fails with 'No collection param specified on request and no default collection has been set' ([CloudSolrClient, lines 950 to 963](https://github.com/nick-boss-tech/solr/blob/3f9367d796a6446fa2f0549d8e9763e07831e6e8/solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java#L950-L963))."

**SJ-R1-74 | SOLR-17866 | should-fix.** "The ticket comments also discuss a constructor that takes the flag." Replacement: "Not implemented: a constructor that takes `requiresCollection`, with the other constructors and `setRequiresCollection` deprecated. Ticket comments from 2025-08 propose it, and note that core-URL constructors must keep working on the 9.x line."

**SJ-R1-77 | SOLR-18341 | should-fix.** "An idempotent update after a 500, 503, timeout or reset now fails over." The retry-code list includes 403 and 404 too. Replacement: "an idempotent update after a 403, 404, 500, 503, timeout or reset now fails over."

**SJ-R1-78 | SOLR-18341 | should-fix.** "The ticket says these stay as they are." The ticket names admin, security and unspecified requests only. Replacement: "The ticket says admin, security and unspecified requests stay as they are. For the load balancer they do not."

## Notes

Slice A notes: SJ-R1-10 (the 2018 "making it work" sentence), SJ-R1-11 (the `SolrClient` link anchor covers one of four lines), SJ-R1-12 (the stream reference page still says Solr ignores the option; the owner has not chosen), SJ-R1-13 (a null key in 3722), SJ-R1-14 (plain-language wording in 3999), SJ-R1-15 and 16 (the 4336 link label, and the blank typed element). Slice B notes: SJ-R1-22 (base links to a different repository), SJ-R1-23 (unlabelled pre-change links in 4424), SJ-R1-25 and 26 (the missing Choice for 5220, and the receipt's test names), SJ-R1-28 and 29 (unlabelled pre-change links and the 6046 headline's breadth), SJ-R1-30 and 31 (unlabelled links in 7709, and the owner-requested rebase), SJ-R1-32 (length of 4424 and 6046), SJ-R1-33 (changelog fragment names), and SJ-R1-34 (module-check wording). Slice C notes: SJ-R1-44 (the MDC abbreviation), SJ-R1-47 (EnumSet is also not supported), SJ-R1-54 (the Jira packet's relation to the header report), and SJ-R1-59 (the reader's error status, which the draft states accurately). Slice D notes: SJ-R1-72 and 75 (the tidy wording), SJ-R1-79 (SOLR-5220 ordering not mentioned), SJ-R1-80 (an unsourced example), and SJ-R1-81 (the Jetty connect timeouts).

## Carried and branch-side items

SJ-R1-17 (length of three drafts, an owner call), and the branch-side changelog titles for 3999, 4335 and 4336. Several SolrJ items depend on the owner's decisions recorded in the roll-up, and each is named in its row.

## Not checked

- Gate logs and JUnit XML, which are not on disk, so Proof counts rest on receipts.
- The live state of the SOLR-6045 pull request and of apache/solr PR 4829.
- The objects for the packaged commits of SOLR-10364 and SOLR-11356, and for `e044bf20`-era receipts at the merge-base.
- The master receipts ledger, the takeover log and security material.

## Convergence line

NOT CONVERGED. Five blocking and thirty-eight should-fix findings are open across the nineteen drafts.

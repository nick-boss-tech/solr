# SolrJ convergence, round 1: main-side response

Round 1 returned NOT CONVERGED across the category: 71 findings (5 blocking, 38 should-fix, 28 notes) against 19 drafts. This lane verified every blocking and should-fix finding against the receipts and the branch code before applying anything, and applied each holding finding's exact replacement text to its draft. Two blocking findings on SOLR-11356 and one on SOLR-10364 are held on the packaged heads that are local to vm2 and not yet pushed; nothing that depends on those heads was applied. Two should-fix findings (SJ-R1-06, SJ-R1-09) prescribed adding a run date only if the main side could not confirm one; the takeover log confirms both gates ran on 2026-10-06, so the drafts' dates stand and no edit was made. One replacement (SJ-R1-53) was applied with a one-phrase label correction, disclosed below with its evidence. Application method: exact-string replacements, each asserted to occur exactly once before applying; the full diff was then reviewed file by file.

## Counts

| Grade | Items | Applied in this lane | Held on a packaged-head push | Settled without edit | Standing |
|---|---|---|---|---|---|
| Blocking | 5 | 2 (SJ-R1-41, SJ-R1-76) | 3 (SJ-R1-45, SJ-R1-48, SJ-R1-49) | 0 | 0 |
| Should-fix | 38 | 36 | 0 | 2 (SJ-R1-06, SJ-R1-09) | 0 |
| Note | 28 | 0 | 0 | 0 | 28 standing or record items |

After application, 17 of the 19 drafts are CONVERGED on their text. SOLR-10364 and SOLR-11356 are NOT CONVERGED; both stand on their packaged-head holds. Drafts SOLR-2018, SOLR-4424 and SOLR-7709 carried notes only in round 1 and are unchanged.

## Fact checks before applying

- **SOLR-3722 (SJ-R1-01, 02):** at head `213457aa91fd` there is exactly one `nameValueMapToList` call site (NamedList line 92), and SimpleOrderedMap's Map.Entry array constructor reaches it through `super`. The new test is absent at the merge-base and present at head, and receipts/SOLR-3722.md records the base failure as a NullPointerException on `ent.getKey()` with a null element, matching the applied Proof text.
- **SOLR-3999 (SJ-R1-03):** the run-shape prefix holds: the test file is new on the branch (0 test methods at the merge-base `b6b2b8f10e98`, 1 at head).
- **SOLR-4335 (SJ-R1-05, 06):** TestXMLEscaping has 7 test methods at the merge-base and 10 at head; receipts/SOLR-4335.md records the proof as exactly 2 of 9 failing with the branch test file. The takeover log records the SOLR-4335 gate green on 2026-10-06 at `8f6e267d648` (TestXMLEscaping 10 of 10), so the draft's "verified 2026-10-06" stands under SJ-R1-06.
- **SOLR-4336 (SJ-R1-07, 08, 09):** at head, `getNumeric` and `getFieldNumeric` return null for an exactly empty value and for a missing value alike. SolrParamTest has 9 test methods at the merge-base and 11 at head; receipts/SOLR-4336.md records the proof as exactly 2 of 11. The takeover log records the SOLR-4336 gate green on 2026-10-06 at `798618aa08f` (SolrParamTest 11 of 11), so the date stands under SJ-R1-09.
- **SOLR-5220 (SJ-R1-24):** the LBAsyncSolrClient citation now points at the base `cabedd1d968` lines 194 to 197 with a "(base)" label; those lines hold the retry and zombie step, and the branch diff does not touch that file.
- **SOLR-6046 (SJ-R1-27):** XMLLoader lines 381 to 398 at head `3b833e369a0b` combine repeated elements for one field into one update before update processing sees them, so the SOLR-6045 problem does not come from this path, as the applied sentence states.
- **SOLR-8536 (SJ-R1-41, 42, 43):** at head `28552ddc26eb`, ExecutorUtil line 349 replaces with the ASCII-only class `\p{Cntrl}`, so the narrowed claim is the true one and the draft's Limits bullet on characters above ASCII already matches it. The base code doubles slashes (the draft's own line 9), so "unchanged" in the old summary was wrong and is deleted. The new test `testMdcControlCharactersNotInThreadName` has 0 hits at the merge-base `14c7aac0d151` and 1 at head.
- **SOLR-10364 (SJ-R1-46):** SOLR-4422 changes DocumentObjectBinder.java (one line plus the LinkedHashMap import, verified against its merge-base `97d973814336`); SOLR-10364 changes the same file and import block. The added Limits bullet states the interaction and that the two changes were not run together.
- **SOLR-11356 (SJ-R1-52, 53):** receipts/SOLR-11356.md records the premise run as base production at `cabedd1d968` with the branch tests kept: ConcurrentUpdateJettySolrClientTest, 12 tests, 1 failure, the failing test reporting bob's document sent under alice's identity at ConcurrentUpdateSolrClientTestBase.java line 328. The base class has 11 test methods at the base; the branch test is the twelfth. On SJ-R1-53's label: the receipt itself records that `git merge-base` against origin/main returns `e044bf20b405` and that `cabedd1d968` is not on main's history, while the branch's commits sit on `cabedd1d968`. The register's replacement called `cabedd1d968` "the merge-base with upstream/main", which that record contradicts, so the applied text names it "the base commit the branch starts from, `cabedd1d968059215188f4e7563fb303241899ed`". The substance of the finding (name the base commit) is applied; the label follows the receipt. This is the one deviation from a register replacement in this lane, recorded here for round 2 to judge.
- **SOLR-12094 (SJ-R1-55 to 58):** at head `8d957a73f4fb`, `testMappedFieldAfterSplitIsRejected` spans lines 250 to 279 (the next method starts at 281), and the unmapped-fields portion spans lines 270 to 278. The test is absent at the base `c3cdf7b46e8`. receipts/SOLR-12094.md records the gate finished 2026-10-05 with JsonLoaderTest 31 of 31, confirming the added date.
- **SOLR-14187 (SJ-R1-64, 66):** `git merge-base` of the branch against origin/main is `cabedd1d968` (the branch has four commits on top of it); `e044bf20b405`, which the draft's links and the receipt named as the merge-base, is an older ancestor. The three cited lines hold identical content at `cabedd1d968` (line 260, lines 1829 to 1832, line 1874), so the links were re-pointed with their anchors unchanged. receipts/SOLR-14187.md records the packaged head gated and pushed on 2026-10-11, confirming the added date. Receipt line 6 is corrected in this push: it now names `cabedd1d968` as the merge-base and records that the proof leg used production at `e044bf20b405`.
- **SOLR-14298 (SJ-R1-68):** receipts/SOLR-14298.md records the gate through the takeover log's 2026-10-06 round 27 entry, confirming "Verified 2026-10-06 at this head."
- **SOLR-14967 (SJ-R1-70, 71):** at head `ee76643f5b3f`, the state-version block in CloudSolrClient runs only for requests that name a collection, are not admin and are not v2 collection requests, and the immutable-params copy that carries the client's version is made only from that computed value. The scoped replacement texts state exactly that reach.
- **SOLR-17866 (SJ-R1-73):** at head `3f9367d796a6`, CloudSolrClient lines 950 to 963 contain the throw "No collection param specified on request and no default collection has been set", as the appended sentence states.
- **SOLR-18341 (SJ-R1-77):** at head `458b098719d7`, the load balancer's retry status set is {404, 403, 503, 500} (LBSolrClient line 130), so the failover list in the draft now includes 403 and 404.

## Held items

- **SJ-R1-45 (SOLR-10364, blocking): HELD** on the packaged head `efb1e6717f9f`, which is local to vm2; the live tip is `502bdbf033fa`. The draft's head citations move when the packaged head is pushed.
- **SJ-R1-48 (SOLR-11356, blocking): HELD** on the packaged head `6120dae28d04a07763f44d4bc5e9d9793af025f6`, local to vm2; the live tip is `8474e5a3a26d`. Same shape as SJ-R1-45.
- **SJ-R1-49 (SOLR-11356, blocking): HELD with the branch.** The recorded owner decision is to copy the headers in the stream key. Until the branch copies them and the proof re-runs at the new head, the draft stays as it is. The register's post-fix texts are recorded here for that pass: the change summary becomes "**Each stream keeps a copy of the credentials and headers of the request that opened it, and only matching requests join it.**", and the live-view Limits bullet is deleted.
- **SJ-R1-51 (SOLR-11356, should-fix): applied now, marked for revision.** The inserted Limits summary ("**The test depends on timing, and two parts of the match are still open: the headers and the user principal.**") is accurate today and the formula requires the summary; the register directs revising it after SJ-R1-49 is fixed, and round 2 carries that check.

## Per-finding dispositions

### SOLR-2018
- Notes only (SJ-R1-10, 11, 12): stand. CONVERGED, unchanged.

### SOLR-3722
- SJ-R1-01, SJ-R1-02 (should-fix): applied (the constructor path sentence; the base-run shape in Proof).
- SJ-R1-13 (note): stands.

### SOLR-3999
- SJ-R1-03 (should-fix): applied (run-shape prefix).
- SJ-R1-14 (note): stands. The packaging title correction is recorded under "Packaging titles" below.

### SOLR-4335
- SJ-R1-04, SJ-R1-05 (should-fix): applied (the #65535 sentence; the run-shape lead-in).
- SJ-R1-06 (should-fix): settled without edit; the 2026-10-06 gate is confirmed in the takeover log (fact checks above).
- Packaging title recorded below.

### SOLR-4336
- SJ-R1-07, SJ-R1-08 (should-fix): applied (the empty-value summary; the run-shape prefix).
- SJ-R1-09 (should-fix): settled without edit, same evidence as SJ-R1-06.
- SJ-R1-15, SJ-R1-16 (notes): stand. Packaging title recorded below.

### SOLR-4422
- SJ-R1-21 (should-fix): applied. The Limits follow-up offer now reads "A follow-up ticket and PR can be opened on request."
- SJ-R1-22 (note): stands.

### SOLR-4424
- Notes only (SJ-R1-23, SJ-R1-32 in part): stand. CONVERGED, unchanged.

### SOLR-5220
- SJ-R1-24 (should-fix): applied (base link with label).
- SJ-R1-25, SJ-R1-26 (notes): stand.
- SJ-R1-79 (note, filed under SOLR-18341 in the register): the SOLR-5220 landing order relative to SOLR-14298 is not mentioned in either draft; it stands as a record item for the openings slate, where the landing order is decided.

### SOLR-6046
- SJ-R1-27 (should-fix): applied (the XML loader sentence).
- SJ-R1-28, SJ-R1-29 (notes): stand. Packaging title recorded below.

### SOLR-7709
- Notes only (SJ-R1-30, SJ-R1-31): stand. CONVERGED, unchanged.

### SOLR-8536
- SJ-R1-41 (blocking), SJ-R1-42, SJ-R1-43 (should-fix): applied (the ASCII narrowing; "unchanged" deleted; "with the new test added" in Proof).
- SJ-R1-44 (note): stands. Packaging title recorded below.

### SOLR-10364
- SJ-R1-45 (blocking): HELD, above.
- SJ-R1-46 (should-fix): applied (the SOLR-4422 interaction bullet).
- SJ-R1-47 (note): stands (EnumSet is also unsupported by the binder; the draft's Limits name Set only).
- Spotted, not edited: the draft's first Limits bullet carries a pre-existing first-person "we" sentence ("We can open a follow-up ticket and pull request for it on request."). Pre-existing text is left alone in this lane; it is flagged for the opening pass.

### SOLR-11356
- SJ-R1-48, SJ-R1-49 (blocking): HELD, above.
- SJ-R1-50 (should-fix): applied (the non-template "Title:" line is deleted).
- SJ-R1-51 (should-fix): applied now, marked for revision after SJ-R1-49 (above).
- SJ-R1-52 (should-fix): applied ("with the new test added" in the base Proof bullet).
- SJ-R1-53 (should-fix): applied with the disclosed label correction (fact checks above).
- SJ-R1-54 (note): stands.

### SOLR-12094
- SJ-R1-55, SJ-R1-56, SJ-R1-57, SJ-R1-58 (should-fix): applied (both test-link spans; "with the new test added"; the JsonLoaderTest date).
- SJ-R1-59 (note): stands; the register confirms the draft states the reader's error status accurately.

### SOLR-14187
- SJ-R1-61 to SJ-R1-66 (should-fix): applied (the Limits summary; "sends no basic-auth user" in both places; the inconclusive-by-construction summary; the 2026-10-11 date; "A separate check"; the three links re-pointed to the merge-base). The receipt correction under SJ-R1-66 is in this push.

### SOLR-14298
- SJ-R1-67, SJ-R1-68, SJ-R1-69 (should-fix): applied ("No SolrJ change is needed." deleted; "Verified 2026-10-06 at this head."; the inconclusive-by-construction wording).
- Spotted, not edited: a pre-existing "we" in the first Limits bullet ("a follow-up that we can open on request"); flagged for the opening pass, same as SOLR-10364.

### SOLR-14967
- SJ-R1-70, SJ-R1-71 (should-fix): applied (both scoped texts).
- SJ-R1-72 (note): stands.

### SOLR-17866
- SJ-R1-73, SJ-R1-74 (should-fix): applied (the no-default-collection failure sentence with its citation; the constructor-proposal replacement from the 2025-08 ticket comments).
- SJ-R1-75 (note): stands. The SOLR-17866 owner flag (submit ahead of the maintainer-owned design conversation) is the owner's call at opening approval; it is not a finding and this lane takes no position on it.

### SOLR-18341
- SJ-R1-76 (blocking): applied. The paragraph now reads: "A separate open pull request, [apache/solr PR 4829](https://github.com/apache/solr/pull/4829), touches the same retry logic."
- SJ-R1-77, SJ-R1-78 (should-fix): applied (403 and 404 in the failover list; "The ticket says admin, security and unspecified requests stay as they are. For the load balancer they do not.").
- SJ-R1-80, SJ-R1-81 (notes): stand.
- Spotted, not edited: pre-existing "we" sentences in the Choices and Limits sections; flagged for the opening pass, same as SOLR-10364.

## Packaging titles (draft-side check)

The five packaging title corrections were checked draft-side: none of the five drafts carries title text (each changelog line is a link), so there is nothing to edit in the drafts. The corrected fragment titles are recorded verbatim here for the packaging lane, from reports/draft-fidelity-streaming-solrj-s2.md and reports/draft-fidelity-streaming-solrj-s3.md:

- SOLR-3999: "SolrDocumentBase, SolrDocument, SolrInputDocument and SolrInputField declare serialVersionUID = 1L, so the version number no longer follows the class shape."
- SOLR-4335: "XML written by SolrJ and Solr no longer contains the illegal characters U+FFFE and U+FFFF; they are escaped like the other illegal control characters."
- SOLR-4336: "A blank numeric request parameter such as start= or rows= is treated as unset instead of causing a bad request error."
- SOLR-6046: "SolrJ's XML update writer expands Object array values of atomic update operations (such as set with a String[]) instead of writing their toString()."
- SOLR-8536: "Thread names set by MDC aware executors no longer contain ASCII control characters, such as line breaks, that come from application MDC values."

Notes SJ-R1-32, 33 and 34 (draft lengths, changelog fragment names, module-check wording) stand as record items across the category.

## Verdicts after application

CONVERGED: SOLR-2018, 3722, 3999, 4335, 4336, 4422, 4424, 5220, 6046, 7709, 8536, 12094, 14187, 14298, 14967, 17866, 18341 (17).
NOT CONVERGED: SOLR-10364 (SJ-R1-45 held), SOLR-11356 (SJ-R1-48 and SJ-R1-49 held; SJ-R1-51 to be revised after SJ-R1-49).

## What round 2 needs to settle

1. Whether the two packaged heads (SOLR-10364 `efb1e6717f9f`, SOLR-11356 `6120dae28d04`) have been pushed; if so, re-check both drafts against the pushed heads.
2. Whether the SOLR-11356 branch now copies the headers in the stream key (SJ-R1-49); if so, check the recorded post-fix texts and the SJ-R1-51 revision.
3. The SJ-R1-53 label deviation: judge the receipt evidence (receipts/SOLR-11356.md records `cabedd1d968` is not on main's history) against the register's prescribed wording.
4. Whether the five packaging titles recorded above have landed on their branches.
5. Confirmation that the applied replacements are present in the live drafts, word for word in substance.

# Review group 2: SOLR-5754, SOLR-5939, SOLR-5941, SOLR-5887

Group 2 of the opened-PR review (assignment `assignments/review-opened-28.md`): PRs #5073, #5075, #5076, #5074. Read-only. Gate state comes from `receipts/` on pr-prepare. No builds and no tests. Draft line numbers refer to `pr-drafts/update-processing/SOLR-<ticket>.md` on pr-prepare. Changelog line numbers refer to the fragment at the PR head.

## SOLR-5754 (PR 5073)

Verdict: FIX FIRST

Findings:

1. PR title (GitHub field, not in the draft). Current: `SOLR-5754: SolrStreamingServers returns a synchronizedList of Errors.` The title names a class that does not exist. The fragment title (`changelog/unreleased/SOLR-5754-streaming-clients-error-snapshot.yml`, line 1, head 46b919e2d4e8) is "Hardening: StreamingSolrClients.getErrors() returns a snapshot copy instead of the live list, and the retry pass takes and clears the errors in one atomic drain under the list's lock". Corrected title: `SOLR-5754: Hardening: StreamingSolrClients.getErrors() returns a snapshot copy instead of the live list, and the retry pass takes and clears the errors in one atomic drain under the list's lock`.

2. Proof, draft line 27: "No run count is recorded for this class." The SOLR-5939 receipt records `StreamingSolrClientsTest` 2/2 in the same combined run (merged tree, 26 of 26). The 5754 receipt lists 21 of the 26 tests, so the 5754 draft leaves out the fifth class. Replace the sentence with: "It passed 2 of 2 in the same combined run, as the SOLR-5939 receipt records."

Notes (no effect on the verdict):
- The draft names the combined head `7fbe0128d8b0` and the merge `1ddbf36202d` with tree `f2e33340f0ea`. `7fbe0128d8b0` is the commit just before the live tip (the receipt's top-up note says the same). A read-only `git merge-tree` of origin/solr-5939-submit with `7fbe0128d8b0` gives tree `f2e33340f0ea`, so the tree claim checks out. `1ddbf36202d` is not in this repository, so that commit id cannot be checked here.
- The branch history holds `SOLR-5754-TESTING.md` (added in 36fe859d5fb, edited in c658904626e, deleted in 46b919e2d4e). The net diff against merge base cabedd1d968 has no such file.
- The body is 3,651 characters, a little over the 3,500 guide.

Checks passed: head 46b919e2d4e8 matches the receipt; headRefName solr-5754-submit; baseRefName main; body byte-identical to the draft; all six blob links use the head SHA and their anchors hold (SolrCmdDistributor L107; StreamingSolrClients L51-52, L61-66, L68-75; test L29-32, L40-73); base claims hold (base SolrCmdDistributor L107 copies the live `clients.getErrors()`, L167 calls `clients.clearErrors()`); no fail-before claim is made, so the cross-PR ruling holds; changelog parses as YAML; net diff is four files; header and footer present; no em dash; no first-person plural.

## SOLR-5939 (PR 5075)

Verdict: FIX FIRST

Findings:

1. PR title (GitHub field). Current: `SOLR-5939: Wrong request potentially on Error from StreamingSolrServer`. Corrected: `SOLR-5939: Distributed update errors from a merged stream are now recorded against each request in the stream instead of the first request sent to the node` (matches `changelog/unreleased/SOLR-5939.yml`, line 1, head f8d4bdbea518).

2. What this change does, draft line 17: "Before, the map kept them until the update request ended." Base cabedd1d968 has no lookup map at all (no `reqByUpdateRequest` in base). The map is new in this change: `solr/core/src/java/org/apache/solr/update/StreamingSolrClients.java` L59-L60 declares it and L78 fills it. Replace the sentence with: "The lookup map is new in this change ([L59-L60](https://github.com/nick-boss-tech/solr/blob/f8d4bdbea518901e255ae119f3e5c43e7804a9bf/solr/core/src/java/org/apache/solr/update/StreamingSolrClients.java#L59-L60)), so there is no earlier behavior to compare."

3. Proof, draft line 36: "of this head with the SOLR-5754 branch." The combined run used SOLR-5754 at a specific head, and the description should name it, since it cites the combined tree. Replace the phrase with: "of this head with SOLR-5754 at `7fbe0128d8b0`. The SOLR-5754 tip `46b919e2d4e8` only removes a root handoff file."

Receipt note for the lead (not a PR edit): `receipts/SOLR-5939.md` says "merged tree 1ddbf36202d". The draft calls `1ddbf36202d` a merge commit whose tree is `f2e33340f0ea`. The read-only `git merge-tree` check supports the draft (tree `f2e33340f0ea`). The receipt should read "merge commit `1ddbf36202d`, tree `f2e33340f0ea`". The same wording appears in `receipts/SOLR-5754.md`.

Notes (no effect on the verdict):
- The focused counts add up: 2 + 1 + 2 + 19 + 2 = 26, matching both receipts.
- The body is 6,990 characters, over the 3,500 guide. This is a big change, so the 2026-10-05 amendment covers the length.

Checks passed: head f8d4bdbea518 matches the receipt; headRefName solr-5939-submit; baseRefName main; body byte-identical to the draft; all blob links use the head SHA; the anchors hold (Jetty L92, JDK L40, base SolrJ L268-273, L675-678, L724-727, SSC L155-169, L180-183, L193-201, Tolerant L238-246, DUP L1267-1282, attribution test L104-152); the three SolrJ additions are the only new public or protected members; base builds the client with the first request (base SSC L78-87); the attribution test expects ids "1" and "2" and two errors, as the description says; the 5754 receipt and 5939 receipt agree on the counts and the pre-fix result (2/2 on base); changelog parses as YAML; net diff is eight files with no TESTING or handoff file; header and footer present; no em dash; no first-person plural.

## SOLR-5941 (PR 5076)

Verdict: FIX FIRST

Findings:

1. PR title (GitHub field). Current: `SOLR-5941: CommitTracker should use the default UpdateProcessingChain for autocommit`. Corrected: `SOLR-5941: Auto commits are now processed by the default update request processor chain, so update processors see them like client requested commits. An auto commit still applies only to the core it was triggered on and is not distributed.` (matches `changelog/unreleased/SOLR-5941.yml`, line 1, head a4df7bfd214b).

2. Proof, draft line 40: "At this head, tidy and Error Prone compile are clean, the changelog parses, and the module check is clean." The receipt records the four focused classes and "module check rc=0" only. Tidy and Error Prone are not in the receipt. Replace the line with: "- At this head, the module check passed (rc=0)."

3. Proof, draft line 41: the receipt's pre-fix result is missing. The receipt says "Pre-fix proof: PASS ... the new test fails on the pre-fix head 62516cc338ef". The same line also narrates internal fixes, which the formula keeps out of the Proof. Replace the line with: "- The new test fails on the pre-fix submit head `62516cc338e` (pre-fix proof PASS, recorded at the audit-fix gate on parent `bf17e860d9ea`). Base `cabedd1d968` was not run."

4. Proof, draft line 48: "Five test classes from the earlier focused run at `62516cc338e` were not re-run at this head: DirectUpdateHandlerTest, MaxSizeAutoCommitTest, TestUpdate, SolrCmdDistributorTest and DistributedUpdateProcessorTest. That gap is accepted, with no re-run." The receipt does not record this list, and nothing I can read records the acceptance. Delete the line. If the lead wants it back, first add the list to `receipts/SOLR-5941.md` from the 2026-10-07 gate log.

5. Proof, draft line 38: the anchor `solr/core/src/test/org/apache/solr/update/AutoCommitUpdateChainTest.java#L71-L84` does not reach the claim. The sentence about the recording processor (one autoCommit, marked as an autoCommit and an end point, the document becomes searchable) cites code at head lines L98-L112. Replace `#L71-L84` with `#L71-L112` in that link.

Notes (no effect on the verdict):
- Limits, draft line 49: "A follow-up ticket and PR can cover it on request." This is an offer on request, which the formula allows. It only needs a change if the roll-up reads "no new Jira ticket" strictly.
- The body is 6,419 characters, over the guide. The reference-guide paragraph (`commits-transaction-logs.adoc`) is in the diff but the body does not name it under What this change does. Optional.

Checks passed: head a4df7bfd214b matches the receipt; headRefName solr-5941-submit; baseRefName main; body byte-identical to the draft; the other blob links (CommitTracker L278-280, L288, L297-309; DZUP L158-163; RAUP L92-96; DUP L1166-1192 and L1174-1185; CommitThroughNonLeaderTest L77; IgnoreCommitOptimizeUpdateProcessorFactory L125) use the head SHA and land on the described code; base claims hold (base CommitTracker L280 calls `core.getUpdateHandler().commit(command)` directly; base DZUP L202-250 does not forward or commit a non-leader's request with `commit_end_point=true`); `getBool` throws BAD_REQUEST on "leaders" and "replicas" (SolrParams L186-188, StrUtils parseBool); the test counts 2/2, 1/1, 1/1, 1/1 and the 2026-10-09 date match the receipt; the choice section names real alternatives; header and footer present; changelog parses; net diff is eight files (four core Java files, one test, one test resource, one reference-guide page, one changelog) with no TESTING or handoff file; no em dash; no first-person plural.

## SOLR-5887 (PR 5074)

Verdict: FIX FIRST

Findings:

1. PR title (GitHub field). Current: `SOLR-5887: Document exception don't give core information`. Corrected: `SOLR-5887: Errors for a bad document in an update (such as an unknown field) now name the core that rejected it.` (matches `changelog/unreleased/SOLR-5887-doc-error-core-name.yml`, line 2, head c4c57ef7bcbd; line 1 is a comment).

2. What this change does, draft line 15: "Every path that builds a Lucene document from an add command now goes through a wrapper, `toLuceneDocument`." This is not true. `solr/core/src/java/org/apache/solr/update/processor/ClassificationUpdateProcessor.java` L128 calls `DocumentBuilder.toDocument(doc, cmd.getReq().getSchema(), false, true)` on the add command's document, outside the wrapper, so its bad-document errors keep the old text. Replace the first sentence with: "The add paths in `AddUpdateCommand` now go through a wrapper, `toLuceneDocument`." Keep the existing citation to `AddUpdateCommand.java#L235-L248`. After the sentence about nested documents, add: "The classification processor calls `DocumentBuilder` directly ([ClassificationUpdateProcessor.java](https://github.com/nick-boss-tech/solr/blob/c4c57ef7bcbde765807bf02f9e3a6db605868b7e/solr/core/src/java/org/apache/solr/update/processor/ClassificationUpdateProcessor.java#L128)), so its bad-document errors keep the old text."

   Also add to Limits (the formula asks for named gaps): "- The classification processor keeps the old text. Not tested here."

   This is a description fix. Wrapping the classification call in code would be a branch change that needs a new gate, and it is not required to make the description true.

3. Limits, draft line 31: "These are `TolerantUpdateProcessorTest`, `DocumentBuilderTest`, `TestManagedSchema`, and `TestSolrJErrorHandling`." followed by "None of them was run for this change." `DocumentBuilderTest` was run: 17 of 17 at this head (receipt, and the Proof section). Replace the list with: "These are `TolerantUpdateProcessorTest`, `TestManagedSchema`, and `TestSolrJErrorHandling`."

Notes (no effect on the verdict):
- The branch forks from `b6b2b8f10e98`, two commits behind the `cabedd1d968` base that the other three branches use. The PR diff is still the three files.
- `gh pr view` reports `mergeStateStatus` UNSTABLE for this PR (and for 5073, 5075 and 5076 as well). MERGEABLE is not validation. This is not a gate item here.

Checks passed: head c4c57ef7bcbd matches the receipt; headRefName solr-5887-submit; baseRefName main; body byte-identical to the draft; blob links use the head SHA; anchors hold (DocumentBuilder L301-305, where the text at L304 starts "ERROR: " in base too; AddUpdateCommand L103, L212, L231, L235-248; DocumentBuilderTest L78-92); the "no longer starts with ERROR:" statement holds; the TolerantUpdateProcessorTest `contains` checks (L243, L275) still match after the prefix; the 17/17 result and the base "exactly one failure" match the receipt; changelog parses as YAML; net diff is three files; header and footer present; no em dash; no first-person plural.

## Group summary

- Verdicts: SOLR-5754 FIX FIRST; SOLR-5939 FIX FIRST; SOLR-5941 FIX FIRST; SOLR-5887 FIX FIRST. No owner calls.
- The most common fix is the PR title, which does not match the fragment title on all four PRs.
- Cross-PR rulings: the 5754 and 5939 combined gate is cited consistently (26 of 26, merged tree). The 5754 description claims no fail-before run.
- Rule question for the lead, not counted in any verdict: check 3 asks for "a stated plan to submit a follow-up" for known gaps in Limits. Several gaps have no follow-up line: 5754 (concurrent window not exercised), 5939 (no test for the tolerant path; memory not measured), 5887 (existing tests not run). The formula text only asks that gaps be named. The lead should decide which reading applies.

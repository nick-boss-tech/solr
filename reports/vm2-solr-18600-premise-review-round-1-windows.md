# SOLR-18600 premise review, round 1 (Windows)

Status: REVIEW COMPLETE. Current restoration branch NEEDS WORK; do not open it as a compatibility fix at this head. Recommend a 10.1 upgrade warning, with restoration pursued only after preserving the 10.1 surface as well as the 10.0 surface. No source patch, runtime execution, PR, public comment, or mailing-list post in this review.

## Reviewed revision and scope

| Item | Evidence |
| --- | --- |
| Review date / host | 2026-10-29, America/New_York; Windows Codex desktop |
| Assignment | assignments/vm2-solr-18600-premise-review-round-1.md |
| Initial board snapshot | a6af3056725e2a184303d8095b876ba6b5a923cd |
| Publication preflight snapshot | 2274b9f04c85c284bde3db035f000279aa2b64df; assignment and receipt unchanged, no competing review claim found |
| Claim | claims/vm2-solr-18600-premise-review-round-1-windows.md, posted at 8d30cc1775d6eeb9f61cb07b7f43d51b5febd1ea |
| Reviewed and remotely verified head | c40665af8d6bd23c85fef565f93de5004a7b2ca4, solr-18600-http-abi-restore-submit |
| Actual patch base | a336bf871b2c, branch_10x; one restoration commit |
| Release source controls | releases/solr/10.0.0 and releases/solr/10.1.0 |
| Current main observed | 3db71000514e1edaa8e6ae0426fce92ca97990e3; no port or merge trial performed |
| Diff | 16 files, 1067 additions, 601 deletions; diff --check clean |
| Gate record | receipts/SOLR-18600.md at the initial snapshot; receipt commit 86975eea718c8cc32f4b2bf90b0280dc610f6a16 |

Read the production delta, both restored classes, both new compatibility tests, the predecessor design, the binary-attribution receipt, live SOLR-18600 and SOLR-17968, and PR 3829's body, conversation/reviews and inline comments. The branch helper compared against main and printed a large cross-branch diff; that output is not the patch scope. All assessments here use a336bf871b2c..c40665af8d6b.

Raw vm2 logs and gates/logs/vm2/solr18600-pr/body.md are unavailable on this Windows host. The local probe directory does not exist. Recorded runs below are attributed to their board records; none was independently rerun or checked against raw XML here. The vm2 scratch clone was not touched.

## What is happening

The public example uses ordinary HttpJdkSolrClient.Builder methods, without naming a base class. In 10.0 their generic return type erases to HttpSolrClientBuilderBase. The resulting invocation descriptor includes that return type. In 10.1 the shared builder was moved into HttpSolrClient.BuilderBase, changing the erased descriptor. The JVM does not reinterpret an old invocation using the new source hierarchy. Recompilation emits a different descriptor and therefore repairs this particular example.

The Jackson work in SOLR-18325 exposed the break; it did not cause it. The earlier attribution receipt tests StableConsumer.class (SHA-256 beginning 027d21fd), whereas the smaller SOLR-18600 reproducer is Consumer.class (a3bb85e5). They demonstrate the same missing builder descriptor with different fixtures.

Two handoff corrections matter:

- The statement that nothing has been posted to JIRA is inaccurate if read literally. Live [SOLR-18600](https://issues.apache.org/jira/browse/SOLR-18600) is Open, created October 27 at 22:10:41 UTC and updated at 22:45:40 UTC. Its description already includes the fork fix and verification claims; it has no comments. No PR matching SOLR-18600 was returned by the GitHub PR search. This review makes no claim to have exhaustively searched mailing-list traffic.
- CloudSolrClient.java IS changed, contrary to one sentence in the assignment. SolrRequest.java gains a real overload and delegation, beyond a Javadoc edit. CloudSolrClientCacheTest.java is an existing test with a signature adjustment, not a new test class.

## Findings

### F1 (HIGH): the core restoration drops 10.1 fluent-builder descriptors

Released 10.1 HttpSolrClient.java declares BuilderBase with B bounded by BuilderBase (line 447), and withConnectionTimeout returning B (line 588). Its erased return is HttpSolrClient$BuilderBase. HttpJdkSolrClient.Builder inherits that method. At the candidate, [HttpSolrClient.BuilderBase](https://github.com/nick-boss-tech/solr/blob/c40665af8d6bd23c85fef565f93de5004a7b2ca4/solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java#L129) declares only build(). The inherited timeout method now comes from [HttpSolrClientBuilderBase line 177](https://github.com/nick-boss-tech/solr/blob/c40665af8d6bd23c85fef565f93de5004a7b2ca4/solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClientBuilderBase.java#L177), whose B erases to the restored top-level class.

Thus the patch reconstructs the 10.0 timeout descriptor while removing the 10.1 timeout descriptor. Leaving the nested class name present does not preserve its former fluent method descriptors. The same issue applies to other moved B-returning methods such as withRequestTimeout and withBaseSolrUrl. An unchanged copy of the ticket's source, compiled against 10.1 rather than 10.0, is the missing control. Source inspection predicts NoSuchMethodError against the candidate; this review has not executed that prediction.

Both new descriptor tests explicitly expect the old return type, so they can pass while this problem remains. They do not ask whether the 10.1 descriptor also resolves. The nested class needs suitable forwarding/covariant overrides or another demonstrated mechanism that retains both sets. build() retaining a bridge does not establish that every fluent method retains one.

### F2 (HIGH): surrounding restorations replace released 10.1 APIs

Released 10.1 declares CloudSolrClient.getHttpClient() and CloudHttp2SolrClient.getHttpClient() returning HttpSolrClient. The candidate replaces both with HttpSolrClientBase ([CloudSolrClient line 229](https://github.com/nick-boss-tech/solr/blob/c40665af8d6bd23c85fef565f93de5004a7b2ca4/solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java#L229), [CloudHttp2SolrClient line 106](https://github.com/nick-boss-tech/solr/blob/c40665af8d6bd23c85fef565f93de5004a7b2ca4/solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudHttp2SolrClient.java#L106)). Neither class retains a declaration returning HttpSolrClient. A 10.1 binary invoking that descriptor loses its target. Recompiled source such as `HttpSolrClient transport = cloud.getHttpClient();` also stops compiling.

The change of LBSolrClient.Builder's C bound changes the constructor's erased parameter as well as its getter's return. ConcurrentUpdateBaseSolrClient.Builder replaces its HttpSolrClient-parameter constructors (candidate lines 744 and 768), and replaces getClient's return type (886). Similar erased-type changes affect HttpClusterStateProvider. These are additional surfaces to inventory, not evidence that every affected client was run. Cloud's additive withHttpClient and withHttpClientBuilder overloads retain the new signatures; that care is not consistently applied elsewhere.

The candidate also removes 10.1's protected HttpSolrClient.baseUrl field (release line 91), retaining only serverBaseUrl on the restored superclass. A precompiled subclass reading baseUrl would lose that field. Protected APIs have an explicit unstable warning, so their policy weight differs from the ordinary builder example, but their removal still limits the claimed restoration.

The earlier design acknowledges some newer-source return-type costs but does not settle their binary cost. Adding a differently named getter alone cannot repair an already compiled call. A compatible hierarchy/bridge design must be demonstrated, or the incompatibility must be deliberately accepted and disclosed by maintainers. Java's [binary compatibility rules, sections 13.4.5, 13.4.14 and 13.4.15](https://docs.oracle.com/javase/specs/jls/se17/html/jls-13.html#jls-13.4.15) explain why erasure, parameter and result changes matter.

### F3 (MEDIUM): the release note promises more than the proof establishes

The added 10.2 note says precompiled applications work again without recompiling. The receipt establishes one old consumer and selected descriptor checks, explicitly excludes whole-artifact japicmp closure, and does not establish 10.1-consumer compatibility. The predecessor design also lists unrelated outstanding ABI changes outside this HTTP scope. Limit the claim to the exact HTTP-client cases proved. Add a 10.1 warning even if a later release restores this surface; a 10.2 fix cannot change a published 10.1 jar.

## Evidence audit (assignment items 1-8)

| Evidence | What it establishes and its limit |
| --- | --- |
| 1: released 10.0 consumer on 10.1; recompile control | Strong recorded evidence of a user-facing binary break in ordinary source. It does not by itself choose a repair or prove a general policy. Live ticket records hashes and exact failure. |
| 2: a336bf871b2c control | Recorded same failure on the actual patch base. No raw log inspected. |
| 3: pre-Jackson control | Earlier receipt records released PASS, base FAIL and Jackson candidate FAIL with unchanged StableConsumer bytes. Independently checked the relevant source diff: no solrj/solrj-jetty/solrj-zookeeper changes from f45b38d5f6c0 to a336bf871b2c; only the six-line Otel core change in the searched core tree. Attribution is well supported. |
| 4: builder-only restore fails compilation | Consistent with the released C bound and API referencing HttpSolrClientBase. A dependency result, not a policy argument. |
| 5: restoring class names alone still fails | Consistent with erased descriptors and the need for hierarchy integration. The probe-only Javadoc removal is not a production patch. |
| 6: core four and all ten both pass HttpJdk consumer | A sufficiency test for one 10.0 consumer. It neither proves every one of the four necessary nor constitutes a full history bisect identifying the introducing commit. It says nothing about 10.1 consumers. |
| 7: two Cloud builder probes | Supports restoring the two old Cloud builder parameter signatures. It does not prove the getter/field/constructor changes or the entire remaining six-file set necessary for that fixture. |
| 8: wider API inventory | Provides source/descriptor reasons to inspect Cloud and LB, beyond the ticket reproducer. The short list is not a complete ABI inventory, as generic bounds, protected members and constructors also matter. No old LB runtime fixture or getHttpClient caller is recorded. |

Receipt-attributed checks at c40665af8d6b: YAML parse, clean tidy, Error Prone compilation including core main/test and zookeeper, solrj and solrj-jetty check excluding tests. It lists compatibility tests 4/4 and 2/2, JDK 40/40, Jetty 49/49, Cloud builder 14/14, Cloud cache 6/6, ConcurrentUpdate JDK 11/11, and green LB suites without counts. Do not invent a total including LB. The receipt commit is dated October 27; it is not an independently established execution timestamp. The old-consumer control is the fail-before proof; the new Java compatibility test cannot compile on a base missing the types it imports.

## Premises P1-P7

| Premise | Verdict |
| --- | --- |
| P1: users are affected | SUPPORTED for the ordinary builder example. This is a runtime replacement failure before HTTP, rather than a server/wire-protocol failure. |
| P2: restoration is the right fix | NOT ESTABLISHED. Documentation satisfies an explicit ticket option. Restoration is defensible only with an agreed supported surface and without accidentally breaking 10.1 callers. Current head fails that review. |
| P3: removal was unintended | REJECT as stated. Renaming the base and nesting the builder were explicit changes in SOLR-17968. No explicit acceptance/rejection of the precompiled-builder consequence was found in the inspected discussion. Silence cannot establish intent. PR 3829 also discusses upgrade documentation and user impact; it is broader than names/packages alone. |
| P4: restored internal class is safe | CONDITIONAL. Public visibility and an internal Javadoc tag can coexist. Restoring a compatibility superclass is technically possible; the tag does not establish safety or require restoration. The 10.0 builder base lacks the class-level internal tag, and ordinary source inherits its erased descriptors. F1/F2 are concrete safety concerns independent of that policy debate. |
| P5: all ten files are needed | NOT PROVED by the runtime probes. Each family has a concrete descriptor or integration purpose (below); accepting all changes unchanged is unwarranted. Four-file versus ten-file success is not necessity of each file. |
| P6: Nick's binary requirement is upstream policy | NOT ESTABLISHED. The SOLR-18325 requirement does not bind Solr maintainers. Searches of the relevant development/upgrade documentation, current ticket and PR 3829 found no decision settling this exact 10.x library-binary promise. General client/server compatibility is a different question. This is a bounded search result, not a claim that no policy exists anywhere. |
| P7: branch_10x first | PRACTICAL CANDIDATE, not an established project decision. That is where the tested patch exists. A main port needs separate review/proof; the assignment's old main-conflict observation is not a current merge result. No 10.1.x backport is authorized or selected by this review. |

## Scope, architecture, maintenance and reuse

There are TEN production files under solrj plus ONE production adaptation under solrj-zookeeper, not ten production files total.

| Family / file | Reason to consider it; boundary |
| --- | --- |
| HttpSolrClientBase, HttpSolrClientBuilderBase, HttpSolrClient | Reconstruct old supertypes and erased fluent calls while retaining the newer API. Shared implementation is moved instead of duplicated, but F1 prevents accepting the current hierarchy. |
| SolrRequest | A genuine additive processWithBaseUrl(HttpSolrClientBase,...) overload; current HttpSolrClient overload delegates. Broader than necessary for the ticket's builder-only consumer. |
| CloudSolrClient | Old builder parameter overloads plus return/field/protected-hook restoration. Only the parameter-overload need has the recorded Cloud probes. F2 applies to the larger restoration. |
| CloudHttp2SolrClient | Getter and storage type follow the old Cloud surface; getter needs its own old and new binary callers. |
| HttpClusterStateProvider | Generic erasure and construction of a client from a supplied base. Changes to builder().withBaseSolrUrl(...) also warrant behavior/subclass coverage. |
| LBSolrClient | Bound changes constructor/getter erasure; instanceof expansion admits restored-base-only clients on the requestWithBaseUrl route. Needs an actual old LB builder/dispatch fixture. |
| LBAsyncSolrClient | Protected stored transport and getClient return descriptor. Include only against a declared extension compatibility requirement, with descriptor proof. |
| ConcurrentUpdateBaseSolrClient | Builder constructors, stored client and getter. Needs both release-era consumers; unrelated doSendUpdateStream ABI work stays excluded. |
| SolrClientNodeStateProvider (zookeeper) | In-repo adaptation to Cloud's widened return type. It follows the API design chosen rather than proving that design necessary. |

Architecture assessment: reading the JDK builder alone was insufficient; the Cloud builder/getter and LB/provider consumers expose the consequences of the shared abstraction. Compatibility should be a bounded adapter around current behavior. One further behavior delta merits explicit retention or a test: release 10.1 getRequestUrl calls virtual getBaseURL(), whereas restored HttpSolrClientBase line 123 reads serverBaseUrl directly. A subclass overriding getBaseURL can observe different routing; no runtime probe or established external consumer was checked here.

Maintenance/reuse assessment: the new tests use SolrTestCase and a small nested subclass, without a new server/configset. A base-tree search in the two SolrJ test directories found no existing BinaryCompat/descriptor-reflection helper matching the searched patterns. The fixtures are reasonable for local descriptor pins, but freshly compiled fixtures cannot replace bytecode retained from each release. Donor resource ownership is exercised, although no raw XML was available to audit the result. No full public/protected closure or 10.1 preservation suite is present.

Security assessment: no new endpoint, permission path or server trust boundary identified in the reviewed change. Existing HTTP response/auth helpers are moved. This is a source review of compatibility and routing, not a security audit or runtime validation of all request behavior.

## Answers to the six questions

1. **Document now; restoration remains conditional.** Recommend the 10.1 recompilation/migration warning regardless of a later restoration. Hold this restoration head. If maintainers want restoration, repair it to preserve both release surfaces and prove that before offering it as compatible.
2. **An internal superclass can be restored, with explicit limits.** Keep the distinction between compatibility support and a recommended new extension API. The internal tag alone neither rejects the ordinary-builder complaint nor approves this implementation.
3. **Do not select scope by file count.** For a narrow builder repair, start with the demonstrated JDK/Jetty hierarchy and preserve 10.1 descriptors. For HTTP-family restoration, retain the necessary Cloud/LB/ConcurrentUpdate adaptations after a member-by-member two-release inventory. Existing probes cannot justify accepting all ten unchanged.
4. **No settling maintainer decision was found.** State that the rename was deliberate and the ordinary-builder binary consequence remains an open contract question. Avoid asserting a universal upstream 10.x binary guarantee from a local prerequisite.
5. **A reviewed main port plus a requested 10.x backport is the normal proposal to discuss.** Existing proof is only for branch_10x. Keep that branch as an experimental candidate while deciding the API. Let maintainers choose the contribution target and whether any 10.1.x maintenance release should include it. Do not silently treat a 10.2 build as a fix installed into 10.1.0.
6. **Use the existing ticket first for this issue-specific decision.** Recommend a short update offering the warning and asking whether a dual-release-compatible restoration is wanted. A PR choice section can record a remaining maintainer choice once code is ready. Escalate to dev-list if maintainers want a broader policy discussion; no need to broadcast three parallel requests. No public draft or post was made here.

Decision ladder: rungs 1-3 settle mechanism, attribution and the ticket's two valid routes; rung 4 selects documentation plus conditional restoration as this review's recommendation. Upstream policy and release selection remain maintainer choices. Public posting remains owner-authorized. No owner answer is needed to complete the review itself.

## Linux follow-up specification

The next runtime work belongs to a capable Linux host, preferably vm2 with the existing artifacts. This is a review handoff, not a new gate launch or authorization to reset a dirty clone. Preserve any needed scratch edits before vm2's owner resets or replaces it.

- Freeze consumers compiled once against released 10.0.0 and once against released 10.1.0, with source, complete compile/runtime classpaths and class hashes. Run each first on its own released jars as a positive control, then unchanged on c40665af8d6b. The 10.0-on-10.1 leg remains the known original negative control.
- First discriminator: compile the ticket's ordinary JDK timeout-builder source against 10.1.0 and run on c40665af8d6b. Record the exact missing descriptor predicted by F1. Include the corresponding Jetty builder case.
- Cover 10.1 callers of CloudSolrClient and CloudHttp2SolrClient getHttpClient, LB builder constructor/getter, provider constructor/getter and ConcurrentUpdate builder constructors/getter. Include 10.0 callers for the same promised surfaces. An API inventory must distinguish directly declared members, inherited members, bridges and protected extensions.
- Use javap or equivalent descriptor resolution plus public/protected API comparison against BOTH releases; saving reflection tests alone is insufficient. Include source assignment to the newer HttpSolrClient return type as a source control. Cover baseUrl extensions only if they are in the declared support boundary.
- For any repaired new head, push and remotely verify it, then obtain a fresh exact-head Linux gate with tidy, named focused suites, relevant module checks and the unchanged binary matrix. Do not reuse c40665af8d6b's green receipt for a new implementation.

Disposition: the real reported break is supported, the source review is complete, and the current patch is not ready for a compatibility-restoration PR. No implementation changes were made or promised by this review. The bounded next steps are a release-note contribution and a dual-release compatibility investigation, with any public offering awaiting Nick's authorization.

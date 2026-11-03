# PR description draft: SOLR-18600 restoration plus binary compatibility check (not posted)

Title: SOLR-18600: Restore SolrJ HTTP client binary compatibility and add a binary-compat check to the build

---

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18600

**What happens today**

**SolrJ 10.1.0 broke binary compatibility with 10.0.0, and nothing in the build would catch the next break of the same kind.** The 10.1.0 release removed the top-level `HttpSolrClientBase` and `HttpSolrClientBuilderBase` classes and narrowed the declared types built on them, so code compiled against 10.0.0 fails at runtime with `NoSuchMethodError` on ordinary builder calls. The restoration half of this PR is the fix recorded on the ticket. The second half is a guard: the project has no automated check that compares the SolrJ API surface against the previous release, so a removal like this one can ship again without any build failing.

**What this change does**

**It restores the 10.0.0 SolrJ HTTP client surface, and it adds a japicmp check to `:solr:solrj:check` that fails on binary-incompatible changes the previous release did not have.** The restoration brings back [`HttpSolrClientBase`](https://github.com/nick-boss-tech/solr/blob/83cf27ac11d2e0f44762fbf594e8e42c1c7ffc2c/solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClientBase.java) and `HttpSolrClientBuilderBase` as top-level classes and returns the declared types of the client cluster (`CloudSolrClient`, `HttpClusterStateProvider`, the LB clients, `SolrRequest.processWithBaseUrl`) to their 10.0.0 shapes. The check lives in [`gradle/validation/binary-compat.gradle`](https://github.com/nick-boss-tech/solr/blob/83cf27ac11d2e0f44762fbf594e8e42c1c7ffc2c/gradle/validation/binary-compat.gradle): it compares the built solrj and solr-api jars (solrj bundles solr-api classes, so the pair is the surface users compile against) with the same pair from the previous release, resolved from Maven Central, and reports binary-incompatible changes on the public and protected surface. Intentional breaks are excused one by one in [`solr/solrj/binary-compat-exclusions.txt`](https://github.com/nick-boss-tech/solr/blob/83cf27ac11d2e0f44762fbf594e8e42c1c7ffc2c/solr/solrj/binary-compat-exclusions.txt), each entry naming its ticket; an entry that matches no current change draws a warning so the list cannot rot silently.

**Proof**

**Run against the published 10.1.0 jars with the baseline set to 10.0.0, the new check fails and names the exact classes whose removal broke 10.0.0 users; at this PR's head it passes.** The 10.1.0 comparison lists 480 binary-incompatible changes, including `CLASS_REMOVED` for both `HttpSolrClientBase` and `HttpSolrClientBuilderBase`. At the head, with the baseline property pointed at 10.0.0 for the same comparison, none of the reported changes belongs to the cluster this ticket restores. On the default baseline (10.1.0), the head reports 50 changes and every one is excused by an exclusions entry: the restoration's own descriptors (against 10.1.0, restoring the 10.0.0 types reads as change, because 10.1.0's narrowed types were the break), the ApiMapping removal from PR #5242, and single changes from SOLR-18450, SOLR-15823 and SOLR-18351.

<details>
<summary>Linux verification at head 83cf27ac11</summary>

- Tidy: clean.
- Error Prone compiles: `:solr:solrj`, `:solr:solrj-jetty`, `:solr:solrj-zookeeper`, `:solr:core` main and test sources, all green with `-Pvalidation.errorprone=true`.
- Tests at seed 18600C0FFEE18600: HttpSolrClientBinaryCompatTest 4/4, HttpJettySolrClientBinaryCompatTest 2/2, HttpJdkSolrClientTest 40/40, HttpJettySolrClientTest 52/52, CloudHttp2SolrClientBuilderTest 14/14, CloudSolrClientCacheTest 6/6, ConcurrentUpdateJdkSolrClientTest 11/11, LBSolrClientTest 8/8, LBSolrClientRetryUnsentTest 3/3.
- `:solr:solrj:check -x test` and `:solr:solrj-jetty:check -x test` green; the solrj check includes the new `checkBinaryCompat` task. Root `check -x test` green in a normal clone at the same head. One environment note: the first clone run failed only at `:solr:webapp:verifyUiWasm` because the local download relay delivered a corrupted UI zip; the byte-correct zip was seeded from a direct download and the rerun passed.
- The check was also run in its published-jar mode (`-Psolrj.binaryCompat.candidateJar` / `candidateApiJar`) against the 10.1.0 artifacts with the baseline at 10.0.0: it fails, naming the removed base classes, which is the behaviour that would have stopped the 10.1.0 break before release.

</details>

**A choice to check**

**The baseline defaults to the previous release on the branch line (10.1.0), and I would like a reviewer's eye on that default.** Comparing against 10.0.0 instead would also flag the breaks that already shipped in 10.1.0 (167 further changes across the line's API, none of them in this ticket's cluster); those cannot be unshipped, and listing them as exclusions would bury new breaks under old ones. The baseline is a Gradle property (`solrj.binaryCompat.baselineVersion`), so pointing the check at 10.0.0 for an audit run takes one flag, and the default needs a one-line bump at each release.

**Limits**

The check covers the solrj and solr-api jars; solrj-jetty and solrj-zookeeper are not wired in yet. It enforces binary compatibility; source-incompatible shapes that stay binary compatible (an added abstract method on a public class, a changed constant value) are not flagged in this first version. One 10.0.0 member is deliberately not restored: `CloudSolrClient$Builder.withInternalClientBuilder`, an internal builder hook that 10.1.0 removed and that the restoration leaves out. The restoration targets branch_10x; a port to main is a separate step.

*Nick Shanin directed the work and takes responsibility for this contribution.*

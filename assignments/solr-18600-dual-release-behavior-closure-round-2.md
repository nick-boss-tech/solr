# SOLR-18600: close behavioral gaps before promoting the dual-release prototype, round 2

Status: OPEN. Linux behavioral/prototype work; production branch remains HOLD.

- Tags: premise-run, gate, review, implementation, records.
- Intended host: any capable Linux gate host; vm6 preferred for reuse of its verified artifacts. No exclusive reservation. Windows can review source; this assignment's next required step is Linux behavioral proof.
- Context: Nick's direction is compatibility for callers compiled against BOTH 10.0.0 and 10.1.0, with proof before a public offer. The round-1 design receipt is complete; it is not a production gate receipt.
- Read snapshot: 0f19ef310d02b47cc5da72be21462c3728ebf7a0, receipt receipts/solr-18600-dual-release-abi-premise-round-1.md, report reports/solr-18600-dual-release-abi-premise-linux-round-1.md and artifacts reports/solr-18600-dual-release-abi-premise-round-1-artifacts/.
- Pinned fork: solr-18600-http-abi-restore-submit at c40665af8d6bd23c85fef565f93de5004a7b2ca4 (remote separately verified during this review).
- Pinned base: a336bf871b2c2b67abe55808966051d1c553b579. Prototype input: artifact prototype/prototype-vs-c40665a.diff on the fork pin, not a different live branch_10x head.
- Released semantic control: 10.0.0 source 6c6c48a6f78486130682ea9c1f7a2723af5a87be. Reuse the hashed published release jars and unchanged compiled cohort classes from round 1 after verifying identity. New fixtures must be separately compiled against the proper release and kept immutable across their runtime legs.
- Outputs: reports/solr-18600-dual-release-behavior-closure-linux-round-2.md, receipts/solr-18600-dual-release-behavior-closure-round-2.md, and a portable artifact directory with exact diffs, sources, hashes, full logs and fresh XML. Receipt is diagnostic/prototype evidence, not a production PASS.

## Review corrections and recommendation

The dual-descriptor prototype establishes a credible linkage repair path. Keep its existing linkage evidence; do not rerun the whole inventory merely to recreate it. Three claims need stronger treatment before implementing a production candidate.

1. Do not promote the proposed no-op connect restoration as the default compatibility fix. The 10.0 CloudSolrClient.java:664-706 documents eager initialization and implements retry, timeout and interruption. Its deprecation points at ClusterStateProvider.getLiveNodes(). The 10.0 BaseHttpClusterStateProvider.java:92-94 connect body literally calls getLiveNodes(); ZkClientClusterStateProvider.java:214-217 forces getZkStateReader(). On the pinned fork, getLiveNodes() still obtains getZkStateReader() (lines 92-97) and that accessor checks closure and initializes lazily (150 onward). This is a concrete behavior-preserving candidate to investigate, rather than a choice limited to no-op or removal. It is NOT yet proved equivalent for every provider.
2. The cast risk affects legacy calls too. An old getter descriptor synthesized as a bridge invokes the narrowed getter body; it does not bypass that body's cast. Also inspect the legacy HttpClusterStateProvider and LBSolrClient.Builder constructors in the prototype, which narrow HttpSolrClientBase to HttpSolrClient. Do not describe this solely as a risk to a new 10.1 call path.
3. The statement that all protected gaps cannot be expressed in Java is too broad. The unresolved 10.1 list includes createHttpClusterStateProvider(HttpSolrClient) and createOrGetLbClient(HttpSolrClient), whose changed parameter types are candidates for ordinary overloads. HttpSolrClient.baseUrl and the restored serverBaseUrl have DIFFERENT names, so the duplicate-field-name objection does not settle this case. Other return/field changes may require inheritance, compiler bridges or a justified exclusion. Review each member separately; no claim here that all protected extension contracts can be preserved. Java's covariance and field-inheritance rules are background, not a substitute for a released-subclass fixture: https://docs.oracle.com/javase/specs/jls/se17/html/jls-8.html#jls-8.4.8.3 and https://docs.oracle.com/javase/specs/jls/se17/html/jls-13.html#jls-13.4.8.

## Bounded proof legs

Claim before executing. Refresh current claims, receipt and fork head; record any movement rather than silently substituting an input. Follow hosts/pool-linux.md, detached preflight, host build lock and disk/inode guards. Use an isolated full checkout; do not reset another task's scratch tree. Record toolchain, runner, UTC timestamps, exact commands/status, seed and log paths. Preserve existing round-1 artifacts.

### A. connect initialization and waiting

Prototype deprecated CloudSolrClient connect entry points with the old blocking contract. Investigate a deprecated default ClusterStateProvider.connect() delegating to getLiveNodes(), so 10.1 provider implementations need not add a new abstract method and pre-existing custom connect overrides can still dispatch. Retain the Cloud method's provider delegation and the old timed retry shape where appropriate. This is a candidate to test, not permission to introduce a new readiness definition such as requiring a nonempty live-node set.

Use existing provider/mock and embedded ZooKeeper test infrastructure; no external service is required for deterministic retry tests. Prove successful eager initialization, transient provider failure followed by success, exhausted timeout, interruption, and closed-provider behavior. Retain old timeout/exception declarations and check default behavior for a 10.1-shaped provider without connect plus dispatch to a 10.0-shaped provider with a connect override. Add an old-compiled runtime fixture for both Cloud descriptors and the provider descriptor if restoring it; the round-1 matrix has no connect row.

Do not silently replace the waiting contract with a documented no-op. If the existing lifecycle truly prevents preservation, supply the failing control and exact obstacle before escalating; the earlier owner question's no-op recommendation has not been approved by Nick.

### B. custom-client behavior and ownership

Use a real minimal 10.0-compiled HttpSolrClientBase subclass, not an uninitialized fabricated object. Exercise the legacy constructors/getters for ConcurrentUpdate, Cloud, HttpClusterStateProvider and LB as applicable. Determine whether the original object can be returned and used without narrowing-cast failures. Verify shared-client ownership/closure with existing test infrastructure. The same old fixture must pass its 10.0 control before attributing candidate failures.

Where the prototype fails, try a justified design that preserves both descriptors and actual old-call behavior; record the exact source/bytecode change. A transport adapter must preserve identity/ownership expectations or document the precise unavoidable difference. Do not hide the issue by recompiling the consumer, accepting ClassCastException as a pass or silently excluding a public constructor/getter path. If no viable design is found, return a specific constraint and failed alternatives, not a release-ready compatibility claim.

### C. URL dispatch and protected closure

The pinned base's HttpSolrClient.getRequestUrl calls virtual getBaseURL() (lines 140-142); the restoration's HttpSolrClientBase.getRequestUrl reads serverBaseUrl (121-123). The 10.0 base used the field (113-115). Use release-compiled overriding subclasses as controls to document both behaviors. Investigate retaining field semantics in the legacy base and virtual dispatch in the newer HttpSolrClient layer, rather than flattening both into one implementation. Assert the actual destination against a disposable local endpoint, not just a method's existence.

Reassess the remaining protected member list individually with old/new compiled subclasses. Demonstrate ordinary overloads, compatible covariant bridges or inherited fields where feasible. For mutable duplicate state, prove synchronization/dispatch behavior; descriptor presence alone is inadequate. Any remaining exclusion must name the member, the released consumer it affects, the compiler/linker obstacle and why tested alternatives fail. Do not present the blanket Java-impossibility claim as established evidence.

### D. requests and deliverable

After resolving the above in the prototype, exercise successful local HTTP requests (including requestWithBaseUrl/processWithBaseUrl dispatch), relevant Cloud/provider access and caller-owned client behavior. Use the candidate's own resolved dependencies, pinned and recorded; the earlier matrix substituted 10.1 dependencies and only proved linkage. Keep linkage and behavioral results separately labeled.

Produce the minimal reviewed production repair diff and named regression test plan, or the exact remaining blocker. Reuse rather than broaden existing fixtures. Do not push this experimental input onto the held fork branch or claim it is submission-ready. No public communication or maintainer policy decision is authorized here.

Next phase after a viable repair is implemented and pushed: a NEW gate pinned to the repaired SHA with tidy, Error Prone, named SolrJ/Jetty/Cloud/LB/ConcurrentUpdate tests and relevant checks, both immutable release cohorts, the behavioral regressions above, and an explicit supported API boundary. Neither the current restoration's older receipt nor the round-1/round-2 prototype receipts cover a new production head. RC3's vote is closed; this is not an open-vote objection or a request to choose which release cohort to preserve.

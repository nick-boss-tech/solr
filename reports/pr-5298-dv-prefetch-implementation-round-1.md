# SOLR-18608 DocValues prefetch implementation, round 1

Status: Implemented, Proof Pending
Host: windows (Codex desktop)
Owner direction: patch the reviewed PR 5298 branch and hand it to the Linux gate.
Claim: claims/pr-5298-dv-prefetch-windows-round-1.md

## Revisions and outbound files

- Reviewed PR parent B: 7ee68a7000b67bed06ad3130ffff27a086a56aad
- Pushed candidate H: 4d547451e3914de441fc897ea6a8c0652a48736a
- Fork branch: nick-boss-tech/solr:pr-5188-glob-field-loading-followup
- Local task branch/worktree: solr-18608-dv-prefetch / wt/SOLR-18608-dv-prefetch
- Public PR: https://github.com/apache/solr/pull/5298 (same open draft; authorized plain fork push moved its head from B to H).
- Source update is one ordinary commit above B. The older local PR-5188 worktree was clean but at a divergent historical head, so it was left intact and the patch uses an isolated worktree at B.
- Windows board bootstrap snapshot: 3a860c33f4f5ac750c7c80cac7cffaded38a2297

Exactly three outbound files:

1. solr/core/src/java/org/apache/solr/search/SolrDocumentFetcher.java
2. solr/core/src/java/org/apache/solr/util/SolrPluginUtils.java
3. solr/core/src/test/org/apache/solr/BasicFunctionalityTest.java

H's BasicFunctionalityTest.java blob: 379421ae51cef1a9e4b58f0c9889c5ef5e7def6c; SHA-256: f44182b29339e09d2f3fa41db835fec781495bce052a8b912ecef1c7e57dc6ca (LF bytes).

The existing SOLR-18608 changelog fragment already describes this ticket's lazy-prefetch fix and lists Nick Shanin; no extra fragment or dependency/configuration change is needed.

## Fix and review disposition

The reviewed glob prefetch used the candidate stored-field selector before the response optimizer's DocValues substitution. Nonstored-DV globs and unmatched globs therefore visited/cached empty-selection stored documents, while eligible stored-plus-DV globs eagerly retrieved values that the response then read from DocValues anyway.

SolrDocumentFetcher now provides a prefetch selector that applies the same all-or-nothing substitution predicate as RetrieveFieldsOptimizer. That predicate is shared, so prefetch and response retrieval use the same eligibility rule. The original getStoredFieldsToLoad remains a candidate selector before DV substitution, preserving RTG's stored-value requirements. SolrPluginUtils adds highlight fields/unique key after substitution and then skips an empty set. Ordinary stored globs, highlighting and the existing caller's shard/50-hit bounds are retained.

Three integration regression methods use the existing BasicFunctionalityTest harness and schema.xml dynamic fields. Nonstored *_s_dvo and substitutable *_idv selections must return their DV value with no cached stored document both before and after serialization. missing_* must return an empty document without that prefetch. Each method also executes original/unified highlighting controls, which must still eagerly prefetch id/title and return the exact title snippet. Existing stored-glob tests remain positive controls. No new fixture or schema was added.

The lazy-wrapper helper comment and assertion wording now state that the wrapper was not materialized. They do not claim that Lucene's shared backing document has never fetched unrelated values. Warm-cache forcing, backing-read instrumentation and physical I/O/latency measurement remain outside this patch; the deeper review identified these as existing behavior/evidence limits rather than introduced output defects.

## Review and verification

- Reviewed the three-file diff, outbound scope, RTG candidate semantics, shared optimizer predicate, highlight union order, test fixture lifetime and response/cache assertions.
- git diff --check passed. No Windows Gradle, compiler, formatter, test job, benchmark or application was run.
- Fork ref and PR head were separately read back and required to equal H after the plain push. The PR remains a draft. No public comment, JIRA write, live body edit, readiness change or merge was made.
- New fail-before signatures are source predictions until Linux execution. The highlighting assertions are positive coverage pins; they are not independently mutation-proven.

## Linux handoff

Gate: gates/pr-5298-dv-prefetch-round-1.md
Execution receipt: receipts/PR-5298-dv-prefetch-round-1.md
Seed: 5298C0DE

The gate requires three independent intended runtime failures on B with H's BasicFunctionalityTest.java overlaid, all nine focused glob methods green at H, the six changed/neighbor suites, clean tidy, Error Prone compilation and the repository root check excluding tests. Logs/fresh XML must be durable with a hash manifest. Formatting/source/test repairs require a pushed new head, an add-only repair report and a superseding exact-head gate.

A posted gate is the handoff; it is not a claim that Linux has executed or passed it. Keep Implemented, Proof Pending until the named execution host receipt completes every leg at the final head. The historical PR 5188 receipts and the initial PR-body proof claims do not establish this new phase's result.

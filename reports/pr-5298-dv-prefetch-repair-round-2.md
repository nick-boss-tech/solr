# SOLR-18608 prefetch gate repair, round 2

Status: Implemented, Proof Pending at repaired candidate R
Host: windows (Codex desktop)
Claim: claims/pr-5298-dv-prefetch-repair-windows-round-2.md
Authority: continuation of Nick's instruction to patch this branch and hand it to the gate, using the scoped repair rule after the reported failed receipt.

## Inputs and pushed repair

- Failed head H: 4d547451e3914de441fc897ea6a8c0652a48736a
- Repaired head R: a99e40291ae17fbee56b821113cc9bc15659b147
- Original reviewed production B: 7ee68a7000b67bed06ad3130ffff27a086a56aad
- Fork branch: nick-boss-tech/solr:pr-5188-glob-field-loading-followup
- Public draft PR: https://github.com/apache/solr/pull/5298
- Failed job/receipt: gates/pr-5298-dv-prefetch-round-1.md / receipts/PR-5298-dv-prefetch-round-1.md
- New gate: gates/pr-5298-dv-prefetch-round-2.md
- New execution receipt: receipts/PR-5298-dv-prefetch-round-2.md

One ordinary repair commit above H, same three scoped files, Nick Shanin author/committer, no trailers. The plain fork push landed; separate remote-ref and PR-head reads both returned R. The local worktree is clean. The public PR remains open and draft at R; the existing failed receipt remains failed and immutable.

## Receipt findings and diagnosis

Round 1 executed all three intended pre-fix failures, clean tidy, successful Error Prone and a successful repository root check. The first root check was interrupted by a runtime restart; the recorded rerun passed, rather than the interrupted attempt being counted as success. Positive proof was not green: the three new tests failed on JSON passed to XPath, and LargeFieldTest failed at H while passing at B.

The XML failure is a test defect in my implementation. The helper never set wt, unlike the neighboring passing highlight test. R adds wt=xml explicitly before serializing into the XPath validator. The negative prefetch assertions still precede serialization; round 2 must nevertheless repeat those legs with the final test blob.

The LargeFieldTest failure occurs at line 96, its first LAZY_FIELD wrapper assertion, before the LAZY_FIELD-only query at line 99. The round-1 report's description of the failure as after that later query is imprecise. The schema-one-field-no-dynamic-field-unique-key.xml fixture has schema version 1.7 and a stored str field. StrField inherits PrimitiveFieldType.enableDocValuesByDefault() returning true, so the first explicit fl=str response can be served from DV.

At H, applying substitution to every prefetch request suppresses the explicit str prefetch. The test's subsequent unfiltered doc(0) call then populates the empty cache with a full document, making lazyField eager and failing the existing assertion. This source trace explains the receipt's B-pass/H-fail result; it is not a new Windows runtime reproduction.

## Repair and scope decision

Restrict both new behaviors to selections with pattern matching: the prefetch selector substitutes DV only for such selections, and SolrPluginUtils skips an empty set only for such selections. Explicit field selections keep the established prefetch/cache behavior from B, including the fixture's eager str and lazy ordinary/large fields. The response optimizer's substitution predicate and RTG candidate selector stay intact.

This follows decision-ladder rung 1: the reviewed task concerns glob prefetch, and the PR promises unchanged behavior for selections without globs. It avoids expanding the optimization to the existing explicit-field path. LargeFieldTest, its schema and assertions are unchanged; the repair does not weaken a failing neighboring test. No warm-cache forcing or physical-I/O claim is added.

Outbound files:
- solr/core/src/java/org/apache/solr/search/SolrDocumentFetcher.java
- solr/core/src/java/org/apache/solr/util/SolrPluginUtils.java
- solr/core/src/test/org/apache/solr/BasicFunctionalityTest.java

Final BasicFunctionalityTest.java blob f39c8488b6c8ed4982ce82108b9f13ccb3bd3604, SHA-256 e2afe28327ab70fbd696bbc60966b00079b8d7ee17df11e8d95da032f2a74997 (LF bytes).
Unchanged LargeFieldTest.java blob ef3af492fdbaf9493ae5a35a4474b9449af38332, SHA-256 abaf6c092868617d4c8ec08afc3aa7ab498bc3da1b5b2315494f8d50e5a014eb.

## Verification and handoff

Reviewed the repair diff against H and passed git diff --check. Windows ran no Gradle, compiler, formatter or tests. No runtime success is claimed for R. The new gate preserves all original proof/check legs at R with final-test overlays, requires LargeFieldTest independently and in the six-suite run, and retains the failed H evidence. Repairs require a new pushed head and a superseding immutable job. No public comment, PR body edit, readiness flip or JIRA write was made.

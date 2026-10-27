# SOLR-18325 Solr 10 impact conclusion after evidence closeout

Assessment: the tested client-only Jackson 3.2.1 migration is technically viable on the examined Solr 10 development base. The candidate is not established as a compatible replacement for released 10.0.0 binaries. The existing gate remains INCOMPLETE, with binaryHttp failing. These are separate conclusions; neither is a requirement waiver.

## Evidence and scope

Reviewed coordination snapshot `68ac8181edf0897ba0a652e9edd0689d4d2150f1`, including the closeout delivered at `a5d03ce16bb`: `receipts/SOLR-18325-evidence-closeout-round-1.md`, `reports/solr-18325-evidence-closeout-linux-round-1.md`, and `reports/solr-18325-http-compatibility-prerequisite-design-round-1.md`. The earlier feasibility and matched binary-control receipts remain part of the evidence. Refresh to `9f423be21f0ed16ad6834404386f4847c94de758` changed only an unrelated receipt in the board work directories.

Candidate remote independently verified: `8bfca8edd30acf11bdf9d6a733312e97f99ec78a`. Exact pre-Jackson base: `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`. Released reference: Solr 10.0.0, source `6c6c48a6f78486130682ea9c1f7a2723af5a87be`. This Windows assessment reviews the Linux records; it does not rerun their tools or inspect the Linux-only raw files directly.

The real Maven consumer resolves the candidate's published POM to Jackson 3.2.1 with shared annotations 2.22 and no Jackson 2 core/databind runtime dependency. It compiles with release 17 and passes in an external Java 17 JVM, both offline and against a released Solr 10.0.0 server on Java 21. This demonstrates the dependency simplification for this consumer graph; it is not a new measurement of the reporter's application size.

The fresh fixed-head positive control passes 15/15. Defaults, depth, the separately isolated string constraint, and exception-wrapping mutants each fail the intended assertions. The first incomplete-worktree control attempt is setup failure, not product evidence; the successful complete-worktree rerun is the control. These probes establish discrimination for the tested boundaries, not all possible serialization behavior.

## Jackson impact versus inherited breakage

Base-to-candidate public/protected comparison finds three additive helper methods on JacksonContentWriter and its DEFAULT_MAPPER field type change. The latter is a real source/binary incompatibility for callers of that field. JacksonContentWriter already has `@lucene.internal` at base line 26; candidate lines 32 and 36 retain the marker and change the field type. Do not describe that as universal binary compatibility or infer a maintainer policy waiver from the marker. javap additionally finds the anonymous CoreAdminResponse$1 superclass change to Jackson 3 TypeReference, which japicmp skips. Solr-api has no API delta. No further signature incompatibility was found in this pair by the reported comparison.

Released-to-base has extensive separate evolution. The 105 HTTP-related removed members and the 134 inherited processWithBaseUrl removals are different inventory views, not independent defects to add together. The 176 unrelated removed members, 11 solr-api incompatible entries, ConcurrentUpdate return change and ResponseParser constructor visibility also require classification if an artifact-wide release guarantee is sought. Inventory counts are not counts of observed consumer failures or necessarily distinct supported-contract violations.

The same old consumer bytes pass HTTP on released 10.0.0 and fail identically on base and candidate before HTTP. The observed timeout descriptor break is inherited from SOLR-17968. The Jackson delta does not cause that failure, but the candidate still fails it. The inherited SOLR-18587 core test failure also remains visible and outside this patch.

## Recommendation on the three design choices

Keep the HTTP compatibility work separate from SOLR-18325. The present evidence is enough to report conditional client-migration feasibility; it does not require rolling an unrelated API repair into the Jackson branch. The existing absolute compatibility gate stays INCOMPLETE until its requirements are actually met or Nick explicitly changes them. No proof leg is dropped here.

If a separate HTTP repair is commissioned, my recommendations are:

1. Restore the released hierarchy, descriptors, protected fields and constructor contracts. For declarations that cannot coexist by return type alone, restore the released declared types. Add new HttpSolrClient-typed accessors under distinct names only where that narrower runtime type can be guaranteed. This preserves old binaries while admitting that newer source expecting the narrower type may need an explicit change. An accessor cannot preserve the source contract of direct protected-field access in a subclass. This is a recommendation, not permission to apply those API changes.
2. Use the checked JDK-builder override so an old erased call reaches the JDK-specific copy logic for a JDK client. Verify executor and SSL-context copying and ownership, including that closing a derived client does not shut down a caller-owned executor. Match released behavior for unsupported client types and explicit builder settings; the stub only establishes descriptor mechanics.
3. Bound that repair to the documented HTTP hierarchy and related signatures, with a precise residual API report. A passing binaryHttp would prove that fixture's path, not whole-artifact compatibility. Keep artifact-wide compatibility unestablished; assess the non-HTTP changes separately before promising it. This recommendation sets the boundary of a possible follow-up, not a narrower acceptance rule for the existing gate.

A real repair needs old compiled fixtures exercised at runtime, including protected access through an instantiated subclass, the old constructor, bridges, casts, cloud/LB accessors and copying behavior. Merely compiling a subclass or counting a bridge in a stub cannot prove those behaviors. Require every promised old descriptor and class to resolve; do not allow exceptions for the two missing top-level classes. Gate the repaired branch and any later combined Jackson head separately at their exact pushed SHAs.

## Report-back wording

The client-only Jackson 3.2.1 prototype works on the examined Solr 10 development base with Java 17 consumers and a released 10.0.0 server. Maven and Gradle resolve the intended dependency graph, and the tested JSON defaults, limits and exception contracts have discriminating regression checks. The measured Jackson API delta is limited to the documented internal mapper field, three additive helpers and an anonymous implementation class. Released 10.0.0 binary compatibility nevertheless fails on both the unchanged base and candidate because of prior HTTP API evolution, with additional inherited API differences elsewhere. Solr 10 is therefore technically plausible for this client migration, but the candidate is not release-ready or a drop-in replacement for existing binaries. Full server migration and Jackson 3.1 LTS remain untested scopes.

No production changes, new gate execution, requirement waiver, public comment or schedule change were made in this assessment. The Linux mutant worktrees and raw evidence remain on their execution host; this report does not delete or repurpose them.

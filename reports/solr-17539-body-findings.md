# SOLR-17539: live body findings

Read at: the live PR head `f9d201a278b9418ae71d8123ff9269edccd7776a` (branch `solr-17539-submit`), body last updated 2026-10-07, read 2026-10-11. The live branch tip is the same SHA. The receipt's gated head is `bce505f45acd20f80686cb448e17e7c5717d6f25`. The only change from the gated head to the tip is one docs line in `solr-ref-guide` `caches-warming.adoc`. The merge-base with upstream/main is `e54802237c941cc9e561c356cd3deb42a2d3557d`. There is no SOLR-17539 entry in `pr-drafts/`.

Verdicts: findings 1, 2 and 3 are SETTLED. Findings 4 and 5 STILL HOLD, with replacement text below. The subagent that checked them was read-only: nothing was edited, posted, built or tested. The main side applies any replacement.

## Finding 1: SETTLED

Live passage: "The pre-fix proof is inconclusive by construction: the cache tests call `isCaching()`, `isDocValuesIteratorCacheEnabled()`, and `createDocValuesIteratorCache()`, which this PR adds, so they do not compile against the base code." The passage names no constructor.

Evidence: all three names are added lines in the branch diff (`DocValuesIteratorCache.java` line 100; `SolrDocumentFetcher.java` lines 235 and 240). The two-argument constructor is unchanged at the merge-base (`DocValuesIteratorCache.java` line 86), with its "caching is disabled" Javadoc at line 84. The sentence the finding asked for is already on the page.

## Finding 2: SETTLED

Live passage (Limits): "The heap effect of the switch is inferred from the code, not measured: the ticket reports heap growth from 4 GB to 6 GB, and no before/after measurement is part of this proof." The Proof section makes no heap claim.

Outside the five, same area, for the main side to decide: the "What this change does" paragraph still says "so the `getDocValues()` OOM exposure goes away on the two changed paths". That states an unmeasured heap outcome as fact. The code supports only the narrower point that, with the switch off, no per-field supplier survives across calls on those paths (`DocValuesIteratorCache.java` line 95; `SolrDocumentFetcher.java` lines 240 and 241). Suggested replacement: "This also addresses the `getDocValues()` concern raised on the ticket: with the switch off, the two changed paths keep no per-field iterators across calls, but the memory effect is not measured (see Limits). With the switch on (the default), nothing changes."

## Finding 3: SETTLED

Live passages: "The two request paths that build the cache (`SolrDocumentFetcher` and `RealTimeGetComponent`) now go through `SolrDocumentFetcher.createDocValuesIteratorCache()`, which honors the setting." and "`UpgradeCoreIndex` still builds a caching instance unconditionally; that one-off upgrade path is out of scope here."

Evidence: the merge-base has five construction sites: `SolrDocumentFetcher.java:797`, `RealTimeGetComponent.java:354`, `RealTimeGetComponent.java:585` (false), `ExportWriter.java:493` (false), and `UpgradeCoreIndex.java:173`. At the head, the fetcher site moves to `SolrDocumentFetcher.java:810`, and `RealTimeGetComponent.java:354` becomes the factory call. `RealTimeGetComponent.java:585`, `ExportWriter.java:493` and `UpgradeCoreIndex.java:173` are unchanged. The two request paths and the `UpgradeCoreIndex` limit are both stated correctly.

## Finding 4: STILL HOLDS (narrower than the finding text)

Live passage (Limits): "Disabling the cache trades memory for speed: with caching off, every `getSupplier` call builds a new `FieldDocValuesSupplier`, which allocates four arrays sized to the number of segments, so the cost lands per field per document instead of iterators being reused."

The body already has the four arrays and "per field per document". What is missing is the lost absence and first-value shortcut. Evidence: the absence cache (`DocValuesIteratorCache.java` at the head, lines 179 and 180; at the merge-base, lines 172 and 173) and the below-first-value check (head line 188; merge-base line 181) read per-supplier arrays (head lines 147 to 160; merge-base lines 140 to 153). `SolrDocumentFetcher.java` at the head, lines 606 to 620, calls `getSupplier` once per DocValues field per document (line 617). With the switch off, each call builds a new supplier and a fresh `dvFunction` lookup, including for fields absent from the segment. Schema fields without DocValues return NONE and cost little.

Replacement: "Disabling the cache trades memory for speed. With caching off, every DocValues field in the fetch list gets a new `FieldDocValuesSupplier` for every document. Each one allocates four arrays sized to the number of segments and starts with no record of earlier lookups. A field absent from a segment is therefore looked up again on every document, and the skip for documents before a segment's first value of that field is lost. The cost lands per field per document instead of reusing iterators. This is read from the code and was not measured. (Citations: the supplier arrays and the absence and first-value checks are pre-change code, `DocValuesIteratorCache.java` at `e54802237c941cc9e561c356cd3deb42a2d3557d`, lines 140 to 181. The switch that makes that path reachable from solrconfig is new, `SolrDocumentFetcher.java` at `f9d201a278b9418ae71d8123ff9269edccd7776a`, lines 240 and 241.)"

## Finding 5: STILL HOLDS (the test is absent)

Live passage: "The option can also be changed through the Config API, taking effect on the core reload the API triggers, and it is documented in the ref guide; both bundled configsets set it explicitly."

The Config API set-and-observe test is still absent at `f9d201a`. `TestConfigOverlay.java` has only the editable-property assertion (line 44) and `testSetProperty` (lines 58 to 68), which uses an in-memory overlay for `filterCache` only. The solrconfig route is covered by `TestDocValuesIteratorCache.testDisabledViaSolrConfig` (lines 116 to 147). The sentence "taking effect on the core reload the API triggers" has no test behind it.

If a test is written, it would assert: on a collection with the default config, the fetcher reports the switch enabled and a factory-built cache reports `isCaching()` true. After setting `query.enableDocValuesIteratorCache` to false through the Config API and the core reload that it triggers, the reloaded core's fetcher reports the switch disabled, and a factory-built cache reports `isCaching()` false. No test was written in this assignment.

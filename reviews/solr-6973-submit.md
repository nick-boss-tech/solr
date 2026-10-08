# solr-6973-submit

- Branch: origin/solr-6973-submit
- Head: 4c6092614e5a (reviewed and unchanged; no patch)
- Base: upstream/main (merge-base cabedd1d968, 16 commits behind)
- Scope: 4 files, +68. `solr/core/.../update/processor/SignatureUpdateProcessorFactory.java` (new early return for partial updates that contain no signature fields), `SignatureUpdateProcessorFactoryTest.java` (one new pass-through test), changelog fragment, `SOLR-6973-TESTING.md` (author's unrun note, left in place).
- Verdict: Ready for review (code read only; nothing compiled or run)
- Reviewer: review-agent A1, 2026-10-08

## Findings (ranked)

No defects found in the code path. The branch matches its stated mechanism:

- verified: in `processAdd` (around lines 150-196), `isPartialUpdate` is computed before the signature loop. The loop throws only when a signature field is present in a partial update. So an atomic update with none of the explicit `fields` reaches the new block, and before the change it got an empty-field signature written to `signatureField` and used as `updateTerm`. That shared signature is the collision the author describes.
- verified: the early `return` runs after the loop and calls `next.processAdd(cmd)` once. The normal path ends with the same call (line 213), so nothing before it is skipped. Only the signature write and `updateTerm` are skipped, which is the intent.
- verified: the no-`fields` case still throws for partial updates (lines 151-156), before the new block, so that behavior is unchanged. The new pass-through applies only to explicit `fields`, which is the reporter's configuration.
- verified (TESTING guesses): `AddUpdateCommand.solrDoc` and `updateTerm` are public (lines 42, 54). `AtomicUpdateDocumentMerger.isAtomicUpdate` returns true for any `Map` value that is not a `SolrDocumentBase`, so `Map.of("set", ...)` qualifies. `Lookup3Signature` exists in the processor package. The test's imports resolve, and `UpdateRequestProcessor` is in the test's own package.
- verified: the test fails before the change. The old code would add `signatureField` and set `updateTerm`, and both assertions are on those.
- LOW: the code comment at lines 192-193 explains the "why". It is not a comment about the change itself, so it is within AGENTS.md's rule.

## Not checked
- Nothing compiled, formatted, or run. No Gradle, no tests.
- The reporter's 5% symptom. The author's own note says it may be routing (shard placement) rather than this signature path. The ticket is not conclusive, so this change may not fix the reported symptom even though the collision it removes is real by reading.
- A partial update that creates a new document (not an update of an existing one) now lands without a signature. That was not traced further.

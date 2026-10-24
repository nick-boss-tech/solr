# DRAFT, NOT POSTED: CPU regression coverage for PR 2702

Owner note: this is proposed comment text for the existing upstream PR. Nick must review the patch, results and disclosure before posting. Independent Linux CPU proof is still pending; the results below are the executed Windows JVM results.

---

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

I checked `23004d0f0de2014f11b09313f04fcf0de7643c3b` against the linkage failures seen when older cuvs-lucene jars run with Lucene 10.4. The versioned implementations address the removed node-sorting helper, `LeafReader`'s `AcceptDocs` signature, and both `IOContext` changes. Direct compilation and whole-jar constant-pool checks passed for all four module source sets against their intended Lucene versions.

I prepared a small CPU test patch for `CompatKnnFloatVectorQuery`: filtered searches, deletions, and their intersection. Each case also calls the approximate adapter directly, since rewrite's exact-search fallback can conceal a dropped filter mask. The only other changes are two test-only bridges for the `Bits` and `AcceptDocs` signatures.

All four tests passed on Lucene 10.2.2, 10.3.2, 10.4.0 and 10.5.1, seed `18556C0D`. The existing test passes mutations that discard the acceptance mask; all three new cases fail on each mutation, covering both API shapes.

These are direct javac and JVM results using the same revision's Java API sources, with the native implementation excluded. I have not validated GPU execution, file compatibility or the official Maven packaging. Would this CPU regression coverage be useful on top of the PR?

🤖 *AI text above* 🤖 *(reviewed by Nick Shanin)*

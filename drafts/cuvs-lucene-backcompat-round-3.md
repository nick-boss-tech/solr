# DRAFT: PR 2702 backward-index coverage follow-up

Owner note: do not post until Nick reviews the patch and this text and the exact-input CPU gate completes. The reviewed-by sentence below is proposed public wording, not a claim that owner review has already happened.

---
Prepared with AI assistance and reviewed by Nick Shanin.

I prepared an additional CPU test for the archived-index upgrade path. The existing read/upgrade tests pass across Lucene 10.2.2, 10.3.2, 10.4.0 and 10.5.1. The new case deletes old documents, appends a mix of vector-bearing and vectorless documents, and checks stored IDs and exact vector values before and after merging.

It also closes a gap in the self-search check: a valid index can associate a vector with the wrong stored ID and still find itself. Separate CPU-written control indexes with that fault pass the existing methods and fail the new value assertion on both 10.2 and 10.4. No additional production failure was reproduced.

Local direct-JVM proof passes all 12 positive method executions across the compatible archived fixtures. Independent Linux JDK 21 proof is pending. GPU construction/search and native integration remain unvalidated.

Prepared with AI assistance; Nick Shanin reviewed the proposed contribution.

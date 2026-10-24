# DRAFT, NOT POSTED: first-contact comment for NVIDIA/cuvs (revision 1)

Owner note (not part of the comment): Nick has not reviewed this text. It supersedes `drafts/cuvs-lucene-first-contact-round-4.md`, which recommended PR 2867 and must not be posted. Target: PR 2702 at head `23004d0f0de2014f11b09313f04fcf0de7643c3b`. Do not post until Nick reviews the text and approves the target. The header says "reviewed by Nick Shanin" only after that review.

---

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

@imotov, I ran CPU-only checks on the Lucene 10.2 to 10.5 code paths in this PR and wrote two small sets of tests for cases I could not find covered. Both ran on JDK 21 on Linux aarch64 against the four Lucene versions, with no GPU.

1. Query acceptance masks. A filtered search, a search that skips deleted documents, and a search that does both each call the approximate search directly, so a dropped filter cannot hide behind the exact-search fallback. With the mask dropped on Lucene 10.2.2 and 10.4.0, the three new cases fail. With the code as it stands, they pass on Lucene 10.2.2, 10.3.2, 10.4.0 and 10.5.1.
2. Archived-index upgrades. Upgrading the existing archived fixtures that contain deletes and sparse vectors keeps each vector attached to its document ID. The new case checks 183 live documents and 173 vectors on all four versions.

The patch changes only test files at this head. I see that #2867 lands the Lucene 10.2 layout first. If it does, I can port these tests to that layout.

Would you want these tests included in this PR?

# DRAFT, NOT POSTED: first-contact comment for NVIDIA/cuvs

Owner note (not part of the comment): Nick has not reviewed this text and has not chosen the target. Do not post until he does. The header below says "reviewed by Nick Shanin" only after that review. Recommended target: PR 2867 (open, two approvals on 2026-10-22, head 1c6bc921126). Alternative: PR 2702 (open draft, base 23004d0f0de), which PR 2867 was split from. Both targets are checked in `reports/cuvs-lucene-submission-package-round-4.md`.

---

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

@imotov, I ran CPU-only checks on the Lucene 10.2 to 10.5 code paths and wrote two small sets of tests for cases I could not find covered. Both ran on JDK 21 on Linux aarch64, one run per Lucene version, with no GPU.

1. Query acceptance masks. A filtered search, a search that skips deleted documents, and a search that does both each call the approximate search directly. With the mask dropped on Lucene 10.2.2 and 10.4.0, the three new cases fail. With the code as it stands, they pass on Lucene 10.2.2, 10.3.2, 10.4.0 and 10.5.1.
2. Archived-index upgrades. Upgrading the existing archived fixtures that contain deletes and sparse vectors keeps each vector attached to its document ID. The new case checks 183 live documents and 173 vectors on all four versions.

Both sets were written against the compat modules in PR 2702 at `23004d0f0de`. PR 2867 uses per-version Lucene modules instead, so the tests would need a port to its layout. Would you want these tests included in this PR, or kept separate?

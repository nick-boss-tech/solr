# Report: scope-separated adjacent follow-ups composed

Status: Composed and pushed, Proof Pending.

- Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; claim `claims/solr-12613-scoped-integration-windows-round-1.md`.
- Fork branch `solr-12613-adjacent-integration`, exact pushed/read-back head `dabf21456845c0ae44b05b31e3893a5849dfc281`.
- Frontend-only baseline `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`; main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`. Synthetic candidate preserving original picker/prerequisite ancestry, not an adopted contributor revision. Reconcile before an adoption offer or public submission.
- Slice inputs independently re-read from live fork refs: C2 encoded keys `7b83425fe7ce704ee781f273cd099b4425b795af`, C5 path coverage `59ad950e1ac14d628e711e82746ee37c489cf151`, C3 lifecycle `7f8d251d6c27200f3804f8b4e1298b9646f6cab4`, C4 SOLR-18619 UI requests `be369c41c633d639ad66a7291a4d82891ea9836c`.
- Gate `gates/solr-12613-scoped-integration-round-1.md`; receipt target `receipts/SOLR-12613-scoped-integration-round-1.md`.

Four ordinary merges completed without conflicts/manual resolutions. Merge heads are `3466903dac6e2b417716748646f3cc68e6e11d0e` (C2), `fbeaeb24ae27928b7c8f4fa5c964c4d3229486af` (C5), `3d0f598f19dab7f3de1a66d3068e063679147ac8` (C3), then the final head (C4). The shared filter test auto-merged and retains both groups. No source repair or additional feature was introduced during composition. Independent branches remain available.

The 17 paths changed from the frontend-only baseline equal the union of the four slice scopes. All files under solr/api remain identical to main; the only main-relative solr/core changes are NodeTargetProxyFilter.java, NodeTargetProxyFilterTest.java and NodeTargetProxyFilterAuthTest.java. Inherited v2 endpoint/model/aggregation additions remain removed. The original contributor history remains, but those API changes are absent from the final diff. SOLR-15823 / PR 5031 is separate and unmodified.

The composed candidate retains 21 filter methods, 6 auth, 8 picker, 11 node-screen, 6 logging-screen, 2 standalone logging and 3 new native-contract methods. These are source inventories, not executed results. Twenty adjacent methods remain across the four original slices; the baseline's three generated-client contract methods bring newly authored coverage to 23. Their native endpoint rendering and independent header assertions require runtime proof.

Source composition preserves callback-only properties/thread/logging-level GET callers, C3 startup/obsolete-response/fallback handling, and C4 selected-local pinning plus per-request Metrics/system/logging reads. Selected-node logs remain the default with an All nodes option. Nick's requested maintainer choice stays in the eventual unposted PR draft. Same-JVM/stubbed failure tests and BasicAuth coverage do not imply physical-node outage or client-certificate proof.

The new integration job supersedes the withdrawn old combined job. It requires the actual green receipts for the pinned frontend-only baseline and all four slice heads before execution, then combined core/browser tests, all three generated-contract tests, tidy/root checks and test-data screenshots for the log control/origin/failure presentation. Repairs to an input require recomposition and a new exact-head job; neither independent nor combined proof transfers between heads. Mutation/fail-before obligations remain in the slice jobs and must be satisfied there.

Windows verified clean scope, exact ancestry, merge/file preservation, whitespace, Nick's merge identity and matching fork head. Windows ran no Gradle, compiler, browser or tests. No source work remains local in the task branches, and no PR, comment, Jira action, upstream/contributor push or history rewrite occurred. Required Linux receipts were absent at the reviewed board snapshot `b86734c0a0e15d2e58e53040fcb1be37460dbda1`; no gate execution is claimed by this report. The full objective remains incomplete pending actual matching proof and any required repair.

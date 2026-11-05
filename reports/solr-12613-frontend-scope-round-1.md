# Report: frontend-only picker baseline and separate adjacent UI branches

Status: Implemented, Proof Pending.

- Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; claim `claims/solr-12613-frontend-scope-windows-round-1.md`.
- Maintainer scope evidence: https://github.com/apache/solr/pull/5110#pullrequestreview-5778764327 and https://github.com/apache/solr/pull/5110#discussion_r4475714002 . Contributor head remains OPEN at `73b30956ff2c0f7b16f6af3bceacf95bda8271b9` when this work begins.
- Decision ladder rung 3: implement the frontend/header scope, keep backend v2 additions with separate tickets, and preserve Nick's explicit logging-choice instruction.

## Pushed branches

| Role | Fork branch | Independently read-back head | New Linux gate |
| --- | --- | --- | --- |
| Synthetic frontend-only baseline | `solr-12613-frontend-only-baseline` | `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e` | `gates/solr-12613-frontend-only-baseline-round-1.md` |
| C3 lifecycle follow-up | `solr-12613-selection-lifecycle-followup` | `7f8d251d6c27200f3804f8b4e1298b9646f6cab4` | `gates/solr-12613-selection-frontend-round-1.md` |
| C4 SOLR-18619 UI migration | `solr-18619-ui-request-targeting-followup` | `be369c41c633d639ad66a7291a4d82891ea9836c` | `gates/solr-18619-ui-request-targeting-round-1.md` |

These are ordinary commits/merges on Nick's fork. They preserve the contributor's history and existing Selenium prerequisites; no contributor branch is changed and no history is rewritten. They are synthetic integration candidates, not revisions the contributor has adopted. Reconcile with the eventual real frontend-only picker head before an adoption offer or public submission. Their final trees have no difference from upstream main `07ccc73cd24e26e59da169ca35b3fd5f64c35986` anywhere under `solr/api/` or `solr/core/`.

## Baseline correctness and contracts

Baseline parent is `b45bae5da3a151c7b439ff4e42009c383b538fae`. The new commit restores all 17 inherited backend API/test paths to main, removing the picker-only NodeAdminProxySolrCloudTest. Main's existing logging-level PUT broadcast remains unchanged. The retained main-relative changes are the picker frontend, browser prerequisites/tests, three reference-guide pages and the original contributor's changelog; no API expansion remains in the final tree. Existing contributor attribution is preserved.

Restoring main's API contracts removes optional-argument slots from three generated JavaScript methods. JavaPropertiesController and ThreadsController prepare the existing targeting plugin and then call getNodeProperties/getThreadDump with only the callback. LoggingLevelController calls listAllLoggersAndLevels with only its callback. These are the three adjusted controller files; no hand-edited generated client or compatibility wrapper is introduced.

New `solr/webapp/src/test/org/apache/solr/webapp/AdminUiNodeContractTest.java` adds three browser methods. They select a remote node through the actual picker, observe the controller's argument count/type, delegate to the real generated client, require a successful native response shape and check rendered properties/thread/logging-level content. The observer stores booleans/counts only and captures no credentials or response contents. The gate's three isolated mutations reintroduce a spurious options argument and must fail the explicit call-shape assertion; native payload/rendering checks remain positive coverage pins. Node identity is covered separately by the existing/request-targeting tests, not asserted from same-JVM property values.

## Adjacent source scope preserved

C3 merges original head `a331e7faf96c8c215f2d623f4865d4cbabca6492` without conflicts. Relative to the new baseline, only app.js and AdminUiNodePickerTest.java change; both final blobs match the original C3 head. Its four added regressions remain: late cross-node response, late same-node refresh, persisted remote startup target and local-core refresh after metadata failure. The class still has eight methods. No proof transfers from its withdrawn job.

C4 merges original head `67cc03dfbed1866216c8cda8c31a65beb2c5672e`. The same twelve scoped files change relative to the new baseline: services.js, cloud.js, logging.js, index.html, cloud/logging partials, AdminUiTestBase, node/logging/standalone test classes, configuring-logging.adoc and SOLR-18619-target-node.yml. Eleven blobs match the original patch; logging.js additionally retains the callback-only logging-level GET call from the clean baseline. Seven new methods remain, including independent request headers, selected-local pinning, partial failures/recovery, all-failed stale-event clearing and untargeted standalone reads. Its own changelog names Nick Shanin only.

The logging default remains a reviewable proposal: selected node by default, explicit All nodes in cloud mode. The eventual PR draft must pose the maintainer choice of always aggregating cloud logs, as Nick requested. This work does not redesign or publicly settle that choice.

## Architecture, reuse and verification limits

The UI branches consume existing node APIs and the generic targeting header. They do not widen endpoint parameters/models, add backend aggregation, change authorization, or duplicate OPEN SOLR-15823 / PR 5031. The contract tests reuse AdminUiTestBase, its existing lazy two-node fixture, generated-client servlet and browser navigation; no new configset, servlet fixture or dependency is added. The C4 shared XHR helper continues capturing only method/URL/target header. BasicAuth, metadata-failure stubs and same-JVM tests do not establish client-certificate behavior or a complete security assessment.

Static verification checks exact parents/ancestry, final backend-tree equality with main, scoped deltas, original-patch blob equality with the one documented logging adaptation, callback call sites, retained test annotations, clean trees, whitespace, Nick's authored commits and matching remote heads. No Gradle, compiler, browser or tests ran on Windows. Source review and static counts are not runtime proof.

The baseline gate requires the real generated client rebuilt from main-equivalent API sources, three new contract tests plus picker/neighbor/core suites, three intended mutations, tidy and root check. Its matching green receipt is a prerequisite to the two follow-up jobs. The C3 job retains four fail-before assertions against the clean baseline. The C4 job retains six isolated runtime mutations and all seven added tests. Both additionally run the inherited contract class to catch merge/signature regressions. Any baseline repair requires recomposition and new jobs; no old-head result transfers.

Old picker-based jobs stay withdrawn under `gates/solr-12613-adjacent-scope-standdown-round-1.md`. New receipt paths are `receipts/SOLR-12613-frontend-only-baseline-round-1.md`, `receipts/SOLR-12613-selection-frontend-round-1.md` and `receipts/SOLR-18619-ui-request-targeting-round-1.md`. Independent transport inputs and jobs are in `reports/solr-18537-transport-main-round-1.md`. Clean combined integration remains to be composed and gated after the source inputs are established; no complete or submission-ready verdict is claimed. No PR opening, live PR edit, GitHub/Jira comment or upstream push was performed.

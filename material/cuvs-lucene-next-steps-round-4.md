# cuvs-lucene: remaining assignments after CPU proof, round 4

Owner request, 2026-10-24: build detailed assignments for all necessary remaining steps. These files describe work and its prerequisites; they do not authorize public submission, dispatch agents, schedule polling, or assert that a GPU host exists.

Reviewed board snapshot: `21503341856ee86c8aec61bf892463bb573e7a07`. Publication claim: `claims/cuvs-lucene-next-assignments-round-4.md`. Read live claims, receipts and current source before acting. Existing records are immutable; use new deliverables and replacement jobs when inputs change.

## Established inputs

The production compatibility implementation is in NVIDIA/cuvs PR [2702](https://github.com/NVIDIA/cuvs/pull/2702), authored by imotov. At planning time it is open/draft at `23004d0f0de2014f11b09313f04fcf0de7643c3b`. Nick's contribution consists of test additions. Do not carry the round-1 helper-only production patch into the contribution: the PR already supplies that implementation and other required linkage fixes.

| Evidence | Immutable package | Patch SHA-256 | Independent completion |
| --- | --- | --- | --- |
| Round 2, query acceptance masks | `2ad555a87a22acdf617f679ca03f5831f246b4a8`, `material/cuvs-lucene-pr2702-round-2/cpu-regression.patch` | `d9ad46585c65846cf9b0d4f0ccffbf882e79be593f15897d8775115de810b154` | `receipts/CUVS-LUCENE-PR2702-CPU-ROUND-2.md`, introduced by `c6e0a5564bb6a9e6dda9b446b216965be9034e46` |
| Round 3, archived-index upgrades | `e174c1a3e0dcfccf9d30a31847637733823df84b`, `material/cuvs-lucene-backcompat-round-3/backcompat.patch` | `56cb5cb10f210e216c36fd413821dd1ddd931e5dd4ed26fc288299c928a84238` | `receipts/CUVS-LUCENE-BACKCOMPAT-ROUND-3.md`, introduced by `b64dc930dac6d802ed53f37d7b6cb64159fe9eee` |

Both receipts are GREEN on vm2, Linux aarch64, JDK 21. Round 2 covers four production compiles and whole-jar linkage inventories, 16 positive method executions, and two mask-drop mutations with six expected new-test failures. Round 3 covers 12 positive method executions over 138 fixture cases and two expected data-fault failures, with four passing existing-method controls. Seed: `18556C0D`. No positive skips or assumption failures were accepted.

The round-3 compact compile matrix is extracted from actual run stdout. Its entries match that log; it is not a second compile invocation or a full command/source inventory. Do not request a new run merely to regenerate that summary. Old report/job Status lines may predate these receipts. Completion is derived from the matching receipts; do not reopen completed CPU jobs.

## Work and dependencies

| Step | Assignment | Initial state | Trigger or prerequisite |
| --- | --- | --- | --- |
| 1 | [Consolidate unchanged test patches](../assignments/cuvs-lucene-01-consolidate-round-4.md) | OPEN | Can start now |
| 2 | [Prepare the upstream submission package](../assignments/cuvs-lucene-02-submission-package-round-4.md) | HANDED | Step 1 package verified |
| 3 | [Review readiness independently](../assignments/cuvs-lucene-03-readiness-review-round-4.md) | HANDED | Steps 1 and 2 delivered |
| 4 | [Owner-approved upstream handoff](../assignments/cuvs-lucene-04-owner-handoff-round-4.md) | NEEDS-OWNER | Step 3 ready, Nick review and exact posting approval |
| 5 | [Handle substantive upstream feedback](../assignments/cuvs-lucene-05-review-followups-round-4.md) | HANDED | A real maintainer request or upstream change affecting the contribution |
| 6 | [Verify upstream GPU/native and release evidence](../assignments/cuvs-lucene-06-release-evidence-round-4.md) | HANDED | Concrete upstream test evidence or a published candidate release |
| 7 | [Reconcile Solr docs and status drafts](../assignments/cuvs-lucene-07-solr-docs-round-4.md) | OPEN | Can start now; drafts only |
| 8 | [Prepare and implement released-artifact adoption in Solr](../assignments/cuvs-lucene-08-solr-adoption-round-4.md) | HANDED | Suitable released artifact, scope assessment and Nick's explicit Solr activation |
| 9 | [Prove Solr integration on a GPU host and prepare closure](../assignments/cuvs-lucene-09-solr-gpu-acceptance-round-4.md) | HANDED | Step 8 exact pushed head, concrete gate jobs, capable GPU host |

Read the named assignment for its claim path, deliverables and acceptance. HANDED is a conditional work item, not a host reservation. A capable worker may claim when the stated prerequisites are evidenced, without asking anyone to edit the assignment's Status line. Do not take a waiting item merely to announce that its known prerequisite is missing. Step 4 requires explicit human authorization even after every technical prerequisite passes.

## Shared rules

Read WORKFLOW.md, START-HERE.md, UI-PLAYBOOK.md, decision-map and decision-ladder before claims. Claim one assignment at a time; first push wins. Publish durable board deliverables in the same session, with new paths. Never edit another claim, receipt, old gate, assignment, or generated index. Verify content and commit reachability after publishing. Commit as Nick Shanin, `nick.boss.us@gmail.com`, with no trailers.

Until separately authorized, upstream source work remains an exported board package. Do not push an NVIDIA source branch, create a public PR, post a comment, or manufacture a fork URL. A proposal to do so must identify the exact target, bytes, revision and delivery method for Nick's decision. If upstream newly requires DCO sign-off, document the exact requirement and ask Nick to resolve the conflict with the no-trailers rule; do not add a sign-off automatically.

Windows never runs Gradle or the Solr test queue. The original non-Solr direct-javac CPU workflow is permitted where expressly named. Solr build/test proof runs only on a capable Linux gate host under concrete, phase-specific jobs. A GPU-skipped Solr test does not prove this fix.

Consolidating byte-identical, disjoint test changes on the same production base needs application/blob review, not an automatic repeat of both green gates. Keep their evidence scopes separate; do not claim a single combined 28-test run occurred. A changed test, production source, API/dependency set or source base needs a new exact-input CPU job covering the affected proof legs. No generic job may run against a placeholder head.

Upstream GPU construction/search/native evidence, published-artifact identity, and Solr integration evidence are different proofs. Test patch adoption is also separate from shipment of the production compatibility fix. Avoid turning a missing optional test contribution into a reason to ignore a released production fix, or a successful CPU test into a GPU claim.

The Solr guard remains deferred pending upstream's answer. Existing docs/Jira work belongs to `assignments/solr-18556-closure-round-1.md`; step 7 reconciles with that route instead of duplicating it. No release date, dependency pin, guard, or public update is authorized merely by this roadmap.

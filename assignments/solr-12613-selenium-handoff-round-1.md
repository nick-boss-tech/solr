# Assignment: arrange and complete the remaining node-picker Selenium verification

Status: OPEN. Intended host: Windows Selenium/browser agent preferred by Nick; a capable Linux Selenium/gate host may execute legs requiring the Linux build toolchain. Tags: selenium, review, records; gate capability for full Linux jobs.

## Owner direction and execution boundary

Nick asked for a separate Selenium assignment after the transport receipts arrived, and suggested another agent on Windows. He permits assuming the browser behavior works for planning and PR/Jira draft preparation. Do not turn that planning assumption into a passing test result. Coordinate with `assignments/solr-12613-pr-jira-packages-round-1.md`; its drafts need not wait for this assignment, but their proof labels must remain accurate.

Windows is an authorized destination for the browser follow-up, superseding the older Linux-only host preference for this handoff's browser portion. This is not a blanket release of the Windows Gradle/build guards. Use an approved existing browser runner or verified exact-head build artifacts where supported. Do not bypass guards, use -AllowGradle, revive the Windows test queue, or manufacture an unsupported runner. If Windows cannot execute the named Selenium tests within those constraints, record the exact missing artifact/tooling or guard refusal and transfer that execution slice through this assignment to a capable Linux browser host. No further owner question is needed merely to use that existing Linux route. Root/tidy/build and non-browser proof requirements remain with the Linux gate host.

Discovery snapshot `9138f9f3382f7661ca3da7731ae9fc3cc6fbc079`; refresh the live board, claims, receipt paths and remote source heads before claiming. Claim one execution slice at a time and avoid concurrent runs of the same job or worktree. No persistent chat goal is started or resumed.

## Inputs and execution order

Read each full job; those files supply the exact commands, seeds, tests, mutations, expected failures and finishing rules. This assignment arranges browser execution and evidence, not weaker replacement proof.

| Order / role | Fork branch and required head | Job and existing full receipt target |
| --- | --- | --- |
| 1. Frontend-only baseline | `solr-12613-frontend-only-baseline` at `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e` | `gates/solr-12613-frontend-only-baseline-round-1.md`; `receipts/SOLR-12613-frontend-only-baseline-round-1.md` |
| 2a. Lifecycle follow-up | `solr-12613-selection-lifecycle-followup` at `7f8d251d6c27200f3804f8b4e1298b9646f6cab4` | `gates/solr-12613-selection-frontend-round-1.md`; `receipts/SOLR-12613-selection-frontend-round-1.md` |
| 2b. Request-targeting follow-up | `solr-18619-ui-request-targeting-followup` at `be369c41c633d639ad66a7291a4d82891ea9836c` | `gates/solr-18619-ui-request-targeting-round-1.md`; `receipts/SOLR-18619-ui-request-targeting-round-1.md` |
| 3. Combined candidate | `solr-12613-adjacent-integration` at `84743822082ed2c409d87e40a6f8142e2a240042` | `gates/solr-12613-scoped-integration-round-2.md`; `receipts/SOLR-12613-scoped-integration-round-2.md` |

The UI baseline is synthetic: main `07ccc73cd24e26e59da169ca35b3fd5f64c35986` plus the original picker/prerequisite ancestry, with all inherited backend API expansion removed. It is not the contributor's adopted revision. Generate/use the client bundle built from the exact head's main-equivalent API sources, not an old bundle with added optional nodes arguments. A running browser/server or cached artifact must have verifiable commit/build provenance; existence alone is not enough.

Baseline full proof precedes the independent follow-up executions. Combined execution waits for its actual five matching prerequisite receipts. The two transport inputs are independently GREEN at `a676e7a392244e7cf4a35719fc64383b65cd947f` and `0fad9847cc89973c751b1732aa22a259764610b2`, with round 2 receipts. They do not prove UI behavior. No withdrawn/API-expanded job is a substitute.

## Browser work to cover

1. **Baseline, seed 12613F00D:** AdminUiNodeContractTest (3) plus AdminUiNodePickerTest (4), the named node/core/standalone/logging-level/auth neighbors, and the three isolated callback-argument mutations specified by the baseline job. Require real generated callbacks/native payloads and rendered content. Mutation failures must be the intended argument-count assertion, not timeouts or setup errors.
2. **Lifecycle, seed 5110C0AE:** all 8 picker methods, the 3 inherited native-contract methods and specified neighbors; all four required fail-before methods against the exact clean baseline plus unchanged candidate tests. Cover obsolete cross-node/same-node responses, restored initial target and real local core refresh after controlled metadata failure. CDP/setup errors and merely incremented counters are not the required proof.
3. **Request targeting, seed 18619C0DE:** 11 node-screen methods, the three named new logging methods, 2 standalone logging methods, and the named contract/picker/logging-level/auth neighbors. Execute all six isolated mutations from its job. Check per-request target headers, selected-local pinning, aggregate log origins, partial failure/recovery, stale-event clearing and no standalone targeting/discovery. The existing event-watcher test's shared-JVM skip is not a substitute.
4. **Combined, seed 5110F0110:** execute the complete selected browser legs from the round 2 integration job after prerequisites. The final core inventories are 22 filter plus 6 auth, and browser inventories are 3 contract, 8 picker, 11 node-screen, 6 logging-screen (the job selects 5) and 2 standalone logging; inventories are not passing results. Capture screenshots of the All nodes control/origin column and partial-failure labels with test data. Preserve Nick's selected-node-default/All nodes proposal; do not redesign it in the test run.

Use the job's JVM/browser flags and preserve fresh logs/XML before each cleanTest. Record JDK, browser, driver, OS, generated-client provenance, commands, seeds, times, exit codes and actual run/fail/error/skip counts. Capture no Authorization headers or secret property values. Controlled stubs and same-JVM fixtures do not prove a physical node outage or client-certificate behavior.

## Results, repairs and remaining non-browser legs

Do not waive named runtime negative proof or count compilation, HTTP/setup rejection, skipped tests or timeouts as the intended assertion failure. Investigate an executed failure before any retry. Source or formatting repairs require scoped ordinary Nick Shanin commits, plain fork pushes/read-back, new exact-head jobs and any necessary recomposition. Keep mutations isolated and off fork branches. Do not rewrite public/contributor history or edit another host's receipt.

The existing full jobs also require tidy, root checks and specified core neighbors. A Windows browser-only result may satisfy its recorded browser leg, but must not be labeled the whole job GREEN. Have the capable gate host reconcile that evidence and execute outstanding non-browser legs at the same head before writing a full receipt. If the gate cannot accept transferred browser evidence, execute the named job on the supported Linux browser host; record the reason rather than weakening its requirements.

## Deliverables and finish

- `reports/solr-12613-selenium-handoff-round-1.md`: execution-owner/host plan, exact heads, existing proof, browser results or concrete setup blockers, and remaining non-browser work. If a handoff is needed, give the next host an exact runnable slice, not a generic request for Selenium.
- The executing browser host adds `receipts/SOLR-12613-browser-verification-round-1.md` (or a new add-only round if that path is already owned), with a per-head result table and links to fresh logs/XML, mutation diffs/assertion failures and screenshots. Label any partial result explicitly. Full gate receipts remain owned by their actual gate executors.
- A final add-only result report maps every original job requirement to a passing artifact or an explicit outstanding leg and gives the package author precise public wording for what ran. No author adoption, full readiness or passing browser run may be inferred from an assumption.

No PR opening, Jira filing/comment, GitHub comment, contributor/upstream push or source redesign is authorized by this assignment. Finish with the executed result or a concrete capable-host handoff; a posted assignment alone is not execution proof.

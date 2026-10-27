# Gate SOLR-18325 released binary baseline attribution round 1

Status: OPEN

- Tags: gate, premise-run, review, records. Intended host: any capable Linux host, preferably the existing vm2 execution host while its round2 artifacts and evidence remain available. Respect the host build lock and current claims.
- Candidate, unchanged for this job: fork `solr-18325-submit` at `8bfca8edd30acf11bdf9d6a733312e97f99ec78a`.
- Exact pre-Jackson production base: `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`.
- Released compile/runtime control: Maven Central SolrJ10.0.0.
- Existing evidence: `receipts/SOLR-18325-solr10-feasibility.md`, `reports/solr-18325-solr10-feasibility-linux-round-2.md`, `gates/solr18325-superseding-8bfca8e.md` and both vm2 feasibility claims.
- Assessment and response: `reports/solr-18325-binary-attribution-windows-round-1.md`, answering `assignments/vm2-solr-18325-owner-decision-round-1.md` without a binary requirement waiver or permission for an unrelated source repair.
- Deliverables: new single-writer receipt `receipts/SOLR-18325-binary-attribution-round-1.md` and new report `reports/solr-18325-binary-attribution-linux-round-1.md`. Leave the prior host's receipt and prior assignment unchanged.

## Decision under test

Keep `binaryHttp` as a requirement. Do not drop it, replace it by recompilation, or restore/add builder classes in this job. The round2 receipt explicitly says released consumer bytecode was not run against the unmodified base artifact. That missing control is the next step, before choosing a compatibility repair.

Source history points to SOLR-17968 commit `e5340a954b6700627b8826431a243c2e2759b0f3`, which moved the builder and renamed the HTTP base client. Candidate versus pre-Jackson base has no source changes under solrj/impl. Establish runtime inheritance, not just source similarity.

## Preparation and fixture integrity

Refresh the live board, candidate fork head and competing claims. Claim this bounded follow-up under the host lock with exact toolchains, timestamps, runner and absolute evidence paths. No tests or builds ran on Windows. Use full detached source/artifact trees and isolated repositories so candidate and base snapshots cannot overwrite each other.

Reuse the exact StableConsumer class bytes compiled against released10.0.0 from round2, with their SHA-256 and baseline compile graph. If those bytes are unavailable, compile the exact candidate harness source once against released10.0.0, record hashes, and use that one output directory unchanged for every runtime leg. Never compile these 'old' classes against the base or candidate. Do not delete the timeout call or otherwise weaken the failing fixture.

Create a separate build-local Maven artifact repository from the unmodified base. The authorized publication task is `./gradlew --no-daemon mavenToLocalFolder` under the host lock. Keep tracked production/build/test source at the exact base; do not cherry-pick the prototype, use its dependency locks or add a compatibility shim. Preserve the published base POM/JAR hashes, actual resolved version and dependency graph. The base is allowed and expected to depend on Jackson2.

Do not run the existing verifyCandidateDependencies assertion against the base: it intentionally rejects Jackson2, which would confound this control. Resolve the base runtime through a separate minimal external Maven/Gradle consumer using its published POM, without the no-Jackson2 assertion. Run Java directly with the preserved old class directory plus the separately resolved runtime classpath. Reuse the round2 candidate artifacts if their complete hashes and exact source head can be verified.

## Matched HTTP controls

Use the same verified unchanged Solr10.0.0 standalone server/core configuration as round2, or recreate it from a verified distribution. Use a disposable core with stored/indexed unique key id. Record server artifact checksum, Java21 server runtime, startup/core commands, URL/configuration and cleanup. Do not use a production/shared core.

Execute the same old class bytes and same HTTP arguments with Java17 against each runtime:

1. Released10.0.0 runtime. Positive control: it must get through builder setup and real core-status/update/query/delete operations.
2. Unmodified pre-Jackson base runtime, from the exact source base above.
3. Current Jackson candidate runtime, pinned to the exact candidate head above.

The Java invocation shape is `java -ea -cp <old-class-directory>:<resolved-runtime-classpath> org.apache.solr.compat.StableConsumer <server-url> <disposable-core>`. Supply literal resolved paths in the recorded runner and save each exact command, exit code and complete output. Hash old class files before/after every leg. Keep all source/class bytes identical; only the resolved client runtime changes.

For each failure capture the full NoSuchMethodError signature, including return descriptor, and use javap on old consumer bytecode and the three runtime jars to record the invoked/declarative method descriptors and superclass chains. Check the old HTTP client base type as well as the builder: recreating only one class name is not a complete compatibility design. Record any subsequent linkage failure rather than stopping the inventory at the first symbol.

Predicted result: released runtime passes; both base and candidate fail on the same builder descriptor before HTTP begins. This is a prediction, not permission to change the fixture to fit it. If released runtime also fails, fix/classify the control setup before attributing the candidate failure. If base passes but candidate fails, investigate artifact selection/compiler/classpath/production differences before proposing a shim.

## Preserve and qualify existing evidence

Confirm four core tests remain Jackson2 oracles using a fresh ObjectMapper, with their conversion inputs and assertions unchanged. Do not revert them to the migrated client singleton or turn them into evidence of Jackson3 client behavior. That is the explicit design of the prototype.

Link the inherited unload failure to existing SOLR-18587 and the board's `reports/rc3-test-failure-causes-round-2.md`. Preserve the round2 core outcome as 33 tests, 1 inherited failure, not green. Do not merge a separate CoreAdmin fix into this Jackson experiment or suppress the assertion.

Audit the original job's other requested evidence: actual Maven consumer resolution, mutation/default/error-boundary controls and public/protected API comparison. Link the raw logs if already run; otherwise label each pending/unrun and perform the bounded missing proof where feasible. Do not invent counts or claim the Gradle POM-resolution lane was a Maven execution. Include full publication hashes, replacing the abbreviated values in the round2 summary.

No need to repeat tidy, root check or the large focused suites merely because this control is new: candidate source head stays unchanged. If a real harness/source repair becomes necessary, record it, push only to the fork with ICLA identity and no trailers, verify the head and publish a superseding exact-head proof job before treating new-head results as covered. Unrelated production API repairs are outside this control job.

## Outcome and next decision

- If released passes and base/candidate fail identically: record the binary defect as inherited and not caused by the Jackson delta. The absolute released-binary requirement is still unsatisfied; do not call binaryHttp passed, narrow to source-only or mark the original gate green. Recommend a separately scoped compatibility repair/prerequisite with descriptor evidence, preserving the Jackson finding independently.
- If base passes and candidate fails: keep INCOMPLETE, identify the actual candidate regression and its smallest justified fix. Do not assume the class restoration is the cause.
- If all three pass: explain the difference from round2 using exact artifacts/classpaths, then reconcile the prior failure honestly.
- If setup prevents the controls: record INCOMPLETE with the exact missing input; a setup failure is not evidence of Jackson infeasibility.

Publish the receipt/report with all artifact/source/fixture hashes, graphs, Java versions, raw outputs, actual descriptors, HTTP observations, prerequisite recommendation and remaining evidence gaps. No owner waiver, release approval, public GitHub/JIRA comment, PR or upstream push is authorized. This job does not establish the full server migration or the preferred3.1LTS target.

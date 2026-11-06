# Gate: SOLR-18627 ignored-field sibling fixes for PR 5305, round 1

Status: OPEN. Immutable job. Proves only final head F below.

- Required tags: gate, review, records. Intended host: any capable available Linux CPU gate host with JDK 21, the branch's Gradle wrapper/dependencies, and a working environment for the existing ICU/Mockito test fixture. Serialize behind the host's build lock and obey disk/inode guards.
- Fork: nick-boss-tech/solr. Branch: pr-5305-solr-18627-ignored-fields.
- Final head F: 219fee78bf7cae5039e50fe885108a1d4937ccd3.
- Final tree: 0f979d6c070da09f889b829f51c915743df77239.
- Contributor parent O: 00292cd3729126194235e939afe38e351d850c70.
- Public PR: https://github.com/apache/solr/pull/5305. Public head remains O; do not push to the contributor's fork or edit the PR.
- Implementation claim: claims/pr-5305-ignored-fields-windows-round-1.md.
- Implementation report and exact ten-file list: reports/pr-5305-ignored-fields-implementation-round-1.md.
- Execution-host-owned receipt: receipts/PR-5305-ignored-fields-round-1.md.
- Seed for every test leg: 18627C0DE.
- Status at handoff: Implemented, Proof Pending. No Gradle, compiler, formatter or test executed on Windows. Existing upstream CI validates O, not F.

This is a production regression repair. O already fixes StrField, but still has six affected sibling production classes and the matching SortableBinaryField test-framework fallback. Fail-before proof uses O's actual affected implementations with F's tests unchanged. It does not use synthetic absent APIs, compile failures or setup failures as negative proof.

## Claim, preflight and repair rules

Read live WORKFLOW.md and START-HERE.md, this job, claims and receipt state. Verify the live fork ref equals full F and that O is its direct parent. Run the board's detached tidy preflight before claiming execution. Add an execution claim naming the exact source heads/tree, host/JDK, build lock/cache, detached worktrees, runner path, timestamps and absolute log/XML paths. Suggested evidence root is the host's normal log area under pr5305-ignored-fields-r1/<host>-<UTC>; record the actual path. Keep runner and evidence outside the source tree.

If tidy changes tracked source, commit the scoped formatting repair with Nick's identity, plain-push only this fork branch, separately verify the new remote SHA, and post an add-only report and superseding gate pinned to that head before proof. Source/test repairs require the same treatment. This job cannot prove a modified head. Do not weaken assertions or silently exclude tests/tasks. Record and investigate every executed-test failure before a repair retry; an environment refusal before any test executes may be relaunched once under WORKFLOW.md. Mockito assumptions/skips or fixture startup failure do not satisfy this gate.

## 1. Fail-before proof on O implementations with F tests retained

Create a fresh detached worktree at F. Record F/tree and the hashes of both modified test files. Restore only the following seven implementations from O:

```bash
git restore --source=00292cd3729126194235e939afe38e351d850c70 -- solr/core/src/java/org/apache/solr/schema/BinaryField.java solr/core/src/java/org/apache/solr/schema/BoolField.java solr/core/src/java/org/apache/solr/schema/CollationField.java solr/core/src/java/org/apache/solr/schema/SortableTextField.java solr/core/src/java/org/apache/solr/schema/TrieField.java solr/modules/analysis-extras/src/java/org/apache/solr/schema/ICUCollationField.java solr/test-framework/src/java/org/apache/solr/schema/SortableBinaryField.java
```

Save the exact diff and source/test hashes. Stage only those seven restored files in this detached overlay's index and record `git write-tree` as its synthetic overlay tree ID; HEAD remains F. Require each restored file to be byte-identical to O and both test files to remain byte-identical to F. The StrField production fix and BasicFunctionalityTest from the contributor remain present. Do not push this overlay.

Run these as separate recorded commands so the expected first failure does not prevent the ICU/control legs from executing:

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.schema.PrimitiveFieldTypeTest -Ptests.seed=18627C0DE -Ptests.failfast=false -Ptests.maxfailures=100
```

Expect 10 executed methods: exactly the six methods below fail/error with the intended NullPointerException at the named field type's List.of null boundary; the other four methods pass.

| Negative method in PrimitiveFieldTypeTest | Expected originating implementation |
| --- | --- |
| testIgnoredBoolField | BoolField.createFields |
| testIgnoredBinaryField | BinaryField.createFields |
| testIgnoredSortableBinaryField | SortableBinaryField.createFields |
| testIgnoredSortableTextField | SortableTextField.createFields |
| testIgnoredCollationField | CollationField.createFields |
| testIgnoredTrieFields | TrieField.createFields (first concrete case: TrieIntField) |

Passing controls are testIgnoredStrField, testDocValuesOnlyFields, testDefaultOmitNorms, and testDateField. The unfixed Trie test stops at its first failing concrete class; all five concrete classes are required in positive proof. Save fresh logs and XML before another cleanTest.

```bash
./gradlew :solr:modules:analysis-extras:cleanTest :solr:modules:analysis-extras:test --tests org.apache.solr.schema.TestICUCollationField -Ptests.seed=18627C0DE -Ptests.failfast=false -Ptests.maxfailures=100
```

Expect 9 executed methods: only testIgnoredField fails/errors at ICUCollationField.createFields / List.of(null); testDocValuesOnlyField and the seven existing methods pass. Save fresh logs/XML. No skip or Mockito assumption counts as execution of the required cases.

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testIgnoredFields -Ptests.seed=18627C0DE
```

Expect one executed method passing: O already repairs the reported ignored StrField indexing/commit/query path. Do not report this control as a new fail-before regression. Compilation failure, missing matching methods, configuration startup failure, an unrelated exception, or an unexpected pass on any named negative is a failed proof leg to investigate.

## 2. Positive proof at exact clean F

Use a separate clean detached F worktree. Reverify local head/tree, remote full F, exact O-to-F file list, and unchanged tests. Run `./gradlew tidy`, require success and no tracked changes, and record `git diff --check`. If a formatting repair is required, follow the new-head/superseding-job rule above.

Run both complete modified test classes:

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.schema.PrimitiveFieldTypeTest -Ptests.seed=18627C0DE
./gradlew :solr:modules:analysis-extras:cleanTest :solr:modules:analysis-extras:test --tests org.apache.solr.schema.TestICUCollationField -Ptests.seed=18627C0DE
```

Require all 10 core and all 9 ICU methods to pass, zero errors/failures/skips. Verify all seven ignored core methods, both preservation methods across the two classes, and the ICU ignored method actually execute. Positive ignored cases cover single/multivalue shapes and all five Trie subclasses. Binary docValues preservation is intentionally single-valued because BinaryField rejects multiple docValues. Save fresh logs/XML and method counts before subsequent cleanTest tasks. Any different method count must be explained by actual method names, not silently accepted.

Run the existing integration and neighboring field suites at F:

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest --tests org.apache.solr.schema.BooleanFieldTest --tests org.apache.solr.schema.TestBinaryField --tests org.apache.solr.schema.TestSortableTextField --tests org.apache.solr.TestTrie --tests org.apache.solr.schema.TestCollationField --tests org.apache.solr.schema.TestCollationFieldDocValues --tests org.apache.solr.schema.DocValuesTest --tests org.apache.solr.schema.DocValuesMultiTest --tests org.apache.solr.schema.TestUseDocValuesAsStored -Ptests.seed=18627C0DE
./gradlew :solr:modules:analysis-extras:cleanTest :solr:modules:analysis-extras:test --tests org.apache.solr.schema.TestICUCollationFieldDocValues -Ptests.seed=18627C0DE
```

Require success, fresh per-class XML and exact counts with zero failures/errors. Explicitly require BasicFunctionalityTest.testIgnoredFields to execute and pass. Identify any existing assumptions/skips individually and establish that no new regression or required boundary was skipped; otherwise the leg remains incomplete. These suites cover normal indexed/stored values, binary and string/boolean docValues, sortable-text behavior, concrete Trie numerics/dates, and both collation implementations.

## 3. Build, formatting and root validation

At the same clean F, run root validation:

```bash
./gradlew check -x test
```

Require rc 0 with no added task exclusions. Focused/neighbor test tasks provide compilation and test proof for core, analysis-extras and their test-framework dependency; root check provides required project validation. Record any material execution conditions and resolve failures under the repair rules. Existing public CI success is not a replacement for these exact-F legs.

Finish with clean tracked/untracked status, clean diff-check, unchanged test hashes and source head/tree, and a separately read live fork ref still equal to F. Never push an overlay or mutation.

## Receipt and completion

GREEN requires all three fail-before/control commands, both focused positive suites, both neighboring-suite commands, clean tidy and root validation at the final pinned head. Record every command/seed/return code, actual test method counts and skips, exact origin of each expected negative failure, fresh logs/XML and hashes, source/test overlay hashes, repairs/environment conditions and final local/remote heads. The six core negatives include the test utility; the six newly repaired production classes are five core classes plus ICU. Preserve this distinction in public wording.

The docValues assertions are preservation coverage pins unless separately shown to discriminate a mutation; do not claim every assertion is mutation-proven. No additional synthetic mutation is required by this job.

Write receipts/PR-5305-ignored-fields-round-1.md and an add-only execution outcome report, leaving this job and implementation claim unchanged. Include a factual updated PR-description draft reflecting BasicFunctionalityTest.testIgnoredFields and the new sibling coverage, with executed results and limits. A successful receipt may offer only its verified head. No PR/JIRA comment, PR-body edit, approval, merge, history rewrite or contributor-fork write is part of this gate. Public follow-up remains subject to the root clean-gate authorization and any later draft-review hold.

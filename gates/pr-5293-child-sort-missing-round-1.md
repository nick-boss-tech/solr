# SOLR-18622 childfield missing-value reproduction and test gate, round 1

Status: OPEN

Tags: premise-run, gate, review, records.
Intended host: any capable Linux pool host with JDK 21, the branch's pinned Gradle/build dependencies, Git and Python 3; serialize all builds under the host build lock.

## Identity and ownership

- PR: https://github.com/apache/solr/pull/5293, SOLR-18622.
- Implementation claim: `claims/pr-5293-child-sort-missing-windows-round-1.md`.
- Implementation/correction report: `reports/pr-5293-child-sort-missing-windows-round-1.md`.
- Contributor parent: `978e2b1e352c7c39a68f7b952e6ab12eabf80890`.
- Fork branch: `pr-5293-solr-18622-child-sort-missing`.
- Exact pushed test head: `769ca6e177d77acd3f1993a4a24bd3257ae243c0`.
- Receipt, owned by the execution host: `receipts/pr-5293-child-sort-missing-round-1.md`.
- No claim of a production fix. The earlier default-child regression allegation is withdrawn. Preserve the contributor's deliberate null parent missing-value choice.

Read the report before claiming. Check the live fork ref and PR head. If the fork head differs, stop and reconcile; do not silently retarget. Record PR movement separately. Claim with a new file, name the detached runner and build lock, use a clean standalone checkout with no Windows path assumptions, and retain logs and fresh XML outside overwritten build-output paths.

## Questions this job must settle

R1: Does an excluded sibling without a sort field incorrectly supply a missing value to the matching child's numeric min/max selection? Lucene 10.6 counts physical block size against filtered valued-child count. The expected order in `testFilteredChildrenDoNotSupplyMissingValues` ignores the excluded child.

R2: Does descending string sorting with missing values first pass the synthetic MAX_VALUE ordinal to a term lookup? `testStringChildMissingFirst` retains the null parent fallback and requires a normal ordered response in both directions.

These are source hypotheses, not reproduced defects. The other new assertions pin behavior and may both pass on contributor and augmented suites. Do not call them regression proof without runtime discrimination. If a new test fails, capture and investigate the actual assertion/exception; do not weaken its expectation or turn it into expectThrows merely to make the test patch green.

## Execution plan

Use seed `18622C0DE`, JDK 21 and separate clean checkouts for baseline and candidate. Before a gate claim, run the detached tidy preflight at the candidate head. Run `./gradlew tidy` in a disposable checkout and compare the worktree to the exact head; if formatting changes, retain the diff and report a repair requirement. No result for a changed tree proves the named head. A formatting repair requires a new pushed commit and superseding gate.

1. Baseline at contributor parent: run `./gradlew :solr:core:test --tests org.apache.solr.search.join.TestNestedDocsSort -Ptests.seed=18622C0DE`. Expected: the original suite passes. Save command, JDK, SHA, exit code, log and fresh XML.
2. Candidate at the exact test head: run each new method in a separate invocation so one failure does not hide the others:

```bash
./gradlew :solr:core:test --tests org.apache.solr.search.join.TestNestedDocsSort.testNumericChildMissingValues -Ptests.seed=18622C0DE
./gradlew :solr:core:test --tests org.apache.solr.search.join.TestNestedDocsSort.testStringChildMissingFirst -Ptests.seed=18622C0DE
./gradlew :solr:core:test --tests org.apache.solr.search.join.TestNestedDocsSort.testStringChildMissingLast -Ptests.seed=18622C0DE
./gradlew :solr:core:test --tests org.apache.solr.search.join.TestNestedDocsSort.testFilteredChildrenDoNotSupplyMissingValues -Ptests.seed=18622C0DE
```

Save fresh log/XML after each invocation before the next overwrites it. None has a passing result yet. A compile/setup failure is not a reproduction. Only first execution of each independent method is authorized without a failure diagnosis. On a failure, diagnose before a retry; other independent methods may continue.

3. If the candidate methods pass, run the full `TestNestedDocsSort` and neighboring `TestCloudNestedDocsSort` via `:solr:core:test`, same seed, then `./gradlew check -x test`. Preserve no exclusions. Record material environment conditions rather than presenting an earlier setup failure as a pass.
4. If all positive legs pass, demonstrate detection with isolated compiling mutations and unchanged tests, each in a separate disposable checkout:
   - Change only the child missing-value constructor argument in `ChildFieldValueSourceParser` from `missingValue` to null. Numeric first-policy assertions should distinguish ignored child missing values. Do not count string/default assertions as proven by this mutation.
   - Change only parentMissingValue from null to `missingValue`. Numeric all-missing/empty-parent ordering should distinguish the preserved parent fallback. This is synthetic discrimination, not a proposed production fix.
   Retain each exact mutation diff, intended assertion, command/exit, fresh logs/XML and observed assertion. A mutation unexpectedly passing is a gap; no blind retries. Compile/startup failures are not runtime proof.

## Failure and closeout rules

- If R1 or R2 fails at runtime, report the actual failure and whether it comes from Solr wiring or the dependency. Do not invent a Solr workaround or reverse the contributor's parent-policy choice. Post a source repair recommendation or a new implementation assignment if the required repair is established. Any production repair needs a new pushed head and superseding gate. This job can close with a factual failing receipt; it cannot authorize a clean tested-fix comment.
- An environment refusal before tests run may receive one documented host setup repair/relaunch. Executed failures require diagnosis before retry. Do not weaken security settings, silently skip tasks, or reuse stale XML.
- Report checks independently: baseline, each new method, neighboring suites, tidy, root check, each mutation. If a prerequisite fails, remaining legs are explicitly unrun. Do not label this complete/green from upstream CI or a narrow passing method.
- The receipt names exact tested heads, host/JDK, commands, exit codes, test counts, log/XML paths and any unrun legs. Its final section drafts the concise author-facing result with a pinned diff; no public posting by the execution host.

# Gate SOLR-18325 Maven mutations and artifact API evidence round 1

Status: OPEN

- Tags: gate, premise-run, review, records. Intended host: any capable Linux host with JDK21 for Solr/Gradle, JDK17 for consumer execution, Python3, Git and network access for Maven setup if required. Prefer the existing execution host while its artifacts remain available. Serialize under the host build lock.
- Candidate fixed head: `8bfca8edd30acf11bdf9d6a733312e97f99ec78a`, fork `solr-18325-submit`.
- Exact pre-Jackson source base: `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`.
- Released baseline: Maven Central SolrJ and Solr API10.0.0.
- Prior receipts: `receipts/SOLR-18325-solr10-feasibility.md` and `receipts/SOLR-18325-binary-attribution-round-1.md`. Raw existing evidence: `/workspace/gates/logs/vm2/solr18325-binattr/` and the round2 directories named by the receipts.
- Handoff assessment: `reports/solr-18325-evidence-closeout-windows-round-1.md`.
- Deliverables: single-writer new receipt `receipts/SOLR-18325-evidence-closeout-round-1.md`, report `reports/solr-18325-evidence-closeout-linux-round-1.md`, and design report `reports/solr-18325-http-compatibility-prerequisite-design-round-1.md`.
- Seed for repository tests: `1832510C0FFEE`. Windows does not run these commands.

## Scope and state

The three-runtime baseline control is DONE. Do not repeat it or reopen attribution: the observed timeout failure is inherited and the binary requirement remains unsatisfied. This job completes the specifically unrun evidence and supplies a reasoned prerequisite design. It does not waive tests, restore classes, implement a shim, change the production candidate or declare binaryHttp green.

Refresh board/fork state and competing claims, record host lock, toolchains, runner, timestamps and evidence paths. Verify the candidate source head and full artifact hashes from the prior receipt. Keep released, base and candidate artifacts in isolated repositories so identical snapshot names cannot select the wrong bytes. Reuse prior valid proof for the same fixed head; do not rerun the large test/build sets without a new concern or changed source.

## 1 Actual Maven consumer

If `mvn` is absent, set it up: download a supported official Apache Maven binary distribution, verify its published checksum, unpack under a user-owned tools directory and invoke its literal path. No sudo or machine-wide install is required. Record distribution URL/version/checksum and `mvn --version`. Use a host already holding Maven if network/setup prevents installation; absence of a command is a setup limitation, not product failure.

Use an isolated local Maven cache and a minimal external POM resolving the previously published candidate artifacts through their build-local repository and Maven Central for other dependencies. Read actual versions and snapshot metadata; do not guess a build timestamp or overwrite base snapshots with candidate files. No dependency exclusion, forced Jackson version, shading, source substitution or modified publication POM. Canonical filename copies of identical published bytes are allowed only when required by the already documented local snapshot layout, with source/destination hashes and the reason recorded.

Record the effective POM, verbose dependency tree, runtime classpath and full candidate JAR/POM hashes. Require tools.jackson core/databind3.2.1 and shared annotations, with no com.fasterxml.jackson.core:jackson-core/databind on a clean candidate consumer runtime. Compile the normal consumer source against the candidate using release17 and execute it in an external Java17 JVM with only its output plus that resolved runtime. Keep build/plugin dependencies out of the runtime classpath. Record compiler and dependency plugin versions and exact commands. Preserve existing old-consumer bytes; a newly compiled Maven consumer is a source/publication check, not replacement for the failed released-binary leg.

If Maven resolves a different graph than the existing Gradle POM consumers, diagnose metadata and conflict-resolution rules before proposing a fix. Report the actual differences. Do not conceal a failure with exclusions or relabel Gradle output as Maven execution.

## 2 Discriminating mutations

Use clean separate detached candidate-based worktrees for mutants. Keep test bytes identical to the fixed candidate and record test blob hashes. Save exact mutation diffs/hashes and commands. Mutants are temporary experiment inputs and must never be pushed to the submit branch or published as the real candidate.

The fixed candidate's three relevant suites are `org.apache.solr.client.solrj.request.json.JacksonContentWriterTest`, `org.apache.solr.client.solrj.response.json.JacksonDataBindResponseParserTest`, and `org.apache.solr.client.solrj.response.CoreAdminJacksonCompatibilityTest`. Use fresh test execution, the same seed, `cleanTest` and `--no-build-cache`. The command shape is:

```sh
./gradlew --no-daemon :solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.client.solrj.request.json.JacksonContentWriterTest --tests org.apache.solr.client.solrj.response.json.JacksonDataBindResponseParserTest --tests org.apache.solr.client.solrj.response.CoreAdminJacksonCompatibilityTest -Ptests.seed=1832510C0FFEE --no-build-cache
```

Run these controlled variants separately, with minimal compilable production edits:

- Defaults mutant: remove Jackson2 compatibility settings, retaining explicit constraints and exception adaptation. The request-representation oracle should detect changes such as dates, enum names/toString or property order. Record actual failing methods.
- Constraint mutant: retain compatibility settings and exception adaptation, but use Jackson3 default read/write constraints. The 600-level read/write canaries and constraint parity checks should detect depth500 versus1000 and string100M versus20M.
- Error adaptation mutant: retain mapper settings/constraints but bypass IOException wrapping in ContentWriter/parser and IllegalArgumentException wrapping in conversion helpers. The malformed/input/output/empty-bean and invalid-conversion tests should detect the leaked unchecked exceptions.

Compilation/setup failures do not prove a mutation was detected. Each mutant must execute the intended assertions and fail for the intended contract difference; archive fresh XML, outputs, counts and exception types. If one compound variant obscures another boundary, split the mutation and record why. Run the same focused suites once on the untouched fixed head as a positive control for this comparison, or link an equivalent fresh exact-head run with identical test bytes. Do not alter assertions to match a mutant. Ordinary contract pins passing on the old implementation are not failing-before bug tests.

## 3 Full artifact API comparison

Use a documented Java binary API comparison tool suitable for Java17 jars, or a complete class-file inventory that includes access rules, descriptors, fields, constructors, superclasses/interfaces and relevant bridges/generic surfaces. Record tool/version/commands and full raw output. The prior inventory of one fixture's40 references is not a complete public/protected API diff.

Compare both solr-solrj and solr-api across these pairs: released10.0.0 to unmodified base; unmodified base to Jackson candidate; released10.0.0 to candidate. Verify source and artifact hashes for each. Attribute unrelated base evolution separately from the Jackson delta. For internal/experimental classifications, cite the pinned source declaration or policy evidence; public visibility alone neither guarantees support nor permits ignoring a break.

Classify every relevant changed public/protected signature, superclass, constructor and field. Keep the documented internal DEFAULT_MAPPER descriptor change visible as a known incompatibility. Do not assume an additive method is a break or that unchanged source ensures unchanged artifact selection. Report protected member and assignability risks for existing subclasses.

## 4 Compatibility prerequisite design

Based on the complete API inventory, give a definite recommended approach with exact affected files/member surfaces and tests. Assess an additive compatibility hierarchy preserving the current implementation versus restoration of the relevant original hierarchy. Cover both old top-level classes, fluent builder erased return descriptors, superclass assignability, old constructors/methods and protected state used by subclasses. A pair of empty alias classes is not presumed sufficient. Explain any extra bridge methods or implementation moves required and why the alternative is less suitable.

Include small old-compiled fixture proposals for both timeout calls and assignments/casts/subclasses through the old base and builder, plus HTTP core-status/query/update/cleanup on an unchanged10 server. Keep that repair separately scoped and branched from the Jackson experiment; this job requests its design, not production implementation or public submission. If policy rather than code prevents a recommendation, name the exact remaining choice and supply evidence and costs.

## Completion and verdict

Publish the new receipt and both reports with complete hashes, exact commands/exits, actual Maven and Java versions, resolved graph/effective POM, mutant diffs and fresh XML, complete API outputs and classified changes, and a recommended compatibility prerequisite design. Leave prior receipts/assignments unchanged.

Continue to state separately: Jackson-specific evidence relative to base; inherited release-to-base compatibility defects; absolute released-consumer result. The current binaryHttp still FAILS and overall client-first verdict remains INCOMPLETE until that requirement is actually met or the human owner explicitly changes it. Passing the new checks can strengthen the Jackson-specific finding but cannot waive the inherited ABI problem. Preserve the known SOLR-18587 core failure as inherited, not green.

No public GitHub/JIRA comment, PR, upstream write, production restore/shim, release approval, schedule change or full-server/3.1LTS proof is authorized. A source repair to the real candidate, if separately justified later, requires a new pushed head and superseding exact-head proof; do not attach old receipts to changed source.

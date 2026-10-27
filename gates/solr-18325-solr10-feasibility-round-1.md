# Gate SOLR-18325 Solr 10 client feasibility round 1

Status: OPEN

- Tags: implementation, gate, premise-run, review. Intended host: any capable Linux host holding these tags, JDK 21 for the Solr build, JDK 17 for consumer execution, the pinned Gradle distribution, Maven, Python 3, Git and access to build dependencies. One job at a time under the host build lock.
- Owner request: determine whether Jackson 3 is viable for Solr 10, using code/contracts, a candidate and real Linux proof. This is a feasibility experiment, not a public submission or release-approval job.
- Fork: nick-boss-tech/solr, branch `solr-18325-submit`.
- Incoming Windows prototype head: `aa8cc9ab29a012da2ec4e302cae73238e7e78a1f`.
- Exact unchanged Solr 10 source base: `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7` (apache/solr branch_10x, in-progress 10.2.0).
- Released old consumer compile/runtime baseline: Maven Central `org.apache.solr:solr-solrj:10.0.0`, as pinned in the standalone harness catalog.
- Preparation claim: `claims/solr-18325-solr10-prototype-windows-round-1.md`.
- Source assessment: `reports/solr-18325-solr10-feasibility-windows-round-1.md`.
- Receipt target, single writer is the Linux execution host: `receipts/SOLR-18325-solr10-feasibility.md`.
- Outcome report target: `reports/solr-18325-solr10-feasibility-linux-round-1.md`.
- Seed: `1832510C0FFEE` for all named repository test runs.

## The question and its limits

Can SolrJ move to Jackson 3 within Solr 10 while preserving its measured supported client contracts and letting Jackson 3 applications omit Jackson 2 core/databind? The candidate deliberately leaves server Jersey/providers/serializers on Jackson 2, including its CLI mapper. Shared Jackson annotations retain their existing package.

This job must report separate verdicts for (a) this client-first approach, (b) a complete server migration, and (c) release readiness. A client pass cannot close the full ticket or establish the server migration. The internal DEFAULT_MAPPER field has a known binary descriptor break; the negative control proves and discloses it. Databind-specific Jackson 2 annotations/custom serializers do not become Jackson 3 extensions automatically. Broad IOException contracts are adapted; exact old Jackson exception subclasses are not retained.

The prototype uses the already-cataloged Jackson 3.2.1 version. That isolates the Solr 10 question from changing JWT test dependencies. It is not proof for the ticket's preferred 3.1.x LTS target. The central platform constrains every catalog alias; adding a second 3.1 BOM alias does not isolate it from the existing 3.2.1 constraint. Record exact resolved versions and a bounded follow-up for the production-line choice.

## Claim and preparation before proof

Refresh the live board, fork head and competing claim/receipt. Use a full detached Linux checkout; never reuse another task's working tree. Record incoming head, host/toolchain, build lock, full source/evidence paths, runner, commands, UTC timestamps and all exit codes. Windows ran no Gradle, compilation, Java test or HTTP probe.

The incoming prototype intentionally has no generated lock/license changes because Windows forbids the required Gradle execution. This job authorizes a preparation phase on Linux:

1. Run a detached preflight as far as dependency state permits before claiming the proof. A missing new coordinate in strict lock state is preparation pending, not an upgrade test failure. Do not count setup as a test result.
2. Generate dependency state and license/checksum metadata with the branch's documented `./gradlew updateLicenses resolveAndLockAll --write-locks`; inspect every change. Keep Jackson 2 runtime dependencies required by the existing server and test oracles. Do not remove them wholesale to make the standalone consumer graph green. Record the SolrJ runtime versus test/server graphs separately.
3. Run `./gradlew tidy`, inspect formatting/source/compiler failures and repair this bounded prototype/harness where needed. Add new license metadata only from authoritative upstream material and with correct ASF handling. Do not make unrelated dependency upgrades, weaken assertions or change supported client contracts to obtain a pass.
4. Commit required generated/tidy/compile repairs with Nick Shanin's ICLA identity and no attribution trailers, push normally to the same fork branch, and verify remote head. Record the parent and new SHAs and why each changed file is needed. Add a superseding exact-head gate file before final proof runs if the incoming head changed. Final proof is only for that new fixed head; do not attach results to the earlier Windows head.

No changelog/public PR was prepared for this experimental slice. A release contribution needs its own approved scope, changelog/docs and final preparation after the feasibility result.

## Repository tests and behavior controls

Run the following on the final pinned candidate head, with fresh XML and no cached/up-to-date-only test result. Full logs and XML/output paths must be preserved. The example filters are exact classes present in the incoming branch.

```sh
./gradlew --no-daemon :solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.client.solrj.request.json.JacksonContentWriterTest --tests org.apache.solr.client.solrj.response.json.JacksonDataBindResponseParserTest --tests org.apache.solr.client.solrj.response.CoreAdminJacksonCompatibilityTest --tests org.apache.solr.client.solrj.response.SystemInfoResponseTest --tests org.apache.solr.client.solrj.request.TestCoreAdmin --tests org.apache.solr.client.solrj.embedded.TestSolrProperties -Ptests.seed=1832510C0FFEE --no-build-cache
```

```sh
./gradlew --no-daemon :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.admin.CoreAdminCreateDiscoverTest --tests org.apache.solr.handler.admin.CoreAdminHandlerTest --tests org.apache.solr.schema.TestCloudManagedSchema --tests org.apache.solr.schema.TestManagedSchema --tests org.apache.solr.jersey.SolrJacksonMapperTest --tests org.apache.solr.cli.StatusToolTest --tests org.apache.solr.cli.StatusToolPicocliTest -Ptests.seed=1832510C0FFEE --no-build-cache
```

Require all named classes to execute without skips and report per-class counts. The SolrJ test runtime deliberately contains Jackson 2 oracle/server dependencies; these tests cannot prove a clean consumer graph. The independent published-artifact leg below does that.

Contract pins: dates/numerics, enum name versus overridden toString, property order, nulls, shared annotations, nested arrays, stream close/flush behavior, unknown fields, malformed/truncated input, explicit UTF-16LE and stream errors, v2 error extraction, broad IOException/IllegalArgumentException adaptation. The 600-level read/write canaries must succeed. Read constraint parity must retain depth1000/string20M and unchanged number/name/document/token limits.

These ordinary contract tests should also pass on the previous mapper; they are compatibility pins rather than claimed fail-before bug tests. For a discriminating mutation proof, prepare a separate detached candidate-based mutation that removes only the compatibility defaults/explicit constraints, carry identical tests, and show the date/enum/default and depth canaries detect it. A separate mutation removing exception adaptation should be caught by malformed/input/output/empty-bean IOException tests. Record actual failing methods; if a mutation is not run, state that limitation, never invent fail-before numbers.

## Published artifact and old-bytecode consumers

Publish only to the build-local Maven repository. The documented root task is:

```sh
./gradlew --no-daemon mavenToLocalFolder
```

It creates `build/maven-local` and supplies candidate Solr API/transitive artifacts as well as SolrJ. A justified narrow equivalent may publish SolrJ plus every required internal artifact, with its exact commands recorded. Never publish Apache snapshots or external packages. Read the actual candidate version from its generated SolrJ POM, rather than guessing a suffix. Record JAR/POM hashes and the final source SHA.

`gradle/maven/defaults-maven.gradle:63` disables GenerateModuleMetadata. Inspect the POM; do not demand a nonexistent `.module` file. The supplied Gradle harness disables module metadata and uses that POM. Also resolve a small external Maven consumer through the build-local repository, retain its effective POM and dependency tree/classpath, and confirm the same runtime result without exclusions/forced versions. Reuse the supplied Java fixture if desired; record the Maven version/plugin versions and exact commands.

Run the supplied standalone harness with installed/configured Java 17 and 21 toolchains. `<candidate-version>` must be replaced with the actual published version:

```sh
./gradlew --no-daemon -p dev-tools/jackson3-solrj compatibility -PcandidateVersion=<candidate-version> -PconsumerJavaVersion=17
```

```sh
./gradlew --no-daemon -p dev-tools/jackson3-solrj compatibility -PcandidateVersion=<candidate-version> -PconsumerJavaVersion=21
```

The wrapper itself runs on its supported build JDK; JavaExec selects the requested consumer toolchain. Record actual launcher versions, not just compile release17.

All lanes are required: baselineConsumer, candidateConsumer, binaryConsumer (compiled against released10.0.0, executed unchanged on candidate-only runtime), baselineBehavior, candidateBehavior, jackson2Coexistence, baselineInternalFieldControl and internalFieldControl. The latter must report its expected NoSuchFieldError with application Jackson 2 retained; the baseline field control must succeed. A failed dependency resolution, compilation or unexpected linkage error is not that expected control.

Require no `com.fasterxml.jackson.core:jackson-core` or `jackson-databind` on the unmodified candidate runtime graph; require tools.jackson core/databind plus shared annotations. No excludes, shading, source substitution, compiling the old fixture against the candidate, or forced versions to hide old runtime dependencies. Save complete logs and `dev-tools/jackson3-solrj/build/reports/compatibility/*.txt`, including old class hashes and all resolved graph coordinates. Compare baseline/candidate printed stream-ownership signatures. Run the dependency checker against published baseline10 as a documented negative graph control if useful; its Jackson 2 presence is expected.

Generate a public/protected signature diff for the published SolrJ and API jars against the released baseline, using an appropriate available tool, and classify every change relevant to this patch. Existing branch10 evolution must be separated from this prototype: compare final head against the exact source base as well. Do not treat a single sample consumer as universal binary compatibility proof.

## Unchanged Solr 10 server over HTTP

Use a disposable standalone core with indexed/stored unique key `id`, on an unchanged Solr10 server: an official verified10.0.0 distribution or the exact source base above. Record distribution checksums or source SHA, startup/core-creation commands, config, JVM and evidence paths. Do not use a production/shared core. This proof requires actual server I/O and cannot be replaced by synthetic NamedLists.

```sh
./gradlew --no-daemon -p dev-tools/jackson3-solrj candidateHttp binaryHttp -PcandidateVersion=<candidate-version> -PconsumerJavaVersion=17 -PsolrUrl=http://127.0.0.1:8983/solr -PsolrCollection=jackson3_compat
```

Both lanes request real core status and convert startTime/uptime through the changed client code, then add/commit/query/delete their own UUID document. The core-status request's longstanding experimental status is separate from the ordinary offline API linkage sample. Record responses and cleanup. The same old class bytes must be used for binaryHttp. Repeat on21 if an unexplained JVM-dependent difference appears. The candidate-server core tests above exercise both runtimes coexisting in the new server build.

## Checks and decision

Run final `./gradlew tidy` with a clean tracked tree and `./gradlew check -x test` at the exact final head. Root check and module tests are distinct evidence. Diagnose environment/setup failures and name any exclusions; an unexplained failure or skipped required leg cannot become a complete pass. A non-executing environment attempt may be repaired/relaunched once. After actual test failures, investigate before a bounded repair/retry; do not blindly rerun or widen assertions away.

Receipt must contain: incoming/final/base/released artifact heads/versions; all prep commits and verified remote head; exact toolchains/commands/exits; fresh XML counts and full logs; mutation or explicit unrun status; publication artifact hashes/POM/effective Maven model; Maven and Gradle graphs; unchanged old class hashes; all positive/negative control outcomes; HTTP baseline/config/observations; tidy/root check results; remaining questions.

Use these verdicts precisely:

- Client-first TECHNICALLY VIABLE FOR MEASURED CONTRACT only if compilation, focused behavior, old-bytecode consumer, clean published dependency graphs, Java17 execution, coexistence and baseline-server HTTP all pass with no unresolved patch concern.
- Otherwise INCOMPLETE or NOT VIABLE AS WRITTEN, with a concrete failing boundary and proposed repair or blocker. A host setup failure is not a design impossibility.
- Full server migration NOT ESTABLISHED by this job regardless of client result.
- Release-ready NOT ESTABLISHED: maintainers still decide internal/experimental breaks, exact3.1LTS versus3.2 line, release scope and contribution packaging. No public post is made automatically.

The outcome report and receipt close the Linux claim. No PR opening, GitHub/JIRA comment, upstream push, or public body edit is authorized.

# Gate premise: SOLR-18600 dual-release HTTP ABI investigation, round 1

Status: OPEN. Diagnostic/design proof, not a promotion gate for the existing restoration.

- Tags: gate, premise-run, review, records. Intended host: any capable Linux gate host; vm2 preferred while its prior artifacts are available. The preference is not exclusive ownership. Follow hosts/pool-linux.md, the host build lock and current claims.
- Request: Nick asked whether further checks should go to the gate. Post this bounded investigation to establish the compatibility repair path for callers compiled against BOTH 10.0.0 and 10.1.0.
- Board snapshot used to prepare: f8ead2dafd41df1f3d775d346dd3277b17f22601.
- Pinned existing restoration: nick-boss-tech/solr:solr-18600-http-abi-restore-submit, c40665af8d6bd23c85fef565f93de5004a7b2ca4.
- Pinned unmodified branch_10x base: a336bf871b2c2b67abe55808966051d1c553b579.
- Released compile/runtime artifacts: org.apache.solr:solr-solrj:10.0.0 and 10.1.0, with the corresponding solr-api and version-appropriate Jetty module/dependencies where needed. Resolve actual published coordinates per release; do not assume the module/package arrangement is identical.
- Source controls: releases/solr/10.0.0 at 6c6c48a6f78486130682ea9c1f7a2723af5a87be; releases/solr/10.1.0 at 749712b71940c9f96d824c533f32f5c56dcd3924. Hash the actual jars used; source tags are not substitutes for artifact identity.
- Toolchain: JDK 21 for Solr/Gradle and direct compiler probes; compile released-consumer fixtures with --release 17 and use JDK 17 for their runtime compatibility leg when available. Record exact JDK paths/versions. If only JDK 21 is available, distinguish the JDK 21 linkage result from unrun Java 17 support proof.
- Deliverables: new report reports/solr-18600-dual-release-abi-premise-linux-round-1.md and single-writer receipt receipts/solr-18600-dual-release-abi-premise-round-1.md. Preserve scripts, consumer/prototype sources, full logs, descriptor inventories, classpaths and hashes under a declared stable artifact directory. Include small portable sources/commands in the report or a linked accessible artifact, not only host-local cache paths.

## Read and preserve prior findings

Read these at one refreshed board snapshot before claiming:

- assignments/vm2-solr-18600-premise-review-round-1.md
- assignments/vm2-solr-18600-premise-review-addendum-round-1.md
- assignments/vm2-solr-18600-upgrade-poc-review-round-2.md
- reports/vm2-solr-18600-premise-review-round-1-windows.md
- reports/vm2-solr-18600-upgrade-poc-review-round-2-windows.md
- reports/vm2-solr-18600-dual-release-direction-round-3-windows.md
- reports/solr-18325-http-compatibility-prerequisite-design-round-1.md
- receipts/SOLR-18600.md and reports/solr-18600-poc/

The JDK failure is established: 10.0-compiled timeout calls fail on published 10.1; a 10.1-compiled timeout call passes on the base but fails on both the four-core-file and ten-solrj-file restoration variants. The tested Cloud withHttpClientBuilder call compiled against 10.1 passes on those variants. Reuse these controls when artifact/source/fixture identity can be verified; do not rerun merely to recreate an existing table.

The raw probe directories on vm2 are gates/logs/vm2/solr18600-probe/ and /workspace/poc/solr-10-binary-break. Prior reports warn that /workspace/worktrees/pr5253-U-up holds probe edits. Inspect status and preserve any needed work before its owner cleans or replaces it. Never reset or repurpose another live task's tree.

Owner direction is settled for this investigation: preserve both release cohorts in one repaired runtime. RC3's vote is closed, per Nick. Do not turn this into an open-vote objection or another restore-versus-document policy question. No 10.1-compiled-on-10.0 runtime requirement is imposed.

## Phase A: API and fixture inventory

1. Refresh the live fork head, assignment and competing claims. Pin every read/build to the SHAs above. If the fork has moved, record the discrepancy and read the newer work before deciding whether this job remains applicable; do not silently substitute a new candidate. Claim this job with UTC time, toolchain, fixture/log paths and host identity. Use the normal gate preflight and host resource rules for any Solr build.
2. Inventory the public/protected descriptor closure of the HTTP client family for released 10.0, released 10.1, the unmodified base, and c40665af8d6b. Include supertypes/interfaces, constructors, fields, generic erasures, bridge methods and instance/static/access differences. Use javap plus a resolver or japicmp/equivalent. Save machine-readable data and the human-readable delta. A name-only grep or source compilation is not ABI proof.
3. Account for the moved old/new class names in each cohort. Inventory these families and their actual release members: HttpSolrClientBase, HttpSolrClientBuilderBase, HttpSolrClient and nested BuilderBase; HttpJdk and Jetty clients/builders; Cloud client/builders; LBSolrClient.Builder and LBAsyncSolrClient; HttpClusterStateProvider; ConcurrentUpdate client/builders; SolrRequest.processWithBaseUrl. Include zookeeper adapters where the chosen Cloud signatures affect them.
4. Distinguish the ordinary public call contract from protected extension support. List known HTTP-family incompatibilities and any explicit exclusions. Include both 10.0 serverBaseUrl and 10.1 baseUrl if preserving the released protected fields is part of the proposed surface. Keep unrelated NamedList/MapWriter/solr-api/Jackson migrations outside the repair; list them separately if a whole-artifact tool reports them.

## Phase B: unchanged released consumers

Create or reuse separate immutable consumer outputs compiled against each release's actual jars. They may use different source names where the APIs were renamed, but each fixture must exercise a real released member and retain its class bytes on every runtime leg. Hash source and class files before and after runs. Preserve complete compile/runtime classpaths and dependency versions; do not silently compile an old fixture against the candidate.

| Cohort | Required positive control | Candidate result required of a future repair |
| --- | --- | --- |
| Compiled against 10.0.0 | Runs on released 10.0.0 | Same bytes run on the same repaired candidate |
| Compiled against 10.1.0 | Runs on released 10.1.0 | Same bytes run on the same repaired candidate |

Complete the currently missing rows against c40665af8d6b, preserving the known failures rather than repairing them in this phase:

- JDK and Jetty timeout/default fluent calls, build bridges and withHttpClient signatures.
- Cloud builder overloads and actual getHttpClient invocations through each applicable released static type. A passing builder overload is not a getter test. Released 10.1 DOES have withHttpClient(HttpSolrClient); name the exact descriptor in every case.
- LB builder constructor/getter and a minimal requestWithBaseUrl dispatch control for the supported restored-base route.
- HttpClusterStateProvider constructor/getters and client cloning where the restored builder path changes behavior.
- ConcurrentUpdate builder constructors/getters and transport ownership as applicable to the promised API.
- Old and current SolrRequest.processWithBaseUrl overloads.

Use focused fixtures and existing test infrastructure. Construction-only or in-process stub fixtures are sufficient for linkage when they invoke the real member. State clearly whether a row proves linkage, behavior, or both. Do not replace the class under test with a stub or fabricate an uninitialized object to claim an ordinary consumer path passed. If transport work is needed for dispatch/ownership, use a disposable local endpoint; never a shared or production service. Record unconstructible/unrun cases explicitly with a bounded next step.

Capture Java's real exit status and full output before filtering it for the report. The posted demo's grep/head pipelines hide status and its deps.txt has vm2-specific paths; do not use that script unchanged as an acceptance runner. Create needed directories, resolve/pin dependencies portably, validate artifact hashes, and assert the expected outcome of each leg. A positive-control setup failure must be repaired or classified before attributing a candidate failure.

## Phase C: prove a dual-descriptor design

Use a minimal isolated Java compiler/linker prototype to test the smallest design for each discovered family. Do not treat an idea as proved until released-shaped binary callers resolve and execute against the same prototype.

- For fluent builders, test legacy superclass plus explicit newer nested-base forwarding/covariant overrides. Verify BOTH erased return descriptors and relevant build/withHttpClient bridges.
- For parameter and constructor changes, retain old and new overloads as needed; verify generic-bound erasure explicitly.
- For changed getter result types, retain the newer narrowed return and investigate an inherited compatibility declaration or interface that allows javac to generate the legacy covariant bridge. Java source cannot declare two same-parameter methods differing only in result type. A differently named getter alone cannot repair old bytecode.
- Preserve released fields and current virtual URL/routing semantics within the proposed support boundary. Check the source-review observation that the restoration reads serverBaseUrl where 10.1 used virtual getBaseURL().

Prototype classes are descriptor/design evidence only. They do not establish Solr transport behavior or a passing production patch. Save their complete source, commands, generated descriptors and binary callers. If a design fails, record the actual incompatibility and test a justified alternative; do not weaken the fixture or drop one cohort to make the table green.

If source integration is necessary to settle a specific question, record a new isolated prototype input derived from the pinned base and save the exact diff before the run. Mark the result as prototype evidence. This job does not authorize changing the ticket submit branch, force-pushing, or merging an experimental repair into the current candidate.

## Outcomes and handoff

The report must deliver:

- A member-level list of actual 10.0 and 10.1 failures in the current restoration, separating executed failures, descriptor-only findings and unrun cases.
- The minimal compatible design justified by the compiler/linker experiments, or a precise remaining obstacle with the attempted variants and their results.
- An exact source-file/descriptor fix specification, proposed regression fixtures and the supported API boundary. Do not send back the already settled question of which release's callers to preserve.
- A recommendation for permanent regression protection: retained old-compiled consumers, a versioned descriptor baseline/ABI check, or both. Explain which classes of failure each detects and how fixture provenance is retained. Do not add a repository-wide new dependency or blanket ABI policy in this diagnostic job.
- A phase-specific implementation/proof handoff. Once a real repair is pushed, its new head needs fresh Linux tidy, Error Prone compile, named SolrJ/Jetty/Cloud/LB/ConcurrentUpdate tests, relevant module checks and both immutable release cohorts against that EXACT head. c40665af8d6b's existing receipt cannot cover the repaired head.

No need to repeat full core tests, root check or the old large suites solely to establish this premise. Run the smallest new discriminating checks first. Do not mark the existing restoration compatible, change the original receipt, or report a release-ready patch from prototype success.

Receipt verdicts: COMPLETE WITH DESIGN if inventory, missing controls and viable prototype evidence establish a bounded implementation plan; INCOMPLETE with the exact remaining obstacle otherwise. In either case the current production patch remains HOLD until a fresh repaired-head gate passes. Publish the report and this job's new receipt as add-only deliverables, with paths and actual timestamps. No PR, JIRA update, mailing-list send, upstream write, release vote or submission approval is authorized.

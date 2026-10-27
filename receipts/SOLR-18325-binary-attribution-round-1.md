# Receipt: SOLR-18325 released binary baseline attribution (vm2, Linux, round 1)

Gating host: vm2 (Linux). Job: `gates/solr-18325-binary-attribution-round-1.md` (fork commit `9d8a65fe807`). Claim: `claims/solr-18325-binary-attribution-linux-vm2-round-1.md`. Report: `reports/solr-18325-binary-attribution-linux-round-1.md`.

## Verdict

- Client-first verdict remains INCOMPLETE. `binaryHttp` is NOT passed.
- Released 10.0.0 consumer bytes PASS against released 10.0.0 runtime (positive control).
- The unchanged pre-Jackson base FAILS with the same `NoSuchMethodError` as the Jackson candidate, before any HTTP operation.
- The Jackson candidate adds no failure beyond the base in this comparison. Both fail on the same builder return descriptor.
- The binary defect is inherited from the pre-Jackson base, not caused by the Jackson change. This is a separate compatibility prerequisite. It does not waive the binary requirement.
- No waiver, shim, restore, production source change, release decision, GitHub, PR, Jira, or upstream write.

## Inputs and hashes

- Candidate: `nick-boss-tech/solr:solr-18325-submit` at `8bfca8edd30acf11bdf9d6a733312e97f99ec78a`. Verified: `git rev-parse HEAD` in `/workspace/gates/solr18325/cand` returns this head, clean tree.
- Pre-Jackson base: `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`. Verified: `/workspace/gates/solr18325/base` at this head, clean tree.
- Released control: Maven Central `org.apache.solr:solr-solrj:10.0.0`.
- Old consumer: `StableConsumer.class` compiled against released 10.0.0 in round 2 (`/workspace/gates/solr18325/cand/dev-tools/jackson3-solrj/build/classes/java/legacy/...`), copied to `/workspace/gates/logs/vm2/solr18325-binattr/old-classes/`. SHA-256 `027d21fdb1d79c133aab4e4bb4f109f9d17baa05b822c7457e1c9add44bdf18a`. Hash unchanged before and after every leg. Round 2 compile manifest `legacy-compile.txt`: `solr-solrj:10.0.0`, `solr-api:10.0.0`, `jackson-annotations:2.20`, `swagger-annotations-jakarta:2.2.22`.
- Base artifact (published by `./gradlew --no-daemon mavenToLocalFolder` in the base tree, JDK 21.0.12.1+1, Gradle 9.7.0, under `/workspace/tools/.gradle-vm2.lock`, BUILD SUCCESSFUL in 5m 57s, log `base-publish.log`):
  - `solr-solrj-10.2.0-20261027.170400-1.jar` SHA-256 `8cc4e4abd0519dd7e7e785413a3a49d2edf5a43b441ff8ca02275d7a03d51b15`
  - `solr-solrj-10.2.0-20261027.170400-1.pom` SHA-256 `5f21794d4ef04b5931adfe11ae250db10413355a8edad41363983db9e73ecd35`
  - Copy at `/workspace/gates/logs/vm2/solr18325-binattr/base-maven-local/`, hashes in `base-publication-sha256.txt`.
- Candidate artifact reused from round 2 (`/workspace/gates/solr18325/cand/build/maven-local`). Verified the complete hashes against the round 2 record `r2/artifact-sha256.txt`:
  - `solr-solrj-10.2.0-SNAPSHOT.jar` SHA-256 `e9a4236a75bf711f92c99266ea145f93ce0d2882e6bf04449c8c73bf8db7be3c`
  - `solr-solrj-10.2.0-SNAPSHOT.pom` SHA-256 `6e6f07ebc61bec034aea9216bc35ad0b60c85613c34b1f9033a4bcf7da7ed041`
  - Copy at `candidate-maven-local/`, hashes in `candidate-publication-sha256.txt`.

## Runtime graphs (resolved by separate external Gradle consumers, `consumer-released`, `consumer-base`, `consumer-candidate`)

These are Gradle consumers, not Maven executions. `mvn` is not installed on this host. The required check that a Maven consumer resolves the graph is recorded as unrun in the report.

- Released 10.0.0: solr-solrj 10.0.0, solr-api 10.0.0, jackson-core 2.20.0, jackson-databind 2.20.0, jackson-annotations 2.20, slf4j 2.0.17, swagger-annotations-jakarta 2.2.22, jakarta.ws.rs-api 3.1.0, semver4j 6.0.0, jspecify 1.0.0, jcl-over-slf4j 2.0.17.
- Base `f45b38d5f6c0`: solr-solrj 10.2.0-20261027.170400-1, solr-api 10.2.0-20261027.170400-1, jackson-core 2.22.3, jackson-databind 2.22.3, jackson-annotations 2.22, slf4j 2.0.20, swagger-annotations-jakarta 2.2.54, jakarta.ws.rs-api 4.0.0, semver4j 6.0.0, jspecify 1.0.0, jcl-over-slf4j 2.0.20. Jackson 2 is expected for this base.
- Candidate `8bfca8edd30`: solr-solrj 10.2.0-20261027.074222-1, solr-api 10.2.0-20261027.074222-1, jackson-core 3.2.1, jackson-databind 3.2.1, jackson-annotations 2.22, same slf4j/swagger/jakarta set as base.
- Full classpaths: `runs/<leg>.classpath.txt`.

## Server and runs

- Solr 10.0.0 standalone, official archive SHA-512 verified in round 2 (`server/solr-10.0.0.tgz.sha512`), Java 21.0.12.1+1, `bin/solr start --user-managed -p 8983 -m 1g` from `/workspace/gates/solr18325/server/solr-10.0.0`.
- Disposable core `binattr_compat` created by CoreAdmin CREATE with `configSet=_default` (`id` uniqueKey). Not a shared core.
- Java 17.0.21 consumer runs (`/workspace/jdk/jdk17/bin/java -ea -cp <old-classes>:<runtime> org.apache.solr.compat.StableConsumer http://localhost:8983/solr binattr_compat`). Exact commands in `runner.sh`. Raw outputs and exit codes in `runs/`.

| Leg | Runtime | Exit | Result |
| --- | --- | --- | --- |
| 1 | Released 10.0.0 (positive control) | 0 | PASS HTTP CoreAdminResponse startTime Date and uptime Long conversion; PASS HTTP add/commit/query/delete against `binattr_compat`; PASS ordinary SolrJ query/update/response/date consumer |
| 2 | Base `f45b38d5f6c0` | 1 | `NoSuchMethodError` at `StableConsumer.java:83` (the builder call), before any HTTP request |
| 3 | Candidate `8bfca8edd30` | 1 | `NoSuchMethodError` at `StableConsumer.java:83`, identical to leg 2 (`diff` of the two outputs is empty) |

Full signature (legs 2 and 3):

```
Exception in thread "main" java.lang.NoSuchMethodError: 'org.apache.solr.client.solrj.impl.HttpSolrClientBuilderBase org.apache.solr.client.solrj.impl.HttpJdkSolrClient$Builder.withConnectionTimeout(long, java.util.concurrent.TimeUnit)'
	at org.apache.solr.compat.StableConsumer.http(StableConsumer.java:83)
	at org.apache.solr.compat.StableConsumer.main(StableConsumer.java:75)
```

Cleanup: core `binattr_compat` unloaded with `deleteIndex=true&deleteInstanceDir=true` (status 0). Solr stopped with `bin/solr stop -p 8983`; port 8983 closed, no `start.jar` process left.

## Descriptor and superclass evidence (javap, JDK 17)

Old consumer references (`javap -v`, 64 constant-pool refs, 40 non-compat org/apache/solr refs): `HttpJdkSolrClient$Builder.withConnectionTimeout:(JLjava/util/concurrent/TimeUnit;)Lorg/apache/solr/client/solrj/impl/HttpSolrClientBuilderBase;` and the same for `withRequestTimeout`.

Static inventory (`inventory.py`, `inventory.json`): every old-consumer reference checked by name and descriptor through the superclass and interface graph of each runtime.

- Released: 40 of 40 resolve.
- Base: 38 of 40. Missing: `HttpJdkSolrClient$Builder.withConnectionTimeout(JLjava/util/concurrent/TimeUnit;)Lorg/apache/solr/client/solrj/impl/HttpSolrClientBuilderBase;` and `withRequestTimeout` with the same descriptor.
- Candidate: 38 of 40. The same two missing.
- No other linkage failure in the consumer's references. The inventory checks name and descriptor only, not access rules or class initialization, so it is evidence of linkage shape, not a proof of absence of other failures.

Declaring methods:

- Released: `HttpSolrClientBuilderBase.withConnectionTimeout` and `withRequestTimeout`, descriptor `(JLjava/util/concurrent/TimeUnit;)LHttpSolrClientBuilderBase;`. `HttpJdkSolrClient$Builder extends HttpSolrClientBuilderBase`. `HttpJdkSolrClient extends HttpSolrClientBase`.
- Base and candidate: `HttpSolrClient$BuilderBase.withConnectionTimeout` and `withRequestTimeout`, descriptor `(JLjava/util/concurrent/TimeUnit;)LHttpSolrClient$BuilderBase;`. `HttpJdkSolrClient$Builder extends HttpSolrClient$BuilderBase`. `HttpJdkSolrClient extends HttpSolrClient`.
- `HttpSolrClientBuilderBase` and `HttpSolrClientBase` are absent from base and candidate. The released superclass `HttpSolrClientBase` is also absent from base and candidate. The consumer doesn't reference it directly, so the static inventory didn't flag it. It is a latent linkage risk for any released-binary repair, not an observed failure.

This matches SOLR-17968 commit `e5340a954b6700627b8826431a243c2e2759b0f3` (builder moved and renamed the HTTP base client), as noted in the gate. The Jackson prototype changes no file under `solrj/impl`.

## Old fixture integrity

- `StableConsumer.class` SHA-256 is `027d21fdb1d79c133aab4e4bb4f109f9d17baa05b822c7457e1c9add44bdf18a` before and after each of the three legs. Bytes unchanged.
- The old class was not recompiled against base or candidate. The `timeout` call and the HTTP fixture were not altered.

## Round 2 evidence still referenced, not rerun

- The four core tests (`CoreAdminCreateDiscoverTest`, `CoreAdminHandlerTest`, `TestCloudManagedSchema`, `TestManagedSchema`) remain Jackson 2 oracles using a fresh `ObjectMapper`. Kept as the owner confirmed. Not rerun here; no candidate source head change.
- Inherited unload failure `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure` is tracked as SOLR-18587 on the board (`assignments/solr-18587-coreadmin-unload-failed-reload-implementation-round-1.md`, `reports/solr-18587-coreadmin-windows-implementation-round-1.md`, `reports/rc3-test-failure-causes-round-2.md`). Not fixed or suppressed here.

## Not established

- Released binary compatibility of the client-first change: NOT established. Leg 3 fails.
- Base-passes-candidate-fails: not the case. The base also fails, so the candidate regression question does not arise from this control.
- Full server migration, 3.1 LTS target, and release readiness: not established.
- Maven consumer execution (`mvn` not present): unrun.
- Mutation, default and error-boundary controls: no new run. Round 2 `baselineBehavior` and `candidateBehavior` lanes passed (`/workspace/gates/logs/vm2/solr18325/harness-21-reports/`, `harness-17-reports/`). Not repeated.
- Public/protected API comparison: not run in this job. Pending.

## Next decision

Per the gate, this control asks for a separately scoped compatibility prerequisite. It does not choose one. Any repair must reconstruct the released 10.0.0 superclass and builder surface that old binaries link against (`HttpSolrClientBuilderBase`, and `HttpSolrClientBase` as the `HttpJdkSolrClient` superclass). It must be justified by descriptor and supertype evidence like the above, not by recreating one class name. The Jackson finding stands on its own.

## Limits

- Board state read at tip `9d8a65fe807` (`origin/coordination-ui`); the claim was pushed at `94e1d1c6c25`.
- No GitHub, PR, Jira, upstream comment, or fork push. No commit made.
- Raw artifacts: `/workspace/gates/logs/vm2/solr18325-binattr/`.

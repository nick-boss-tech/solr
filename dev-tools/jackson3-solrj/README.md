<!--
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->

# SOLR-18325 SolrJ consumer compatibility probe

This isolated Gradle project consumes Maven POMs and JARs, rather than repository
project dependencies. Run it on the assigned Linux gate host. It has not been
compiled or executed on the Windows implementation host.

The candidate is a SolrJ-only migration prototype. Passing these probes would
support the feasibility of upgrading the client within Solr 10; it would not
establish full server migration, compatibility of every extension, or release
readiness. The selected Jackson 3 version comes from the candidate's published
POM. This harness does not establish which Jackson release line Solr should ship.

## Local publication and execution

From the candidate repository root, first publish the exact gated head:

```sh
./gradlew mavenToLocalFolder
```

`gradle/maven/maven-local.gradle` and `dev-docs/gradle-help/publishing.txt`
document that task. It creates `build/maven-local`. The root task publishes all
configured Maven modules, which ensures the candidate `solr-api` and its
transitive Solr artifacts are available as well as `solr-solrj`.

Read the actual generated SolrJ POM version and substitute it for
`<candidate-version>` below. Do not guess the version from the branch name:

```sh
./gradlew -p dev-tools/jackson3-solrj compatibility -PcandidateVersion=<candidate-version>
```

The candidate must have a version different from the released baseline. To use
another publication directory, supply its absolute path with
`-PcandidateRepository=/absolute/path/to/maven-local`.

The standalone build uses the repository wrapper without evaluating the parent
Solr build. All consumer Java is compiled with `--release 17`. Gradle runs on its
supported host JDK, while JavaExec tasks select a Java 17 toolchain by default.
Install or configure that toolchain on the Linux host. Repeat with
`-PconsumerJavaVersion=21` to exercise the same lanes on Java 21. Record both
launcher versions with the results; Java 17 bytecode alone is not a Java 17
runtime proof.

The candidate repository is restricted to its exact Solr version. Maven Central
cannot supply that version. Gradle module metadata is disabled, so the candidate
and released dependency graphs come from their Maven POMs. No dependency
exclusions, shading, source substitution, or forced dependency versions are used.
The application coexistence lane deliberately adds Jackson 2.

## Required evidence

The `compatibility` task runs these independent lanes:

| Task | Compile input | Runtime | Purpose |
| --- | --- | --- | --- |
| `candidateConsumer` | candidate POM | candidate POM | Ordinary query/update/result/date APIs compile and execute |
| `baselineConsumer` | released SolrJ 10.0.0 | released SolrJ 10.0.0 | Same consumer passes before the change |
| `binaryConsumer` | released SolrJ 10.0.0 | candidate POM only | Preserved old consumer class bytes link and execute with candidate |
| `baselineBehavior` | released SolrJ 10.0.0 | released SolrJ 10.0.0 | Establish writer/parser behavior before the change |
| `candidateBehavior` | candidate POM | candidate POM | Shared annotations, dates, UTF-8, typed parsing, and IOException behavior remain usable |
| `jackson2Coexistence` | candidate plus application Jackson 2 | both Jackson major lines | Application-owned Jackson 2 still works alongside candidate |
| `baselineInternalFieldControl` | released SolrJ 10.0.0 | released SolrJ 10.0.0 | Old internal mapper field reference resolves before the change |
| `internalFieldControl` | released SolrJ 10.0.0 | candidate plus application Jackson 2 | Expected NoSuchFieldError proves internal mapper descriptor incompatibility |

`StableConsumer` avoids Jackson types, generated V2 request constructors,
and internal mapper access. Its date check calls the public
`CoreAdminResponse` accessor. It exercises JavaBin update serialization and
query response decoding offline. The optional HTTP lanes below add real server
round trips and call the longstanding, experimental `CoreAdminRequest.getStatus`
API to exercise the changed mapper through public date and uptime accessors on
an actual server response. The behavioral lane uses the internal writer and experimental
typed parser deliberately; its result is separate from the ordinary API binary
compatibility claim.

`verifyCandidateDependencies` fails if the unmodified candidate runtime graph
contains `com.fasterxml.jackson.core:jackson-core` or `jackson-databind`. It
requires Jackson 3 core/databind and the shared Jackson annotations artifact.
The baseline and candidate behavioral tasks also assert that writing closes the
supplied output stream and parsing closes the supplied input stream, including
the reader and malformed-input paths. Each prints the same `STREAM_OWNERSHIP`
signature so the receipt can compare the two runs.

`verifyCandidateDependencies` also writes the resolved candidate runtime, baseline compile, and coexistence
graphs to `build/reports/compatibility`. `binaryConsumer` writes SHA-256 hashes
of the old consumer class files and verifies they did not change during runtime.
Save those files and full task logs with the Linux gate receipt.

The internal field control is an expected incompatibility, not a supported API
pass. Retaining Jackson 2 in that lane ensures the failure tests the changed
field descriptor, rather than simply a missing Jackson 2 application dependency.
If it passes unexpectedly, investigate whether the candidate or harness differs
from the intended migration before reporting compatibility.

## Real HTTP query/update proof

Start an unchanged standalone Solr 10 server and create a disposable core
with an indexed, stored `id` unique key. Then run both source and old-bytecode
consumer lanes against it:

```sh
./gradlew -p dev-tools/jackson3-solrj candidateHttp binaryHttp \
  -PcandidateVersion=<candidate-version> \
  -PsolrUrl=http://127.0.0.1:8983/solr -PsolrCollection=jackson3_compat
```

Each invocation first fetches core status and exercises the migrated mapper's
date and uptime conversion on actual baseline wire data. It then writes one
uniquely named document, commits, queries its id,
and deletes that document in a finally block. Use a disposable test core:
commits also affect other pending writes in the same core. No credentials are
accepted by this probe. Record the exact server version/head and configuration
in the gate receipt. Repeat against a candidate server only if that is a
separately required proof leg; the SolrJ-only prototype intentionally retains
Jackson 2 in the server.

## Limits and failure handling

A failure is evidence to investigate. It must not be concealed with exclusions,
forced versions, recompiling the old fixture against the candidate, or dropping
an unsuccessful lane. A build/setup repair must be recorded and the final
candidate head rerun if source changes. These samples establish concrete
compatibility checks; they cannot prove universal source or binary compatibility.

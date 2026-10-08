# Gate result: solr-11939-submit

- Branch: origin/solr-11939-submit
- Head gated: d4cff5e76430e6deb88115d6675db537b630948e (from `git rev-parse HEAD` in the head worktree; tip unchanged since claim)
- Base (merge-base with apache/solr main e34067ae64): b6b2b8f10e9827e3b86e44649fb7966ce646c185
- JDK: openjdk version "21.0.12.1" 2026-08-18 LTS, Temurin-21.0.12.1+1
- Gradle: 9.7.0 (project wrapper)
- Branch content: docs only (3 files: changelog fragment, ref guide NOTE in collection-management.adoc, SOLR-11939-TESTING.md). No Java, no test files.

## Per-step results

| Step | Result | rc |
| --- | --- | --- |
| 0. Changelog YAML parse (changelog/unreleased/SOLR-11939-property-name-docs.yml) | PASS | 0 |
| 1. Tidy (root `tidy`, no Java files changed) | PASS; worktree clean after tidy, libs.versions.toml unchanged | 0 |
| 2. Error Prone compile | NOT APPLICABLE: no changed Java files | - |
| 3. Premise leg | NOT APPLICABLE: no new or changed test files | - |
| 4. Head leg | NOT APPLICABLE: no new or changed test files | - |
| 5. `:solr:solr-ref-guide:check -x test` | BLOCKED BY ENVIRONMENT (see below) | 1 |

## Step 5 blocker (environment, not the branch)

Two separate failures on this VM:

1. First run: `:solr:documentation:collectLuceneJavadocs` failed with `java.nio.file.FileSystemException: ... lucene-javadocs: Too many open files`. The shell's open-files limit is 1024 soft and 1024 hard on this VM, and it cannot be raised.
2. Retry (after `./gradlew --stop`): `:solr:solrj:openApiGenerate` failed with `OpenAPI code generation failed: There were issues with the specification ... attribute paths is missing, attribute info is missing`. The input spec `solr/api/build/generated/openapi/solr-openapi-11.0.0-SNAPSHOT.json` is only 25 bytes (`{"openapi": "3.0.1"}`). Task `:solr:api:resolve` completes in about 5 s with no warnings but writes that stub. Re-running with `--no-build-cache` and a deleted output directory gives the same stub.

Control: the same `:solr:api:resolve` run on the merge-base b6b2b8f10e (worktree with no branch changes) writes the identical 25-byte stub. The failure is therefore not caused by this branch. Possible cause, unverified: the swagger resolver's classpath scan on this VM, perhaps related to the 1024 file-descriptor limit.

Failure output (first lines of the second failure):

```
> Task :solr:api:compileJava UP-TO-DATE
> Task :solr:api:resolve
> Task :solr:solrj:openApiGenerate FAILED
* What went wrong:
Execution failed for task ':solr:solrj:openApiGenerate' (registered by plugin 'org.openapi.generator').
> A failure occurred while executing org.openapitools.generator.gradle.plugin.tasks.OpenApiWorkAction
   > OpenAPI code generation failed: There were issues with the specification. The option can be disabled via validateSpec (Maven/Gradle) or --skip-validate-spec (CLI).
      | Error count: 2, Warning count: 0
     Errors:
     	-attribute paths is missing
     	-attribute info is missing
```

## Final verdict

GATE INCOMPLETE (environment). Steps 0 and 1 pass, steps 2 to 4 do not apply to a docs-only branch, and step 5 cannot run on this VM because `:solr:api:resolve` writes an empty OpenAPI spec even on the upstream base. This is NOT GREEN. Gate lane needs the open-files limit or the swagger resolve issue fixed before step 5 can give a verdict for any module that depends on solrj. VM1 should confirm whether its lane shows the same stub.

Note: the user-home `org.gradle.caching=true` was set on this lane as the spec requires. The stub was restored from the build cache once during the investigation; the uncached rerun produced the same stub, so the cache did not cause it.

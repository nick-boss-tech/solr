# Gate: SOLR-18178 rebased configset archive upload, round 1

Status: OPEN

- Tags: gate. Intended host: any capable Linux gate host with JDK 21 and this branch's toolchain and dependencies; one build at a time under the host's build lock.
- Source: Nick's October 28 direct request to rebase PR 4968 and update its body, following the Windows review. This is proof for a new head, not a rerun of the old green receipt.
- Fork branch: `nick-boss-tech/solr:solr-18178-verify`.
- Exact implementation head: `d6abc36c94687dc5744a7573bafb1ce473acc75f`.
- Rebase/main proof base: `35e2be2a7de9855a0f909613065ba22243097102`.
- Old PR head before the authorized lease force-push: `b75e7d4d3c464ceeb9f14fdaf7711212adcccac2`.
- State: Implemented, Proof Pending. Windows ran static diff/whitespace checks only; no Gradle, formatting task, compilation, tests, or local test queue.
- Receipt target: `receipts/SOLR-18178-rebased-20261028.md`. Do not reuse the historical receipt as proof for this head.
- The public PR body was authorized for an editorial update and explicitly says fresh Linux verification is pending. This job authorizes no further public PR or JIRA writes.

## Scope and preflight

Read live WORKFLOW.md and START-HERE.md, check current claims and this job, and verify the fork tip before claiming. Fetch the exact head into a clean detached Linux tree. Record the host, build lock, JDK, full revisions, checkout, runner, log paths, fresh XML paths and timestamps in the gate claim and receipt.

The rebased delta is eight files, 428 additions and 18 deletions:

- `changelog/unreleased/SOLR-18178.yml`
- `solr/core/src/java/org/apache/solr/core/FileSystemConfigSetService.java`
- `solr/core/src/java/org/apache/solr/handler/configsets/UploadConfigSet.java`
- `solr/core/src/test/org/apache/solr/core/TestFileSystemConfigSetService.java`
- `solr/core/src/test/org/apache/solr/handler/configsets/DownloadConfigSetAPITest.java`
- `solr/core/src/test/org/apache/solr/handler/configsets/UploadConfigSetAPITest.java`
- `solr/core/src/test/org/apache/solr/handler/designer/TestSchemaDesignerConfigSetHelper.java`
- `solr/solr-ref-guide/modules/configuration-guide/pages/configsets-api.adoc`

`DownloadConfigSet.java` must be identical to the proof base: it delegates to `Utils.zipDirectory` from SOLR-18320. The redundant `toZipEntryName` helper and its direct unit test were removed while resolving the only rebase conflict. The raw ZIP-entry integration assertions remain. Upload production, filesystem-listing production, upload tests, listing tests and designer tests are unchanged from the old PR head; the guide retains main's other edits. The changelog now describes the remaining production changes.

## Head leg

Run `./gradlew tidy` at the exact head and require a clean tree. A formatting repair must be a scoped Nick Shanin commit, pushed normally to this fork branch and named in a new superseding gate job. Do not edit this job or transfer a result to a different head.

Compile with Error Prone explicitly enabled (it is otherwise off outside CI):

```sh
./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true --rerun-tasks
```

Run all four focused classes with fresh XML:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.configsets.UploadConfigSetAPITest --tests org.apache.solr.handler.configsets.DownloadConfigSetAPITest --tests org.apache.solr.core.TestFileSystemConfigSetService --tests org.apache.solr.handler.designer.TestSchemaDesignerConfigSetHelper -Ptests.seed=4968D15EA5E2
```

Expected discovery from the source is 20 upload methods, 4 download methods (the historical count of 5 included the removed helper test), 3 filesystem-service methods and 8 designer-helper methods. Record actual fresh XML counts, failures, errors and skips; an unexpected count or skipped target must be explained before GREEN. An up-to-date/cache-only run or compile failure is not test execution.

Repeat the upload class with fresh XML at each of `4968D15EA5E3` and `7777777777771007`, using the same cleanTest/test tasks and the UploadConfigSetAPITest filter alone. Require all upload methods to execute with no failures, errors or skips at each seed.

Then run `./gradlew check -x test` as required by the repository AGENTS.md. Record its real result; do not silently exclude tasks or present a failed check as green. Finish with `git diff --check`, exact-head verification and a clean tree.

## Fail-before leg at the current main proof base

Use a separate clean scratch checkout at `35e2be2a7de9855a0f909613065ba22243097102`. Overlay only the candidate `UploadConfigSetAPITest.java` and leave production unchanged. The full candidate test file references new static helpers absent on base, so make only these compile-compatibility edits in the scratch test:

1. Remove the helper-only `testSafeZipEntryPathRules` method.
2. Remove the direct `UploadConfigSet.normalizeZipEntryName` assertion inside `testZipUploadNormalizesBackslashEntryNames`.

Save that exact scratch diff in the evidence. Do not add production helper stubs or weaken any of the three selected tests below. This is a disclosed test adaptation, not an unmodified full-class overlay.

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.configsets.UploadConfigSetAPITest.testZipUploadRejectsUnsafeEntryPathsBeforeBackendDispatch --tests org.apache.solr.handler.configsets.UploadConfigSetAPITest.testSingleFileUploadRejectsTraversalPath --tests org.apache.solr.handler.configsets.UploadConfigSetAPITest.testSingleFileUploadNormalizesBackslashPath -Ptests.seed=4968D15EA5E2
```

Require the three selected tests to execute and fail for the expected missing path-validation/normalization assertions on base. Capture fresh XML, assertion messages and class output. A compile/harness failure is not a fail-before. If any test passes, record that result and investigate the premise rather than claiming it failed. On the head, those same tests must pass within the focused class runs above.

The export separator assertions may pass on this main base, including on Linux: main now uses the portable shared ZIP writer. Do not demand or claim a new fail-before for export at this base. The old Windows export comparisons remain historical evidence only. No new Windows build is authorized.

## Completion

GREEN requires the exact final head, clean tidy, explicit Error Prone compilation, all named head runs passing, the three selected base failures attributable to the missing behavior, successful root checks, and a clean final tree. Preserve complete commands, toolchain, seeds, return codes, logs and fresh XML with timestamps and hashes. Compare failures against unchanged base where needed; do not blindly rerun an executed failing gate. Follow WORKFLOW.md's repair rules.

Write the new receipt with proof legs and limits, including the scratch-test adaptation. If any source or test repair moves the head, push the scoped repair, post a superseding job and obtain proof for that new head. Do not edit this add-only job. No PR comment, PR body edit, ready/draft change, approval, merge or JIRA write is part of this gate.

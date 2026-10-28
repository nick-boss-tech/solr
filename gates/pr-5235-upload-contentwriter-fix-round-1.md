# Gate: PR 5235 ConfigSet upload ContentWriter fix, round 1

Status: CLAIMED (vm4, 2026-10-28T20:57Z)
Tags: gate, review, records
Intended host: any capable Linux gate host with JDK 21, Git, Python 3, the branch Gradle wrapper, and normal Solr build dependencies. Run one gate at a time under the host build lock.

## Inputs and scope

- Ticket: SOLR-18351; Apache Solr PR 5235.
- Fork branch: `nick-boss-tech/solr:solr-18351-submit`.
- Exact patch head: **`e2ad60ecedda7451cb331538a5fd40b66144126b`**.
- Parent: `caf8dde9063fd2470c02d9938b5723e691c0e11e`.
- Current Apache PR head when this job was prepared: `ccace89730ae47cf94127643a39da6d57fe22e22`.
- Patch: `ConfigSetAdminRequest.Upload.setUploadFile` now stores a `RequestWriter.ContentWriter` directly; deprecated `setUploadStream` remains the legacy-stream path.
- This job validates the exact fork patch head above. It does not claim that Apache PR 5235's current head has this patch.
- Existing round-3 compatibility receipt remains valid for its recorded head and tests. This is an add-on gate for the uncovered `TestConfigSetAdminRequest.testUpload` regression.
- New deliverables: `receipts/PR-5235-upload-contentwriter-fix-round-1.md` and `reports/pr-5235-upload-contentwriter-fix-round-1-outcome.md`. Gate host owns the receipt.

## Preflight

1. Read the live board and confirm no active claim exists for this gate.
2. Confirm the remote fork branch still resolves to the exact patch head above. Use a fresh detached Linux worktree at that SHA; do not use the Windows tree or an old dirty proof tree.
3. Run `./gradlew --no-daemon tidy`. If it changes any files, commit and push only the formatting repair, then add a superseding gate pinned to the new head before running tests.
4. Use fresh `cleanTest` output for every test leg. Preserve XML and `OUTPUT` separately. Run sequentially under the host lock; archive each command, exit code, UTC start/end, toolchain, and log.

## Required test legs

Run both CI reproduction seeds for the failing method, then the full test class. Use separate shell arguments and `--no-build-cache`:

```sh
./gradlew --no-daemon :solr:solrj:cleanTest :solr:solrj:test --tests "org.apache.solr.client.solrj.request.TestConfigSetAdminRequest.testUpload" "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m" -Ptests.seed=747FA8C56DB2B91B -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1 --no-build-cache
./gradlew --no-daemon :solr:solrj:cleanTest :solr:solrj:test --tests "org.apache.solr.client.solrj.request.TestConfigSetAdminRequest.testUpload" -Ptests.seed=4478EC01207498A8 "-Ptests.timeoutSuite=600000!" -Ptests.useSecurityManager=true -Ptests.file.encoding=UTF-8 --no-build-cache
./gradlew --no-daemon :solr:solrj:cleanTest :solr:solrj:test --tests "org.apache.solr.client.solrj.request.TestConfigSetAdminRequest" -Ptests.seed=5235C0FFEE18351 --no-build-cache
```

Acceptance: the targeted test passes with both original CI seeds, and the complete class passes with fresh XML. Report actual counts, skips, assumptions, and any retry behavior; do not infer success from the task summary alone. Run `./gradlew --no-daemon tidy` again and confirm the tree remains clean.

## Failure handling and receipt

If a test fails, classify the assertion from the fresh XML and raw test output. Do not rerun as a new gate; any code repair requires a new commit and a superseding exact-head job. Do not change the contributor's branch or post to GitHub/JIRA.

On completion, record the exact fork SHA, toolchain, all commands and exit codes, test counts and skips, tidy status, and durable log/XML locations in the receipt and outcome report. Keep the patch `Implemented, Proof Pending` until the receipt records green proof at this exact head.

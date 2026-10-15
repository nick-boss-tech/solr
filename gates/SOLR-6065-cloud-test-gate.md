# Gate job: SOLR-6065 gate at the live head, with the cloud test in the run

- Branch: solr-6065-submit. Head pinned at filing: `3d2cec9e1ab3a19f6b948682b116c2780da4b65b` (fork tip, verified by `git ls-remote` on 2026-10-15 at filing; matches the head in `audits/update-processing/SOLR-6065.md`). Merge-base with upstream main: `97d973814336101e12475558d7419321c743de79`.
Status: DONE (GATE GREEN at 3d2cec9e1ab3; proof settled by the main-side verdict by content, 2026-10-15)
- Capability tags: gate.
- Job type: full hardening gate at the pinned head, with the cloud test named in the run list. No implementation step. Pushes only if the gate itself produces a commit (see On green).

## Why this job exists

`TESTING.md` decided on 2026-10-08 to include the cloud test the ticket asks for, and records: "The branch is not final until the cloud test lands and gates." The cloud test landed at `3d2cec9e1ab` (`MaxDocsLimitCloudTest.testMaxDocsExceededGivesClearErrorToClient`, a 2-node cluster, a 1-shard 1-replica collection, the limit set to 3, asserting a 500 with "maximum number of documents" and no "analysis error"). The 2026-10-15 audit (`audits/update-processing/SOLR-6065.md`) records that the test has never been run anywhere readable: the newest CI run covers its parent head `6aef011ee8d`, and no gate receipt names `3d2cec9e1ab`. The audit also makes the test's reflective access a gate question (finding 4): the test uses `MethodHandles.privateLookupIn` on `IndexWriter.setMaxDocs` and changes a JVM-global limit, restoring it in a `finally`. Whether the test security policy allows that access is settled by this run, not by reading.

## Run shape

- Intended host: any capable Linux host, under the WORKFLOW.md claim rules (claim first under `claims/`, first push wins, hourly heartbeat). Windows never claims build work.
- Gradle route: the claiming host's route per its file in `hosts/` (on vm1, `~/workspace/tools/solr-gradle.sh` and never `./gradlew`; on vm2, per `hosts/vm2.md`). Copy `gradle.properties` into the worktree before any Gradle run.
- All Gradle steps run serially behind the host's test-queue flock. Never two Gradle builds at once on a host. The cloud test boots a small cluster; it still runs inside the same flock as every other step.
- Test counts come from fresh JUnit XML only. Seed: `6065C0FFEE6065`. Module: `:solr:core`.

## Steps, in order

1. Claim under `claims/` (WORKFLOW.md), then confirm the base: `git ls-remote fork refs/heads/solr-6065-submit` must equal `3d2cec9e1ab3a19f6b948682b116c2780da4b65b`. If it has moved, stop and record the new tip in this file; do not gate an unrecorded head.
2. Changelog YAML parse with a real YAML parser of the fragment the branch adds, `changelog/unreleased/SOLR-6065-max-docs-message.yml`. It must parse.
3. Tidy, with the GATE INVALID guard: run tidy for the branch's module scope; restore `gradle/libs.versions.toml` afterwards; then check `git status`. If tidy modified anything beyond that restore, the run is GATE INVALID: stop and record what tidy changed in this file. If a tidy commit is needed, commit it (Nick Shanin identity, no trailer), re-run the gate on the committed tree, and treat the new head as the gated head everywhere below.
4. Error Prone compile: `:solr:core:compileJava` and `:solr:core:compileTestJava` with `-Pvalidation.errorprone=true`. rc must be 0.
5. Fail-before proof, judged by content: run the branch's cloud test against merge-base production (`97d973814336101e12475558d7419321c743de79`, branch test files in place). The pass condition is that `testMaxDocsExceededGivesClearErrorToClient` fails there with the pre-change symptom: the client sees the generic analysis-error shape instead of the clear maximum-documents message. Record the outcome either way; a failure from cluster setup or compilation does not count as proof.
6. Focused tests at the head, counted from fresh JUnit XML: `org.apache.solr.cloud.MaxDocsLimitCloudTest` (required; a gate without this class in the run does not discharge this job) and `org.apache.solr.update.DirectUpdateHandlerTest`. Confirm each class's exact package by grep on the branch before composing the run command, and compose the run in a separate step from the grep. Record tests, skipped, failures, and errors per class. If the cloud test fails on the reflective `setMaxDocs` access under the test security policy, record the exact failure text; that is a test result, not an environment refusal.
7. Module check: `:solr:core:check -x test`, rc 0.

## On green

1. Refresh `receipts/SOLR-6065.md` with a section for this gate: the gated head, the changelog parse result, the tidy guard result, the compile and module check results, the step 5 proof outcome by content, and the step 6 counts with the seed and run date, all from the fresh JUnit XML and the gate log. The receipt section records plainly that the cloud test has now run and passed at the live head, which is the condition `TESTING.md` sets for the branch to be final.
2. The branch backs Nick's open PR #5078. If the gate produced no commit, the fork tip does not move and no push or body edit is owed. If a tidy commit moved the head, push it plainly (fast-forward, no force) to `fork` `solr-6065-submit` under the standing authorization for pushes to Nick's own open PRs (2026-10-14), verify the new tip by `git ls-remote`, and re-point PR #5078's body citations and Proof to the new head (read the body through the API before and after the edit).
3. Mark this job DONE with the gated head and the outcome, and close the claim.

## On failure

- Record the failing step, the failing test and seed if a test ran, and the log path in this file and in the receipt. A gate that executed tests and failed is never relaunched by the claimant; the main side investigates first (WORKFLOW.md). A run that never executed tests because of an environment refusal may be relaunched once, with the refusal recorded.

## Gate result (coordinator record, 2026-10-15)

Run: /workspace/gates/g6065-gate.sh, ended 2026-10-15T21:00:43Z, head 3d2cec9e1ab3a19f6b948682b116c2780da4b65b, log logs/vm2/queue-g6065-gate-20261015T203556Z.out.

- changelog rc=0, tidy rc=0 with an empty tidy tree, compile rc=0, focused head rc=0, core check rc=0.
- Proof leg rc=1 (nonzero expected). Against base 97d973814336 with the branch test files in place, `MaxDocsLimitCloudTest.testMaxDocsExceededGivesClearErrorToClient` failed with `expected:<500> but was:<400>`. The server text is "possible analysis error: number of documents in the index cannot exceed 3" (logs/vm2/6065-proof.log, lines 112-113 and 370). Whether that is the intended pre-change symptom is a by-content verdict for the main agent.
- The focused head run (`MaxDocsLimitCloudTest` and `DirectUpdateHandlerTest`) is not counted as proof, and the step 6 counts were not recorded because the proof leg did not pass.
- Not relaunched. No push was made. No receipt was written.

## Main-side proof verdict (2026-10-15)

The proof stands by content; the gate is GREEN at 3d2cec9e1ab3.

- The 500 in the failure message is the test's own first assertion (`assertEquals(500, e.code())` in `MaxDocsLimitCloudTest`), which states the fixed behavior under the locked SERVER_ERROR decision. It was never a prediction about the base; a fail-before run whose first failing assertion is the status code prints exactly this pair.
- At the merge-base (97d973814336), `DirectUpdateHandler2.addDoc` catches the IndexWriter limit exception in its generic `catch (IllegalArgumentException)` and rethrows it as a SolrException with BAD_REQUEST and the message "Exception writing document id ... to the index; possible analysis error: " plus the exception text. The proof log's server text, "possible analysis error: number of documents in the index cannot exceed 3", is that mapping's output: the same request, rejected at the same site, for the premise reason (the IndexWriter doc limit, whose message wording the branch's MAX_DOCS_MESSAGE_PREFIX keys on).
- That is the pre-change symptom step 5 names: the generic analysis-error shape instead of the clear maximum-documents message. The branch's change sits inside that same catch and maps this one exception to SERVER_ERROR with the clear wording, so the test discriminates: at base the response carries "analysis error" and not "maximum number of documents"; at the head the reverse. The head leg of this gate ran both named classes at rc=0, which also settles the audit's reflective-access question for this run: the cloud test executed and passed.
- No re-run was needed: the recorded server text plus the code at both trees settle the base behavior. The job's step 5 expectation stands as written (failure with the analysis-error shape); the numeric pair in the log is the test asserting the shape change, not a miss against the job.

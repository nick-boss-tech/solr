# SOLR-18609 / PR 5299 review fixes: Linux gate round 1

Status: OPEN
Tags: gate, bats, records, review.
Intended host: any available capable Linux gate host with JDK 21 and JDK 25, Bash/BATS, Python 3, curl, OpenSSL, keytool, Docker daemon, and access to the previous-major release image. Windows is implementation only.
Requested by Nick on 2026-11-05: patch the reviewed branch and hand off to the gate.

## Exact inputs and ownership

- Fork: `nick-boss-tech/solr`.
- Branch: `solr-bats-assertion-hardening-submit`.
- Exact candidate: `f342e160417d64ed2dced989052b29a76d31996e`.
- Reviewed parent: `99d4b8e68b446b1e750159a2ccb6549495154d60`.
- Live Apache PR base at implementation: `1fc38d1e40cb12512aba2b1990b22b04111dad2d`; preserved merge base `1fdb455a524a7b8d334d5baea04e395ed4d2d7f8`.
- Implementation claim: `claims/pr-5299-review-fixes-round-1-windows.md`.
- Implementation report: `reports/pr-5299-review-fixes-round-1-windows.md`.
- Gate claim: add `claims/pr-5299-review-fixes-round-1-<host>.md` after detached preflight and ownership checks.
- Result report: add `reports/pr-5299-review-fixes-round-1-<host>.md`.
- GREEN receipt: `receipts/pr-5299-review-fixes-round-1.md`, written only by the executing gate host.
- Seed: not applicable to BATS; do not invent a randomized-tests seed.

This supersedes the candidate input of `gates/bats-assertion-hardening-round-1.md` and its head-2/head-3/head-4 records for this PR follow-up. It does not turn earlier results into proof for this head or erase the old failures. Read that gate, its October 27 finish report and supplement, and this file together. This file governs the new source changes, head and completion bar.

The old phone-agent-2 claim was marked stale in `claims/phone-agent-2-stale-claims-20261104.md`. Check old-host processes, build lock and log growth before taking over; a stale administrative record does not prove no build is active. No second host runs the same gate concurrently. Read the live board instructions and your host record, run detached tidy preflight at this SHA under the host lock and headroom guards, then add a new claim naming the old claim and this superseding job.

## Change and producer correction

The four-file follow-up adds `client_hostname_rejection_logged`, uses it for the fresh server-side SSL hostname diagnostic, adds five helper regressions (14 helper cases total), and adds a dedicated rolling-upgrade CI job that builds/pins the candidate image and rejects skipped or missing test execution. No production code changed.

The previous diagnosis that the unsupported-algorithm message necessarily proves an unrelated failure was too broad. OpenJDK 25 GA first invokes the hostname comparison, then wraps its certificate exception with that message. OpenJDK 21 GA propagates the direct mismatch instead. Verify the trace against the exact executing runtime sources; do not treat the wording alone as a new production bug. The adjacent matching-client `mTLS wanted` case is relevant positive coverage but not a substitute for the paired control below.

Producer references:
- https://github.com/openjdk/jdk/blob/jdk-21-ga/src/java.base/share/classes/sun/security/ssl/X509TrustManagerImpl.java#L392-L417
- https://github.com/openjdk/jdk/blob/jdk-25-ga/src/java.base/share/classes/sun/security/ssl/X509TrustManagerImpl.java#L391-L425

The predicate must remain specific to direct SAN hostname/IP mismatch or the known JDK wrapper. Bare ClosedChannelException, connection refusal, missing-client-certificate, PKIX/trust failure, protocol failure, old log text, and success are not hostname rejection proof.

## Environment and evidence

Use JDK 21 for Gradle. Pin task-level `JAVA_HOME` and `SOLR_JAVA_HOME` to each server/CLI runtime, `SOLR_INCLUDE=/dev/null`, and `SOLR_PICOCLI=false` or `true` explicitly. Preserve the init script and actual child environment/JVM fingerprints. Use the original gate's Picocli `testFiles` replacement rather than appending to its curated list.

Prior failures included localhost/IPv6 loopback refusals. Diagnose binding/address-family preconditions before the matrix. A task-scoped `-Djava.net.preferIPv4Stack=true` is permitted when needed, with the exact condition recorded and applied consistently to candidate/control legs. Do not count a failed startup as a certificate rejection; do not silently rerun executed failures or change production behavior. A failed executed leg must be investigated before a repair retry.

For every leg preserve: clean exact SHA, command and init/environment inputs, UTC start/end, host/JDK/tool versions, full transcript and stdout/stderr, fresh JUnit XML, Solr logs and failure snapshots, exit status and pass/fail/skip counts, selected files, lock/headroom observations. Archive output before another task deletes it. Preserve raw XML; if invalid control bytes require sanitized derivatives, retain both and label the transformation. No stale/up-to-date reports or zero-test passes.

## Required proof A: focused helper and TLS discrimination

1. Run `test_bats_assertions.bats` normally on JDK 21 and JDK 25: all 14 declared cases pass, no skips. Do this before expensive server legs.
2. In isolated scratch copies, keep the new helper's executable signature and the new tests unchanged, but replace its predicate with the parent's wrapped-message-only behavior. The new direct IP and DNS cases must fail at their intended helper assertions, not because the helper is missing. Record this as a synthetic old-predicate comparison, not a production fail-before run.
3. Separately mutate only offset handling to read the whole log: the earlier-diagnostics case must fail. Separately accept arbitrary SSLHandshakeException text: the unrelated-failures case must fail. Removing wrapped-message recognition must fail its positive fixture. Preserve each mutation diff and selected fresh BATS report. Retain the original helper guards and their six mutations from the original gate; rerun them on this exact helper source.
4. Run the real `start solr with mTLS needed`, `start solr with mTLS wanted`, peer-name and keystore-reload cases normally on both runtimes as part of proof B. Capture the actual client and fresh producer diagnostic, preserving offsets. Do not infer real JDK execution from the fixtures.
5. In a scratch variant, keep the same issuer, EKU, node/port and `NEED_CLIENT_AUTH=true` / `CLIENT_HOSTNAME_VERIFICATION=true` settings while changing only the client SAN from mismatching to the actual loopback peer identity. Show the matching client connects and the mismatching client is rejected for the intended cause. Preserve the fixture diff and logs; no production change. If this cannot be established, the hostname-policy item remains explicitly unresolved and the full gate is incomplete.
6. If a supported older JDK 21 runtime is available, also execute the focused real case there and record its direct diagnostic. If unavailable, state that the older-diagnostic portability proof is source trace plus synthetic fixtures; do not claim an older-runtime run. Normal real JDK 21 and 25 execution remains mandatory.

## Required proof B: current-head normal matrix

Use the original gate's temporary runtime/Picocli init-script mechanism. All paths below are under `solr/packaging/test/`.

Legacy list, 12 non-Docker files, 67 declared cases after this follow-up:

```text
test_bats_assertions.bats
test_assert.bats
test_basic_auth.bats
test_compression.bats
test_delete_collection.bats
test_export.bats
test_healthcheck.bats
test_post.bats
test_postlogs.bats
test_ssl.bats
test_start_solr.bats
test_stream.bats
```

- JDK 21 legacy, then JDK 25 legacy: `:solr:packaging:integrationTests --rerun-tasks` with `--tests <file>` repeated for these 12 files.
- JDK 21 Picocli, then JDK 25 Picocli: `:solr:packaging:integrationTestsPicocli --rerun-tasks`, replacing `testFiles` with helper, delete, healthcheck and postlogs (23 declared cases).
- Verify selected files and actual counts from fresh reports. The unchanged GNU-timeout precheck may produce the already documented one legacy skip; show its baseline behavior and do not relabel it a pass. No additional unexplained skip/failure is acceptable. Docker is a separate required leg below, not an accepted skip inside these legs.

The older candidate's 57 pass / 4 fail / 1 skip legacy results and 18/18 Picocli results are historical. None satisfies this head's matrix. Do not report the PR body's older 55/62 or 17/18 as final proof.

## Required proof C: candidate-image Docker rolling upgrade and CI guard

Run the new `rolling-upgrade` job's steps locally in the same order on the Linux gate host, or preserve a successful exact-head execution of that job and its fresh artifacts. Upstream CI alone does not substitute for the remaining local gate legs.

1. Derive the previous major from root `build.gradle` exactly as the workflow does. This source has baseVersion 11.0.0, so the default previous-major tag is `solr:10-slim`; do not hardcode the review's earlier assumption of Solr 9.
2. Build/tag this exact candidate using `:solr:docker:docker`, with `SOLR_DOCKER_IMAGE_REPO=github-pr/solr` and a unique task tag. Set `SOLR_END_IMAGE` to that tag before inspection. No registry push is requested.
3. Pull the previous-major image; preserve both `docker image inspect` objects, release RepoDigest, candidate image ID, build transcript and source SHA. Pin `SOLR_BEGIN_IMAGE` to the release digest and `SOLR_END_IMAGE` to the candidate image ID for execution. Record actual runtime JVMs inside both images; host JDK choice does not establish container runtime.
4. Run `:solr:packaging:integrationTests --tests test_rolling_upgrade.bats --rerun-tasks` with those explicit task-level environment values. Require exactly one executed passing case, no skip. Preserve Docker artifacts, health reports, full transcript and fresh XML. A missing daemon/image or a failing setup is an incomplete leg, not success.
5. Execute the new workflow's Python report guard on the real fresh report. In isolated temporary report directories, demonstrate its result for: one passing case (exit 0); no XML; zero cases; one skipped case; one failed case; one errored case; two cases (all nonzero). Use the unchanged guard body with only its report-directory input redirected in the scratch harness. Preserve fixtures and exit codes. This proves that CI cannot quietly accept the rolling-upgrade skip.
6. Validate the workflow YAML and embedded shell/Python syntax with the host's available workflow/static tools; record which tools actually ran. Confirm the workflow builds and tests in one job and that its candidate image is the locally built head. A tidy/YAML repair creates a new candidate head and requires a superseding gate input.

## Required proof D: inherited assertion controls and final checks

Read the eight discriminating groups in `gates/bats-assertion-hardening-round-1.md`. At this exact head, reproduce the scoped controls for posting count, swallowed postlogs adds with successful commit, failed selects and a successful select body without `numFound`, degraded health, failed ZK lookup versus absence, assert error 100 versus expected 1, stale export, unrelated stream/auth error, quiet-post warning, failed no-compression curl and stop no-op. Preserve base/candidate comparisons with their exact inputs and normalization caveats. The old authenticated PostLogs base accepted the injected add failure; the ordinary old base failed later, so do not invent an ordinary-base false pass.

Complete the outstanding TLS refusal targets after positive TLS preconditions: peer-name, missing client certificate, client-hostname, truststore, curl exit 7 versus expected 60, and reload. The new hostname predicate does not authorize weakening the existing client failure/refusal checks. Setup failures before an injection do not count as controls.

Run final `tidy`, `git diff --check`, `:solr:packaging:check -x test` and root `check -x test` at the final candidate through the host runner. Record Error Prone only if it actually runs; report `errorProneSkipped` explicitly otherwise. No repository-wide application test run is requested. Any source/formatting repair must be committed and plain-pushed with Nick's identity, separately verified on the fork/PR, recorded in an add-only superseding head job, and proved on the new SHA. No force push.

## Completion

GREEN requires all mandatory legs above at the same final SHA, no unresolved hostname/loopback/formatting issue, and no missing Docker execution. An unavailable Docker host remains a capability blocker requiring a named capable-host subleg; this new CI wiring is not itself proof. Record any non-reproduced premise explicitly rather than silently dropping a required control.

The result report and receipt must identify exact normal/mutation/base counts, runtime and image identities, all execution conditions and limits, raw artifact paths and final branch/PR head. The receipt ends with an accurate PR description draft reflecting the 14 helper cases and new Docker job. PR body edits, public comments, JIRA writes, ready-for-review conversion and merge are outside this job. The current PR body still describes its parent evidence; prepare a corrected draft, do not silently present it as current-head proof.

Until the executing host posts a qualifying receipt: **Implemented, Proof Pending**. This job's publication is a handoff, not an execution claim.

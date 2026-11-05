# SOLR-12613 round-2 exact-head lifecycle gate evidence

This set contains the fresh, exact-head round-2 execution record. Original command logs, JUnit XML, resource traces, source checks, failure debug artifacts and the isolated overlay diff/restoration proof are available below. The separate Windows AddCore evidence is not duplicated; it remains linked from the existing [browser-verification receipt](../SOLR-12613-browser-verification-round-1.md).

| Proof leg | Evidence | Result |
|---|---|---|
| Detached F2 pre-claim tidy preflight | `preflight/` | Exact F2 `b0e096ae19ad637107227e1bc04e951b9023a680`; tidy clean, 62 actionable tasks, rc 0. |
| F2 normal-clone root tidy | `tidy/` | `./gradlew tidy`, rc 0, 62 actionable tasks; clean tracked tree. |
| Error Prone compile | `compile/` | `:solr:webapp:compileTestJava -Pvalidation.errorprone=true`, rc 0; pinned wasm SHA verified. |
| Native API client generation | `generation/` | `:solr:webapp:js-client:generateJsClientBundle`, rc 0 from F2's native API sources. The wasm archive is not claimed as OpenAPI provenance. |
| Original P picker | [`steps/p-picker/`](steps/p-picker/) | Seed `5110C0AE`: 4 tests, 0 failures/errors/skips. |
| Candidate F2 picker | [`steps/f2-picker/`](steps/f2-picker/) and [`picker-method-order.txt`](support/picker-method-order.txt) | Seed `5110C0AE`: all 8 methods pass in the listed order; no shared-setup timeout. |
| Paired 17-method neighbors | [`steps/neighbors/`](steps/neighbors/) | P `5110C0AE`: 17/17; P `12613F00D`: 17/17; F2 `12613F00D`: 17/17. F2 `5110C0AE`: 16/17, one `testAddCoreViaUi` timeout at `#add_name`; 0 errors, 0 skips. Fresh five-class XML, logs and resource traces for all four runs are included. The failing run also includes screenshot, page HTML, console log and raw test output. |
| Combined fail-before | [`steps/fail-before/`](steps/fail-before/) | With only `app.js` restored from P and the F2 picker test unchanged, all 4 selected methods executed and failed at their four intended old-behavior assertions; 0 errors/skips and no setup timeouts. Overlay diff, preflight, assertion XML/log and F2 restoration record are included. |
| F2 root check | [`steps/root-check/`](steps/root-check/) | Normal F2 clone `./gradlew check -x test`, rc 0; 555 tasks (524 executed, 31 up-to-date), clean tracked tree. Antora local site/link check completed with 0 errors and 0 warnings; no backend tests are claimed. |
| Pins, provenance and controls | [`support/final-source-verification.md`](support/final-source-verification.md), [`support/wasm-artifact-provenance.txt`](support/wasm-artifact-provenance.txt), [`support/thread-leak-scan.md`](support/thread-leak-scan.md), [`support/evidence-scan.md`](support/evidence-scan.md) | PR/F2 pins and clean worktrees verified; lowest sampled free-inode level was 34.2235%, above the 15% stop floor; no explicit thread-leak message or credential pattern found. |

The root check used Ubuntu 24.04.4, OpenJDK 21.0.12.1, Gradle 9.7.0, Node 24.22.1 and npm 11.19.1, with `NODE_ENV` unset. `npmSetup` was skipped; Gradle/npm downloaded Antora components and built the local guide. npm emitted deprecation/audit notices (7 vulnerabilities reported: 6 moderate, 1 high); these did not fail the build. No CA/truststore override variables were set. The dedicated Error Prone compile was run separately with validation explicitly enabled; the root check's default Error Prone skip is not used as compile proof.

Integrity hashes for all files in this evidence set, excluding the checksum file itself, are in [`SHA256SUMS`](SHA256SUMS).

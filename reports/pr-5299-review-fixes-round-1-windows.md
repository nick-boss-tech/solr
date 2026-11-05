# SOLR-18609 / PR 5299 review fixes: Windows implementation report

Status: IMPLEMENTED, PROOF PENDING
Claim: `claims/pr-5299-review-fixes-round-1-windows.md`.

## Pushed result

- Fork branch: `nick-boss-tech/solr:solr-bats-assertion-hardening-submit`.
- New commit: `f342e160417d64ed2dced989052b29a76d31996e`.
- Parent: `99d4b8e68b446b1e750159a2ccb6549495154d60`.
- Commit: `SOLR-18609: accept JDK hostname diagnostics and require rolling-upgrade proof`.
- Author/committer: Nick Shanin, `nick.boss.us@gmail.com`; no trailers.
- Plain fast-forward push completed. Separate fork-ref read-back and separate live PR read-back both returned this full SHA. The open draft PR 5299 head moved to this commit. No public comment or body edit was made.
- Diff: https://github.com/nick-boss-tech/solr/compare/99d4b8e68b446b1e750159a2ccb6549495154d60...f342e160417d64ed2dced989052b29a76d31996e

## Changes

Exactly four outbound files, 108 insertions and 8 deletions:

1. `solr/packaging/test/bats_helper.bash`: extracted the fresh-log hostname rejection predicate, accepting the specific direct DNS/IP SAN mismatch and the known JDK wrapper diagnostic. It still requires an SSLHandshakeException and respects the operation's byte offset.
2. `solr/packaging/test/test_bats_assertions.bats`: five meaningful cases for wrapped, direct IP and direct DNS rejection, stale diagnostics, and unrelated TLS/connection failures; 14 helper cases now declared.
3. `solr/packaging/test/test_ssl.bats`: use the shared predicate and correct the explanation of JDK wrapping. Existing client failure and connection-refusal checks remain required.
4. `.github/workflows/bin-solr-test.yml`: separate rolling-upgrade job builds/tags the candidate image in the same runner, derives the previous major from source, records and pins image identities, runs the selected packaging test, rejects skipped/missing/failed reports, and archives evidence. At this head the source base version is 11.0.0, so the preceding-major image is Solr 10 rather than Solr 9.

No production behavior, dependencies or changelog changed. The new helper reuses the existing BATS infrastructure and scopes matching to actual JDK producer forms. It does not accept arbitrary TLS exceptions.

## Source reasoning and correction

The deeper review's portability finding is addressed in source. OpenJDK 21 GA calls the hostname matcher directly, while JDK 25 GA can wrap the resulting certificate exception. Therefore the prior blanket diagnosis that the unsupported-algorithm message necessarily means identity checking did not happen is retracted. This is a source trace, not runtime validation of the complete hostname policy.

The Docker finding is addressed by executable workflow wiring; no Docker execution has yet proved it. The existing ordinary script job may still skip its unprovisioned rolling case, but the dedicated new job requires candidate-image execution and explicitly rejects a skip.

## Checks and limits

Reviewed the exact outbound diff and staged file list. `git diff --check` and staged diff check were clean. The additional workflow path has no intervening change between the recorded merge base and PR base. The task-specific helper refuses unexpected files, branch/parent changes, a different fork remote, or a moved fork branch before pushing.

Windows ran no Gradle, BATS, Docker, compiler or product tests. The five new cases are unexecuted assertions until the Linux gate proves their normal and mutant behavior. No old receipt covers this head.

The old gate records are under the descriptive `bats-assertion-hardening` name and remain OPEN/INCOMPLETE. Later October 27 evidence records 57 pass / 4 TLS failures / 1 known skip on each legacy runtime, 18/18 Picocli on each runtime, successful older-head final checks, several controls, and outstanding TLS/select/Docker proof. Those records are historical, not current-head verification.

## Handoff

New exact-head job: `gates/pr-5299-review-fixes-round-1.md`. It supersedes the old candidate input without erasing prior failures, requires capable-host claim/preflight, the normal JDK 21/25 matrix, hostname fixtures/mutations and matched/mismatched real control, all original outstanding assertion controls, a pinned candidate-image rolling upgrade, report-guard negative fixtures, workflow syntax validation, tidy, packaging check and root check.

The old phone-agent-2 claim is administratively stale per the November 4 record. A gate host must still check prior lock/process/log state before takeover. This Windows claim does not claim execution. Publication does not mean a Linux host has accepted or run the job.

The live PR remains an open draft with no comments/reviews at implementation read-back. Its body still describes parent/older evidence and the prior Docker limitation. This report does not edit that public body; the gate receipt must provide an updated draft for authorized publication. No merge, ready conversion or public communication is included.

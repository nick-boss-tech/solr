# Gate: packaging BATS assertion hardening, round 1

Status: OPEN.
Posted at Nick Shanin's direction: patch the static-review findings on a fork branch and hand off for Linux proof.
Tags: gate, bats, records. Intended host: any capable Linux BATS gate host with JDK 21 and JDK 25, Bash/BATS dependencies, Python 3, Git, curl, OpenSSL, keytool, Docker and the two rolling-upgrade images. One gate per host behind its build lock.

## Candidate and ownership

- Fork: nick-boss-tech/solr, branch `solr-bats-assertion-hardening-submit`.
- Exact head: `0d7c6f4e6cb6d2211b59ed9eed36db90182b122e`.
- Base: apache main `0d6b2e32f598d772aed094038022a6a588fdfc5a`.
- Implementation claim: claims/bats-assertion-hardening-implementation-round-1-windows.md.
- Implementation report: reports/bats-assertion-hardening-implementation-round-1.md.
- Review basis: reports/bats-scripts-assertion-review-round-1.md and unit 1/2/3 reports.
- Gate claim target: claims/bats-assertion-hardening-round-1-<host>.md.
- Gate report target: reports/bats-assertion-hardening-round-1-<host>.md.
- GREEN receipt target: receipts/bats-assertion-hardening-round-1.md.
- Current proof: static diff review and git diff --check only; no BATS or Gradle execution yet.
- Seed: not applicable to BATS. Do not invent a randomized-tests seed.

Do not touch test_security_manager.bats, the solr-5225-bats-followup candidate, or gates/pr-5225-bats-followup-round-1.md and its reserved checkpoint controls. This is a separate branch and gate. Existing PR 5204 parser/help work is also separate; do not port its changes into this branch.

Read WORKFLOW.md, START-HERE.md, the host record, this job and existing claims at one live board snapshot. Use detached preflight at the pinned head before claiming, under the standard host build lock/runner and headroom rules. Record the snapshot, exact commands, host/toolchain versions, log paths and UTC start. Do not reuse another claim's worktree or run overlapping packaging jobs.

## Scope

Fourteen test/helper files only, 209 insertions and 49 deletions. Nine new helper regression tests plus strengthened integration assertions. Production CLI behavior is deliberately unchanged. This is test hardening: normal behavior must pass, and deliberately injected unrelated failures must no longer satisfy the assertions.

Legacy focused suites (13 files, 63 declared tests including the Docker case and nine helper cases):

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
test_rolling_upgrade.bats
test_ssl.bats
test_start_solr.bats
test_stream.bats
```

Picocli focused suites (compatible migrated subset, four files, 18 declared tests): test_bats_assertions.bats, test_delete_collection.bats, test_healthcheck.bats, test_postlogs.bats. The other changed suites remain legacy coverage because the normal build's curated Picocli list does not include them. Do not claim Picocli proof for unexecuted suites.

## Runtime setup and normal matrix

Use JDK 21 for Gradle. For each leg set both task-level JAVA_HOME and SOLR_JAVA_HOME to the intended runtime, SOLR_INCLUDE=/dev/null, and SOLR_PICOCLI explicitly false/true. Setting only the Gradle shell JAVA_HOME does not establish the BATS child runtime.

A temporary, uncommitted root-build init script can pin runtime and the Picocli list. Substitute actual absolute JDK paths in the command property; preserve this script with the logs:

```groovy
gradle.projectsEvaluated {
  def packaging = gradle.rootProject.findProject(':solr:packaging')
  if (packaging != null) {
    def runtimeHome = System.getProperty('bats.runtime.java.home')
    if (!runtimeHome) {
      throw new GradleException('Set -Dbats.runtime.java.home to the runtime JDK')
    }
    ['integrationTests', 'integrationTestsPicocli'].each { name ->
      packaging.tasks.named(name) {
        environment 'JAVA_HOME', runtimeHome
        environment 'SOLR_JAVA_HOME', runtimeHome
        environment 'SOLR_INCLUDE', '/dev/null'
        environment 'SOLR_PICOCLI', name == 'integrationTestsPicocli' ? 'true' : 'false'
      }
    }
    packaging.tasks.named('integrationTestsPicocli') {
      testFiles = [
        'test/test_bats_assertions.bats',
        'test/test_delete_collection.bats',
        'test/test_healthcheck.bats',
        'test/test_postlogs.bats'
      ]
    }
  }
}
```

Run through the host's supported runner and build lock, with a distinct preserved log directory per leg:

1. JDK 21 / legacy: `:solr:packaging:integrationTests --rerun-tasks`, with `--tests <file>` repeated for all 13 legacy files above.
2. JDK 25 / legacy: the same explicit file list, runtime changed only through the task environment/init property.
3. JDK 21 / Picocli: `:solr:packaging:integrationTestsPicocli --rerun-tasks` with the four-file init-script list.
4. JDK 25 / Picocli: same four-file list with runtime JDK 25.

Include `-I /absolute/path/bats-hardening.init.gradle -Dbats.runtime.java.home=/absolute/runtime/jdk` for each leg and the host's required Python/toolchain arguments. The Picocli --tests option appends to its default list; the init-script replacement above is intentional. Verify actual selected files from the execution report rather than assuming selection worked.

Require normal tests to execute with expected assertions. Docker is a prerequisite for the rolling-upgrade case: prepare the test's SOLR_BEGIN_IMAGE/SOLR_END_IMAGE defaults (or record owner-approved compatible choices) and preserve image digests; an unavailable image/daemon skip does not prove that changed case. If this leg must move to another capable host, publish an explicit subleg handoff and keep the gate incomplete.

An existing test_start_solr timeout precheck invokes timeout without operands and may skip on GNU timeout. Record the exact skip and baseline behavior; this unchanged case is a known coverage limit and must not be relabeled as executed. There must be no unexplained additional skip, zero-test result, startup failure accepted as a pass, or stale/up-to-date report.

Preserve fresh JUnit XML, full BATS transcript, all Solr logs/console logs, child environment/parser and runtime JVM fingerprints, tool versions, exit code, pass/fail/skip counts, UTC timestamps, worktree HEAD and source cleanliness for each leg before another task deletes its output directory.

## Discriminating controls

All mutants/injections run in isolated scratch trees or wrappers and are never pushed. Retain the actual injected diff/wrapper, commands, stdout/stderr, XML and state observations. A control is successful only when the hardened check rejects the injected bad outcome at the intended assertion, not from an unrelated environment error.

1. **Helper regression suite:** all nine cases must pass normally. In isolated copies, weaken each guard separately: permit failed ZK listings; accept any nested healthy status; accept healthy-looking output with nonzero command exit; accept Server refused connection, bare ClosedChannelException, or an unrelated TLS protocol error as a certificate failure. The corresponding named helper regression must fail. These are synthetic fixture controls; label them as such, not as real TLS/server reproduction.
2. **Posting count:** in a real focused post test and the authenticated post case, make document adds fail/no-op while commit succeeds and the attempted-file summary is still printed. Preserve fresh query evidence that the intended document was not indexed. The old assertion shape can accept the count; the candidate must fail its new exit/state/error checks. Do not claim a false pass for no-type/directory/extraction tests whose existing queries already reject an empty index.
3. **Postlogs:** fail only batch adds while allowing empty commit. Preserve Batch sending failed plus Committed and query state. Both normal and authenticated log-post checks must reject it; the authenticated monitor document must not satisfy the file_s query. Also inject a failed HTTP/select response after posting: a body lacking numFound must not count as positive documents.
4. **TLS:** after establishing the positive TLS preconditions, refuse only the negative probe connection. Each hardened certificate check must reject a ConnectException/Server refused diagnostic, and curl must reject connection-refusal exit 7 instead of treating it as peer-verification exit 60. Separately retain real missing-client-cert, peer-name, client-hostname, truststore and reload causes from the unmodified normal JDK 21/25 cases.
5. **Health:** preserve a responsive collection with a DOWN/non-ACTIVE replica or another real degraded state, while the health command returns zero and a nested shard can still be healthy. The collection-level predicate/poll must reject it. The final Docker live-node count alone is insufficient. Preserve the report and actual replica state.
6. **Deletion / exit code:** keep a child present and make only the relevant post-delete ZK listing fail; the absence check must reject the lookup failure. In a separate mode-assert control, inject tool-error exit 100 only on the --exitcode invocation; the candidate's required exit 1 must reject it.
7. **Export / authentication diagnostics:** after the first export creates output.json, fail the repeated export before it touches output; prior existence must not satisfy the new check. Separately inject an unrelated error before export's standalone/cloud diagnosis and a missing/unreadable expression before stream's authentication request. Neither must satisfy the new specific-cause assertions.
8. **Quiet post / compression / stop:** fail only the quiet args post and retain its WARNING/error; it must not pass suppressed-output checks. Fail the no-compression curl request after normal setup; empty output must be rejected. Fail/no-op the tested graceful stop while both processes remain live; successful teardown later must not satisfy the tested operation.

For the representative posting, commit, TLS and stale-export scenarios, compare the old base test shape and the hardened test under the same injection where practical: old acceptance plus new rejection is the relevant discrimination, followed by passing unmutated candidate tests. Do not force a conventional fail-before claim where the old defect is a wrong pass. Explain precisely what each base/synthetic leg proves.

## Failure investigation and repair

Java TLS may surface only a closed channel to the client. Do not make a failing normal test green by restoring generic connection/closed-channel alternatives, accepting an arbitrary SSL exception, or skipping it. Inspect the real JDK/Jetty client and server cause. If client output omits the specific cause, add an operation-scoped, fresh server-log check using an observed/producer-traced diagnostic and retain the connection-refusal control. No expected-order regex may be invented without checking its real producer/output.

Likewise, validate the actual verbose export mode and stream HTTP-401 messages; if a wrapping layer hides them, trace and use the real specific cause. A pattern mismatch is a candidate repair, not permission to accept ERROR or the complement of success again.

Routine test-scoped gate repairs and formatting fixes on this fork branch are authorized by Nick's patch-and-gate request. No Java/launcher behavior changes or unrelated PR 5204/5225 work are authorized. Commit/push any repair normally with Nick Shanin identity, no trailers, no force; verify the remote. Post a superseding add-only head/job record and obtain final proof on that new exact head. Never silently move this claimed job's input or treat old-head runs as final-head proof.

On a failed executed leg, investigate the captured evidence and repair the cause before any retry; distinguish pre-existing failures on the pinned base from regressions, and record any unresolved baseline blocker. No blind reruns.

## Formatting, completion and receipt

Run tidy preflight at the pinned head before claiming. Run final `tidy`, `git diff --check`, and `:solr:packaging:check -x test` on the final exact head through the normal host runner. No repository-wide application test run is requested. Report Error Prone only if it actually executed; preserve errorProneSkipped accurately.

GREEN requires the final-head normal matrix, all named discriminating controls (or explicit evidence-backed non-reproduction/retraction of a suspected premise), successful final formatting/checks and no unexplained source changes. Missing Docker/JDK/runtime legs or unmatched TLS cause assertions keep this gate incomplete, not green.

Publish an add-only report with every leg/control's actual result and artifacts, plus the final fork head. On GREEN only, write receipts/bats-assertion-hardening-round-1.md with final head, exact matrix, control results, limits, formatting/check outcome, logs and a concise PR description draft. No PR opening, upstream push, comment or Jira write is authorized by this job.


# Interrupted client-close fix: final preparation and Linux validation, round 1

Status: OPEN
Tags: implementation, gate, records
Intended host: any available capable pool Linux host with these tags and JDK 21.0.12.1+1; vm2 holds the original proof artifacts

## Scope and inputs

Owner's ongoing instruction authorizes review and handover of further required
Linux proof. Read `reports/teardown-interrupt-close-fix-review-round-1.md` and
`reports/teardown-interrupt-close-fix-round-1.md` first.
Review commit: 5cd2d7efdea6620860d6cdba5bcdfb2d649f2029.

Fork: nick-boss-tech/solr, branch **solr-teardown-interrupt-close-fix**.
Starting head H: **8787c6605ad5a5d322e9bca253235cd73142f101**.
Base M: d298cd0b42602247bee7bf9e2bcf302e725f6690.
JDK: 21.0.12.1+1, Linux. Seed: FFF5A3C3AAC69084.
Security manager on; file encoding ISO-8859-1; host build lock and guarded runner.

This is final validation of an implemented fix, not another premise pair. Do not
rerun the unchanged before/focused-class legs or extend the fix to interrupts
arriving during stop. No public PR/Jira action, dependency change, new production
hook, seed matrix, root application suite or unrelated ticket. The earlier
two-invocation probe budgets remain closed.

## First preserve existing proof; no rerun to recreate it

On vm2 archive the existing runner logs/status and fresh JUnit XML in
`/workspace/gates/gates-vm2/logs/fix-before/`, `fix-after/`, and `fix-final/` before
any clean or cleanup. Include command lines, source heads, runtime pins and
SHA-256 manifest. Publish raw artifacts at new unique board evidence paths and
link them in the result. Another host may perform the new gate without access to
vm2-local files, but must label the old evidence host-reported until archived.
If any original artifact is gone, disclose it; do not silently reproduce it.

Expected old before shape: the owned and supplied-executor methods fail with
Exception on closing client; borrowed passes; classMethod reports tracking left
by the failing tests. These are not three independent fixed defects. The final
reported class result is 53/0/0/0 on H. Parse preserved XML instead of restating
counts from memory. Do not overwrite the older external-executor/probe records.

## Phase 1: small source preparation, then pin the final head

Refresh the live branch and existing claims. If H moved, inspect the new commits
and post a reconciliation before execution; do not silently gate another input.
Use a clean dedicated worktree and one driver under the host lock.

In HttpJettySolrClientTest.java add the import
`org.eclipse.jetty.client.HttpClient` and shorten only the three new fully
qualified declarations. This obeys AGENTS.md:23. No production edit and no new
test semantics are requested. Run tidy through the guarded runner and inspect
the diff. Only this test cleanup and formatting of the branch's two Java files
may be included. Do not fold unrelated base formatting or generated changes in.

Commit that preparation with Nick Shanin's ICLA identity and no trailers; plain
push to the same fork branch and independently verify its remote SHA. No force
push. Record H -> final head F. If the cleanup has already been independently
performed, verify it and reuse that clean head rather than repeat it.

Before executing the gate, add a **new immutable gate file** at
`gates/teardown-interrupt-close-fix-final-validation-round-1.md` with full F,
branch, base, exact commands below, host/runner/lock/driver details and unique
log paths. Record F and gate path in the assignment claim or a new phase claim.
Follow the board's tidy preflight rule before claiming that gate. From this
point onward, the gate proves only F. No source or test edits during its legs.

## Phase 2: final-head checks

Archive prior logs first. Run sequentially from the clean F checkout through the
guarded host runner; no overlapping driver, Gradle or test JVM. Record all
commands, timeouts, exit codes and tree state. The commands below are the task
payloads; retain the host's documented wrapper and containment.

Formatting preflight (already performed in phase 1; verify it stays clean at F):

```bash
./gradlew --no-daemon --max-workers=2 tidy
```

Force actual module compilation with Error Prone enabled. Cleaning only this
module and disabling the build cache prevents claiming a cached/non-executed
target compilation as the requested check:

```bash
./gradlew --no-daemon --max-workers=2 --no-build-cache \
  :solr:solrj-jetty:clean \
  :solr:solrj-jetty:compileJava :solr:solrj-jetty:compileTestJava \
  -Pvalidation.errorprone=true
```

Require compilation to execute and Error Prone to be enabled. An alternative
toolchain / errorProneSkipped result is incomplete proof, not a pass. Do not
disable warning failures, skip checks or upgrade dependencies to make it green.

Run the module static checks:

```bash
./gradlew --no-daemon --max-workers=2 \
  :solr:solrj-jetty:check -x test -Pvalidation.errorprone=true
```

Run the **whole solrj-jetty module suite once**, with fresh XML and no --tests
filter. Its concurrent-update, compatibility, proxy and authentication consumers
are the reason for widening beyond the previously green close test class:

```bash
./gradlew --no-daemon --max-workers=2 \
  :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test \
  -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 \
  -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1
```

Do not invent an aggregate test count. Parse new XML by class and method and
verify all three new interrupt-close methods ran. The earlier report counts
53 test cases for HttpJettySolrClientTest; identify any difference. The source contains
seven concrete test classes. Explain exclusions/skips from actual runner/config
evidence, and do not present skipped new regressions as a passing gate.

This phase allows at most **one module-suite invocation**, no automatic repeat
or new seed. Use a 30-minute per-leg watchdog and 90-minute total execution cap
including preparation. A failed test/check requires diagnosis and a new named
repair input before another run; do not silently retry or add exclusions. If
tidy changes source at F, the gate is invalid for that SHA: commit/push the
allowed preparation, record the replacement head and gate before execution.

## Receipt and stopping point

Publish `receipts/teardown-interrupt-close-fix-final-validation-round-1.md`, owned
by the gating host, and raw evidence with hashes at new unique paths. Include
starting/final/verified remote SHA, exact task and runtime pins, clean-tree
guards, logs and exits, parsed fresh XML totals and new method names, recovered
before/final proof artifacts or their explicit absence, plus any warnings,
excluded tasks or limitations. Do not call module check a root-repository check.

End with a concise PR description draft using only verified final-head proof.
Keep the pre-existing-interrupt scope, AI disclosure and the interrupt-during-
stop / original-CI limits. No public action is part of this assignment.

No changelog is added before there is a Jira or PR number: AGENTS.md:83-84 says
not to. The source supports a PR-linked fragment after a PR is authorized and
numbered; record that remaining publication step without fabricating an ID or
posting a no-changelog comment. Any later source-head change needs its own
appropriately scoped exact-head validation before claiming full proof.

On green, this unit ends at the receipt and draft, with the fork head pushed and
verified. Opening a PR remains Nick's decision. Do not start the other ranked
sweep candidates, BATS work or DistributedExpand settling runs from this unit.

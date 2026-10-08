# Gradle queue handoff: the round 36 branches (2026-10-08)

From: VM1, the original Linux queue owner and master record
keeper. The machine-readable queue is
`inventory/queue-2026-10-08-round36.tsv` (78 branches).

## Purpose

The 78 branches of round 36 went to review first (the
`code-review` branch on this fork carries the reviews). Every one
of them still needs a full gate before it can ship, and gates do
not parallelize on one machine. This VM is the second gate lane:
it gates branches as their reviews complete, in parallel with
VM1's lane, and reports results on this branch. VM1 folds the
results into the master record and does the post-green packaging
from its side.

## Which branches, in which order

Gate a branch when ALL of these hold:

- Its review on the `code-review` branch is complete (a
  `reviews/<slug>.md` with a final verdict at the branch's
  current head, and any patches the reviewer made are pushed).
- The queue TSV's gate_state for it is not GATED at the current
  head and not HELD-BY-VM1.
- You have claimed it in `claims/` on this branch and your claim
  push landed.

Priority: (1) review-complete branches that were never gated
(the TSV state says NOT GATED; start with the Group A branches
whose reviews are already up), (2) Group B branches as their
reviews and patches complete, (3) re-gates of GATED branches
whose heads have moved since VM1 gated them. SOLR-9342 is
script-only: light verification only (no Gradle; `bash -n` and
the script's own checks), recorded the same way.

Not in this queue: SOLR-10424, 10789, 17287, 17372, 17708,
17029, 16914, 17842 (gating on VM1); SOLR-13705, 14171, 16499
(Windows gate lane); the five spot-check branches 10492, 11364,
12007, 12543, 13943 (a VM1 wave). The four HELD-BY-VM1 rows
(15863, 15003, 3729, 6320) join this queue when VM1 updates
their gate_state; until then, leave them alone.

## Reading the branch before gating

Most branches carry `SOLR-<ticket>-TESTING.md`: the author's
run-shape note (which tests, what the premise should show).
Read it first. It STAYS in the branch during the gate; it does
not affect tidy, compile, tests, or check, and removing it is
post-green packaging done by VM1. Treat any "hypothetical" or
"unrun" label in it as an unverified claim: the premise leg
below is where it gets proven or not.

Heads move. Re-read the branch tip when you claim it; gate the
head you claimed, and record that head in the result from
`git rev-parse HEAD` in the worktree. If the tip moves mid-gate,
stop and say so in the result file.

## Toolchain

- Temurin JDK 21 (record the exact `java -version` string in
  the result).
- Gradle 9.7.0 (the project wrapper pins it).
- Copy the repo's `gradle.properties` into every worktree
  before building (it carries file.encoding UTF-8,
  org.gradle.workers.max=1, tests.jvms=1). Set
  org.gradle.caching=true in the user-home gradle.properties.
- One Gradle build at a time on this VM.

## The gate (steps in order, stop at the first failure)

0. Changelog parse: strictly parse every changelog fragment
   the branch adds or changes under `changelog/unreleased/`
   as plain YAML. An unquoted title containing ": " is invalid.
   If no parser is available, report NOT RUN for this step.
1. Tidy: run the tidy task (module-scoped when every changed
   Java file is in one module, root tidy otherwise). Restore
   `gradle/libs.versions.toml` afterward (tidy prunes an
   unused entry as a side effect). If tidy leaves ANY other
   modification, the gate is INVALID: stop, and put the full
   diff in the result file. Do not commit it.
2. Error Prone compile: `<module>:compileJava
   <module>:compileTestJava -Pvalidation.errorprone=true`.
3. Premise leg: in a separate worktree at the branch's
   merge-base, copy in ONLY the branch's new or changed test
   files, run the focused classes, and record the counts and
   the failing test names from the JUnit XML. A premise that
   passes on the base, or fails for an unrelated reason, is a
   finding: record it plainly. Some branches are honest pins
   whose tests cannot discriminate by construction; say so
   instead of dressing them up.
4. Head leg: run the focused classes at the claimed head.
   Seed: `<ticket>C0FFEE<ticket>` unless the TESTING.md names
   a different one. Counts come from the JUnit XML at
   `<module>/build/test-results/test/TEST-<fqcn>.xml`, never
   from console summaries.
5. `<module>:check -x test`.

Focused classes: the ones the TESTING.md names; otherwise the
test files the branch adds or changes. Get each class's exact
package from its file (never construct it from the class name);
one class per run is the safe default.

Platform caveats: if a branch's tests are file-lock heavy or
depend on symlinks, permissions, or case sensitivity, or if a
failure smells platform-specific (paths, separators, line
endings), mark the result PLATFORM-SUSPECT and stop; VM1
confirms locally before any code changes anywhere.

## The result file

`results/<slug>.md`, one per branch, containing:

- Branch, head gated (full SHA from `git rev-parse HEAD`),
  base (merge-base SHA), JDK version string.
- Per-step rc for steps 0 to 5, with the tidy verdict.
- Premise leg: class, seed, tests/failures/errors/skipped
  from the XML, and the failing test names.
- Head leg: same.
- The first ~30 lines of any failure output.
- Final verdict: GATE GREEN, GATE FAILED (name the step), or
  GATE INVALID (tidy).

## Coordination

- VM1 is the master record keeper. It reads results from this
  branch, records them in its ledger, and does the post-green
  packaging (including the TESTING.md removal commit) from its
  side. The gate lane never needs to push a submit branch.
- Claims are the only locking between the lanes: VM1 will not
  launch a local gate for a branch claimed here, and this lane
  does not gate a branch VM1 holds (HELD-BY-VM1 rows, or a
  branch VM1 announces in an inventory update).
- New arrivals and released HELD branches arrive as updates to
  the queue TSV on this branch, with a note in
  `inventory/handoff-updates.md`.

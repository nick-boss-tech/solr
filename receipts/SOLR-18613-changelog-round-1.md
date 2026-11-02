# Receipt: SOLR-18613 changelog-only gate (phone agent)

Status: GREEN (metadata-only) at the exact head.

- Job: `gates/SOLR-18613-changelog-round-1.md`. Claim:
  `claims/SOLR-18613-changelog-gate-phone-agent.md` (phone agent, started
  2026-11-02T17:11:39Z, at Nick's direction in the main chat).
- Branch: `nick-boss-tech/solr:solr-lb-attempt-limits-pr1-submit` at
  H `b49413e5ab5217ec45f1693fa9eff6c3e11f78dc`. Parent:
  `c2d2c6259462a7e848008b065d65df69a1b82ebb`, gated GREEN in
  `receipts/SOLR-18613.md` (unchanged by this gate).
- Checkout: full clone `~/workspace/solr-clones/18613-gate` (real .git
  directory), detached at H; the same clone the parent gate used.
  Toolchain: vendored JDK 21 and Gradle 9.7.0 via
  `~/workspace/tools/solr-gradle.sh`; Python 3 with PyYAML 6.0.1. Runner:
  `~/workspace/tools/18613-changelog-gate-runner.sh`; outputs
  `~/workspace/tools/18613-changelog-gate-runner.out` (attempt 1) and
  `~/workspace/tools/18613-changelog-gate-runner2.out` (attempt 2, ends
  GATE GREEN, GATE RUNNER DONE). Step logs
  `~/workspace/tools/18613-changelog-*.log`.

## Delta and fragment review

- The entire parent-to-head diff is the single new file
  `changelog/unreleased/SOLR-18613-lb-attempt-limits.yml` (10 lines, one
  file changed, 10 insertions), verified in the runner and before it.
  No code, test, configuration, or dependency change.
- Fragment review: type `fixed`; authors list is the single entry
  `name: Nick Shanin` (no nick field); links carry SOLR-18613 with the
  Jira URL; the title states the change's actual scope (attempt limits
  across live endpoints and zombie fallback, no retry of exhausted live
  endpoints, identifiable exhaustion in sync and async requests).

## Fresh checks at this head

- Fork ref verified `b49413e5ab52` before and after the run.
- YAML validation: `python3 dev-tools/scripts/validate-changelog-yaml.py`
  rc=0, fragment reported valid (log `18613-changelog-yaml.log`). The
  Windows attempt recorded in the job stopped at a missing PyYAML import;
  PyYAML is present on this host, so the validator itself ran here.
- Tidy: rc=0, 0 tracked files changed (attempt 2; see the environment
  note below).
- Root check in the full clone: `check -x test` BUILD SUCCESSFUL
  (2m 58s, 556 tasks: 186 executed, 2 from cache, 368 up-to-date). The
  up-to-date share is high because this clone also ran the parent gate at
  the parent tree and only the changelog file differs; every task's
  inputs are keyed to this head. As in the parent gate, this leg ran
  with the combined Hatch CA truststore in JAVA_TOOL_OPTIONS after a
  daemon restart, and no task was excluded beyond the job's `-x test`.
- Final state: HEAD unchanged, `git status` clean, `git diff --check`
  clean.

## Environment note (attempt 1)

Attempt 1 stopped at the tidy step before any task executed: Gradle
could not start because the shared Gradle user-home journal cache was
locked by a daemon left over from the parent gate's root-check leg (an
incompatible daemon configuration, owner PID 19490, already dead when
inspected). Remedy: remaining Gradle daemons stopped, the stale journal
lock removed, and the runner relaunched unchanged as attempt 2. This was
a host environment collision, not a branch or fragment failure; the tree
stayed clean throughout.

## Carried-forward runtime proof (not run at this head)

Per the job, the runtime proof from the parent receipt carries forward
unchanged, because no Java or test blob differs between parent and head:

- 47 focused and neighboring tests, 0 failures, 0 errors, 0 skipped at
  `c2d2c6259462`, seed 5B407A2D9C1310E7 (`receipts/SOLR-18613.md`).
- The four original-production discriminating failures, reconciled in
  that receipt and originally reported in
  `receipts/lb-attempt-limits-pr1.md`; they used a synthetic base (the
  two tests plus the inert exception declaration), not untouched
  production. Only 4 of the 24 new methods are discriminated; the other
  20 are compatibility pins.
- Nothing in this receipt claims those tests ran at `b49413e5ab52`.

## Limits and state

- This is a metadata-only GREEN: it covers the changelog completion and
  the root check at this head, on top of the parent's runtime proof.
- No PR, public comment, or Jira edit was made. Opening the PR for
  SOLR-18613 is Nick's call; the PR description draft in
  `receipts/SOLR-18613.md` should have its changelog line updated when
  used, since the fragment now exists at this head.

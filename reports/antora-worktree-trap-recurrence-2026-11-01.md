# Antora worktree trap: recurrence record (2026-11-01)

Host: phone agent. Posted at Nick's direction on 2026-11-01, after
the trap cost a gate leg again on the PR 5279 gate.

## Mechanism

Antora requires its local content source to be a git repository
directory. In a git worktree, `.git` is a file (a `gitdir: ...`
pointer), not a directory, so `:solr:solr-ref-guide:buildLocalAntoraSite`
fails with `Local content source must be a git repository:
<worktree>`. The Gradle log may show only that the npx process
exited non-zero; Antora's own message does not reach the Gradle
log. Any gate leg that runs a root `check`, a `:check`, or a
ref-guide task inside a worktree hits this, regardless of the
branch under test. It is a run layout failure, never a branch
defect. The same `.git`-is-a-file shape is what makes `:rat`
misbehave in worktrees; the two traps are siblings.

Canonical fix and diagnosis procedure: `pool/guides/gradle-gate-gotchas.md`,
item 1. In short: run the affected leg in a normal clone (a real
`.git` directory) at the same head, and disclose the layout in
the receipt.

## Occurrences recorded so far

- 2026-10-03, SOLR-5821: ref-guide build failed in a linked
  worktree; verification was moved to the main checkout.
- 2026-10-19, SOLR-18555: `:solr:solr-ref-guide:check -x test`
  failed at `buildLocalAntoraSite` with the same message at the
  branch head and, on a control run, at base; recorded as a
  pre-existing run-shape limit, not caused by the PR.
- 2026-10-24, SOLR-17988: round 2 failed in a linked worktree;
  round 3 in a standalone clone passed `buildLocalSite`,
  `checkSiteLinks`, and `checkCliDocsUpToDate`.
- 2026-11-01, PR 5279: a worktree root `check -x test` ran
  12m 31s (525 tasks executed) and failed on
  `buildLocalAntoraSite` alone; no yarn or kotlinWasm task
  appeared in that log. The same head then passed the full root
  check in a normal clone (555 tasks, BUILD SUCCESSFUL). Receipt:
  `receipts/PR-5279-analysis-extensibility-tests.md`.

## Why it keeps happening

`pool/bin/sweep-run.sh` refuses these tasks in a worktree (exit
5), but that protection only covers runs launched through
sweep-run.sh. Gate runners written per gate (the phone agent's
local gate-runner scripts are one example) launch Gradle
directly, so they bypass the refusal and spend the worktree run
before anyone reruns in a clone.

## Rule going forward

- Any gate runner, however launched, checks the checkout before
  a root `check` or ref-guide leg: if `.git` is a file, that leg
  runs in a normal clone at the same head from the start.
- A worktree Antora failure is recorded as a layout failure in
  the receipt, with its log kept; it is not evidence about the
  branch, and it is not the yarn or npm split.
- Gate files that ask for a root check should name this rule, so
  the claimant does not have to rediscover it.

No code change, no PR, no Jira or GitHub post outside this
board.

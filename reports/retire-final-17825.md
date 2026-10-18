# Retire final pack: SOLR-17825

Verdict: RETIRE CLEAN. The branch's beanutils upgrade has nothing to change on main, because cross-dc-manager has no commons-beanutils on its runtime classpath.

Checked 2026-10-15.
- Fork tip: `origin/solr-17825-submit` = 0ef08ec86c650af99a51029da29e4c36503a70c4 (ls-remote origin refs/heads/solr-17825-submit). Matches the receipt and RR1.
- Main: upstream main = 714d6ceff852e6dc743ef51ae094e4eaf8f3b3f9 (live).

## Premise

From `receipts/SOLR-17825.md`:
- "NO GATE, and none is owed. The branch is RETIRED as obsolete on the main side's record."
- The branch "upgrades the commons-beanutils jar to 1.11.0 or later in solr/cross-dc-manager/build.gradle to fix CVE-2025-48734, but cross-dc-manager no longer carries the kafka213 runtime dependency or any beanutils dependency on current main (the Kafka 4 upgrade removed them), and the ticket is Resolved upstream (fixed in 10.1). The branch's own guard task would fail check on main now."

RR1 left one point open: transitive absence was not settled without a build. It is settled below on the lock record.

## What the branch holds

Merge-base 86bc6f292245. Branch-only commits 4b35e5a5165 and 0ef08ec86c6. Two files:
- `solr/cross-dc-manager/build.gradle`: a beanutils 1.11.0 floor constraint and a `verifyCommonsBeanutilsFloor` task wired into check.
- `SOLR-17825-DECISION.md`.

## Checked on main 714d6ceff

- `git grep beanutils` under `solr/cross-dc-manager` returns no match. That covers build.gradle, the lockfile and the sources.
- `solr/cross-dc-manager/gradle.lockfile` holds the resolved runtimeClasspath entries. Line 107, for example, is `org.apache.kafka:kafka-clients:4.3.1=...,runtimeClasspath,...`. It has no commons-beanutils line.

So the project lock record puts no beanutils on the cross-dc runtime classpath.

## Gainable, and what is taken

- Gainable from the branch: nothing. Its guard task would fail on main.
- Main-side note, not a gain from this branch: the root `gradle.lockfile` pins commons-beanutils 1.9.4 for the ratDeps configuration (RAT validation, build time only, RR1 section 5). If the owner wants that build-tool pin moved, that is a main-side change.
- Taken: nothing.

## Owner step

1. Label the fork branch `solr-17825-submit` RETIRED and leave it in place (owner rule, 2026-10-14: a retired branch is never deleted).
2. PR: none. No PR authored by nick-boss-tech on apache/solr has this head (`gh pr list --author nick-boss-tech --state all`, 2026-10-15).
3. Local worktree `wt\SOLR-17825` is clean and tracks origin. Its local branch is `wt-solr-17825-submit`. Leave both as they are.
4. Optional, the owner's call per the receipt: the SOLR-18300 Jira link. Not needed for the labelling.

Deletion is not the current rule. It would apply only if the owner reverses the 2026-10-14 ruling: `git -C C:\Users\shaninna\dev\Solr-issues\source push origin --delete solr-17825-submit`. Here origin is the fork (nick-boss-tech/solr).

## Jira note (draft, not posted, optional)

"Branch retired without a change. On current main, cross-dc-manager no longer has commons-beanutils on its runtime classpath after the Kafka 4 upgrade, so the beanutils upgrade proposed on the fork branch has nothing to change."

Jira status: Resolved, per the receipt. Not checked live. No comment is needed for the labelling.

## Condition

The retire call depends on no commons-beanutils below 1.11.0 reaching the cross-dc-manager runtime classpath on main. The lock record says none does. For a build-level check, the owner can run a runtimeClasspath dependency report for `:solr:cross-dc-manager` on the gate host. This is not needed for the retire call. If beanutils below 1.11.0 does show up there, the CVE is live on main, the upgrade becomes gainable, and this verdict must be reopened.

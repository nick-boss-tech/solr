# Assignment: SOLR-18539 QueryComponent totals fix on current main, round 1

Status: DONE

- Completed: 2026-10-24 by vm2 under claim `claims/solr-18539-port-main-round-1.md`. Gate GREEN at the pushed head `9a16d680231` on branch `solr-18539-merge-totals-main`; receipt `receipts/SOLR-18539.md` (fail-before recorded there: both new assertions fail on base). Status flipped in the 2026-10-27 cleanup, `reports/phone-agent-open-work-cleanup-round-2.md`. The only remaining step is the owner's PR-opening go, which is not pool work.

- Intended host: phone agent, or the Linux gate host it hands this to. Tags: implementation, gate.
- Ticket: SOLR-18539, the follow-up that `reports/pr-5117-followup-plan.md` names for the `QueryComponent.mergeIds` partial-shard totals leak.
- Source commit: `f1bb8743d5d` ("SOLR-18541: skip a partial shard before its totals in QueryComponent"), on vm2 at branch `solr-18541-followups` (worktree `/workspace/worktrees/work-18541`). Not on the fork yet, because vm2 cannot push (see the claim `claims/vm2-pr5117-leftover-decisions-round-1.md`). Its parent is `49bcb450a27`.
- Base: upstream main `79b23df58daac14b92cc35f23b8b81f72098b4ba`.
- Checked: `QueryComponent.java` is identical on main and in the PR chain, so the production hunk should apply on main.

## Scope (from the plan, rung 3)

- Production change only in `QueryComponent.mergeIds`: a field-sorted partial shard that the existing skip path excludes adds nothing to `numFound`, `maxScore`, or `approximateTotalHits`, and the merged exactness goes false. The no-header `approximateTotalHits` fallback adds that shard's own `docs.getNumFound()`.
- Do not change the non-sort partial-result behavior (SOLR-15595).
- Do not include `c91b606355e` (the `CombinedQueryComponent` bounds-check). It is a separate defect, see `assignments/solr-18541-short-list-owner-round-1.md`.

## Steps

1. Create `solr-18539-merge-totals-main` from `79b23df58da`. Cherry-pick `f1bb8743d5d`. The new test `QueryComponentMergeTotalsTest` uses helpers that may be missing on main (`MockShardRequest`, `MockResponseBuilder`). Port it to main's helpers, and keep only what the two SOLR-18539 assertions need.
2. Add a changelog fragment under `changelog/unreleased/` for the SOLR-18539 title. The commit's `SOLR-18541` subject must change to `SOLR-18539`.
3. Fail-before: run the new test on `79b23df58da` with the production file reverted. Both new assertions must fail there. Record the failing names.
4. Gate at the exact pushed head: changelog YAML parse, tidy with the tree clean afterwards, Error Prone compile (`-Pvalidation.errorprone=true`), focused `QueryComponentMergeTotalsTest` and `QueryComponentPartialResultsTest`, and `:solr:core:check -x test`. Use `JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1` and `GRADLE_USER_HOME=/workspace/gradle-home`.
5. Push the branch to `nick-boss-tech/solr` after the gate. Write `receipts/SOLR-18539.md` with the head, the counts, and the fail-before names.

## Limits and owner items

- Pushing the new fork branch is standing. Opening the PR is not: it needs Nick's go. The PR body must not say the offered follow-up is done until the gate receipt exists.
- No JIRA or GitHub comment. No trailers. Commit as Nick Shanin.
- Stop and post a new assignment if the port needs a production change beyond the hunk above.

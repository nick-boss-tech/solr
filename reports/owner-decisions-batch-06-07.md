# Owner decisions left open in skip remediation batches 06 and 07

Decided by vm5 on 2026-10-20T11:42:33Z with `material/decision-ladder.md`, per Nick's direction that open decisions are settled in the pool and the technical recommendation is accepted. Each entry names the rung used, the choice, and the reason. The queue verdicts in `material/skip-audit/queue-current.json` already match each choice; no draft verdict changed.

## SOLR-13505: scope of close-obsolete

- Rung 3 (the ticket's named target, the Techproducts example link) with rung 1 (MoreLikeThis defaults are the component's intended design, per the 2019 comment).
- Choice: close-obsolete, scoped to the example link (`solr-13505-draft-close`). The component's defaults stay as they are.

## SOLR-13305: close as satisfied

- Rung 1 (the timeout setting is present at the pin) and rung 4 (the ask also names an environment variable for the scripts, which does not exist).
- Choice: keep-open. The variable is not added in this pool, and the ref guide does not answer the question, so close-answered does not hold. The request for a script variable stays open for the maintainers.

## SOLR-13390: default-on

- Rung 4 (back-compat: an install already configuring the component under another name would get a second instance).
- Choice: keep the component opt-in. No default change.

## SOLR-13346: scoring of per-group phrase boost

- Rung 4 (a per-group boost changes scoring for every query with boolean operators, which is beyond the ticket's scope).
- Choice: no scoring change in this pool. keep-open.

## SOLR-13283: limit and offset on explicit term lists

- Rung 1 (no maintainer spec, the one comment asks for patches) and rung 4.
- Choice: no change to `{!terms}` in this pool. keep-open.

## SOLR-13310: facet executor ownership

- Rung 4 (no load evidence of update starvation at the pin).
- Choice: keep the shared executor. No change without a load run. keep-open.

## SOLR-13501: whether the rename satisfies the ticket

- Rung 1 (the class is renamed, but the concurrent client the ticket asks to remove is still the update path) and rung 4.
- Choice: the rename does not satisfy the ticket. keep-open (`solr-13501-draft-open`).

## SOLR-13278: two-dictionary run

- Rung 2 is needed; the decision is to run it, not to guess the outcome.
- Choice: open work, not a decision. It is claimable as a run.

## SOLR-13270: scope of close-obsolete

- Rung 1 (the Apache HttpClient-based client the ticket names was removed by cf7f5079e8, SOLR-17962, an ancestor of the pin; the named class has no hits in the tree).
- Choice: close-obsolete, scoped to that client (`solr-13270-draft-close`).

## SOLR-12646: warning text

- Rung 1: the warning text in the ticket ("Resetting task ... requestid ... taskKey") is still logged at `solr/core/src/java/org/apache/solr/cloud/OverseerTaskProcessor.java:672`, so the close-obsolete draft does not hold.
- Choice: keep-open (`solr-12646-draft-open`, the verdict batch 08 already recorded). The close-obsolete draft is superseded and not used.

## SOLR-15443: stray branch solr-15443-draft-keep

- Rung 5 for the deletion itself: deleting a branch is shared-state and the standing rules require the owner's OK.
- Choice: keep the branch. It is not deleted in this pool; the owner decides only whether to delete it, and the pool records no other open question on it.

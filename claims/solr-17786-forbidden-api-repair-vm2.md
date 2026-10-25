# Claim: SOLR-17786 forbidden-API repair decision (vm2)

- Claimant: vm2. Started: 2026-10-25T16:20:00Z (approximate; first board record of this repair).
- Input: `solr-17786-reverse-cursor-submit` at `2c8af559c8456ded97811455622fb06786ae74ba`, round-1 two-shard gate final check `:solr:core:forbiddenApisMain` failed at `solr/core/src/java/org/apache/solr/search/SortSpec.java:68` (`Collections.singletonList(null)`, rule `Collections#singletonList(**)` in `gradle/validation/forbidden-apis/defaults.all.txt`). Logs: `/workspace/gates/gates-vm2/logs/solr-17786/check.log`.
- Introduced by branch commit `7ba3b78c45c1` (the base `f3a798e424bb` has no such call).

## Ladder record

- Rung 1 (code and history): the same intentional call exists in `solr/core/src/java/org/apache/solr/security/RuleBasedAuthorizationPluginBase.java:373`, and it is annotated `@SuppressForbidden(reason = "singletonList(null) is intentional")` on the method at line 362. That is the repo's own contract for a list that must hold a single null, and the forbidden-API check passes there.
- Rung 2: not needed; the check output names one call and the rule.
- Rung 3 (stated intent): the gate job requires `:solr:core:check -x test` to pass and says code/test repairs are committed ticket-scoped and gated by a superseding job pinned to the new head. The job also says no other source changes are authorized; this change is the minimum needed to satisfy the required check, adds no behavior, and touches one file.
- Rung 4 (choice): options were (A) `@SuppressForbidden` on `SortSpec.reverse()`, matching the precedent exactly; (B) `Arrays.asList((SchemaField) null)`, which is not forbidden but changes list mutability and the list type's semantics. Chosen: (A), for zero behavior change and an exact precedent.
- Rung 5 not reached: the change is ticket-scoped, behavior-preserving, and consistent with the existing contract, so it does not widen the approved scope.

## Action

1. Commit only `SortSpec.java` (import plus annotation), as Nick Shanin with no trailers, fast-forward to the fork branch.
2. Publish a superseding gate job pinned to the new head and its claim, then rerun the full gate there. The old head's partial results stay as recorded.

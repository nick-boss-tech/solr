# Report: PR 5265 split-shard follow-up, round 1 (phone agent)

- Assignment: `assignments/pr-5265-split-shard-followup-round-1.md`. Claim: `claims/pr-5265-split-shard-followup-round-1-phone-agent.md`. Gate: `gates/pr-5265-split-shard-followup-round-1.md`; receipt: `receipts/pr-5265-split-shard-followup-round-1.md` (PASS).
- PR: https://github.com/apache/solr/pull/5265 (SOLR-16398), branch `nick-boss-tech/solr:solr-16398-split-shard`.
- Head movement: `6c56a1823689af74ecef99caca12fa7f3f8990ad` (as opened) → `0495ffa2b00772189d2f92d83b32140f1b5e8b39` by two fast-forward commits, author Nick Shanin, no trailers: `7e0419e399` (mapping coverage), `0495ffa2b0` (changelog title). Remote head read back equal after the push and again after the gate.

## Findings addressed

1. **Request-body coverage.** New `SplitShardMessageMappingTest` (on the existing `MockV2APITest` harness, the same shape as `CreateShardAPITest`) sets every `SplitShardRequestBody` field at once and asserts each one in the captured overseer message under the key the v1 SPLITSHARD command reads: `ranges`, `split.key`, `numSubShards`, `splitFuzz`, `timing`, `splitByPrefix`, `followAliases`, `splitMethod`, `waitForFinalState`, `coreProperties` flattened as `property.foo`/`property.bar`, and `async` asserted as the submission's async id. Empty-body and null-body cases assert only `collection` and `shard` are sent. `SplitShardAPITest` gains two end-to-end legs through the generated client: a split with explicit uneven `ranges` asserting the sub-shards carry exactly those ranges in cluster state, and an async split asserting the response carries the request id and the sub-shards become active. The all-properties test passed as written, so no production mapping defect was demonstrated and `SplitShard.java` is unchanged, per the assignment's fix-only-on-proof rule.
2. **Changelog title.** `changelog/unreleased/SOLR-16398.yml` now reads "Migrate the v2 split-shard API to JAX-RS". Author remains Nick Shanin; no `nick:` line; the file parses as YAML.

## Files changed (3 files, 169 insertions, 1 deletion)

- `solr/core/src/test/org/apache/solr/handler/admin/api/SplitShardMessageMappingTest.java` (new, 123 lines)
- `solr/core/src/test/org/apache/solr/handler/admin/api/SplitShardAPITest.java` (+45)
- `changelog/unreleased/SOLR-16398.yml` (title line)

No production file changed.

## Proof

Exact-head gate at `0495ffa2b0` is PASS; per-step commands, counts, and logs are in the receipt. Summary: tidy clean, Error Prone compile rc 0, SplitShardAPITest 4/4, TestCollectionAPIs 2/2, SplitShardMessageMappingTest 3/3 (seed `16398C0DE`), `:solr:core:check -x test` rc 0, `git diff --check` clean, final head and tree verified.

## Remaining limits

- SOLR-17241 is untouched by this patch and remains separately tracked, as the assignment directs; nothing here fixes or verifies the non-hosting-node `wt` behavior, and the PR's proof stays scoped to the generated client's typed JSON parser.
- The live PR description still cites the original head `6c56a18236` and its original two tests in its Proof section. This assignment did not authorize a PR body edit, so the update is left for the owner.
- No GitHub comment, review, or JIRA write was made.

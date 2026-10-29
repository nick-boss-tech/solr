# Receipt: SOLR-18601 / PR 5253, round 5 remaining gate steps (vm2, Linux)

Closes the two steps left open after `receipt-round5` (board `4341a139cdc`), at head K `21804641d0eb`.

## Changelog YAML parse (K)

`changelog/unreleased/SOLR-18601-migrate-cleanup-on-failure.yml` parses with PyYAML. Keys: authors, links, title, type (`fixed`). Result: pass.

## Pre-fix proof on the real base (upstream `e59ce3c97d`)

Clean clone of apache/solr at `e59ce3c97d` (status clean). The PR's `TestInjection.java` and `MigrateRouteKeyTest.java` from K copied in. `MigrateRouteKeyTest`, seed 5253C0DE: 8 run, 5 failed, 3 passed.

- Failed (behavioural, the bug): `testRetryAfterFailedMigrate` (target left in BUFFERING, `expected:<ACTIVE> but was:<BUFFERING>`); `testFailedMultiShardMigratePreservesEarlierRange` (`Expected exception RemoteSolrException but no exception was thrown`).
- Failed (cascade from the same bug): `testRetryAfterMigrateAssertionFailure`, `testFailedMigratePreservesEarlierForwardedUpdates`, `testFailedMigrateRestoresExistingRule`. Each setup `MIGRATE` fails with `collection already exists: split_shard1_temp_shard1`. The temporary collection left by an earlier failed MIGRATE is held by alias `failedMigrate-blocker`, so the next MIGRATE in the shared cluster cannot create it.
- Passed as expected: `multipleShardMigrateTest`, `testMissingSplitKey`, `updateSucceedsAfterMigrateTargetIsDeleted`.

Caveat: the cascade means the three cascade failures are not independent evidence of the specific behaviour. The two behavioural failures are. Negative legs A, B and C remain as recorded.

Logs: `repair-I/prefix-upstream.log`, `repair-I/prefix-upstream-xml/`.

## Status

- Changelog parse: done.
- Pre-fix proof on the real base: done, 5 of 8 fail on upstream.
- Error Prone: the root check (`:check -x test`) compiled the tree at K; not separately confirmed that Error Prone ran as its own step.
- Verdict unchanged: NOT GREEN under the strict rule, with the neighbour failure accepted by the owner as pre-existing (confirmed on upstream `e59ce3c97d`, see `receipt-round5-base-correction`).

No GitHub, PR, or JIRA action taken.

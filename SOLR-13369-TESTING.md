# SOLR-13369 - TriLevelCompositeIdRoutingTest: flawed invariant, test-only fix (not run)

Nothing here was compiled or executed. Verify the guesses below first.

## JIRA context
`TriLevelCompositeIdRoutingTest` failed on a Jenkins seed with "routePrefix app9/2!user32 found in
multiple shards" (3 shards). The test was disabled with `@AwaitsFix` until someone could say whether
the test or `CompositeIdRouter` was wrong. Hossman and Shalin both suspected the `/bits` semantics.

## Finding: the router is right, the assertion is too strict
A route prefix fixes only the leading hash bits. For `app9/2!user32` that is 2 bits of `app9` plus
the default 8 bits of `user32`, so the prefix covers a hash range 2^22 wide
(`KeyParser.getRange`). A shard boundary can fall inside that range, and then docs under the same
prefix legitimately land on two adjacent shards. That is also why `getSearchSlicesSingle` uses range
overlap and can return more than one slice.

Checked by hand with a throwaway murmur3 computation (not in the tree): for `app9`/`user32` the
prefix range is [713031680, 717225983]; an equal 3-way split of the signed int space has a boundary
at 715827882, inside that range. That reproduces the reported shard2/shard3 split.

## Fix (test only)
The test now records every shard each route prefix was found in. After the usual "no duplicate
uniqueKeys / no missing docs" checks it asserts that each of those shards is one that
`router.getSearchSlicesSingle(prefix + "!", ...)` returns, i.e. the shards a `_route_` query for the
prefix would hit. `@AwaitsFix` is removed.

## What was guessed / verify first
- That `getSearchSlicesSingle(prefix + "!")` yields the same range as the add path for 3-level keys
  with and without `/bits` (it goes through `KeyParser` with a trailing `!`, giving three pieces).
- That the collection is `DEFAULT_COLLECTION` with the default composite router in `ShardRoutingTest`.
- Flake rate: the test should now pass for any seed; run it multiple times, including with 3 shards.
- This does not prove the router honors the bit masks; it only checks consistency with the router's
  own search range. A stricter check would compute the expected shard from `sliceHash` directly.
- Spotless/import ordering.

# solr-7504-submit

- Branch: origin/solr-7504-submit
- Head: e3fdc8eb58f2
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind)
- Scope: 2 commits. `CountFieldValuesUpdateProcessorFactory.java` (+68: `countAtomicUpdate`, `countOperand`), `FieldMutatingUpdateProcessorTest.java` (+121: four new tests and one helper), changelog `SOLR-7504-count-values-atomic-update.yml` (+8, type `fixed`)
- Verdict: Nearly
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

Owner-locked design (BAD_REQUEST rejection of non-`set` atomic operations): this review checks that the code implements it. It does not revisit the choice.

## Delta check against earlier disposition

- Handoff: owner-locked BAD_REQUEST design, verified in round 33 at this head. Head unchanged: `origin/solr-7504-submit` is at `e3fdc8eb58f2`, as listed.
- Locked design, as implemented: a `set` is counted across several operation maps, array and collection operands are counted by their elements, and any other operation on the counted field raises `BAD_REQUEST`. Verified in `countAtomicUpdate` and `countOperand` in `CountFieldValuesUpdateProcessorFactory.java`.

## Verified code facts

- Base behaviour for the same input: `SolrInputField.getValueCount()` returns 1 for any single non-collection value (`SolrInputField.java:139-144`). On base, `Map.of("set", List.of("aaa","bbb"))` is counted as 1, and `Map.of("add","aaa")` is replaced by the plain value 1. So base silently stores a count for an operation it cannot count. The branch changes that outcome, as the changelog says.
- `FieldMutatingUpdateProcessor.java:92-95` catches the mutator's `SolrException` and rethrows `BAD_REQUEST` with the message `Unable to mutate field '<name>': <original message>`. The test helper's field-name and `'add'` checks therefore hold.
- `countOperand`: null counts 0; a `Collection` counts its size; an array counts its length; anything else counts 1. The `setNull` test uses `HashMap` rather than `Map.of`, which correctly avoids the `Map.of` null-value exception.
- Multiple `set` operations: the last one decides (`setOperand` is overwritten in the loop), which matches the Javadoc and the test `testCountValuesAtomicUpdateMultipleMaps`.
- Entry check: the first value must be a `Map` and not a `SolrDocumentBase` for the atomic path to run, so child documents are not treated as operations.

## Findings (ranked)

1. **LOW, verified. Changelog title is one very long line.** The title runs to about 250 characters on one line. It is accurate, but it is harder to read than the other fragments. Owner's style call; no change proposed beyond shortening if the owner wants.

2. **LOW, verified (test coverage positive).** The four new tests check the codes and messages, not just the count. `testCountValuesAtomicUpdateUnsupportedOperations` covers `remove`, `inc`, `add-distinct`, `removeregex`, a mixed `set`+`add` map, and an empty map. `testCountValuesAtomicUpdateMultipleMaps` covers a rejected `add` among flattened operations and a plain value mixed with maps. `testCountValuesAtomicUpdateArrayOperand` covers `String[]`, `int[]`, and `Object[]`.

3. **Positive, verified by reading (not run).** The tests discriminate on base. Base returns 1 where the tests expect 2, 0, or 3, and base never raises the `BAD_REQUEST` that the tests expect.

4. **LOW, hypothesis.** The locked `BAD_REQUEST` is a 400 for requests that previously "succeeded" by overwriting the stored value with a count. That is the intended change. The changelog states it, so no document change is needed. Noted so the owner can confirm the release note is enough for existing clients.

## Owner calls (not decided here)

No decision needed in this review. The locked design is untouched. Two semantic choices are in the code but are not in the locked-design note; the owner may want to confirm them:

- `set` with a null operand counts as 0 (`countOperand`, first branch).
- Duplicates inside one collection operand are counted (`Collection.size()`), not de-duplicated.

## Not checked

- Not compiled, formatted (spotless), or run. Pass and fail on base and head is a hypothesis (finding 3).
- The processor's position in the update chain relative to `AtomicUpdateProcessorFactory` (ordering matters: the count must see the atomic maps). Not checked against a real chain config.
- Error Prone was not run.
- Nothing pushed or posted to GitHub or JIRA.

# SOLR-16673 handoff review, round 2 (vm5, 2026-10-19T04:14:30Z)

Heads: `solr-16673-uniquekey` at `fce77f45a3`; `solr-16673-rootfield` at `e9ce052946`. Round 1 is `reports/SOLR-16673-handoff-review-round-1.md`.

## Round 1 findings, resolved

1. Rootfield test did not compile (`assertEquals(1, numDocs)`): the rewritten test does not use that line.
2. Both regression tests built a Points uniqueKey that Solr refuses to load: uniqueKey test now uses the default string uniqueKey; rootfield test uses a trie long (`tlong`, `TrieLongField`) uniqueKey, which loads.
3. uniqueKey fix verified: the string-uniqueKey test fails on the base cast (`ClassCastException: Long cannot be cast to String`) and passes at head.
4. Nested sample indexing failure: root cause found. `DefaultSampleDocumentsLoader` uses `JsonLoader.buildDoc`, which stores `_childDocuments_` as a field named `_childDocuments_`, not as child documents, so the parent is indexed with no children. Fixed locally in the loader (`nestChildDocs`). Verified by a mutation: loader reverted, failure returns.
5. Reachability: confirmed with a trie long uniqueKey. With the base helper, `_root_` stays `string` and the schema will not load: `_root_ field must be defined using the exact same fieldType as the uniqueKey field (id) uses: tlong`. The Issue 3 mechanism is real for numeric keys that are not Points types. Helper and SchemaDesigner reverted: this failure returns.
6. `_root_` retype after an existing `_root_` is found: not changed in this round. Still a code-read concern, not verified. Recommended as a follow-up probe, not a gate blocker.

## Open

- Step 2 (Error Prone, `-Werror`) fails on the same two `DirectInvocationOnMock` warnings in `CoreReplicationAPITest.java` for both branches. Neither branch changes that file. Decision needed: see the gate files' Result r2 sections.
- vm1's rootfield claim names the superseded head `5fb792482f`. Decision needed: see the rootfield gate file.
- Finding 6 is unverified.

## Changes this round

- `solr-16673-uniquekey`: `fce77f45a3` rewrites `testNumericUniqueKeyDocIds` on a string uniqueKey.
- `solr-16673-rootfield`: `e9ce052946` adds the loader child-document fix, rewrites the test on a trie long uniqueKey, and widens the changelog title.

Logs: `/workspace/gates/logs/vm5/` (`16673-uniquekey-r2`, `16673-rootfield-r2`, `rf-proof-*`, `review-*`).

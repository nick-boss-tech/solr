# SOLR-16437 draft: part b report

Result: draft written at `pr-drafts/solrcloud/SOLR-16437.md`, naming head `aa2a6b8afb6f`. Length: 5,035 characters with link URLs (about 1,800 of them URLs), 3,218 without. The guide is about 3,500, so the draft is over only when the URLs are counted. Not trimmed. The Choice section is kept as a short question, and can be deleted (lines 27 to 31) without affecting anything else.

The draft's wrong-shard wording is correct. The adopted answer's wording is not (see finding 1).

## Draft checks

- Dashes: none. Process words: none. Old head references: none.
- Head references: all five blob links name `aa2a6b8afb6f1720c0a04b7869f331ab47bf554d`. The Proof names `aa2a6b8afb6f`.
- Base references: six links to `e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f`, each labeled as base.
- Section order matches `pr-formula.md`: header and Jira link, What happens today, What this change does, Proof, A choice to check, Limits, Changelog link, AI assistance footer. Each section opens with a bold one-line summary.
- Proof names `testAddReplicaPropRejectsUnknownReplica` and says one failure with the change reverted. It does not restate the class counts (25 tests, 1 skipped), as the adopted answer requires.

## Findings

1. FIX in earlier files, not in the draft. The adopted answer says that before this change a wrong-shard request "changed the replica in the other shard". That is wrong on base `e2cdb2d7e8ae`. `ReplicaMutator.java` lines 162 to 163 find the replica in any shard through `DocCollection.getReplica`. Then line 184 takes only the named shard's replicas. For a non-unique property, line 186 `replicas.get(replicaName)` returns null, which throws an NPE. `Overseer.java` lines 436 to 439 log that and skip it, and the caller already had success. For `preferredleader`, which is unique (`SliceMutator.java` lines 50 to 53), lines 187 to 194 remove the property from every replica in the named shard and set it on none. The draft's line 17 says exactly this. Correction owed to `material/solrcloud-round-1-answers.md` line 168 and `reports/solrcloud-round-1-p5.md` line 79.
2. NOTE, Proof coverage. The test asserts HTTP 400 only for the unknown replica (`CollectionsAPISolrJTest.java` line 1307). The unknown shard and collection cases (lines 1310 to 1321) assert only that an exception is thrown. The draft says so (line 25). Adding a stronger assertion would need a new gate.
3. NOTE, unverified by a run. "Unknown collection and shard still get HTTP 200" (draft line 7) rests on reading. The base `AddReplicaPropCmd` (`CollApiCmds.java` lines 331 to 350) has no existence check, and the Overseer throws in `ReplicaMutator` through `ClusterState.getCollection`. No run.
4. OWNER DECISION, not in the draft. The new check reads the cluster state cached on this node (`CollApiCmds.java` lines 348 to 349). A replica added moments earlier may not be visible yet, so a valid request could get a 400. `ZkStateReader` was not read to confirm. An optional Limits line: "The check reads the cluster state cached on this node. A replica added moments earlier may not be visible yet. This is not tested."
5. OWNER DECISION, length. The largest cut, if wanted, is the Overseer and distributed-mode citation sentences in What happens today (line 9), about 470 characters with URLs.
6. NOTE, wording. Line 37 says "I can open a follow-up ticket and PR ... on request". The other drafts use the form "A follow-up ticket and PR can be opened on request." Owner to pick the voice. Line 38 "were not checked" is process wording in public text. Consider "are not covered by the new test."
7. NOTE, missing files. `g16437-gate.log` is not on disk. `research/jira-context/SOLR-16437.json` is in the main checkout, not the worktree. Its description says "(and other APIs)", which supports the Limits line.
8. NOTE, changelog at head. The author entry is `- name: Nick Shanin`, with no `nick:` line. The draft links the file at the head.

## Owner decisions

- Keep, or delete, the Choice section (lines 27 to 31).
- Whether to add the cached-state Limits line (finding 4).
- Whether to trim the Overseer citation sentences (finding 5).
- The voice of the follow-up offer (finding 6).
- Correct the wrong-shard wording in the earlier answers and the p5 report (finding 1). This round does not edit those files.

## Not checked

- No tests, builds, or gate log read. The proof counts come only from the receipt.
- The HTTP 400 for the shard and collection cases is inferred from the same `SolrException(BAD_REQUEST)` path. It is not tested.
- Per-replica state collections were not read.
- The race in finding 4 was not read.
- Line anchors were checked against `git show` at each SHA. The GitHub blob URLs were not opened.
- The "verified 2026-10-10" date comes from the receipt's "first gate finished 2026-10-10" line.
- Commit-body process words are part a's check.

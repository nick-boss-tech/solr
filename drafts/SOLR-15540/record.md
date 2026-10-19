# Skip record: SOLR-15540

- Ticket: SOLR-15540, changes in internal usage of _root_ field cause failures to deleteById or duplicate docs when updating after upgrading from Solr7 to Solr8. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-07-14, updated 2021-09-28, no fix versions, linked to SOLR-5211 (Closed).
- Verdict: keep-open.
- Draft branch: solr-15540-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at this head (detached worktree).
- Source: remediation redo of batch 03, item 3, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) is background only.

## Ticket reading

On upgrade from Solr 7.7 to 8.8 with the same index, a document update with the same id leaves the Solr 7 copy in place (two documents with that id), and deleteById misses it. The schema defines `_root_` as an indexed, unstored string, and `id` as a string uniqueKey. The reporter reads DirectUpdateHandler2 in 7.x as using the id term for the update and asks why 8.x uses the root term.

## Code reading

- solr/core/src/java/org/apache/solr/schema/IndexSchema.java:2194-2199: `isUsableForChildDocs()` is true when `_root_` exists and has the same field type name as the uniqueKey. With the reporter's schema (both `string`), it is true.
- solr/core/src/java/org/apache/solr/update/DirectUpdateHandler2.java:1194-1196: `getIdTerm` returns a term on `_root_` when `isUsableForChildDocs()` is true, and on the id field otherwise. Both the delete path (:614) and the update path (:1176) use it.
- solr/core/src/java/org/apache/solr/update/DirectUpdateHandler2.java:1176-1180: `updateDocuments(updateTerm, nestedDocs)` replaces by `idTerm` unless an update term is given. So a Solr 7 document with no `_root_` value, which the 8.x schema now indexes under `_root_`, is not matched by the term and is not replaced.
- solr/core/src/java/org/apache/solr/update/AddUpdateCommand.java:98-99 and :237: `_root_` is populated for new documents when the schema is usable for child docs. The Solr 7 documents were written without that value, so there is nothing for the root term to match.
- The change that made `_root_` always populated (SOLR-5211, a346ba0d3c, 2018-11-25) is an ancestor of the pinned head. It does not backfill older documents, so the mismatch stays for indexes written before it. The reporter's index is pre-8 and the reporter's own comment says re-indexing is the workaround (hossman, 2021-07-29).

## History

`git log -S` on the root-term selection gave 4cb3ad4a1c (SOLR-14923, 2021-01-07), which made `isUsableForChildDocs()` the switch for `getIdTerm`. The ticket is about 8.8, which predates or sits near that change; the 8.8 code path was not run.

## Reproduction attempts

Not attempted in this pass. A reproduction needs an index written with the 7.x behavior (no `_root_` on non-nested documents), then an update under the 8.x schema. Building that index needs an older build or a hand-written index file, which is not in the repository. The code path above predicts the duplicate, but this record does not show it by a run.

## Gap analysis

The mechanism is in the code: updates and deletes by id use `_root_` once the schema is child-doc usable, and older documents do not carry it. The ticket asks for a design decision, not a bug report: the commenters agree the fix needs either a re-index or a custom PostingsFormat fallback on the id field (dsmiley, 2021-07-29), and the enumeration of terms for that fallback is open. Nothing on main decides which option is right.

## Verdict justification

keep-open: the attempts are the code path reads above at the pinned head and the Jira read. The missing piece is a run that reproduces the duplicate on an index written without `_root_`, and a maintainer decision on the fix (re-index guidance, or a fallback lookup on the id field). No closing verdict is supported: the fix is not on main, and the behavior is not obsolete.

Missing for a closing or PR verdict: (1) the reproduction on a 7.x-style index, with command and output; (2) a design decision between documented re-index and a code fallback.

## Limits

This record does not reproduce the duplicate. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.

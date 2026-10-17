# Draft fidelity review: Configsets, Query parsing and Schema and analysis drafts

Assignment: `assignments/pool-draft-fidelity-configsets-query-schema.md`. Claim: `claims/pool-draft-fidelity-configsets-query-schema.md`. Lead: the windows review agent. Part reports, with the exact replacement text for each item:

- `reports/draft-fidelity-configsets-query-schema-s1.md`: configsets SOLR-15478, 17363, 6960, 7267.
- `reports/draft-fidelity-configsets-query-schema-s2.md`: query-parsing SOLR-10897, 11391, 11761, 12212, 12532.
- `reports/draft-fidelity-configsets-query-schema-s3.md`: query-parsing SOLR-12608, 12871, 15615, 16267, 17280.
- `reports/draft-fidelity-configsets-query-schema-s4.md`: query-parsing SOLR-17311, 17796, 4824, 6014, 874.
- `reports/draft-fidelity-configsets-query-schema-s5.md`: query-parsing SOLR-8977, 9048, 9149; schema SOLR-14199, 15357.
- `reports/draft-fidelity-configsets-query-schema-s6.md`: schema SOLR-15358, 15945, 16977, 17047, 18134, 9349.

No draft was edited. Read-only throughout. No build, test, gate or test-queue run. No PR, comment, review, submit-branch, PR-description or Jira write.

## Verdicts

Thirty drafts: twenty-eight are DRIFT and two are CONSISTENT. Every named head matched the fork's live tip in `ls-remote` at the time of the check.

| Draft | Verdict | Part report |
|---|---|---|
| SOLR-15478 | DRIFT (1 item) | s1 |
| SOLR-17363 | DRIFT (2 items) | s1 |
| SOLR-6960 | DRIFT (3 items) | s1 |
| SOLR-7267 | DRIFT (2 items) | s1 |
| SOLR-10897 | DRIFT (1 item) | s2 |
| SOLR-11391 | DRIFT (1 item) | s2 |
| SOLR-11761 | DRIFT (3 items) | s2 |
| SOLR-12212 | DRIFT (3 items) | s2 |
| SOLR-12532 | DRIFT (1 item) | s2 |
| SOLR-12608 | DRIFT (5 items) | s3 |
| SOLR-12871 | DRIFT (2 items) | s3 |
| SOLR-15615 | DRIFT (1 item) | s3 |
| SOLR-16267 | DRIFT (2 items) | s3 |
| SOLR-17280 | DRIFT (1 item) | s3 |
| SOLR-17311 | CONSISTENT | s4 |
| SOLR-17796 | DRIFT (2 items) | s4 |
| SOLR-4824 | CONSISTENT (held by round 1, FIX 1 and a parameter-name call) | s4 |
| SOLR-6014 | DRIFT (4 items) | s4 |
| SOLR-874 | DRIFT (4 items) | s4 |
| SOLR-8977 | DRIFT (3 items) | s5 |
| SOLR-9048 | DRIFT (2 items) | s5 |
| SOLR-9149 | DRIFT (1 item) | s5 |
| SOLR-14199 | DRIFT (3 items) | s5 |
| SOLR-15357 | DRIFT (4 items) | s5 |
| SOLR-15358 | DRIFT (2 items) | s6 |
| SOLR-15945 | DRIFT (2 items) | s6 |
| SOLR-16977 | DRIFT (1 item) | s6 |
| SOLR-17047 | DRIFT (3 items) | s6 |
| SOLR-18134 | DRIFT (5 items) | s6 |
| SOLR-9349 | DRIFT (2 items) | s6 |

## What the drift is

Most items are one of four kinds, and the part reports give the exact text for each.

- **Citations.** Pre-change symptom code is linked at the head SHA, or at upstream main, where the citation rule needs the merge-base, with the text saying so. This is the most common item, across most drafts.
- **Changelog titles.** The branch changelog title is wider or narrower than the change, or it omits a behavior. These are branch-side title fixes. A title fix moves the head, so each affected draft needs its links re-pointed and a new head after the owner edits the branch.
- **Verification and placeholders.** Some Proof lines lack a verification date. Some drafts carry an OWED or CONFIRM placeholder, or a "TO FILL" base count, that must come out before posting.
- **Facts the code or receipt does not support.** Examples: SOLR-12608's Limits and SOLR-15357's Proof wording; SOLR-18134's Lucene claim (checked on 9.12 only); SOLR-6014's CI run, which is a child of the head, not the head itself.

## Holds and open items for the owner or the main side

1. **SOLR-4824.** Held by round 1 (FIX 1 and the parameter-name call). The draft reads consistent against the current text, but the held call stands until Nick rules.
2. **SOLR-17311.** Consistent against the live text. The live-head NPE line is not on disk, so that one line is not verified.
3. **Branch-side changes.** Several changelog-title fixes are needed before the drafts can open. Each one moves a head, so the links have to be re-pointed after the edit.
4. **SOLR-15615.** The fallback authorization path was not traced end to end.
5. **SOLR-16267.** The receipt's head `8f4b0c6` differs from the queue's head SHA `67ffcb2b63f`. Left for the owner to reconcile.
6. **SOLR-12608.** Five items, including the Limits. The branch changelog title change moves the head.
7. **SOLR-14199 and SOLR-15357.** The schema changelog and internal markers need removing before the draft is public. The 14199 ledger file is not on disk.
8. **Counts that are receipt-only.** The counts in the Proof lines were checked against the receipts. The JUnit XML and gate logs are not on disk. One case is open: SOLR-8977's source count (`TestSolrQueryParser` 37 against 36 source methods) is unconfirmed.
9. **Lucene.** Lucene 9.x and 10.x claims were not rechecked for the query-parsing set (no Lucene recheck was requested in the round, but a roll-up point asked for one).
10. **Live Jira.** No live Jira text was read. Spot checks used the local packets only.

## Not done

- No build, test, Gradle, gate or test-queue run.
- Receipt-named gate logs, premise logs and JUnit XML are not on disk. Counts are receipt-only, as recorded.
- No live GitHub or Jira state was checked, apart from read-only spot calls that the part reports name.
- Several parts (query-parsing q2, q4, q5, q6, q8; schema s3, s4) were searched for the ticket numbers, not read in full, because they name none of these keys.

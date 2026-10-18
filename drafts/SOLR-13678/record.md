# Skip record: SOLR-13678

- Ticket: SOLR-13678, ZkStateReader.removeCollectionPropsWatcher can deadlock with concurrent zkCallback thread . JIRA status at check: Open, checked 2026-10-18.
- Verdict: close-fixed.
- Draft branch: solr-13678-draft-close.
- Pinned head: fork origin/main 498a09aea9.
- Checked by: vm5, 2026-10-18T21:32:17Z.

## Ticket reading

The ticket is Open with no resolution at check. The summary is above; the report row is the evidence below.

## Code reading

The deadlock is in `removeCollectionPropsWatcher`. Commit a052fb5436 (2019) only hardened `CollectionPropsTest.testReadWriteCached` and did not fix the lock ordering. The fix is SOLR-17720, commit 67a642fe02 (April 2025). It moves the lock on the collection outside `collectionPropsObservers.compute()`, which removes the lock-order inversion this ticket describes (`solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/CollectionPropertiesZkStateReader.java`).

I checked the code change, not the thread dump attached to this ticket.

Proposed resolution: Fixed (link SOLR-17720)

## History

- `67a642fe02`: ancestor of origin/main.

## Reproduction attempts

Not attempted. This is a fix-commit verdict: the commit that fixes the reported problem is on main, and its changed code is still present at the pinned head. A reproduction is not required for close-fixed.

## Gap analysis

Fix commit named in the draft; the draft text is the evidence.

## Verdict justification

close-fixed: the fixing commit (or, for a removed feature, the removal commit and an empty search) is named above, each cited commit is an ancestor of 498a09aea9, and the changed code or the empty search is recorded at the pinned head. Rubric minimum for close-fixed is met.

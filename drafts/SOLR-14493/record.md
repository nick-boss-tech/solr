# Skip record: SOLR-14493

- Ticket: SOLR-14493, "NullPointerException on retrieving the status of the collection with implicit routing". JIRA status Open, resolution none, fix versions none, created 2020-05-16, updated 2020-05-16. Checked 2026-10-19 UTC.
- Verdict: close-fixed (changed from keep-open; this redo supersedes the recorded verdict).
- Draft branch: solr-14493-draft-close (the earlier keep-open branch solr-14493-draft-open is not updated).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14493-vm2.md).

## Ticket reading

Creating an implicitly routed collection and then calling COLLECTIONSTATUS on it throws a NullPointerException in ColStatus.getColStatus (the reporter's stack, ColStatus.java:152 in their release). The reporter gives a reproduction with SolrJ and the create-then-status sequence. The report has no comments.

## Code reading

- The failing computation is the range of a slice. Implicit-router slices have no range, so `s.getRange().toString()` throws.
- At the pin, solr/core/src/java/org/apache/solr/handler/admin/ColStatus.java:163-165 guards the call: `if (s.getRange() != null) { sliceMap.add("range", s.getRange().toString()); }`. The guard is in place at origin/main 498a09aea97.
- The guard was added by commit 83770854a35 (2021-08-03, "SOLR-14506: Fix NPE in COLSTATUS for collections using implicit routing"). That commit is an ancestor of the pinned head (git merge-base --is-ancestor). Its diff replaces the unguarded call with the guard and also changes the collections list it reads (getCollectionStates). It adds 14 lines to solr/solrj-jetty or core test CollectionsAPISolrJTest.java, which exercises collection status for implicit collections.

Commands at the pinned head:
- git show origin/main:solr/core/src/java/org/apache/solr/handler/admin/ColStatus.java | sed -n 163,165p: the guard.
- git log --format='%h %ad %s' --date=short -S 'if (s.getRange() != null)' origin/main -- solr/core/src/java/org/apache/solr/handler/admin/ColStatus.java (bounded, path-limited): 83770854a35.
- git log --oneline --grep='SOLR-14493' origin/main: no output.

## History

- 83770854a35 (2021-08-03): the fix for the implicit-routing NPE in COLLECTIONSTATUS, under the ticket SOLR-14506. Ancestor of the pinned head. Its commit message does not name SOLR-14493.

## Reproduction attempts

Not run in this pass. The commit's own test in CollectionsAPISolrJTest covers collection status for implicit collections; a run of that test class was not made here.

## Gap analysis

The reported NPE is the unguarded range call for implicit slices, and the guard for it is on main. The link between SOLR-14493 and commit 83770854a35 rests on the shared symptom and code path, not on a ticket reference: the commit names SOLR-14506, which may be a duplicate of this ticket, and the report's own line numbers come from an earlier release. Confirming that the guard fixes the reporter's exact reproduction needs a run of the reporter's sequence on the pin.

## Verdict justification

close-fixed meets the rubric in its minimum form: a commit on main fixes the reported problem (83770854a35), the fixing commit is an ancestor of the pinned head, and the changed code is still present at the head (ColStatus.java:163-165). The requirement not fully met is that the commit does not name this ticket, so the match is by symptom and code path, and the reporter's sequence was not run on the pin. Given that, the verdict should be read as close-fixed with the linkage stated as an inference.

## Limits

This record does not run the reporter's reproduction. It does not confirm that SOLR-14506 and SOLR-14493 are duplicates. It does not run CollectionsAPISolrJTest.

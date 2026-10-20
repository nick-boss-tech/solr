# Skip record: SOLR-10959

- Ticket: SOLR-10959, o.a.s.c.MapSerializable missing in solr-core maven artifact. JIRA read in this pass: Open, resolution none, fix versions none, 0 comments; updated 2019-06-08.
- Verdict: close-obsolete (unchanged status; this record replaces the earlier record on this branch, whose cross-audit reading named missing items J, C, G and L).
- Draft branch: solr-10959-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: after updating a test dependency from solr-core 5.5.4 to 6.6.0, compilation fails with ClassNotFoundException for org.apache.solr.common.MapSerializable, which was missing from the solr-core artifact. The report is about a published artifact of Solr 6.6.0.

## Code reading

- The named class is absent from the whole solr tree at the pin: git grep -c MapSerializable over solr returns 0 files.
- The removal: commit f9f5abf21e (SOLR-17600 p4, "Delete MapSerializable", 2026-06-21) deleted solr/solrj/src/java/org/apache/solr/common/MapSerializable.java (33 lines). Earlier steps p1 to p3 (ddb1923d15, 22c37b3c7f, 9cc72f1638) migrated its users. Ancestry checked: f9f5abf21e is an ancestor of the pin.

So the symbol the ticket names does not exist at the pin, and the failure it reports (a missing MapSerializable in the solr-core artifact) cannot be reproduced against a build of the pin.

## History

- SOLR-9502 (1e18c12c19) made the response writers support MapSerializable; the artifact question in the ticket dates from the 6.6.0 release.
- No commit at the pin names SOLR-10959 (git log --grep at the pin: none).

## Reproduction attempts

No run was attempted. For close-obsolete the rubric's check is the removed code and the whole-tree grep, which are done above.

## Gap analysis

The ticket asks why a class is missing from a released artifact. The class is now removed from the source, so any user still referring to it would fail at compile time for a different reason (the class is gone), which is outside this ticket's question. The packaging question for 6.6.0 is historical and not in the pin.

## Verdict justification

close-obsolete: the named class is absent from the whole tree at the pin, and the commit that removed it (f9f5abf21e, SOLR-17600) is an ancestor of the pin. The rubric's close-obsolete check (removed code, empty whole-tree grep) is met.

## Limits

This record does not check the published 6.6.0 artifact contents, does not run a build, and does not post anything to JIRA or GitHub.

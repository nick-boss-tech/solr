# Skip record: SOLR-14601

- Ticket: SOLR-14601, "Using [child] and [subquery] DocTransformer / FieldList". JIRA status Open, resolution none, fix versions none, created 2020-06-27, updated 2020-07-15. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14601-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14601-vm2.md).

## Ticket reading

With fl=*,[child],customer:[subquery] (or the reverse order), and luceneMatchVersion 8.0 or later, the request fails with "[subquery] name customer is duplicated". The reporter traces it to the child transformer, which copies the request's fl into the child return fields, so a [subquery] entry is applied again inside the children. The reporter quotes the 8.5.2 and master code, where a version-dependent branch chooses the child fl. Five comments (2020-06-27 to 2020-07-15).

## Code reading

- solr/core/src/java/org/apache/solr/response/transform/ChildDocTransformerFactory.java:136-141 (origin/main 498a09aea97): the child return fields come from the request's fl parameter when it is set, and otherwise from new SolrReturnFields(req), which includes the whole request. The version-dependent branch the reporter quotes is not present at the pin.
- solr/core/src/java/org/apache/solr/response/transform/SubQueryAugmenterFactory.java:126-139: the conflict map is kept in the transformer context under the factory's class name; a second [subquery] with the same name raises "[subquery] name <name> is duplicated" (line 135).
- Whether a [subquery] entry from the request's fl is carried into the child return fields at the pin, and so applied twice with the same name, was not confirmed by a run.

Commands at the pinned head:
- git grep -n 'conflictMap\|is duplicated' origin/main -- solr/core/src/java: SubQueryAugmenterFactory.java:126-139.
- git grep -n 'childReturnFields' origin/main -- solr/core/src/java/org/apache/solr/response/transform/ChildDocTransformerFactory.java: lines 136-139.
- git log --oneline --grep='SOLR-14601' origin/main: no output.

## History

- Commits on origin/main naming SOLR-14601: none.

## Reproduction attempts

Not attempted. The check is a query with fl=*,[child],customer:[subquery] on the pin, with a child document and a subquery name, which needs a test class with a parent-child index. Not set up in this pass.

## Gap analysis

The conflict check and the child fl default are located. Whether the request's [subquery] is applied inside the children at the pin, which would produce the duplicate, is the step to confirm.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-fixed does not hold: no commit names the ticket (bounded --grep, no output). close-obsolete does not hold: the duplicate check and the child fl default are still present. close-cannot-reproduce does not hold: no reproduction was made. The missing piece is a reproduction of the query shape on the pin.

## Limits

This record does not reproduce the duplicate error. It does not confirm that the request's [subquery] reaches the child return fields at the pin.

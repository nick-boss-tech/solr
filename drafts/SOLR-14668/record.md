# Skip record: SOLR-14668

- Ticket: SOLR-14668, "CompositeID change provokes duplicates". JIRA status Open, resolution none, fix versions none, created 2020-07-19, updated 2020-08-19. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14668-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14668-vm2.md).

## Ticket reading

When the composite part of a document ID (for example, a manufacturer or country prefix) changes, the new version of the document is routed to a different shard, and the old version stays on its original shard, so the same document exists twice. The ticket asks for a way to clean up the obsolete copies. Four comments (2020-07-19 to 2020-07-23): maintainers note that Solr cannot know the old ID, and suggest a copy field with an analysis chain and a delete by query or by ID; the reporter says the stored composite part cannot be used to find the old record.

## Code reading

- solr/solrj/src/java/org/apache/solr/common/cloud/CompositeIdRouter.java:80 (origin/main 498a09aea97): CompositeIdRouter maps the prefix of a composite ID to a shard hash and the rest of the ID to a document hash. A changed prefix therefore changes the target shard, and the old copy is not removed by an update to the new ID. The routing behaviour is the mechanism of the report.
- `git grep -n 'SOLR-14668' origin/main`: no output. No commit names the ticket, and no cleanup mechanism for stale copies was found in the router.

Commands at the pinned head:
- git grep -n 'class CompositeIdRouter' origin/main -- solr/solrj/src/java: CompositeIdRouter.java:80.
- git grep -n 'SOLR-14668' origin/main: no output.

## History

- Commits on origin/main naming SOLR-14668: none.

## Reproduction attempts

Not attempted. The behaviour follows from the routing design. A reproduction would show the duplicate with two updates that change the composite prefix, which is a multi-shard test of a documented routing property rather than a failing run.

## Gap analysis

The duplicate is the expected result of the routing design when an ID's composite prefix changes: the router sends the new document to a different shard and nothing removes the old copy. The ticket is a feature request for a cleanup mechanism, and whether Solr should provide one, or the documentation should describe the copy-field and delete-by-query approach the comments suggest, is a design decision for the maintainers.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-answered would need the reference guide to answer the question explicitly; it does not describe a cleanup for changed composite IDs, and the comment's approach is not documented as a supported answer. close-obsolete and close-fixed do not hold. The missing piece is a maintainer decision on whether the cleanup is in scope, or on documenting the copy-field and delete-by-query approach.

## Limits

This record does not reproduce the duplicate. It does not evaluate the copy-field approach. It does not check the documentation for a stated answer beyond the router code.

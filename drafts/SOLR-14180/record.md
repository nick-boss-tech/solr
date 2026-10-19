# Skip record: SOLR-14180

- Ticket: SOLR-14180, "RequestHandlerBase.java:227 assertion failing: not all 'Tragic' exceptions have 500 status, assert masks real failure". JIRA status Open, resolution none, fix versions none, created 2020-01-08, updated 2020-01-21. Links SOLR-14179 (Open) and SOLR-12477 (Closed, the regression). Checked 2026-10-19 UTC.
- Verdict: close-obsolete (changed from keep-open; this redo supersedes the recorded verdict).
- Draft branch: solr-14180-draft-close (the earlier keep-open branch solr-14180-draft-open is not updated).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14180-vm2.md).

## Ticket reading

The 2018 change SOLR-12477 added a check to RequestHandlerBase: when the index writer has a tragic exception, an error that is a SolrException must have status 500, enforced by an assert. Test failures showed the assert firing on a different error, which hid the real failure from clients. The ticket asks for the assert not to mask the real error. A comment (hossman, 2020-01-08) links an example log from SOLR-14179.

## Code reading

- solr/core/src/java/org/apache/solr/handler/RequestHandlerBase.java:321-341 (origin/main 498a09aea97): processReceivedException handles a tragic exception by giving up leadership and rolling the index writer when the node is ZooKeeper aware, and then returns `SolrException.wrapLuceneTragicExceptionIfNecessary(e)`. The code has no `assert ((SolrException) e).code() == 500` at the pin.
- `git grep -n 'code() == 500' origin/main -- solr/core/src/java`: no output. The assertion that masked the real error is not in the code.
- The assertion was removed by commit 8efed055589 (2022-09-13, "SOLR-16347: Allow JAX-RS v2 API definitions (#975)"). That commit is an ancestor of the pinned head (git merge-base --is-ancestor). Its diff converts several fields to public and removes the assertion while moving the tragic-exception handling; its message does not mention the ticket or the assertion.

Commands at the pinned head:
- git show origin/main:solr/core/src/java/org/apache/solr/handler/RequestHandlerBase.java | sed -n 318,346p: the current handling without the assert.
- git grep -n 'code() == 500' origin/main -- solr/core/src/java: no output.
- git log --format='%h %ad %s' --date=short -S 'assert ((SolrException) e).code() == 500' origin/main -- solr/core/src/java/org/apache/solr/handler/RequestHandlerBase.java (bounded, path-limited): 8efed055589 (removal) and 8d28bbc905a (SOLR-12477, the addition).

## History

- 8d28bbc905a (2018-07-30): SOLR-12477, the change that added the assertion. The ticket's regression reference.
- 8efed055589 (2022-09-13): SOLR-16347, a JAX-RS change that removed the assertion as part of moving the tragic-exception handling. It does not name SOLR-14180.
- Commits on origin/main naming SOLR-14180: none.

## Reproduction attempts

Not applicable. The code the ticket is about is absent at the pin, so a reproduction would test code that no longer exists.

## Gap analysis

The assertion that masked real failures is removed, and a search of main finds no remaining use. Whether the replacement handling reports the real error in every tragic-exception case was not tested. The removal was not made for this ticket, and the commit does not name it, so the link is by the removed code, not by intent.

## Verdict justification

close-obsolete meets the rubric: the code the ticket is about (the 500-status assertion in RequestHandlerBase) has been removed (8efed055589, an ancestor of the pinned head), and a search of main finds no remaining use (the empty grep above). The minimum evidence for close-obsolete is the grep command and its empty result at the pinned head, which is recorded here. The requirement that is not fully met is that the removal was incidental; the verdict rests on the absence of the code, not on a deliberate fix.

## Limits

This record does not test tragic-exception handling. It does not confirm that SOLR-14179 is also resolved. It does not check the SOLR-12477 regression discussion beyond the ticket text.

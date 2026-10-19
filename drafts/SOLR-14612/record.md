# Skip record: SOLR-14612

- Ticket: SOLR-14612, "Cannot register custom plugins in instances with hostname starting with 'solr'". JIRA status Open, resolution none, fix versions none, created 2020-07-01, updated 2020-07-15. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14612-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14612-vm2.md).

## Ticket reading

Custom plugin registration through the package API fails on a host whose name starts with "solr". The reporter says that PackageAPI.java (at lucene-solr commit 6eb7bc3b, around line 418) replaces "/solr" with "/api" in the URL, so "http://solrexample.domain/solr" becomes "http://apiexample.domain/api". The report names no test class and has no comments.

## Code reading

- solr/core/src/java/org/apache/solr/pkg/PackageAPI.java (origin/main 498a09aea97): the class is documented as the public endpoints under /api/cluster/package (line 63). A bounded search for the /solr-to-/api replacement in the package code (`git grep -n 'replaceFirst\|replace("/solr\|"/solr'` over solr/core/src/java/org/apache/solr/pkg and handler/admin) found no such replacement.
- The reporter's line 418 is not in this file at the pin. So the exact code the report names is absent or moved, and the path that builds the plugin URL on the pin was not traced.

Commands at the pinned head:
- git grep -n 'replaceFirst\|replace("/solr\|"/solr' origin/main -- solr/core/src/java/org/apache/solr/pkg solr/core/src/java/org/apache/solr/handler/admin: no match for the rewrite.
- git log --oneline --grep='SOLR-14612' origin/main: no output.

## History

- Commits on origin/main naming SOLR-14612: none.
- The removal or move of the replacement code was not located within the bounded search.

## Reproduction attempts

Not attempted. A reproduction needs a server whose hostname starts with "solr" and a package registration through the API. That needs a running node, which was not set up in this pass.

## Gap analysis

The replacement the report names is not found at the pin, but the URL-building path that would replace it was not traced, so whether a host name starting with "solr" still breaks plugin registration is not established.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-obsolete does not hold: the named line is not in the file at the pin, but the removal was not located and the URL path was not traced. close-fixed does not hold: no commit names the ticket. close-cannot-reproduce does not hold: no reproduction was made. The missing pieces are (1) the URL-building path for package plugin registration at the pin, and (2) a run with a host name starting with "solr".

## Limits

This record does not reproduce the failure. It does not locate the removal of the cited replacement. It does not test a host name starting with "solr".

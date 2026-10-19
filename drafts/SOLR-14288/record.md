# Skip record: SOLR-14288

- Ticket: SOLR-14288, "Search on parent-child by specific key - results returns more than the required document after some attempt". JIRA status Open, resolution none, fix versions none, created 2020-02-27, updated 2020-05-27. Checked 2026-10-19 UTC.
- Verdict: close-answered (changed from keep-open; this redo supersedes the recorded verdict).
- Draft branch: solr-14288-draft-close (the earlier keep-open branch solr-14288-draft-open is not updated).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14288-vm2.md).

## Ticket reading

A new user with nested (parent and child) documents indexed in bulk asks for the parent with a given key, and gets the parent and all of its children, plus other parents' children from the same search, instead of only the requested parent and its matching children. The report is a usage question about the parent-child results, with no comments.

## Code reading

- solr/solr-ref-guide/modules/query-guide/pages/document-transformers.adoc:137 (origin/main 498a09aea97): the child transformer example `q=book_title:Solr&fl=id,[child childFilter=doc_type:chapter limit=100]`.
- The same page, lines 142-149: the `childFilter` option. Its entry reads "A query to filter which child documents should be included. This can be particularly useful when you have multiple levels of hierarchical documents." The default is "all children", which explains the reporter's extra children.
- The block join parser page (solr/solr-ref-guide/modules/query-guide/pages/block-join-query-parser.adoc) is also in the guide and covers the which-parent filter; it was not read in full for this pass.

Commands at the pinned head:
- git grep -n -i 'childFilter' origin/main -- solr/solr-ref-guide/modules/query-guide/pages/: document-transformers.adoc:137, 142 and related lines.
- git log --oneline --grep='SOLR-14288' origin/main: no output.

## History

- Commits on origin/main naming SOLR-14288: none.

## Reproduction attempts

Not applicable. The question is answered by the documented option, and the check is the text at the pinned head.

## Gap analysis

The documented answer is the childFilter option on the child transformer, which limits the children returned to those matching a query, with all children as the default. Whether the reporter's exact query shape needs the which-parent filter as well is not checked against the block join page here.

## Verdict justification

close-answered meets the rubric: the reference guide answers the support question (document-transformers.adoc:142-149, the childFilter option), and the link and sentence are recorded above. The rubric asks for a link to the page and the sentence that answers the question, which this record gives. The part not verified is the full query shape of the reporter, which the block join page would cover.

## Limits

This record does not test the reporter's query. It does not read the block join page in full. It does not confirm the reporter's expected output.

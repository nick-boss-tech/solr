# Skip record: SOLR-16020

- Ticket: SOLR-16020, "StringIndexOutOfBoundsException in BaseFragmentsBuilder when using the Highlighter". JIRA status Open, resolution none, fix versions none, created 2022-02-18, updated 2022-05-12. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-16020-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-16020-vm2.md).

## Ticket reading

The reporter sees sporadic StringIndexOutOfBoundsException (every few days) from the highlighter on Solr 7.7.3, which becomes HTTP 500 responses. The stack trace ends in org.apache.lucene.search.vectorhighlight.BaseFragmentsBuilder.makeFragment (a substring call with begin 66, end 43, length 201). The report contains no query, no field definition, no document, and no comments (0 comments in JIRA). The ticket names no test class.

## Code reading

- The failing method is in Lucene (org.apache.lucene.search.vectorhighlight.BaseFragmentsBuilder). It is not in this repository: git ls-tree -r origin/main finds no BaseFragmentsBuilder.java. The Solr code that calls into it is solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java:539-541, which creates a FastVectorHighlighter when the field uses term vectors and the highlighter option useFastVectorHighlighter is set.
- The stack trace's line numbers (BaseFragmentsBuilder.java:180, DefaultSolrHighlighter.java:520) are from Solr 7.7.3. They do not match the pinned head, so the frames cannot be matched line-for-line to this tree.
- The Lucene version used by this tree is set in gradle/libs.versions.toml. This pass did not read the Lucene source for that version, so it is not known whether the substring logic has changed since 7.7.3.

Commands at the pinned head:
- git ls-tree -r --name-only origin/main | grep BaseFragmentsBuilder.java: no output.
- git grep -n 'FastVectorHighlighter\|getBestFragments' origin/main -- solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java: import at line 58, use at 539-541.
- git grep -n -i 'StringIndexOutOfBounds\|BaseFragmentsBuilder' origin/main -- solr/core/src/test lucene/highlighter/src/test: no output.

## History

- Commits on origin/main naming SOLR-16020: none.
- Lucene-side history: not checked in this pass.

## Reproduction attempts

Not attempted. The input that produced the exception is not in the report. A reproduction needs the stored field value and the term-vector offsets of the failing document, and the query. Without them, a test would have to guess an input, and a guessed input would not show whether the reporter's case is covered.

## Gap analysis

The exception is a substring with begin greater than end, computed from term-vector offsets against the stored field text. That means the offsets and the text did not agree for one document. The likely cause is a mismatch between the indexed term vectors and the stored value, which can come from an analysis or update path, or from the Lucene fragment code. The report does not show which. Solr's own part is the call at DefaultSolrHighlighter.java:539-541; the failing computation is in Lucene.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempt and the missing piece are listed. The missing piece is the failing document and query, or a reproduction on the Lucene version this tree uses. A close verdict needs a fix commit or a reproduction showing the case cannot happen; a pr-needed verdict would have to change Lucene, which is not in this repository.

## Limits

This record does not reproduce the exception. It does not read the Lucene source for the version this tree uses. It does not establish whether a later Lucene release fixed the substring logic.

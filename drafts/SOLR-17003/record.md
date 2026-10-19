# Skip record: SOLR-17003

- Ticket: SOLR-17003, "UnifiedHighlighter breaks with DateRangeField". JIRA status Open, resolution none, created 2023-09-28, last updated 2023-10-21, checked 2026-10-19 UTC. Link: SOLR-16885 (Open).
- Verdict: keep-open.
- Draft branch: solr-17003-draft-open.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

A query on a `DateRangeField` with `hl=true` and the default highlighter fails with HTTP 500: `ArrayIndexOutOfBoundsException: Index -1 out of bounds for length 10` at `org.apache.lucene.spatial.prefix.tree.NumberRangePrefixTree.readCell`. Setting `hl.method=fastVector` avoids it. The reporter then sees repeated warnings in the log with fastVector, and the commenter asks whether the unified results are correct with the original method. The description links a mailing-list thread.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `solr/core/src/java/org/apache/solr/highlight/UnifiedSolrHighlighter.java:135-175`: the unified highlighter is Solr's wrapper around Lucene's `UnifiedHighlighter`; `getHighlighter(req)` creates it. No field-type check is made before the highlighter reads terms for a field.
- `git grep -n "DateRangeField\|NumberRangePrefixTree\|RangeField" -- solr/core/src/java/org/apache/solr/highlight`: no match. The highlight package has no special case for range or spatial field types.
- `solr/core/src/java/org/apache/solr/schema/DateRangeField.java:47`: `DateRangeField` is a Solr field type backed by Lucene's spatial prefix-tree range encoding (`NumberRangePrefixTree` in `lucene-spatial-extras`).
- The exception is raised in Lucene's `NumberRangePrefixTree.readCell`, which the unified highlighter reaches through the field's terms. The index value -1 is read from the term bytes of a range encoding, not from a Solr path.

So the failure path is: Solr passes a non-text field to the Lucene unified highlighter; the highlighter reads its terms; Lucene's range-cell decoder fails. Solr does not guard the field type. Whether the guard belongs in Solr (skip highlighting for range or spatial fields) or in Lucene's decoder is not decided by the code.

## History

- `git log` on `solr/core/src/java/org/apache/solr/highlight` for DateRangeField or the ticket key: not searched in detail.
- No commit on the fork names SOLR-17003 (empty `git log --grep`).
- SOLR-16885 (linked, Open): not traced on the fork.

## Reproduction attempts

Not attempted. A reproduction is a small Solr test: a schema with a `DateRangeField` field, one indexed value, and a query with `hl=true` and `hl.fl` on that field under the default (unified) method, run at the pinned head. Its result would show whether the failure is reproduced on main. That run is the missing piece; it was not run in this closeout.

## Gap analysis

The reported exception is in Lucene's range-cell decoder, reached from Solr's unified highlighter with no field-type check. The ticket does not say which field value triggered it, and the code does not show whether every `DateRangeField` value fails or only some. The decision (Solr-side guard or Lucene fix) is open.

## Verdict justification

keep-open: the attempts (code reads at the pinned head, the highlight package search, the field-type read) are named, and the missing piece is a reproduction on main and the decision about where the guard belongs. close-cannot-reproduce needs a reproduction on the pinned head; none was run. pr-needed needs a decided change; the location of the change (Solr or Lucene) is not decided. The keep-open minimum is met.

## Limits

This record does not reproduce the failure. It does not trace SOLR-16885 or the mailing-list thread. It does not check Lucene's decoder code at its own version (the fork does not contain Lucene sources).

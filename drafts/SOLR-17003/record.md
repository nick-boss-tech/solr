# Skip record: SOLR-17003

- Ticket: SOLR-17003, "UnifiedHighlighter breaks with DateRangeField". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2023-09-28, updated 2023-10-21, no fix version. Link: SOLR-16885 (Open). Four comments: stillalex (2023-10-18) gives the stack trace and points at the intersection of the UnifiedHighlighter and DateRangeField; sarcanon (2023-10-19) confirms `hl.method=fastVector` avoids the error; sarcanon (2023-10-20) reports a warning that the field does not store term vectors with positions and offsets; stillalex (2023-10-21) asks whether the original highlighter gives correct results.
- Verdict: keep-open. Unchanged from the earlier keep-open verdict; the earlier vm5 closeout (DONE, pre-remediation) is not reused.
- Draft branch: solr-17003-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 14:27-14:28 UTC.

## Ticket reading

Highlighting a query on a `DateRangeField` fails with a 500. The reporter's stack trace, quoted in the comments, ends at `org.apache.lucene.spatial.prefix.tree.NumberRangePrefixTree.readCell` (an `ArrayIndexOutOfBoundsException` with index -1). Setting `hl.method=fastVector` avoids the error, at the cost of a warning about term vectors.

## Code reading

- `gradle/libs.versions.toml:39`: `apache-lucene = "10.4.0"`. The pinned head uses Lucene 10.4.0.
- `solr/core/src/java/org/apache/solr/highlight/UnifiedSolrHighlighter.java:135`: the unified highlighter class. A search at the pin for `DateRange` in `solr/core/src/java/org/apache/solr/highlight/` finds no DateRangeField handling in the highlight package.
- `UnifiedSolrHighlighter.java:26-29` imports `NumberRangePrefixTreeStrategy`, `DateRangePrefixTree` and `NumberRangePrefixTree.NRShape`/`UnitNRShape`, so the highlighter file uses the same Lucene spatial classes as the failing call.

What this shows at the pin: the failing method is in Lucene (`NumberRangePrefixTree.readCell`), which Solr uses but does not contain. Solr's highlighter does not guard DateRangeField values before handing them to the Lucene call. So the root cause sits in Lucene, and a Solr-side fix would be a guard or a different highlight path for this field type. The audit's "possible Solr-side guard, not investigated" stays open.

## History

- No commit on the pinned head names SOLR-17003 (evidence tool: no hits).
- SOLR-16885 (linked, Open) is not checked for a fix in this pass.
- Lucene's `NumberRangePrefixTree` change history is not traced here: Lucene is a separate repository.

## Reproduction attempts

Not attempted. Reproducing needs a `DateRangeField` index with the unified highlighter on the pinned build, which is a test run; this pass is review-only.

## Gap analysis

Two possible fixes exist, and both are decisions outside this repo's review: a Lucene fix for `readCell` (the cause the stack trace names), or a Solr-side guard that routes DateRangeField highlighting elsewhere. The `fastVector` workaround avoids the error but produces the term-vector warning, and the reporter asked whether the original highlighter gives correct results. That answer is not in the ticket.

Missing piece for a verdict change: (1) the status of the Lucene issue for `readCell` and whether Lucene 10.4.0 carries a fix; (2) the owner's decision on a Solr-side guard; (3) a run of the highlighter on a DateRangeField query at the pin, to check the default highlighter result against the `fastVector` one.

## Verdict justification

keep-open meets the rubric: the code path is read (the Lucene dependency pin, the highlighter, the DateRangeField strategy classes), the attempts are named (the comments' workaround), and the missing pieces are named (the Lucene status and a run). close-fixed and close-obsolete are not met. pr-needed is not met as a shown Solr defect, because the failing call is in Lucene and the Solr-side change is a design choice.

## Limits

This record does not reproduce the failure, does not check Lucene's issue tracker, and does not decide the Solr-side guard. It does not post anything to JIRA or GitHub.

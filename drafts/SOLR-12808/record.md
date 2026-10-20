# Skip record: SOLR-12808

- Ticket: SOLR-12808, Wrong highlighting using PatternReplaceCharFilterFactory.
- Verdict: keep-open.
- Draft branch: solr-12808-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 08, item 3 (`assignments/pool-aaa-skip-verdict-remediation-08.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-09-26, updated 2019-06-08, no fix versions, no links. The report: the default highlighter marks the wrong text for the query `verb_esame_num_tnv:(00031665 0035 9)` on a `TextField` whose analyzer runs `PatternReplaceCharFilterFactory` before the tokenizer. One comment (2018-09-26, the reporter): the documentation says char filters may change characters while preserving offsets back to the original text, which the reporter expects.

## Code reading

Read at the pin:

- The offset mapping between the filtered text and the original text is made by Lucene's `PatternReplaceCharFilter` (the `CharFilter` class and its offset correction); it is not in this tree (the Lucene sources were removed by `168df90d79`, SOLR-14497, an ancestor of the pin).
- The Solr highlighter takes offsets from the token stream it is given (`DefaultSolrHighlighter.java`, which delegates to Lucene's `TokenSources`); the reading did not show a Solr-side place where the char-filter offsets are dropped or shifted.
- The reporter's field type is in the ticket text; no test for a pattern-replace char filter combined with the highlighter was identified in the tree.

## History

- `168df90d79` (SOLR-14497, removed Lucene sources): ancestor of the pin; the offset code is outside the tree after it.
- No commit at the pin names SOLR-12808 (the evidence tool lists none).

## Reproduction attempts

Not run. This pass ran no builds or tests. The reproduction is the reporter's analyzer and query on a core at the pin, with `hl=true` and the highlighted field's stored text, comparing the highlight snippet against the original text. The result decides whether the fault is in Lucene's offsets or in Solr's use of them.

## Gap analysis

- The offset mapping is in Lucene; a Solr-side defect is not shown by reading.
- The reporter's text and the field's stored value are needed to decide where the wrong span comes from.
- The ticket asks for a correct highlight, which may need a Lucene change; that is not a Solr-only fix.

## Verdict justification

keep-open: the offset mapping lives in the Lucene dependency and the Solr path has not been shown to be at fault; the reproduction has not been run. close-fixed and close-obsolete do not fit; close-answered does not fit (the docs say offsets are preserved, which is the expectation the report contradicts, and that is a defect question).

Missing for this verdict: a run of the reporter's analyzer and query at the pin, with the highlight output against the original text and the Lucene version's offset behavior.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>

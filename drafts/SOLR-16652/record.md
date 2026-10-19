# Skip record: SOLR-16652

- Ticket: SOLR-16652, "multi-term synonym rule applied at query time prevents single-term matching". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2023-02-10, updated 2024-02-27, no fix version. Three comments: rseitz (2023-02-13) relays a suggestion from mkhl to rewrite the rule as directional rules; rseitz (2023-02-13) works out the two directional rules; mkhl (2023-02-13) says the behavior is by design, citing Lucene's `TestQueryParser` case for `guinea pig => cavy`.
- Verdict: keep-open. Unchanged from the earlier record; this record replaces the audit-only record with a pinned-head check.
- Draft branch: solr-16652-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 14:51-14:52 UTC.

## Ticket reading

With a query-time synonym rule `foo bar,baz`, an edismax query for `foo bar` no longer matches documents that contain only `foo`, or only `bar`. Both terms are now required, in any position. The reporter's proposed fix (adding the original terms to the rule) would let a query for `foo` match a document with only `bar`. The commenters propose directional rules as a workaround and identify the behavior as by design in Lucene's query builder.

## Code reading

- `solr/solr-ref-guide/modules/query-guide/pages/edismax-query-parser.adoc:40-44`: with `sow=false` (the default), "whitespace-separated term sequences will be provided to text analysis in one shot, enabling proper function of analysis filters that operate over term sequences, e.g., multi-word synonyms and shingles." This explains the mechanism that makes the multi-term rule apply to the whole sequence.
- `solr/solr-ref-guide/modules/indexing-guide/pages/filters.adoc:3114-3116`: "Synonym Filter has been deprecated in favor of Synonym Graph Filter, which is required for multi-term synonym support."
- `solr/solr-ref-guide/modules/query-guide/pages/standard-query-parser.adoc:42` and `other-parsers.adoc:1333` describe the same `sow` behavior for the other parsers.

What this shows at the pin: the docs state that multi-term synonyms work on the whole sequence (so the rule applies to `foo bar` as one unit). They do not state the consequence the ticket reports, namely that single-term matches are lost. The by-design explanation is in Lucene's query builder and its test, which is outside this repository and was not re-read here.

## History

- No commit on the pinned head names SOLR-16652 (evidence tool: no hits).
- Lucene's `TestQueryParser` case is cited by the commenter and not re-read in this pass.

## Reproduction attempts

Not attempted. A check needs an index with the rule `foo bar,baz` and an edismax query for `foo`, `bar` and `foo bar`, which is a test run; this pass is review-only.

## Gap analysis

The ticket's behavior follows from how multi-term synonyms are analyzed, and the docs describe that mechanism without the consequence. Two open choices remain: document that single-term matches are lost (a docs change, which would then answer the question for users), or change the semantics (a Lucene and Solr design change). The ticket does not choose between them.

Missing piece for a verdict change: a decision on whether the behavior should be documented as intended (a docs note, after which the ticket can be closed as answered) or changed (a design issue with an owner). With that decision, the record can name the close or the PR.

## Verdict justification

keep-open meets the rubric: the code path and docs are read at the pin (`edismax-query-parser.adoc:40-44`, `filters.adoc:3114-3116`), no reproduction is run, and the missing piece is named (the documentation or design decision). close-answered is not met: the docs describe the mechanism, not the single-term consequence, so they do not answer the question. pr-needed is not met: the behavior is by design in the analyzer, and no defect in Solr's code is shown.

## Limits

This record does not reproduce the matching behavior and does not read Lucene's query builder at the pin. It does not decide whether a docs note or a semantic change is wanted. It does not post anything to JIRA or GitHub.

# Skip record: SOLR-13832

- Ticket: SOLR-13832, MoreLikeThis parsers SimpleMLTQParser and CloudMLTQParser - termvectors vs fieldvalues. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-10-09, updated 2020-01-21, no fix versions, no links, no comments.
- Verdict: keep-open.
- Draft branch: solr-13832-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and grep run at this head (detached worktree).
- Source: remediation redo of batch 05, item 19, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (vm2, from the C reading) cited the same two parsers; the lines are confirmed here.

## Ticket reading

The standalone MLT parser finds similar documents from the term vectors of the original document. The cloud parser builds the query from the stored field values, so a field that is indexed but not stored gives a different result. The reporter asks for a parameter that chooses the source.

## Code reading at the pinned head

- solr/core/src/java/org/apache/solr/search/mlt/CloudMLTQParser.java: `likeDoc` (from about line 62) builds the terms from the stored document's field values (`doc.getFieldValues` at :70, through `getFieldValuesIncludingCopyField`, defined at :115-124).
- solr/core/src/java/org/apache/solr/search/mlt/SimpleMLTQParser.java:60: `moreLikeThis -> moreLikeThis.like(scoreDocs[0].doc)`. This is the term-vector path the ticket describes.
- `git grep -n -i "termvector\|termVector\|useTermVectors\|fieldvalues" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/core/src/java/org/apache/solr/search/mlt` returns only field-value lines (for example `getFieldValuesIncludingCopyField`). No parameter switches between the two sources.

So the difference the ticket describes is in code, and no option to choose the source exists.

## History

- No commit on the pin names SOLR-13832.

## Reproduction attempts

Not run. A reproduction needs a standalone and a SolrCloud core with a field that is indexed and not stored, then an MLT request on each. This pass did not start a core.

## Gap analysis

The two parsers differ as described, and the ticket asks for a choice between them. Adding that parameter is a feature decision (the name, the default, and how the cloud parser would read term vectors when they are not stored).

## Verdict justification

keep-open: the attempts made are the code reads and the greps above at the pinned head, the Jira read, and the absence of any switch. The missing piece is the feature decision (parameter design and default) and a run that shows the two results on an unstored field. close-fixed and close-obsolete are not supported: both parsers exist and the difference is present. pr-needed is not supported without that decision.

Missing for a closing or PR verdict: the parameter decision, and the run output.

## Limits

This record does not run an MLT request. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.

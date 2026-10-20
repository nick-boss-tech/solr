# Skip record: SOLR-10856

- Ticket: SOLR-10856, ExtendedDismaxQParser (edismax) override OR when mm=100%. JIRA read in this pass: Open, resolution none, fix versions none, 1 comment (2017-06-25, a reply that the behavior is what mm is supposed to do, citing SOLR-2649 and SOLR-8812); updated 2019-06-08.
- Verdict: keep-open (unchanged status; this record replaces the earlier record on this branch, whose cross-audit reading named missing items J and C).
- Draft branch: solr-10856-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: with defType=edismax and mm=100%, the query q=type_s:(A OR C) returns no documents from Solr 5.5.1 onward (the parsed query becomes +((type_s:A type_s:C)~2)), where Solr 5.4.1 returned them. The reporter links it to SOLR-8812. The 2017 reply says the behavior is what mm is designed to do and that the change came from SOLR-2649.

## Code reading

- solr/solr-ref-guide/modules/query-guide/pages/dismax-query-parser.adoc:78-84: the mm section defines mm over the "optional" clauses of the query: the minimum number of those clauses that must match.
- solr/solr-ref-guide/modules/query-guide/pages/edismax-query-parser.adoc:47-52: the default mm is 0% unless q.op is AND with no explicit operators; the guide does not state how an explicit OR inside parentheses is treated under an explicit mm of 100%.
- The OR-ed group in the ticket is a single optional clause of two sub-clauses; mm=100% makes every optional clause required, which is the reading the 2017 reply gives. This is consistent with the guide's definition, but the guide does not state the case directly.
- The parser code for mm at the pin was not traced line by line in this pass.

So the behavior the ticket reports is the documented definition of mm applied to the explicit OR group, as the 2017 reply argues. Whether it is a defect is the open question.

## History

- No commit at the pin names SOLR-10856 (git log --grep at the pin: none).
- SOLR-2649 and SOLR-8812 (named in the ticket and the reply) were not traced in this pass.

## Reproduction attempts

Not run. A reproduction would send the ticket's query with defType=edismax and mm=100% at the pin and record the parsed query and the hit count against the same index with mm unset.

## Gap analysis

The code and the guide's mm definition agree with the 2017 reply. Whether the outcome is intended is a maintainer decision; the guide does not settle the OR-group case.

## Verdict justification

keep-open: the behavior matches the mm definition in the guide, but the guide does not address an explicit OR group directly, and the reporter's expectation has not been answered by a maintainer in the ticket. close-answered would be a maintainer decision to treat the 2017 reply as the answer; the rubric's close-answered path needs a ref-guide link that answers the question, which the guide does not state for this case. close-fixed and close-obsolete are not supported.

Missing for a closing verdict: a maintainer decision on whether mm=100% applies to explicit OR groups as the reply describes (then close as answered, citing the mm definition in dismax-query-parser.adoc), or a change to the behavior; or a run at the pin recording the parsed query and hits.

## Limits

This record does not run the query, does not trace the edismax mm code, and does not post anything to JIRA or GitHub.

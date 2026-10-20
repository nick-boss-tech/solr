# Skip record: SOLR-11858

- Ticket: SOLR-11858, "NPE in DirectSpellChecker". JIRA status read 2026-10-20T07:12:00Z (UTC): Open, resolution None, updated 2022-02-10. Linked: LUCENE-8147 (Open).
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-11858-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T07:12:00Z (UTC).

## Ticket reading

A misspelled query with more than zero results throws an NPE in Lucene's `DirectSpellChecker.suggestSimilar`. The reporter and a committer could not reproduce it on 7.1 or 7.2. Rob Muir suspects a non-positive count from the Solr adapter and opened LUCENE-8147. No later comment.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/spelling/DirectSolrSpellChecker.java:197-203`: `count` is `options.alternativeTermCount` when it is positive and the term frequency is positive, otherwise `options.count`. The value goes straight into `checker.suggestSimilar(term, count, ...)`.
- Nothing in this method rejects a count of zero or less before the call.

## History

- evidence.py at the pinned head: no commits naming SOLR-11858; no test classes named.

## Reproduction attempts

Not attempted. The ticket's spellcheck config is cut at 1200 characters. A run needs the full config, a misspelled query with more than zero results, and a setting that gives a non-positive count.

## Gap analysis

The likely trigger is a non-positive count reaching Lucene, consistent with the code path. It is not confirmed: the trigger config is unknown, and LUCENE-8147 is open.

## Verdict justification

keep-open meets the rubric: attempts made are the code-path reading and comment review; missing pieces are the full config and a run hitting a non-positive count. close-cannot-reproduce is not supported, since no run hit the trigger. close-fixed and close-obsolete are not supported: the path is present and LUCENE-8147 is open.

## Limits

This record does not establish the trigger. It does not check the Lucene version the fork uses.

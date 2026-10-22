# Skip record: SOLR-4533

- Ticket: SOLR-4533, "Synonyms, created in custom filters are ignored after tokenizers". JIRA status read 2026-10-22T06:12Z (UTC): Open, resolution None, created 2013-03-06, updated 2016-11-16, fix versions none.
- Verdict: keep-open (kept; re-read at the pinned head, replaces the audit-based record).
- Draft branch: solr-4533-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-22.

## Ticket reading

With q.op=AND, a custom filter adds a synonym at the same position as an existing token, and edismax makes both tokens required, which cannot match. The reporter proposes putting the synonyms in a separate clause with occur=SHOULD. Comment 1 (2013-03-06) says "Fixed" with no test. Comment 2 (dsmiley, 2016-11-16) asks whether the issue still exists and asks for a test. Comment 3 (reporter, 2016-11-16) says they cannot check it any more.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java:1343-1364`: with AND (`required`), each query is added as one MUST clause. Only a BooleanQuery whose clauses are all SHOULD DisjunctionMaxQuery is unwrapped. Any other BooleanQuery of same-position alternatives is wrapped as a single MUST clause, so its inner SHOULD clauses keep their own occur. This is the reading that the ticket's failure mode depends on, and at this head it does not match it.
- `ExtendedDismaxQParser.java:1445`: the comment says same-position synonyms become a SynonymQuery. This is the path for real synonym filters.
- `git log --oneline -3 -- solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java` at the pinned head: the recent commits are unrelated (Error Prone update, minimum-match, alwaysStopwords).
- evidence.py at the pinned head: no commit on origin/main names SOLR-4533. No test class is named in the ticket.

## History

No commit names the ticket. The reporter's "Fixed" comment refers to a patch that was never attached in the ticket text.

## Reproduction attempts

Not run in this pass. The code reading suggests the failure does not occur at this head, but the ticket asked for a test from 2016 and none was ever written. A focused check would build a custom filter that emits a same-position token and run an edismax query with q.op=AND at the pinned head, recording the output and log path.

## Gap analysis

The code reading argues against the reported failure, but the claim has not been shown by a run. close-cannot-reproduce needs a reproduction attempt, and a code-path argument alone does not meet the rubric, so the verdict stays keep-open. The missing piece is that run.

## Verdict justification

- close-fixed: not supported. No commit names the ticket.
- close-obsolete: not supported. The edismax path is present.
- close-cannot-reproduce: not met. The reading is strong, but no reproduction was run.
- pr-needed: not met. The code reading does not show a defect.
- keep-open: the attempts made are the JIRA re-read, the code-path reading, and the history check. The missing piece is the reproduction at the pinned head.

## Limits

This record does not run a test. It does not establish whether the reported query fails on the current head.

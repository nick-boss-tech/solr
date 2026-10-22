# Skip record: SOLR-4558

- Ticket: SOLR-4558, "raw query parser cannot parse value "{!"". JIRA status read 2026-10-22T06:06Z (UTC): Open, resolution None, created 2013-03-11, updated 2013-03-16, fix versions none.
- Verdict: keep-open (kept; re-read at the pinned head, replaces the audit-based record of 2026-10-19).
- Draft branch: solr-4558-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-22.

## Ticket reading

The reporter says `QParser.getParser` handles local params (`{!...}`) before the query reaches a parser plugin, so the raw parser never sees a value beginning with `{!`. The proposed fix is an `enableLocalParams` option that bypasses the local-params step. Comment 1 (rjernst, 2013-03-16) added that option in a patch and waited for a test in SOLR-4559. Comment 2 (yseeley, 2013-03-16) argued that a query-wide switch is a poor idea, and that an explicit raw query with a dereferenced value works.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/search/QParser.java:392-396`: local params are allowed when the default parser is lucene or func.
- `solr/core/src/java/org/apache/solr/search/QParser.java:424-426`: when local params are allowed and the query starts with `{!`, `QueryParsing.parseLocalParams` runs before any parser plugin is chosen. This is the step the ticket describes.
- `solr/core/src/java/org/apache/solr/search/RawQParserPlugin.java:36`: the raw parser plugin is present.
- `git grep -n enableLocalParams 498a09ae... -- solr`: no match. The option the ticket proposes is not on main.

## History

`git log` at the pinned head finds no commit naming SOLR-4558 (evidence.py, ref origin/main, run 2026-10-22). No commit has added the option.

## Reproduction attempts

Not run in this pass. The record is based on the code path above and the absence of the option. A focused run would send a raw query whose value starts with `{!` through the default request path at the pinned head and record the output and log path.

## Gap analysis

The mechanism in the ticket is still in the code. What is not settled is whether the behaviour is a defect or the intended result of local-params parsing, and the ticket's own comments disagree on that. The proposed fix is a design choice, not a bug fix, so it needs a maintainer decision before a change can be made.

## Verdict justification

- close-fixed: not supported. No commit names the ticket and the option is absent.
- close-obsolete: not supported. The raw parser and the local-params path are present.
- close-cannot-reproduce: not met. No reproduction was attempted at the pinned head.
- pr-needed: not met. The fix is a design call, and no reproduction was run.
- keep-open: the attempts made are the JIRA re-read, the code-path reading, and the history check. The missing piece is a reproduction at the pinned head and a maintainer decision on the option.

## Limits

This record does not run a test. It does not establish that a value beginning with `{!` fails on the current head.

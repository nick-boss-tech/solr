# SOLR-4690 premise report (Critical batch 2, unit 9)

Ticket: Highlighting Doesn't works when boost is used along with
query. Jira Open, Critical, reporter lukes shaw, last updated
2016-12-07. No fork branch, no PR, no prior premise report or
receipt on coordination-ui.

## Verdict: NOT REPRODUCED on current main

Scratch probe only, never committed, fresh worktree at batch base
83d560030fde (main, SOLR-18585). Probe test
org.apache.solr.highlight.Solr4690PremiseProbeTest (SolrTestCaseJ4
harness only; a throwaway, not a committed test), seed
4690C0FFEE4690, BUILD SUCCESSFUL, 1 test, 0 failures, fresh JUnit
XML 2026-11-01T22:42:13Z (copy ~/workspace/tools/4690-premise-
TEST-Solr4690PremiseProbeTest.xml; logs
~/workspace/tools/4690-premise-probe.log and
~/workspace/tools/4690-premise-probe2.log).

Setup: one doc, subject "apple pie and apple sauce", title
"apple tart", weight 2.0; edismax query qf='subject title'
v='apple'; hl.fl=subject. Every leg returned highlighted snippets
for the one matching doc:

- control-no-boost: HIGHLIGHTING PRESENT
- boost-function (boost=weight, multiplicative, the ticket's
  failing shape): HIGHLIGHTING PRESENT
- boost-plus-group (group=true, group.field=title): HIGHLIGHTING
  PRESENT
- boost-original-hl (hl.method=original, the 4.1-era highlighter):
  HIGHLIGHTING PRESENT
- control-original-hl: HIGHLIGHTING PRESENT
- boost-hlq (hl.q set explicitly to the same edismax query, the
  ticket's other distinctive parameter): HIGHLIGHTING PRESENT
- boost-hlq-original: HIGHLIGHTING PRESENT

## Reading of the ticket, and limits of this probe

The 2013 report is Solr 4.1.0, the requester never posted the
requested response, and the only later comment (2016) is a dismax
workaround, not a reproduction. The mechanism the ticket implies
(a multiplicative boost breaking highlighting) does not survive
on current main in any of the seven shapes above, including the
original highlighter and the grouping leg.

Limits, stated plainly: single-node probe on the standard test
schema (subject/title/weight), not the ticket's own fields
(body/title/boost_weight) and not its full parameter set
(pf/ps/pf2/pf3, group.ngroups, group.limit, hl.q as a _query_
wrapper). A byte-faithful replay of the 4.1 request was not
attempted; the probe settles the core claim (boost kills
highlighting), not every parameter combination in the thread.

## Recommendation

No implementation unit follows from this premise run: there is no
reproduced defect to fix, and no branch. If the ticket is ever
revisited, it needs a current-release reproduction from a real
request/response pair first, which is the question the board's
existing stale-check draft (jira/jira-comment-SOLR-4690-draft.md)
already poses. No PR, no Jira/GitHub post.

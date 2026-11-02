# Claim: SOLR-11310 gate (fixed batch 2 queue, follow-up to the reproduced premise)

Host: phone agent. Run: board-work-2026-11-01, 2026-11-01 ~17:53 MST.
Unit: exact-head gate of the existing fork branch solr-11310-submit
at e38ddec5279c465f65a2fa2de22ee9c92e0f5e31 (base 14c7aac0d1; two-line
ReRankCollector fix removing the break, testBoostedDocsSurviveLTRRescore
in TestLTRReRankingPipeline, changelog fragment). Pre-claim checks:
no 11310 gate claim, gate file, or receipt on coordination-ui; only
the premise claim (DONE, REPRODUCED) exists; fork branch confirmed at
the head above. Rebase check: ReRankCollector.java and the test file
are unchanged between the branch base 14c7aac0d1 and current main
83d560030fde, so the fix applies identically and no rebase is needed
for the gate; the branch is gated at its exact head, as the SOLR-16725
unit did for its pre-existing branch.
Plan: worktree at the exact head, full hardening gate (changelog YAML
parse, root tidy clean, Error Prone compile, pre-fix proof with
ReRankCollector.java reverted to base 14c7aac0d1 with the test
expected to FAIL, head focused test expected to PASS at seed
11310C0FFEE11310 with fresh JUnit XML, :solr:core:check -x test and
:solr:modules:ltr:check -x test), receipt at receipts/SOLR-11310.md.
No PR, no Jira/GitHub post.

Status: DONE. Verdict GATE GREEN at the exact head
e38ddec5279c465f65a2fa2de22ee9c92e0f5e31 (changelog rc=0; tidy rc=0
clean; Error Prone compile BUILD SUCCESSFUL; pre-fix proof 1 test
1 failure as expected; head test 1 test 0 failures, seed
11310C0FFEE11310, fresh JUnit XML; :solr:core:check -x test and
:solr:modules:ltr:check -x test both BUILD SUCCESSFUL). Receipt at
receipts/SOLR-11310.md. No branch change, nothing re-pushed.

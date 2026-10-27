# Assignment: SOLR-13770 replica recovery deep nested directories, implementation round 1

Status: OPEN

Tags: implementation, premise-run

Source: `reports/critical-implementation-triage-round-1.md`, rank 10. Posted by the overnight factory buffer check on 2026-10-27 as a buffer top-up.

Claim this assignment first with a file under `claims/` referencing `critical-impl-13770-round-1` before starting.

## Defect in one line

Replica recovery after restart creates deep nested directories and fails.

## Step 1: premise run first

Restart a node that has several indexes and tlog replay in progress, following the ticket's steps. Confirm or kill: the nested directories appear and recovery fails.

If the premise fails, the deliverable is a report saying so, and the work stops. Do not implement, and do not push a branch.

## Step 2: implement narrowly, only after the premise reproduces

Implement narrowly on branch `solr-13770-submit` only. The triage points at tlog replay and directory creation during recovery; keep the change to that surface. The triage notes reproduction needs a cluster run.

New tests extend `SolrTestCase` or `SolrCloudTestCase`, never `SolrTestCaseJ4`.

## Stop point

Stop at "branch pushed, back to the owner for gate". When the branch is pushed, request its gate with `pool/bin/request-gate.py`.

## Limits

No PR, no Jira post, no GitHub comment. No work outside branch `solr-13770-submit`.

## Collision note, checked live 2026-10-27

No fork branch named `solr-13770-submit` existed, and no assignment, claim, gate job, or implementation report for SOLR-13770 existed on the board at coordination-ui tip e45cc59f9652802deab194f8baf81137155c1160. No open apache/solr PR naming SOLR-13770 was found in a live GitHub PR search. The triage collision check (2026-10-25) also found none. The board holds a Jira comment draft for this ticket (`jira/jira-comment-SOLR-13770-draft.md`); that is not an assignment, claim, gate job, or report, so it does not trigger the skip rule. Higher-ranked tickets were skipped this run because they already have critical-impl assignments (SOLR-17370, SOLR-12069, SOLR-16139, SOLR-14996, SOLR-17291, SOLR-14833, SOLR-7351) or because a fork branch named `solr-<key>-submit` already exists (SOLR-14638, SOLR-15326).

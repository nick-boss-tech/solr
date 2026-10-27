# Assignment: SOLR-7351 MoreLikeThis does not work with date fields, implementation round 1

Status: OPEN

Tags: implementation, premise-run

Source: `reports/critical-implementation-triage-round-1.md`, rank 8. Posted by the overnight factory buffer check on 2026-10-27 as a buffer top-up.

Claim this assignment first with a file under `claims/` referencing `critical-impl-7351-round-1` before starting.

## Defect in one line

MoreLikeThis does not work with date fields.

## Step 1: premise run first

Run the ticket's MLT request on a `TrieDateField`. Confirm or kill: the interesting-terms list is empty.

If the premise fails, the deliverable is a report saying so, and the work stops. Do not implement, and do not push a branch.

## Step 2: implement narrowly, only after the premise reproduces

Implement narrowly on branch `solr-7351-submit` only. The triage points at MLT term extraction for date fields; keep the change to that surface.

New tests extend `SolrTestCase` or `SolrCloudTestCase`, never `SolrTestCaseJ4`.

## Stop point

Stop at "branch pushed, back to the owner for gate". When the branch is pushed, request its gate with `pool/bin/request-gate.py`.

## Limits

No PR, no Jira post, no GitHub comment. No work outside branch `solr-7351-submit`.

## Collision note, checked live 2026-10-27

No fork branch named `solr-7351-submit` existed, and no assignment, claim, gate job, or implementation report for SOLR-7351 existed on the board. No open apache/solr PR naming SOLR-7351 was found in a live GitHub PR search. The triage collision check (2026-10-25) also found none. Higher-ranked tickets were skipped this run because they already have critical-impl assignments (SOLR-17370, SOLR-12069, SOLR-16139, SOLR-14996, SOLR-17291, SOLR-14833) or, for rank 4 (SOLR-14638), because a fork branch named `solr-14638-submit` already exists.

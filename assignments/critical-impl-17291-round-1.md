# Assignment: SOLR-17291 indexDeletionPolicy must not be null on new replica first searcher, implementation round 1

Status: OPEN

Tags: implementation, premise-run

Source: `reports/critical-implementation-triage-round-1.md`, rank 6. Posted by the overnight factory buffer check on 2026-10-27 as a buffer top-up.

Claim this assignment first with a file under `claims/` referencing `critical-impl-17291-round-1` before starting.

## Defect in one line

"indexDeletionPolicy must not be null" when a new replica opens its first searcher.

## Step 1: premise run first

Create a replica on a configuration with the deletion policy unset. Confirm or kill: `openNewSearcher` receives a null policy.

If the premise fails, the deliverable is a report saying so, and the work stops. Do not implement, and do not push a branch.

## Step 2: implement narrowly, only after the premise reproduces

Implement narrowly on branch `solr-17291-submit` only. The triage points at the core open path where the deletion policy is read; keep the change to that surface.

New tests extend `SolrTestCase` or `SolrCloudTestCase`, never `SolrTestCaseJ4`.

## Stop point

Stop at "branch pushed, back to the owner for gate". When the branch is pushed, request its gate with `pool/bin/request-gate.py`.

## Limits

No PR, no Jira post, no GitHub comment. No work outside branch `solr-17291-submit`.

## Collision note, checked live 2026-10-27

No fork branch named `solr-17291-submit` existed, and no assignment, claim, gate job, or implementation report for SOLR-17291 existed on the board. No open apache/solr PR naming SOLR-17291 was found in a live GitHub PR search. The triage collision check (2026-10-25) also found none. Rank 4 (SOLR-14638) was skipped this run because a fork branch named `solr-14638-submit` already exists.

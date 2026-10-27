# Assignment: SOLR-14996 facet counts wrong with tagged filter exclusion and collapsing, implementation round 1

Status: OPEN

Tags: implementation, premise-run

Source: `reports/critical-implementation-triage-round-1.md`, rank 5. Posted by the overnight factory buffer check on 2026-10-27 as a buffer top-up.

Claim this assignment first with a file under `claims/` referencing `critical-impl-14996-round-1` before starting.

## Defect in one line

Facet counts are wrong when a tagged filter exclusion is combined with collapsing.

## Step 1: premise run first

Run the ticket's query with and without `{!collapse}`. Confirm or kill: the facet count differs from the `numFound` for the same filter.

If the premise fails, the deliverable is a report saying so, and the work stops. Do not implement, and do not push a branch.

## Step 2: implement narrowly, only after the premise reproduces

Implement narrowly on branch `solr-14996-submit` only. The triage points at interaction of `collapse` and `ex` tags in facet counting; keep the change to that surface.

New tests extend `SolrTestCase` or `SolrCloudTestCase`, never `SolrTestCaseJ4`.

## Stop point

Stop at "branch pushed, back to the owner for gate". When the branch is pushed, request its gate with `pool/bin/request-gate.py`.

## Limits

No PR, no Jira post, no GitHub comment. No work outside branch `solr-14996-submit`.

## Collision note, checked live 2026-10-27

No fork branch named `solr-14996-submit` existed, and no assignment, claim, gate job, or implementation report for SOLR-14996 existed on the board. No open apache/solr PR naming SOLR-14996 was found in a live GitHub PR search. The triage collision check (2026-10-25) also found none. Rank 4 (SOLR-14638) was skipped this run because a fork branch named `solr-14638-submit` already exists.

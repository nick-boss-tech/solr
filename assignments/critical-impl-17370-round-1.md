# Assignment: SOLR-17370 facet IllegalAccessException, implementation round 1

Status: OPEN

Tags: implementation, premise-run

Source: `reports/critical-implementation-triage-round-1.md`, rank 1. Posted by the overnight factory buffer check on 2026-10-27 as a buffer top-up.

Claim this assignment first with a file under `claims/` referencing `critical-impl-17370-round-1` before starting.

## Defect in one line

Search with facets throws `IllegalAccessException` when a string field holds numeric values.

## Step 1: premise run first

Index a string field with values such as `123` and facet on it with a second field. Run on main. Confirm or kill: the exception appears only when the field mixes numeric-looking values.

If the premise fails, the deliverable is a report saying so, and the work stops. Do not implement, and do not push a branch.

## Step 2: implement narrowly, only after the premise reproduces

Implement narrowly on branch `solr-17370-submit` only. The triage points at facet accumulator selection for string fields in `search/facet`; keep the change to that surface.

New tests extend `SolrTestCase` or `SolrCloudTestCase`, never `SolrTestCaseJ4`.

## Stop point

Stop at "branch pushed, back to the owner for gate". When the branch is pushed, request its gate with `pool/bin/request-gate.py`.

## Limits

No PR, no Jira post, no GitHub comment. No work outside branch `solr-17370-submit`.

## Collision note, checked live 2026-10-27

No fork branch named `solr-17370-submit` existed, and no assignment, claim, gate job, or implementation report for SOLR-17370 existed on the board. The only apache/solr PR naming SOLR-17370 is PR #3346, closed unmerged on 2025-08-30. The batch 07 critical-comments draft for this ticket is a stale-check, not implementation work.

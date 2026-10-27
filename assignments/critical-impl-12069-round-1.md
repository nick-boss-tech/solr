# Assignment: SOLR-12069 q.op ignored on standard query parser, implementation round 1

Status: OPEN

Tags: implementation, premise-run

Source: `reports/critical-implementation-triage-round-1.md`, rank 2. Posted by the overnight factory buffer check on 2026-10-27 as a buffer top-up.

Claim this assignment first with a file under `claims/` referencing `critical-impl-12069-round-1` before starting.

## Defect in one line

`q.op` is ignored on the standard query parser.

## Step 1: premise run first

Reproduce on the techproducts example as the ticket says. Run `q=a b` with `q.op=AND` and with the default. Confirm or kill: the results match the default operator.

If the premise fails, the deliverable is a report saying so, and the work stops. Do not implement, and do not push a branch.

## Step 2: implement narrowly, only after the premise reproduces

Implement narrowly on branch `solr-12069-submit` only. The triage points at default-operator plumbing in the query parser; keep the change to that surface.

New tests extend `SolrTestCase` or `SolrCloudTestCase`, never `SolrTestCaseJ4`.

## Stop point

Stop at "branch pushed, back to the owner for gate". When the branch is pushed, request its gate with `pool/bin/request-gate.py`.

## Limits

No PR, no Jira post, no GitHub comment. No work outside branch `solr-12069-submit`.

## Collision note, checked live 2026-10-27

No fork branch named `solr-12069-submit` existed, and no assignment, claim, gate job, or implementation report for SOLR-12069 existed on the board. No open apache/solr PR naming SOLR-12069 was found in a live GitHub PR search. The triage collision check (2026-10-25) also found none.

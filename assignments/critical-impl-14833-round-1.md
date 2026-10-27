# Assignment: SOLR-14833 Empty highlight entry for Arabic phrase queries with the regex fragmenter, implementation round 1

Status: OPEN

Tags: implementation, premise-run

Source: `reports/critical-implementation-triage-round-1.md`, rank 7. Posted by the overnight factory buffer check on 2026-10-27 as a buffer top-up.

Claim this assignment first with a file under `claims/` referencing `critical-impl-14833-round-1` before starting.

## Defect in one line

Empty highlight entry for some Arabic phrase queries with the regex fragmenter.

## Step 1: premise run first

Replay the ticket's request against a small index with `hl.fragmenter=regex`. Confirm or kill: the highlight entry is empty when a match exists.

If the premise fails, the deliverable is a report saying so, and the work stops. Do not implement, and do not push a branch.

## Step 2: implement narrowly, only after the premise reproduces

Implement narrowly on branch `solr-14833-submit` only. The triage points at the regex fragmenter with multi-term phrase matches; keep the change to that surface.

New tests extend `SolrTestCase` or `SolrCloudTestCase`, never `SolrTestCaseJ4`.

## Stop point

Stop at "branch pushed, back to the owner for gate". When the branch is pushed, request its gate with `pool/bin/request-gate.py`.

## Limits

No PR, no Jira post, no GitHub comment. No work outside branch `solr-14833-submit`.

## Collision note, checked live 2026-10-27

No fork branch named `solr-14833-submit` existed (and no fork branch containing `14833`), and no assignment, claim, gate job, or implementation report for SOLR-14833 existed on the board. No open apache/solr PR naming SOLR-14833 was found in a live GitHub PR search. The triage collision check (2026-10-25) also found none. The ticket appears in `reports/critical-comments-round-1-batch-05.md` only as a stale-check comment draft, explicitly not a fix, which does not collide with this implementation assignment.

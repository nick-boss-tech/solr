# Assignment: SOLR-17268 head reconciliation (records, round 1)

Status: OPEN
Tags: records

Before any opening decision on SOLR-17268, settle which head is the PR vehicle. The records name three heads: `9993394741` (re-gate NOT GREEN, vm2), the live fork tip of `solr-17268-submit` at `77a7e748e9dd` (top commit 2026-10-18, "SOLR-17268: fix the arrntv facets test request and the terms value match"), and `f888c1cea8` (re-gate GREEN, vm3, 2026-10-18, operator-approved, per `gates/SOLR-17268.md` on the archive board). A GitHub compare of `f888c1cea8...77a7e748e9dd` reports diverged (34 ahead, 7 behind, 211 files, mostly base drift), so the gated head and the live tip are not the same line.

## Work

1. Tree-compare the two heads restricted to the ticket's own files (the JSON writer tests and fixtures the branch touches): are the ticket changes identical in content between `f888c1cea8` and `77a7e748e9dd`?
2. Establish from the gate record and the branch history which head the GREEN gate actually ran on, and whether that head is pushable/openable as the PR vehicle today (base freshness, changelog entry present, tidy state).
3. Deliverable: `reports/solr-17268-head-reconciliation-round-1.md` naming the single head the owner would open from, the evidence for it, and any step still owed (for example a re-gate if the openable head is not the gated one).

No pushes, no PR, no branch edits in this assignment; it produces the settled head for the owner's route decision.

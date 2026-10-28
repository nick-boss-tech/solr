# Gate job: SOLR-10928 live head re-gate (elevate.q)

Status: DONE

Completed 2026-10-28 (cleanup slice, phone agent): executed by vm6 on 2026-10-25 at the pinned head `f2001e640a46`; all legs rc 0 and the focused classes passed in both runs. Recorded in `receipts/SOLR-10928.md`, section 'Live-head gate, 2026-10-25'.
Tags: gate

- Branch: `solr-10928-submit` on the fork (nick-boss-tech/solr).
- Head to gate: `f2001e640a464e8e861ee3c202821b6dcc4bd497` (the live tip; the earlier gate was green at `1ff57c1b21`, and two factory commits sit on top of it: coverage hardening and a gate repair).
- Base: origin/main at gate time.
- Module: `:solr:core`.
- Focused classes: `org.apache.solr.handler.component.QueryElevationComponentTest`, `QueryElevationComponentParamTest`, and the distributed elevation test named on the branch.
- Standard legs: tidy (with the GATE INVALID guard), Error Prone compile (`-Pvalidation.errorprone=true`), and `:solr:core:check -x test`.
- Deliverable: a receipt section for this head in `receipts/SOLR-10928.md` (create the file if absent; it is the single-writer receipt exception), citing the gate log path and the fresh JUnit XML.

Why: the ticket now carries a status note draft citing this branch; the gate evidence should cover the exact head the note points at.

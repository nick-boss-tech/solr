# Claim: skip-SOLR-16253-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: ACTIVE; mark DONE with the deliverable paths when the work lands.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 1. Start from `material/skip-audit/remediation-current.json` (item SOLR-16253) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T17:09:52Z (UTC).

Heartbeat: 2026-10-19T17:09:52Z

Append one heartbeat line at least once an hour while the claim is active, and push it.

Reading: the collateParam override is applied in `solr/core/src/java/org/apache/solr/spelling/SpellCheckCollator.java` (lines 111 to 121 at origin/main 498a09aea97): each `spellcheck.collateParam.X` value calls `params.set(X, ...)`, so it replaces the request's `fq` for the verification query. The hit check is at lines 152 to 180 (`hits > 0 || !verifyCandidateWithQuery`). The ticket's expected result (collation returned with `collateParam.fq=*:*`) follows from that code path; whether it reproduces needs a run on an index.

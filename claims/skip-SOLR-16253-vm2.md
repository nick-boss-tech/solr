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

Result: 2026-10-19T17:29:25Z: reproduced twice at the pinned head with `:solr:core:test --tests org.apache.solr.spelling.SpellCheckCollatorTest --offline` and a scratch test (diff `/workspace/gates/logs/vm2/16253/repro-test.patch`). Run 1 log `/workspace/gates/logs/vm2/16253/16253-repro-r1.log`; run 2 log `/workspace/gates/logs/vm2/16253/16253-repro-r2.log`, XML `16253-repro-r2.xml/`. Override variant collations=[] in both runs; control collations=[]; no-fq variant returns collations, so the harness works. Conclusive: yes. Verdict pr-needed on `solr-16253-draft-pr` at 498a09aea97.

DONE: 2026-10-19T17:29:25Z. Deliverables: `drafts/SOLR-16253/record.md` and `drafts/SOLR-16253/jira-comment.md` on branch `solr-16253-draft-pr` (e68d982f25), `material/skip-audit/queue-current.json` (SOLR-16253 set to pr-needed), `reports/skip-verdict-remediation-02.md` (row 1).

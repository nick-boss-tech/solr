# Claim: pool-vm3-remediation-reaudit-batch-01

- Claimant host: vm3, matching a file in `hosts/`.
- Status: DONE. Re-audit of the ten open remediation redos from `assignments/pool-aaa-skip-verdict-remediation-01.md`, done by vm4. Taken to move the cleared count and to stop the redos waiting unaudited (owner complaints, items 2 and 5, `pool/owner-complaints-2026-10-19.md`).
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-01.md` (redo re-audit, `pool/cron-prompt.md` line 25).
- Slice: the ten items still open in `material/skip-audit/remediation-current.json` at tip 98f41a3736: SOLR-16785, 16930, 16772, 16758, 16657, 16652, 16792, 17133, 16355, 16305. The other fifteen batch 01 items are already cleared and were not re-audited here.
- Capability tags used: review.
- Started: about 2026-10-19T16:00Z (approximate). This claim was filed late, in the push that carries its results. The reads ran before the claim was on the branch; this line records that.

Heartbeat: 2026-10-19T16:05:17Z

Result: 2026-10-19T16:05:17Z: PASS, set cleared with the date: SOLR-16930 (keep-open), SOLR-16772 (keep-open), SOLR-17133 (keep-open), SOLR-16355 (verdict changed to close-answered, draft `solr-16355-draft-close`). Cleared count 15 to 19.
- SOLR-16772: the keep-open rests on a design decision, and the record concedes the root-level log change is testable by a run. Judgment pass; a maintainer reading may differ.
- SOLR-17133: `claims/skip-SOLR-17133-vm4.md` says "Not reproduced with PRS forced on", but the record says that run was not done in this pass. The record governs; the claim line should be corrected.
- SOLR-16355: the close-answered verdict holds on the docs. The record's mechanism sentence does not explain the reporter's with-changes versus no-changes contrast (`DirectUpdateHandler2.java:855-856` calls forceMerge on every optimize). Soften that sentence before the JIRA comment is posted.
- FAIL, left open, gaps appended to `reports/skip-verdict-audit.md`: SOLR-16785, 16758, 16657, 16652, 16792, 16305.
- Also fixed the owner note at the head of `remediation-current.json` to match `pool/cron-prompt.md` (complaint 4).

DONE: 2026-10-19T16:05:17Z. Deliverables: `material/skip-audit/remediation-current.json` (four items cleared, note corrected), `reports/skip-verdict-audit.md` (six gap entries under "2026-10-19 remediation re-audit, batch 01 redos (vm3)"), `claims/pool-vm3-remediation-reaudit-batch-01.md`.

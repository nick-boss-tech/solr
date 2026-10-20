# Claim: skip-SOLR-10706-vm2

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: DONE (close-cannot-reproduce, changed from the recorded keep-open). Remediation item 1 of `assignments/pool-aaa-skip-verdict-remediation-11.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-11.md` (batch 11, item 1), claim `claims/pool-aaa-skip-verdict-remediation-11.md`.
- Slice: SOLR-10706 closeout, steps 11 to 15 of pool/cron-prompt.md.
- Capability tags used: review, draft.
- Started: 2026-10-20T11:42:17Z (UTC).
- Links: Ticket: SOLR-10706. Category: SolrJ client. Siblings: none known. Supersedes: the recorded keep-open verdict.
- Idempotency key: SKIP-SOLR-10706@498a09aea97aac86c2cc7ac114113b946d249942.

Heartbeat: 2026-10-20T11:42:17Z

Result: 2026-10-20T11:42:17Z: close-cannot-reproduce at pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Draft branch solr-10706-draft-close at 4a8b3488bd (drafts/SOLR-10706/record.md, jira-comment.md). Reproduction: java Enc.java on JDK 21 gives a+b for a space and %2B for a literal plus; SolrParams.java:415-429 is the only URLEncoder use in solrj.

DONE: 2026-10-20T11:42:17Z. Deliverables: drafts/SOLR-10706/record.md and jira-comment.md on solr-10706-draft-close (4a8b3488bd); material/skip-audit/queue-current.json (close-cannot-reproduce); line in reports/skip-verdict-remediation-11.md.

# Claim: skip-SOLR-10841-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T08:23:33Z. Remediation redo of batch 10, item 19, under `assignments/pool-aaa-skip-verdict-remediation-10.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-10.md` (item 19, SOLR-10841).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T08:22:40Z (UTC).

Heartbeat: 2026-10-20T08:22:40Z

Result: 2026-10-20T08:23:33Z: keep-open on solr-10841-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 77ae83f355, unchanged verdict; earlier record replaced). Jira read this pass: Open, no resolution, 8 comments; 2018-10-23 comment reports the failure again. Fix 39256e8348 (test-side retry for min RF) and a4f83f66ea (BadApples removal) ancestors (checked); retry present at TestTlogReplica.java:659, :703, :712. Broken-pipe cause not explained by product code. Repeated run not done.

DONE: 2026-10-20T08:23:33Z. Deliverables: drafts/SOLR-10841/record.md and jira-comment.md on solr-10841-draft-open (77ae83f355); queue-current set; reports/skip-verdict-remediation-10.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.

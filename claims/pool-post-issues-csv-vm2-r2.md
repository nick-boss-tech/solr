# Claim: pool-post-issues-csv-vm2-r2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE.
- Job or assignment: `assignments/pool-post-issues-csv-vm2.md`. This claim supersedes the BLOCKED claim `claims/pool-post-issues-csv-vm2.md`, which stays on the record.
- Owner decision: Nick Shanin, 2026-10-19, publish the full 18,364-row upload to this branch, not only the open rows.
- Slice: the whole assignment.
- Capability tags used: records.

Verification:

- Original file: `/workspace/uploads/solr-jira-metadata-2026-10-18-1792393324869.csv` on vm2.
- Copied to: `material/jira-issues-csv/solr-jira-metadata-2026-10-18-1792393324869.csv`, byte-identical (cmp).
- Data rows: 18364 (header excluded).
- Header: `IssueKey,Summary,IssueType,Priority,PRAvailable,Status,StatusCategory,Created,Updated,Labels,IssueUrl`.
- First key: SOLR-1. Last key: SOLR-18556.
- Size: 4140142 bytes.
- SHA-256: 5c2a97038a8a714412a41fb22c63893ef31a5d905569521e38a5c33a9732b60b.

The assignment's Status line still reads CLAIMED. That line is outside this claim's limits, so it is left for the next host that edits the assignment.

Heartbeat: 2026-10-19T16:30:00Z

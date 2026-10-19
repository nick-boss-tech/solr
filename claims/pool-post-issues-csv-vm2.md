# Claim: pool-post-issues-csv-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: BLOCKED (see the BLOCKED line below). Not copied; no DONE.
- Job or assignment: `assignments/pool-post-issues-csv-vm2.md`.
- Slice: the whole assignment (locate the CSV, verify it, copy it unchanged to `material/jira-issues-csv/`).
- Capability tags used: records.
- Started: 2026-10-19T16:00:45Z (UTC).

Search record (vm2, 2026-10-19T16:00Z to 16:10Z):

- Searched: `/home/claude`, `/tmp`, `/var/tmp`, `/mnt`, `/data`, `/opt`, `/srv`, `/root`, and all of `/workspace` (including `uploads/`, `downloads/`, `artifacts/`, `tools/`, `worktrees/`, `pr-edits/`, `pr-prepare/`), skipping `.git`, `node_modules`, `gradle-home`, `jdk`, and the solr checkout and gate directories in the `/workspace` pass. Search used `find -xdev` with a 100 s limit per root; the `/workspace` pass completed.
- Jira-keyed CSV found (only one): `/workspace/uploads/solr-jira-metadata-2026-10-18-1792393324869.csv`. Size 4140142 bytes. SHA-256 `5c2a97038a8a714412a41fb22c63893ef31a5d905569521e38a5c33a9732b60b`. Header: `IssueKey, Summary, IssueType, Priority, PRAvailable, Status, StatusCategory, Created, Updated, Labels, IssueUrl`. Data rows: 18364 (parsed with the csv module, 18364 unique keys, 0 ragged rows). First key SOLR-1, last key SOLR-18556.
- Other CSV files found are fixtures or tooling data, not Jira exports: `books.csv`, `books_numeric_ids.csv`, `films.csv`, `mappings.csv`, and `reports/test-sleep-audit/sleeps-apache-main.csv`.

Why BLOCKED: the assignment says the file has roughly 4,000 data rows. The only Jira export on disk has 18364 rows, about 4.6 times that. Nothing matched the stated size, so nothing was copied. The assignment also forbids a substitute export from Jira, so the file was not regenerated. The one question for Nick is in the assignment's Status line: is the 18364-row upload the file he sent, or is a different ~4,000-row file meant? If he confirms, a new claim copies that file unchanged.

BLOCKED: 2026-10-19T16:10:00Z. The claim stays on the record with no DONE line.

Heartbeat: 2026-10-19T16:00:45Z

Append one heartbeat line at least once an hour while the claim is active, and push it.

# Pool assignment: post the 4,000-issue CSV Nick sent vm2 to this branch

Status: CLAIMED (claims/pool-post-issues-csv-vm2.md)

- Capability tags: records.
- Intended host: vm2 only. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-19, at the direction of Nick. (Reposted the same day; the first posting was lost before it reached the branch tip.)

Nick sent vm2 a CSV file listing about 4,000 Solr Jira issues. The file is on vm2's local disk. It is not on this branch or any other branch, and the main side has verified that. This assignment is to put it on this branch so every host can work from it.

## Task

1. Claim this assignment as `claims/pool-post-issues-csv-vm2.md`.
2. Find the CSV on your disk: search your home directory and workspace for `.csv` files with roughly 4,000 data rows whose columns identify Jira issues (a key column with SOLR- keys). If more than one file matches, take the one Nick sent most recently and say in the claim how you chose.
3. Verify before copying: record the original file name and path, the data row count (excluding the header), the column header line, the first and last issue keys, the file size in bytes, and the SHA-256 of the file.
4. Copy the file unchanged to `material/jira-issues-csv/` on this branch, keeping its original file name. Do not edit, re-sort, filter, or regenerate its contents.
5. Mark the claim DONE with the verification details from step 3 and the path the file now has on this branch, and push.

If no matching CSV exists on your disk, mark the claim BLOCKED instead: state the directories you searched and the CSV files you did find, with their row counts. Do not create a substitute export from Jira; the point is to publish the exact file Nick sent.

## Deliverable

- `material/jira-issues-csv/<original file name>`, byte-identical to the file on vm2's disk (SHA-256 in the claim must match the committed file).
- `claims/pool-post-issues-csv-vm2.md` marked DONE (or BLOCKED with the search record).

## Limits

- Change nothing else on this branch beyond the claim, the copied CSV, and the regenerated OPEN-WORK.md if the posting tooling requires it.
- No posts to Jira or GitHub, no PRs, no force-push.

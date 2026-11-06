# Monthly index contract

Import inputs are the existing `open-prs.json`, `merged-prs.json` and `scan-register.csv` from one campaign. Raw GitHub JSON timestamps are preferred over the localized dates in the campaign CSV. Input bytes are hashed and checked again after reading to detect a concurrent update; this is not a claim that the historical collection was atomic.

Each PR belongs to one month bucket: merged records use `mergedAt` converted to UTC; currently Open/Draft records use `createdAt` converted to UTC. Open and Draft counts describe the captured backlog by creation month. They are not counts of all PRs opened that month and not update/review event counts. The cohort excludes closed-unmerged PRs and can omit open PRs created before its window; always state this boundary. November is a partial captured month in the initial package.

Capture metadata consists of a UTC capture timestamp, window-start date, cohort definition and source hashes. The normalized snapshot carries `head_sha`, `scan_head`, scan status and original record locators. A complete scan at another head is stale for this captured record. A completed matching scan is source-review coverage only; it is not runtime proof or proof that a merged change remains unchanged on current main.

The importer rejects duplicate PR numbers, mismatched input states, missing/invalid timestamps, future timestamps beyond capture, records outside the supplied cohort, duplicate scan rows and malformed SHAs. Unmatched scan rows are reported, not silently counted as reviewed PRs. Do not mix differently timed live queries into a pinned input without creating a new snapshot.

Reproduction commands use the same script and normalized snapshot:

```sh
python monthly_index.py --snapshot snapshot.json --output a-new-directory
```

Output is deterministic for a snapshot. Refreshing creates a new snapshot and output directory; retain the original. The script uses only the Python standard library and makes no network calls.

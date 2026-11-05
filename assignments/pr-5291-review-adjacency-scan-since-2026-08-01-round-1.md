# Pool assignment: PR 5291 review followed by adjacency scan, since 2026-08-01

Status: OPEN

- Capability tags: review, records.
- Category: existing PR follow-up.
- Intended host: any available host with `review` and `records`; no build or gate capability is required. Claim each item or scan slice under `claims/` before starting, per `WORKFLOW.md`.
- Posted by: phone-agent-2, 2026-11-04, at the direction of Nick Shanin.

## Context and snapshot

The review target is upstream [apache/solr#5291](https://github.com/apache/solr/pull/5291), `SOLR-18621: Add SolrParams.of(String...) and ModifiableSolrParams.of(String...)`. At assignment posting it is open at head `600e0ec4afd82004a94852c419892897be14e6f0`, based on `apache/solr:main` at `82abb86bb5b7326fe48c117e4de69981a43e4251`. Re-read the live PR and record its state, head, base and UTC check time before reviewing. Review only the public upstream PR diff. No unpushed local changes are part of this target.

The adjacency census is for Apache Solr PRs through 2026-11-04 inclusive. Initial API census: 604 unique eligible PRs, comprising 177 currently open PRs created since 2026-08-01 and 427 PRs merged from 2026-08-01 through 2026-11-04, including PRs opened before August. Refresh and reconcile the set from the pull-request API at the start of each scan slice, deduplicate by PR number, and record the actual snapshot time and counts. If API search results disagree with pull-request records, verify the individual PR records rather than silently omitting an item.

## Items

1. **Review upstream PR #5291.** Re-read the live PR, full diff, changed code and tests, relevant history, and current review/check context. Assess API behavior and compatibility, including empty and odd-length inputs, duplicate names, null handling, immutable versus modifiable results, overload resolution, migrated helper callers, and regression coverage. Give each finding a severity and cite exact files/lines plus evidence; distinguish a defect from a question or preference. Run a focused check only if practical and safe, and record any checks not run. Do not modify the PR branch. Start from the live [PR page and diff](https://github.com/apache/solr/pull/5291). Claim: `claims/pr-5291-review-round-1-<host>.md`. Output: `reports/pr-5291-review-round-1.md`.

2. **Scan older-opened PRs merged since August.** After item 1's report exists, inventory every PR opened before 2026-08-01 and merged from 2026-08-01 through 2026-11-04 inclusive. Compare each PR's changed paths and patch content with the live #5291 review target; inspect candidate diffs for file, symbol, API-contract, caller, test or behavioral adjacency. Include a per-PR disposition, not just a list of possible matches. Claim this slice only. Start from the paginated Apache Solr PR records and the #5291 report. Claim: `claims/pr-5291-adjacency-pre-aug-merged-round-1-<host>.md`. Output: `reports/pr-5291-adjacency-pre-aug-merged-round-1.md`.

3. **Scan PRs created in August.** After item 1's report exists, scan PRs created 2026-08-01 through 2026-08-31 inclusive that were open at the census or merged by 2026-11-04. Use the same adjacency criteria and per-PR disposition as item 2. Claim only this date slice. Start from the paginated Apache Solr PR records and the #5291 report. Claim: `claims/pr-5291-adjacency-2026-08-round-1-<host>.md`. Output: `reports/pr-5291-adjacency-2026-08-round-1.md`.

4. **Scan PRs created in September.** After item 1's report exists, scan PRs created 2026-09-01 through 2026-09-30 inclusive that were open at the census or merged by 2026-11-04. Use the same adjacency criteria and per-PR disposition as item 2. Claim only this date slice. Start from the paginated Apache Solr PR records and the #5291 report. Claim: `claims/pr-5291-adjacency-2026-09-round-1-<host>.md`. Output: `reports/pr-5291-adjacency-2026-09-round-1.md`.

5. **Scan PRs created October 1 through October 15.** After item 1's report exists, scan PRs created 2026-10-01 through 2026-10-15 inclusive that were open at the census or merged by 2026-11-04. Use the same adjacency criteria and per-PR disposition as item 2. Claim only this date slice. Start from the paginated Apache Solr PR records and the #5291 report. Claim: `claims/pr-5291-adjacency-2026-10-01-15-round-1-<host>.md`. Output: `reports/pr-5291-adjacency-2026-10-01-15-round-1.md`.

6. **Scan PRs created October 16 through October 31.** After item 1's report exists, scan PRs created 2026-10-16 through 2026-10-31 inclusive that were open at the census or merged by 2026-11-04. Use the same adjacency criteria and per-PR disposition as item 2. Claim only this date slice. Start from the paginated Apache Solr PR records and the #5291 report. Claim: `claims/pr-5291-adjacency-2026-10-16-31-round-1-<host>.md`. Output: `reports/pr-5291-adjacency-2026-10-16-31-round-1.md`.

7. **Scan PRs created in November through November 4.** After item 1's report exists, scan PRs created 2026-11-01 through 2026-11-04 inclusive that were open at the census or merged by 2026-11-04. Include #5291 in the inventory as the anchor, but do not report it as adjacent to itself. Use the same adjacency criteria and per-PR disposition as item 2. Claim only this date slice. Start from the paginated Apache Solr PR records and the #5291 report. Claim: `claims/pr-5291-adjacency-2026-11-01-04-round-1-<host>.md`. Output: `reports/pr-5291-adjacency-2026-11-01-04-round-1.md`.

8. **Synthesize the review and completed adjacency scan.** After item 1 and all six scan reports exist, reconcile their coverage, deduplicate PRs, summarize the review verdict and all substantiated adjacent PRs, and state the as-of time and any gaps. Do not elevate a same-module match without evidence of a changed-path, symbol, API, caller, test or behavioral connection. Start from the seven reports above. Claim: `claims/pr-5291-adjacency-synthesis-round-1-<host>.md`. Output: `reports/pr-5291-adjacency-since-2026-08-01-round-1.md`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Scan inclusion | decided: current open PRs created on/after 2026-08-01, plus PRs merged from 2026-08-01 through 2026-11-04 inclusive, regardless of creation date | Deduplicate by PR number; exclude closed-unmerged PRs; refresh and record the census before scanning | Nick Shanin |
| Scan sequence | decided: finish item 1 before any scan slice; finish all six slices before item 8 | Items 2-7 may run in parallel only after item 1's report is posted; item 8 waits for all six | Nick Shanin |
| Adjacency test | decided: exact changed-path overlap or evidence of a shared symbol, API contract, caller, test or behavior | Shared module alone is not adjacency; inspect candidate diffs and cite the connection | Nick Shanin |
| Local changes | decided: public upstream PR only | Do not treat, push, or mix any unpushed local working-tree changes into the target review or scan | Nick Shanin |
| External actions | decided: read-only review and reporting | No source edits, PR comments or reviews, PR opening, merge, Jira entry, or other public action; report findings on the board only | Nick Shanin |

## Deliverable

`reports/pr-5291-adjacency-since-2026-08-01-round-1.md`: one section per item with status, report path, exact PR coverage counts and snapshot time, review verdict, adjacent PRs with evidence, and any limits or gaps. Each scan report must list every PR in its slice with number, title, status, relevant date, changed-path overlap result and a concise adjacency disposition; analyze candidate diffs rather than relying on titles alone.

## Limits

- No code changes or pushes to `apache/solr` or any contributor branch. Exclude all unpushed local work from review evidence; do not publish it or treat it as part of #5291.
- Do not post a PR comment or formal review, open or merge a PR, or write to Jira. A public action requires Nick's separate approval.
- Use paginated GitHub data and record the query boundary, census time, PR numbers covered, and any API failures or omissions. Resolve API/search-index mismatches against the pull-request records.
- Findings belong in the board reports with exact evidence. No claim may call a test verified unless the test ran and its result is recorded.
- Commit as Nick Shanin with no trailers. No em dashes.

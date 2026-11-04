# PR 5289 packaged README correction and gate handoff

Status: IMPLEMENTED, PROOF PENDING. Source review finding corrected; Linux execution not claimed or performed by this Windows host.

- PR: https://github.com/apache/solr/pull/5289 (draft).
- Fork branch: `nick-boss-tech/solr:solr-18169-slim-no-examples`.
- Previous head: `a8d2cfb124f584395478124e15c334c435b3119e`.
- New pushed head: `f2166af983d2536d51e4b7ba3bc875e89cbdc1bd`.
- Commit: `SOLR-18169: clarify example availability in packaged README`.
- Exact outbound file for this commit: `solr/packaging/README.txt`; four insertions and two deletions. The full PR now changes seven files.
- Review finding: the README copied into slim still said examples were included and recommended techproducts. It now qualifies the instructions as requiring full, states slim omits examples, and marks `example/` excluded from slim alongside the existing module exclusions.
- No production or test changes after a8d2cfb1. Static diff whitespace checks passed. No Gradle, tests or startup ran on Windows.
- User explicitly authorized both the branch patch and PR body edit on 2026-11-04.
- PR body updated at `2026-11-04T17:23:57Z`. Separate read-back at `2026-11-04T17:24:29Z` confirmed exact equality to the prepared draft, unchanged head f2166af9, and unchanged conversation/review summaries. Inline comments were refreshed before publication and remained the same two comments.
- Body describes the final packaging/CLI/tests/docs implementation, names the current head and pending proof, and explicitly limits the older f5a5480 results to that earlier head. AI header/footer retained. No review reply, JIRA comment or PR state change was made.
- Superseding gate: `gates/SOLR-18169-reviewer-followup-f2166af9.md`, posted and verified at board commit `1860a3c2150259e73db330c08e386a2467f62e48`.
- New gate supersedes a8d2cfb1 and 4f3346a3 jobs. It retains required focused positive/negative proof, tidy, distribution assembly, Error Prone/core check, slim CLI startup, and standalone Ref Guide checks. It adds installed README inspections in both distributions and the existing neighboring picocli suite, while recording that the new inherited validation tests bypass parser-specific invocation.
- Execution receipt: `receipts/SOLR-18169-reviewer-followup-f2166af9.md`. No receipt exists or is claimed by this report. Nick will take the branch to the gate.

The remaining requirement is Linux proof at this exact pushed head. Earlier green receipts do not establish that proof. A gate repair changing the source head requires a superseding gate before accepting results.

# Pool assignment: independent review of three PR candidates, round 1

Status: OPEN

Priority: Owner-directed follow-up. The TOP Blocker and Critical review assignment remains ahead for pool Linux hosts. A Windows review host may claim this work now; a Linux host may claim it only when no unclaimed unit remains available to that host in the TOP assignment.

- Capability tags: review.
- Category: Cross-category review. Per-item categories are listed below.
- Intended host: any pool host with review capability other than phone-agent-2, which prepared these branches. Claim one ticket at a time under `claims/` before starting; first push wins.
- Posted by: phone-agent-2, 2026-10-22, at the direction of Nick Shanin.

These are branch candidates, not open pull requests. A live GitHub lookup found no pull request from any of the three branches at posting time. The implementation receipts are already posted at `receipts/`; read each newest GREEN section and the preserved earlier attempts before reviewing. The hashes below were confirmed against the live fork tips immediately before posting.

## Items

1. **SOLR-17840, LTR partial results after Lucene 10 upgrade.** Review branch `solr-17840-preserve-partial-results-phone-agent-2` at `21bf1dbb6ea2365cb3be7caab74ea853af74dd0d`. Receipt: `receipts/SOLR-17840.md`. Category: Search and query components, secondary Test quality. Siblings: SOLR-17631 and QueryComponent work SOLR-18539 and SOLR-18541; the receipt says these sibling changes are already in base or use different paths. Supersedes: none; review only, no implementation or receipt change. Claim: `claims/pool-pr-candidates-review-round-1-solr-17840.md`. Start with the current GREEN section and inspect the older failed candidate and boundary evidence below it. Output: `reports/pr-candidate-review-round-1/SOLR-17840.md`.
2. **SOLR-17844, TestFieldCacheSortRandom failures.** Review branch `solr-17844-restore-sort-assertions-phone-agent-2` at `08ecc90ff7b5505e576985c8dcb514393314ce2b`. Receipt: `receipts/SOLR-17844.md`. Category: Test quality, secondary Search and query components. Siblings: SOLR-17631 and SOLR-17840; the receipt records no shared changed files. Supersedes: none; review only, no implementation or receipt change. Claim: `claims/pool-pr-candidates-review-round-1-solr-17844.md`. Start with the current GREEN rework section and inspect the earlier failed oracle attempt below it. Output: `reports/pr-candidate-review-round-1/SOLR-17844.md`.
3. **SOLR-18446, path-scoped collection routing.** Review branch `solr-18446-path-routing-phone-agent-2` at `8bb60e6f61abc9827fb58dcae1863cabb33f9796`. Receipt: `receipts/SOLR-18446.md`. Category: SolrCloud and ZooKeeper, secondary Search and query components. Sibling: SOLR-18470, explicitly excluded in the receipt. Supersedes: none; review only, no implementation or receipt change. Claim: `claims/pool-pr-candidates-review-round-1-solr-18446.md`. Start with the current GREEN follow-up section and inspect the earlier failed gate and its limits below it. Output: `reports/pr-candidate-review-round-1/SOLR-18446.md`.

## Review process

For each ticket, confirm the live fork head with `git ls-remote` before starting, read the current public Jira issue and the receipt, inspect the diff against its recorded base, and review the changed code and tests. Check that the implementation matches the ticket, does not broaden behavior unnecessarily, follows surrounding code, and that the tests cover the claimed behavior. Check relevant linked work and sibling surfaces. Read the historical receipt sections too; they explain why earlier candidate heads were not ready.

Write a standalone report for each ticket. State the exact head and base reviewed, the receipt section used, evidence checked, and a verdict. Cite precise file and line locations for every finding, give its severity, and pose an exact review question when one is needed. Distinguish code review from test evidence: a green module check is not the full test suite, and a green suite does not by itself prove the fix is covered.

Verdict rule: use `CLEAR FOR NICK'S PR DECISION` only when there is no blocker or major finding, the live head matches the reviewed head, and its current receipt is GREEN at that exact head. Otherwise use `HOLD FOR FOLLOW-UP` and name the specific finding or missing evidence. Minor findings may be listed without blocking the first verdict. This is a review recommendation only, not permission to open a PR or change its state.

If a branch moved before review, review the live tip and record the move. If its receipt is not GREEN at that exact tip, use `HOLD FOR FOLLOW-UP`; do not refresh a receipt or request a gate from this assignment.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Unit of work | decided: one ticket per claim and report | A host may claim another ticket after closing its current claim; do not create duplicate live claims | Nick Shanin |
| Candidate head changes | decided: review the live fork tip and compare it with the receipt | If receipt head differs, report the exact difference and use `HOLD FOR FOLLOW-UP` | Nick Shanin |
| Review finds a blocker or major concern | decided: do not repair in this assignment | Record severity, file and line, reasoning, and the exact question or bounded next action in the report | Nick Shanin |
| Missing or ambiguous evidence | decided: do not infer it | Name the missing evidence and use `HOLD FOR FOLLOW-UP` | Nick Shanin |
| Linux host priority | decided: the TOP Blocker and Critical review assignment stays first | Linux hosts claim this assignment only when no unclaimed unit remains available to them in the TOP assignment; Windows may claim this review now | Nick Shanin |

## Deliverable

Three reports under `reports/pr-candidate-review-round-1/`, one per ticket, with the verdict, exact reviewed head and base, receipt section used, category, siblings, supersession status, evidence checked, findings with precise file and line pointers, and the next step if held. Each claim is marked DONE in the same push as its report.

## Limits

- Read-only review. No source edits, product branch pushes, new test runs, Gradle builds, receipt edits, or new gate jobs.
- No PR opening, PR comment, PR state change, ready flip, Jira post or comment, or email.
- Do not describe a candidate as submitted, merged, or approved. The review only informs Nick's later PR decision.
- Commit as Nick Shanin with no trailers. No em dashes.

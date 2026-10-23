# Pool assignment: SOLR-10928 request hardening
Status: DONE
- Capability tags: implementation, review, settling-run.
- Category: Search and query components.
- Intended capability: Linux implementation and tests; claimed by phone-agent-2.
- Posted at Nick Shanin's direction on 2026-10-23.
- Product branch: nick-boss-tech/solr:solr-10928-submit.
- Starting head: 1ff57c1b21c22031513f733b641496b77057ea55.
- Base: b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb.

## Items
1. Add full embedded request coverage beyond the helper-only tests, including search-query independence, request/local-parser precedence and fallback, and bounded interaction cases. Clarify the parameter documentation. Inspect adjacent tests and run an independent read-only coverage review.
2. Validate formatting, Error Prone, discriminating proof mutations with archived XML, request and adjacent elevation suites across multiple seeds, and core check. Report exact resulting head, evidence, and remaining limits in reports/solr-10928-request-hardening.md. Request a separate full gate if required by the workflow; do not claim a gate as the implementation host.

## Decisions
| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Coverage and docs | decided: strengthen on existing submission branch | Preserve production behavior unless a reproducible defect requires a scoped repair | Nick Shanin, direct instruction |
| New test fails | investigate before retry | Correct a demonstrated implementation/test defect with recorded evidence; no unchanged-tree retries | Standing workflow |
| Branch moved | stop product push | Record the new tip and do not overwrite it | Standing workflow |
| Public actions | prohibited | No PR, comments, Jira, or PR title/body edits | Nick Shanin, direct instruction |

## Deliverable
reports/solr-10928-request-hardening.md, committed tests/docs on solr-10928-submit, preserved local validation evidence, and any new gate request.

## Limits
Fast-forward pushes only. Commit as Nick Shanin without trailers. No unrelated scope or new dependencies. Preserve completed prior gate records without editing terminal claims.

## Implementation handoff
Head d5621a29281f28659db7b9e9ca57cc46c2651f53 was fast-forward pushed and read back. Three files changed from the previous head: 183 insertions, 3 deletions. No production source changed. Two independent read-only reviews found no concrete test or implementation defects. Independent gate session is next actor, under gates/SOLR-10928-phase-request-hardening.md. Final report remains reports/solr-10928-request-hardening.md.

## Final result
DONE 2026-10-23T04:14:10Z. Final branch head `f2001e640a464e8e861ee3c202821b6dcc4bd497` is fast-forward pushed and validation is GREEN. Report: reports/solr-10928-request-hardening.md. Receipt: receipts/SOLR-10928.md. Gate and gate claim complete in the same atomic closeout push. Exact counts: 39 passed on each of two seeds; two override proof failures and one fallback proof failure as expected; Error Prone, tidy, YAML, core check and root check excluding tests passed. Full-suite and exhaustive combination limits are stated in the report.

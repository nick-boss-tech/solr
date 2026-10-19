# Skip verdict remediation, batch 02

Assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`. Pinned head `498a09aea97aac86c2cc7ac114113b946d249942`. One line per item, in the order listed.

| # | Ticket | Claim | Verdict recorded | Draft branch and head | Evidence cited |
| --- | --- | --- | --- | --- | --- |
| 1 | SOLR-16253 | `claims/skip-SOLR-16253-vm2.md` | pr-needed (was keep-open) | `solr-16253-draft-pr` at e68d982f25 (was `solr-16253-draft-open`) | JIRA read 2026-10-19: Open, no resolution. Code path SpellCheckCollator.java:111-121 and :152-156, QueryComponent.java:223-226. Reproduced twice at the pinned head (test run, log `/workspace/gates/logs/vm2/16253/16253-repro-r1.log` and `-r2.log`). Record `drafts/SOLR-16253/record.md`. |

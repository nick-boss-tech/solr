# Skip verdict remediation, batch 02

Assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`. Pinned head `498a09aea97aac86c2cc7ac114113b946d249942`. One line per item, in the order listed.

| # | Ticket | Claim | Verdict recorded | Draft branch and head | Evidence cited |
| --- | --- | --- | --- | --- | --- |
| 1 | SOLR-16253 | `claims/skip-SOLR-16253-vm2.md` | pr-needed (was keep-open) | `solr-16253-draft-pr` at e68d982f25 (was `solr-16253-draft-open`) | JIRA read 2026-10-19: Open, no resolution. Code path SpellCheckCollator.java:111-121 and :152-156, QueryComponent.java:223-226. Reproduced twice at the pinned head (test run, log `/workspace/gates/logs/vm2/16253/16253-repro-r1.log` and `-r2.log`). Record `drafts/SOLR-16253/record.md`. |
| 2 | SOLR-16200 | `claims/skip-SOLR-16200-vm2.md` | keep-open (unchanged) | `solr-16200-draft-open` at ec3643f2a6 (fast-forward from 618aec2fbf) | JIRA read 2026-10-19: Open, no resolution. Code path jetty.xml:70-74 (forwarded-headers block commented out). Not reproduced: needs the packaged server behind a proxy. Record `drafts/SOLR-16200/record.md`. |
| 3 | SOLR-16167 | `claims/skip-SOLR-16167-vm2.md` | keep-open (unchanged) | `solr-16167-draft-open` at 023c029736 (fast-forward from dc6b615295) | JIRA read 2026-10-19: Open, updated 2025-10-17. Code path gradle/validation/error-prone.gradle:304 (ClassInitializationDeadlock commented out). SOLR-16165 fix ccc5db9fd04 is an ancestor. Review not finished; not reproduced. Record `drafts/SOLR-16167/record.md`. |

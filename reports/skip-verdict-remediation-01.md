# Skip verdict remediation, batch 01

Assignment: `assignments/pool-aaa-skip-verdict-remediation-01.md`. Pinned head `498a09aea97aac86c2cc7ac114113b946d249942`. One line per item, in the order listed.

| # | Ticket | Claim | Verdict recorded | Draft branch and head | Evidence cited |
| --- | --- | --- | --- | --- | --- |
| 1 | SOLR-17820 | `claims/skip-SOLR-17820-vm4.md` | pr-needed (was keep-open) | `solr-17820-draft-pr` at 45df914487 (was `solr-17820-draft-open`) | JIRA read 2026-10-19: Resolved, Fixed. Code path `ClusterStatus.java:358` (health) before `:375` (live-node check). Upstream fix 7e3b175021 is not an ancestor of the pin. Not reproduced. |
| 2 | SOLR-17763 | `claims/skip-SOLR-17763-vm4.md` | close-fixed (unchanged) | `solr-17763-draft-close` at 879cb28f58 (fast-forward from 1f97dd8f7e) | JIRA read 2026-10-19: Open. Fix 1177796e32 (SOLR-17118) is an ancestor of the pin; the latch wait it removed is absent at the pin (`git grep` empty). Not reproduced. |

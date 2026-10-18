# 2026-10-16 review follow-ups, code and tests (round 2): index

Host: vm2. Claim: `claims/pool-review-2026-10-16-code-round-2.md`. Assignment: `assignments/pool-review-2026-10-16-code-round-2.md`.

| Item | PR | Fork branch and head | Status | Check file |
| --- | --- | --- | --- | --- |
| 1 | 5134 SOLR-6831 | `solr-6831-submit` moved to `5c1124bdef95`; assignment head `4afed04987b2343af58261b83c4b49fda0af45c0` | escalated: stopped at the moved head, not run | `SOLR-6831-round-2-check.md` |
| 2 | 5147 SOLR-9124 | `solr-9124-submit` `3dd985b93a` | confirmed: distributed classes green at head; removing the within-group clause fails `TestExactStatsCache.test` (checkWithinGroupOrder) | `SOLR-9124-round-2-check.md` |
| 3 | 5117 SOLR-18541 | `combinedquerycomponent-merge-totals` `81a88e40dd` | confirmed: short per-query list throws IndexOutOfBoundsException at `CombinedQueryComponent.java:342`, before the skip at line 393; fix escalated to the main agent | `SOLR-18541-check.md` (test patch `SOLR-18541-shortlist-test.patch`) |
| 4 | 5118 SOLR-18010 | `solr-18010-submit` `571307400b` | confirmed: persistConf needs write access to SOLR_HOME (AccessDeniedException on the temp file); new file mode 0644 under umask 0022; escalated to Nick | `SOLR-18010-check.md` (test patch `SOLR-18010-scratch-test.patch`) |

No PR, comment, JIRA or email was posted. No fork branch was pushed. No production code changed.

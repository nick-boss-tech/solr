# Skip verdict remediation, batch 04

Assignment: `assignments/pool-aaa-skip-verdict-remediation-04.md`. Pinned head `498a09aea97aac86c2cc7ac114113b946d249942`. One line per item, in the order listed.

| # | Ticket | Claim | Verdict recorded | Draft branch and head | Evidence cited |
| --- | --- | --- | --- | --- | --- |
| 1 | SOLR-15023 | `claims/skip-SOLR-15023-vm2.md` | keep-open (unchanged) | `solr-15023-draft-open` at ebc6761559 (fast-forward from 07fafaab60) | JIRA read 2026-10-19: Open, one comment. AdminHandlerProxy absent at pin (grep 0); removal commit not identified. MetricsHandler.java present; not timed on 200 collections. Record `drafts/SOLR-15023/record.md`. |
| 2 | SOLR-14966 | `claims/skip-SOLR-14966-vm2.md` | keep-open (was close-fixed; verdict changed) | `solr-14966-draft-open` at dfb26c4f72 (new branch; `solr-14966-draft-close` not updated) | JIRA read 2026-10-19: Open, two comments. stateVer check uses uncached overload: ZkStateReader.java:538, ClusterState.java:128-144. No fixing commit on that path. Read counts not measured. Record `drafts/SOLR-14966/record.md`. |
| 3 | SOLR-14881 | `claims/skip-SOLR-14881-vm2.md` | close-fixed (was keep-open; verdict changed) | `solr-14881-draft-close` at 8ffbc4f7e2 (new branch; `solr-14881-draft-open` not updated) | JIRA read 2026-10-19: Open, two comments linking fix 73c06e35e8b (ancestor of pin; line CoreContainer.java:2060 present). One passing run of ManagedSchemaRoundRobinCloudTest at pin. Record `drafts/SOLR-14881/record.md`. |

# Claim: SOLR-18628 idle h2c streamed update implementation, Windows round 1

- Source of work: OPEN assignment `assignments/solr-18628-httpjdk-h2c-idle-round-1.md`, board snapshot `c517092a1b87140c40bd7b3d2cb5a83b731bd10f`; no competing claim exists.
- Claimant: windows, Codex desktop; tags implementation, review, records.
- Started: 2026-11-06T07:10:21Z.
- Base: `upstream/main` at `c0c219e374611e0edc475c0f793c9bd688b13292`, fetched and verified.
- Scope: reproduce the cleartext HTTP/2 idle-expiry failure in `HttpJdkSolrClientTest`, use a fail-before result to verify the cause, and try the narrowest safe client fix. Keep the HTTP/1.1 control and assess extra request overhead if the fix re-probes HEAD.
- Branch/worktree: `solr-18628-idle-h2c-update` in `wt/SOLR-18628`; fork branch `solr-18628-submit`.
- Deliverables: focused test and justified fix, changelog fragment if behavior changes, pushed branch, exact-head Linux gate, and report with code/history/test evidence.
- Decision ladder: rung 1 shows the current code caches a successful HEAD result by base URI for the client lifetime before streamed cleartext updates. That supports the reporter's hypothesis but does not prove Jetty or the JDK reset path. Rung 2 is required: run the focused fail-before test on JDK 25 with a shortened JDK keepalive timeout, then run the same test after the fix.
- Verification: Windows will not run Gradle or the local test queue. Linux gate will use JDK 21 for Gradle and JDK 25 for the SolrJ test runtime; proof remains pending until the receipt covers the exact pushed head.
- Public communication: SOLR-18628 creation was explicitly authorized. No JIRA comment, GitHub comment, or PR is authorized by this claim.
- Status: ACTIVE; Windows implementation in progress, Linux proof pending.

# Claim: SOLR-11356 premise run then first gate (round 2, job 5)

Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-2.md. Job: gates/SOLR-11356.md.
Capability tags: gate, premise-run. Staffing: 1.
Branch: solr-11356-submit at 8474e5a3a26d8aacd25d9fc9838c627bc6c5b14f (live tip, verified by ls-remote at claim time). Proof base: cabedd1d968059215188f4e7563fb303241899ed (branch sits on it; origin/main merge-base e044bf20b405 not used).
Started: 2026-10-11T03:25:23Z (UTC).

Heartbeat: 2026-10-11T03:25:23Z.

Premise run 2026-10-11T03:43:03Z: PREMISE HOLDS. Base (ConcurrentUpdateJettySolrClient at cabedd1d96, branch tests kept) fails testRequestsWithDifferentCredentialsAreNotSentOnOneStream at ConcurrentUpdateSolrClientTestBase.java:328 ("document of bob sent under another identity"); head (8474e5a3a2) tests=12 failures=0.

Result 2026-10-11T03:50:44Z: GATE GREEN at packaged head 6120dae28d04a07763f44d4bc5e9d9793af025f6. Changelog OK, tidy clean, compileTestJava rc 0, ConcurrentUpdateJettySolrClientTest tests=12 failures=0 at head, :solr:solrj-jetty:check -x test rc 0. Proof base: cabedd1d96, not e044bf20b405 (see gates/SOLR-11356.md).
DONE: 2026-10-11T03:50:44Z (UTC), outcome GREEN.

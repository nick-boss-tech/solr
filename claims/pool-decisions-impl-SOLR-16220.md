# Claim: pool-decisions-impl-SOLR-16220 (vm5)

- Claimant host: vm5, matching a file in `hosts/`.
- Status: STOPPED 2026-10-20T11:43:17Z (not implementable as written at the pin). Row 4 of `assignments/pool-owner-decisions-round-1-implementation.md`.
- Evidence: the answer (clear error for an explicit non-SSL SSLConfig paired with an https URL) assumes a solrj SSLConfig option. At the pin, `git grep -i "sslConfig"` over solr/solrj/src/java returns no hits, and the only SSL configuration type is `SSLConfigurations` (solr/core/src/java/org/apache/solr/util/configuration/SSLConfigurations.java), which has no "explicit non-SSL" case. The decisions file (assignments/pool-vm3-owner-decisions-round-1.md, row 5) also records that the documented case does not reproduce on base.
- Action: stopped, no branch. The assignment says to record the evidence instead of improvising a different behavior. This is recorded as the claim's result; the owner's answer is not changed.
- Started: 2026-10-20T11:43:17Z (UTC).

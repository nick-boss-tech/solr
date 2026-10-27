# Failure verdict: BATS assertion-hardening round 1 — phone-agent-2

Claim named: `claims/bats-assertion-hardening-round-1-phone-agent-2.md`.

**Verdict: INCOMPLETE / handed back.** The final-head JDK 21 and JDK 25 legacy runs each recorded four TLS failures and one known skip. Group 4 remains incomplete; Group 3 still lacks the old-base and missing-`numFound` response comparisons; the two Picocli matrix legs and final required checks were not run. `test_rolling_upgrade.bats` is UNRUNNABLE on current hosts because Docker is unavailable, as recorded in Nick's amendment.

Candidate: `nick-boss-tech/solr:solr-bats-assertion-hardening-submit` at `4d5d876d946a4f5ac174a7f68e16f4dd9b5f9f68` (clean at postrun). The detailed evidence handback is `reports/bats-assertion-hardening-finish-round-1-phone-agent-2.md`.

This is a claim-status record only. The shared gate remains OPEN / INCOMPLETE; no GREEN receipt or pass claim was added. The original claim and gate files were not edited.
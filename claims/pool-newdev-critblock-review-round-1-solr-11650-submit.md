# Claim: pool-newdev-critblock-review-round-1, unit 7 (SOLR-11650)

- Claimant host: vm5, matching a file in `hosts/`.
- Assignment: `assignments/pool-newdev-critblock-review-round-1.md`, unit 7, Critical.
- Branch: `solr-11650-submit`, head `e4f5e941cd8e88524196b03f2022261c2215c3ba` (ls-remote 2026-10-20T14:46Z). Receipt: receipts/SOLR-11650.md.
- Capability tags: review, gate.
- Status: CLAIMED. Three review passes, each with a regate.
- Started: 2026-10-20T14:46:00Z (UTC).

Heartbeat: 2026-10-20T14:46:00Z

HANDED BACK: 2026-10-20T14:58:00Z (vm5), pass 1 of 3. Head e4f5e941cd. Reason: redaction fails open for a raw / or ? in a password (solrj URLUtil.redactUserInfo); the password fragment reaches the IndexFetcher FORBIDDEN and malformed messages. Gate on the head: compile rc=0; IndexFetcherLeaderUrlRedactionTest 2/2; URLUtilTest 18/18. Returned to implementation; not repaired in this review round. Records: reports/newdev-critblock-review-round-1/solr-11650-submit.md, gates/SOLR-11650-review-pass-1.md, receipts/SOLR-11650.md (pass 1 section).

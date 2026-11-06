# Claim verdict: SOLR-18602 alias opt-out control test, round 2

Status: DONE — GREEN at the exact Nick fork candidate F2 `2efbd9884eac22cc8ddd0bdad2f8715f800eebee`.

- Claim: `claims/pool-phone-agent-2-gate-pr-5259-solr-18602-alias-optout-control-test-round-2.md`.
- Gate: `gates/pr-5259-solr-18602-alias-optout-control-test-round-2.md`.
- Receipt: `receipts/pr-5259-solr-18602-alias-optout-control-test-round-2.md`.
- Evidence archive: `receipts/pr-5259-solr-18602-alias-optout-control-test-round-2-evidence/round2-evidence.tar.gz`.
- The exact-F2 tidy preflight and standalone root check passed; baseline H and implementation F2 focused suites passed 8/8 and 9/9 with zero failures/errors/skips; the isolated named-test mutation produced the required single expected `SolrException` assertion failure.
- The synthetic mutation was discarded after its patch, log, fresh XML, hash, and monitor were preserved. No synthetic change was committed or pushed.
- This verdict is for the Nick fork candidate branch only. Public PR #5259 remains open at H on `sarkaramrit1993/solr:deletereplica-count-alias-18602`; no PR content or public comment was changed.
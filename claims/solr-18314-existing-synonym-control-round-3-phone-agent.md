# Claim: SOLR-18314 existing synonym control gate, round 3, phone agent

Status: CLAIMED

- Job: `gates/solr-18314-existing-synonym-control-round-3.md` (Status: OPEN; intended host: capable Linux CPU gate host with JDK 21).
- Branch: `nick-boss-tech/solr:solr-18314-edismax-empty-phrase` at E3 `1ea8ceb2d5253fef6c2e05a3ee800687dea700ba`, verified live by fetch at claim time. E2 `9b4bf9276515187b3d29a39a0923f644e1b457d2` and P `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a` (PR 5294 head) are ancestors of E3. The E2-to-E3 diff is exactly `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java`, +11/-3, test only.
- No competing Linux claim on the board at claim time. The Windows claim (`claims/solr-18314-existing-synonym-control-round-3-windows.md`) covers the E3 implementation only and ran no Gradle, compilation, or tests.
- Execution: all five legs of the gate on this host at seed `18314E`, one Gradle build at a time under the test-queue lock; the root check leg runs in a normal clone at the verified head, not a worktree. Logs and fresh JUnit XML are preserved per leg.
- Receipt: `receipts/SOLR-18314-existing-synonym-control-round-3.md`. The round-1 failed receipt stays untouched as history.
- No PR, PR-body edit, public comment, Jira write, upstream push, or force push is authorized or taken by this claim.

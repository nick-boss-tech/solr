# Claim: SOLR-12613 Add Core neighbor timeout diagnosis and bounded repair

Started: 2026-11-05 22:00:06 UTC. Host: Windows Codex desktop. Tags: implementation, review, records.

Assignment: assignments/solr-12613-addcore-neighbor-timeout-implementation-round-1.md, OPEN at board efcffaefd4e41969baf131de5cbbd813d9c0b237. No competing Add Core implementation claim at that snapshot. Nick announced the gate and this assignment; continue within its explicit diagnosis/repair authority. Read the round-2 receipt, handoff, assignment, exact test/controller source and captured failure page. The round-2 receipt is NOT GREEN; the picker isolation repair is proven but one AddCore neighbor timed out.

Exact inputs: P 6b5d830132b941faa85856347099ade8b190be0e, F2 b0e096ae19ad637107227e1bc04e951b9023a680. Preserve F2 branch pr-5110-core-menu-lifecycle and approved upstream PR unchanged. Any justified fix goes on new fork branch solr-12613-addcore-initial-status, ordinary Nick Shanin commits, plain push and independent full-SHA read-back. No force, contributor/public/Jira writes or changes to prior receipts/claims.

Investigate only the Core Admin Add action and add_name timeout. Source currently redirects bare /~cores to its first core when asynchronous initial status arrives; the test can click Add before that callback. Inspect captured DOM/screenshot and test helper readiness, then author a controlled callback-order regression and smallest justified repair if the source chain supports it. Do not classify the historical timeout as a flake or a proven candidate regression merely from mixed pass/fail runs. If source evidence cannot justify a separable implementation, hand bounded diagnostic reproduction to Linux rather than inventing a fix.

Decision ladder: rung 1 source and captured state first; fresh controlled runtime proof on Linux is required to validate a repair. Windows runs no Gradle/compiler/Selenium/tests. Any production/test change needs a new exact-head gate; the previous passing legs do not clear this failed selection. Use existing generated-client/browser fixtures, no blind retries or timeout/skip suppression.

Deliverables: reports/solr-12613-addcore-neighbor-timeout-implementation-round-1.md; if source changes, new fork head/read-back and gates/solr-12613-addcore-initial-status-round-1.md with discriminating old-production assertion and full relevant lifecycle/neighbor proof. Public comment remains unposted until actual required proof and live review reconciliation permit an offer.

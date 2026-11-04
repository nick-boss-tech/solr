# Claim: pr-5287-configset-upload-hardening-round-2-phone-agent

- Claimant host: phone agent (Linux VM).
- Status: DONE. Round 2 gate executed at the pinned head; verdict GATE PASSED with the S3 legs environment-blocked (no Docker on any pool host). Receipt: `receipts/PR-5287-configset-upload-hardening-round-2.md`.
- Job: `gates/pr-5287-configset-upload-hardening-round-2.md`.
- Branch: `pr-5287-configset-source-validation` on nick-boss-tech/solr. Pinned head: `d0cf2471f37ae973ca06390ec195cf51f8fccbb3`, re-verified against the fork remote at claim time and again at gate step 6. Contributor parent: `19f7c83812a4c81227601ff1abadca84beb05cb9`.
- The test repairs at this head were made by the phone agent at Nick Shanin's direction after the round 1 receipt: tidy repair `8899ebd9d7`, security-manager test fix `d0cf2471f3`. Targeted proof before the gate, at seed `5287C0DE`: both repaired classes pass in full at the head (LocalFS 12/12, FileAPI 5/5), and with the contributor parent's production files restored both symlink tests fail on their assertions (LocalFS expected 400, got 200; FileAPI expected SolrException, none thrown). The full gate then reproduced that discrimination across every focused suite.
- Runner: `~/workspace/tools/solr-gradle.sh` (Gradle 9.7.0, vendored JDK 21), serialized under the host build lock. Step 5 root check ran in a normal clone at the same head. Logs and fresh JUnit XML retained under `~/workspace/tools/gate-5287-r2/`.
- Seed: `5287C0DE`. Receipt: `receipts/PR-5287-configset-upload-hardening-round-2.md`.
- Links: SOLR-18618, PR 5287. Category: existing PR follow-up. Supersedes the round 1 claim.

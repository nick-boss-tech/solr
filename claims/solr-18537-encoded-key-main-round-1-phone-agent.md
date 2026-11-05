# Claim: solr-18537-encoded-key-main-round-1, phone agent

- Claimant host: phone agent (Linux VM). Claimed 2026-11-04 evening MST at Nick Shanin's direction in the main chat ("do these gates").
- Status: DONE. Round 1 at `7b83425fe7` stopped GATE INVALID on tidy only (all test legs passed); the formatting repair was committed and pushed as `a676e7a392244e7cf4a35719fc64383b65cd947f`, a superseding round 2 job was posted (`gates/solr-18537-encoded-key-main-round-2.md`), and round 2 closed GATE GREEN at that head.
- Receipts: `receipts/SOLR-18537-encoded-key-main-round-1.md` and `receipts/SOLR-18537-encoded-key-main-round-2.md`.
- Seed `5110EAC0DE`, JDK 21, Gradle 9.7.0 via `~/workspace/tools/solr-gradle.sh`, host build lock, root check in a verified-head normal clone. No PR, GitHub comment, or Jira action from the gate host.

# Claim: solr-18537-path-coverage-main-round-1, phone agent

- Claimant host: phone agent (Linux VM). Claimed 2026-11-04 evening MST at Nick Shanin's direction in the main chat ("do these gates").
- Status: DONE. Gate executed at the pinned head `59ad950e1ac14d628e711e82746ee37c489cf151`; verdict GATE FAILED on substance: both new coverage methods are rejected by Jetty with 400 "Ambiguous URI path encoding" before reaching their assertions, and the mutation leg is not satisfied. Baseline (14 of 14), tidy, neighbors, and the root check at the verified head all pass. Receipt: `receipts/SOLR-18537-path-coverage-main-round-1.md`.
- Seed `5110A7C0DE`, JDK 21, Gradle 9.7.0 via `~/workspace/tools/solr-gradle.sh`, host build lock, root check in a verified-head normal clone. No source repair, push, PR, GitHub comment, or Jira action from the gate host; the test redesign belongs to the implementation side.

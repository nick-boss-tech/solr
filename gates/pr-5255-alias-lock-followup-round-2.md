# Gate: SOLR-18589 alias lock follow-up, round 2 (superseding round 1)

Status: OPEN

- Tags: gate, premise-run, review. Host: vm5 (Linux, JDK 21.0.12.1, Gradle 9.7.0 through the wrapper).
- Supersedes: `gates/pr-5255-alias-lock-followup-round-1.md`, which stays immutable. Round 1's exact head `fd49245a38ba779f78840477dbf6db34b92c5f6b` failed the tidy preflight: `AliasCollectionApiLockTest.java` needed formatting.
- Exact implementation head for this gate: `c09411a16cca5ca8948db9e6a5232756f1b7991b` on `nick-boss-tech/solr:solr-18589-submit`. That is one fast-forward commit above `fd49245a38ba779f78840477dbf6db34b92c5f6b`: "SOLR-18589: Format alias lock test with tidy", formatting only (2 insertions, 4 deletions in one file), committed under Nick Shanin's identity with no trailers.
- Parent / negative proof base: contributor's PR 5255 head `1edd6a113b2a9e1b029f509c9279f4fb58683bba` (`sarkaramrit1993/solr:alias-lock-key-18589`).
- Claim: `claims/pr-5255-alias-lock-followup-gate-round-2-vm5.md`.
- Receipt target: `receipts/PR-5255-alias-lock-followup.md` (absent until the gate writes it).

## Proof

Round 1's Preflight, Formatting and positive proof, Negative proof, and Completion and repair sections apply unchanged at `c09411a16cca5ca8948db9e6a5232756f1b7991b`, in `gates/pr-5255-alias-lock-followup-round-1.md`. Tidy must be clean at this head (the tidy step is the same command), and the focused classes, seed `5255C0DE` and `18589C0DE`, the neighboring suites, `./gradlew check -x test`, and the negative proof on `1edd6a113b2a9e1b029f509c9279f4fb58683bba` all run as stated there. The completion rules and receipt contents are unchanged. The gate changes only the fork branch and board records; no apache/solr or contributor write.

# Gate: SOLR-18589 alias lock follow-up, round 3 (superseding rounds 1 and 2)

Status: OPEN

- Tags: gate, premise-run, review. Host: vm5 (Linux, JDK 21.0.12.1, Gradle 9.7.0 through the wrapper).
- Supersedes: `gates/pr-5255-alias-lock-followup-round-1.md` and round 2 (no round-2 job file; the round-2 claim is `claims/pr-5255-alias-lock-followup-gate-round-2-vm5.md`). Both stay immutable. Round 2 ended in a failure verdict at `claims/pr-5255-alias-lock-followup-gate-round-2-vm5-verdict-compile-failed.md`: the focused test failed to compile at `c09411a16c`.
- Exact implementation head for this gate: `bf5098b3da32d3245b5f04ae50825bc8b005330a` on `nick-boss-tech/solr:solr-18589-submit`. It is two fast-forward commits above round 1 head `fd49245a38ba779f78840477dbf6db34b92c5f6b`: `c09411a16c` (tidy-only formatting of `AliasCollectionApiLockTest.java`) and `bf5098b3da` (test-only compile repair, both test files: a private helper sets `followAliases` before `process`, replacing five chained calls on the `void` return).
- Parent / negative proof base: contributor's PR 5255 head `1edd6a113b2a9e1b029f509c9279f4fb58683bba` (`sarkaramrit1993/solr:alias-lock-key-18589`).
- Claim: `claims/pr-5255-alias-lock-followup-gate-round-3-vm5.md`.
- Receipt target: `receipts/PR-5255-alias-lock-followup.md` (absent until the gate writes it).

## Proof

Round 1's Preflight, Formatting and positive proof, Negative proof, and Completion and repair sections apply at `bf5098b3da32d3245b5f04ae50825bc8b005330a`, in `gates/pr-5255-alias-lock-followup-round-1.md`. The negative proof is taken on the parent with these two test files overlaid from `bf5098b3da`. The gate changes only the fork branch and board records; no apache/solr or contributor write.

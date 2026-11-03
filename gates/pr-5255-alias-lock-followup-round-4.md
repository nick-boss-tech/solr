# Gate: SOLR-18589 alias lock follow-up, round 4 (superseding rounds 1 and 2)

Status: DONE, skip accepted (owner ruling, Nick, 2026-11-02). Every required step passed at `52dacc23b5bb67d254c111c948733da7bc4a9f81` except one inherited skip in the neighboring suites: `CollectionsAPISolrJTest.testCreateAndDeleteShard`, `@AwaitsFix(SOLR-13021)`, byte-identical on apache main (verified 2026-11-02, annotation at line 313 of the test file on main), and vm5's probe run with `-Ptests.awaitsfix=true` passed, so the skip hides no failure. The owner accepts the gate as green with that named limit: PR #5255 is Amrit Sarkar's, not ours, and David Smiley has approved it. Receipt `receipts/PR-5255-alias-lock-followup.md`. (An earlier closure line in the 2026-11-02 cleanup overstated this as a clean GREEN and was corrected the same evening; this line records the owner's acceptance.)

- Tags: gate, premise-run, review. Host: vm5 (Linux, JDK 21.0.12.1, Gradle 9.7.0 through the wrapper).
- Supersedes: `gates/pr-5255-alias-lock-followup-round-1.md` and round 2 (round-2 claim `claims/pr-5255-alias-lock-followup-gate-round-2-vm5.md`, failure verdict `claims/pr-5255-alias-lock-followup-gate-round-2-vm5-verdict-compile-failed.md`). Round 3 ended in a failure verdict at `claims/pr-5255-alias-lock-followup-gate-round-3-vm5-verdict-test-status-read.md`: two focused tests read a REQUESTSTATUS after SolrJ `waitFor` had deleted it.
- Exact implementation head for this gate: `52dacc23b5bb67d254c111c948733da7bc4a9f81` on `nick-boss-tech/solr:solr-18589-submit`. It is three fast-forward commits above round 1 head `fd49245a38ba779f78840477dbf6db34b92c5f6b`: `c09411a16c` (tidy-only formatting), `bf5098b3da` (test-only compile repair: a private helper sets `followAliases` before `process`, replacing five chained calls on the `void` return), and `52dacc23b5` (test-only read-order repair: both tests poll with a TimeOut until the status reads FAILED, then read it once, instead of calling SolrJ `waitFor`, which deletes the status).
- Parent / negative proof base: contributor's PR 5255 head `1edd6a113b2a9e1b029f509c9279f4fb58683bba` (`sarkaramrit1993/solr:alias-lock-key-18589`).
- Claim: `claims/pr-5255-alias-lock-followup-gate-round-4-vm5.md`.
- Receipt target: `receipts/PR-5255-alias-lock-followup.md` (absent until the gate writes it).

## Proof

Round 1's Preflight, Formatting and positive proof, Negative proof, and Completion and repair sections apply at `52dacc23b5bb67d254c111c948733da7bc4a9f81`, in `gates/pr-5255-alias-lock-followup-round-1.md`. The negative proof is taken on the parent with these two test files overlaid from `bf5098b3da`. The gate changes only the fork branch and board records; no apache/solr or contributor write.

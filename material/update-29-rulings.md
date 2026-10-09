# Update-29: owner rulings on the consistency pass, and their execution

Recorded by the main side, 2026-10-09. The owner ruled on the three real contradictions in `reports/update-29-consistency-pass.md`: R1, R2 and R3 all go with the main side's recommendations in `material/update-29-reconciliation-answers.md`.

- **R1 (7504 against 12705): 7504's rule governs counted fields.** Executed: SOLR-12705's counting pin was re-scoped (aa56b7b1be4, test-only), the branch re-gated green, and a combined-tree run with 7504 passed. Landing order: 7504 before 12705.
- **R2 (7504 against 6045): one rule, reject in both orders.** Executed: SOLR-6045's merger check widened to plain-first (8d1dd2bde06), a null-valued-field guard followed (bcae04d77bd), and the branch re-gated green at that head (AtomicUpdatesTest 32 tests, 0 failures; pre-fix proof PASS).
- **R3 (14718 against 5939): 5939's per-request attribution stands.** No code change. SOLR-14718 lands after 5939, and its draft's Limits are rewritten to the post-5939 mechanism in the next drafting round.

With the four top-up verifications (4841, 5754, 7022, 16673) recorded the same day, all 29 update branches are gated at the heads their PRs will open from.

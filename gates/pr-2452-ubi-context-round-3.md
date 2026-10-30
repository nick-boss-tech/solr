# Gate: PR 2452 UBI recording context fix, Linux round 3 (superseding)

Status: OPEN (exact-head gate; proof pending)

- Supersedes: `gates/pr-2452-ubi-context-round-2.md`, whose root check failed in `forbiddenApisMain` on a violation already in PR head `6402b4b528` (`Collections.singleton` in `UBIComponent.java`).
- Decision record: `claims/pr-2452-ubi-context-rework-vm4-round-1.md`, section "Decision record (add-only)". Rung 4 choice, separate commit.
- Contribution base B: apache/solr PR 2452 head `6402b4b528d3ec8daa134772abc84532380e3f7c`.
- Candidate F: `b48c4509a0375f93080f8be50520c18dba23aa29` on `nick-boss-tech/solr:ubi-pr2452-context-submit`. Three commits above B: `37be6eedfe` (race fix and test), `bbc00109d0` (formatting of the test), `b48c4509a0` (static check: `Set.of` in place of `Collections.singleton`, unused import removed).
- Final regression test blob: `886244b0025ac22b747dfcaa3bf1b5a963bfc618`, unchanged from round 2.
- Environment and legs are identical to round 2 (`gates/pr-2452-ubi-context-round-2.md`): fail-before on B with final test overlaid, pass-after on F at seeds `11994C01` and `11994C02`, neighbors at F, tidy, diff check, root `check -x test` with NODE_ENV unset. Driver: `/workspace/gates/logs/vm4/pr2452-ubi-context-round-3/run.sh`.
- Fail-before is re-run because the gate's proof is per head.

## Interpretation

Same boundary as round 2. The static-check commit is separable from the race fix and is reported as its own commit so an author can take the fix alone.

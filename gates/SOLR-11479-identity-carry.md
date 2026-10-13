# Gate job: SOLR-11479 identity rewrite, carry-over check

- Branch: solr-11479-submit. Head: `6f739cfbbd383f08e7393ea38358b7fc77254e58` (fork tip after the lease push). Previous head: `360dca60fcfade6937353d1d1f41e01152aaa3c7`, the gated head in `receipts/SOLR-11479.md`.
- Status: DONE 2026-10-13T08:08Z. Carry-over verified at 6f739cfbbd; receipt section written. Claim: claims/pool-vm2-gate-backlog-11479-identity-carry.md.
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: carry-over check. No code change.
- Why: the tip commit was rebuilt to change its author and committer from a GitHub noreply address to `nick.boss.us@gmail.com`, as Nick directed on 2026-10-13. The tree did not change. The gate at `360dca60fcfa` carries over if the trees match.

## Steps

1. Confirm the fork tip: `ls-remote origin refs/heads/solr-11479-submit` must equal `6f739cfbbd383f08e7393ea38358b7fc77254e58`. If it does not, stop and record the new tip.
2. Confirm the trees: `git rev-parse 360dca60fcfa^{tree}` and `git rev-parse 6f739cfbbd3^{tree}` must be the same, `eb90a93c38c74bdd3190353f5e4f3c0104088300`. Confirm the parent of the tip is `7e538e844c4573881c362e47341d709db8954158`.
3. Confirm every commit from the merge-base to the tip has author and committer `nick.boss.us@gmail.com`, with no trailer lines.
4. Changelog YAML parse for `changelog/unreleased/SOLR-11479*.yml`.
5. Refresh `receipts/SOLR-11479.md`: the new head, the tree-identity result, the author check, and the changelog parse. State that the gate counts carry over from the gated head because the tree is identical. Mark this job DONE.

## Checklist

- [x] fork tip confirmed
- [x] trees identical; parent confirmed
- [x] author and committer checked on every commit; no trailers
- [x] changelog parse
- [x] receipt refreshed at the new head; job DONE

# PR 5221 license-header Linux gate, completion report (round 1)

Gate job: `assignments/pr-5221-license-headers-linux-gate-round-1.md` (unchanged). Claim: `claims/pr-5221-license-headers-gate-vm4-round-1.md`. Receipt: `receipts/PR-5221-license-headers-round-1.md`.

Verdict: GATE GREEN at `2855d3a04f29cc9121ceb677b9e495849cdae25a`. Every required leg passed. No repair was needed.

Read the receipt for leg-by-leg results, commands, return codes, log paths and the three environment failures on leg 4. Summary of the environment issues:

- `NODE_ENV=production` in the host shell made npm skip devDependencies, so `@asciidoctor/tabs` was never installed (two attempts).
- Antora rejects a linked git worktree, so leg 4 ran in a shallow clone of the same commit.
- Three attempts total for leg 4. Only the clean run is counted.

Possible related observation, not verified here: the earlier PR 5204 js-client npm failure (missing `@babel/plugin-syntax-dynamic-import`) may have the same `NODE_ENV` cause. Worth a test on a clean environment before it is cited as a pre-existing defect.

Not done: no PR, comment, review or apache/solr write. The PR-facing draft in the receipt's last section is for Nick's review.

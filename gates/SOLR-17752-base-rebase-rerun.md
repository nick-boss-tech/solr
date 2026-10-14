# Gate job: SOLR-17752 rebase onto current upstream main and focused rerun (HELD: owner item)

- Branch: solr-17752-submit. Head: e5c0a64f993c7ce5e9a594f5a6eb0679925e9de9 (live tip, verified by `git ls-remote origin refs/heads/solr-17752-submit` on 2026-10-13; the gated head in receipts/SOLR-17752.md, gate green at this head on 2026-10-04).
- Current merge-base with upstream main: 56ec140e3636d5f4150fa87fbf7103529536ac99. The receipt's proof leg ran at 14c7aac0d151402b00259e2fb9bf5eed7049ec5d, which is neither the merge-base nor current main. That is why the rerun is owed.
- Target base: upstream main when the rebase runs. Read on 2026-10-13 as d38e0f1623728bdd305e76a598f013f1bf32e449. The runner re-reads upstream main by ls-remote and records the value it rebases onto.
- Status: HELD. Owner item. The rebase rewrites a published branch, and the build-docs record reserves that to the owner (`reports/build-docs-misc-round-1.md`, item 9: "rebasing a published branch is the owner's call"). Nothing claims or runs this job until Nick directs the rebase in the main chat. Written 2026-10-13 by the review coordinator; not a claim.
- Intended host: a Linux gate host, after that direction. Windows never claims build work.
- Capability tags: gate.
- Decision adopted: rebase onto current upstream main. This is item 9's recommendation, adopted under the decision default (`reports/build-docs-convergence-round-2-response.md`).
- Drift check (2026-10-13, read-only): neither Java file the branch touches (`SolrResponseUtil.java`, `TestShardResponseLogging.java`) changed on upstream main between 56ec140e3636 and d38e0f1623. The changelog file name is new. The rebase is expected to apply cleanly; the gate still proves the behavior at the rebased head.
- Module: solr/core. Focused test: `org.apache.solr.handler.component.TestShardResponseLogging` (1 test). Seed: 17752C0FFEE17752 (the seed the runner passes, recorded in the log).
- Gate steps, after the direction:
  1. Rebase solr-17752-submit onto the target base. Record the old head (e5c0a64f993c7ce5e9a594f5a6eb0679925e9de9) and the new head. Push only with `--force-with-lease` pinned to e5c0a64f993c, and only after the owner's direction names the push.
  2. Tidy at the new head (`./gradlew --no-daemon tidy`); leave the tree clean.
  3. Error Prone compile of `:solr:core`, as the gate runner did it (receipts/SOLR-17752.md).
  4. Focused test: `./gradlew --no-daemon :solr:core:test --tests org.apache.solr.handler.component.TestShardResponseLogging` with seed 17752C0FFEE17752. Count from the fresh JUnit XML: 1 test, 0 failures, 0 errors, 0 skipped.
  5. Fail-before at the new merge-base: the same test, with production code at the new merge-base, must fail. The record must show the query text reaching the captured warning, as the receipt's run 3 showed at the old base. Record the failing line and the seed.
  6. Module check: `./gradlew --no-daemon :solr:core:check -x test`.
  7. Changelog YAML parse: `./gradlew --no-daemon logchangeLint`.
- Proof shape: focused test plus fail-before at the new merge-base, per steps 4 and 5.
- On green: refresh `receipts/SOLR-17752.md` at the new head, with the base, the old and new heads, and every step result. Then update `pr-drafts/build-docs/SOLR-17752.md`: remove the HOLD line; move the merge-base and head links, the Proof's head and base, and the changelog link to the new base and head.
- Hold until: owner direction for the rebase (the owner item). This job is then the owed gate.

## Step checklist

- [ ] owner direction recorded for the rebase (owner item; not this review's call)
- [ ] rebase; old and new heads recorded
- [ ] tidy; Error Prone compile
- [ ] focused test green, counted from the XML
- [ ] fail-before at the new merge-base recorded
- [ ] module check; changelog parse
- [ ] receipt refreshed; draft re-pointed; HOLD removed

# Gate: PR 5108 deterministic reverse shard merge, round 3, at repaired head F2 (superseding)

Status: OPEN. Immutable once pushed. Proves only the exact head below.
Supersedes: `gates/pr-5108-reverse-merge-round-3.md` for proof. That job named `c5a16393d2b0d2ad8b05f66697a906733e551b69`, which tidy changes by one line in `DistribCursorPagingTest.java`. The original job's own preflight rule requires a repair commit and a superseding job pinned to the repaired head.

- Exact head F2: `495d3fbdbe07bfb474b43d77b89f8c033bbeb8fd` (commit "SOLR-17786: tidy the local query call in DistribCursorPagingTest", Nick Shanin, no trailers, one commit above `c5a16393d2`).
- Original head, not proven by this job: `c5a16393d2b0d2ad8b05f66697a906733e551b69`.
- Parent: `fde070281383f113a331a9ec5ccde173b66fac3c`, the round-2 gated head.
- Fork: `nick-boss-tech/solr`, branch `solr-17786-reverse-cursor-submit`. Read back by ls-remote after the push.
- Claim: `claims/pr-5108-reverse-merge-round-3-vm4.md`.
- Host: vm4, Linux aarch64. JDK Temurin 21.0.12.1+1 at `/workspace/tools/jdk21`. Gradle 9.7.0 via the repo wrapper, `GRADLE_USER_HOME=/workspace/gradle-home`.
- Build lock: `/workspace/tools/.gradle-vm4.lock`, through `/workspace/tmp/pool-bin/sweep-run.sh`. One build at a time.
- Positive worktree: `/workspace/worktrees/gate-5108`, detached at F2.
- Logs: `/workspace/gates/logs/vm4/pr-5108-round-3/`. Each leg has `.log`, `.headroom.log`, and `.xml/` for fresh JUnit XML.

## Legs

The commands, seeds, and completion rules are those of `gates/pr-5108-reverse-merge-round-3.md`, with these bindings:

0. Preflight: `tidy` at the original head changed one line; the repair commit is F2. `preflight-tidy-f2` runs `tidy` at F2 and requires a clean tree.
1. Detached preflight at F2 as above, plus the test-only delta check and `git diff --check` from the parent to F2.
2. Compile with Error Prone at F2.
3. Focused new tests at F2, seed `5108C0DE`.
4. Focused new tests at F2, seed `17786C0DE`.
5. Existing cursor and ordering suites plus the new tests at F2, and `QueryResponseTest` in solrj.
6. Pristine F2 checkout: `:solr:core:check :solr:solrj:check -x test`.
7. Mutation M1 (coordinator comparator), exact patch and expected failures as in the original job, applied to a disposable checkout of F2.
8. Mutation M2 (tiebreaker inversion), exact patch and expected failure as in the original job, applied to a separate disposable checkout of F2.
9. Restore pristine state and finish with a fresh positive focused run at F2 and the final checks on the pinned clean head.

## Completion

GREEN requires every positive, formatting, check, and mutation leg above at F2, with fresh XML. Mutation results are recorded in the round-3 receipt with expected and actual IDs. Failures are recorded by an add-only verdict naming this job and its claim. No public PR or JIRA action is part of this job.

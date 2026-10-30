# Receipt: PR 2452 UBI recording context, Linux round 1 (vm4)

Status: PROOF PENDING. Test legs green at `bbc00109d0`. Root check FAILED at that head.

- Ticket: none (no JIRA key on PR 2452). Commits carry no JIRA key or PR number.
- Base B: apache/solr PR 2452 head `6402b4b528d3ec8daa134772abc84532380e3f7c`.
- Candidate F: `bbc00109d07b9d53e4c30659c28e626c48dcfca4` on `nick-boss-tech/solr:ubi-pr2452-context-submit`. Final test blob `886244b0025ac22b747dfcaa3bf1b5a963bfc618`.
- Gate: `gates/pr-2452-ubi-context-round-2.md` (superseding `gates/pr-2452-ubi-context-round-1.md`). Report: `reports/pr-2452-ubi-context-rework-and-gate-round-1.md`.
- Results at F, Linux, JDK 21.0.12.1, Gradle 9.7.0, `--no-daemon --no-build-cache`:
  - Fail-before at B with final test overlaid, seed 11994C01: 5 run, 2 failed (overlap cases, `[query-B, query-A]` expected vs `[query-B, query-B]`).
  - Pass-after at F: seed 11994C01 5/5 green; seed 11994C02 5/5 green.
  - Neighbors at F, seed 11994C01: 23 tests green across five UBI and config classes.
  - Tidy clean at F. Diff check clean.
  - Root `check -x test`, NODE_ENV unset: FAILED in `:solr:core:forbiddenApisMain` on `Collections.singleton` at `UBIComponent.java:395`, which is the PR head's line 391. Reproduced on B alone.
- Not covered: Windows, the full UBI draft, listener migration, background ingestion, multi-shard merging, redaction, production overhead.

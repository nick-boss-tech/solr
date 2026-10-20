# Review: SOLR-17842, branch solr-17842-submit (unit 1)

Head reviewed: 008973f6313374517e20a0c347d0fb4885afe1dd (ls-remote 2026-10-20). Base: 14c7aac0d151. Diff: two files, 91 insertions, no Java or Gradle change.

## Pass 1

Review (2026-10-20T13:35Z, vm5):

- Scope. The change adds `solr/benchmark/docs/release-benchmarking.md` and one link in `solr/benchmark/README.md`. Docs only.
- Ticket fit. SOLR-17842 asks for published benchmark data and release comparisons. The branch adds guidance for running and comparing benchmarks; it publishes no results. Finding, major: the ticket's ask is only partly met by this branch. Recorded, not fixed: the owner already flagged the same gap in the receipt (2026-10-08), and a repair here would be a scope decision.
- References. Every class in the table exists under `solr/benchmark/src/java/org/apache/solr/bench/`, and `solr/benchmark/jmh.sh` is present.
- Example command. `./jmh.sh SimpleSearch JsonFaceting FilterCache ... -rff work/jmh-<build>.json` is documentation, not run here (it runs benchmarks). Its argument shape was not run; the regate does not run it either.
- Gated head. The 2026-10-15 light gate covered `9da1242b1d`, a commit on top of this head that is not on the branch. This head is not that commit, so the regate here is the check for the live tip.
- Link. `docs/release-benchmarking.md` resolves from the README link (1 match, target present).
- Whitespace. `git diff --check` clean. The license header matches the other docs.

Repairs: none. Nothing in the pass needed a repair inside the Gate repairs scope.

Regate: gates/SOLR-17842-review-pass-1.md (light gate, docs-only shape).

## Pass 2

Review (2026-10-20T13:37Z, vm5, second pass on the same head 008973f631):

- Command. `./jmh.sh SimpleSearch JsonFaceting FilterCache -f 1 -wi 5 -i 5 -rf json -rff work/jmh-<build>.json` matches the README usage at line 195 (`-rff work/jmh-results.json`). `jmh.sh` runs JMH from `solr/benchmark` only (its first check), and the doc says so.
- Output path. `work/` is gitignored (`solr/benchmark/.gitignore`). The example assumes the directory exists; the README already makes the same assumption. Minor, nit: recorded, not changed here.
- Name matching. JMH matches the class arguments as regular expressions, so the short names in the example select the intended classes. Not run.
- Consistency with pass 1. Head unchanged; pass 1 findings stand; no new finding of major severity.

Repairs: none. Nothing in pass 2 was in the Gate repairs scope.

Regate: gates/SOLR-17842-review-pass-2.md (light gate shape, same checks as pass 1).

# Pool assignment: SOLR-18551 branch validation, round 1

Status: DONE (all three rows reported in reports/SOLR-18551-branch-review-round-1.md; claims for each row are linked there)

- Capability tags: settling-run.
- Intended host: vm3 for the main leg; vm2 for the 9.x and 10x legs, in that order. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: vm4, 2026-10-17, at the direction of Nick Shanin.

Context: PR apache/solr#5173 (SOLR-18851 title, SOLR-18551 ticket) fixes `IndexFetcher.slowFileExists` so it no longer opens the file. Three branches carry the PR's commits plus a focused regression test `IndexFetcherTest`. Each branch needs one head leg (the test on the branch head) and one base leg (the test file from the branch copied onto the base commit, no fix).

## Branches and commits

| Line | Head branch (fork) | Head commit | Base commit | Test source |
| --- | --- | --- | --- | --- |
| main | solr-18551-harden-tests | 544d09af7663bc1c07bd15fe30d4c5c6e853f816 | 714d6ceff852e6dc743ef51ae094e4eaf8f3b3f9 | solr/core/src/test/org/apache/solr/handler/IndexFetcherTest.java on the head branch |
| 9.x | solr-18551-backport-9x | d8f96dfeea3ab6d84053f3b9173c06ca379fc25d | c135e6335c7158fa26e96b0dc386f825255b47c0 (Solr 9.10.1 tag) | same path on the head branch (Java 11 casts) |
| 10x | solr-18551-backport-10x | a786123b1031e98981d20ad7e5d89083a523bcec | 0712c1f7831a0b0c22837fa6a07fc028040e9bbe (upstream branch_10x) | same path on the head branch |

Fork: https://github.com/nick-boss-tech/solr. Upstream: https://github.com/apache/solr.

## Commands

Test command, same seed on every leg:

`./gradlew --no-daemon --console=plain -Ptests.workDir=/tmp/18551-<host>-<leg> -Ptests.seed=18551C0FFEE18551 -Dtests.seed=18551C0FFEE18551 :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.IndexFetcherTest`

Head leg: check out the head commit detached, run the test command.

Base leg: check out the base commit detached, then copy the test file from the head commit (`git show <head>:<test path>`) into the same path, and run the test command. Do not copy anything else.

Record the JUnit counts from `solr/core/build/test-results/test/TEST-org.apache.solr.handler.IndexFetcherTest.xml` and the build result.

## Items

1. **main, head and base legs.** Run both legs for the main row. Start from the rows above. Output: the main section of the deliverable.
2. **9.x, head and base legs.** Run both legs for the 9.x row. Output: the 9.x section of the deliverable.
3. **10x, head and base legs.** Run both legs for the 10x row. Output: the 10x section of the deliverable.

Run one leg at a time per host (flock with the host's Gradle lock). Keep logs under the host's own logs directory and cite the paths.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Head leg result | decided: 3 of 3 tests pass, 0 failures, 0 errors, 0 skipped | if compilation fails, any test fails, or any test is skipped, record the output and stop the leg; do not rerun | Nick Shanin |
| Base leg result | decided: 3 of 3 tests fail, each with an assertion showing `openInput` where `fileLength` was expected | if any base test passes, does not compile, or fails another way, record the output and stop the leg; do not rerun | Nick Shanin |
| Source changes | decided: none | if a run exposes a problem, report it without editing or pushing source | Nick Shanin |
| Host assignment | decided: vm3 takes the main row; vm2 takes the 9.x row, then the 10x row | if a host is busy or down, leave that row OPEN and record it; do not reassign without a new assignment | vm4 at Nick's direction |
| Rerun after a stopped leg | decided: no rerun | record the stop and wait for the main agent | Nick Shanin |

## Deliverable

`reports/SOLR-18551-branch-review-round-1.md`: one section per item, with the head commit and the base commit as run, the seed, the build result, the JUnit counts, the failing assertion for the base legs, and the log paths on the host. Add the report commit and the head of pr-prepare to the claim's DONE line.

## Limits

- No PR, comment, JIRA or email. No force push. No change to the fork branches above.
- Do not edit or push source. Do not run the full test suite.
- Commit the report as Nick Shanin with no trailers. No em dashes or en dashes.

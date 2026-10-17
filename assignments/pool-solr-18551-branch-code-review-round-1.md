# Pool assignment: SOLR-18551 branch code review, round 1

Status: OPEN

- Capability tags: review.
- Intended host: Windows (review agent). Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: vm4, 2026-10-17, at the direction of Nick Shanin.

Context: PR apache/solr#5173 (titled SOLR-18851, which is a typo for SOLR-18551) fixes `IndexFetcher.slowFileExists` so that it checks existence with `fileLength` instead of opening the file. The PR's base is upstream main at 714d6ceff852e6dc743ef51ae094e4eaf8f3b3f9, and its head is 076cf49f5b plus d05eabeee2 on gerlowskija/solr. Three fork branches carry the PR's commits plus a regression test `IndexFetcherTest`. A separate assignment (assignments/pool-solr-18551-branch-review-round-1.md) covers the test runs on Linux hosts. This assignment is code review only. Do not run Gradle.

## Branches

| Line | Branch (fork) | Head commit | Base commit |
| --- | --- | --- | --- |
| main | solr-18551-harden-tests | 544d09af7663bc1c07bd15fe30d4c5c6e853f816 | 714d6ceff852e6dc743ef51ae094e4eaf8f3b3f9 |
| 9.x | solr-18551-backport-9x | d8f96dfeea3ab6d84053f3b9173c06ca379fc25d | c135e6335c7158fa26e96b0dc386f825255b47c0 (Solr 9.10.1) |
| 10x | solr-18551-backport-10x | a786123b1031e98981d20ad7e5d89083a523bcec | 0712c1f7831a0b0c22837fa6a07fc028040e9bbe (upstream branch_10x) |

Fork: https://github.com/nick-boss-tech/solr. Upstream PR: https://github.com/apache/solr/pull/5173. The leak probe used for the measurements in the draft comment: https://gist.github.com/nick-boss-tech/bbdb73c27f36e060dd31af0cacb951ef (secret gist, Nick's account).

## Items

1. **Fix hunk on each line.** Confirm that the `IndexFetcher.java` change on each of the three branches is the same change as PR 5173's, once the line offsets are ignored. Use `git diff <base> <head> -- solr/core/src/java/org/apache/solr/handler/IndexFetcher.java` on each branch and compare with the PR's diff. Start from the table above. Output: a finding per line, "matches" or "differs", with the differing lines quoted.
2. **Cherry-pick faithfulness.** Confirm the 9.x and 10x branches contain the PR's two commits (076cf49f5b and d05eabeee2) unchanged apart from the cherry-pick, and that the only other commit is the test commit. Check the author and message lines. Output: one finding per branch.
3. **Java 11 test on 9.x.** Diff `solr/core/src/test/org/apache/solr/handler/IndexFetcherTest.java` between the main head and the 9.x head. The only differences should be the two `instanceof` lines, which must not use pattern matching because 9.x targets Java 11. Confirm nothing else changed. Output: the diff summary and any extra differences.
4. **Test review on main.** Read the test file on the main head. Check it against the repo's `AGENTS.md` test rules: `SolrTestCase` as the base, no `Thread.sleep`, no manual system-property cleanup, and `newDirectory()` where a randomized directory is wanted. Check that each assertion would fail on base for the stated reason. Note what the tests do not cover: the call site in the replication loop, and any leak that happens through a path other than `openInput`. Output: a finding per problem, with the file and line.
5. **Changelog.** Read `changelog/unreleased/SOLR-18551-avoid-mmap-leak-in-indexfetcher.yml` on each branch. Check the fields against `dev-docs/changelog.adoc` and the other entries in `changelog/unreleased/`. Confirm the title, type, and links are correct and that the title matches the PR's title apart from the typo. Output: one finding per branch.
6. **Writing rules.** Search every file added or changed on the three branches for em dashes and en dashes (U+2014, U+2013). Output: the count per file, and the line numbers if any.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Severity scale | decided: blocker, should-fix, nit | each finding gets exactly one severity; a blocker is a wrong result or a rule break in shipped code | Nick Shanin |
| Source changes | decided: none | record findings; do not edit, commit, or push any branch | Nick Shanin |
| Test runs | decided: none by this host | if a finding needs a run, add it to the Linux settling assignment; do not run Gradle here | Nick Shanin |
| Title typo | decided: report it, do not fix it | record the typo as a finding on the PR title only; the fork branches keep the commit messages as they are | Nick Shanin |

## Deliverable

`reports/SOLR-18551-branch-code-review-round-1.md`: one section per item, with the findings (severity, file, line, and what to change), and "no findings" where that is the result. Include the commit you reviewed for each branch.

## Limits

- No Gradle, no test runs, no PR, comment, JIRA or email. No force push.
- Do not edit, commit, or push any fork branch. Do not modify PR 5173.
- Commit the report as Nick Shanin with no trailers. No em dashes or en dashes.

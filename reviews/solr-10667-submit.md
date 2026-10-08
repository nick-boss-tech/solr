# solr-10667-submit

- Branch: origin/solr-10667-submit
- Head: 32b594f280c (listed head 5cee0d5bcf17, plus one review patch commit)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d968, 16 commits behind)
- Scope: the author's 3 commits plus 1 review patch. `gradle/solr/packaging.gradle` (+4: `assemblePackaging` copies the module's `example` directory, patch only), `solr/packaging/test/test_modules.bats` (+7: `ltr module ships its example directory`), the changelog fragment, and `SOLR-10667-TESTING.md` (kept in place).
- Verdict: Nearly
- Patch: 32b594f280c, `SOLR-10667: assemblePackaging copies the module's example directory (the 045e156 commit only added the BATS check)`
- Reviewer: review-agent A2 (claude-haiku-5-5, Claude Code), 2026-10-08

Nothing here was compiled, run, or tested. No Gradle, no BATS run. Every claim below was checked by reading code, except where marked as hypothesis.

## Premise check (hypothetical-reproduction handoff)

- VERIFIED: the base `assemblePackaging` task (`gradle/solr/packaging.gradle:80-102`) is a `Sync` that copies `README.md`, the module jar, and its runtime libs. It does not copy `example/`. So the distribution's module directory lacks the LTR examples, as the JIRA reports.
- VERIFIED: `solr/modules/ltr/example/` is tracked on `upstream/main` and is on disk. It holds `README.md`, `config.json`, `exampleFeatures.json`, `libsvm_formatter.py`, `train_and_upload_demo_model.py`, `user_queries.txt`, and `.gitignore`. The three files the BATS test checks are all present.
- VERIFIED: `ltr` is the only module with an `example` directory (`solr/modules/*/example` on `upstream/main`). A module without one is unaffected, because Gradle skips a missing `from` source.
- VERIFIED: the `045e156` commit, titled "package module example directories (modules/ltr/example) in the distribution", changes only `solr/packaging/test/test_modules.bats`. The packaging change the TESTING doc describes was not on the branch. Without it, the new BATS assertion cannot pass. This is the defect the patch fixes.

## Findings (ranked)

MEDIUM (verified, patched in 32b594f280c): The commit that claims to package the examples only added the BATS check. `assemblePackaging` was not changed, so the new test would fail on the distribution. The patch adds `from("example", { into "example" })` to `assemblePackaging`, in the same closure style as the `tasks.jar` copy beside it (`packaging.gradle:83-85`). Gradle skips a missing `from`, so modules without an example directory are unaffected.

LOW (hypothesis): The build change was not run. The "a missing source is silently ignored" behavior is the standard Gradle `Copy`/`Sync` behavior, but it was not checked against this Gradle version. If the build complains, the TESTING doc's suggested guard, `file("example").exists()`, is the fallback.

LOW (verified): `example/.gitignore` (one line, `solrclient/`) will be copied into the distribution's `example/` directory, because `from` copies dotfiles. This is harmless but worth knowing.

LOW (verified): `from("example")` copies what is on disk, not what git tracks. A developer's generated `solrclient/` (from `copyPythonClientToExample`, which `.gitignore` excludes) would be packaged on that machine but not on a clean checkout. This matches the TESTING doc's second guess. The owner may want an explicit exclude.

## Verified correct (by reading; not run)

- The packaging change uses the same closure form as the neighboring copies. Its placement after `from "README.md"` keeps the file's existing order.
- The BATS test follows the AGENTS.md BATS guidance: `run ls` followed by `assert_output --partial`.
- The license concern in the TESTING doc is covered. The example files are already in the source tree, so any license or RAT check on the source tree already sees them. Packaging does not add new files.
- The changelog fragment has the right format (`type: fixed`, ICLA author, JIRA link).
- All commits, including the patch, are authored as the ICLA identity, with no Co-Authored-By trailers.

## Owner decisions

None blocking. Optional: whether the distribution should exclude generated `solrclient/` explicitly (see the second LOW).

## Not checked

- Nothing was compiled, packaged, or run. The Linux gate runs the Gradle build and the BATS test.
- Gradle's behavior for a missing `from` source in this version was not confirmed (see the first LOW).
- Other packaging consumers of `assemblePackaging` (the root `solr/packaging` assembly) were not traced beyond the module packaging directory.
- Upstream conflicts. The branch is 16 commits behind `upstream/main`.

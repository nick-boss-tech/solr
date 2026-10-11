# Retire candidates review round, part RR2

Assignment: `assignments/pool-retire-candidates-review-round.md`, slice RR2 (four items). Claim: `claims/pool-retire-candidates-review-round.md` (not edited by this part; the DONE mark goes with the coordinator's push). Worktree `wt/pr-prepare-suggester` at f56590a8660. Read-only: no branch, PR, receipt, build, test or gate action; GitHub reads only (`gh pr view`, `gh pr list`, `gh run list`, `gh run view`).

Verdict counts: RETIRE CLEAN 3 (SOLR-10198, SOLR-18119-submit, SOLR-9759); SALVAGE FIRST 1 (SOLR-18277, evidence only).

Refs at review time: upstream `refs/heads/main` d76fc14b9857 (`git ls-remote upstream refs/heads/main`). Fork tips (`git ls-remote origin`): solr-10198-submit 9448cda146f7; solr-18119-submit 723022d35fce; solr-18277-submit 17c0e6448128; ci/solr-18277 35c9e9952285; ci/solr-18277-proof 44c35a3deafd; solr-9759-submit 31e702622dae (all match the assignment and receipts). Pair branch solr-18119-jvm 660faedd026d.

Method, all items: merge-base from `git merge-base <tip> d76fc14b`; net file set from `git diff --name-only <base> <tip>`; net content check `git diff --stat <tip> d76fc14b -- <paths>` (empty means identical on main); squash check `git diff --stat <tip> <squash-sha> -- <paths>`; commits not on main from `git rev-list --count d76fc14b..<tip>` (these counts include squashed history, so the net content check is the authority).

---

## 1. SOLR-10198 (branch solr-10198-submit): RETIRE CLEAN

- Tip: 9448cda146f7e09f3389551c4ef883a63d415eeb (ls-remote). Equals `headRefOid` of PR #4965 (apache/solr, MERGED 2026-10-07T13:19:33Z, mergeCommit e97c2a0884cb).
- Receipt `receipts/SOLR-10198.md`: MERGED, merged head equals live tip, nothing to draft.
- Category: `reports/solrj-clients-round-1.md` (merged; fork branch deletion is the owner's call).
- Merge commit e97c2a0884cb is a single-parent squash (parent 52fba69a4eb3); it is an ancestor of d76fc14b.
- Merge-base with main: 0d2a4649c79. The branch changes 5 files: `changelog/unreleased/SOLR-10198.yml`, `solr/core/.../client/solrj/embedded/EmbeddedSolrServer.java`, `solr/core/.../response/DocsStreamer.java`, `solr/core/src/test/.../embedded/TestEmbeddedSolrServerStreamingTypes.java`, `solr/core/src/test/.../handler/tagger/EmbeddedSolrNoSerializeTest.java`.
- `git diff 9448cda d76fc14b -- <those 5 paths>` is empty, and `git diff 9448cda e97c2a0884cb -- <those 5 paths>` is empty. The branch's net content is on main.
- The 10 branch commits not reachable from main are the pre-squash history of the same change; their net effect is e97c2a0884cb. No commit carries a fix or test missing from main.
- Fork CI: `gh run list --branch solr-10198-submit` returns no runs.

## 2. SOLR-18119-submit (branch solr-18119-submit, PR #4999 OPEN): RETIRE CLEAN, conditional on the pair

- Tip: 723022d35fcede32de2a884c29e5aa0ff08e3f1f (ls-remote).
- Receipt `receipts/SOLR-18119.md`: retire candidate, superseded by solr-18119-jvm on 2026-10-08.
- Category: `reports/build-docs-misc-round-1.md` (retire candidate) and `branch-focus-inventory-2026-10-08.md` row 460.
- Merge-base with main: 0d2a4649c79. The branch's net change is one file, `gradle/documentation/changes-to-html.gradle` (+10/-9). Main still has the base content of that file (`git diff 0d2a4649c79 d76fc14b -- <file>` is empty), so the hunk is not on main.
- The hunk resolves python3 through `project.externalTool`, fails with a GradleException when python3 is missing, and honours `-Ppython3.exe`. It is the only content on this branch.
- Shipped pair: solr-18119-jvm (660faedd026d, PR #5061 OPEN) removes the Python route. Its `gradle/documentation/changes-to-html.gradle` is a buildinfra Java task with no python call, and `changes2html.py` is deleted there. `git grep` for `py3Executable` and `python3` in the jvm tree's `gradle/documentation` and build-infra finds nothing relevant.
- So the hunk is not in the pair, and the pair removes the route the hunk edits. Nothing gainable.
- Caveats: (a) solr-18523-submit (26678c3737c, PR #5062 OPEN) still carries the base python route, so the route goes only when #5061 lands. If the Java port is dropped, this hunk becomes the fallback and the verdict must be revisited. (b) The g6 note (`reports/build-docs-misc-round-1-g6.md`) said PR #5034 is based on #4999. PR #5034 (janhoy, "GraalPy python fallback for the build") is CLOSED, unmerged, on 2026-10-09, so that hold is no longer live. Its head bc84112b is not in the local object store and I did not check its ancestry.

## 3. SOLR-18277 (solr-18277-submit, ci/solr-18277, ci/solr-18277-proof): SALVAGE FIRST (evidence only)

- Tips (ls-remote): solr-18277-submit 17c0e64481285e8683c6bf974d9c7f9dfc73555a (the gated head in `receipts/SOLR-18277.md`); ci/solr-18277 35c9e995228; ci/solr-18277-proof 44c35a3deaf.
- Receipt `receipts/SOLR-18277.md`: gate green (hardened) at 17c0e644812, proof INCONCLUSIVE, audit scope is the retire question.
- Category: `material/solrcloud-round-1-answers.md` (DISCUSS 6: delete all three branches; recommendation delete all three).
- PR #4959 MERGED 2026-10-05T10:46:07Z, headRefOid 17c0e644812, mergeCommit 0cc328310f8 (single-parent squash, an ancestor of d76fc14b).
- solr-18277-submit: merge-base 0d2a4649c79; 4 files (`changelog/unreleased/SOLR-18277.yml`, `SplitShardCmd.java`, `ShardSplitTest.java`, `SplitShardCmdCleanupTest.java`). The branch tip, 0cc328310f8 and d76fc14b are identical on all 4 (empty diffs both ways). Nothing on the branch is missing from main.
- ci/solr-18277 (35c9e995228): one commit on the PR head adding `.github/workflows/fork-test-runner.yml` (48 lines, manual workflow_dispatch focused-test runner). Not on main. It is fork tooling, not a test, fix or evidence for apache/solr. Recoverable only from this commit (or from 44c35a3deaf) if the branches are deleted.
- ci/solr-18277-proof (44c35a3deaf): one commit on base 0d2a4649c79 (base production): the same workflow file, plus `ShardSplitTest.java` with two `@AwaitsFix` annotations removed. That hunk is already on main.
- The gain is evidence. Fork CI runs (read with `gh run view <id> --log`):
  - 37237885304, head 44c35a3 (base production + branch ShardSplitTest): `:solr:core:test --tests "...ShardSplitTest.testSplitAfterFailedSplit*" -Dtests.nightly=true`: 2 tests, SUCCESS.
  - 37238381991, same head and filter: 2 tests, SUCCESS.
  - 37238383951, same head and filter: 2 tests, SUCCESS.
  - 37237887493, head 35c9e995 (PR head + runner), same filter with `-Ptests.iters=5`: 10 test invocations, SUCCESS.
  So `testSplitAfterFailedSplit` and `testSplitAfterFailedSplit2` pass on base production code in all three base runs. They do not fail without the fix and are not a fail-before pin for SOLR-18277.
- Why the unit class is INCONCLUSIVE: `SplitShardCmdCleanupTest` (5 tests) calls `SplitShardCmd.buildCleanupShardStateUpdates`. That method is absent at base 0d2a4649c79 (`git grep` no match) and present on main (2 matches). The class cannot compile on base, so its fail-before is INCONCLUSIVE by construction, as the receipt says.
- Salvage job (proposed, not started): a doc-only record update to `receipts/SOLR-18277.md` on main, by the main side. Content: the four run IDs with head SHA, filter, flags and result; the statement that the two ShardSplitTest tests pass on base production and so do not pin the fix; the compile-level reason for INCONCLUSIVE; and the commit SHAs that carry `fork-test-runner.yml` (35c9e995228, 44c35a3deaf) so the file can be recovered before branch deletion. Gate: none; a text check that the run IDs match the list above. Reason to do it now: GitHub run logs expire.
- Optional, spec only, not run: a fail-before for the unit class needs a variant that does not call the new helper. Class: `SplitShardCmdCleanupTest` variant. Base: 0d2a4649c79. Expected: fails on base, passes at 17c0e64481285e8683c6bf974d9c7f9dfc73555a. Seed: not in the receipt (`g4959-harden2.log` is not in this worktree).

## 4. SOLR-9759 (branch solr-9759-submit, head 31e702622dae): RETIRE CLEAN

- Tip: 31e702622dae43a36ed8d7ec8477c1547e5ebc42 (ls-remote; matches the assignment and receipt).
- Receipt `receipts/SOLR-9759.md`: NO GATE; the branch's own TESTING note says nothing was compiled or run.
- Category: `reports/admin-ui-round-1.md` and parts u1, u2 (superseded on main by c6f910d3ccf, retire recommended).
- Merge-base with main: cabedd1d968. Five files: `SOLR-9759-TESTING.md`, the changelog fragment, `AdminUiStreamScreenTest.java`, `stream.js`, `services.js`.
- c6f910d3ccf is an ancestor of d76fc14b. `stream.js`, `services.js` and `AdminUiStreamScreenTest.java` are identical at c6f910d3ccf and at main (empty diffs).
- Claims in the TESTING note, checked against main:
  1. GET expression in the URL, 413, blank box, JSON.parse error: main posts the expression (`Query.queryPost`, form-encoded), guards non-JSON, shows HTTP errors. Covered.
  2. Error callback showing HTTP status and body: main's error path shows rejection data or "HTTP status statusText". Covered, different wording.
  3. Address-bar link is the GET URL and fails for very long expressions: still true on main. Main keeps `Query.url` for display with a comment saying it is reference only. The branch only documents this; main already shows the same link with the same caveat. Not a branch gain.
  4. Long-expression test: the branch's `testLongStreamingExpressionViaUi` (10,000 spaces, asserts stream-doc-1 and stream-doc-3) differs from main's `testLargeExpressionSucceedsViaUi` (20,000-character padded fq, zero hits, asserts EOF). Main's assertion catches the same regression (no body, or a truncated body, gives no EOF response). The branch's positive document assertion is the only thing main lacks; it is not worth a job by itself.
  5. Jetty `solr.jetty.request.header.size` default 8192: still on main (`solr/server/etc/jetty.xml` line 63). A config fact, not a change.
  6. Guessed items (form-encoded POST to `/stream`, `$resource` signature): main relies on the same shape (`queryPost`), but no run of the branch test exists anywhere (`gh run list --branch solr-9759-submit` returns none).
- Changelog: the branch file name differs from main's (`SOLR-9759-admin-ui-stream-post.yml` against `solr-9759-admin-ui-stream-post.yml`); two fragments would collide. Not a gain.

---

## Could not settle

- Jira status of SOLR-18277 (Fixed on 2026-10-06 per the assignment) was not checked; no Jira call in this round.
- Review decisions on PR #4999 were not checked (state OPEN only).
- PR #5034 head ancestry (see item 2).
- `g4959-harden2.log` and the harden record are not in this worktree, so the INCONCLUSIVE seed and gate detail are unverified here.
- No branch, PR or receipt was changed; the claim is left for the coordinator's DONE mark.

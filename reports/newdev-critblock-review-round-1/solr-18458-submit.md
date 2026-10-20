# Review round 1, unit 3: SOLR-18458 (branch solr-18458-submit), vm4

## Pass 1 (vm4, 2026-10-20T14:48:24Z)

- Head reviewed: `344485485fb8e1706a1e04c02e373d8a97bcd1b8` (live tip, unchanged). Base `c8ab71430f`. Diff: 60 files, 250 insertions, 249 deletions (docs, SolrJ constant, LTR and core tests).
- Ticket SOLR-18458 (Open): "Drop support for debugQuery=true in lieu of debug=all." Checked the branch against it.

### Findings

1. **Minor (severity minor).** `debugQuery` is now ignored without any log line. The ticket says "we could use DeprecationLog to alert users in 10.x." The branch does not add a warning, so a client that still sends `debugQuery=true` gets no debug output and no hint why. The ticket does not require the warning, so this is recorded, not repaired (outside the repair scope: it changes behaviour).
2. **Public API removal (intended, recorded).** `CommonParams.DEBUG_QUERY` is removed from the SolrJ public interface and `SolrQuery.setShowDebugInfo` now sets `debug` instead. This follows the ticket. The changelog entry (`type: removed`) names the change. Confirmed: no remaining `CommonParams.DEBUG_QUERY` reference in `solr/core`, `solr/solrj` or `solr/modules` at the head.
3. **Legacy param pinned.** `MoreLikeThisHandlerTest` asserts that `debugQuery=true` does not add a debug section, so the removal is covered by a test that would fail if the param came back.
4. **Docs.** Five reference-guide pages and the admin UI query partial move from `debugQuery` to `debug`. Checked the query guide page list in the diff; no page still points at `debugQuery`.

Repairs in pass 1: none.

### Pass 1 regate

- Gate job: `gates/SOLR-18458-review-pass-1.md` (log directory `gates/logs/vm4/18458/`). Worktree at `344485485f`, detached.
- Step 0 changelog parse: rc 0 via uv (`step0-uv.log`). The system Python has no PyYAML, so the first attempt in `pass1.log` (rc 1) was a host tool gap and not a changelog failure.
- Step 1 tidy: rc 0, `git status` clean afterward (`step1-tidy-status.txt` empty).
- Step 2 Error Prone compile, four modules main and test: rc 0 (`step2-errorprone.log`).
- Step 3 proof leg: not run in pass 1. The receipt's proof leg (round 3) is recorded there; this pass does not repeat it.
- Step 4 focused head run, seed `18458C0FFEE18458`, counted from fresh XML (copies in `gates/logs/vm4/18458/xml/`):
  - `solr/core`: 30 classes, 303 tests, 2 skipped, 0 failures, 0 errors.
  - `solr/solrj`: 1 class (CloudSolrClientRoutingTest), 2 tests, 0 failures, 0 errors.
  - `solr/modules/ltr`: 15 classes, 91 tests, 1 skipped, 0 failures, 0 errors.
  - `solr/modules/language-models`: 1 class, 23 tests, 0 failures, 0 errors.
- Step 5 module checks (`check -x test`): rc 0 for `solr:core`, `solr:solrj`, `solr:modules:ltr`, `solr:modules:language-models`.
- Failed attempts before the green run, recorded:
  1. Step 4 combined in one Gradle call: all four modules' `--tests` filters in one invocation; the fail-on-no-tests guard stopped the build (`step4-focused.log`, rc 1). This was a runner invocation mistake, not a test result.
  2. Step 4 per module, class names missing the `org.` prefix (my list had been built with a sed that dropped it): the guard stopped every module (`step4-core.log` and the others, rc 1). Fixed in `tests.txt`; the class list now has 47 `org.apache` names.
  3. Step 4 per module, fixed list: green (above).

Pass 1 result: GREEN at `344485485f`. Pass 2 starts on the same head.

## Pass 2 (vm4, 2026-10-20T15:25:10Z)

- Head reviewed: `344485485fb8e1706a1e04c02e373d8a97bcd1b8` (unchanged from pass 1). Base `c8ab71430f`.
- Review focus: the behaviour pinned by tests, not only the code. Checked that the legacy parameter is pinned on both the standalone and the SolrJ paths, and that `SolrQuery.setShowDebugInfo` sends `debug` (`DebugComponentTest.testSolrQueryShowDebugInfoUsesDebugParameter`).
- Proof leg (step 3), run on base `c8ab71430f` with the head's `DebugComponentTest.java` overlaid: 8 tests, 2 failed, as expected. `testLegacyDebugQueryParameterIgnored` fails with `count(//lst[@name='debug'])=0` (base still honours `debugQuery`). `testSolrQueryShowDebugInfoUsesDebugParameter` fails for the same reason (base sets `debugQuery` in `setShowDebugInfo`). Both tests therefore pin the change.
- Findings: the pass 1 minor finding stands (silent drop with no DeprecationLog warning). No new findings.
- Repairs in pass 2: none.
- Regate: green (see the gate job `gates/SOLR-18458-review-pass-2.md`). Counts from fresh XML match pass 1: core 30/303 (2 skipped), solrj 1/2, ltr 15/91 (1 skipped), language-models 1/23, zero failures. Tidy rc 0, tree clean. Module checks rc 0 on four modules.

Pass 2 result: GREEN at `344485485f`. Pass 3 starts on the same head.

## Pass 3 (vm3, 2026-10-20T21:58:22Z, taken over from vm4's stale pass-3 slot)

- Head reviewed: `344485485fb8e1706a1e04c02e373d8a97bcd1b8` (unchanged). Base `c8ab71430f`. Passes 1 and 2 reused, not redone.
- Review focus: the production paths of the four core files, the SolrJ constant removal, and the ReRank fallback. Checked the MoreLikeThis and SearchHandler changes against the ticket: `debug=all` and `debug=true` both enable the debug paths, and the legacy `debugQuery` key is no longer read.
- Finding (minor, new in pass 3): `ReRankQParserPlugin` fallback (used only without SearchHandler and ResponseBuilder) now returns true for `debug=results` as well as `debug=all` and `debug=true`. Before, only `debug=true` counted. The ticket asks for `debug=all`; the widening is not asked for and no test pins it. It changes behaviour, so it is recorded, not repaired (outside the repair scope). Owner's call whether to keep it or narrow it to the ticket.
- Repairs in pass 3: none.
- Regate: `gates/SOLR-18458-review-pass-3.md`, GREEN at `344485485f`. Logs `gates/logs/vm3/solr-18458-review-pass-3/`. Changelog rc 0; tidy rc 0 with an empty status; Error Prone compile of four modules rc 0; focused runs with seed 18458C0FFEE18458, counted from fresh XML: core 28 classes, 290 tests, 2 skipped, 0 failures, 0 errors; solrj 1 class, 2 tests, 0 failures; ltr 15 classes, 91 tests, 1 skipped, 0 failures; language-models 1 class, 23 tests, 0 failures; module checks rc 0.
- Count note: pass 2 counted 30 core classes and 303 tests. This pass counted the touched test files that contain `@Test` (28 classes). The two-class difference is not reconciled here; both counts are zero-failure.

Pass 3 result: GREEN at `344485485f`. Unit 3 is complete after pass 3.

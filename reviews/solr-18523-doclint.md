# Review: assignment-18523-doclint (SOLR-18523, DocLint switch)

- Reviewer: Claude Code agent (Solr-issues workspace), for the branch owner
- Date: 2026-10-08
- Branch: origin/assignment-18523-doclint (fork nick-boss-tech/solr)
- Submit branch reviewed: solr-18523-submit at 43f57c1b853 (apache/solr main d94c5eeb6b5)
- Commits on this branch above the submit tip:
  - f2dacbd51c8 Claim SOLR-18523 DocLint follow-up review
  - 7b6aa631486 SOLR-18523: Run the documentation build from check only on CI
  - this file (reviews/solr-18523-doclint.md)

## Verdict: Ready with notes

The submit branch removes the right things. It differs from Lucene's merged apache/lucene#14905 in one place: the `check` to `documentation` edge is unconditional. The adjustment in 7b6aa631486 restores Lucene's CI gate. Before the PR opens, three things remain:

1. Fold 7b6aa631486 into solr-18523-submit, or decide to keep the unconditional edge and revert the commit here.
2. Run Gradle on the adjusted tree. Not done here; see the re-verification record.
3. Maintainer decision on local `check` coverage, described under "Coverage trade-off". It is the same trade Lucene made.

## Premise correction

The assignment says Lucene's crawl "covered javadoc HTML only". The deleted Lucene file in #14905, `build-tools/build-infra/src/main/groovy/lucene.documentation.check-broken-links.gradle`, sets `docsDir` to `file(project.docroot)`. That is the whole documentation site, the same scope as Solr's crawl. Lucene accepted the site-wide trade in #14905, so the Choice in the PR draft has a Lucene precedent. No javadoc-only scope adjustment is needed.

## Lucene follow-ups: candidates and dispositions

| # | Candidate (Lucene source) | Disposition | Grounding in the Solr tree |
| --- | --- | --- | --- |
| 1 | `check` depends on `documentation` only when `isCIBuild`: #14905 commit "Use global extension flag to detect CI builds" (20484b251fa, merged 2025-07-07); kept by #15350 (a0bd3c5f3c6) | **Applies. Implemented in 7b6aa631486** | `gradle/documentation/documentation.gradle` had an unconditional `check.dependsOn 'documentation'`. `isCIBuild` is already defined in `gradle/globals.gradle:175`, inside `project.ext { }` in `allprojects`, which runs before `documentation.gradle` is applied (`build.gradle:211`). |
| 2 | #14905 render-javadoc change: drop `check` → `renderJavadoc` for non-published projects | Does not apply | Solr has no such edge. No `check.dependsOn` in `gradle/` references `renderJavadoc`. The javadoc alias in `gradle/documentation/render-javadoc.gradle:41-45` is not attached to `check`. |
| 3 | #14903 (merged 2025-07-07): overview.html `-overview` path bug | Does not apply | Solr builds `"${srcDirs[0]}/overview.html"` from a directory (`render-javadoc.gradle:322`). The file-as-directory bug cannot occur. Solr's 15 javadoc overview files (`src/java/overview.html` in each module) are validated by `renderSiteJavadoc`, which the CI gate runs. |
| 4 | #15350 (a0bd3c5f3c6): documentation scripts moved to Java (`DocumentationConfigPlugin.java`, `RenderJavadocPlugin.java`) | Does not apply | Structural. Solr stays on Groovy. The Java file keeps the same CI gate. No later commit touches `DocumentationConfigPlugin.java`. |
| 5 | #14862 (a7fc3d0ae83, 2025-07-01, before #14905): javac config moved to Java | No change needed | Lucene's `JavacConfigurationPlugin.java` carries `-Xdoclint:all/protected`, `-Xdoclint:-missing`, `-Xdoclint:-accessibility` and `-Werror`, the same as `gradle/java/javac.gradle:58-60`. The javadoc tool's `-Xdoclint:all,-missing` (Lucene `RenderJavadocPlugin.java`) matches `render-javadoc.gradle:422`. |
| 6 | #14912 and #14914 (c30f23a082c, 5893b81a634, 2025-07-07): `-Xlint:path` toggled off and back on | Out of scope | javac lint, not DocLint or link checking. Solr's `javac.gradle` has no `-Xlint:path`. |
| 7 | Later Lucene javac lint-list changes (e.g. `-Xlint:dangling-doc-comments`, `-Xlint:restricted`, `-Xlint:auxiliaryclass`; edbd15d4477 "identity synchronization lint") | Out of scope | Solr's `javac.gradle` has no `dangling-doc-comments`. This is lint-set alignment, not part of removing the crawl. Not implemented. |
| 8 | #15326 "Add matomo tracker for javadoc", reverted by c365b405cee | No effect | Added and reverted; net zero. |
| 9 | #15568 (ee305c2c4d0, 2026-01-14): markdown javadocs | No wiring change | Its commit is not in the later histories of `DocumentationConfigPlugin.java` or `RenderJavadocPlugin.java`. |
| 10 | #16592 (merged 2026-09-05): build infra backport | No effect on this machinery | Its file list has no documentation, javadoc or javac wiring. The missing-doclet `build.gradle` was only moved. |
| 11 | Lucene javadoc content fixes after #14905 (e.g. #15598, #15625, #15719, #16508, #16766) | Content only | Comments, not wiring. Solr's full-tree compile with reference checks is green (established fact in the assignment). |
| 12 | #15235 (92888a75b44, 2025-09-27): changes2html ported to Java | Not a crawl or DocLint follow-up | Relevant to the java port (below), not to this branch's files. |
| 13 | #14287 (2025-02-25): checkJavadocLinks detects invalid references | Predates #14905 | Not a follow-up. |

Other notes:

- Solr's `missing-doclet` and `--missing-level` checks (`render-javadoc.gradle:26-34, 361`) already exist. They are not a Lucene follow-up. They run inside `renderSiteJavadoc`, so the CI gate moves them to CI too (see Coverage trade-off). The `missing` DocLint group stays out of scope, as the assignment states.
- Title searches of apache/lucene PRs for "part 2", follow-up, javadoc, doclint, checkJavadocLinks, broken links and modernize found no follow-up PR to #14905, open or merged.
- Content searches of Lucene commits returned nothing usable, so these dispositions rest on per-file history. The files checked since 2025-07-07 are `lucene.documentation.gradle`, `lucene.documentation.render-javadoc.gradle`, `JavacConfigurationPlugin.java`, `RenderJavadocPlugin.java` and `DocumentationConfigPlugin.java`.
- Depth varies. For `JavacConfigurationPlugin.java` I judged the later commits from their titles and the current file. I did not diff each commit. The root `build.gradle` history since 2025-07-01 was listed, not diffed.

## Adjustment commits

- **7b6aa631486** "SOLR-18523: Run the documentation build from check only on CI". Changes `gradle/documentation/documentation.gradle` (+6/−4): `check.dependsOn 'documentation'` moves inside `if (isCIBuild) { }`. The comment now describes the standing behavior.
  - Mirrors apache/lucene#14905 and its later Java port (#15350).
  - One commit, reversible.

## Coverage trade-off (maintainer decision)

- Before SOLR-18523, every local `check` ran `documentation`. The old `checkBrokenLinks` depended on it, and `:solr:documentation`'s `check` depended on `checkBrokenLinks`.
- With 7b6aa631486, a local `check` no longer runs `documentation`. The full-site crawl's coverage, overview.html validation (`-overview` with javadoc's `-Xdoclint:all,-missing`) and the missing-doclet level checks now run only when `isCIBuild` is true. That means `CI` set, or `JENKINS_*` / `HUDSON_*` variables.
- Lucene made the same change in #14905. The submit branch's comment claimed the unconditional edge exercises overview references. The gate removes that local coverage.
- Keep or revert 7b6aa631486 on the maintainers' call. The PR's Choice should name this, alongside the site-wide coverage trade.

## Relation to the java port (solr-18119-jvm)

- SOLR-18523 deletes the Python crawl that the java port reimplements: `CheckJavadocLinks.java`, `CheckBrokenLinksTask.java`, and the URL helpers in `PythonCompat.java`. If SOLR-18523 lands, the port has nothing to replace. Findings 2 and 3 of the 18119 review apply only if the crawl is kept.
- The branches overlap on one file. `gradle/validation/check-broken-links.gradle` is modified by solr-18119-jvm and deleted here. Combining them produces a modify/delete conflict.
- The changes-to-html part of the port (`changes-to-html.gradle`, `ChangesToHtml.java`) is untouched by SOLR-18523 and stays relevant. Lucene made the same kind of port in #15235.

## Re-verification record

- **Platform:** Windows 11, PowerShell 5.1 and Git for Windows. No JDK or Gradle run.
- **Ran: static wiring proof.** `research/verify-solr18523.ps1` against `wt/assignment-18523-doclint`. Result: 7/7 PASS, exit 0.
  - One `check.dependsOn 'documentation'`, inside `if (isCIBuild)`.
  - No check-broken-links apply in `build.gradle`.
  - `gradle/validation/check-broken-links.gradle` and `dev-tools/scripts/checkJavadocLinks.py` absent.
  - DocLint flags in `javac.gradle` unchanged.
  - No tracked references to the removed crawl outside changelog, claims, reviews and ASSIGNMENT.md.
- **Negative control.** The gate pattern matches 0 times on 43f57c1b853 and 1 time on 7b6aa631486, so the proof discriminates.
- **Queued, not drained.** `test-queue.ps1 enqueue -Issue SOLR-18523 -ProofScript verify-solr18523.ps1`.
- **Not run: Gradle.** Workspace rule: Gradle is forbidden during ticket work and runs only when the user asks to verify. So `:solr:documentation:check -x test` and the root `check --dry-run` have not run on 7b6aa631486. The assignment's Linux gate covers 43f57c1b853 only.
- **Commands for the verify session** (JDK 21):
  - `CI=true ./gradlew :solr:documentation:check -x test` should run `documentation`.
  - `./gradlew :solr:documentation:check -x test` without CI should succeed and not run `documentation`.
  - `CI=true ./gradlew check --dry-run` should show `:solr:documentation:documentation` and no `checkBrokenLinks`.
  - `./gradlew check --dry-run` without CI should show neither `documentation` under `:solr:documentation` nor `checkBrokenLinks`.
  - Known caveats from the assignment still apply: `:rat` and `:solr:solr-ref-guide:buildLocalAntoraSite` in a linked-worktree setup.

## Open items

1. Fold 7b6aa631486 into solr-18523-submit, or revert it here if the unconditional edge is kept.
2. Run the Gradle commands above on the adjusted tree.
3. Decide the local-coverage trade-off for the PR's Choice.
4. Resolve the modify/delete conflict on `gradle/validation/check-broken-links.gradle` if the java port and SOLR-18523 are combined.

Nothing was posted to GitHub or Jira. No changes were made to solr-18523-submit or solr-18119-jvm.

## Correction (2026-10-08, after the paired review)

Two statements above give the gate a wider scope than it has. The corrected scope is:

- The `isCIBuild` gate covers `:solr:documentation:check` only: that project's `check` builds the `documentation` output only when `isCIBuild` is true.
- A root `check` still builds the documentation through a different path. When the ref guide is included (`refguide.include`, on by default when a `.git` directory exists), the ref guide's `check` runs `checkSiteLinks`, which needs the documentation site, so `:solr:documentation:documentation` runs on a root `check` with or without CI.

The earlier statements are superseded by this correction: the Coverage trade-off line "a local `check` no longer runs `documentation`", and the verify-session expectation that a root `check --dry-run` without CI shows no `documentation`. Both hold only for `:solr:documentation:check` itself. The paired review of the SOLR-18119 / SOLR-18523 branches records the same correction.

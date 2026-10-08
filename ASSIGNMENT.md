# Assignment: Lucene follow-ups check for solr-18523-submit (SOLR-18523, DocLint switch)

Research and, where an adjustment clearly applies, implement. Deliverables go on THIS branch (`assignment-18523-doclint`). Do not push to `solr-18523-submit`, do not open a PR, and do not comment on GitHub or Jira.

## The branch under work

- Branch: `solr-18523-submit` on fork `nick-boss-tech/solr`, tip `43f57c1b853` on `apache/solr` main `d94c5eeb6b5`. This assignment branch is that tip plus this file.
- The change implements SOLR-18523 ("build: javadoc validation: switch to DocLint", filed by David Smiley, modeled on Lucene PR apache/lucene#14905): it deletes `dev-tools/scripts/checkJavadocLinks.py` and `gradle/validation/check-broken-links.gradle`, and makes `:solr:documentation:check` depend on the `documentation` task directly (`gradle/documentation/documentation.gradle`). Five files, +13/-359, plus the changelog fragment `changelog/unreleased/SOLR-18523.yml`.
- Gate state (Linux, 2026-10-08): `:solr:documentation:check -x test` BUILD SUCCESSFUL; task graph verified (no `checkBrokenLinks` in the root `check --dry-run` graph, `:solr:documentation:documentation` present). Root `check -x test` stops at `:rat` and at `:solr:solr-ref-guide:buildLocalAntoraSite` in a linked-worktree setup; both reproduce identically on unmodified main, so neither is caused by this branch. Treat these as known environment caveats, not findings.

## Why this assignment exists

Smiley, on SOLR-18523 (2026-10-08T19:13Z): "Someone taking this up should look in Lucene for any followups / adjustments. There very likely are some, since after all the PR referenced is labelled as 'part 1'."

The branch was modeled on Lucene #14905 as merged. If Lucene adjusted that wiring afterwards (a part 2, fixes, exclusions, changed `check`/`documentation` edges, javadoc link handling, anything in the same files), the Solr change should mirror whatever applies before the PR opens.

## Task

1. Claim first: commit `claims/solr-18523-doclint.md` on this branch, following the channel's usual pattern, before starting work.
2. Research Lucene's history after apache/lucene#14905 for follow-ups touching the same machinery: the deleted link-crawl script and its wiring, the `check` to `documentation` dependency, DocLint configuration in the javac/javadoc Gradle files, and any replacement validation Lucene added later. Sources: the apache/lucene GitHub repo (PRs, commits, current file state) and the Lucene Jira if a PR references one. Record each candidate follow-up with its PR number or commit hash and a one-line summary.
3. Disposition each candidate against the Solr tree as this branch leaves it: applies, or does not apply, with the reason grounded in Solr's actual files (not analogy alone). Note in particular that Solr's crawl covered the entire documentation site (docroot: all subprojects' rendered javadoc, Changes.html, markdown and assets), while Lucene's covered javadoc HTML only; a Lucene follow-up that assumes javadoc-only scope needs that difference addressed explicitly.
4. For each follow-up that clearly applies, implement the adjustment as its own commit on THIS branch (stacked on the submit tip), with the commit message naming the Lucene source it mirrors. Keep adjustments minimal and wiring-shaped; do not redesign.
5. Re-verify after any adjustment: `:solr:documentation:check -x test` green and the root `check --dry-run` task graph still showing no `checkBrokenLinks` and `documentation` present. Record what you ran, on which platform and toolchain, with results.
6. Deliverable: commit `reviews/solr-18523-doclint.md` on this branch with: the follow-up list and dispositions, the adjustment commits (if any) with their hashes, the re-verification record, and a closing verdict of Ready, Ready with notes, or Needs work, meaning: is `solr-18523-submit` (plus any adjustment commits here) ready to open as a PR once the main side folds the adjustments in?

## Established facts (from the scoping scan; do not redo)

- Solr already runs DocLint at compile time: `gradle/java/javac.gradle` applies `-Xdoclint:all/protected`, `-Xdoclint:-missing`, `-Xdoclint:-accessibility` plus `-Werror` to every JavaCompile task. Lucene #14905 changed no javac configuration; it only deleted the crawl and its wiring and added `check` -> `documentation`.
- Full-tree `compileJava` on main `d94c5eeb6b5` with `reference` checks widened to all scopes: BUILD SUCCESSFUL, 0 failing references, 0 failing modules. A planted bad `{@link}` fails `:solr:api:compileJava` under both the shipped and the widened configuration, so the compile-time check works.
- The `missing` DocLint group stays out of scope. Measured on main at the shipped `/protected` scope it reports 22,710 diagnostics (15,450 elements with no javadoc comment, 3,079 missing `@param`, 1,849 missing `@return`, 1,056 undocumented default constructors, 711 missing `@throws`, 565 empty or tag-only comments). That is a tree-wide documentation effort, not part of SOLR-18523; do not enable it, and do not treat its absence as a finding.
- The site-wide coverage trade (the crawl checked ref-guide and other non-javadoc pages; DocLint does not) is already posed as the Choice in the PR draft. It is a maintainer decision for the PR, not something this assignment resolves.

## Boundaries

- Do not push to `solr-18523-submit` or `solr-18119-jvm`; adjustments live on this branch until the main side folds them in.
- Do not open PRs and do not post on GitHub or Jira.
- If a Lucene follow-up turns out to be large or contentious, disposition it in the review file with reasons instead of implementing it.

# Assignment: code review of PR #5062 (SOLR-18523, DocLint switch)

Review only. Do not patch the branch under review. Findings go in a review file committed to THIS branch.

## What to review

- PR: https://github.com/apache/solr/pull/5062, from branch `solr-18523-submit` on fork `nick-boss-tech/solr`.
- Review the tip of that branch as it stands when you start. Record the exact head SHA in your review file. Expected shape: two commits on `apache/solr` main at `d94c5eeb6b5`: `43f57c1b853` "SOLR-18523: Remove the checkJavadocLinks crawl; javadoc links are validated by DocLint at compile time" and `8a746512bba` "SOLR-18523: Run the documentation build from check only on CI". Expected head: `8a746512bba`. If the branch has moved, note it and review what is actually there.
- The change deletes the documentation link crawl and relies on compile-time DocLint instead:
  - Deleted: `dev-tools/scripts/checkJavadocLinks.py` (277 lines) and `gradle/validation/check-broken-links.gradle` (the inline `CheckBrokenLinksTask`, the `:solr:documentation:checkBrokenLinks` task, the root `checkBrokenLinks` alias), plus the `apply from` line in the root build.
  - `gradle/documentation/documentation.gradle`: `:solr:documentation:check` now depends on the `documentation` task, inside `if (isCIBuild) { }`, mirroring apache/lucene#14905 as adjusted by its later Java port (#15350). Local `check` no longer builds the documentation; CI still does.
  - Changelog fragment `changelog/unreleased/SOLR-18523.yml` added.
  - Dev-docs references to the removed check swept.

## Context

- Jira SOLR-18523 (filed by David Smiley, 2026-10-08): "build: javadoc validation: switch to DocLint". It copies Lucene PR apache/lucene#14905 (Robert Muir), which deleted Lucene's equivalent crawl because `javac` DocLint `reference` checks fail compilation on invalid `{@link}` references. Smiley's only comment so far asks that whoever takes it up check Lucene for follow-ups to #14905 (labelled "part 1").
- That follow-ups audit is DONE (assignment branch `assignment-18523-doclint`, verdict file `reviews/solr-18523-doclint.md` on that branch): 13 Lucene candidates dispositioned; the one that applied was the CI gate on the `check` -> `documentation` edge, now the second commit on this branch. The audit also corrected a premise: Lucene's deleted crawl also read `file(project.docroot)`, the whole documentation site, so the site-wide coverage trade is the same one Lucene accepted in #14905.
- SOLR-18119 is the parent discussion. PR #5061 (the pure-Java port) overlaps this branch on one file: it modifies `gradle/validation/check-broken-links.gradle`, which this branch deletes. If #5062 merges first, #5061 gets slimmed to its `ChangesToHtml` half. A line in your review on the interaction is welcome but the sequencing is not yours to decide.
- The `missing` DocLint group stays disabled by design. Measured on main at the shipped `/protected` scope it reports 22,710 diagnostics (a tree-wide javadoc completion effort). Out of scope; do not treat its absence as a finding.

## Verification already done (Linux, 2026-10-08)

Treat these as claims to sanity-check against the code and the PR description where you can, not as substitutes for reading. If a claim cannot be corroborated from the tree, say so in the review rather than repeating it as fact.

- DocLint state: `gradle/java/javac.gradle` applies `-Xdoclint:all/protected`, `-Xdoclint:-missing`, `-Xdoclint:-accessibility` plus `-Werror` to every JavaCompile task. Full-tree `compileJava` on main `d94c5eeb6b5` with `reference` checks widened to all scopes: BUILD SUCCESSFUL, 0 failing references. A planted bad `{@link}` fails `:solr:api:compileJava` under the shipped and the widened configuration.
- Gate on the final head: `CI=true :solr:documentation:check -x test` BUILD SUCCESSFUL in 5m 5s with the documentation genuinely built (8,225 HTML files); without CI the same command succeeds in 1m 19s and the documentation build does not run. Dry-run pair on `:solr:documentation:check`: `documentation` present with `CI=true`, absent without; `checkBrokenLinks` absent from all graphs.
- Caveat recorded in the PR description: root `check -x test` stops at `:rat` and at `:solr:solr-ref-guide:buildLocalAntoraSite` in a linked-worktree setup, identically on unmodified main; neither is caused by this branch.
- The crawl step removed was measured at 4m 18s on top of a full documentation build.

## Review focus

- Completeness of the deletion: anything left referencing `checkBrokenLinks` or `checkJavadocLinks` anywhere in the tree (Gradle files, dev-docs, ref-guide sources, CI configuration under `.github`, scripts)? Anything the deleted `.gradle` file provided besides the crawl (task aliases other builds or docs rely on)?
- The wiring change in `documentation.gradle`: is `isCIBuild` defined before this file is applied (see `gradle/globals.gradle` and the root build's apply order)? Is the comment on the new block accurate about the behavior it describes, including that overview.html validation and the missing-doclet checks also move to CI only?
- The changelog fragment: parses as YAML, type and title appropriate for a build change.
- The PR description (read it on GitHub): do its claims match the branch? In particular the Proof measurements, the two trades named in the Choice (site-wide crawl coverage retired; local `check` coverage reduced to CI), and the Limits. Flag any claim that overstates what the branch or the recorded verification shows.
- Anything a maintainer would flag before merging: leftover references, misleading comments, an edge that behaves differently on Apache Jenkins (which sets `JENKINS_*` variables) than the description implies.

## Deliverable

- Claim the assignment first with `claims/solr-18523-pr.md` on this branch, following the channel's usual pattern.
- Commit `reviews/solr-18523-pr.md` to THIS branch with: the head SHA reviewed, findings by severity, and a closing verdict of Ready, Ready with notes, or Needs work.
- Do not open or comment on the PR, do not comment on Jira, and do not push to `solr-18523-submit`.

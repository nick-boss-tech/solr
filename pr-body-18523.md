🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18523

## What happens today

**The documentation build still crawls the built site with a Python script to find broken javadoc links, even though those references are already validated at compile time.**

- `:solr:documentation:check` depends on `checkBrokenLinks`, which builds the full documentation site and crawls it with `dev-tools/scripts/checkJavadocLinks.py` (277 lines) under python3. The crawl was a hard python3 dependency: it resolves python3 through `externalTool(...)` and fails the build when python3 is absent.
- The javadoc part of that validation already happens earlier: [`gradle/java/javac.gradle:58-60`](https://github.com/nick-boss-tech/solr/blob/c0a3972d6677bc8a43b7921a0da87f9cf0e2a7d1/gradle/java/javac.gradle#L58-L60) applies `-Xdoclint:all/protected`, `-Xdoclint:-missing` and `-Xdoclint:-accessibility` to every JavaCompile task, with `-Werror`, so an invalid `{@link}` in public or protected javadoc fails compilation before any HTML exists.
- The crawl step alone measured 4m 18s on top of the documentation build it depended on (measured 2026-10-08 on `main` at `d94c5eeb6b5`).
- Lucene removed the equivalent crawl in [apache/lucene#14905](https://github.com/apache/lucene/pull/14905) for the same reason.

## What this change does

**The crawl and its wiring are deleted, and `:solr:documentation:check` builds the documentation only on CI.**

- Deleted: `dev-tools/scripts/checkJavadocLinks.py`, `gradle/validation/check-broken-links.gradle` (the `CheckBrokenLinksTask` class, the `:solr:documentation:checkBrokenLinks` task and the root `checkBrokenLinks` alias), and the `apply from` line in the root build.
- `gradle/documentation/documentation.gradle`: `:solr:documentation:check` depends on `documentation` inside `if (isCIBuild) { }`, the shape Lucene used in #14905 and kept in its Java port (#15350). The gate covers this project's check path only.
- Companion change: the documentation build's other Python script, `changes2html.py`, is ported to Java in [the PR for SOLR-18119](https://github.com/apache/solr/pull/5061). The two changes touch disjoint files, so they can merge in either order (a test merge of the two heads is recorded on the companion PR).
- Not addressed here: the silent skip at the heart of [SOLR-18119](https://issues.apache.org/jira/browse/SOLR-18119). [`gradle/documentation/changes-to-html.gradle:71-77`](https://github.com/nick-boss-tech/solr/blob/c0a3972d6677bc8a43b7921a0da87f9cf0e2a7d1/gradle/documentation/changes-to-html.gradle#L71-L77) still warns and skips `Changes.html` generation when python3 is missing (`pythonExists()` at [`:108`](https://github.com/nick-boss-tech/solr/blob/c0a3972d6677bc8a43b7921a0da87f9cf0e2a7d1/gradle/documentation/changes-to-html.gradle#L108)), so `changes2html.py` remains a soft python3 dependency of the documentation build. On a host without python3, the failure SOLR-18119 reports now surfaces at the ref guide's `checkSiteLinks` instead of at `checkBrokenLinks`; SOLR-18119 stays open for that behavior.

## Proof

**Everything below was run on 2026-10-08 at head `c0a3972d667` on `main` at `9becf6c6a15`, including two planted-error controls; the DocLint scan line is labeled with its own base.**

- `CI=true :solr:documentation:check -x test`, with the documentation build directory deleted first: BUILD SUCCESSFUL in 5m 54s. `documentation`, `changesToHtml`, `markdownToHtml`, `createDocumentationIndex` and `copyDocumentationAssets` all executed; the built site holds 8,243 HTML files. `checkBrokenLinks` appears nowhere in the run.
- Scoped dry-run pair on `:solr:documentation:check`: with `CI=true` the graph contains `:solr:documentation:documentation`; without CI it does not. `checkBrokenLinks` is absent from both graphs.
- Negative control, overview references: a planted `{@link org.apache.solr.core.NoSuchClassForControl}` in `solr/core/src/java/overview.html` makes the CI run fail at `:solr:core:renderSiteJavadoc` with `overview.html:20: error: reference not found` (BUILD FAILED in 2m 18s). The javadoc tool is the only check of overview references: it runs with `-Xdoclint:all,-missing` ([`gradle/documentation/render-javadoc.gradle:422`](https://github.com/nick-boss-tech/solr/blob/c0a3972d6677bc8a43b7921a0da87f9cf0e2a7d1/gradle/documentation/render-javadoc.gradle#L422)), while compile-time DocLint never reads overview files. The render had to be forced to re-execute for this control, because `overview.html` is not a declared input of the cacheable `renderSiteJavadoc` task; the run used a deleted site output and `--no-build-cache`. The plant was removed afterward.
- Negative control, docroot-only links: a planted link to `changes/NoSuchControlPage.html` in `solr/documentation/src/markdown/index.template.md` leaves the CI run green (BUILD SUCCESSFUL in 1m 38s) while the built `site/index.html` contains the broken `href` and the target page does not exist in the site. Nothing in the check path flags it. The plant was removed afterward.
- DocLint scan, measured on `main` at `d94c5eeb6b5`, the commit directly behind this branch's base (the one intervening commit, SOLR-18515, touches no javadoc or build wiring): full-tree `compileJava` with `reference` checks widened beyond the shipped `all/protected` scope to all scopes passed with 0 failing references. A planted bad `{@link}` fails `:solr:api:compileJava` under the shipped configuration (public and protected elements) and under the widened one.
- Lucene follow-ups audit: 13 candidate follow-ups to #14905 were dispositioned against this tree; the CI gate on the `check` edge was the one that applied, and it is the second commit on this branch. Record: [`reviews/solr-18523-doclint.md`](https://github.com/nick-boss-tech/solr/blob/assignment-18523-doclint/reviews/solr-18523-doclint.md) on the `assignment-18523-doclint` branch.

## A choice to check

**Following Lucene retires two kinds of coverage, and both are implemented the way Lucene made them.**

- Docroot-only page coverage: the crawl was the only check of links whose source page lives in the docroot rather than the ref guide, such as the markdown index ([`solr/documentation/src/markdown/index.template.md:27`](https://github.com/nick-boss-tech/solr/blob/c0a3972d6677bc8a43b7921a0da87f9cf0e2a7d1/solr/documentation/src/markdown/index.template.md#L27)) and `SYSTEM_REQUIREMENTS.html`. The markdown negative control in Proof shows a broken link of exactly that kind now passing. Ref-guide links are not part of this trade: `checkSiteLinks` stays in the check graph and checks "all links in the ref-guide, both internal and java-docs" ([`solr/solr-ref-guide/build.gradle:475`](https://github.com/nick-boss-tech/solr/blob/c0a3972d6677bc8a43b7921a0da87f9cf0e2a7d1/solr/solr-ref-guide/build.gradle#L475)); it is the check that failed on the ref-guide link to the missing `Changes.html` in SOLR-18119's report.
- Local check coverage: a local `:solr:documentation:check` no longer builds the documentation, so the javadoc-tool-only checks (overview references, missing-doclet levels) run on CI only for this project's check path. A root `check` still reaches the documentation build through the ref guide: `check.dependsOn 'checkSiteLinks'` ([`solr/solr-ref-guide/build.gradle:58`](https://github.com/nick-boss-tech/solr/blob/c0a3972d6677bc8a43b7921a0da87f9cf0e2a7d1/solr/solr-ref-guide/build.gradle#L58)); `checkSiteLinks` depends on `buildLocalSite`, which consumes the `:solr:documentation` `site` configuration ([`solr/solr-ref-guide/build.gradle:76`](https://github.com/nick-boss-tech/solr/blob/c0a3972d6677bc8a43b7921a0da87f9cf0e2a7d1/solr/solr-ref-guide/build.gradle#L76), [`:464-478`](https://github.com/nick-boss-tech/solr/blob/c0a3972d6677bc8a43b7921a0da87f9cf0e2a7d1/solr/solr-ref-guide/build.gradle#L464-L478)); and that artifact is `builtBy documentation` ([`gradle/documentation/documentation.gradle:100`](https://github.com/nick-boss-tech/solr/blob/c0a3972d6677bc8a43b7921a0da87f9cf0e2a7d1/gradle/documentation/documentation.gradle#L100)). Gating that path too would be a separate, larger change.
- Lucene's deleted crawl also read `file(project.docroot)`, the whole site, so the first trade is the one Lucene accepted in #14905, and the second is the gate Lucene added there and kept in #15350.

Implemented position: follow Lucene on both trades, rather than keep a reduced crawl for the docroot-only pages or an unconditional `check` edge for this project only. Were these the right calls?

## Limits

**Three bounds on this change are worth naming: the disabled `missing` group, the CI detection semantics, and the removed task name.**

- The `missing` DocLint group stays disabled (`-Xdoclint:-missing` is untouched). Measured on `main` at the shipped `/protected` scope, enabling it reports 22,710 diagnostics: 15,450 public or protected elements with no javadoc comment, 3,079 missing `@param`, 1,849 missing `@return`, 1,056 undocumented default constructors, 711 missing `@throws`, 565 empty or tag-only comments. That is a tree-wide javadoc completion effort, not part of this change; happy to open a follow-up ticket for it.
- `isCIBuild` matches environment variable names, not values ([`gradle/globals.gradle:175`](https://github.com/nick-boss-tech/solr/blob/c0a3972d6677bc8a43b7921a0da87f9cf0e2a7d1/gradle/globals.gradle#L175) tests `System.getenv().keySet()`), so `CI=false` on a developer machine still enables the gate, as does any `JENKINS_*` or `HUDSON_*` variable. Apache Jenkins sets `JENKINS_*`, so CI behavior there is as described.
- Nothing in this repo invokes `checkBrokenLinks` by name: the GitHub precommit workflow runs `check -x test` ([`.github/workflows/gradle-precommit.yml:26`](https://github.com/nick-boss-tech/solr/blob/c0a3972d6677bc8a43b7921a0da87f9cf0e2a7d1/.github/workflows/gradle-precommit.yml#L26)), and a tree search finds no other caller. The task name disappears with this change; job configurations outside this repo are not visible from here, so a caller there would have to move to `check`.
- `compileTestJava` carries the same DocLint configuration as `compileJava`, because the flags apply to every JavaCompile task, but it was not measured separately; the crawl never checked test javadoc either.

Changelog: `changelog/unreleased/SOLR-18523.yml`

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.

# Review: PR #5062 (SOLR-18523, DocLint switch)

- Reviewer: Claude Code agent (Solr-issues workspace), for the branch owner
- Date: 2026-10-08
- Review branch: origin/review-18523-pr (fork nick-boss-tech/solr), claim at 6748392ac1b
- PR: https://github.com/apache/solr/pull/5062, from `solr-18523-submit` on the fork
- Head reviewed: `8a746512bbaed5a2217c7185349976bc2f501da2`. This matches the expected head. Two commits above apache/solr main `d94c5eeb6b5`: `43f57c1b853` and `8a746512bba`.
- PR base now: `9becf6c6a154`, one commit ahead of `d94c5eeb6b5` (SOLR-18515, PackageTool to picocli, #5047). It touches no file in the PR's diff. The PR reports MERGEABLE.
- Claim order: the claim (6748392ac1b) was pushed after the first reads of the PR, the branch and the PR description. The assignment asks for the claim first. That is recorded here rather than hidden.
- Not run: Gradle. The workspace rule forbids it during ticket work, and it runs only when the user asks to verify. The checks are static reads of the tree, plus one standalone JDK 21 experiment outside the repo (the javadoc and javac samples in the scratchpad, `doclint-overview/`).

## Verdict: Needs work

The code change is small and mechanically coherent. The deletion is complete in the tree, `isCIBuild` is defined before it is used, and the gate commit's content is identical to the one reviewed on the assignment branch. But the comment in `documentation.gradle` and the PR's central Choice both describe the behavior wrongly, and the PR asks maintainers to decide on exactly those trade-offs. The fixes are to the comment and the PR text, plus one verification step in the verify run. The Gradle code can stay as it is.

## Findings, most severe first

### 1. Major: a local root `check` still builds the documentation. The gate changes only `:solr:documentation:check`.

Where: `gradle/documentation/documentation.gradle:74-80` (the comment and the gate). The PR's "A choice to check", second trade: "a local `check` no longer builds the documentation at all".

The chain, all in the tree at `8a746512bba`:
- `solr/solr-ref-guide/build.gradle:58`: `check.dependsOn 'checkSiteLinks'`, inside the `refguide.include` guard. The default is `file("${rootDir}/.git").exists()`, which is true in a clone and in a linked worktree.
- `solr/solr-ref-guide/build.gradle:473-478`: `checkSiteLinks` depends on `downloadLinkValidator` and `buildLocalSite`.
- `solr/solr-ref-guide/build.gradle:464-465`: `buildLocalSite` depends on `buildLocalAntoraSite` and `configurations.localJavadocs`.
- `solr/solr-ref-guide/build.gradle:76`: `localJavadocs` includes `project(path: ":solr:documentation", configuration: 'site')`.
- `gradle/documentation/documentation.gradle:95-99`: the `site` artifact is `builtBy documentation`.
- An unqualified `check` run from the root executes every project's `check`. The PR's own caveat already shows the root check reaching `:solr:solr-ref-guide:buildLocalAntoraSite`.

So a local `./gradlew check` still runs `:solr:documentation:documentation`, including `renderSiteJavadoc` (overview validation and the missing-doclet level checks). The PR's Proof bullet says this ("documentation in both variants through :solr:solr-ref-guide"). The Choice and the code comment say the opposite.

Consequences:
- "check builds it only on CI" in the comment is false for the root check. It is true for `:solr:documentation:check` only.
- The "faster local check" effect is limited to the crawl's own time (4m18s as the PR measures it). The documentation build stays on the root path.
- The "CI-only coverage" trade is real only for `:solr:documentation:check`.

Suggested fix: scope the comment and the Choice to `:solr:documentation:check`, and say that the ref guide's `checkSiteLinks` still builds the site on a local root `check`. If maintainers want the gate to hold at the root, the ref guide's `localJavadocs` path has to be gated too. That is a larger, separate decision, not part of this minimal change.

### 2. Major: "links in the ref-guide pages ... are no longer checked by anything" is false

Where: the PR's "A choice to check", first trade, and "Behavior change, stated openly" in the description.

- `solr/solr-ref-guide/build.gradle:473`: `checkSiteLinks` is described as "Check that all links in the ref-guide, both internal and java-docs, are correct". It runs `link-checker --disable-external` over `${buildDir}/site` and stays in the check graph (finding 1).
- Ref-guide pages link into the docroot. `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-6.adoc:21`, `-7.adoc:26`, `-8.adoc:29` and `-9.adoc:29` link to `{solr-javadocs}/changes/...Changes.html`.
- The SOLR-18119 log, in the workspace note `research/115-solr18119-research-note.md` ("What The Ticket Says"), shows `:solr:solr-ref-guide:checkSiteLinks FAILED` for `solr-upgrade-notes.html` → `..\..\..\documentation\build\site\changes\Changes.html`, reason "page not found". That is the ref-guide-to-Changes.html case the PR uses as its example, and the check that stays catches it. The PR removes the crawl, not that check.
- What only the crawl covered: links whose source page is in the docroot and not in the ref guide. For example `solr/documentation/src/markdown/index.template.md:27` (`[Changes](changes/Changes.html)`). The same SOLR-18119 log shows the crawl's failure for `index.html → changes/Changes.html`.
- Not shown in the tree: how far `link-checker` follows links into the docroot beyond the ref guide's own pages. The log proves the `Changes.html` target only.

Suggested fix: state the trade as "docroot-only pages are no longer checked (the markdown index's links and SYSTEM_REQUIREMENTS.html, for example)". Remove "ref-guide pages ... no longer checked by anything". In the verify run, a planted broken link in `solr/documentation/src/markdown` would show what still fails.

### 3. Medium: SOLR-18119's reported failure is not resolved, and "the crawl is the remaining python3 dependency in check" is inaccurate

Where: the PR's "A choice to check" (the python3 sentence in the crawl paragraph) and "What this change does".

- The SOLR-18119 symptom: `Changes.html` is silently skipped without python3, and the failure shows up in a downstream task. `gradle/documentation/changes-to-html.gradle:71-77` still warns and skips when `python3` is missing. `pythonExists()` is at `:108`.
- The 18119 log shows the same host failing `:solr:solr-ref-guide:checkSiteLinks` with "page not found". This PR does not change that path, so the same failure is expected on a host without python3 after the merge. The failure moves from `checkBrokenLinks` to `checkSiteLinks`.
- The crawl was the hard python3 dependency: `externalTool("python3")`, which fails when python is missing. After the change, `changes2html.py` is still a python3 dependency, soft (skipped when absent), of `documentation`. `documentation` is still on the root check path (finding 1). So "remaining python3 dependency in check" is wrong. The accurate version is "the crawl was the hard python3 dependency".

Suggested fix: reword the sentence as above. Say that SOLR-18119 stays open for the silent-skip behavior, or link it as not addressed by this PR.

### 4. Medium: the CI gate's overview-reference claim has a mechanism check, but no recorded run proves it

Where: `documentation.gradle:74-77` ("it also exercises references that only the javadoc tool sees, such as those in overview files") and the PR's Proof.

I checked the mechanism with JDK 21 directly, outside the repo and outside Gradle. The samples are in the scratchpad (`doclint-overview/bad-overview`, `doclint-overview/bad-class`):
- `javadoc -Xdoclint:all,-missing -overview overview.html` with `{@link p.NoSuchClass}` in the overview: `error: reference not found`, exit 1.
- `javac -Xdoclint:all/protected -Xdoclint:-missing -Xdoclint:-accessibility -Werror` on the same overview error: exit 0. Compile-time DocLint does not read `overview.html`.
- The same bad reference in a class, with the same javac flags: `error: reference not found`, exit 1.

So the comment is right: the javadoc tool, not compile-time DocLint, is the only check of overview references. The javadoc run uses `-Xdoclint:all,-missing` (`render-javadoc.gradle:422`) and goes through `project.quietExec` (`:451`), which is documented as surfacing output on an error code (`globals.gradle:88-90`).

The gap: the PR's Proof shows that the documentation build succeeds on the real tree. It never shows that a planted overview error fails the CI gate.

Suggested fix: add that negative control to the verify run. Plant a bad `{@link}` in one module's `src/java/overview.html`. Expect `CI=true :solr:documentation:check -x test` to fail in `renderSiteJavadoc`, and expect it to pass without the plant. Not run here.

### 5. Low: the changelog type should be `other`

Where: `changelog/unreleased/SOLR-18523.yml`, `type: changed`.

- `dev-docs/changelog.adoc` (the type table, around lines 57-88) assigns `other` to "build changes, test infrastructure, or documentation". It assigns `changed` to behavior changes that users see.
- The build-change fragments already in `changelog/unreleased/` use `other`: `PR#4037-reformat-gradle.yml`, `PR#4140-gradle-sync-versions.yml`, `SOLR-18289-gradle-9.yml`, `extract-ui-module.yml`.
- The same page says most `other` changes are too small for an entry. If maintainers agree, the `no-changelog` label (see `.github/workflows/validate-changelog.yml`) is the alternative.
- YAML: parses by inspection (four top-level keys; the title is double-quoted and contains a colon). `dev-tools/scripts/validate-changelog-yaml.py` was not run, because there is no Python on this host.

### 6. Low: "public scope" in the DocLint wording

The PR's Proof says the planted error fails "under the shipped configuration (public scope) and under the widened one (public and private)". The shipped flag is `-Xdoclint:all/protected` (`gradle/java/javac.gradle:58`), which covers public and protected elements. Suggested wording: "public and protected".

The Limits claim that `compileTestJava` carries the same DocLint configuration is correct. The flags apply to every `JavaCompile` task in a `JavaPlugin` project, through the `allprojects { plugins.withType(JavaPlugin) { tasks.withType(JavaCompile) … } }` block in `javac.gradle`.

### 7. Low: `isCIBuild` matches variable names, not values

`gradle/globals.gradle:175` tests the names in `System.getenv().keySet()`. So `CI=false`, `CI=0`, or any `JENKINS_*` or `HUDSON_*` variable on a developer machine turns the gate on. Apache Jenkins sets `JENKINS_*`, so on Jenkins the PR's description is accurate. The PR's wording "(CI, or JENKINS_* / HUDSON_* variables)" is accurate about names. One sentence in the Choice would help a reader who expects value semantics.

### 8. Low: the removed `checkBrokenLinks` alias may be called from outside the repo

Apache Jenkins job configuration lives outside the repo. Nothing in the tree calls `checkBrokenLinks`, so the tree is clean. A Jenkins job that runs `checkBrokenLinks` by name would fail after the merge. Maintainers should confirm that none does.

### 9. Info: no upstream run exists for this head

On `8a746512bba`, `Gradle Precommit` (`./gradlew check -x test`, `.github/workflows/gradle-precommit.yml:26`) and `Validate Changelog` show `action_required`, which means a maintainer has to approve the fork's workflow run. `Pull Request Labeler` passed. The PR's `mergeStateStatus` is UNSTABLE for that reason. Until a maintainer approves the run, the PR's `check` evidence is the author's local run only. This review does not approve or trigger runs.

### 10. Info: the base has moved by one commit

The PR's `baseRefOid` is `9becf6c6a154`, one commit ahead of `d94c5eeb6b5` (SOLR-18515, #5047). That commit touches no file in the PR's diff, and the PR is MERGEABLE. The Proof was measured on `d94c5eeb6b5`, so it should be re-run on the current base or labeled as measured on the older one.

## Correction to my earlier review on the assignment branch

`reviews/solr-18523-doclint.md` on `assignment-18523-doclint` (pushed earlier) says that a local `check` no longer runs `documentation`. That is true only for `:solr:documentation:check`. The root `check` still builds the documentation through `:solr:solr-ref-guide:checkSiteLinks` (finding 1). The PR carries the same error. That earlier file is not changed in this review. Correcting it needs a separate commit on that branch.

## Claims in the PR description, checked

| Claim | Status | Evidence |
| --- | --- | --- |
| Crawl, `check-broken-links.gradle`, root alias and `apply` line removed | Confirmed | Tree search at `8a746512bba` (see Completeness). `build.gradle` loses only the apply line. |
| `:solr:documentation:check` depends on `documentation` only on CI | Confirmed | `documentation.gradle:78-80` |
| A local `check` no longer builds the documentation at all | Overstated | Finding 1 |
| Links in ref-guide pages are no longer checked by anything | Overstated | Finding 2 |
| The crawl is the remaining python3 dependency in `check` | Inaccurate | Finding 3 |
| DocLint validates javadoc `{@link}` at compile time, so the crawl duplicated it | Confirmed by mechanism | javac experiment (finding 4); `javac.gradle:58-60` |
| Overview references are checked only by the javadoc tool | Confirmed by mechanism | javadoc and javac experiment (finding 4) |
| The CI gate keeps overview and missing-doclet checks on CI | True for `:solr:documentation:check`, not for the root check | Finding 1 |
| `isCIBuild` covers CI and JENKINS_*/HUDSON_* | Confirmed by names | `globals.gradle:175` |
| Lucene made the same gate choice (#14905, kept in #15350) | Not re-verified here | Recorded in `reviews/solr-18523-doclint.md` (assignment branch), row 1 |
| `:solr:documentation:check`: 5m 05s with CI, 1m 19s without; 8,225 HTML files; `documentation` in the graph only with CI | Not reproduced | Gradle not run |
| Root `check` stops at `:rat` and `buildLocalAntoraSite` on a linked worktree | Not reproduced | Gradle not run |
| Crawl 4m 18s; widened DocLint with 0 failing references; 22,710 missing-doc diagnostics | Not reproduced | Measured on the author's machine; not in the tree |
| A planted bad `{@link}` fails `:solr:api:compileJava` | Mechanism confirmed; module not run | javac experiment; `javac.gradle` applies to every JavaPlugin module |

## Completeness of the deletion (tree at `8a746512bba`)

- A search for `checkBrokenLinks`, `checkJavadocLinks`, `check-broken-links`, `CheckBrokenLinks`, `CheckJavadocLinks`, `JavadocLinks`, `checkJavadoc` and `broken links` returns only:
  - `dev-tools/scripts/smokeTestRelease.py:809` (`checkJavadocAndSourceArtifacts`, an artifact-presence check; unrelated)
  - `gradle/documentation/render-javadoc.gradle:382-383` (comments about broken class links in the test framework; unrelated)
  - the new changelog entry
- Nothing in `dev-docs/`, the ref guide, `.github/` or `dev-tools/` refers to the removed task.
- The deleted file defined only the `checkBrokenLinks` tasks, the `CheckBrokenLinksTask` class and the `check` edge. Nothing else read its properties. Its `docsDir` only read `project.docroot`.
- `isCIBuild` is defined at `gradle/globals.gradle:175`, inside `allprojects { project.ext { … } }` (opened at lines 25 and 42). The root applies globals at `build.gradle:41` and `documentation.gradle` at `:211`, so the definition comes first.
- The DocLint flags are unchanged: `javac.gradle:58-60` (`-Xdoclint:all/protected`, `-Xdoclint:-missing`, `-Xdoclint:-accessibility`), with `-Werror` through `javac.failOnWarnings`, which defaults to true.

## Relation to the java port (solr-18119-jvm, PR #5061)

- This PR deletes `gradle/validation/check-broken-links.gradle`, which #5061 modifies. The modify/delete conflict described in the assignment review still stands.
- If the crawl is removed, the port's `CheckJavadocLinks` has nothing to replace. If a reduced crawl is kept for the docroot-only pages (finding 2), the port is the candidate for that slice. The sequencing is not decided here.

## Not verified here

- Gradle was not run. The verify run should include: the commands in `reviews/solr-18523-doclint.md`, a planted overview error (finding 4), and a planted broken link in a markdown page (finding 2).
- The changelog validator was not run (no Python on this host).
- The timings, file counts and diagnostic counts were not reproduced.

## Open items for the branch owner

1. Scope the comment in `documentation.gradle` and the PR's Choice to `:solr:documentation:check`. Say that the ref guide's `checkSiteLinks` still builds the site on a local root `check` (finding 1).
2. Reword the link-coverage trade to name the docroot-only pages, not the ref guide (finding 2).
3. Reword the python3 sentence, and say that SOLR-18119 is not resolved by this PR (finding 3).
4. Change `type: changed` to `type: other`, or drop the entry under the `no-changelog` label (finding 5).
5. Change "public scope" to "public and protected" (finding 6).
6. Run the planted-overview negative control in the verify run (finding 4).
7. Ask maintainers to confirm that no Apache Jenkins job calls `checkBrokenLinks` (finding 8).

Nothing was posted to GitHub or Jira. Nothing was pushed to `solr-18523-submit`, and the PR was not changed.

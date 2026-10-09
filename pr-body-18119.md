🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18119

## What happens today

**The documentation build renders Changes.html by shelling out to a Python script, and silently skips the page when python3 is not on the PATH.**

- The `changesToHtml` task is an inline Groovy class that execs `gradle/documentation/changes-to-html/changes2html.py`; its `pythonExists()` probe makes a missing python3 a warning and a skip, and downstream tasks then fail confusingly. Hostetter's report on the Jira (2026-02-13) shows the silent skip and the resulting `checkSiteLinks`/`checkBrokenLinks` failures on the Windows Jenkins box.
- The direction has competing open PRs: [#4999](https://github.com/apache/solr/pull/4999) (fail loudly when python3 is missing) and [#5034](https://github.com/apache/solr/pull/5034) (fall back to GraalPy, by Jan). Smiley raised the JVM-native option on the Jira on 2026-10-07, Hostetter ranked it "even better still" the same day, and Jan wrote on 2026-10-08 that GraalPy may be "a stop-gap solution until someone are comfortable porting them".
- This PR is one half of a pair. [PR #5062](https://github.com/apache/solr/pull/5062) ([SOLR-18523](https://issues.apache.org/jira/browse/SOLR-18523)) removes the broken-links crawl, the documentation build's other Python script; this PR ports the changelog converter. Together they take both Python scripts out of the build path, and the two file sets do not overlap, so they merge in either order (test merge in Proof).

## What this change does

**`changes2html.py` is ported to Java in build-infra and deleted; the crawl and its wiring are not touched here.**

- The converter is [ChangesToHtml.java](https://github.com/nick-boss-tech/solr/blob/3fa8af32707faa771393baf2b8bee29f5cb1ac4a/build-tools/build-infra/src/main/java/org/apache/lucene/gradle/ChangesToHtml.java#L270-L274) with the `@CacheableTask` wrapper [ChangesToHtmlTask.java](https://github.com/nick-boss-tech/solr/blob/3fa8af32707faa771393baf2b8bee29f5cb1ac4a/build-tools/build-infra/src/main/java/org/apache/lucene/gradle/ChangesToHtmlTask.java#L39-L72), exposed through the `buildinfra` extension the way `checksumClass()` already is ([BuildInfraPlugin.java](https://github.com/nick-boss-tech/solr/blob/3fa8af32707faa771393baf2b8bee29f5cb1ac4a/build-tools/build-infra/src/main/java/org/apache/lucene/gradle/buildinfra/BuildInfraPlugin.java#L61-L63)) and registered in [changes-to-html.gradle](https://github.com/nick-boss-tech/solr/blob/3fa8af32707faa771393baf2b8bee29f5cb1ac4a/gradle/documentation/changes-to-html.gradle#L20-L25). The inline Groovy task class and its `python3` exec are gone from that file.
- [PythonCompat.java](https://github.com/nick-boss-tech/solr/blob/3fa8af32707faa771393baf2b8bee29f5cb1ac4a/build-tools/build-infra/src/main/java/org/apache/lucene/gradle/PythonCompat.java#L28-L85) holds only the helpers the converter uses: trimming with the Python whitespace set, regex escaping, and match replacement driven by a function of each match. The URL and HTML-entity helpers from the earlier full port existed only for the link checker and are not part of this change.
- Behavior change, stated openly: `Changes.html` is written with LF line endings on every platform, so its bytes and the task's cache key do not depend on the build machine; the Groovy task's writer used the platform line separator.
- `dev-tools/scripts/checkJavadocLinks.py` and `gradle/validation/check-broken-links.gradle` are untouched in this PR; they are [PR #5062](https://github.com/apache/solr/pull/5062)'s. The `python3`-on-PATH notes come out of `dev-docs/how-to-contribute.adoc` and `dev-docs/solr-source-code.adoc`, which is accurate once both halves of the pair have merged.
- Not addressed here: the silent skip as a policy question. The Java task never probes for python3, so there is nothing left to skip with, but this PR does not ask for SOLR-18119 to close on that basis; [PR #5062](https://github.com/apache/solr/pull/5062)'s description makes the same statement for its half, and the ticket stays open for the behavior question.

## Proof

**The port is behavior-preserving, so the proof is equivalence against the Python original, plus a test merge with PR #5062; everything below was run on 2026-10-08 at head `3fa8af32707` on `main` at `9becf6c6a15`.**

- Unit tests in `:build-tools:build-infra:test`, counted from the JUnit XML: [ChangesToHtmlTest](https://github.com/nick-boss-tech/solr/blob/3fa8af32707faa771393baf2b8bee29f5cb1ac4a/build-tools/build-infra/src/test/java/org/apache/lucene/gradle/ChangesToHtmlTest.java#L25-L146) 11/11, [PythonCompatTest](https://github.com/nick-boss-tech/solr/blob/3fa8af32707faa771393baf2b8bee29f5cb1ac4a/build-tools/build-infra/src/test/java/org/apache/lucene/gradle/PythonCompatTest.java#L24-L40) 3/3; total 14, 0 failures, 0 errors, 0 skipped. The suite is smaller than the one in this PR's earlier revision because the link-checker classes and their tests went with the crawl, which PR #5062 removes.
- Equivalence: `:solr:documentation:changesToHtml --rerun-tasks` with `-Ppython3.exe=/does/not/exist` is BUILD SUCCESSFUL and produces `Changes.html` with SHA-256 `457d199d1b7e41c661a37c79281234aa3c1d1dd129a1639162c7cadf5698b5f3`. The Python original, run on the same tree's `CHANGELOG.md` under CPython 3.12.3, produces output that differs in exactly one line: the generated header comment, which names `ChangesToHtml` instead of the script.
- `:solr:documentation:check -x test` is BUILD SUCCESSFUL at this head standalone; `spotlessCheck` on build-infra passes, root `tidy` leaves the tree unchanged, and the changelog fragment parses.
- Test merge with PR #5062's head `c0a3972d667`: the merge is clean, zero conflicts. On the merged tree, `CI=true :solr:documentation:check -x test` is BUILD SUCCESSFUL with `documentation` executed, `checkBrokenLinks` absent (task lookup reports it not found in the project, and the string appears nowhere in the run's log), the build-infra tests pass 14/14, and the merged site's `Changes.html` carries the same SHA-256 as above.
- The Gradle wiring above was verified on Linux only; see Limits for the Windows state.

## A choice to check

**Whether changelog HTML generation belongs in the Gradle build at all is still open; this PR implements the in-build answer.**

- Implemented: the converter lives in `build-infra` and runs as part of the documentation build, so `Changes.html` exists wherever the build produces the site, with no interpreter dependency.
- The alternative raised on the Jira: Smiley questioned why changelog-to-HTML generation is in the Gradle build at all (he removed `Changes.html` from the binary distributions in July, and the website HTML is a release-time job for the release manager), and Jan suggested the script could move to `dev-tools/scripts` and be invoked from the release wizard instead. On that route this converter would be a release tool rather than build wiring, and the port is usable either way, since the converter class has no Gradle dependencies.
- If maintainers prefer the release-tooling route, I can move the converter in this PR; if the in-build route stands, the pair with PR #5062 finishes the python3 removal. Which home should the converter have?

## Limits

**Three bounds are worth naming: fragment anchors stay unvalidated, PythonCompat stays a single class, and the final head is Linux-verified only.**

- Fragment anchors are not validated by any tool once this pair has merged: the crawl checked that target pages exist, not that `#fragment` anchors resolve, and this port does not add anchor checking. Measured on 2026-10-08 over the built site (8,243 HTML files): 185,623 internal fragment links, 0 broken. A validator has to decode HTML character references the way browsers do; a naive raw-string comparison reports about 9,365 false positives, essentially all javadoc constructor anchors of the `#<init>(...)` form. Anchor validation is a follow-up change I can make on request; it is not included here.
- `PythonCompat` stays one package-private class holding the converter's string and regex helpers. It can be renamed or split in this PR if maintainers prefer a different shape.
- Not verified on Windows: the Gradle wiring and tests at this head were run on Linux only. The oracle equivalence comparisons did run on Windows against CPython 3.12.12 at an earlier head of this branch, but the final head's Windows state is unverified.

Changelog: [changelog/unreleased/SOLR-18119.yml](https://github.com/nick-boss-tech/solr/blob/3fa8af32707faa771393baf2b8bee29f5cb1ac4a/changelog/unreleased/SOLR-18119.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
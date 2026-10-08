# Review: solr-18119-jvm (SOLR-18119, pure-Java port)

- Reviewer: Claude Code agent (Solr-issues workspace), for the branch owner
- Date: 2026-10-08
- Branch: origin/solr-18119-jvm (fork nick-boss-tech/solr)
- Head reviewed: **525ea0c6ae1**, single commit "SOLR-18119: Build Changes.html and the broken-links check without Python"
  - The head moved during this session. The claim recorded d0c546a34ef (commit "TESTING.md for the Windows full run"). That commit was force-replaced by 525ea0c6ae1, which drops TESTING.md. No `testing/` directory is in the reviewed tree.
- Base: parent 34e475784af (SOLR-18510). The assignment expected e34067ae647, which is one commit earlier. upstream/main is now 4ae645ef0e1, 3 commits ahead of this branch. None of the commits between 34e475784af and 4ae645ef0e1 touch build-tools, dev-tools/scripts, gradle/documentation, gradle/validation or dev-docs, so the port's base is still current for the files it changes.
- Scope: 10 files, +2049 / -138. New: ChangesToHtml.java (811), ChangesToHtmlTask.java (72), CheckBrokenLinksTask.java (70), CheckJavadocLinks.java (578), PythonCompat.java (498). Changed: BuildInfraPlugin.java (+10), changes-to-html.gradle, check-broken-links.gradle, two dev-docs lines.
- Verdict: **Needs work**

**Not compiled or run.** Nothing here was compiled, formatted, tested or executed: no Gradle, no Spotless, no tidy, no drain, no Python. Findings come from reading both sides. Python behavior is taken from the Python source in the branch tree. CPython's `html.parser` and `urllib.parse` are not installed on this machine (the only `python` on PATH is the Windows Store shim), so statements about CPython internals are marked as hypothesis.

## Summary

The Gradle wiring is sound. The `changesToHtml` and `checkBrokenLinks` tasks declare their inputs and outputs, follow the `checksumClass()` pattern, and the Python `externalTool` / `pythonExists` paths are fully removed. `ChangesToHtml` reads as a faithful port of `changes2html.py`. The branch is not ready for a PR yet. It has no in-tree tests, the link checker can silently stop reading a file on malformed markup or abort the whole run on one bad href, and the cacheable `ChangesToHtmlTask` writes host-dependent bytes.

## Findings (ranked)

### HIGH

1. **[verified] No in-tree tests for the port.** `build-tools/build-infra` has no `src/test` source set at the base, and the branch adds no test files. The equivalence evidence in ASSIGNMENT.md comes from an out-of-tree harness (`verify-solr18119-port.ps1`, TESTING.md, which is dropped before PR). Once the Python is deleted, nothing in the repo would catch a regression. Failure scenario: a later edit to `HtmlScanner` or `urljoin` changes link results, and no test fails. Before PR: add golden tests (ChangesToHtml output for CHANGELOG.md and the six crafted changelogs; CheckJavadocLinks over the 28-file link fixture, with the expected key lines). This also requires creating a test source set for build-infra (see Architecture).

### MEDIUM

2. **[verified on the Java side; CPython side is hypothesis] The link checker silently stops reading a file on malformed markup.** `HtmlScanner.scan()` breaks out of its loop whenever `dispatch()` returns -1 (incomplete construct). That happens for an unterminated comment, an unterminated attribute quote, or a start tag with no `>` before a non-attribute character. Everything after that point is dropped from the link set, with no warning and no failure. Example, assuming no later `'` appears in the file: `<a href='foo>` followed by `<a href="missing.html">`. Java: nothing reported, exit 0. CPython's `goahead(end=True)` (run from `close()`) emits the incomplete construct as text and keeps parsing, so the Python checker reports `BROKEN LINK` for `missing.html` and exits 1. Fix: resume after the next `>` (or `<`) as text, as `goahead` does, or at least report a warning. Confirm with a 3-line fixture before and after.

3. **[verified] One bad href aborts the whole check.** `LinkCollector.startTag` calls `PythonCompat.urljoin`, which calls `urlsplit`. `urlsplit` throws `IllegalArgumentException("Invalid IPv6 URL")` for an authority with unbalanced brackets, for example `<a href="//[host/x">`. `parse()` catches only `ParseFailure`. Python's `parse()` uses a bare `except:` around `feed()` (checkJavadocLinks.py lines 108–117), so the same href marks only that file as failed-to-parse and the crawl continues. In Java, the exception escapes the crawl phase before any verification runs, so no broken links are reported in that run. The task fails with a raw stack trace, not the "Broken links check failed" message. Fix: catch `RuntimeException` in `parse()`, matching the bare `except`, or catch `IllegalArgumentException` around `urljoin`.

4. **[verified; pre-existing, preserved by the port] Fragment anchors are never validated for resolvable links.** In Python (lines 173–176 and 243–257) and in Java (`verify()`, lines 170–200), the fragment is removed from `link` before the scheme branches. Every relative href resolves to a `file:` URL, which takes the `file:` branch and checks only that the file exists. The `BROKEN ANCHOR` branch is reached only for schemes other than http(s), mailto, javascript and file:. There it fails in both implementations: Python raises `KeyError`, Java throws `IllegalStateException("KeyError: ...")`. So `<a href="ftp://host/x#y">` crashes the checker in both. Consequence for the PR: the name "checkBrokenLinks" and the ASSIGNMENT's wording overstate what is checked. Decide before PR. Either document the gap in the known-differences list and the PR description, or fix it as a deliberate behavior change and re-baseline the fixture. Do not describe the port as checking anchors.

5. **[verified by reading] `ChangesToHtmlTask` is `@CacheableTask`, but its bytes depend on the host.** `ChangesToHtml.write()` replaces `\n` with `System.lineSeparator()`. Gradle keys the build cache on inputs and task implementation, not on the host OS. A Changes.html produced on Linux (LF) can therefore be restored on Windows, and the reverse. The old Python task was not cacheable, so this is new. `CheckBrokenLinksTask`'s report has the same issue, because `println` uses the platform separator, though a passing run writes only headers and blank lines. Recommendation: always write LF (HTML does not care), or drop `@CacheableTask`. The justification for known difference #4 ("matching Python text mode per platform") does not hold once the task is cached.

6. **[PR readiness, not a code defect] Changelog gate.** `.github/workflows/validate-changelog.yml` checks for a file under `changelog/unreleased/` or the `no-changelog` label. The branch has neither. The research note mentions `changelog/unreleased/SOLR-18119.yml`, but that file is not in this head. Decide before the PR opens.

### LOW

7. **[verified against the code; CPython table is hypothesis] Numeric references CPython drops are kept.** `PythonCompat.unescapeReference` returns the character for every valid code point. CPython's `html.unescape` maps its invalid-code-point set to `''` (for example `&#1;`, `&#127;`, `&#65535;`). The class comment lists only the 0x80–0x9F remap as a limit, so this is an undisclosed difference. Impact is negligible, since it only affects href values containing such references.

8. **[verified] Output differences missing from the known-differences list.** (a) On a parse failure, the Python traceback is replaced by one message line. (b) Files are checked in sorted path order, while `os.walk` yields filesystem order. (c) A missing docs directory now throws; that is already known (#2), but the exception reaches Gradle as a raw `NoSuchFileException` rather than the `GradleException` the rest of the task uses. Add (a) and (b) to the list, so the 21-key-line comparison is read correctly.

9. **[verified] The checker reads host filesystem state outside its declared inputs.** For `file:` links, `fileExists` tests the host filesystem. `CheckBrokenLinksTask` declares only the docs tree as input, so a cache hit could mask an absolute `file:` link that exists on the machine that populated the cache. Probably harmless. I did not check whether the generated docs contain absolute `file:` links.

10. **[verified] `PythonCompat` is misnamed and bigger than its callers need.** `ChangesToHtml` imports three helpers from it (`strip`, `escapeRegex`, `replaceAll`). The URL, entity and `urljoin` code (about 300 lines) is used only by `CheckJavadocLinks`. After the Python is gone, the name describes nothing in the tree. See Architecture.

11. **[verified] Hygiene.** AGENTS.md says changes should not carry code comments that communicate the change. The `PythonCompat` class comment says what it replaces ("previously provided by changes2html.py and checkJavadocLinks.py"). Reword it to say what the class provides.

## Port correctness: what I checked and found equivalent

Verified by reading both sides.

- **changes2html.py → ChangesToHtml.** CR/CRLF normalization. Preamble detection. Release, section and item patterns, with `UNIX_LINES | UNICODE_CHARACTER_CLASS` matching Python `str` regex semantics. Continuation lines and HTML-comment skipping. Issue extraction: ordering, joining, and the rule that bounds author extraction to the tail. Author grouping and bracket/paren depth. `formatSingleAuthor`. Placeholder order in `convertMarkdownLinks`. The `escapeMeta` and `shouldExpand` JavaScript: the Python f-string escapes (`{{`, `}}`, `\\\\`) and the Java text-block escapes produce the same output. `pyStr` renders missing relids as `None`, as Python does. `re.escape` special characters match Python 3.7+. The trailing newline matches `print()`.
- **Entities.** `nbsp;` maps to U+00A0 (bytes `C2 A0`), matching `html.unescape`. The named table is a subset (known difference #1).
- **Whitespace.** `PythonCompat.isSpace` covers Python's `str.isspace` cases that matter here, including U+0085, U+00A0 and U+2028, and excludes U+200B and U+FEFF.
- **Regex replacement.** `replaceAll` appends the replacement literally, so `$` and `\` in URLs are not interpreted, matching `re.sub` with a function.
- **URLs.** `urlsplit`, `urljoin` and `urlunsplit` follow CPython 3.12's `urllib.parse` structure. `urlunsplit` uses the netloc rules that determine the `file:` key shape on Windows (`file:C%3A/...` versus `file:///C%3A/...`). The key shape only matters for the printed link text, because keys and joins use the same function. That the port matches 3.12.12 is hypothesis; it depends on the CPython patch level.
- **CheckJavadocLinks.verify.** Branch order. External allowlist (including the `.` in the `index.html` regex, which is unescaped in both). Mailto, javascript and `Field.html` exemptions. `file:` existence with the `[1:]` fallback for Windows drive paths. `Changes.html` exemption. Duplicate `name` exemption for serialized-form pages. Anchors recorded from `name` only, as Python does.
- **Tag sets.** `START_TAGS_NOT_STACKED` and `END_TAGS_IGNORED` match Python's tuples, including `wbr` appearing only in the start list.
- **Gradle.** `ChangesToHtmlTask` declares `changesFile` (`@InputFile`), `siteDir` (`@InputDirectory`) and `targetDir` (`@OutputDirectory`). `CheckBrokenLinksTask` declares `docsDir` (`@InputDirectory`, RELATIVE) and `outputFile` (`@OutputFile`, in the build directory). The `dependsOn` map entry and `check.dependsOn` wiring are unchanged. A grep of the tree finds no remaining Gradle reference to `changes2html`, `checkJavadocLinks`, `python3` or `externalTool("python`.
- **CSS.** The stylesheets copied by `ChangesToHtmlTask` live in `gradle/documentation/changes-to-html/` next to `changes2html.py`. Deleting the script must not delete that directory.
- **Dev docs.** The two removed `python3` notes are the only docs references to the doc build's Python requirement. The remaining `python3` mention in `dev-docs/changelog.adoc` is for `dev-tools/scripts/changes2logchange.py`, which is unrelated.

## Architecture and structure

- **Size.** About 2000 lines of Java for about 1090 lines of Python. Most of the difference is `HtmlScanner` (about 200 lines, reimplementing `html.parser`), the urllib routines, and the entity subset. If build-infra already has an HTML parser or URI utility on its classpath, check that before keeping the hand scanner. I did not check build-infra's dependency list. `java.net.URI.resolve` is not a drop-in replacement for `urljoin`, so keep the custom `urljoin` if you go that way.
- **Test source set.** build-infra has none at the base, so the PR must create one (JUnit declared through `gradle/libs.versions.toml`, per AGENTS.md). That is a structural decision for the owner, not a patch.
- **Should the Python scripts be deleted in the PR?** Recommendation: yes, in the same PR, but only after golden tests exist. Reasons: (1) nothing runs the scripts after the rewire, so they would rot silently, and an edit to one copy would not reach the other; (2) the thing that keeps the port honest is the golden output generated from the scripts, so generate it first and commit it as test resources, then delete the scripts; (3) git history keeps the full spec, so cite the commit in the PR description. Delete `changes2html.py` and `checkJavadocLinks.py` only, not the `changes-to-html/` directory, which still holds the CSS.
- `ChangesToHtml.write` is separate from `render`, which makes golden tests straightforward. Keep that split.

## Assignment items

- **Head SHA:** 525ea0c6ae1 (recorded above).
- **Expected shape:** a single commit on the base, with the expected subject. No TESTING.md and no `testing/` directory at the reviewed head.
- **Known differences:**
  1. The `html.unescape` subset is acceptable for CHANGELOG content, but add the invalid-code-point gap (finding 7).
  2. The missing docs directory throws. That is the stricter direction and acceptable, but wrap the failure in a `GradleException` so it looks like the rest of the task (finding 8c).
  3. The header comment wording is acceptable. It changes every generated Changes.html once, which is trivial.
  4. Platform line endings are not acceptable with `@CacheableTask` (finding 5).
  5. The `file:` key normalization is acceptable only if the comparison interpreter is the one whose `urlunsplit` the port copies. State the CPython version in the PR (hypothesis).
  - **Missing from the list:** the anchor gap (finding 4), the output-format differences (finding 8), and the crash cases (findings 3 and 4). Finding 2 is a bug, not a difference.
- **Blockers before PR:** findings 1, 2, 3, 5 and 6.

## Not checked

- Nothing was compiled, formatted or run. Spotless and tidy claims in ASSIGNMENT.md were not re-checked.
- CPython 3.12.12 `html.parser` and `urllib.parse` behavior was not checked against a local copy. Statements about CPython are based on the Python code in the tree and on my knowledge of CPython's `goahead` and `html.unescape` logic. Findings 2 and 7 depend on that and should be confirmed with a fixture.
- The 28-file link fixture, the six crafted changelogs and the built site were not re-run.
- Whether the generated docs contain absolute `file:` links (finding 9).
- build-infra's dependency list (architecture note).
- Upstream drift: upstream/main moved to 4ae645ef0e1 during the review, 3 commits ahead of the branch. The branch was not rebased. Re-check the port's base before the PR.

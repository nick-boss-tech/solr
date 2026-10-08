# Assignment: code review of solr-18119-jvm (SOLR-18119, pure-Java port)

Review only. Do not patch the branch under review. Findings go in a review file committed to THIS branch.

## What to review

- Branch: `solr-18119-jvm` on fork `nick-boss-tech/solr`.
- Review the tip of that branch as it stands when you start. Record the exact head SHA in your review file. Expected shape: a single commit on `apache/solr` main at `e34067ae647`, subject "SOLR-18119: Build Changes.html and the broken-links check without Python". No `TESTING.md` and no `testing/` directory belong in that tree; if either is present, the branch moved mid-handoff and you should note it and review what is actually there.
- The change ports the documentation build's two Python scripts to Java in `build-tools/build-infra` and rewires the Gradle scripts to the Java tasks: `ChangesToHtml` (was `gradle/documentation/changes-to-html/changes2html.py`), `CheckJavadocLinks` (was `dev-tools/scripts/checkJavadocLinks.py`), `PythonCompat` (helpers for the Python behaviors the scripts depend on), `ChangesToHtmlTask` and `CheckBrokenLinksTask` (`@CacheableTask`), accessors on the `buildinfra` extension, and the two `.gradle` files with the `python3` calls removed. The Python scripts remain in the tree, unreferenced, as the port's specification.

## Context

- Jira SOLR-18119: Changes.html generation is quietly skipped if `python3` is not found, causing confusing downstream failures (Hostetter, 2026-02-13; the log is from a Windows Jenkins box).
- Three directions exist. PR #4999 (fail loudly when python3 is missing) and PR #5034 (GraalPy fallback, by the Jira assignee Jan Høydahl) are the Python-based options. This branch is the third: no Python in the build at all. Smiley suggested it on the Jira (2026-10-07: "Wouldn't it be simpler to convert these scripts to something JVM native like Java or Kotlin?"); Hostetter ranked it "even better still" the same day. Jan, this morning (2026-10-08), called GraalPy possibly "a stop-gap solution until someone are comfortable porting them" and wrote "If anyone fancy developing the kind of tooling we're talking about here in pure Java, then feel free", while predicting many more lines than the Python equivalents.
- A PR from this branch is planned but deliberately waits for this review.

## Verification already done (Linux, 2026-10-08)

Treat these as claims to sanity-check against the code where you can, not as substitutes for reading:

- `changesToHtml` runs with `-Ppython3.exe=/does/not/exist` and produces `Changes.html` with SHA-256 `457d199d1b7e41c661a37c79281234aa3c1d1dd129a1639162c7cadf5698b5f3`, identical to the normal run and to the standalone harness output.
- `checkBrokenLinks` is green over a full documentation build; a third `changesToHtml` run reports UP-TO-DATE.
- Output equivalence against the Python scripts: real `CHANGELOG.md` identical except one documented header-comment line; six crafted changelogs the same; a 28-file link fixture gives 21 key lines and exit 1 from both; a built site gives 0 key lines clean and 9 with five injected errors. Comparisons were run against CPython 3.12.12 (Windows), 3.12.3 and 3.12.15 (Linux); the fixture matches literally under 3.12.12 and 3.12.15.
- Spotless passes on the four new Java files; root tidy is clean.

## Known intentional differences (from the port's notes)

1. `html.unescape` is a subset: named references `amp lt gt quot apos nbsp` and legacy forms; numeric references in 0x80-0x9F are not remapped to Windows-1252.
2. A missing docs directory throws (`Files.walk`) where Python's `os.walk` silently yielded nothing.
3. The generated header comment reads "ChangesToHtml, invoked by the changesToHtml task." instead of "Python script 'changes2html.py'."
4. Line endings follow the platform (`System.lineSeparator()`), matching Python text mode per platform.
5. `file:` key normalization follows CPython 3.12.12.

## Review focus

- Port correctness against the Python originals, which are in the tree: tokenizing, URL handling, entity handling, exit-code and report behavior of `CheckJavadocLinks.check`, and the converter's handling of the crafted cases (CRLF, NBSP, zero-width space, U+2028, no releases, empty file).
- Gradle wiring: task input/output declarations and cache correctness on both tasks, the `buildinfra` accessor pattern, failure behavior of `CheckBrokenLinksTask` compared to the old Groovy task.
- The five known differences: are they acceptable, and is anything important missing from that list?
- Should the two Python scripts be deleted in the PR, or kept as the specification? Give a recommendation with reasons.
- Architecture: the layout follows the existing `checksumClass()` pattern. Note any structural change you would want before this goes upstream.
- Anything that would block opening the PR.

## Deliverable

- Commit `reviews/solr-18119-jvm.md` to THIS branch with: the head SHA reviewed, findings by severity, and a closing verdict of Ready, Ready with notes, or Needs work.
- Claim the assignment first with `claims/solr-18119-jvm.md` on this branch, following the channel's usual pattern.
- Do not open a PR, do not comment on GitHub or Jira, and do not push to `solr-18119-jvm`.

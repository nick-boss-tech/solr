# SOLR-18551 branch code review, round 1

Review date: 2026-10-17 (Eastern).

This is a code review of the three fork branch heads against their assigned bases and PR #5173 at head `d05eabeee25a10f1dda6a5c9ae0882757f6050de`. No fork branch was changed. No Gradle or test run was performed on Windows.

## Reviewed revisions

| Line | Base commit | Reviewed head |
| --- | --- | --- |
| main | `714d6ceff852e6dc743ef51ae094e4eaf8f3b3f9` | `544d09af7663bc1c07bd15fe30d4c5c6e853f816` |
| 9.x | `c135e6335c7158fa26e96b0dc386f825255b47c0` (Solr 9.10.1) | `d8f96dfeea3ab6d84053f3b9173c06ca379fc25d` |
| 10x | `0712c1f7831a0b0c22837fa6a07fc028040e9bbe` (upstream branch_10x) | `a786123b1031e98981d20ad7e5d89083a523bcec` |

PR #5173 is open at head `d05eabeee25a10f1dda6a5c9ae0882757f6050de`, based on `714d6ceff852e6dc743ef51ae094e4eaf8f3b3f9`. Its current title is `SOLR-18851: Prevent mmap leak in IndexFetcher`.

## 1. IndexFetcher fix parity

| Line | Result | Fix location |
| --- | --- | --- |
| main | Matches PR #5173, with no code hunk difference | `IndexFetcher.java:1341` |
| 9.x | Matches PR #5173 after ignoring the line offset | `IndexFetcher.java:1369` |
| 10x | Matches PR #5173, with no code hunk difference | `IndexFetcher.java:1341` |

Each branch replaces `dir.openInput(fileName, IOContext.DEFAULT).close()` with `dir.fileLength(fileName)` and carries the same explanatory comment as the PR.

Findings:

- **nit**: `IndexFetcher.java:1334` on main and 10x, `IndexFetcher.java:1362` on 9.x. The Javadoc still says the file is opened, though the method now checks its length. Update the description to explain the existence check and missing-file behavior. [An existing PR inline comment](https://github.com/apache/solr/pull/5173#discussion_r4300199455) asks for the same refresh.
- **should-fix**: PR #5173 title. It says `SOLR-18851`; the issue and changelog use `SOLR-18551`. Correct the PR title key. Per the assignment decision, do not change the branch commit messages.

## 2. Backport commit fidelity

| Line | PR fix and tidy commits | Branch commits | Additional commit |
| --- | --- | --- | --- |
| 9.x | `076cf49f5b` and `d05eabeee2`, both by Jason Gerlowski | `f8300975fc` and `01f7103ccb`, same authors and subjects; the fix and comment-wrap patches match | `d8f96dfeea`, Nick Shanin, `SOLR-18551: regression tests for mmap retention in slowFileExists` |
| 10x | `076cf49f5b` and `d05eabeee2`, both by Jason Gerlowski | `07d9c30a68` and `5d332f2d2f`, same authors and subjects; the fix and comment-wrap patches match | `a786123b10`, Nick Shanin, `SOLR-18551: regression tests for mmap retention in slowFileExists` |

Each backport contains only the two PR-derived commits and its test commit after the assigned base. No extra commit or changed file was found. No findings.

## 3. Java 11 test diff on 9.x

The main and 9.x test files differ only at lines 107-108 of the 9.x file. The two pattern-matching `instanceof` expressions are expressed with Java 11 casts. No other test code differs. The 10x test file is identical to main. This is the expected compatibility change. No findings.

## 4. Main test review

`IndexFetcherTest` extends `SolrTestCase` (`IndexFetcherTest.java:35`). It uses `newDirectory()` for the randomized directory (`:77`) and also checks `MMapDirectory`. There is no `Thread.sleep` and no system-property cleanup.

The assertions at `IndexFetcherTest.java:47`, `:60`, and `:70` require exactly one `fileLength` call for present, empty, and missing files. The base method at `IndexFetcher.java:1339` calls `openInput`; `TrackingDirectory` records that call at `IndexFetcherTest.java:125` and records `fileLength` at `:131`. By code inspection, each assertion fails on the base because the recorded call differs, including the missing-file case where the method result remains false. These are method-level checks; no test was run in this review.

Coverage limits requested by the assignment:

- The test invokes the private helper through a method handle (`IndexFetcherTest.java:93`, `:103`). It does not execute `moveAFile` or its replication uses at `IndexFetcher.java:1422` and `:1426`.
- The test tracks `openInput` and `fileLength` only. It does not detect a leak through another directory operation or another path.

No test-rule finding.

## 5. Changelog review

All three branch fragments are identical. The `fixed` type is listed in `dev-docs/changelog.adoc`; `name` is the required author field, and Jason Gerlowski matches the PR author. The JIRA link targets SOLR-18551, whose summary is about IndexFetcher mmap leaks. The [Lucene issue #15068](https://github.com/apache/lucene/issues/15068) is titled `MMapDirectory sometimes "leaks" 1000s of maps`.

| Line | Branch | Result |
| --- | --- | --- |
| `changelog/unreleased/SOLR-18551-avoid-mmap-leak-in-indexfetcher:2` | main | **nit**: fragment title is `Fix mmap and virtual-memory leak in IndexFetcher`; PR subject is `Prevent mmap leak in IndexFetcher`. If the requested title match is literal, align the wording. Type and links are correct. |
| `changelog/unreleased/SOLR-18551-avoid-mmap-leak-in-indexfetcher:2` | 9.x | **nit**: same title wording mismatch. Type and links are correct. |
| `changelog/unreleased/SOLR-18551-avoid-mmap-leak-in-indexfetcher:2` | 10x | **nit**: same title wording mismatch. Type and links are correct. |

## 6. Writing rules

Counts are for the three files changed on each branch. All files have zero U+2014 and zero U+2013 characters. There are no line numbers to report.

| Line | File | U+2014 | U+2013 |
| --- | --- | ---: | ---: |
| main | `changelog/unreleased/SOLR-18551-avoid-mmap-leak-in-indexfetcher.yml` | 0 | 0 |
| main | `solr/core/src/java/org/apache/solr/handler/IndexFetcher.java` | 0 | 0 |
| main | `solr/core/src/test/org/apache/solr/handler/IndexFetcherTest.java` | 0 | 0 |
| 9.x | `changelog/unreleased/SOLR-18551-avoid-mmap-leak-in-indexfetcher.yml` | 0 | 0 |
| 9.x | `solr/core/src/java/org/apache/solr/handler/IndexFetcher.java` | 0 | 0 |
| 9.x | `solr/core/src/test/org/apache/solr/handler/IndexFetcherTest.java` | 0 | 0 |
| 10x | `changelog/unreleased/SOLR-18551-avoid-mmap-leak-in-indexfetcher.yml` | 0 | 0 |
| 10x | `solr/core/src/java/org/apache/solr/handler/IndexFetcher.java` | 0 | 0 |
| 10x | `solr/core/src/test/org/apache/solr/handler/IndexFetcherTest.java` | 0 | 0 |

## Verification evidence

No Gradle or local test run was performed by the Windows reviewer. At PR #5173 head `d05eabeee25a10f1dda6a5c9ae0882757f6050de`, the upstream [changelog check](https://github.com/apache/solr/actions/runs/38955609655), [Solr tests](https://github.com/apache/solr/actions/runs/38955609639), [Gradle check](https://github.com/apache/solr/actions/runs/38955609676), and [labeler](https://github.com/apache/solr/actions/runs/38955604243) all succeeded. Each run's head SHA was verified against the PR head. These checks are not the separate paired head/base runs assigned to Linux hosts. No `receipts/SOLR-18551.md` was present on `pr-prepare` when this review evidence was collected.

Disposition: no blocking code finding. The review records one PR title correction, one Javadoc nit, and the changelog wording nit on each branch. The branch head/base test legs remain with the separate Linux assignment.

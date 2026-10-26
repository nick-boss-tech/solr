# PR 5221 license-header fixes: implementation handoff, round 1

Status: Implemented, Proof Pending

Nick requested these fixes in the Windows chat on October 25, 2026 (Eastern), following the comparison of our `solr-tidy-license-headers` branch with apache/solr PR 5221. This work builds on the other contributor's exact PR head, preserving their history, and does not move the public PR head.

## Branch and revisions

- Fork branch: [`solr-pr-5221-license-headers-submit`](https://github.com/nick-boss-tech/solr/tree/solr-pr-5221-license-headers-submit).
- Parent PR head: `e0453c0931cab27fcb5c001f6bca3e412b3b1570`.
- Implementation commit: `aae00978bd9e26986e6f2d1ed14eedf8f0981b64`, `PR 5221: preserve notices and safely select new Java sources`.
- Fixture correction: `2855d3a04f29cc9121ceb677b9e495849cdae25a`, `PR 5221: keep formatting probes scoped to fixture subprojects`.
- Final submitted head for the gate: **`2855d3a04f29cc9121ceb677b9e495849cdae25a`**.
- Fork push and separate GitHub ref read-back succeeded at that exact SHA; the final local worktree is clean.
- New commits use Nick Shanin's ICLA identity with no attribution trailers. Original contributor commits are retained without rewriting.
- Windows claim: `claims/pr-5221-license-headers-windows-round-1.md`, posted at board commit `4fe02e20cb05815b6c3d953d754c9fcc2a1c2ce8`.

## What changed relative to PR 5221

Three files only:

1. `gradle/license-headers.gradle`: retain Git-added/nonignored untracked selection; inspect leading comments for existing notices rather than full-file ASF string matches; skip generated sources, known JavaCC parser output, ignored-output directories, and symlinks; use UTF-8 NUL-delimited Git output; resolve upstream/main, origin/main, then local main, with an explicit base override; fail missing refs/failed Git queries before writing; document a source-archive no-op; preserve BOM and existing UTF-8 body/line endings; order actual Java formatting tasks after header insertion.
2. `dev-docs/gradle-help/formatting.txt`: document selection, exclusions, ref override, failure/no-Git behavior, and the functional test command.
3. `dev-tools/scripts/test_license_headers.py`: 12 Linux functional tests using the real Gradle task script in disposable repositories, with original-script substitution for fail-before proof and per-invocation logs.

The final fixture correction makes formatting probes resolve their own subproject files and tracks fixture subproject build files so they survive linked-worktree checkout. It is an additive second commit, not a history rewrite.

PR 5221's canonical header template reuse, explicit UTF-8 behavior, root build application, and RAT hint are retained. No dependency or changelog fragment was added. This is contributor build tooling; no Solr runtime API or user data path changes.

## Scope decisions and limits

The helper conservatively leaves recognized leading license/copyright/SPDX and generation text for RAT/manual review. It is not a license classifier. An ASF phrase in an ordinary Java string does not count as a header. Existing tracked Java files that are only modified remain outside the automatic repair scope.

Java in the independent `build-tools/` and `ui/` builds remains manual. Their formatter task graphs are separate from the root build's `allprojects` graph, so silently rewriting those files during root tidy could overlap their own formatting. This boundary is documented and covered by the fixtures. Supporting those builds can be a separate per-build extension.

The source archive intentionally does no autofix without Git metadata. In a Git checkout, missing Git or comparison refs and failed Git queries are errors rather than success with partial candidate discovery. A comparison ref must have shared history with HEAD. The explicit gate base avoids reliance on a gate host's local remote naming.

## Windows validation and Linux handoff

- Python AST parsing passed for the final fixture driver. The tests themselves were not executed.
- `git diff --check` passed for the commit range above PR 5221 and for the staged files before each commit.
- The isolated worktree's outbound paths were reviewed; only the three files above entered the two new commits. The unrelated dirty main source checkout was not edited.
- No Gradle, formatting execution, or functional/build test ran on Windows.
- Required proof is defined in `assignments/pr-5221-license-headers-linux-gate-round-1.md`, covering the exact final head, head fixtures, original-script assertion failures, real parallel tidy/Spotless integration, and `check -x test` with a clean checkout.
- Required receipt: `receipts/PR-5221-license-headers-round-1.md`.

No public PR/comment/review/JIRA write was made. The original PR remains owned by its contributor. Both new commits are intended to be reviewable as a small follow-up commit range above that PR; no second public PR is requested here.

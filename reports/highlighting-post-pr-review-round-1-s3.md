# Highlighting post-PR review round 1, slice 3: SOLR-4540 (draft PR #5105)

Reviewer: pool review sub-agent, Windows host, read-only. Assignment: `assignments/pool-highlighting-post-pr-review-round-1.md`, slice 3. Sources: `origin/pr-prepare` (tip `bd6f4181994`), fork tip `180b6e8a7d3c23ac308a28d547f61816b5815f04`, live PR #5105 read through `research/gh.ps1`.

## Verdict

**STILL OPEN.** Two items in the live body need a fix. Everything else checks out.

1. **F1 (Proof, no counts):** the Proof states no pass count and no date. The receipt supports "FastVectorHighlighterTest 3 of 3 at this head". Add the line (see Fixes).
2. **F2 (Proof, CI at this head):** the Proof says a GitHub Actions run "completed successfully at this head". The receipt records that run at an earlier commit. The body must not say "this head". Restate or drop the bullet (see Fixes).

Non-blocking notes: N1 (changelog path is code text, not a link), N2 (symptom link points at head, where the builder lookup is no longer the first step). Both are judgment calls for the main side.

## Head and PR state

- `git ls-remote origin refs/heads/solr-4540-submit`: `180b6e8a7d3c23ac308a28d547f61816b5815f04`. Equals the head in the assignment.
- PR #5105 (apache/solr): state OPEN, isDraft true, headRefName `solr-4540-submit`, headRefOid `180b6e8a7d3c23ac308a28d547f61816b5815f04` (equals the fork tip), mergeStateStatus UNSTABLE, reviewDecision empty.
- Title: "SOLR-4540: FastVectorHighlighter no longer does per-field work for fields a document does not have".

## Item table

| # | Item | Live wording or source | Source check | Result |
|---|---|---|---|---|
| 1 | Head | headRefOid `180b6e8a...` | Equals the fork tip. PR is OPEN and a draft. | SATISFIED |
| 2 | Title | "SOLR-4540: FastVectorHighlighter no longer does per-field work for fields a document does not have" | At head, `DefaultSolrHighlighter.java` L636-L640 returns null when `doc.getFieldValues(fieldName) == null`, before the builder lookup (L641) and `getBestFragments`. The title matches the changelog title at head and the receipt's shipped title. | SATISFIED |
| 3 | Body vs draft | Live body, CR stripped, against `pr-drafts/highlighting/SOLR-4540.md` on the tip | `diff` shows no difference. | SATISFIED |
| S | Symptom figures (not Proof) | "QTime above 10 seconds with `hl.fl=fulltext_*`, and about 30 ms with the explicit field name" | Local ticket packet `research/jira-context/SOLR-4540.json` (read only): "> 10s" with `fulltext_*`, "~30ms" with `fulltext_1234`. | SATISFIED |
| 4a | Proof: counts, date, head | Body states no run count and no date. | Receipt: "FastVectorHighlighterTest 3 of 3 at the head". Head test file has 3 `@Test` methods (static count, not a run). Receipt's title re-gate at this head finished 2026-10-09. | **STILL OPEN (F1)** |
| 4b | Proof: CI run "at this head" | "A GitHub Actions run of `:solr:core` that covers FastVectorHighlighterTest completed successfully at this head." | Receipt: run 37622803991 was at "the earlier head". The run is on the fork (nick-boss-tech/solr), workflow "Fork test runner", manual dispatch, `head_sha` `3543d10b34a7c790af348ede00ce034b1f2426bc` on branch `ci/4540-fvh-r28`, created 2026-10-07. That commit is `62c06439fb0` plus one workflow file. Its Java code equals the live head. It is not the live head. | **STILL OPEN (F2)** |
| 4c | Proof: fails on base | "On the base code, this test fails: ... fails at line 106 of the test." | Receipt: on base production with the branch test, `testFieldAbsentFromDocIsSkipped` fails at line 106. Head test file L106 is the `assertQ(` call. Base is `cabedd1d968` (merge-base with `upstream/main`, computed locally). | SATISFIED |
| 4d | Proof: no seeds, run IDs, internal words | Live body | Case-insensitive grep for seed, gate, receipt, ledger, premise, log, JUnit, rc=, lucene, em or en dash. The only hit is "changelog". No run IDs. | SATISFIED |
| 5 | Citations | Four blob links, all at `180b6e8a...` | See citation table. | SATISFIED (N1, N2) |
| 6 | Bold one-line summaries | "What happens today", "What this change does", "Proof", "Limits" each open with a bold line. | Read. The "AI assistance" footer is approved template text. | SATISFIED |
| 7a | Limits true at head | "Covered: skipping absent fields on the FastVectorHighlighter path. Not covered: the timing in the ticket." "No timing is measured in this change." "The default highlighter path is not changed." | Head diff for `DefaultSolrHighlighter.java` is one 5-line insertion in `doHighlightingByFastVectorHighlighter`. No timing code. Limits are true. | SATISFIED |
| 7b | Choice section | None in the body. | The answers file records no choice call for SOLR-4540. Not required. | SATISFIED (N3) |
| 7c | Answers: OPEN line | Base-code result, seed and log removed | Answers file: filled from the premise run, the draft holds the base result, and the seed and log are gone. Matches. | SATISFIED |
| 7d | Answers: changelog title | Head title: "FastVectorHighlighter no longer does per-field work for fields a document does not have." | Answers asked for a title that states the behavior change (absent fields are skipped), re-gated. Commit `180b6e8` is the title commit. | SATISFIED |
| 8 | Lucene version claims | No Lucene mention in the body. | Not applicable. | N/A |
| 9 | Reviewers, bots, CI | See the CI and review section. | No reviews, no comments. One check in the rollup (labeler, success). Three upstream workflows at head have conclusion action_required. | SATISFIED for the body. Upstream checks not run. |

## Citation table

All four body links use `180b6e8a7d3c23ac308a28d547f61816b5815f04`. Anchors checked with `git show <head>:<path>`.

| Body link | Claim | Anchor at head | Result |
|---|---|---|---|
| `DefaultSolrHighlighter.java#L641` | Builder lookup runs for each field (symptom) | L641: `SolrFragmentsBuilder solrFb = getSolrFragmentsBuilder(fieldName, params);`. Symptom cited at head. At base (`cabedd1d968`) the same statement is L636, the first line of the method. At head it now comes after the new guard. | SATISFIED (N2) |
| `DefaultSolrHighlighter.java#L415-L421` | Unknown builder name throws a 400 | L415 starts `getSolrFragmentsBuilder`. L418-L421: `if (solrFb == null) { throw new SolrException(BAD_REQUEST, "Unknown fragmentsBuilder: " + fb); }`. The change starts at L633, so these lines are the same at base. | SATISFIED |
| `DefaultSolrHighlighter.java#L636-L640` | Fix: null return before builder lookup and FVH | L636: `if (doc.getFieldValues(fieldName) == null) {`. L639: `return null;`. L640: `}`. | SATISFIED |
| `FastVectorHighlighterTest.java#L94-L123` | New test | L94 javadoc opens, L99 `@Test`, L100 method signature, L123 closing brace. | SATISFIED |
| `changelog/unreleased/SOLR-4540-fvh-skip-absent-fields.yml` (code text, not a link) | Changelog file | File exists at head. Its title matches the PR title. | NOTE (N1) |

## Proof check

- Test named: `FastVectorHighlighterTest.testFieldAbsentFromDocIsSkipped`. The body's description (two documents, `hl.fl=tv_*`, unknown builders on `tv_b` and `tv_c`, doc 1 matches) matches the test code at head.
- Base failure: the receipt says the test fails at line 106 on base production with the branch test (log `g4540-premise.log`, not on disk here). The body says the same. Head L106 is the `assertQ(` call. Static reasoning: on base, `hl.fl=tv_*` expands to `tv_a`, `tv_b`, `tv_c` (index names, `SolrHighlighter.expandWildcardsInFields`). For doc 1, `tv_b` is absent and the base code calls `getSolrFragmentsBuilder("tv_b")`, which throws the 400. The `tv_*` dynamic field in `schema.xml` L759-L760 has termVectors, termPositions and termOffsets, so the FVH path is used. Consistent. Not re-run (hard limit).
- Passing result at this head: not stated in the body (F1). The receipt gives 3 of 3 at the head.
- Dates: the original gate was 2026-10-07 at `62c06439fb0`. The title re-gate at `180b6e8` finished 2026-10-09.
- CI bullet: see 4b (F2).
- Internal vocabulary and identifiers: none in the body (see 4d).

## Body vs draft

No difference. The live body (CR stripped) is byte-identical to `pr-drafts/highlighting/SOLR-4540.md` on the tip. Both carry the same F1 and F2 problems, so the same fix goes into both.

## CI and review state

| Check or run | Source | State at head | Note |
|---|---|---|---|
| Pull Request Labeler (run 38097779721, pull_request_target) | statusCheckRollup, `gh pr checks`, check-runs | completed, success | Labels only. |
| Gradle Precommit (run 38097779739) | Actions runs with `head_sha` = head | completed, conclusion action_required | Not in the rollup. Jobs did not run. |
| Solr Tests via Crave (run 38097779754) | same | completed, conclusion action_required | Same. |
| Validate Changelog (run 38097779714) | same | completed, conclusion action_required | Same. |

- mergeStateStatus is UNSTABLE. reviewDecision is empty.
- Reviews (`pulls/5105/reviews`): none. Issue comments: none. Review comments: none.
- Combined commit status at head: pending with 0 statuses.
- Reading: upstream build and changelog checks have not run at this head. Per the standing rule, action_required counts as incomplete. The fork run is not upstream CI.

## Automated findings

- Verified: none to verify. The only automated check is the labeler, which succeeded and posted nothing.
- Rejected: none.

## Receipt notes for the main side (receipt is not part of this slice's write set)

- R1: `receipts/SOLR-4540.md` says "at the earlier head" without a SHA. Name it: `3543d10b34a7c790af348ede00ce034b1f2426bc` (CI commit on `62c06439fb0`, adds only `.github/workflows/fork-test-runner.yml`, Java code equal to `180b6e8`). The receipt also says the re-gate "stands on the local gate alone", which matches the fix in F2.
- R2: the receipt's "3 of 3" does not name its run or date. Confirm which run produced it. The title re-gate (finished 2026-10-09 at this head) is the likely source. The static count of 3 `@Test` methods at head is consistent.
- R3: the receipt says "base production" without a SHA. Name `cabedd1d968059215188f4e7563fb303241899ed` (merge-base with `upstream/main`, computed locally; `upstream/main` was not refreshed).

## Fixes for the main side

Apply the same change to the live body (PR #5105) and to `pr-drafts/highlighting/SOLR-4540.md`. Use plain text, no dashes.

- **F1.** In "Proof", after the bullet "On the base code, this test fails: ...", add:
  `- \`FastVectorHighlighterTest\` 3 of 3 pass with this change (verified 2026-10-09 at this head). Confirm the date against R2 first.`
- **F2.** Replace the bullet "A GitHub Actions run of `:solr:core` that covers FastVectorHighlighterTest completed successfully at this head." with one of:
  - `- A manual CI run on the author's fork passed this class on 2026-10-07, on an earlier commit that has the same Java code as this head.`
  - or delete the bullet. The receipt says the local gate stands alone.

Optional, not blocking:
- **N1.** Link the changelog path to `https://github.com/nick-boss-tech/solr/blob/180b6e8a7d3c23ac308a28d547f61816b5815f04/changelog/unreleased/SOLR-4540-fvh-skip-absent-fields.yml`. The approved template keeps it as code text, so the main side decides.
- **N2.** For the symptom link, point at base for "looks up the builder first": `https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java#L636`. The current head link is allowed by the assignment.
- **N3.** No Choice section. Correct, since the answers file records no choice for SOLR-4540. The behavior change (a bad builder name no longer fails for absent fields) is already stated under "What this change does".
- **N4.** "FVH" appears in the body before it is spelled out. The title spells out FastVectorHighlighter. Minor, for the simple-language rule.

## Not checked

- `g4540-premise.log` and the title re-gate log are not on disk in this worktree. The receipt is the only source for run results. No re-run (hard limit).
- The fork run's dispatched test list was not read. Checked: the workflow's default module (`:solr:core`) and the receipt's statement.
- `upstream/main` was not fetched. The merge-base was computed against the local ref.
- The changelog YAML was read, not re-parsed. The receipt says it parses.

# Highlighting post-PR review round 3, slice 3: SOLR-4540 (draft PR #5105)

Assignment: `assignments/pool-highlighting-post-pr-review-round-3.md`, slice 3. Prior round: `reports/highlighting-post-pr-review-round-2.md` and `reports/highlighting-post-pr-review-round-2-s3.md`. Sources: `origin/pr-prepare` (read with `git show`), the fork ref `origin/solr-4540-submit` (read-only fetch), live PR #5105 read through `research/gh.ps1` (pr view and GET api calls only), the local git objects for the base and head commits, and the JIRA packet `research/jira-context/SOLR-4540.json` (read only). Read-only throughout. No build, Gradle, test, Selenium, gate, or test-queue run. Nothing posted, edited, pushed, reviewed, or closed.

## Verdict

**STILL OPEN: 1 item.** The changelog file reference in the body is bare code, not a link. Every other check passes. The fix is one line (item 5 below). The same rule was applied to the SOLR-2681 changelog line in round 2 slice 2, so the lead should apply one rule to both slices.

## Head and PR state

- `git ls-remote origin refs/heads/solr-4540-submit`: `180b6e8a7d3c23ac308a28d547f61816b5815f04`. Equals the assignment head. The read-only fetch of the fork ref also gives `180b6e8`.
- Live PR #5105 on apache/solr: state OPEN, isDraft true, headRefName `solr-4540-submit`, headRefOid `180b6e8a7d3c23ac308a28d547f61816b5815f04`. Equal.
- Title: "SOLR-4540: FastVectorHighlighter no longer does per-field work for fields a document does not have". Accurate. The head change is a 5-line guard in `doHighlightingByFastVectorHighlighter` that returns null for a field the document lacks, before the builder lookup and the FVH call.

## Item table

| # | Item | Live wording (short quote) | Source check | Result |
|---|---|---|---|---|
| 1 | Round 2: symptom links at the base commit, text says so | "At the base commit, for each field name, the FVH step looks up the field's fragments builder first ([DefaultSolrHighlighter.java L636](...cabedd1d...#L636))." and "An unknown builder name throws a 400 error ([L415-L421](...cabedd1d...#L415-L421))." | Base L636 is `SolrFragmentsBuilder solrFb = getSolrFragmentsBuilder(fieldName, params);`, the first builder lookup in the FVH method. Base L415-L421 is `getSolrFragmentsBuilder`, which throws the 400 at L419-L420. Both links use `cabedd1d`. The "At the base commit" clause is in the same paragraph (see note N1). | SATISFIED |
| 2 | Round 1 item 1: pass count and date | "`FastVectorHighlighterTest` 3 of 3 pass with this change (verified 2026-10-09 at this head)." | Receipt Counts line: 3 of 3 at the head, from the title re-gate at 180b6e8, finished 2026-10-09. Head test file has 3 `@Test` methods. | SATISFIED |
| 3 | Fork CI sentence (run 37622803991 at 3543d10) | "A manual CI run on the author's fork passed this class on 2026-10-07, on an earlier commit that has the same Java code as this head." | Live API: run 37622803991 (workflow_dispatch, head_sha 3543d10, created 2026-10-07T12:41:19Z, success). Job `org.apache.solr.highlight.FastVectorHighlighterTest` success. `git diff 3543d10 180b6e8 -- solr` is empty. The only changes are the fork workflow file and the changelog title. The sentence names neither the run ID nor the commit (see N2, N3). | SATISFIED |
| 4 | Fix citations link the head SHA and contain the new code | "([DefaultSolrHighlighter.java L636-L640](...180b6e8...#L636-L640))" and "([FastVectorHighlighterTest.java L94-L123](...180b6e8...#L94-L123))" | Head L636 `if (doc.getFieldValues(fieldName) == null) {`, L639 `return null;`, L640 `}`. Head L94 is the javadoc start, L99 `@Test`, L100 the method, L123 its closing brace. | SATISFIED |
| 5 | Changelog file reference | "Changelog: `changelog/unreleased/SOLR-4540-fvh-skip-absent-fields.yml`" | A file citation written as bare code. The presentation rule (2026-10-08) says each file citation links the head SHA. The file exists at head and its title matches the PR title. | **STILL OPEN** (see fix below) |
| 6 | Title accurate | "SOLR-4540: FastVectorHighlighter no longer does per-field work for fields a document does not have" | Matches the head guard, which skips the FVH work for a field the document lacks. Same wording as the receipt's shipped changelog title. | SATISFIED |
| 7 | New test fails on base at line 106 | "On the base code, this test fails: `FastVectorHighlighterTest.testFieldAbsentFromDocIsSkipped` fails at line 106 of the test." | Receipt Proof line: on base production with the branch test in place, the test fails at line 106. Head L106 is the `assertQ(` call. The test file was last changed in fd68bae, so the line is the same at head. Receipt-based; not re-run (hard limit). | SATISFIED |
| 8 | Test description | "The index has two documents. The query matches only the first, which lacks the other two fields. The request uses `hl.fl=tv_*` and sets an unknown builder for each of those two fields." | Head L102 adds doc 1 with `tv_a`. L103 adds doc 2 with `tv_b` and `tv_c`. L110 `tv_a:alpha`. L116 `tv_*`. L117-L120 set `noSuchBuilder` for `tv_b` and `tv_c`. | SATISFIED |
| 9 | Behavior claims | "the method returns null before it looks up the builder or runs FVH ... The caller then tries the alternate field, as before." and "before, a bad `fragmentsBuilder` name failed the request whether or not the document had the field. Now it fails only when a matching document has that field." | Base caller loops every field for every document (base L503-L505) and reaches the FVH call when FVH is in use (base L539, L575). At base, L636 runs for every such field, so a bad name throws regardless of the document. At head the guard returns before the lookup. The caller calls `alternateField` when the field result is null (base L509, head L509). The only `fragmentsBuilder` validation in `solr/core/src/java` is head L416-L420. | SATISFIED |
| 10 | Symptom figures | "QTime above 10 seconds with `hl.fl=fulltext_*`, and about 30 ms with the explicit field name." | JIRA packet: "QTime is horribly big (> 10s)" with `fulltext_*`, and "QTime is acceptable (~30ms)" with `fulltext_1234`. | SATISFIED |
| 11 | Limits true of the head | "Covered: skipping absent fields on the FastVectorHighlighter path. Not covered: the timing in the ticket." "No timing is measured in this change. The test checks the builder path, not QTime." "The default highlighter path is not changed." | The diff to `DefaultSolrHighlighter.java` is 5 added lines inside `doHighlightingByFastVectorHighlighter`. No timing code is in the diff. `doHighlightingByHighlighter` is untouched. | SATISFIED |
| 12 | Choice section | None in the body. | The answers file records no choice for SOLR-4540. pr-formula does not require one here. The behavior change (a bad builder name on an absent field is no longer rejected) is stated openly in "What this change does", as section 2 requires. | SATISFIED (not required) |
| 13 | Answers: OPEN line filled from the premise run | "On the base code, this test fails ... at line 106 of the test." | Answers say the line is filled from the premise run. The body carries the base result and the line only. No seed or log name, which fits the vocabulary rule. | SATISFIED |
| 14 | Answers: changelog title corrected at packaging | Title as in item 6. | Answers say the title states the behavior change, with a re-gate. Receipt says the shipped title is this one, re-gated at 180b6e8. | SATISFIED |
| 15 | Each section opens with a bold one-line summary | Bold lines open What happens today, What this change does, Proof, and Limits. | Read at head. | SATISFIED |
| 16 | Lucene version mentions name the other versions | None. | No "lucene" or "version" in the body. | SATISFIED (N/A) |
| 17 | No internal vocabulary or run identifiers beyond the one CI reference | None. No seed, gate, receipt, premise, log, pool, branch, or run ID. SHAs appear only inside citation URLs (head and merge-base). | Grep of the body. Zero em or en dash bytes. | SATISFIED |
| 18 | Cross-repo references as full links | No PR references in the body. | Grep. | SATISFIED (N/A) |
| 19 | Length | 3,148 bytes (about 3,140 characters, two emoji). | Under the 3,500 guide. | SATISFIED |

Fix for item 5 (apply to the live body and to `pr-drafts/highlighting/SOLR-4540.md`, replacing the line that starts "Changelog:"):

`Changelog: [changelog/unreleased/SOLR-4540-fvh-skip-absent-fields.yml](https://github.com/nick-boss-tech/solr/blob/180b6e8a7d3c23ac308a28d547f61816b5815f04/changelog/unreleased/SOLR-4540-fvh-skip-absent-fields.yml)`

Tension for the lead: the approved template in `pr-formula.md` (2026-10-04) shows the changelog path as code. The presentation rule (2026-10-08) says every file citation is a link. Round 2 slice 2 applied the rule to SOLR-2681's changelog line. Round 2 slice 3 called the SOLR-4540 line "the main side's call". This report applies the rule as written. If the lead decides the template governs, mark item 5 SATISFIED and align SOLR-2681 to match.

## Citation table

| Link (as in the body) | SHA used | Fix or symptom | Lines and content at the SHA | Check |
|---|---|---|---|---|
| `DefaultSolrHighlighter.java#L636` (What happens today) | `cabedd1d968059215188f4e7563fb303241899ed` (merge-base) | Symptom | Base L636 `SolrFragmentsBuilder solrFb = getSolrFragmentsBuilder(fieldName, params);`. Base L634-L635 are reads only. | Text says "At the base commit". SATISFIED |
| `DefaultSolrHighlighter.java#L415-L421` (What happens today) | `cabedd1d968059215188f4e7563fb303241899ed` | Symptom | Base L415-L421 is `getSolrFragmentsBuilder`, with the 400 throw at L419-L420. Same code at head. | Same paragraph says "At the base commit" (N1). SATISFIED |
| `DefaultSolrHighlighter.java#L636-L640` (What this change does) | `180b6e8a7d3c23ac308a28d547f61816b5815f04` (head) | Fix | Head L636 guard, L639 `return null;`, L640 `}`. | SATISFIED |
| `FastVectorHighlighterTest.java#L94-L123` (Proof) | `180b6e8a7d3c23ac308a28d547f61816b5815f04` (head) | Fix (test) | Head L94 javadoc, L99 `@Test`, L100-L123 the method. | SATISFIED |
| `changelog/unreleased/SOLR-4540-fvh-skip-absent-fields.yml` (bare code, not a link) | would be head | File citation | File exists at head. Title matches. | **STILL OPEN** (item 5) |

## Proof and count check

Live Proof lines (quoted in the item table): "3 of 3 pass with this change (verified 2026-10-09 at this head)", "fails at line 106 of the test", and "passed this class on 2026-10-07, on an earlier commit that has the same Java code as this head."

| Number or claim | Live | Receipt (`receipts/SOLR-4540.md`) | Result |
|---|---|---|---|
| Count | 3 of 3 | 3 of 3 at the head, fresh JUnit XML | Match. Head has 3 `@Test` methods. |
| Date | 2026-10-09 at this head | Re-gate at 180b6e8 finished 2026-10-09 | Match. |
| Base failure line | 106 | Premise run: fails at line 106 | Match. Head L106 is the `assertQ(` call. |
| Fork CI date | 2026-10-07 | Run 37622803991 completed SUCCESS 2026-10-07 | Match. Also checked live (created 2026-10-07T12:41:19Z). |
| Fork CI commit | "earlier commit, same Java code" | 3543d10 on top of 62c06439fb0; Java code equal to head | Match. `git diff 3543d10 180b6e8 -- solr` is empty. "Earlier" means committed earlier (3543d10 at 2026-10-07 12:41 UTC, head at 2026-10-10 00:46 UTC). 3543d10 is not an ancestor of the head. The sentence does not claim one (N2). |

## Body vs draft

The live body (read from the PR JSON and written to a scratch file) and `pr-drafts/highlighting/SOLR-4540.md` on origin/pr-prepare are both LF-only, with no CR bytes. Both are 3,148 bytes. `diff` shows **no differences**. The item 5 fix must go into both files.

## CI and review state

| Check or run | Source | State at head `180b6e8` |
|---|---|---|
| labeler (Pull Request Labeler, run 38097779721, pull_request_target) | check-runs (the only check in `statusCheckRollup`) | COMPLETED, SUCCESS. Labels applied: `tests`, `cat:search`. |
| Gradle Precommit (run 38097779739) | Actions runs with head_sha = head | COMPLETED, **action_required** (not run) |
| Solr Tests via Crave (run 38097779754) | same | COMPLETED, **action_required** (not run) |
| Validate Changelog (run 38097779714) | same | COMPLETED, **action_required** (not run) |

- PR state: OPEN, isDraft true, reviewDecision empty, mergeStateStatus UNSTABLE. Head equals the fork tip.
- Reviews: none. Review comments: none. Issue comments: none. Requested reviewers and teams: none.
- The fork run 37622803991 is not upstream CI. The body does not present it as upstream CI.
- `action_required` means the upstream workflows have not run at this head, usually because they wait for maintainer approval. It is a state, not a code finding.

## Automated findings

- Verified: the labeler check succeeded and applied labels. It posted no comment. No code finding.
- Rejected: none. The three `action_required` workflow runs are states, not findings, so there is nothing to verify against the code.

## Non-blocking notes

- N1: The second symptom sentence, "An unknown builder name throws a 400 error", has no base wording of its own. It relies on "At the base commit," in the sentence before it. This satisfies the text rule. Optional: "At the base commit, an unknown builder name throws a 400 error ([L415-L421](...cabedd1d...#L415-L421))."
- N2: "On an earlier commit" is true by commit date, but 3543d10 is not an ancestor of the head. Optional: "on a fork commit from 2026-10-07 that has the same Java code as this head."
- N3: The fork run is described, not linked. Optional link: `https://github.com/nick-boss-tech/solr/actions/runs/37622803991`.
- N4: The Proof does not name the base SHA. Optional: "On the base code (cabedd1d968), this test fails ...".

## Not checked

- The premise and title-gate logs (`g4540-premise.log`, `g4540-title-gate.log`) are not on disk in this worktree. The receipt is the only source for run results, as the assignment says. No re-run was done (hard limit).
- The changelog YAML was read, not parsed. The receipt says it parses.
- No write call was made to GitHub. The only remote action was the read-only fetch of `refs/heads/solr-4540-submit`.

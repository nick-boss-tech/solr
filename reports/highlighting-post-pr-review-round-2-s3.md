# Highlighting post-PR review round 2, slice 3: SOLR-4540 (draft PR #5105)

Assignment: `assignments/pool-highlighting-post-pr-review-round-2.md`, slice 3. Round 1 part report: `reports/highlighting-post-pr-review-round-1-s3.md`. Sources: `origin/pr-prepare` (tip read with `git show`), the fork branch `origin/solr-4540-submit`, live PR #5105 read through `research/gh.ps1` (pr view, api reads only). Read-only throughout. No build, Gradle, test, gate, or test-queue run. Nothing posted, edited, pushed, or closed.

## Verdict

**STILL OPEN: 1 item.** Two symptom citations in the body link to the head commit, and one of them shows a line that no longer supports the sentence at head. Fix the two links (item 3a below) and the slice is SATISFIED. Round 1 items 1 and 2 are SATISFIED as written.

## Head and PR state

- `git ls-remote origin refs/heads/solr-4540-submit`: `180b6e8a7d3c23ac308a28d547f61816b5815f04`. Equals the assignment head.
- Live PR #5105 (apache/solr): state OPEN, isDraft true, headRefName `solr-4540-submit`, headRefOid `180b6e8a7d3c23ac308a28d547f61816b5815f04`. Equal.
- Title: "SOLR-4540: FastVectorHighlighter no longer does per-field work for fields a document does not have". Accurate. The head change is a 5-line guard in `doHighlightingByFastVectorHighlighter` that returns before the builder lookup and the FVH call for a field the document lacks. The title makes no timing claim.

## Item table

| # | Item | Live wording | Source check | Result |
|---|---|---|---|---|
| 1 | Round 1 item 1: pass count and date | "`FastVectorHighlighterTest` 3 of 3 pass with this change (verified 2026-10-09 at this head)." | Receipt Counts line: "FastVectorHighlighterTest 3 of 3 at the head ... (log g4540-title-gate.log, step 4 head counts tests=3, failures=0), finished 2026-10-09." The re-gate ran at 180b6e8 (the title commit = head). Head test file has 3 `@Test` methods. | SATISFIED |
| 2 | Round 1 item 2: CI sentence | "A manual CI run on the author's fork passed this class on 2026-10-07, on an earlier commit that has the same Java code as this head." | Receipt Corroboration line: run 37622803991 at 3543d10, SUCCESS, 2026-10-07. Live API: run 37622803991 (workflow_dispatch, head_sha 3543d10, created 2026-10-07T12:41:19Z, completed, success); job `org.apache.solr.highlight.FastVectorHighlighterTest` success. `git diff 3543d10 180b6e8`: only `.github/workflows/fork-test-runner.yml` and the changelog title change. No Java or test file differs. The body no longer says "at this head". | SATISFIED |
| 3 | Round 1 items unchanged in the draft | The Proof no longer says "passes at this head" for a CI run. | Same as item 2. | SATISFIED |
| 3a | Final read: symptom citations | "For each field name, the FVH step looks up the field's fragments builder first ([DefaultSolrHighlighter.java L641](...180b6e8.../DefaultSolrHighlighter.java#L641))." and "An unknown builder name throws a 400 error ([L415-L421](...180b6e8.../DefaultSolrHighlighter.java#L415-L421))." | At head, L636-L640 is the new guard, so L641 (the builder lookup) now runs only for documents that have the field. The claim "for each field name ... first" is true at the base commit only (base L636 is the first statement after the parameter reads). The head link shows the guard directly above the cited line, which contradicts the sentence. The head link for the 400 (L415-L421) shows the same code as base, so it is accurate but is a symptom link at head, not base. | **STILL OPEN** |
| 3b | Final read: fix citations | "([DefaultSolrHighlighter.java L636-L640](...180b6e8.../DefaultSolrHighlighter.java#L636-L640))" and the test link L94-L123 | Head L636 `if (doc.getFieldValues(fieldName) == null) {`, L639 `return null;`, L640 `}`. Head test L94 javadoc start, L99 `@Test`, L100 method signature, L123 closing brace. | SATISFIED |
| 4 | Final read: title | See Head and PR state. | Matches the head change. | SATISFIED |
| 5 | Final read: Proof numbers in receipt | "3 of 3", "2026-10-09", "2026-10-07", "line 106" | All four are in `receipts/SOLR-4540.md` (see the Proof and count check). | SATISFIED |
| 6 | Final read: the new test fails on base at line 106 | "On the base code, this test fails: ... fails at line 106 of the test." | Receipt Proof line: "on base production with the branch test in place, testFieldAbsentFromDocIsSkipped fails at line 106." Head test L106 is the `assertQ(` call. The test file has not changed since fd68bae (only the changelog changed after it), so line 106 is the same in the branch test file that ran on base. | SATISFIED (receipt-based; the premise log is not on disk, so not re-run) |
| 7 | Final read: test description | "The index has two documents. The query matches only the first, which lacks the other two fields. The request uses `hl.fl=tv_*` and sets an unknown builder for each of those two fields." | Head test L102-L103 adds doc 1 with `tv_a` and doc 2 with `tv_b`, `tv_c`. L110 `tv_a:alpha` matches doc 1 only. L116-L119 set `hl.fl` to `tv_*` and `noSuchBuilder` for `tv_b` and `tv_c`. | SATISFIED |
| 8 | Final read: behavior claims | "Behavior change: before, a bad `fragmentsBuilder` name failed the request whether or not the document had the field. Now it fails only when a matching document has that field." "The caller then tries the alternate field, as before." | Base L636 calls `getSolrFragmentsBuilder` unconditionally in the FVH method, so a bad name throws for any highlighted document. Head guard returns null first. Caller `doHighlighting` (head L507-L510) calls `alternateField` when the field result is null. | SATISFIED |
| 9 | Final read: symptom figures | "QTime above 10 seconds with `hl.fl=fulltext_*`, and about 30 ms with the explicit field name." | Local JIRA packet `research/jira-context/SOLR-4540.json` (read only): "> 10s" with `fulltext_*`, "~30ms" with `fulltext_1234`. | SATISFIED |
| 10 | Final read: Limits true at head | "Covered: skipping absent fields on the FastVectorHighlighter path. Not covered: the timing in the ticket." "No timing is measured in this change. The test checks the builder path, not QTime." "The default highlighter path is not changed." | Head diff from base to `DefaultSolrHighlighter.java` is the 5-line guard inside `doHighlightingByFastVectorHighlighter` only. `doHighlightingByHighlighter` is untouched. No timing code in the diff. | SATISFIED |
| 11 | Final read: Choice | No choice section. | The answers file records no choice call for SOLR-4540. A choice section is not required. | SATISFIED |
| 12 | Answers file: OPEN line | Base result, no seed, no log name. | Answers say the OPEN line is filled from the premise run with the seed and log removed. The draft says the base result and the line only. | SATISFIED |
| 13 | Answers file: changelog title | Title: "FastVectorHighlighter no longer does per-field work for fields a document does not have." | Answers ask for a title that states the behavior change, with a re-gate, and no timing claim. Head changelog YAML has this title. Receipt says the re-gate ran at this head. | SATISFIED |
| 14 | Section summaries | Each of What happens today, What this change does, Proof, Limits opens with a bold one-line summary. | Read at head (bold lines at body lines 7, 13, 20, 29). | SATISFIED |
| 15 | Lucene version mentions | None in the body. | Grep "lucene" or "version": 0 matches. | N/A |
| 16 | Internal vocabulary and run IDs | None. | Grep for seed, gate, receipt, ledger, premise, log, JUnit, rc=, pool, worktree, branch, CI run IDs, commit SHAs other than the PR head, and dashes: no matches (URL hits only for the fork name). No em or en dash bytes. | SATISFIED |
| 17 | Length | 3124 characters. | Under the 3,500 guide. | SATISFIED |

Item 3a fix, exact text (apply the same change to the live body and to `pr-drafts/highlighting/SOLR-4540.md`):

- In "What happens today", replace the L641 link with a base link and say so:
  `For each field name, the FVH step looks up the field's fragments builder first (at the base commit, [DefaultSolrHighlighter.java L636](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java#L636)).`
- Replace the L415-L421 link with the base commit, same file path, same lines (content is identical at base and head): `https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java#L415-L421`.
- Optional but useful: in the Proof, change "On the base code, this test fails" to "On the base code (cabedd1d968), this test fails", so a maintainer knows which base to check out.

Note for the lead: the formula (`pr-formula.md`, presentation rule) says to link each file citation at the head SHA. The round 2 assignment allows a base SHA for symptom citations if the body says so. This report follows the assignment, because the head line for L641 does not show the claim. If the lead reads the formula as controlling, the L641 sentence needs a different wording instead (for example, describe the base code without the word "first"), and the item stays open until one of the two is done.

## Citation table

All body links are to `nick-boss-tech/solr` blobs. Anchors checked with `git show <sha>:<path>`.

| Body link | Kind | SHA | Claim | Line at the SHA | Result |
|---|---|---|---|---|---|
| `DefaultSolrHighlighter.java#L641` | symptom | head 180b6e8 | builder lookup runs for each field name | L641 `SolrFragmentsBuilder solrFb = getSolrFragmentsBuilder(fieldName, params);` but after the new guard (L636-L640). Base L636 is the same statement with no guard above it. | **STILL OPEN** (3a) |
| `DefaultSolrHighlighter.java#L415-L421` | symptom | head 180b6e8 | unknown builder name throws a 400 | L418-L421 `if (solrFb == null) { throw new SolrException(BAD_REQUEST, "Unknown fragmentsBuilder: " + fb); }`. Same at base. | Accurate; move to base (3a) |
| `DefaultSolrHighlighter.java#L636-L640` | fix | head 180b6e8 | returns null before the builder lookup and FVH | L636 guard, L639 `return null;`, L640 `}` | SATISFIED |
| `FastVectorHighlighterTest.java#L94-L123` | test | head 180b6e8 | the new test | L94 javadoc, L99 `@Test`, L100-L123 method | SATISFIED |
| `changelog/unreleased/SOLR-4540-fvh-skip-absent-fields.yml` | file (code text, not a link) | head 180b6e8 | changelog fragment | File exists at head; title matches the PR title. | Note N1 |

## Proof and count check

Live Proof lines, quoted:

- "`FastVectorHighlighterTest` 3 of 3 pass with this change (verified 2026-10-09 at this head)."
- "On the base code, this test fails: `FastVectorHighlighterTest.testFieldAbsentFromDocIsSkipped` fails at line 106 of the test."
- "A manual CI run on the author's fork passed this class on 2026-10-07, on an earlier commit that has the same Java code as this head."

Receipt lines, quoted (`receipts/SOLR-4540.md` on origin/pr-prepare):

- Counts: "FastVectorHighlighterTest 3 of 3 at the head, from fresh JUnit XML ... The count is from the title re-gate at this head (log g4540-title-gate.log, step 4 head counts tests=3, failures=0), finished 2026-10-09."
- Proof: "on base production with the branch test in place, testFieldAbsentFromDocIsSkipped fails at line 106 (log g4540-premise.log)."
- Corroboration: "GitHub run 37622803991 ... at the earlier head 3543d10b34a7c790af348ede00ce034b1f2426bc ... completed SUCCESS (2026-10-07, before the Actions suspension)."

| Number | Live | Receipt | Result |
|---|---|---|---|
| Count | 3 of 3 | 3 of 3 at the head | match |
| Date | 2026-10-09 at this head | re-gate at 180b6e8 finished 2026-10-09 | match |
| Base failure line | 106 | 106 | match |
| Fork CI date | 2026-10-07 | run completed 2026-10-07 | match (also checked live) |
| Fork CI commit | "earlier commit, same Java code" | 3543d10, same Java code | match (diff: no Java change) |

Note: 3543d10 is not an ancestor of 180b6e8. Both sit on 62c06439fb0 (3543d10 on the CI branch, 180b6e8 on the ticket branch). "Earlier" is by date (committed 2026-10-07 versus 2026-10-10), and the body does not call it an ancestor. Accurate as written.

## Body vs draft

The live body (CRLF stripped, 3128 bytes) is byte-identical to `pr-drafts/highlighting/SOLR-4540.md` on origin/pr-prepare (CRLF stripped, 3128 bytes). No differences. The item 3a fix must go into both.

## CI and review state

| Check or run | Source | State at head |
|---|---|---|
| labeler (Pull Request Labeler, run 38097779721) | statusCheckRollup, check-runs | COMPLETED, SUCCESS (labels only) |
| Gradle Precommit (run 38097779739) | workflow runs with head_sha = head | COMPLETED, action_required (not run) |
| Solr Tests via Crave (run 38097779754) | same | COMPLETED, action_required (not run) |
| Validate Changelog (run 38097779714) | same | COMPLETED, action_required (not run) |

- mergeStateStatus UNSTABLE. reviewDecision empty. isDraft true.
- Reviews (`repos/apache/solr/pulls/5105/reviews`): none. Issue comments: none. Review comments: none.
- action_required is a state: upstream checks have not run at this head. It is not a code finding and this slice does not treat it as one. The fork run is not upstream CI, and the body does not present it as upstream CI.

## Automated findings

- Verified: none. The only automated check is the labeler, which succeeded and posted nothing.
- Rejected: none.

## Non-blocking notes

- N1: The changelog path is code text, not a link. The approved template keeps it as code text, so this is the main side's call. If linked: `https://github.com/nick-boss-tech/solr/blob/180b6e8a7d3c23ac308a28d547f61816b5815f04/changelog/unreleased/SOLR-4540-fvh-skip-absent-fields.yml`.
- N2: Limits do not say that only `FastVectorHighlighterTest` was run at this head. The Proof names only that class, so it is implied. An optional Limits line could say that other highlighting test classes were not re-run. Not required by the checklist.
- N3: The fork CI run is described but not linked. A link (`https://github.com/nick-boss-tech/solr/actions/runs/37622803991`) would let a maintainer check it. Optional.

## Not checked

- `g4540-premise.log` and `g4540-title-gate.log` are not on disk in this worktree. The receipt is the only source for run results. No re-run (hard limit).
- `upstream/main` is present locally at 3f5d4c5bf8, and the merge-base of 180b6e8 with it is cabedd1d968, which is the base used above. No fetch was made.
- The changelog YAML was read, not parsed. The receipt says it parses.
- No Gradle, build, or test was run. The "3 of 3" count is taken from the receipt; the static count of 3 `@Test` methods at head is consistent with it.

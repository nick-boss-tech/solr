# Highlighting post-PR review round 4, slice 3: SOLR-4540 (draft PR #5105)

Assignment: `assignments/pool-highlighting-post-pr-review-round-4.md`, slice 3. Sources: `origin/pr-prepare` (read with `git show`), `git ls-remote origin refs/heads/solr-4540-submit` for the fork tip, the local git objects for base `cabedd1d968059215188f4e7563fb303241899ed`, head `180b6e8a7d3c23ac308a28d547f61816b5815f04`, and the fork run commit `3543d10b34a7c790af348ede00ce034b1f2426bc`, live PR #5105 read through `research/gh.ps1` (pr view and GET api calls only), and the JIRA packet `research/jira-context/SOLR-4540.json` (read only).

Read-only throughout. No build, Gradle, test, Selenium, gate, or test-queue run. The optional read-only fork fetch was not run: the head commit is already present locally and the ls-remote tip matches it. Nothing was posted, edited, pushed, reviewed, or closed. No claim was made (the lead owns claims).

## Verdict

**SATISFIED.** Every item passes. The changelog line is a link at the head SHA, and the file exists at the head with the PR title. Each symptom citation is at the base commit and the text says so. Each fix citation is at the head and its lines hold the new code. Every Proof number matches the receipt. The live body is identical to the draft on origin/pr-prepare. Five notes (N1 to N5) are optional wording or judgment points and do not block the verdict.

## Head and PR state

- `git ls-remote origin refs/heads/solr-4540-submit`: `180b6e8a7d3c23ac308a28d547f61816b5815f04`. Equals the assignment head.
- Live PR #5105 on apache/solr: state OPEN, isDraft true, headRefName `solr-4540-submit`, headRefOid `180b6e8a7d3c23ac308a28d547f61816b5815f04`. Equal.
- Title: "SOLR-4540: FastVectorHighlighter no longer does per-field work for fields a document does not have". Accurate for the head change (a guard that skips the FVH step for a field the document lacks).
- Head diff against base: 3 files, 43 insertions. `DefaultSolrHighlighter.java` +5 inside `doHighlightingByFastVectorHighlighter`, `FastVectorHighlighterTest.java` +31, changelog YAML +7.

## Item table

| # | Item | Live wording (quote) | Source check | Result |
|---|---|---|---|---|
| 1 | Changelog line is a link at the head SHA | "Changelog: [changelog/unreleased/SOLR-4540-fvh-skip-absent-fields.yml](https://github.com/nick-boss-tech/solr/blob/180b6e8a7d3c23ac308a28d547f61816b5815f04/changelog/unreleased/SOLR-4540-fvh-skip-absent-fields.yml)" | `git ls-tree` at head: blob `8fd9327` at that path. File title at head: "FastVectorHighlighter no longer does per-field work for fields a document does not have." Same as the PR title. | SATISFIED |
| 2 | Symptom link, base L636 | "the FVH step looks up the field's fragments builder first ([DefaultSolrHighlighter.java L636](...cabedd1d...#L636))" | Base L636: `SolrFragmentsBuilder solrFb = getSolrFragmentsBuilder(fieldName, params);`, the first lookup in `doHighlightingByFastVectorHighlighter` (method starts base L626). The text says "At the base commit". | SATISFIED |
| 3 | Symptom link, base L415-L421 | "An unknown builder name throws a 400 error ([L415-L421](...cabedd1d...#L415-L421))." | Base L415-L421 is `getSolrFragmentsBuilder`; the throw is at L419-L420 (`BAD_REQUEST`, "Unknown fragmentsBuilder: "). The same paragraph opens with "At the base commit" (see N1). | SATISFIED |
| 4 | Fix link, head L636-L640 | "the method returns null before it looks up the builder or runs FVH ([DefaultSolrHighlighter.java L636-L640](...180b6e8...#L636-L640))" | Head L636 `if (doc.getFieldValues(fieldName) == null) {`, L637-L638 comment, L639 `return null;`, L640 `}`. L641 is the builder lookup. The claim holds. | SATISFIED |
| 5 | Fix link, head test L94-L123 | "[FastVectorHighlighterTest.java L94-L123](...180b6e8...#L94-L123)" | Head L94 `/**`, L99 `@Test`, L100 method start, L106 `assertQ(`, L123 `}`. | SATISFIED |
| 6 | Proof count and date | "`FastVectorHighlighterTest` 3 of 3 pass with this change (verified 2026-10-09 at this head)." | Receipt: 3 of 3 at head 180b6e8 from the title re-gate (log g4540-title-gate.log, step 4 tests=3, failures=0), finished 2026-10-09. Head test file has 3 `@Test` methods. | SATISFIED |
| 7 | Fork CI sentence | "A manual CI run on the author's fork passed this class on 2026-10-07, on an earlier commit that has the same Java code as this head." | Live API: run 37622803991, name "Fork test runner", workflow_dispatch, head_sha `3543d10b`, created 2026-10-07T12:41:19Z, completed success. Job `org.apache.solr.highlight.FastVectorHighlighterTest` completed success (12:41:30 to 12:45:12Z). `git diff 3543d10 180b6e8 -- solr` is empty. "Earlier" holds by time (see N2). | SATISFIED |
| 8 | New test fails on base at line 106 | "On the base code, this test fails: `FastVectorHighlighterTest.testFieldAbsentFromDocIsSkipped` fails at line 106 of the test." | Receipt: on base production with the branch test in place, the test fails at test line 106 (log g4540-premise.log). Head L106 is the `assertQ(` call. Not re-run (hard limit). | SATISFIED |
| 9 | Test description | "The index has two documents. The query matches only the first, which lacks the other two fields. The request uses `hl.fl=tv_*` and sets an unknown builder for each of those two fields." | Head L102 adds doc 1 with `tv_a`. L103 adds doc 2 with `tv_b` and `tv_c`. L110 `tv_a:alpha`. L116 `tv_*`. L117-L120 set `noSuchBuilder` for `tv_b` and `tv_c`. | SATISFIED |
| 10 | Behavior: returns null, then alternate field | "the method returns null before it looks up the builder or runs FVH ... The caller then tries the alternate field, as before." | Base caller (field loop L499, call at L507-L509) calls `alternateField` when the field result is null. Same at head (L509). Base L651-L652 return null for empty snippets, so a non-stored term-vector field gives the same result. | SATISFIED |
| 11 | Behavior change: bad builder name | "before, a bad `fragmentsBuilder` name failed the request whether or not the document had the field. Now it fails only when a matching document has that field." | Base: `doHighlightingOfField` reaches FVH (L539, L575) for every document in the result set, and base L636 throws for an unknown name, so the request fails whatever the document holds. Head: the guard (L636) returns before the lookup (L641). `getDocPrefetchFieldNames` (base L590) always returns a set, so `doc` holds the highlighted and alternate fields and the guard does not skip a present field. The only builder check in `solr/core/src/java` is head L419-L420. | SATISFIED |
| 12 | Symptom figures | "QTime above 10 seconds with `hl.fl=fulltext_*`, and about 30 ms with the explicit field name." | JIRA packet: "QTime is horribly big (> 10s)" with `fulltext_*`; "QTime is acceptable (~30ms)" with `fulltext_1234`. The body attributes these to the reporter. | SATISFIED |
| 13 | Title | "SOLR-4540: FastVectorHighlighter no longer does per-field work for fields a document does not have" | Matches the head guard and the changelog title. | SATISFIED |
| 14 | Limits, summary line | "Covered: skipping absent fields on the FastVectorHighlighter path. Not covered: the timing in the ticket." | The diff has no timing code. The change covers the FVH path only. | SATISFIED |
| 15 | Limits, timing and default path | "No timing is measured in this change. The test checks the builder path, not QTime." "The default highlighter path is not changed." | The diff touches only `doHighlightingByFastVectorHighlighter` (head L636-L640, inside the method that starts at L626). `doHighlightingByHighlighter` (head L664) is not in the diff. | SATISFIED |
| 16 | Choice section | None in the body. | Formula: a choice section only with a live alternative. The behavior change is stated in "What this change does", as section 2 requires. See N4 for the judgment point. | SATISFIED (N4) |
| 17 | Bold one-line summary per section | Bold lines open "What happens today", "What this change does", "Proof", "Limits". | Read at head. | SATISFIED |
| 18 | Answers: OPEN line filled from the run log | "fails at line 106 of the test" (no seed, no log name). | Answers file: OPEN line filled from the premise run. Body carries the result and the line only. | SATISFIED |
| 19 | Answers: changelog title corrected at packaging | Title as in item 1. | Receipt: shipped title is "FastVectorHighlighter no longer does per-field work for fields a document does not have", re-gated at 180b6e8. | SATISFIED |
| 20 | Round 3 item: changelog line as bare code | Now a link (item 1). | Round 3 slice 3 STILL OPEN item is closed. | SATISFIED |
| 21 | Internal vocabulary | No gate, receipt, ledger, handoff, takeover, audit, JUnit XML, premise, seed, log name, claim, pool, workspace, or "submission". "runs" (lines 15 and 16) is plain English. The one CI reference (line 25) is the sanctioned one. | Grep of the live body. | SATISFIED |
| 22 | Dashes | No em dash or en dash bytes in the body. | Grep, LC_ALL=C. | SATISFIED |
| 23 | Lucene version mention | None in the body. | Grep. | SATISFIED (N/A) |
| 24 | Cross-repo references | No PR or issue links except the JIRA link. | Grep. | SATISFIED (N/A) |
| 25 | Length | 3,292 bytes. | Under the 3,500 guide. | SATISFIED |
| 26 | AI header and footer | Line 1 header; "### AI assistance" footer with the approved wording. | Read at head. | SATISFIED |

## Citation table

| Link (in body) | SHA used | Fix or symptom | Lines and content at that SHA | Check |
|---|---|---|---|---|
| `DefaultSolrHighlighter.java#L636` | `cabedd1d968059215188f4e7563fb303241899ed` (base) | Symptom | L636 `getSolrFragmentsBuilder(fieldName, params)` in the FVH method | Text says "At the base commit". SATISFIED |
| `DefaultSolrHighlighter.java#L415-L421` | `cabedd1d968059215188f4e7563fb303241899ed` (base) | Symptom | L415-L421 `getSolrFragmentsBuilder`, throw at L419-L420 | Same paragraph says "At the base commit" (N1). SATISFIED |
| `DefaultSolrHighlighter.java#L636-L640` | `180b6e8a7d3c23ac308a28d547f61816b5815f04` (head) | Fix | L636 guard `if (doc.getFieldValues(fieldName) == null) {`, L639 `return null;`, L640 `}` | SATISFIED |
| `FastVectorHighlighterTest.java#L94-L123` | `180b6e8a7d3c23ac308a28d547f61816b5815f04` (head) | Fix (test) | L94 `/**`, L99 `@Test`, L106 `assertQ(`, L123 `}` | SATISFIED |
| `changelog/unreleased/SOLR-4540-fvh-skip-absent-fields.yml` | `180b6e8a7d3c23ac308a28d547f61816b5815f04` (head) | File citation (not a code claim) | Blob `8fd9327` at head; title matches the PR title | SATISFIED |
| JIRA link `issues.apache.org/jira/browse/SOLR-4540` | none | Not a code citation | Kept per the template | SATISFIED |

## Proof and count check

| Number or claim | Live | Receipt (`receipts/SOLR-4540.md`) or source | Result |
|---|---|---|---|
| Count | 3 of 3 | 3 of 3 at head 180b6e8, title re-gate (log g4540-title-gate.log, step 4 tests=3, failures=0). Head has 3 `@Test` methods. | Match |
| Date | 2026-10-09 at this head | Re-gate at 180b6e8 finished 2026-10-09 | Match |
| Base failure line | 106 | Premise run: fails at test line 106 (log g4540-premise.log). Head L106 is `assertQ(`. | Match |
| Fork CI run | 2026-10-07, class passed, earlier commit, same Java code | Run 37622803991 at 3543d10, SUCCESS, created 2026-10-07T12:41:19Z. Job FastVectorHighlighterTest success. `git diff 3543d10 180b6e8 -- solr` empty. | Match |

## Body vs draft

- Live body: read with `research/gh.ps1 pr view 5105 --json body --jq .body` and saved to the session scratchpad. Draft: `pr-drafts/highlighting/SOLR-4540.md` on origin/pr-prepare.
- CR bytes: 0 in each file.
- Byte size: 3,292 bytes in each. The live capture added one trailing LF during save; after removing it, `cmp` reports the two files identical.
- `diff` after CR strip: no differences. The item 1 fix from round 3 is in both.

## CI and review state

| Check or run | Source | State at head 180b6e8 |
|---|---|---|
| Pull Request Labeler (run 38097779721) | `statusCheckRollup` (the only check listed there) | COMPLETED, SUCCESS |
| Gradle Precommit (run 38097779739) | Actions runs at head_sha | COMPLETED, **action_required** (not run) |
| Solr Tests via Crave (run 38097779754) | same | COMPLETED, **action_required** (not run) |
| Validate Changelog (run 38097779714) | same | COMPLETED, **action_required** (not run) |

- PR: OPEN, isDraft true, reviewDecision empty, mergeStateStatus UNSTABLE.
- Labels: `tests`, `cat:search`.
- Reviews (`repos/apache/solr/pulls/5105/reviews`): none.
- Issue comments (`repos/apache/solr/issues/5105/comments`): none.
- Review comments (`repos/apache/solr/pulls/5105/comments`): 0.
- `action_required` means the upstream workflows have not run at this head, usually because they wait for maintainer approval. It is a state, not a code finding.

## Automated findings

- Verified: the labeler check succeeded and applied `tests` and `cat:search`. It posted no comment, so there is no code finding to check.
- Verified as states, not findings: the three `action_required` workflow runs at head 180b6e8.
- Rejected: none. No bot or reviewer comment exists on the PR to test against the code.

## Notes (non-blocking)

- N1 (item 3, optional). The second symptom sentence, "An unknown builder name throws a 400 error", carries the base claim only through the paragraph's "At the base commit," clause. Optional, to make it explicit: "At the base commit, an unknown builder name throws a 400 error ([L415-L421](...cabedd1d...#L415-L421))."
- N2 (item 7, optional). "On an earlier commit" holds by time: 3543d10 is dated 2026-10-07T12:41:08Z, the head 2026-10-10T00:46:28Z. But 3543d10 is not an ancestor of the head (`merge-base --is-ancestor` fails): it carries the fork workflow file that the head removed. Optional, to avoid the ancestry reading: "on a fork commit from 2026-10-07 that has the same Java code as this head."
- N3 (item 7, optional). The fork run is described, not linked. The body names neither the run nor the commit, which fits the vocabulary rule. Optional link: `https://github.com/nick-boss-tech/solr/actions/runs/37622803991`.
- N4 (item 16, judgment, not required). No choice section. The implemented route moves the builder-name check behind the document-has-field test. A maintainer could plausibly reject that behavior change and want an eager check across all documents, which keeps today's 400 on every request. `pr-formula.md` asks for a choice section only with a live alternative, and rounds 2 and 3 did not add one. The lead may decide; the verdict does not depend on it.
- N5 (optional). The Proof does not name the base SHA. Optional: "On the base code (cabedd1d968), this test fails ...".

## Not checked

- No build, Gradle, test, Selenium, gate, or test-queue run. The 3 of 3 count and the line 106 failure come from the receipt and were not re-run.
- Gate logs g4540-premise.log and g4540-title-gate.log are not on disk in this worktree. The receipt is the only source for their results.
- The changelog YAML was read, not parsed. The receipt says it parses.
- Requested reviewers and teams were not queried (not in the assignment).
- No fetch was run. The head, base, and fork run commits are present locally, and ls-remote matched the head.

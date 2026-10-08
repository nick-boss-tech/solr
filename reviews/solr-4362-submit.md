# solr-4362-submit

- Branch: origin/solr-4362-submit
- Head: e96a057439c8 (matches the listed head)
- Base: upstream/main at 9d7cc2884e8a (merge-base 14c7aac0d151, 45 commits behind). This base is older than the other eDisMax branches. No upstream commit has touched `ExtendedDismaxQParser.java` or its test since that merge-base (checked), so the three-dot diff is not missing drift.
- Scope: 6 commits, 3 files (+100). `ExtendedDismaxQParser.java` (+42: `isSlopClause` at lines 297-331, `previousClause` and `followsPhrase` in `addPhraseFieldQueries` at lines 344-350), `TestExtendedDismaxParser.java` (+49), changelog `SOLR-4362-edismax-pf-phrase-slop.yml` (+9, type `fixed`)
- Verdict: Close (the slop rule is correct at head. Whether the rule covers `term~N` is an owner call on scope.)
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

## Delta check against earlier disposition

- Bulk review `research/branch-reviews/round-28/SOLR-4362-review.md` (verdict Close) was written at snapshot `f0f3d1f004a`, an ancestor of the head. The delta is one commit, `e96a057439c` ("a boost on a phrase slop is part of the phrase clause"), plus its tests.
- Round-28 F1 (a slop with a boost still leaks): addressed. `isSlopClause` now accepts a `\^` boost with digits or one dot after the slop digits (lines 311-331). `"phrase query"~10^2 term` is covered by the test.
- Round-28 F2 (the test would pass if pf2 never ran): addressed. The new positive control `"x y"~2 foo bar` must still produce `"foo bar"`, and the main-query side keeps `"phrase query"~10`.
- Round-28 F3 (the rule cannot tell `"a b"~10` from `"a b" ~10`): unchanged. Both spellings produce the same clause shape, and Lucene reads both as slop, so skipping both is correct.
- Round-28 F4 (handoff doc added and removed): no handoff or TESTING file at head.
- Round-28 F5 (`term~2` leak): still open; see finding 3.

## Verified code facts

- Lucene reads a whitespace-separated `~N` after a quoted token as slop. `QueryParser.jj` lines 286-287 and 316-317 accept `FUZZY_SLOP` after a quoted token, and `CARAT` and `FUZZY_SLOP` in either order.
- `previousClause` is updated for every clause before the `continue`s (lines 344-347). A fielded phrase (`title:"a b"~3`) therefore still marks the next slop clause.

## Findings (ranked)

1. **LOW, verified. The slop rule is limited to a leading `\~N`.** `isSlopClause` requires the clause value to start with `\~`. A term with its own fuzzy modifier, such as `foo~2 bar`, becomes the clause `foo\~2`, which is not skipped. By the same mechanism as the ticket, the pf analyzer would see `foo~2` and could build a `foo 2 bar` shingle. I did not trace or run this. It is outside the ticket's stated scope (the slop of a quoted phrase).

2. **LOW, verified. The ticket case is discriminating by reading.** Base builds a pf2 shingle from the `\~10` clause for `"phrase query"~10 term`, which gives the `10 term` text the test forbids. The test is therefore discriminating by reading. The positive control (`"x y"~2 foo bar`) guards against a pf2 that never runs.

3. **LOW, proof. Outcomes are not run.** The discriminating assertions and the positive control are in the test diff. They have not been run, so pass and fail on base and head are hypotheses.

4. **LOW, history. The handoff doc was added and removed on the branch.** Commit `6631400a9f4` adds the hypothetical-reproduction handoff doc and `f0f3d1f004a` removes it. `007e293656f` fixes the author line. The net diff is clean, and no TESTING or handoff file is present at head. The commits stay in the branch history. No force-push. Whether to squash is the maintainers' call on merge.

## Owner calls (not decided here)

- Scope: only the slop of a quoted phrase (the ticket), or also a `term~N` fuzzy modifier (finding 1). Either way, this is separate from the `~N^boost` fix, which is correct.
- The handoff ties the SOLR-6320 demotion rule to this branch as a PR-time call. I found no code dependency: the slop skip does not read operator words. The only shared place is the pf skip of `AND|OR|NOT|TO` (base line 313), which neither branch changes.

## Interactions with other branches

- SOLR-6320: the pf code still skips only exact uppercase `AND|OR|NOT|TO` words by `clause.val`. A demoted lowercase `or` therefore stays a pf term. That matches the main query and is base behaviour.
- SOLR-3962: both branches add a `continue` to the same loop in `addPhraseFieldQueries` (3962 at line 310, 4362 at line 350). The semantics do not conflict, but the two edits will conflict textually if both land.

## Not checked

- Not compiled, formatted, or run.
- The base shingle output for the ticket example was traced by reading only.
- The `"..."~N^B` grammar ordering was read from `QueryParser.jj`, not run.
- No GitHub or JIRA writes.

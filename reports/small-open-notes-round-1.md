# Small open notes, round 1

Verdicts: SOLR-12864 APPLIED (one Limits bullet deleted). SOLR-5939 APPLIED (one link re-pointed to the merge-base). SOLR-6065 APPLIED (one link re-pointed to the merge-base). One wording conflict with the assignment, decided in favour of the recorded recommendation. See the section on the link forms.

Scope: `assignments/pool-small-open-notes-round-1.md`. Claim: `claims/pool-small-open-notes-round-1.md`. One read-only subagent. Its verdicts were checked here against the git objects before any edit.

## SOLR-12864: Limits line 31

Note: `reports/update-processing-convergence-round-3.md` says to delete Limits line 31.

Verdict: APPLIED. The bullet is true, and it repeats the first clause of the bold line above it ("The test covers one combination"). The combination itself is already stated in "What this change does", so deleting the bullet loses no content. The next bullet, about the `srcField` branch, stays.

Ladder rung: step 1 (the round note, and the draft's claim checked against the test at the head), then step 2 (apply the note).

Before (Limits, lines 29 to 32):

> **The test covers one combination, and each run takes one random branch.**
>
> - The test covers one combination only: echo with `mapUniqueKeyOnly` on the JSON update path.
> - The `srcField` branch is chosen at random in the test, so a single run uses one branch.

After:

> **The test covers one combination, and each run takes one random branch.**
>
> - The `srcField` branch is chosen at random in the test, so a single run uses one branch.

## SOLR-5939 and SOLR-6065: link forms

Notes: `reports/update-processing-convergence-round-1.md` (UPC-R1-227 (d) for SOLR-6065: "base line 765 is the form to use"), and `reports/update-processing-convergence-round-3.md` (line 76: "the merge-base form is preferred for unchanged content").

Verdict: APPLIED, to the merge-base form. Both files named by the notes are unchanged between the merge-base and the draft's head. `git diff --name-only` is empty for `solr/core/src/java/org/apache/solr/update/processor/DistributedUpdateProcessor.java` between `cabedd1d968059215188f4e7563fb303241899ed` and `f8d4bdbea518901e255ae119f3e5c43e7804a9bf`, and for `gradle/libs.versions.toml` between `97d973814336101e12475558d7419321c743de79` and `3d2cec9e1ab3a19f6b948682b116c2780da4b65b`. A link at the merge-base therefore shows the same lines as a link at the head.

Wording conflict: the assignment says to use "the same head SHA the draft already names" and "do not change the head". The round notes recommend the merge-base form for unchanged content. The decision default adopts a recorded recommendation, so the merge-base form is applied. The head SHAs the drafts name for their changed files (`f8d4bdbea518` for SOLR-5939, `3d2cec9e1ab3` for SOLR-6065) are unchanged. Only the two unchanged-content links move. If the main side wants the head form instead, reverse the two link edits; the verdict would then be KEPT WITH LINK.

Ladder rung: step 1 (the round notes, and `git diff` on both file pairs), then step 2 (apply the recorded form).

SOLR-5939, Limits, client-visible text bullet:

- Before: `https://github.com/nick-boss-tech/solr/blob/f8d4bdbea518901e255ae119f3e5c43e7804a9bf/solr/core/src/java/org/apache/solr/update/processor/DistributedUpdateProcessor.java#L1267-L1282`
- After: `https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/update/processor/DistributedUpdateProcessor.java#L1267-L1282`

Lines 1267 to 1282 hold the `buildMsg` method at the merge-base (`git show` of the merge-base file), and the file is the same at the head.

SOLR-6065, Proof-to-Limits Lucene pin link (line 39):

- Before: `https://github.com/nick-boss-tech/solr/blob/3d2cec9e1ab3a19f6b948682b116c2780da4b65b/gradle/libs.versions.toml#L39`
- After: `https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/gradle/libs.versions.toml#L39`

Line 39 of the file at the merge-base reads `apache-lucene = "10.4.0"`, the pin the sentence names. The draft's other base-code link in the same section (line 38, `addIndexes`) already uses the merge-base form.

## Checks

- `git cat-file -e` passes for `cabedd1d968059215188f4e7563fb303241899ed:solr/core/src/java/org/apache/solr/update/processor/DistributedUpdateProcessor.java`.
- `git cat-file -e` passes for `97d973814336101e12475558d7419321c743de79:gradle/libs.versions.toml`.
- The SOLR-12864 edit changes no link. Its remaining links are at the merge-base and were not touched.
- `git diff --stat`: three files, one line removed (SOLR-12864), two lines changed (SOLR-5939, SOLR-6065).
- No dash or process word was introduced in the edited text.
- No build, test, Gradle run or public post was made.

## Note for the main side

- The SOLR-12864 note is applied as written.
- The SOLR-5939 and SOLR-6065 link edits follow the round notes' merge-base form, not the assignment's head-SHA wording. The head-SHA form is one line each to restore if the main side prefers it.

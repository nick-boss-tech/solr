# Gate job: SOLR-10305 branch text (changelog line and test javadoc)

- Branch: solr-10305-submit. Head: `c43de86d4c133ed4987ac8963d1a3806e40c5132` (live tip, read with `git ls-remote origin refs/heads/solr-10305-submit` on the Windows review host, 2026-10-14; matches the gated head in receipts/SOLR-10305.md).
- Intended host: any capable Linux host with the Gradle toolchain (gate capability). The Windows host does not run it.
- Posted by: the review round for search components drafts (pool-census-followup-search-components-drafts-round-1, slice S4). Spec source: reports/receipt-refresh-round-2-b1.md, findings F6 and F7 (replacement wording recorded there).
Status: DONE
- Why: the changelog line and the test javadoc on the branch name the wrong place for the NullPointerException. The code site is `QueryComponent.createMainQuery` (base commit cabedd1d968, line 793), which runs before any shard answers, not `mergeIds` or "while merging shard responses".
- Decision recorded here (scope, owner decision 3 in receipt-refresh-round-2-b1.md): the default standalone case, where the URL allow list returns 403 before `QueryComponent.prepare`, is not changed by this branch. It is named in the draft's Limits as a follow-up. No code change. Settled under the ladder's third rung (optional, defer as a named follow-up), because the reported NullPointerException is not reached in that setup.

## Edits (text only, no logic change)

1. `changelog/unreleased/SOLR-10305-distributed-query-requires-unique-key.yml`, line 2. Replace the phrase "instead of a NullPointerException while merging shard responses." with the full line:
   `  A distributed query (shards parameter) against a schema without a uniqueKey now fails with a 400 "Distributed search requires a uniqueKey field in the schema" instead of a NullPointerException.`
   Keep the `title: >` folded shape.
2. `solr/core/src/test/org/apache/solr/MinimalSchemaTest.java`, line 76. Replace the javadoc line with:
   `/** SOLR-10305: a shards request on a schema without a uniqueKey must be a 400, not a NullPointerException */`
   This line is longer than 100 columns, so the formatter will rewrap it. Keep the rewrapped form. Do not change any code, the allow-list hook at lines 37 to 40, or the test body.

## Steps

1. Delta check: `git diff --stat c43de86d4c133ed4987ac8963d1a3806e40c5132 HEAD` must list only the changelog fragment and `MinimalSchemaTest.java`. Stop and record here if anything else differs.
2. Parse the changelog fragment with a real YAML parser. The title must equal the text in edit 1 exactly.
3. Formatting and compile for the one test file: `:solr:core:spotlessApply` on that file, then `:solr:core:spotlessJavaCheck` and `:solr:core:compileTestJava`. Record the results. If spotless rewraps the javadoc, that rewrap is the committed text.
4. Focused test run: not owed. The change is comment and changelog text only. The receipt rule is that a comment-only edit is not test setup, so state that in the receipt. The proof at the old head stays labelled as the proof at `c43de86d4c13`.

## On green

- Commit the two text edits as one commit under the Nick Shanin identity the workflow requires. No co-author trailer and no other name in the author or committer fields.
- Push plainly (fast-forward) to `solr-10305-submit`.
- Refresh `receipts/SOLR-10305.md`: new live tip, a line that the delta from `c43de86d4c13` is text only, and the proof left at `c43de86d4c13` and labelled as such.
- Re-point every link in `pr-drafts/search-components/SOLR-10305.md` from `c43de86d4c13` to the new head, then re-read the draft.
- Mark this job DONE with the new head.

## Step checklist

- [x] delta against c43de86d4c13 is the changelog fragment and MinimalSchemaTest.java only
- [x] changelog YAML parsed; title equals edit 1
- [x] spotless apply, spotless check and compileTestJava on the test file recorded
- [x] pushed plainly to solr-10305-submit; receipt refreshed; draft links re-pointed; job DONE

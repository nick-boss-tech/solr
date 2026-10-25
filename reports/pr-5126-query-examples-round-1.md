# PR 5126 query examples, round 1 (slice Q)

Verdict: REPRODUCED, only with `hl.weightMatches=false`. With the default `hl.weightMatches=true` (`UnifiedSolrHighlighter.java:472`), the cats highlight did not appear in any of the 12 rows.

Claim: `claims/pr-5126-query-examples-vm4-round-1.md` (active), done record `claims/pr-5126-query-examples-vm4-round-1-done.md`.
Assignment: `assignments/pr-5126-linux-examples-round-1.md` (not edited; records are add-only).
Ticket and PR: SOLR-3479, apache/solr PR 5126 (David Smiley), still open, not draft, at pinned head `74b09fb443f060caac3f8ec9701909cec276cfb4` at the time of checking (2026-10-25). Base `bd2b2d5a02584893c5b5fe16c1a7eee89c2bb6c9`.

## Baseline and diagnostic heads

- Baseline (production source): `74b09fb443f060caac3f8ec9701909cec276cfb4`, fetched from `refs/pull/5126/head` and checked out detached in `/workspace/worktrees/pr-5126-q`.
- Diagnostic commit: `022eb6d7811cd876afde3b84115696cb1c6af50e`, branch `solr-3479-pr5126-query-examples-r1` on `nick-boss-tech/solr` (remote read back as the same full SHA). Author Nick Shanin, no trailers.
- The commit changes one file: `solr/core/src/test/org/apache/solr/response/transform/TestHighlightAugmenterFactory.java`, +94 lines, 0 deletions. `git diff 74b09fb HEAD --stat` shows no other file. Production source is byte-identical to the pinned head.
- Patch: `evidence/test-patch-022eb6d781.patch`.
- No changelog fragment (diagnostic, test-only).

## Environment

- Host `vm4`, Linux aarch64, JDK 21.0.12.1 at `/workspace/tools/jdk21`, Gradle 9.7.0 via the repo wrapper. The wrapper jar was bootstrapped by `gradlew` and matches the repo's `gradle-wrapper.jar.sha256`.
- Runner: `pool/bin/sweep-run.sh` from `coordination-ui`, under `/workspace/tools/.gradle-vm4.lock`, with the inode guard. Free space on `/` stayed at 4.6G and inodes at about 479,570 free before and after each leg.
- Seed `34795126` (`-Ptests.seed=34795126`) on every leg.
- Raw logs stay on the VM under `/workspace/gates/logs/vm4/pr-5126/`. Copies of the XML and run records are in `evidence/`.

## Runs

| Leg | Command (task form) | UTC start | UTC end | Exit | Result |
| --- | --- | --- | --- | --- | --- |
| 1 focused matrix | `:solr:core:cleanTest :solr:core:test --tests ...TestHighlightAugmenterFactory.testReview5126QueryExamples` | 16:14:01 | 16:20:31 | 0 | 1 test, 0 failures, 0 errors |
| 2 regression | `:solr:core:cleanTest :solr:core:test --tests ...TestHighlightAugmenterFactory.testReview5126ImplicitBoostDiffersFromExplicit` | 16:21:44 | 16:23:19 | 0 | 1 test, 0 failures, 0 errors |
| 3 tidy and compile | `tidy :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true` | 16:24:43 | 16:30:54 | 0 | BUILD SUCCESSFUL |
| 4 whole class | `:solr:core:cleanTest :solr:core:test --tests ...TestHighlightAugmenterFactory` | 16:32:23 | 16:36:49 | 0 | 10 tests, 0 failures, 0 errors, 0 skipped |

Leg 1 ran before the regression method was added and before tidy; its matrix text is unchanged by tidy in substance. Leg 4 is the final code. The whole class has 8 original methods plus the 2 new ones. Tidy's delta was in the diagnostic test only; the 94-line diff is the test patch.

## Harness

- Method `testReview5126QueryExamples` runs the 24 requests from the assignment: weightMatches (true, false) by boost (`bq` absent, `bq=body_t:cats`) by component (`hl=false`, `hl=true` with `hl.method=unified`, `hl.fl=body_t`) by variant (implicit; `hl.q=fox`; local `q=fox` in `fl`). It prints each request and the full response before its assertions.
- Assertions in the matrix: two documents returned in order 1, 2; document 1 has `<em>fox</em>`; document 2 has no `<em>cats</em>` in the transformer output for every row except implicit with `bq`, where it is not asserted.
- Harness change from the assignment's snippet: the component-highlighting `cats` assertion was removed and recorded as an observation only, because the assignment asks for component output as a separate comparison and asserting it would stop the loop on the disputed case.
- Method `testReview5126ImplicitBoostDiffersFromExplicit` (regression leg) uses `weightMatches=false` and `bq=body_t:cats`. It asserts that the implicit doc 2 body contains `<em>cats</em>` and that the `hl.q=fox` version does not. This passes on unchanged production. It documents the difference; it is not a fix and not a green regression.

## Fixture

Default configset (`ExternalPaths.DEFAULT_CONFIGSET`), with the existing `TestHighlightAugmenterFactory` documents:

| id | title_t | body_t |
| --- | --- | --- |
| 1 | Fox story | The quick brown fox jumps over the lazy dog. Nothing else here. The fox ran away. |
| 2 | Fox cameo | A story about cats. Cats sleep a lot. |

`*_t` is the dynamic field `text_general` (`managed-schema.xml:150`, type at line 299): standard tokenizer with lowercasing, multiValued false.

## Matrix result (24 rows)

Document 1 has `<em>fox</em>` in all 24 rows (positive control).

Document 2, `cats` highlighted by the transformer:

| weightMatches | bq | hl (component) | implicit | explicit (`hl.q=fox`) | local (`q=fox` in fl) |
| --- | --- | --- | --- | --- | --- |
| true | absent | false | no | no | no |
| true | absent | true | no | no | no |
| true | `body_t:cats` | false | no | no | no |
| true | `body_t:cats` | true | no | no | no |
| false | absent | false | no | no | no |
| false | absent | true | no | no | no |
| false | `body_t:cats` | false | **yes** | no | no |
| false | `body_t:cats` | true | **yes** | no | no |

The component highlighting (`highlighting` section, `hl=true`) lists only document 1 (`body_t: "The <em>fox</em> ran away."`) in all 12 component rows. It never highlights `cats`, including implicit with `bq`.

## Smallest reproducing pair (weightMatches=false)

Shared parameters: `q=fox&defType=edismax&qf=body_t title_t&bq=body_t:cats&sort=id asc&rows=10&fl=id,body_hl:[hl f=body_t]&hl.method=unified&hl.defaultSummary=false&hl.tag.pre=<em>&hl.tag.post=</em>&hl.weightMatches=false`

- Request A (implicit): document 2 `body_hl` = `"A story about <em>cats</em>. <em>Cats</em> sleep a lot."`
- Request B: same plus `hl.q=fox`. Document 2 has no `body_hl` field. Document 1 is `"The <em>fox</em> ran away."` in both.

Document 2 matches `q=fox` in its title (`Fox cameo`). `cats` appears only in its body and in the boost clause.

## Reading against the code

- Implicit path (no `hl.q`, no local `q`): `HighlightAugmenterFactory.setContext` uses `context.getQuery()` (`BasicResultContext.java:48`, populated by `QueryComponent.java:190-197`). That query includes the eDisMax boost clause, which matches the measured cats highlight at weightMatches=false.
- Explicit `hl.q` and local `q` go through `parseHighlightQuery`, which the explicit and local rows show without cats. The component path uses `getHighlightQuery()`, which excludes boost clauses (`ExtendedDismaxQParser.java:703-705`), matching the component rows.
- So the observed discrepancy is real for the transformer on the implicit path at weightMatches=false. It does not match the claim that a request `q` goes through `parseHighlightQuery`; that path is only reached by `hl.q` or local `q`.

## Open point

- The weightMatches=true rows show no cats for implicit with `bq`. The reason is not established in this round. Explaining it means reading the Unified Highlighter weight-matches path; that was not done here.
- The default `hl.weightMatches` is true, so a plain request with `bq` and no `hl.weightMatches` does not show the cats highlight. The PR comment's example needs `hl.weightMatches=false` to reproduce.

## Not done

- Slice R (RTG) was not taken here. The assignment leaves it open for a second Linux VM.
- No PR, comment, or Jira write was made. The reply is a draft only: `reports/pr-5126-reply-with-examples-round-1.md`.

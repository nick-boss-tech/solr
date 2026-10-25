# PR 5126 query examples, round 1 (slice Q), revision 1

Supersedes the round 1 report for the weightMatches=true explanation and the component-assertion statement. Source: `lucene-highlighter-10.4.0-sources.jar` and `lucene-core-10.4.0-sources.jar` from Maven Central, unpacked and read; Solr sources at `74b09fb443f060caac3f8ec9701909cec276cfb4`. No new Gradle run was made for this revision.

Verdict: REPRODUCED, only with `hl.weightMatches=false`. With the default `hl.weightMatches=true`, the transformer does not highlight "cats" for any variant, and doc 2 gets no `body_hl` at all. Source explanation for the default-setting result is in "Why weightMatches=true differs" below.

Claim: `claims/pr-5126-query-examples-vm4-round-1.md`; done record `claims/pr-5126-query-examples-vm4-round-1-done.md`.
Assignment: `assignments/pr-5126-linux-examples-round-1.md` (not edited).
Ticket and PR: SOLR-3479, apache/solr PR 5126 (David Smiley), open, not draft, at pinned head `74b09fb443f060caac3f8ec9701909cec276cfb4` when checked on 2026-10-25. Base `bd2b2d5a02584893c5b5fe16c1a7eee89c2bb6c9`.

## Baseline and diagnostic heads

- Baseline (production source): `74b09fb443f060caac3f8ec9701909cec276cfb4`.
- Diagnostic commit: `022eb6d7811cd876afde3b84115696cb1c6af50e`, branch `solr-3479-pr5126-query-examples-r1` on `nick-boss-tech/solr`. Author Nick Shanin, no trailers.
- One file changed: `solr/core/src/test/org/apache/solr/response/transform/TestHighlightAugmenterFactory.java`, +94 lines, 0 deletions. Production source is byte-identical to the pinned head.
- Patch: `evidence/test-patch-022eb6d781.patch`.

## Environment and runs

- Host `vm4`, Linux aarch64, JDK 21.0.12.1 at `/workspace/tools/jdk21`, Gradle 9.7.0 via the repo wrapper. The wrapper jar was bootstrapped by `gradlew` and matches the repo's `gradle-wrapper.jar.sha256`.
- Runner: `pool/bin/sweep-run.sh` under `/workspace/tools/.gradle-vm4.lock`, with the inode guard. Seed `34795126` on every leg.

| Leg | Task form | UTC start | UTC end | Exit | Result |
| --- | --- | --- | --- | --- | --- |
| 1 focused matrix | `:solr:core:cleanTest :solr:core:test --tests ...testReview5126QueryExamples` | 16:14:01 | 16:20:31 | 0 | 1 test, 0 failures, 0 errors |
| 2 regression | `... --tests ...testReview5126ImplicitBoostDiffersFromExplicit` | 16:21:44 | 16:23:19 | 0 | 1 test, 0 failures, 0 errors |
| 3 tidy and compile | `tidy :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true` | 16:24:43 | 16:30:54 | 0 | BUILD SUCCESSFUL |
| 4 whole class | `:solr:core:cleanTest :solr:core:test --tests ...TestHighlightAugmenterFactory` | 16:32:23 | 16:36:49 | 0 | 10 tests, 0 failures, 0 errors, 0 skipped |

Leg 1 ran before the regression method was added and before tidy. Leg 4 is the final code. Raw XML: `evidence/focused-matrix-TEST.xml`, `evidence/regression-TEST.xml`, `evidence/full-class-TEST.xml`. Run records: `evidence/run-records.txt`.

## Harness

- `testReview5126QueryExamples` runs the 24 requests: weightMatches (true, false) by `bq` (absent, `bq=body_t:cats`) by component (`hl=false`, or `hl=true` with `hl.method=unified`, `hl.fl=body_t`) by variant (implicit; `hl.q=fox`; local `q=fox` in `fl`). It prints each request and full response before asserting.
- Assertions: two documents in order 1, 2; document 1 has `<em>fox</em>`; document 2 inline `body_hl` has no `<em>cats</em>` in every row except implicit with `bq`, which is not asserted.
- The component-highlighting assertion is not in the committed test. See "Component assertion" below.
- `testReview5126ImplicitBoostDiffersFromExplicit` (regression leg) uses `weightMatches=false` and `bq=body_t:cats`. It asserts that the implicit document 2 `body_hl` contains `<em>cats</em>` and that the `hl.q=fox` version does not. It passes on unchanged production. It documents the difference; it is not a fix.

## Fixture

Default configset (`ExternalPaths.DEFAULT_CONFIGSET`):

| id | title_t | body_t |
| --- | --- | --- |
| 1 | Fox story | The quick brown fox jumps over the lazy dog. Nothing else here. The fox ran away. |
| 2 | Fox cameo | A story about cats. Cats sleep a lot. |

`*_t` is `text_general` (`managed-schema.xml:150`, type at line 299): standard tokenizer, lowercasing, multiValued false.

## Matrix (24 rows)

Document 1 has `<em>fox</em>` in all 24 rows.

Document 2 inline `body_hl` (the transformer output) contains `<em>cats</em>` only in the two implicit rows with `bq` and `weightMatches=false`:

| weightMatches | bq | hl (component) | implicit | explicit (`hl.q=fox`) | local (`q=fox` in fl) |
| --- | --- | --- | --- | --- | --- |
| true | absent | false | no `body_hl` | no `body_hl` | no `body_hl` |
| true | absent | true | no `body_hl` | no `body_hl` | no `body_hl` |
| true | `body_t:cats` | false | no `body_hl` | no `body_hl` | no `body_hl` |
| true | `body_t:cats` | true | no `body_hl` | no `body_hl` | no `body_hl` |
| false | absent | false | no `body_hl` | no `body_hl` | no `body_hl` |
| false | absent | true | no `body_hl` | no `body_hl` | no `body_hl` |
| false | `body_t:cats` | false | **cats** | no `body_hl` | no `body_hl` |
| false | `body_t:cats` | true | **cats** | no `body_hl` | no `body_hl` |

The `highlighting` section (component output, present only when `hl=true`) has no `cats` in any of the 12 component rows. Document 2 is `{}` in all of them.

## Inline versus `highlighting`: captured JSON

Both come from the same response. Inline is `response.docs[i].body_hl`, written by `HighlightAugmenterFactory` (the transformer). `highlighting` is the separate section written by the highlight component. Excerpts from `evidence/focused-matrix-TEST.xml`:

- weightMatches=false, implicit, bq=body_t:cats, hl=false (no component):
  `{ "id":"2", "body_hl":"A story about <em>cats</em>. <em>Cats</em> sleep a lot."}`; no `highlighting` key.
- weightMatches=false, implicit, bq=body_t:cats, hl=true:
  inline `{ "id":"2", "body_hl":"A story about <em>cats</em>. <em>Cats</em> sleep a lot."}`; `"highlighting":{ "1":{ "body_t":["The <em>fox</em> ran away."]}, "2":{}}`.
- weightMatches=true, implicit, bq=body_t:cats, hl=true:
  inline `{ "id":"2"}` (no `body_hl`); `"highlighting":{ "1":{ "body_t":["The <em>fox</em> ran away."]}, "2":{}}`.

So at weightMatches=false the transformer's inline output contains cats while the component's `highlighting` does not. That matches the source: the component uses `parser.getHighlightQuery()`, which excludes boost clauses (`ExtendedDismaxQParser.java:703-705`), and the transformer uses `context.getQuery()` on the implicit path (`HighlightAugmenterFactory.java:180`), which includes them.

## Component assertion

The earlier report said the component-highlighting `cats` assertion was removed "because asserting it would stop the loop on the disputed case." That was wrong. The captured `highlighting` section is `{}` for document 2 in all 12 component rows, so the assertion would have passed. It was removed to follow the assignment's instruction to record component output as a separate comparison. The committed test has no such assertion; the result above is an observation from the captured JSON, not a test assertion. Restoring it is a one-line change that would need a rerun; that has not been done.

## Why weightMatches=true differs (source)

Default: `hl.weightMatches` defaults to true (`UnifiedSolrHighlighter.java:472`). The `WEIGHT_MATCHES` flag is added only when phrases and multi-term are also on, which are default true (`UnifiedSolrHighlighter.java:472-475`).

With `WEIGHT_MATCHES`, Lucene's field strategy takes offsets only from `Weight.matches` (`lucene-highlighter 10.4.0`, `FieldOffsetStrategy.java:72-75`). The classic extracted-terms path is not used for offsets. Lucene's `extractTerms` (`UnifiedHighlighter.java:481`) would have found `body_t:cats` from the boost clause, but only the classic path uses it.

The Weight is built on a `FilterLeafReader` that remaps every field the field matcher accepts onto the highlighted field's postings (`FieldOffsetStrategy.java:117-126`: `terms(field)` returns `terms(components.field())` when `fieldMatcher().test(field)`). Solr's default matcher accepts every field (`UnifiedSolrHighlighter.java:259` and `:502`, since `hl.requireFieldMatch` defaults to false). For `body_t`, that means `title_t` reads `body_t`'s postings.

The edismax query is `+(body_t:fox | title_t:fox) bq:(body_t:cats)` with the user query as a required MUST clause (`ExtendedDismaxQParser.java:175`) and the boost query as SHOULD (`:183`, `:189`).

For document 2:
1. `title_t:fox` now reads `body_t`, which has no `fox` in document 2, so the disjunction fails.
2. The user query is required, so `BooleanWeight.matches` returns null (`BooleanWeight.java:137-140`). The SHOULD clause `body_t:cats` never gets a chance to contribute.
3. `FieldOffsetStrategy` gets no matches for document 2 and emits no `body_hl`.

Document 1 is unaffected, since `body_t:fox` matches its body.

This explains every weightMatches=true row. It also explains the `highlighting` section: the component uses the same flags and the same remap, so document 2 is `{}` there as well.

I did not run a separate experiment for this chain. A cheap check, if wanted: `q=fox` with `qf=title_t` only, at weightMatches=true, should give document 2 no highlight even though its title is "Fox cameo". At weightMatches=false it should give `<em>Fox</em>`. That is a prediction from the source, not a measured result.

## Smallest reproducing pair (weightMatches=false, document 2 inline `body_hl`)

Shared: `q=fox&defType=edismax&qf=body_t title_t&bq=body_t:cats&sort=id asc&rows=10&fl=id,body_hl:[hl f=body_t]&hl.method=unified&hl.defaultSummary=false&hl.tag.pre=<em>&hl.tag.post=</em>&hl.weightMatches=false`

- Implicit: `"A story about <em>cats</em>. <em>Cats</em> sleep a lot."`
- With `hl.q=fox` added: no `body_hl` field.

Document 2 matches `q=fox` in its title. `cats` appears only in its body and in the boost clause.

## Not done

- Slice R (RTG) was not taken here. It is optional and does not bear on this discrepancy.
- No PR, comment, or Jira write was made. The reply is a draft only: `reports/pr-5126-reply-with-examples-round-1.md`.

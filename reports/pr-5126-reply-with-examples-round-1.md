# PR 5126 reply with examples, round 1 (draft, not posted)

Status: DRAFT for Nick Shanin's review. Not posted. Posting needs Nick's explicit approval.
Source: `reports/pr-5126-query-examples-round-1.md`, measured at pinned head `74b09fb443f060caac3f8ec9701909cec276cfb4`.

## Draft text

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

@dsmiley, my original note compressed too much. I ran the examples on the pinned head with a test-only overlay. The production source is unchanged.

What reproduced: with `hl.weightMatches=false`, an implicit query with `bq=body_t:cats` highlights "cats" in document 2 through the highlight transformer. Adding `hl.q=fox`, or local `q=fox` in `fl`, removes it. Document 2 matches `q=fox` only in its title ("Fox cameo"), and "cats" appears only in its body and in the boost clause.

What did not reproduce: with the default `hl.weightMatches=true`, no variant highlights "cats", with or without `bq`. The component path (`hl=true`, `hl.method=unified`) never highlights "cats" in any of the 24 rows. I have not yet explained the `weightMatches=true` result.

Smallest pair (`weightMatches=false`, document 2 `body_hl`):

- Implicit (`q=fox&defType=edismax&qf=body_t title_t&bq=body_t:cats`): `"A story about <em>cats</em>. <em>Cats</em> sleep a lot."`
- With `hl.q=fox` added: no `body_hl` field returned.

So the static prediction holds for the transformer's implicit path, but only with `weightMatches=false`. Evidence: the test-only commit `022eb6d781` on `nick-boss-tech/solr` branch `solr-3479-pr5126-query-examples-r1` (https://github.com/nick-boss-tech/solr/commit/022eb6d7811cd876afde3b84115696cb1c6af50e) and the 24-row matrix in https://github.com/nick-boss-tech/solr/blob/coordination-ui/reports/pr-5126-query-examples-round-1.md.

Our reading of the code is that the request `q` does not reach `parseHighlightQuery`; only `hl.q` and local `q` do. The measurements above fit that reading at `weightMatches=false`. Does it match what you expected?

## Notes for Nick (not part of the comment)

- The disclosure header and the opening are per `comment-formula.md`, GitHub form.
- The draft does not state an RTG finding; slice R is not run.
- The draft names @dsmiley, acknowledges the compressed explanation, states what ran, and includes the smallest request/response pair. It does not claim a fix and does not ask for review labor.
- If Nick prefers to hold the "weightMatches=true" point until explained, delete the paragraph that mentions it.

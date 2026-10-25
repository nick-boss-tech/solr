# PR 5126 reply with examples, round 1 (draft, not posted)

Status: DRAFT for Nick Shanin's review. Not posted. Posting needs Nick's explicit approval.
Source: `reports/pr-5126-query-examples-round-1.md`, measured at pinned head `74b09fb443f060caac3f8ec9701909cec276cfb4`.

## Draft text

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

@dsmiley, my first note compressed too much, so here is what ran. This is a test-only overlay on the pinned head; the production source is unchanged.

With `hl.weightMatches=false`, this request highlights "cats" in document 2 through the highlight transformer:

```
q=fox&defType=edismax&qf=body_t title_t&bq=body_t:cats&sort=id asc&rows=10
fl=id,body_hl:[hl f=body_t]&hl.method=unified&hl.weightMatches=false
hl.defaultSummary=false&hl.tag.pre=<em>&hl.tag.post=</em>

response.docs[1] (id 2): "body_hl": "A story about <em>cats</em>. <em>Cats</em> sleep a lot."
```

Adding `hl.q=fox` to the same request returns no `body_hl` for document 2. Document 2 matches `fox` only in its title ("Fox cameo"), so the body highlight of "cats" comes from the boost clause.

This does not reproduce with the default, `hl.weightMatches=true`. With the default, no variant highlights "cats", and document 2 gets no `body_hl` at all. The Unified Highlighter's weight-matches path reads the title field from the body field's postings, so the title match for `fox` fails and the boost clause never contributes.

The component's separate `highlighting` section (`hl=true`, `hl.method=unified`) does not highlight "cats" in any of the 12 runs with the component enabled, including with `bq`. That is consistent with the component using the parser's highlight query, which excludes boost clauses.

Evidence: commit `022eb6d7811cd876afde3b84115696cb1c6af50e` on https://github.com/nick-boss-tech/solr/tree/solr-3479-pr5126-query-examples-r1, and the report with the 24-row matrix and raw responses at https://github.com/nick-boss-tech/solr/blob/coordination-ui/reports/pr-5126-query-examples-round-1.md.

Does that match what you expected from the `parseHighlightQuery` path? Our reading is that only `hl.q` and a local `q` reach it, and the implicit query goes through the full query instead.

## Notes for Nick (not part of the comment)

- The default-setting caveat is the reviewer's requirement. Keep it in the comment; without it, the example overstates the issue.
- The component paragraph is stated from the captured `highlighting` section (document 2 is `{}` in all 12 component rows). It does not claim the transformer and the component agree.
- The draft names @dsmiley, acknowledges the compressed first note, states only what ran, and includes the precise request and response. It does not claim a fix and does not ask for review labor.
- Not in the draft: the RTG question, and any claim that the weightMatches=true result is a bug.

# Pool assignment: SOLR-11129 draft Limits touch-up, round 1

Capability tags: `review`. Intended host: windows. Staffing: one subagent. Claim: this round runs under the standing coordinator claim, claims/coordinator-review-rounds.md.

## Purpose

The SOLR-11129 offset fix is gated green at head dc82676b8096ed85cb018d4e5b80f6855e5d26d2 (receipts/SOLR-11129.md; job record gates/SOLR-11129-offset-fix.md). The draft, pr-drafts/search-components/SOLR-11129.md, was updated for the new head, but two statements in its Limits section predate the fix and are now inaccurate. Under the owner's patch-loop directive of 2026-10-12, the review side writes the corrections and applies them directly to the draft in this round, instead of sending the edit back to the main side.

## The two corrections

Read the draft's Limits section and receipts/SOLR-11129.md first. At the posting tip the Limits section opens:

> **Only facet.mincount has a test. The other local params are read but not tested.**
>
> - facet.limit, facet.sort, facet.prefix, facet.missing, facet.zeros, and the overrequest params have no test in this change.
> - A local facet.offset is still not honored at the coordinator. The shards apply it, so a distributed request with a local offset can return the wrong page. Not changed here.
> - The changelog names facet.mincount only.

1. The summary line and the first bullet predate the offset fix. The change now carries a test for facet.offset as well: DistributedFacetLocalParamsOffsetTest, which fails at the previous head and passes at the gated head (receipts/SOLR-11129.md). Correct the summary so it no longer says only facet.mincount has a test, and correct the untested-params statement so facet.offset is not counted among the untested params. The remaining untested list (facet.limit, facet.sort, facet.prefix, facet.missing, facet.zeros, and the overrequest params) stands as written.

2. The offset bullet ends "Not changed here", which is no longer true. The double application is fixed at the gated head: the coordinator now reads the offset from the request params, so a local offset is applied once, by the shards. The bullet's other clauses stand and stay: the coordinator still does not honor a local offset itself, and because the shards apply the offset per shard, a distributed page can still differ from a single-node page. Rewrite the bullet so it states the fixed behavior and keeps those two standing points, and drop the "Not changed here" ending.

## Rules for the edit

- Plain language in the draft's own voice, consistent with the rest of the draft. No internal process vocabulary, no "we" for the owner plus agent, no em dashes.
- Change only the summary line, the first bullet, and the offset bullet. Every other part of the draft stays untouched, including the Proof section, the changelog line and link, and the AI header and footer.
- Keep the section shape: the summary line stays a bold one-line summary that the bullets then support.
- Apply the corrections directly to pr-drafts/search-components/SOLR-11129.md and commit on pr-prepare under the standing claim, in the usual push discipline.
- After applying, re-read the corrected Limits against receipts/SOLR-11129.md once, as a fresh check, before writing the report.

## Deliverable

reports/solr-11129-draft-limits-touchup-round-1.md, stating:

- For each of the two corrections, the before text and the after text, quoted exactly.
- A verdict line: APPLIED AND VERIFIED, or NOT APPLIED with the reason.
- The commit that carries the draft edit.

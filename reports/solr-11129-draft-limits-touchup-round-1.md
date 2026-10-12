# SOLR-11129 draft Limits touch-up, round 1

Verdict: **APPLIED AND VERIFIED.** Both corrections are in `pr-drafts/search-components/SOLR-11129.md`, and a fresh read against the receipt confirms them. Nothing else in the draft changed.

Scope: `assignments/pool-solr-11129-draft-limits-touchup-round-1.md`. Claim: `claims/pool-solr-11129-limits-touchup.md`. The fork tip `solr-11129-submit` is `dc82676b8096`, the gated head the assignment names. The gated head's code was checked directly: `FacetComponent.java` line 1406 reads the offset from `rb.req.getParams()`, and the comment at line 1404 says a local `facet.offset` is applied by the shards. One read-only subagent did the fresh check, as the assignment names.

## Correction 1: the Limits summary (line 29)

- **Before:** "**Only facet.mincount has a test. The other local params are read but not tested.**"
- **After:** "**facet.mincount and facet.offset have tests. The other local params are read but not tested.**"

The offset test is `DistributedFacetLocalParamsOffsetTest`, which the receipt records as passing at the gated head. The first Limits bullet, which lists the untested params, stands as written.

## Correction 2: the offset bullet (line 32)

- **Before:** "- A local facet.offset is still not honored at the coordinator. The shards apply it, so a distributed request with a local offset can return the wrong page. Not changed here."
- **After:** "- A local facet.offset is now applied once, by the shards. The coordinator does not honor it itself, and because each shard applies the offset to its own results, a distributed page can still differ from a single-node page."

The receipt records that the coordinator now reads the offset from the request params. It records the earlier double application, where the shards and the coordinator each applied the offset, as the reason for the fix.

## Fresh check

- The diff against the pre-edit draft changes exactly two lines, the summary and the offset bullet.
- Both lines match the replacement text word for word.
- The receipt supports both corrections.
- No em dash or en dash, and no internal process words, appear in the draft.

## Note for the main side (not a defect in the edit)

The standing point that "a distributed page can still differ from a single-node page" follows from the code design, in which each shard applies the offset to its own results. The receipt's top section does not state the residual difference at the gated head. The assignment asked to keep that point, and the draft carries it. The receipt could say it explicitly if the main side wants the Limits to rest on a record.

## Commit

The draft edit and this report are committed on pr-prepare under the standing claim, in the commit that also marks the claim DONE.

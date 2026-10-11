# Gate job: SOLR-11129 offset fix and re-gate

- Branch: solr-11129-submit. Head: 6c1356bdff706328a8b78d39782f9e94845827d4 (live tip, matches receipts/SOLR-11129.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Job type: one-line branch fix plus a comment correction, then the full hardening gate at the new head. Posted by the Group O settlement (2026-10-11): the category record (reports/search-components-1.md, owner decision 4) requires the offset double-apply fixed before the draft posts, and part f2 owner decision 1 names the one-line replacement as the recommended route, adopted under the owner's decision default. The draft side is already applied in pr-drafts/search-components/SOLR-11129.md (offset stated as a Limit, precedence bullet corrected); this job does the branch side.

## The fix (FacetComponent.java, two hunks)

1. Offset read. In `FieldFacet.fillParams`, `this.offset = params.getFieldInt(field, FacetParams.FACET_OFFSET, 0);` reads the offset from the local-first params view, so a local facet.offset is applied by the shards and again by the coordinator. Replace that read with the request-level params: `this.offset = rb.req.getParams().getFieldInt(field, FacetParams.FACET_OFFSET, 0);`, with the comment: a local facet.offset is applied by the shards, so the coordinator keeps the request value here. (Anchors are the line's content at the head above; line numbers move, per the standing rule.)
2. Precedence comment. At the `SolrParams.wrapDefaults(localParams, rb.req.getParams())` call in the `FieldFacet` constructor, the comment "local params (e.g. {!facet.mincount=1}fld) take precedence, as they do on the shards" overstates the order: a per-field request setting still wins. Replace it with: "local params (e.g. {!facet.mincount=1}fld) take precedence over plain request params, as they do on the shards; per-field request params (f.fld.facet.mincount) still win".

## Proof shape

- The premise for hunk 1 is the branch's own pre-fix head: add a focused test (extend DistributedFacetLocalParamsMinCountTest or a sibling class in the same package) that requests a local facet.offset in a distributed search and shows the offset applied once, not twice. The test must fail at the pre-fix head 6c1356bdff7 and pass at the fixed head; record both outcomes from fresh JUnit XML.
- Regression leg at the fixed head: DistributedFacetLocalParamsMinCountTest, DistributedFacetExistsSmallTest, DistributedFacetPivotSmallTest (the classes in the current receipt) all pass.
- Full hardening gate at the fixed head: changelog YAML parse (the fragment is unchanged), tidy (with the GATE INVALID post-tidy guard and gradle/libs.versions.toml restored), Error Prone compile, module check.

## On green

- Push the fixed head to the fork (plain fast-forward; no history rewrite in this job).
- Refresh receipts/SOLR-11129.md at the new head with the gate record and the proof outcomes.
- Update pr-drafts/search-components/SOLR-11129.md: head, Proof counts and links to the new head, and remove the HOLD comment.
- Mark this job DONE and the claim DONE in the same push sequence.

## Step checklist

- [ ] fix hunks applied on a worktree of the live tip
- [ ] focused offset test fails at the pre-fix head, passes at the fixed head (fresh JUnit XML both legs)
- [ ] regression classes pass at the fixed head
- [ ] hardening gate green at the fixed head (changelog parse, tidy guard, Error Prone compile, module check)
- [ ] branch pushed; receipt refreshed; draft updated and HOLD comment removed; job and claim DONE

# Skip record: SOLR-13505

- Ticket: SOLR-13505, MLT (MoreLikeThis) broken in Techproducts example.
- Verdict: close-obsolete, scoped to the Techproducts example's MLT link. The MoreLikeThis component is not removed (see gap analysis).
- Draft branch: solr-13505-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the earlier C4 reading (vm2, 2026-10-19) is replaced by this pass.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-05-31, updated 2019-08-22, no fix versions. The reporter says the "more like this" link in search results of the `/browse` handler in the Techproducts example always returns nothing unless `mlt.mintf=1&mlt.mindf=1` is added. The 2019-08-22 comment says the default min term frequency and min document frequency in MoreLikeThis are intended, and that the behavior is correct.

## Code reading

Read at the pin:

- `solr/server/solr/configsets/sample_techproducts_configs/conf/` has no `/browse` request handler and no MLT request handler or defaults. `git grep -n '/browse' 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/server` returns no matches. `git grep -n 'mintf' ... -- solr/server` returns no matches. `git grep -n 'mlt' ... -- solr/server/solr/configsets` returns no matches.
- The `/browse` page and its Velocity rendering were removed by `2364a7aded` (SOLR-14792, "Remove VelocityResponseWriter", 2020-09-03, ancestor of the pin). `git grep -l 'VelocityResponseWriter' 498a09aea97aac86c2cc7ac114113b946d249942 -- solr` returns one file, `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-9.adoc`, a release note.
- The MoreLikeThis component is still in the code: `solr/core/src/java/org/apache/solr/handler/component/MoreLikeThisComponent.java` (named "More Like This" at :492), `solr/core/src/java/org/apache/solr/handler/MoreLikeThisHandler.java`, and `solr/core/src/java/org/apache/solr/handler/admin/api/MoreLikeThisAPI.java`.

## History

- `2364a7aded` (SOLR-14792): removed the Velocity writer and with it the example `/browse` page. Ancestor of the pin.
- No commit at the pin names SOLR-13505.

## Reproduction attempts

Not run. No build or test run in this pass.

## Gap analysis

- The ticket's named code, the Techproducts `/browse` MLT link, is gone. The grep for it returns no matches under `solr/server`, and the Velocity writer it depended on is gone.
- The MoreLikeThis component is not removed. The ticket's default-threshold complaint is about that component's defaults, which the 2019 comment explains as intended. If the owner reads the ticket as being about the component's defaults rather than the example link, close-obsolete does not hold and the verdict would be keep-open. This is a recorded choice (decision ladder rung 4); the owner may overrule.

## Verdict justification

close-obsolete, scoped to the example: the code the ticket names (the example's MLT link on `/browse`) was removed by SOLR-14792, and a search of the pinned head finds no remaining `/browse` or MLT configuration in the sample configsets. The MoreLikeThis component remains and is not claimed to be obsolete.

Missing for this verdict: none for the rubric minimum (grep with empty result at the pin). The scope choice above is the open point.

## Limits

This record does not establish a reproduction. It does not check whether the component defaults in the Lucene MoreLikeThis class (an external jar at the pin) match the 2019 comment. It does not post anything to JIRA or GitHub.

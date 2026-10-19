# Skip record: SOLR-13390

- Ticket: SOLR-13390, Provide Query Elevation Component by default.
- Verdict: keep-open.
- Draft branch: solr-13390-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the queue audit (round audit-1, 2026-10-04, confirmed-skip) is replaced by this pass.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-04-10, updated 2020-02-20, no fix versions, no links. The request: make the QueryElevationComponent available out of the box on `/select`, instead of requiring it to be added and an empty elevate.xml to be created. The comments: gerlowskija (2020-02-10) supports it; hossman (2020-02-19) says the main concern is back-compat for installs that already configure the component, including under other names; janhoy (2020-02-20) raises the packages and plugin model.

## Code reading

Read at the pin:

- `solr/server/solr/configsets/_default/conf/solrconfig.xml:999` mentions the QueryElevationComponent only in a comment. No `searchComponent` or `/select` entry for it in `_default`.
- `solr/server/solr/configsets/sample_techproducts_configs/conf/solrconfig.xml:974-981` configures it explicitly (`elevator`, `config-file` elevate.xml, plus a lazy `/elevate` handler). The sample is opt-in.
- `solr/core/src/java/org/apache/solr/handler/component/QueryElevationComponent.java:107` defines the class. `git grep 'QueryElevationComponent' -- solr/core/src/java` shows no registration in the default component list (SearchHandler and SolrCore have no reference to it).

So the feature is not on by default at the pin: a `/select` in the default configset does not run it, and no Java code adds it.

## History

- No commit at the pin names SOLR-13390 (evidence tool: none).

## Reproduction attempts

Not applicable: the ticket asks for a feature, not a defect. No run in this pass.

## Gap analysis

- The gap is confirmed by reading: the default behavior is absent at the pin.
- Whether to add it is a maintainer decision. The 2019-2020 comments leave the back-compat design open: an install that already configures the component under another name would get a second instance. The rubric has no close verdict for a declined feature request.
- Not close-obsolete (the component is present and in use), not close-answered (no docs page answers a feature request), not close-fixed (no commit adds it).

## Verdict justification

keep-open: the gap is confirmed at the pin, but whether the default should change is an open maintainer question (ambiguous intent), not a defect the record can close or fix. The earlier audit reasoning, "design-heavy, not a bug", matches this reading.

Missing for a closing verdict: a maintainer ruling on whether the component should be default and how existing configurations are handled. For pr-needed, a design agreed on the mailing list.

## Limits

This record does not weigh the mailing-list discussion beyond the JIRA comments. It does not check the packages-based plugin model mentioned in the 2020-02-20 comment. It does not post anything to JIRA or GitHub.

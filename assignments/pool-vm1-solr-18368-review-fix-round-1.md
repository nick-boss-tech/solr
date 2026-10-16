# Pool assignment: SOLR-18368 review fix round 1 (name HttpSolrClient.BuilderBase in the basic-auth prose, site build at the new head)

Status: DONE (site build green at 15dd2aa74b08044cb919a079e7c87044923e9934, 2026-10-16)

- Capability tags: implementation, docs build.
- Intended host: vm1. Claimed by the vm1 implementation lane at the direction of the main agent, 2026-10-16.
- Branch: solr-18368-submit at a7ec9a1b65c05b2e359e9a186fc2938fe7b7ca7d.

## Direction

A review of the opened draft PR raised four points: (1) the prose sentence above the example drops the type name, reading "an HTTP SolrClient builder" where the pre-fix sentence named a type; (2) the PR title's "so it compiles" overstates, a main-side call; (3) the site build proof on record ran at 1acaced80b, not at the live head a7ec9a1b65; (4) the description is long for a three-line docs fix, also a main-side call. This assignment covers points 1 and 3 only. Fix shape, verified against the source by the main side:

1. In the prose sentence at page line 371 of solr/solr-ref-guide/modules/deployment-guide/pages/basic-authentication-plugin.adoc, replace "an HTTP SolrClient builder" with "an `HttpSolrClient.BuilderBase` instance", the parameter type of CloudSolrClient.Builder.withHttpClientBuilder. Change nothing else in the page. Commit as Nick Shanin, no trailers; push plainly to fork solr-18368-submit.
2. At the new head: changelog YAML parse of any branch fragment; tidy recorded as not applicable (.adoc-only); run :solr:solr-ref-guide:buildLocalSite via solr-gradle.sh under the test-queue flock; success means the build completes and the Basic Authentication Plugin page is generated.

## Outcome (2026-10-16, vm1)

DONE. Fix commit 15dd2aa74b08044cb919a079e7c87044923e9934 pushed plainly to solr-18368-submit (fast-forward from a7ec9a1b65, ls-remote verified); the diff is the one prose line and nothing else. Changelog: the branch adds no fragment (the SOLR-18368 fragment in the tree is upstream's removal entry, present at the base); parsed rc 0. Build: `:solr:solr-ref-guide:buildLocalSite` BUILD SUCCESSFUL, rc 0, log g18368-reviewfix-build2.log, run in a standalone clone of the repository at the new head, because Antora refuses a linked git worktree as a content source ("Local content source must be a git repository"); the first attempt in the linked worktree failed on that alone, before any page processing (log g18368-reviewfix-build.log). Generated page solr/solr-ref-guide/build/site/solr/latest/deployment-guide/basic-authentication-plugin.html in that clone carries the new sentence and no occurrence of the old wording. Receipt: receipts/SOLR-18368.md top section. Flagged for the main side: the Proof paragraph and one "What this change does" sentence in pr-drafts/security/SOLR-18368.md that the new head makes stale, quoted in the lane's report.

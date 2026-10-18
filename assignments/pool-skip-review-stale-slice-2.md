# Pool assignment: Verify stale tickets without a named test, slice 2 of 4

Status: CLAIMED (vm3, claims/pool-skip-review-stale-slice-2-vm3.md, 2026-10-18T07:25Z)

- Slug: `pool-skip-review-stale-slice-2`.
- Capability tags: review.
- Intended host: any pool Linux host (see hosts/pool-linux.md). Claim under `claims/` before starting.
- Posted by: main agent coordinator, 2026-10-18, at the direction of Nick.

### Items

1. **Check each ticket.** For each row, apply the Method in the Decisions table and record the verdict. Output: one row per ticket in the deliverable.
2. **Record.** Write results into the deliverable as you go, so a stop keeps partial results. Name any ticket you did not reach.

### Tickets

| Ticket | Summary | Skip note (from queue) | Verdict |
| --- | --- | --- | --- |
| SOLR-5559 | DELETE collection command doesn't works in some cases | 4.6 old-style solr.xml delete collection | |
| SOLR-5566 | NPE in NamedList when using highlighting with shards | 4.6 highlighting NPE with shards, highlight component rewritten | |
| SOLR-5579 | Leader stops processing collection-work-queue after failed collection reload | 4.5.1 overseer queue stall, old, overseer rewritten | |
| SOLR-5712 | Nullpointer while using TermVectorComponent when all the defined 'termVectors' fields of a docume... | 4.6 TermVectorComponent NPE, component reworked | |
| SOLR-5736 | Solr Suggester not working in sharding (distributed search) | 4.5 distributed suggester, no repro | |
| SOLR-5759 | increasing hl.fragsize loses part of the search term | 4.4 highlighter fragsize, old highlighter | |
| SOLR-5788 | Document update in case of error doesn't return the error message correctly | 4.6.1 error message formatting, old | |
| SOLR-5833 | OVERSEEROP aka ZkController.forceOverSeer doesn't work: old overser keeps running | old overseer forceOverseer test finding | |
| SOLR-5850 | Race condition in ConcurrentUpdateSolrServer | old CUSS race, patch discussed in 4.x | |
| SOLR-5878 | Incorrect number of rows returned in distributed search with group.format=simple | usage question: group.format=simple rows semantics | |
| SOLR-5919 | AbstractFullDistribZkTestBase: control server thinks it's part of the cloud, takes overseer role | test framework issue, invalid per discussion | |
| SOLR-5990 | SolrCloud with LotsOfCores does not come up fully | 4.7 lots of cores config question | |
| SOLR-5995 | Spurious spellcheck results | spellcheck spurious single-char results, no repro | |
| SOLR-6184 | Replication fetchLatestIndex always failed, that will occur the recovery error. | 4.6 full replication during commits, commitReserveDuration config | |
| SOLR-6255 | Misleading error message when usable questionable update syntax | update syntax error message, vague | |
| SOLR-6366 | When I use minimum match and maxCollationTries parameters together in edismax, Solr gets stuck | 4.9 edismax mm with collation hang, old | |
| SOLR-6429 | Unload core during tlog replay | 4.8.1 tlog replay unload race, old | |
| SOLR-6468 | Regression: StopFilterFactory doesn't work properly without deprecated enablePositionIncrements="... | StopFilter enablePositionIncrements removal is a Lucene decision | |
| SOLR-6525 | Investigate Overseer performance issues | investigation umbrella | |
| SOLR-6534 | Multipolygon query problem with datelineRule=ccwRect | spatial4j bug, not Solr | |
| SOLR-6561 | LBHttpSolrServer's aliveCheckExecutor is leaking connection when ResponseParser is null | 4.10 LBHttpSolrServer, class replaced | |
| SOLR-6582 | FileSystem#newInstance does not always return a new FileSystem instance as it's javadoc says it d... | no description, Hadoop FileSystem caching detail | |
| SOLR-6678 | Collection/core reload is causing a memory leak | 4.10 reload leak, cannot reproduce | |
| SOLR-6712 | Highlighting not working in solr cloud grouping query when using group.query=xxx | group.query highlighting in cloud: deep resultIds handling, not isolated | |
| SOLR-6756 | The cloud-dev scripts do not seem to work with the new example layout. | scripts, resolved | |
| SOLR-6782 | PostingsSolrHighlighter produces strange highlight results | PostingsSolrHighlighter hl.fl whitespace; highlighter reworked | |
| SOLR-6811 | TestLBHttpSolrServer.testSimple stall: 2 CloserThreads waiting for same lock? | test stall | |
| SOLR-6822 | Block Join duplicates when _root_ is different type than uniqueKey | schema misconfiguration (_root_ type must match uniqueKey), 4.10 | |
| SOLR-6828 | Pivot Facet Problem with solr.PathHierarchyTokenizerFactory | 4.10 pivot facets with path hierarchy, no repro | |
| SOLR-6836 | highlighting doesn't formatting the snippet if there are more than 5 characters before the matchi... | highlighter fragment size behavior 4.10 | |
| SOLR-6890 | SPLITSHARD fails with custom lib in solrconfig.xml | 4.10 splitshard with lib directive, old | |
| SOLR-6903 | deletes with commitWithin are not reflected in all nodes | 4.10 commitWithin deletes, old, cannot reproduce on current distributed update flow | |
| SOLR-6922 | Spatial example in Solr | example docs request | |
| SOLR-6929 | Highlighting in search - not working for special characters without spaces | C# client highlight issue, not Solr core | |
| SOLR-6934 | bin/solr -e cloud has poor failure behavior when solr has (certain types of) startup errors | script behavior superseded by bin/solr assert | |
| SOLR-7088 | bin/solr script will not start Solr if CDPATH environment variable is set | bin/solr CDPATH, script rewritten | |
| SOLR-7153 | BBoxField with dynamic fields yield error | 4.10 BBoxField dynamic field stored subfields; schema handling since changed, no repro | |
| SOLR-7192 | Poor performance of Hunspell with Czech Dictionary | Hunspell Czech dictionary performance (Lucene) | |
| SOLR-7249 | Solr engine misses null-values in "OR null" part for eDisMax parser | query syntax usage (pure negative), 4.10 | |
| SOLR-7295 | Remove uses of Query.clone() | perf cleanup umbrella (Query.clone) | |
| SOLR-7343 | IndexWriter is always used to reopen index readers | Lucene limitation | |
| SOLR-7415 | Facet Module API Improvements | parent umbrella, 5.2 | |
| SOLR-7482 | Refactor code to avoid using httpclient directly | refactor | |
| SOLR-7536 | adding fields to newly created managed-schema could sometimes cause error | 5.x managed schema in cloud, no repro | |
| SOLR-7559 | Cannot use Faceting Feature while using/mlt handler | user parameter error | |
| SOLR-7612 | CloudSolrServer.setIdField is misnamed; change to setRouteField | API rename (setIdField), cosmetic | |
| SOLR-7721 | When using group.facet=true there are duplicate facets returned | group.facet float duplicates, old 4.10 | |
| SOLR-7806 | SolrCloud to use 127.0.01 instead of localhost | localhost vs 127.0.0.1 rejected (IPv6) | |
| SOLR-7846 | QTs with $variable does not work with query parameter substitution | XML property substitution syntax limitation | |
| SOLR-7867 | implicit sharded, facet grouping problem with multivalued string field starting with digits | docValues group.facet issue on 5.x, no isolated cause | |
| SOLR-7973 | JettyWebappTest bug - exceptions at end | test noise (JettyWebappTest) | |
| SOLR-7982 | SolrCloud: collection creation: There are duplicate coreNodeName in core.properties in a same col... | 4.7 core.properties duplicates, old | |
| SOLR-8079 | Config API requesthandler command should support "last-components" and "first-components" keys | Config API feature (first/last components) | |
| SOLR-8098 | Immutable ConfigSets can still change in ZK | not a bug, tracking note | |
| SOLR-8106 | stats for long field (e.g. _version_) are not accurate | stats formatting for long, old; no clear defect | |
| SOLR-8117 | Rule-based placement issue with 'cores' tag | rules placement semantics debated; maintainer says invalid rule | |
| SOLR-8171 | Facet query filterCache usage is psychic | filterCache usage question (facet query) | |
| SOLR-8310 | Solr-5.3.1 doesn't start on CentOS Linux 5 - 64-Bit Server | 5.3.1 CentOS 5 environment | |
| SOLR-8365 | If you change LogUpdateProcessor logging from info, rather than turning off update logging, you s... | logging config UX, vague | |
| SOLR-8469 | Look into how HttpSolrCall#remoteQuery handles IOException and passing error messages back to a c... | error message improvement for proxy errors, vague | |
| SOLR-8572 | Json Facets not working correctly when request is "quite complex" | user error (reused tags), resolved | |

### Decisions

| Decision | Answer or default |
| --- | --- |
| Head | decided: the fork origin/main sha 498a09aea9 at posting time. Record the sha you read. Do not move it. |
| Method | decided: read the ticket description (anonymous JIRA REST at issues.apache.org/jira/rest/api/2/issue/KEY, read only), then check the named code path on the pinned head with `git grep` or `git show`. Cite file path and line. |
| Verdict set | decided: exactly one of: gone (cite the removal or the grep showing no hits), present (cite the code path still there), fixed (cite the commit that changed it), unclear (say what is missing). No other verdicts. |
| Unclear | default: mark unclear and state the missing piece; do not guess a verdict. |
| Gradle | decided: none. No builds or test runs in a review item. |
| Closing or commenting | decided: none. No JIRA, GitHub or email posts. |
| Scope | decided: the rows in this table, keys sorted ascending. |

### Deliverable

`reports/2026-10-18-skip-repro/C2-stale-slice-2.md`: one row per ticket with verdict and cited path or grep.

### Limits

- No PR, comment, JIRA or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.

### Partial progress (2026-10-18)

One claimed slice reviewed 11 of the 61 tickets against the pinned head. The
other 50 were not reached and remain available for another reviewer. See
`reports/2026-10-18-skip-repro/C2-stale-slice-2.md`.


# Pool assignment: Verify stale tickets without a named test, slice 3 of 4

Status: OPEN

- Slug: `pool-skip-review-stale-slice-3`.
- Capability tags: review.
- Intended host: open, any Linux host. Claim under `claims/` before starting.
- Posted by: vm5 (main agent coordinator), 2026-10-18, at the direction of Nick.

### Items

1. **Check each ticket.** For each row, apply the Method in the Decisions table and record the verdict. Output: one row per ticket in the deliverable.
2. **Record.** Write results into the deliverable as you go, so a stop keeps partial results. Name any ticket you did not reach.

### Tickets

| Ticket | Summary | Skip note (from queue) | Verdict |
| --- | --- | --- | --- |
| SOLR-8623 | JSON FacetAPI with enumField throws "Expected numeric field type" | EnumField JSON facet, old; EnumField legacy | |
| SOLR-8635 | Shards don't propagate the document update correctly | user config question (luceneMatchVersion); no bug | |
| SOLR-8681 | Json facets in range dates do not mantain the order across multiple shards | JSON range facet ordering across shards, old, cannot repro | |
| SOLR-8756 | Need 4 config "zkDigestUsername"/"zkDigestPassword"/"zkDigestReadonlyUsername"/"zkDigestReadonlyU... | feature: zk digest credentials in solr.xml | |
| SOLR-8879 | Wrong number of matches is returned when group cache limit is exceeded and some results are filte... | grouping cache with post filter, deep and 5.3.1 | |
| SOLR-8909 | Streaming Expressions should leverage streaming facets | feature: streaming facets in expressions | |
| SOLR-9042 | Logging fixes RuleBasedAuthorization's principal and permission | logging polish; permission toString present on main | |
| SOLR-9089 | Nodes appear twice in state.json | 6.0 upgrade state.json duplicates, no repro | |
| SOLR-9122 | ExactStatsCache doesn't share all stats | ExactStatsCache rework since 5.5, patch in ticket, not reproducible as stated | |
| SOLR-9173 | NullPointerException during recovery | 5.3.2 recovery NPE, no repro | |
| SOLR-9183 | Test ImplicitSnitchTest.testGetTags_with_wrong_ipv4_format_ip_returns_nothing is failing | test failure, environment specific | |
| SOLR-9206 | TermVectorComponent NPE when use group parameter with Solr Cloud | 5.3 TermVector with group in cloud; deep, component rewritten | |
| SOLR-9208 | ConcurrentModificationException on SolrCore.close() resulting in abnormal CPU consumption | no repro, custom close hooks | |
| SOLR-9213 | Join does not work on multiValued fields / single field | 5.4.1 join question, no repro | |
| SOLR-9248 | HttpSolrClient not compatible with compression option | not reproducible on 6.5/master per comment | |
| SOLR-9250 | Search breaks with EU symbol € and wildcard * | known wildcard analysis limitation (tokenizer splits at euro sign) | |
| SOLR-9312 | eDisMax operator "-" ignores parentheses | user-list question about edismax negation; no repro on main | |
| SOLR-9329 | [SOLR][5.5.1] ReplicateAfter optimize is not working | 5.5 replicateAfter optimize, replication rewritten, no repro | |
| SOLR-9352 | matchAllDocsQuery instead of WildCardQuery from lucene qp with df and * | intended behavior per linked issue | |
| SOLR-9398 | deleteByQuery blocks during an optimize | Lucene merge blocking behavior, not Solr-fixable | |
| SOLR-9468 | HTTP api endpoint /replication does not handle errors propely | replication backup error handling in old handler, rewritten as API | |
| SOLR-9492 | Request status API returns a completed status even if the collection API call failed | split shard status, cannot reproduce per later comment | |
| SOLR-9571 | Time-allowed detection logic in LBHttpSolrClient should return a fake partial results response | LBHttpSolrClient timeAllowed partial response, old class replaced | |
| SOLR-9723 | "Error writing document" on document add caused by NegativeArraySizeException | intermittent NegativeArraySizeException in Lucene indexing, no repro | |
| SOLR-9741 | Solr has a CPU% spike when indexing a batch of data | CPU spike report 5.3.2, no repro | |
| SOLR-9754 | No shell specified in the su call | init.d script su shell, 5.5.3 install script question | |
| SOLR-9769 | solr stop on a service already stopped should return exit code 0 | script exit-code policy, maintainers prefer current behavior | |
| SOLR-9791 | Collections API Async Backup / Restore Status Misleading | 6.2.1 async restore status, no repro, since reworked | |
| SOLR-9825 | Solr should not return HTTP 400 for some cases | vague http status policy | |
| SOLR-9830 | Once IndexWriter is closed due to some RunTimeException like FileSystemException, It never return... | IndexWriter closed handling, broad, no repro | |
| SOLR-9866 | Reduce memory pressure for expand component | memory optimization, inconclusive | |
| SOLR-9871 | Java-level deadlock on SolrCore.getSearcher | java deadlock report from 6.2.1, no repro | |
| SOLR-9925 | Child documents missing from replicas during parallel delete+add | child docs DBQ race, large repro scripts, deep | |
| SOLR-9942 | MoreLikeThis Performance Degraded With Filtered Query | 5.5.2 perf regression gone in 6.0 | |
| SOLR-9970 | Wrong groups order with sort and group.sort | group.sort ordering with sort, deep grouping semantics, unclear | |
| SOLR-10010 | NGramTokenizer with SynonymFilterFacory doesn't work properly when using Managed-Schema | ManagedIndexSchema synonym loading, analysis chain, unclear repro, old | |
| SOLR-10022 | solr fieldType change failed | old schema delete/change with data; no repro on main | |
| SOLR-10110 | SolrCore reload can miss commit hooks if the commit tracker tries to open a new searcher on a clo... | test fragility, old CommitTracker reload race | |
| SOLR-10242 | Cores created by Solr RESTORE end up with stale searches after indexing | no repro (autocommit config), 6.3 | |
| SOLR-10325 | If SolrCore creation fails, there should be an attempt to clean up any pre register changes to ZK. | no description; core creation cleanup too vague | |
| SOLR-10334 | API create collection times out periodically | 4.10.3 vendor issue, resolved by SOLR-8152 | |
| SOLR-10395 | NPE during spell check with result collation | custom parser/user config, no repro | |
| SOLR-10407 | v2 API introspect output: availableSubPaths should be sorted | feature/cosmetic, v2 introspect sort order | |
| SOLR-10534 | Core state doesn't get cleaned up if core initialization fails | no clear scope; core init failure cleanup, vague | |
| SOLR-10597 | Overseer election endless loop on NodeExists /overseer_elect/leader | overseer election loop, no repro, cluster state corruption | |
| SOLR-10650 | First start fails due to "Problem accessing /solr/. Reason: Not Found" | user JDK9 environment problem | |
| SOLR-10706 | SolrJ wrongfully uses URLEncoder.encode for constructing url fragments | vague, SolrJ URL encoding of params is correct form-encoding on main | |
| SOLR-10712 | UpdateRequest.setDocIterator() writes the payload in memory before sending to server | stale: docIterator path now streams | |
| SOLR-10806 | Solr Replica goes down with NumberFormatException: Invalid shift value (64) in prefixCoded bytes ... | user schema change / Lucene-level, no Solr bug | |
| SOLR-10856 | ExtendedDismaxQParser (edismax) override OR when mm=100% | expected edismax mm behavior per discussion | |
| SOLR-10896 | RawQParserPlugin doesn't work with PointFields | raw parser is intentionally a raw TermQuery; {!term} handles points via getFieldTermQuery | |
| SOLR-10917 | Port starvation issue in Luke | cluster proxy loop operational report, no concrete fix identified | |
| SOLR-10936 | ArrayIndexOutOfBoundsException: -65536 when indexing large document | AIOOBE in Lucene TermsHashPerField, not Solr code, no repro | |
| SOLR-11022 | SynonymGraphFilterFactory proximity search error | same root cause as SOLR-12532 (slop not applied to SpanNearQuery in SolrQueryParserBase); covered by branch... | |
| SOLR-11139 | FreeTextSuggester exits on first error while building suggester index | FreeTextSuggester is Lucene code, not Solr | |
| SOLR-11150 | When V2Request is sent to the wrong node, it throws NPE | no repro or description, unclear what wrong node means | |
| SOLR-11301 | Delete collection time out after 180s or create the configset time out:300s | no repro: overseer queue timeouts on a single-node setup until restart; reporter code reading only, no steps. | |
| SOLR-11405 | SQL errors out with some non-specified table/field in query | no repro: /sql error references an identifier from an earlier query on a different table (Calcite schema st... | |
| SOLR-11408 | Long response times (1 min) for initial elevate query execution | performance report (5000-entry elevate.xml initial load), no defect/repro data. | |
| SOLR-11441 | windows shell splits args on "=" so we should consider updating our docs to always quote args lik... | Windows script/doc issue (solr.cmd in PowerShell splits -Dfoo=bar); bin script, no JUnit surface; low value. | |
| SOLR-11517 | ToParentBlockJoinQuery fails when the parents/child fall in to different segments | not a Solr defect shown: block-join parent without children (known workaround: empty child marker); Lucene ... | |

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

`reports/2026-10-18-skip-repro/C3-stale-slice-3.md`: one row per ticket with verdict and cited path or grep.

### Limits

- No PR, comment, JIRA or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.


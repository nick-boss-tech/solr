# Pool assignment: Verify stale tickets without a named test, slice 4 of 4

Status: CLAIMED (claims/pool-skip-review-stale-slice-4.md, vm5, 2026-10-18T07:36:33Z)

- Slug: `pool-skip-review-stale-slice-4`.
- Capability tags: review.
- Intended host: any pool Linux host (see hosts/pool-linux.md). Claim under `claims/` before starting.
- Posted by: main agent coordinator, 2026-10-18, at the direction of Nick.

### Items

1. **Check each ticket.** For each row, apply the Method in the Decisions table and record the verdict. Output: one row per ticket in the deliverable.
2. **Record.** Write results into the deliverable as you go, so a stop keeps partial results. Name any ticket you did not reach.

### Tickets

| Ticket | Summary | Skip note (from queue) | Verdict |
| --- | --- | --- | --- |
| SOLR-11672 | NPE in FacetComponent countFacets() | not reproducible on main: FacetComponent.countFacets removes under-limit facet queries only after looping o... | |
| SOLR-11688 | Transient cache should not get initiated unless explicitly configured | improvement (do not init transient cache unless configured), not a bug; Erick Erickson raised semantics que... | |
| SOLR-11768 | NPE in QueryComponent | user/config error: schema changed to docValues without reindex so old docs return null id; same as SOLR-103... | |
| SOLR-11814 | Solr REST test cases trips assertion error but doesn't fail the test | test-infra: assertion about closing response stream under Jetty forward in REST tests; background in SOLR-8... | |
| SOLR-11816 | Rolling restarts (rf1) cause latency spike | operational/perf report (rolling-restart latency spike) with docker repro, no code-level defect identified. | |
| SOLR-11827 | MockAuthorizationPlugin should return 401 if no principal is specified | test-infra only: MockAuthorizationPlugin behavior change, no product bug. | |
| SOLR-11858 | NPE in DirectSpellChecker | no repro (reporter and Cassandra Targett could not reproduce); suspected Lucene DirectSpellChecker with cou... | |
| SOLR-12263 | Log replay throwing ClassCastException | revisited in session 3: ClassCastException reading a tlog written by 7.2.1 (Long where String expected), co... | |
| SOLR-12300 | Unpopulated SolrDocument using Custom DocTransformer | revisited in session 3: custom transformer without getExtraRequestFields shadows fields, user code issue (c... | |
| SOLR-12315 | the number of docs in each group depends on rows | revisited in session 3: not reproducible (commenter wrote a unit test that passed); no data | |
| SOLR-12460 | Filtercache getting filled up when domain switches are involved in Json facets | revisited in session 3: filterCache efficiency observation for join domains; no concrete defect or fix dire... | |
| SOLR-12781 | OverseerCollectionConfigSetProcessor reports "node with improper name: qnr-0000000002" | stale 2018 cosmetic DEBUG log noise; reporter unsure it's harmful, 0 comments, no functional bug demonstrated | |
| SOLR-12808 | Wrong highlighting using PatternReplaceCharFilterFactory | Lucene-side PatternReplaceCharFilter offset bug, not fixable in Solr repo; 2018, screenshot-only repro, no ... | |
| SOLR-12900 | NPE in OneComparatorFieldValueHitQueue.lessThan while indexing | stale: 8y, 0 comments, no repro; NPE frame is Lucene's FieldValueHitQueue (not in solr repo) | |
| SOLR-13021 | You cannot safely create and delete the same collection. | Stale 2018 ticket, empty description; committer stress tests never reproduced on master (2019), no assignee... | |
| SOLR-13094 | NPE while doing regular Facet | stale 2018 faceting NPE with no root-cause stack or repro; overlaps open SOLR-13056 SortableTextField facet... | |
| SOLR-13216 | Trouble restoring a collection | stale 2019 restore ticket: CorruptIndexException file-read error on backup mount after successful copy, no ... | |
| SOLR-13242 | RegexReplaceProcessorFactory not making accurate replacement | Reporter self-resolved (whitespace pattern issue in own config); maintainers could not reproduce; factory i... | |
| SOLR-13416 | Overseer node getting stuck | Stale 2019 overseer-hang ticket: unconfirmed hypotheses, witness retracted, reporter self-assigned with no ... | |
| SOLR-13505 | MLT (MoreLikeThis) broken in Techproducts example | stale 2019 no-bug: only comment says defaults tf=2/df=5 behave correctly; /browse handler no longer in tech... | |
| SOLR-13570 | Unable to control timeout for requests from Overseer to other nodes | stale 2019 ticket; hardcoded 120000 socketTimeoutMillis and getDefaultHttpClient() code paths no longer exi... | |
| SOLR-13591 | AlreadyClosedException in SolrJ | Stale 2019 thread; consensus points to client-side misuse (bad close()) / httpclient version, not a SolrJ b... | |
| SOLR-13627 | newly created collection can see all replicas go into recovery immediately on first document addi... | 7y stale, root cause undiagnosed; 2019 term behavior, current main has new guards (canBecomeLeader deferral... | |
| SOLR-13698 | Querying suggester with alias fails | stale 2019 alias-suggest shard-timeout report; no comments/repro; root cause unconfirmed; PR-check false po... | |
| SOLR-13781 | TestContainerReqHandler.testPackageAPI failures imply race condition between update-package and d... | Stale 2019 test-only issue; the flaky test class TestContainerReqHandler no longer exists in current main, ... | |
| SOLR-13832 | MoreLikeThis parsers SimpleMLTQParser and CloudMLTQParser - termvectors vs fieldvalues | feature proposal: new mlt param to choose termvectors vs stored values; stale since 2019, 0 comments; PR ch... | |
| SOLR-13861 | SynonymGraphFilterFactory - with pattern tokenizer - not able to start | bug is real but lives in apache/lucene (SynonymGraphFilterFactory.loadTokenizerFactory passes shared tokArg... | |
| SOLR-13933 | Cluster mode Stress test suite | Feature proposal: external stress-test harness, self-assigned to active maintainer Ishan, stale since 2021;... | |
| SOLR-13976 | Solr java.lang.ArrayIndexOutOfBoundsException: Index 1048 out of bounds for length 848 | Stale 2019 grouping AIOOBE; zero comments, no repro; crash inside Lucene GrowableWriter/Packed64SingleBlock... | |
| SOLR-14074 | Make CollapsingQParserPlugin re-entrant | stale - collapse+grouping deliberately disallowed by SOLR-13970; re-entrancy no longer wanted | |
| SOLR-14178 | Tests emitting megabytes of output | test-hygiene proposal, stale since 2020, no product bug | |
| SOLR-14288 | Search on parent-child by specific key - results returns more than the required document after so... | stale usage question: newcomer parent/child query confusion, 0 comments since 2020, no repro, no component ... | |
| SOLR-14756 | Sync of replicas are failing for two collection among 3 | Stale 2020 ops report of replica recovery failures, no repro, no comments; generic 'Replication for recover... | |
| SOLR-14966 | Solr should use cache collection state to validate _stateVer_ sent by SolrJ | Minor optimization not a bug per Smiley (2024); stale since 2020; no PR exists | |
| SOLR-15037 | config update listener reloads solr core while schema is changed | Stale: 2020-era async-reload complaint; current main reloads the core synchronously in the request thread a... | |
| SOLR-15234 | PRS has issues with many concurrent creations | Stale (2021) logging-improvement request; reporter himself mused the error may be benign - no identified bug | |
| SOLR-15237 | Distributed search with index sharding is not working with basic authentication plugin enabled | BasicAuthPlugin+standalone distributed search 401; assignee Mark Miller diagnosed fix intent - active maint... | |
| SOLR-15279 | .SolrClientNodeStateProvider Error on getting remote info, trying again | Stale 2021 report of SolrClientNodeStateProvider NPE during remote metrics fetch on 8.4; no repro, no maint... | |
| SOLR-15371 | Backups randomly fail sometimes | Stale 2021 report of random NFS backup failures; reporter's own last comment resolved it environmentally (m... | |
| SOLR-15420 | Exception thrown during intensive batch partial updating and segment merging | stale 2021 overflow-during-merge report: no repro, reporter silent 5+ yrs, root cause disputed (dawid.weiss... | |
| SOLR-15443 | Atomic Update of parent document in nested documents fails and needs solr restart | stale unconfirmed 2021 report; no repro, no comments; nested-doc merge logic reworked since | |
| SOLR-15481 | Exception when starting a node with index replication | stale - 2021 8.8 shards-whitelist crash path gone from main; allowlist now checks only non-null leaderUrl w... | |
| SOLR-15571 | Content of some fields not return in streaming expressions | Stale 2021 report, 0 comments; whole /export-streaming chain writes values verbatim on current main, no fau... | |
| SOLR-15679 | 'Too many close' - error | stale 2021 question-style report, no repro, no comments, no stack trace | |
| SOLR-15727 | Split-brain can occur with heavy ingest and violent shutdown | stale misconfiguration report: fluctuating hit counts after violent shutdown are caused by small numRecords... | |
| SOLR-15848 | Fix some BadApple tests for branch_8_11 | stale: 2021 BadApple quarantine on retired branch_8_11, fix commits landed same day; nothing to implement o... | |
| SOLR-15977 | Solr - portuguese synonyms are not working | not a bug - reporter's schema had stemmer before synonym filter; maintainer declined action | |
| SOLR-16062 | Solr 8.1 - Score for same catalog entry differs for different persons / environment | stale 2022 Solr-8.1 LTR environment scoring difference report, no comments, no repro, unverifiable | |
| SOLR-16084 | When Solr server runs with HTTPS java clients can't connect but curl can. | feature request / stale: no comments in 4+ yrs, reporter concedes misconfiguration, ticket-era Http2SolrCli... | |
| SOLR-16526 | java.lang.IllegalArgumentException: Invalid URI host: null | environment issue: underscores in SOLR_HOST hostname rejected by Java/Jetty URI parsing, not a Solr bug; st... | |
| SOLR-16548 | CaffeineCache seems to be not working in 9.0 and later | stale 2022 filter-cache stats complaint; maintainer (Jan Høydahl) said tests pass and questioned the bug; n... | |
| SOLR-16785 | Solr node is going down automatically and not scaling up. | stale 2023 ops issue, Solr 7.7.1, no stack trace/comments; not a code bug | |
| SOLR-16786 | Zookeeper connection timeout even after Solr trying to connect. Connection establishes after rest... | Stale 2023 ZK-timeout ops report on Solr 8.6, 0 comments, no repro - likely environmental, not a focused bug | |
| SOLR-17154 | solr jdbc SQL encoding error | Upstream Calcite issue CALCITE-2616 (literal charset always ISO-8859-1), not a Solr bug; workaround exists,... | |
| SOLR-17259 | Reverse the order of two ref guide steps in releaseWizard | release-workflow process proposal, not a product bug; maintainer-reported (Gus Heck), unconfirmed speculati... | |
| SOLR-17376 | Direct buffer memory leak in Solr Java Client | unconfirmed upstream Jetty pool leak; maintainer redirected to Jetty; jetty/jetty.project#12084 closed stal... | |
| SOLR-17509 | Collection Backup: Invalid status request | Duplicate root cause: async status expiry (SOLR-17160/SOLR-17462), resolved by reporter via config; stale, ... | |
| SOLR-17610 | Solr 9.7 on CentOS 7 with OpenJDK 11 has a 10-second delay for local network client access | Environment-specific DNS/security-manager troubleshooting report; no focused Solr bug, zero maintainer enga... | |
| SOLR-17747 | Not able to optimize solr 9.8.1 HNSW index | stale silent-failure HNSW optimize report, no comments, no repro; fix would be Lucene merge-policy side, no... | |
| SOLR-17765 | Nodes should publish themselves as DOWN ASAP during shutdown | maintainer improvement idea; Miller pushback (live-node timeout suffices); stale since May 2025 | |
| SOLR-17920 | Solr seems to hold disk space while running, but it is released if it is stopped | stale diagnostic ticket: disk space held while running, released on stop; 0 comments, no repro, no assignee | |

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

`reports/2026-10-18-skip-repro/C4-stale-slice-4.md`: one row per ticket with verdict and cited path or grep.

### Limits

- No PR, comment, JIRA or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.

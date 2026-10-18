# Pool assignment: Verify stale tickets without a named test, slice 1 of 4

Status: OPEN

- Slug: `pool-skip-review-stale-slice-1`.
- Capability tags: review.
- Intended host: any pool Linux host (see hosts/pool-linux.md). Claim under `claims/` before starting.
- Posted by: main agent coordinator, 2026-10-18, at the direction of Nick.

### Items

1. **Check each ticket.** For each row, apply the Method in the Decisions table and record the verdict. Output: one row per ticket in the deliverable.
2. **Record.** Write results into the deliverable as you go, so a stop keeps partial results. Name any ticket you did not reach.

### Tickets

| Ticket | Summary | Skip note (from queue) | Verdict |
| --- | --- | --- | --- |
| SOLR-1247 | StatsComponent uses top-level FieldCache | 1.4 stats top-level FieldCache, stats rewritten on docValues | |
| SOLR-1542 | Optimized indexes are not replicated on repeaters with only replicateAfter=optimize | 1.4 replication of repeaters, old | |
| SOLR-1780 | existence of exactly one value for uniqueKey field is not checked when overwrite=false or allowDu... | uniqueKey single value check with overwrite=false, debatable | |
| SOLR-1785 | Handle +/-Inf, NaN when scoring | 1.4 NaN/Inf scoring, old | |
| SOLR-2274 | SolrCore load failure can leave newly created index locked | 2010 core load failure leaves index locked, old, locking rewritten | |
| SOLR-2435 | Duplicate Jars in (binary) release packages | release packaging, old | |
| SOLR-2507 | Parsing issue with double quotes in WildCardQuery | 4.0 nightly parse issue, old | |
| SOLR-2751 | TermsComponent terms.regex and terms.upper does not always work | 3.3 TermsComponent regex/upper, old | |
| SOLR-2845 | Adding extra highlighting term to a synonym | 3.4 highlighting synonyms, old | |
| SOLR-2913 | Interrupted Exception when closing a SolrCore | 3.4 interrupt on close, old | |
| SOLR-2936 | Error Logs of core.SolrCore's finalize | finalize refcount log, old | |
| SOLR-3023 | IndexOutOfBoundsException after disk full on replication | 3.5 disk full replication | |
| SOLR-3027 | At least some Solr plugins are instantiated twice | plugin double instantiation, 4.0-ALPHA, old | |
| SOLR-3034 | replicateAfter optimize not working | replication config usage, 4.0-ALPHA | |
| SOLR-3050 | highlighting not working with int and float data types | old highlighter numeric fields | |
| SOLR-3380 | Improve java client(s) test coverage | test coverage umbrella | |
| SOLR-3408 | org.apache.lucene.store.AlreadyClosedException: MMapIndexInput already closed: MMapIndexInput(pat... | old MMap closed exception, no repro | |
| SOLR-3452 | Binary not returning properly when searching across shards. | 3.6 binary fields across shards, old | |
| SOLR-3523 | ReturnFields.getLuceneFieldNames inconsistently includes non-field names | ReturnFields internals, reworked since | |
| SOLR-3576 | Setup latency orientated defaults and clarify HttpShardHandlerFactory configuration | defaults tuning 4.0 | |
| SOLR-3586 | spellchecker directory/files not deleted | 3.6 spellcheck dir deletion on unload, old | |
| SOLR-3605 | Problem with group paging (or JavaBinCodec conversion error for the request) | 3.6 group paging javabin, old | |
| SOLR-3692 | Error when highlighting document | 3.6 highlighter token limit, config | |
| SOLR-3693 | SimpleFacets leak threads | old SimpleFacets static executor, rewritten | |
| SOLR-3694 | Thread leak in SolrCmdDistributor (thread pool not closed). | old SolrCmdDistributor leak, rewritten | |
| SOLR-3697 | ConcurrentUpdateSolrServer's scheduler thread pool is not shut down properly | thread leak in test shutdown, old class | |
| SOLR-3716 | Make SolrResourceLoaders ClassLoader available as context class loader | classloader context request, old | |
| SOLR-3721 | Multiple concurrent recoveries of same shard? | recovery concurrency, resolved by Mark Miller | |
| SOLR-3739 | ExtendedDismaxQParser (edismax) does not obey q.op for tokens split by an analyzer | edismax q.op semantics, later work | |
| SOLR-3740 | ExtendedDismaxQParser (edismax) does not obey q.op for parenthesized sub-queries | edismax q.op semantics, later work | |
| SOLR-3741 | ExtendedDismaxQParser (edismax) does not obey q.op for queries with operators | edismax q.op semantics, later work | |
| SOLR-3785 | Cluster-state inconsistent | 4.0 cluster state, rewritten | |
| SOLR-3798 | copyField logic in LukeRequestHandler is primitive, doesn't work well with dynamicFields | Luke copyField logic, reworked (SOLR-4511 etc.) | |
| SOLR-3806 | solrcloud node addresses | hostname environment issue | |
| SOLR-3905 | EmbeddedSolrServer throws NPE when terms contain slash | slash is query syntax now (regex), invalid per maintainers | |
| SOLR-3919 | Problem with hl.mergeContinuous - snippet is too long but only one hit | 4.0 highlighter mergeContinuous, old | |
| SOLR-3991 | SOLR stuck on initialization with warmup and spellcheck collation on for /select handler | 4.0 warmup/spellcheck deadlock, old | |
| SOLR-4006 | Many tests on Apache Jenkins are failing with lingering threads. | test infra lingering threads | |
| SOLR-4122 | EnglishMinimalStemmer incorrectly tokenizes words ending in "hes" and "xes" | stemmer is Lucene EnglishMinimalStemmer | |
| SOLR-4277 | Spellchecker sometimes falsely reports a spelling error and correction | 4.0 spellcheck false positives, usage | |
| SOLR-4304 | Coupling between SpellCheckComponent(s) and QueryConverter(s) is a mess and makes no sense | 4.0 queryConverter/spellcheck coupling, rewritten | |
| SOLR-4338 | Atomic Updates on multi-valued fields giving unexpected results (again) | 4.1 atomic add multiple ops, old; atomic update merge rewritten | |
| SOLR-4344 | Can not access FileItemContentStream in other package | visibility change request, old | |
| SOLR-4414 | MoreLikeThis on a shard finds no interesting terms if the document queried is not in that shard | distributed MLT, handled by shards.qt, later SOLR-5480 | |
| SOLR-4423 | NPE in SolrParams.toSolrParams when an invalid datatype is used in an XML named list in solrconfi... | NamedList datatype validation old, 4.0 | |
| SOLR-4427 | Solr should complain with a readable warning if more than one of a plugin type occurs in solrconf... | warning for duplicate plugin types, validation feature | |
| SOLR-4499 | StatsComponent could use some serious TLC | umbrella TLC for stats component | |
| SOLR-4501 | MoreLikeThisComponent is misusing the mlt.count parameter | 4.1 mlt.count misuse, MLT component reworked | |
| SOLR-4533 | Synonyms, created in custom filters are ignored after tokenizers. | 4.1 custom filter synonyms with q.op=AND, no test | |
| SOLR-4635 | shardHandlerFactory loading in solr.xml hides class loading exceptions | solr.xml shardHandlerFactory loading error message, solr.xml parsing rewritten | |
| SOLR-4659 | SolrResourceLoader should always get parent classloader explicitly (no "default" behavior) | classloader refactor | |
| SOLR-4686 | HTMLStripCharFilter and Highlighter generates invalid HTML | HTMLStripCharFilter is Lucene, highlight limitation | |
| SOLR-4733 | Rollback does not work correctly with tlog and optimistic concurrency updates | rollback with tlog and optimistic concurrency, known limitation (SOLR-4895) | |
| SOLR-4739 | May be lost update when creating or deleting Alias | 4.x alias update race; aliases reworked | |
| SOLR-4851 | Highlighter duplicates numeric token in snippet when term vectors/positions/offsets on | 3.6 highlighter numeric token duplicate | |
| SOLR-5046 | IllegalArgumentException using distributed group.query when one shard does not match any docs | group.query distributed, deep, 4.3 | |
| SOLR-5145 | wait w/o notify situation happening on shutdown in CoreContainer/CloserThread/SolrCores.modifyLock | old CloserThread stall, code replaced | |
| SOLR-5252 | SolrCloud core UNLOAD + CREATE | 4.x core unload/create semantics, reworked | |
| SOLR-5376 | if I ask for facet.method=fcs but the field is multiValued, I should get an error back | fcs error on multivalued, minor UX | |
| SOLR-5435 | An edismax query wrapped in parentheses parsed wrong | 4.3.1 edismax parentheses, no repro | |
| SOLR-5508 | AbstractFullDistribZkTestBase gives false positive if collection fails to init | test harness false positive | |

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

`reports/2026-10-18-skip-repro/C1-stale-slice-1.md`: one row per ticket with verdict and cited path or grep.

### Limits

- No PR, comment, JIRA or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.


# Pool assignment: Verify already-committed and support-question tickets

Status: DONE 2026-10-18T05:20Z (vm2, claims/pool-skip-review-committed-support-round-1.md; deliverable reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, 37 of 37 rows)

- Slug: `pool-skip-review-committed-support-round-1`.
- Capability tags: review.
- Intended host: any pool Linux host (see hosts/pool-linux.md). Claim under `claims/` before starting.
- Posted by: main agent coordinator, 2026-10-18, at the direction of Nick.

### Items

1. **Check each ticket.** For each row, apply the Method in the Decisions table and record the verdict. Output: one row per ticket in the deliverable.
2. **Record.** Write results into the deliverable as you go, so a stop keeps partial results. Name any ticket you did not reach.

### Tickets

| Ticket | Summary | Skip note (from queue) | Verdict |
| --- | --- | --- | --- |
| SOLR-7352 | Synchronize CloudSolrStream EOF Tuple Map | already committed | |
| SOLR-8899 | MergeStrategy code creates HttpClient(s) and HttoSolrClient(s) that it does not close | already committed | |
| SOLR-10601 | StreamExpressionParser should handle white space around = in named parameters | already committed | |
| SOLR-11269 | Node gone after reconnecting to ZK | already addressed on main: ZkController catches NodeExistsException when re-creating the ephemeral live node. | |
| SOLR-11550 | 'addChildDocument' does not maintain the atomicity | already addressed by SOLR-10114 (shared _version_ for parent/children); reporter's repro not reproducible; ... | |
| SOLR-11864 | Create Collection API allows creating collection with trailing space | already handled on main: collection names go through SolrIdentifierValidator.validateCollectionName (Create... | |
| SOLR-12236 | Admin UI logging page: "Core" column never gets populated | revisited in session 3: Admin UI log Core column; Log4j2Watcher.toSolrDocument already copies the core MDC ... | |
| SOLR-13278 | Using multiple dictionaries with Solr Suggester | stale 2019 support question, ill-defined; multi-dict querying already works on main | |
| SOLR-13572 | NullPointerException leads to msg=null. | stale 2019 NPE-report; no repro, reporter never answered Jan's 8.x retest; NPE path already null-guarded in... | |
| SOLR-13616 | Possible racecondition/deadlock between collection DELETE and PrepRecovery ? (TestPolicyCloud fai... | design-heavy, reporter is active maintainer (Hostetter) with open design questions, 7yr stale, main already... | |
| SOLR-15801 | Ref Guide docs on Java support out of date | documentation-only (system_requirements.adoc Java matrix), stale - 2021 text already rewritten in current g... | |
| SOLR-15970 | Release Wizard must clean up staging artifacts after a failed RC | already addressed on main (abort flows already svn-rm staging artifacts since 2022-01-14), 0 comments since... | |
| SOLR-16077 | Solr basic authentication is a bit strange | Not a user-facing bug per Jan Høydahl; double-SHA256 hashing is a deliberate per-request scheme, changing i... | |
| SOLR-17224 | Getting NPE in apache SOLR 8.11.2 while doing atomic update using add-distinct from java based ap... | duplicate of SOLR-17721; doAddDistinct NPE null-check already on main (a1ebd0433c7) | |
| SOLR-17820 | Shard and Collection health is GREEN with down replicas | Not reproducible on current main: calcShardHealth already yields YELLOW for 1-down-of-5 with leader; logic ... | |
| SOLR-3742 | group.func and group.facet do not work together | group.func with group.facet unsupported | |
| SOLR-5772 | duplicate documents between solr "block join" documents and "normal" document | mixing block join and normal docs unsupported, per maintainers | |
| SOLR-5845 | File locked after indexing | 4.6 file lock after indexing, support | |
| SOLR-6334 | Solr node(cores) go into recovering state and peersync failed | 4.7 recovery, support | |
| SOLR-6588 | Combination of nested documents and incremental partial update on int field does not work | nested docs with atomic update unsupported combination | |
| SOLR-7363 | Expand component throws an Exception when the results have been collapsed and grouped | expand with grouping unsupported combination, feature | |
| SOLR-7526 | When replacing index contents and reloading the core, index is deleted | 5.1 replace index files then RELOAD, unsupported workflow | |
| SOLR-7947 | SolrCloud: after a solr node restarted, all cores in the node are down in /clusterstate.json due ... | 4.7 overlapping file lock, support | |
| SOLR-9713 | TLS-SSL Mutual-Auth doesn't work, Unable to load keyStore with given password | keystore password handling on Windows script, 6.2.1 support | |
| SOLR-10096 | Parallel SQL not returning the correct results for count distinct aggregation | feature: count distinct unsupported in old SQL handler | |
| SOLR-10974 | Replication - Unable to download tlog | support question, no repro (user list) | |
| SOLR-11175 | FuzzyLookupFactory with DocumentDictionaryFactory returns only one result for document | vague suggester config/support question, no clear defect | |
| SOLR-11290 | replace expression doesn't allow strings | not reproducible by reading main: ReplaceWithValueOperation supports string originals/values and documents ... | |
| SOLR-11764 | preanalyzed field with highlight option throws exception | support question solved in-ticket (storeOffsetsWithPositions for PreAnalyzedField highlighting); not a bug. | |
| SOLR-13167 | Duplicate Child Documents and undeterministic search | stale 2019 nested-child-docs support ticket: no comments, no repro, no assignee; likely pipeline re-ingesti... | |
| SOLR-13825 | AccessDeniedException \server\solr\custom_master_index\data\index\_jh7m_Lucene50_0.pos | Stale 2019 Windows/Azure support question: AccessDeniedException during Lucene FSDirectory fsync is OS file... | |
| SOLR-14083 | Solr GUI access through Internet Explorer | stale 2019 IE11-only Admin UI login issue; Jan Høydahl explicitly declined IE support; IE EOL, no repro on ... | |
| SOLR-15226 | org.apache.lucene.index.IndexNotFoundException: no segments* file found in LockValidatingDirector... | stale 2021 support question (no segments* file after network failure), zero comments, code path gone from m... | |
| SOLR-15408 | ConcurrentUpdate issue of solr 8.8.1 | stale support-style report vs Solr 8.8.1 (2021), zero comments, no repro, no defect claim; ConcurrentUpdate... | |
| SOLR-16306 | Faceting over a DateRangeField gives empty result | feature request: facet.field support for DateRangeField never existed; stale since 2022, 0 comments, no PR | |
| SOLR-16609 | Unable to set properties when creating config/collection in Solr Cloud environment | Feature/support request: wants SolrJ property-setting on collection/configset create; only comment (dsmiley... | |
| SOLR-17751 | SolrException: Unable to read response info for shard | Stale no-repro support-style report: one-hour burst of shard-response merge failures on 9.6.1, no comments ... | |

### Decisions

| Decision | Answer or default |
| --- | --- |
| Head | decided: the fork origin/main sha 498a09aea9 at posting time. Record the sha you read. Do not move it. |
| Method | decided: read the ticket description (anonymous JIRA REST at issues.apache.org/jira/rest/api/2/issue/KEY, read only), then check the named code path on the pinned head with `git grep` or `git show`. Cite file path and line. |
| Verdict set | decided: exactly one of: gone (cite the removal or the grep showing no hits), present (cite the code path still there), fixed (cite the commit that changed it), unclear (say what is missing). No other verdicts. |
| Unclear | default: mark unclear and state the missing piece; do not guess a verdict. |
| Gradle | decided: none. No builds or test runs in a review item. |
| Closing or commenting | decided: none. No JIRA, GitHub or email posts. |
| Committed rows | decided: find the commit with `git log origin/main --grep=KEY` and check it covers the ticket title. Verdict fixed or unclear. |
| Support rows | decided: check whether the reference guide or the docs answer the question. Verdict gone is not used here; use fixed when answered in docs, present when the code path is still open, unclear otherwise. |

### Deliverable

`reports/2026-10-18-skip-repro/B3-committed-support-round-1.md`: one row per ticket with verdict and evidence.

### Limits

- No PR, comment, JIRA or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.


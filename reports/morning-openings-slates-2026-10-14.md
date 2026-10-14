# Morning openings slates, 2026-10-14

Workstream W4 of `assignments/pool-category-closeout-overnight-round-1.md`, as widened by its census addendum. Sources: `reports/openings-census-round-1.md` (263 rows), the census follow-up rounds 1 to 5 (`reports/census-followup-scheduled-round-*.md`), `reports/category-ready-lists-round-1.md`, `reports/solrcloud-openings-slate-round-2.md`, the round 2 settlement responses (`reports/*-convergence-round-2-response.md`), and the receipts for the heads cited.

Verification during assembly: every one of the 263 census branch tips was re-checked against `git ls-remote` on the fork on 2026-10-14. Twelve tips had moved since the census; each moved tip was re-checked against its gate record before its row was placed (SOLR-874, 9124, 10234, 10305, 12044, 12608, 14381, 16813, 3865, 8554, 9060, 9342; the SOLR-16272 and SOLR-16813 title gates and the SOLR-9342 title check were read in their gate files and receipts directly). Receipts spot-checked at the live tip for the slate heads named in the SolrCloud round 2 slate and the CLI title gates. One further tip moved while this file was being assembled: solr-11356-submit, whose header-copy gate landed green at 8b2e826154af; its row was updated to the new head before this file was pushed. Nothing in this file opens anything; each slate is approved per category.

## Headline totals

- **Openable as-is: 159**
- **Openable after one named step: 26** (a packaging fix, a posted gate job, or one owner word; the step is named under its slate)
- **Slate total proposed today: 185**
- **Not ready: 78** of the 263 census rows, each with its named next step in the last slate section

| Category | Slate rows | Openable as-is | After one named step | Not ready |
|---|---|---|---|---|
| Search components | 56 | 52 | 4 | 10 |
| Query parsing | 16 | 14 | 2 | 6 |
| SolrCloud | 15 | 12 | 3 | 10 |
| Core admin | 21 | 20 | 1 | 7 |
| SolrJ | 18 | 11 | 7 | 2 |
| Schema | 10 | 9 | 1 | 1 |
| Replication and backup | 6 | 6 | 0 | 5 |
| Streaming | 5 | 4 | 1 | 5 |
| CLI | 9 | 9 | 0 | 3 |
| Build and docs | 9 | 7 | 2 | 4 |
| Configsets | 5 | 4 | 1 | 0 |
| Security | 1 | 1 | 0 | 3 |
| Spellcheck | 8 | 8 | 0 | 1 |
| Suggester | 5 | 2 | 3 | 3 |
| Metrics | 1 | 0 | 1 | 0 |
| eDisMax | 0 | 0 | 0 | 13 |
| Highlighting | 0 | 0 | 0 | 2 |
| Admin UI | 0 | 0 | 0 | 2 |
| Update processing | 0 | 0 | 0 | 1 |
| **Total** | **185** | **159** | **26** | **78** |

## How the three readiness bars reconcile

The records use three different bars, and they do not count the same thing:

- **Census READY TO OPEN (91)** is the loosest bar: a gate green at the live tip and a draft converged in its category record. It does not check packaging items (changelog titles, TESTING files, fragment types).
- **Ready-lists READY (30 of 62)** is the strictest bar: full packaging verification at the live tip (head citations, Proof counts against the receipt, changelog YAML and type, bold one-line summaries, length, no TESTING file). It covers only search components and query parsing.
- **Follow-up SATISFIED (rounds 1 to 5)** is a draft bar: the draft text was fixed and re-reviewed until SATISFIED, read against the receipt at the live tip. It is not a fresh gate; where a follow-up fix moved a branch head, the row stands only where a gate record covers the moved head.

The split this file uses: a row is **openable as-is** when its gate record covers the live tip and its draft stands converged or SATISFIED with no packaging item outstanding. A row is **openable after one named step** when exactly one thing stands between it and that state, and that thing is named under its slate: a packaging title or fragment fix (9 rows), a posted gate job (4 rows), the missing-receipt jobs (2 rows), a draft confirmation against a landed head (1 row), a staged draft-text application against a gate that landed during assembly (1 row), or one owner word at approval (9 rows). Everything else is **not ready** and carries its next step below.

Two honest caveats inside the as-is count. First, the W1 ready lists applied the ruling that a Proof verification date is advisory; 13 slate rows (search components SOLR-6207, 6831, 6975, 7550, 10694; query parsing SOLR-4824, 8977, 9048, 9149, 10897, 11761, 17280, 17311) stand as-is under that reading. Owner ruling (2026-10-14): the Proof verification date is advisory, not mandatory. The 13 rows stand as openable as-is and the as-is total of 159 stands; owner item 4 below is discharged. Second, SOLR-18109 stands as-is on the ruling that a tests-only change needs no changelog fragment.

## Slates by category

Rows marked * are the after-one-step rows; their step is listed under the slate. Approving a slate approves its as-is rows for opening as drafts; the marked rows open as soon as their named step lands.

### Search components (56 rows)

| Ticket | Branch | Live tip | Change, one line (from the draft) |
|---|---|---|---|
| SOLR-4374 | solr-4374-submit | 801c62290f79 | A top-level item that starts with a digit is read as a field when the schema has a field with that name Opened as draft PR #5130 on 2026-10-14. |
| SOLR-5394 | solr-5394-submit | 967445622f92 | The default per-field thread count changes from -1 (no limit) to 1 Opened as draft PR #5131 on 2026-10-14. |
| SOLR-6207 | solr-6207-submit | b099a9f1be5a | The javadoc now states what the method returns and what it leaves out Opened as draft PR #5132 on 2026-10-14. |
| SOLR-6759 | solr-6759-submit | 42f03eb52376 | The expand search now calls `complete()` on its collector chain after the search Opened as draft PR #5133 on 2026-10-14. |
| SOLR-6831 | solr-6831-submit | 96b33ba5f6f8 | The pivot loop checks the query limits before each value and stops at the first limit that has tripped Opened as draft PR #5134 on 2026-10-14. |
| SOLR-6975 | solr-6975-submit | 761aa629bb83 | A `_docid_` sort no longer reads a sort value from the shards. Results from different shards are grouped by shard Opened as draft PR #5135 on 2026-10-14. |
| SOLR-7390 | solr-7390-submit | 7463dd006a78 | An unregistered bracket transformer in `fl` now fails with 400 Bad Request Opened as draft PR #5136 on 2026-10-14. |
| SOLR-7498 | solr-7498-submit | 2050d8e447a7 | A stream with an unknown size gets no `stream_size` value. A known size is still recorded Opened as draft PR #5137 on 2026-10-14. |
| SOLR-7520 | solr-7520-submit | 10b6931e1c05 | The grouped path now calls `complete()` on the post filter after each shard search, as the non-grouped path does Opened as draft PR #5138 on 2026-10-14. |
| SOLR-7550 | solr-7550-submit | 687165651f92 | A 500 for the versions request now counts as success when `cantReachIsSuccess` is set, the same as a 503 Opened as draft PR #5139 on 2026-10-14. |
| SOLR-8003 | solr-8003-submit | 17c516a607a8 | A glob followed by a raw value transformer now writes each matching field raw Opened as draft PR #5140 on 2026-10-14. |
| SOLR-8009 | solr-8009-submit | 8795661ddc98 | An unrouted id on an implicit-router collection is now sent to every shard Opened as draft PR #5141 on 2026-10-14. |
| SOLR-8020 | solr-8020-submit | 79f790523d2f | `SearchHandler` adds its empty debug section only when the response has no debug entry yet Opened as draft PR #5142 on 2026-10-14. |
| SOLR-8051 | solr-8051-submit | e50a2aa3437c | A shard with no response is now skipped in the global stats merge Opened as draft PR #5143 on 2026-10-14. |
| SOLR-8088 | solr-8088-submit | 567efa9b78c3 | Grouping on a multiValued field is rejected up front with a 400 error Opened as draft PR #5144 on 2026-10-14. |
| SOLR-8240 | solr-8240-submit | fad7a1dd8e23 | A request that sends `f` with `mapUniqueKeyOnly=true` now fails with a 400 error Opened as draft PR #5145 on 2026-10-14. |
| SOLR-8939 | solr-8939-submit | a855a2d8965c | Date keys are sent as ISO-8601 text with milliseconds, on the plain and the grouping stored-fields paths Opened as draft PR #5146 on 2026-10-14. |
| SOLR-9124 | solr-9124-submit | 64b8256cde72 | Grouped distributed queries that score now send the merged term statistics in the group stages, and they read the statistics the shards return Opened as draft PR #5147 on 2026-10-14. |
| SOLR-9396 | solr-9396-submit | a5ab2eda4e6a | The transformer asks the index for the fields its subquery references Opened as draft PR #5148 on 2026-10-14. |
| SOLR-9595 | solr-9595-submit | bf99cd0e89c2 | Terms are cached per field. The live-docs view is built once, when the wrapper is created Opened as draft PR #5149 on 2026-10-14. |
| SOLR-9864 | solr-9864-submit | b5826e466b9a | `getCopy()` also copies the sort clause list Opened as draft PR #5150 on 2026-10-14. |
| SOLR-10305 | solr-10305-submit | f55912697215 | A distributed request on a schema with no uniqueKey field now gets a 400 error with a clear message Opened as draft PR #5151 on 2026-10-14. |
| SOLR-10424 | solr-10424-submit | e629ab8bb293 | The sample no longer sets `mapUniqueKeyOnly` for `/update/json/docs`, so each posted field is indexed under its own name Opened as draft PR #5152 on 2026-10-14. |
| SOLR-10492 | solr-10492-submit | ecf21e2192cb | Refinement counts are grouped counts when group.facet=true Opened as draft PR #5153 on 2026-10-14. |
| SOLR-10694 | solr-10694-submit | 64e86811548b | The CSV writer writes a map, NamedList, iterator or array value as one JSON cell |
| SOLR-10844 | solr-10844-submit | e87515c56d04 | Grouped faceting now rejects these numeric fields with a 400 error that names the field |
| SOLR-11129 | solr-11129-submit | dc82676b8096 | The coordinator now reads the per-field local params of field facets, as the shards already do |
| SOLR-11153 | solr-11153-submit | 093df0d65bdc | When the name or version is missing, the writer leaves that attribute out |
| SOLR-11310 | solr-11310-submit | e38ddec5279c | Every elevated document in the rerank window gets the boost, not only a leading run |
| SOLR-11364 | solr-11364-submit | 562d3e7e685a | When fl has a glob, a field named by its own name in fl is now returned, even if it has useDocValuesAsStored=false |
| SOLR-11470 | solr-11470-submit | f44c294da374 | The main query is made searchable before the rank query wraps it |
| SOLR-12044 | solr-12044-submit | de8461adfca8 | `getDocSet(Query, DocSet)` returns the filter itself when the query matches all documents and a filter is given |
| SOLR-12543 | solr-12543-submit | 88d236db6b7d | The handler now rejects an /export request with no sort, or with no fl, with HTTP 400 |
| SOLR-13245 | solr-13245-submit | 16e62ab65425 | Daemons are kept per collection on each node, so list, start, stop and kill see all of the collection's daemons on that node |
| SOLR-13876 | solr-13876-submit | 2e110473dbad | Each group reports the highest score among the documents returned for that group |
| SOLR-14381 | solr-14381-submit | 7caf8d33d3bf | Grouping counts are long values through the grouping code and the final response. Shards send a count as Integer whenever it fits |
| SOLR-14451 | solr-14451-submit | e60891d8716d | Facet shard requests get query debugging when the user asked for query debugging |
| SOLR-14678 | solr-14678-submit | 5d94e6cf3981 | Each document keeps the nested children the transformer attached to it, in search responses |
| SOLR-14931 | solr-14931-submit | 1a7d678d9a15 | Shards now expand macros in the handler's appends and invariants, using the request parameters as the macro source |
| SOLR-15018 | solr-15018-submit | d0f29b4630c0 | Reconstruction keeps nested child documents under a skipped field name, and still drops plain values |
| SOLR-15041 | solr-15041-submit | 55fca0a7c243 | Line breaks inside a split value are kept, and the split continues after them |
| SOLR-15319 | solr-15319-submit | 4bda91f46fc1 | Each shard's statistics are keyed by its collection and shard name. The coordinator uses the keys that the shards send |
| SOLR-15331 | solr-15331-submit | 6769b4cd8a12 | `BucketBasedJsonFacet` reads the `missing` entry and exposes it through a new `getMissingCount()` method |
| SOLR-15479 | solr-15479-submit | 57bd53ce4d07 | For a request that uses a rank query, maxScore is now the highest final score in the collected result window |
| SOLR-15895 | solr-15895-submit | 02930909397a | A create request now checks the id against the file storage first, and returns 400 when the file cannot be named |
| SOLR-16444 | solr-16444-submit | 8ffee94a5e75 | A registration for a resource that is already loaded now gets the loaded data at once |
| SOLR-17051 | solr-17051-submit | 148544e9ed54 | A missing bucket below a `mincount` above 1 is left out of the response. At the default `mincount`, nothing changes |
| SOLR-17155 | solr-17155-submit | 1413237f7a75 | The unique key is read from its stored value, or from docValues when it is not stored |
| SOLR-17748 | solr-17748-submit | dfacaf347668 | The field step skips a failed shard that has no name, and it keeps the error when the exception has no cause |
| SOLR-17791 | solr-17791-submit | a39c1c973766 | A store name in the URL now becomes the store for features in that request that have no `store` attribute |
| SOLR-17976 | solr-17976-submit | 56ea43c448ed | Ties are broken by the SolrCloud shard name, so the order no longer depends on which replicas answer |
| SOLR-18109 | solr-18109-submit | b19395e1f60a | This change is test only. It adds two regression tests and cleans up one shared test index. It changes no production code |
| SOLR-8767 * | solr-8767-submit | 3b5f2d235732 | The response keeps stored copy-field targets, with the same values the index holds after a commit. Atomic updates still drop them |
| SOLR-9148 * | solr-9148-submit | 30f0d7a42d50 | Each `fq` value is passed to every Solr query that the statement becomes |
| SOLR-12556 * | solr-12556-submit | 033ec65a0e1b | With `processEmpty:true`, buckets known before refinement that were not refined against every shard that returned the facet are now dropped instead of returned with partial stats |
| SOLR-16155 * | solr-16155-submit | 0881de1ed68b | Error messages now name the document id and the field, not the values |

Packaging and opening steps for this slate:

- SOLR-8767 (*): Owner confirmation at approval: the draft takes the /get route, and the follow-up round flags that this may override an earlier owner hold. Owner item below.
- SOLR-9148 (*): Packaging: confirm the changelog fragment type at opening (the draft uses "added"; the W1 list flags fixed or changed as the likely right type for a pass-through fix). Main side, at opening.
- SOLR-12556 (*): Packaging: narrow the changelog title as recorded in receipts/SOLR-12556.md (line 9). Main side, at opening.
- SOLR-16155 (*): Owner call at approval: an existing PR covers the same change more widely, so the opening itself is the owner's call. Owner item below.
- SOLR-9148 (marked *): confirm the changelog fragment type at opening; the W1 list flags "added" against fixed or changed for a pass-through fix.
- SOLR-12556 (marked *): narrow the changelog title as recorded in receipts/SOLR-12556.md, line 9.
- Bold one-line summaries: the W1 verification flagged missing summaries on 29 drafts; every slate row here had its fix applied and re-reviewed SATISFIED in the census follow-up rounds, so no summary fix is outstanding on a slate row.
- TESTING.md: the W1 verification confirmed no TESTING file stands at any of the 62 W1 submit tips; nothing to remove on those rows.

- Openings run, 2026-10-14: the first 24 as-is rows of this slate (SOLR-4374 through SOLR-10492, in table order) were opened as draft PRs and are annotated inline on their rows. No holds: every branch tip matched the tip recorded here, and every prepared draft stood as written.

### Query parsing (16 rows)

| Ticket | Branch | Live tip | Change, one line (from the draft) |
|---|---|---|---|
| SOLR-874 | solr-874-submit | a283e050904a | Trailing AND, OR, NOT, && and || are removed. Leading AND, OR, && and || are removed |
| SOLR-4824 | solr-4824-submit | 76c777e4661e | A new `fuzzy.maxExpansions` parameter sets the limit for the lucene parser. Other parsers keep 50 |
| SOLR-8977 | solr-8977-submit | 395b24964fd0 | The graph parser makes its traversal filter queryable itself |
| SOLR-9048 | solr-9048-submit | b145018563cb | An empty nested query adds no clause, so the block join parsers apply no constraint |
| SOLR-9149 | solr-9149-submit | 151dfed119ee | The parser clears its nested-query marker before it builds a phrase with an explicit slop |
| SOLR-10897 | solr-10897-submit | d9240d0750fa | For a point field, the simple parser now asks the field type to build the query |
| SOLR-11761 | solr-11761-submit | 41893ee9ce61 | Each parse call starts from a new token manager |
| SOLR-12212 | solr-12212-submit | 876953fdc927 | With `q.op=AND`, a lone parenthesized pure negative leaves the parser unwrapped |
| SOLR-12532 | solr-12532-submit | 69c06da44679 | The slop is applied to every phrase inside the query that a token graph produces, not only to a plain phrase |
| SOLR-12871 | solr-12871-submit | c79a49320cdb | The child-field sort parser rejects a REWRITEABLE field type before it builds the sort |
| SOLR-15615 | solr-15615-submit | be77267bf554 | When the local lookup misses, the parser looks in the other collections of the request |
| SOLR-16267 | solr-16267-submit | 8f4b0c6d2fb0 | The single and two argument math wrappers now report when their input has no value |
| SOLR-17280 | solr-17280-submit | 40817c5cb7ec | The range query no longer puts its DocSet in the filter cache from inside its weight |
| SOLR-17311 | solr-17311-submit | 9f7524582f9d | A null top value is passed through unchanged instead of being wrapped in a `BytesRef` |
| SOLR-12608 * | solr-12608-submit | c16d49d84173 | Runs of unescaped `*` in a wildcard term are collapsed into one `*`. The terms matched do not change |
| SOLR-16570 * | solr-16570-submit | 9d3466101a29 | For a field with no uninversion type, the top-level reader returns the field's docValues directly |

Packaging and opening steps for this slate:

- SOLR-12608 (*): Draft confirmation against the landed title head c16d49d84173 (title gate green 2026-10-14), and the fail-before sentence rests on a premise log not on disk (disagreement D-1, main side). Review side, then main side.
- SOLR-16570 (*): Two owner items from the follow-up round: which category holds SOLR-16570, and the ticket title against the SOLR-16611 change on main. Owner items below.
- SOLR-12608 (marked *): draft confirmation against the landed title head, plus the D-1 premise-log flag for the main side.
- Landing order: SOLR-12212 enters this slate, and SOLR-17796 (not ready, below) changes the same QueryParser.jj production after it.

### SolrCloud (15 rows)

SOLR-17680 and SOLR-9155 stand READY FOR OWNER APPROVAL from slate round 1, unchanged; approving this slate is the word that opens them (owner item 1).

| Ticket | Branch | Live tip | Change, one line (from the draft) |
|---|---|---|---|
| SOLR-5813 | solr-5813-submit | 90b8baa08aef | An empty collection name now defaults to the core name, as a missing one does |
| SOLR-9155 | solr-9155-submit | 29bf37382899 | An interrupt now reaches the caller as an InterruptedException. Other failures stay a SolrException, with a clearer message |
| SOLR-11288 | solr-11288-submit | cbf0c678f277 | Each name is trimmed, blanks are dropped, and a repeat is kept once. A blank-only value now means an omitted one |
| SOLR-12651 | solr-12651-submit | de3f3604f0ee | A failure before the restored shards become active now deletes the new collection, except a failed shard copy in an async restore |
| SOLR-12991 | solr-12991-submit | 1a86966179e1 | Both connect-failure messages now include the exception, at WARN level |
| SOLR-13186 | solr-13186-submit | b436d90d2a88 | When the Overseer does not start, the node deletes the leader node it wrote, if that node still belongs to it |
| SOLR-13369 | solr-13369-submit | dfa0db5bdf96 | The test accepts a prefix in several shards when the router searches all of those shards for that prefix |
| SOLR-15674 | solr-15674-submit | 75ddf9ba518a | The cache check now compares the znode's mzxid instead of its data version |
| SOLR-15863 | solr-15863-submit | 78dff1d52f6c | Each shard reports the oldest Lucene version among its segments, and the backup records the oldest of those values |
| SOLR-16013 | solr-16013-submit | 37b01337eb4c | The Overseer finishes closing before the session ends, and an interrupt does not cut that wait short |
| SOLR-16437 | solr-16437-submit | a23f770e4b95 | ADDREPLICAPROP now returns HTTP 400 and changes nothing when the collection, the shard or the replica in that shard is not found |
| SOLR-17680 | solr-17680-submit | 52b641ca1a26 | Multi-dimensional aliases send a top-level router name, and the field check moves out of the command |
| SOLR-3865 * | solr-3865-submit | 1ca7bf9ad0e6 | After `close()`, the provider stays closed, including when it never connected |
| SOLR-10234 * | solr-10234-submit | 8745afd00cf9 | The two multi-node base classes now suppress HandleLimitFS as well as ExtrasFS. This changes test base classes only |
| SOLR-10641 * | solr-10641-submit | fbc1f47b2808 | The response write and the request delete now go in one ZooKeeper multi transaction |

Packaging and opening steps for this slate:

- SOLR-3865 (*): Owner decision 8 (commit-body cleanup) is still open for this branch. Owner item below.
- SOLR-10234 (*): Owner decision 8: the gate job rewrote commit 48784679f96's message; confirm before opening. Owner item below.
- SOLR-10641 (*): Owner call at approval: the draft ships the branch alone, and the SolrCloud answers file still marks that call DISCUSS (item 11). Owner item below.
- SOLR-3865, SOLR-10234 and SOLR-10641 (marked *): each waits on the one owner word named in its row, not on a code or text fix.
- Landing order: SOLR-15478 (configsets slate) lands after SOLR-15674 in this slate.

### Core admin (21 rows)

| Ticket | Branch | Live tip | Change, one line (from the draft) |
|---|---|---|---|
| SOLR-4502 | solr-4502-submit | 3695620b22ab | `create` now checks that the container is loaded, and fails with a message that names `load()` |
| SOLR-4989 | solr-4989-submit | 5bac95376cd4 | `show=all` now adds the schema section after the fields section |
| SOLR-5262 | solr-5262-submit | 616efd008b05 | When ulogDir is not set, `solr.core.ulogDir` takes the dataDir value |
| SOLR-6438 | solr-6438-submit | 8c77988d5917 | A request with both kinds of source now gets a 400 error, and no source is opened |
| SOLR-8275 | solr-8275-submit | e52e10fa50a3 | The timeout message now names the replica, the wait settings, and the last state seen |
| SOLR-8554 | solr-8554-submit | 1e8a8a4b6bf0 | FORCELEADER now returns an error when no active leader appears, when the shard is removed, and when the wait is interrupted |
| SOLR-8576 | solr-8576-submit | 883bb522caf9 | One new test covers both name collisions, and it checks that the rejected creates leave the original collection in place and add no collection named like the alias |
| SOLR-9750 | solr-9750-submit | f97da6da14aa | The `/graph` handler now reads the `_GRAPH` paramset |
| SOLR-11939 | solr-11939-submit | cdb9903506e9 | The CREATE section gets a note that core names are generated, and that `property.name` does not rename the cores of a new collection |
| SOLR-12007 | solr-12007-submit | bdeba582fd63 | On close, the cleanup runs in the closing thread, before the factory closes. The reload path still uses the background thread |
| SOLR-12916 | solr-12916-submit | d62ddfc24616 | A list of alternating names and plain values now becomes one warming query |
| SOLR-13246 | solr-13246-submit | 6817c6c0c267 | The debug line logs only the searcher name. The message text is otherwise unchanged |
| SOLR-15003 | solr-15003-submit | 1004abee39ab | The old index directory is cleaned up after the new index is active, and files a named snapshot keeps are not deleted |
| SOLR-15024 | solr-15024-submit | f95b5010b3fe | Luke now returns `charFilters` as an ordered list. Each entry has `className` and `args` |
| SOLR-15805 | solr-15805-submit | 6d78603d8257 | Startup failures that are not `Error`s now fail the servlet context |
| SOLR-16499 | solr-16499-submit | 51b7addddd45 | `parallel` and `timeout` now reach the operation in both the v1 and v2 APIs |
| SOLR-16849 | solr-16849-submit | d612b055da20 | This change adds one regression test and changes no production code |
| SOLR-17297 | solr-17297-submit | c0ab38fc0a8b | `NodeConfig` adds the modules before the shared lib, so a shared lib SPI is registered with the loader that stays open |
| SOLR-17377 | solr-17377-submit | 22b5f209a11a | The class check runs at the end of the `NodeConfig` constructor, after the shared lib and the modules are on the class loader |
| SOLR-17708 | solr-17708-submit | 2eceb5473037 | For v2 requests that match no v2 API object and are admin or core requests, the first check is skipped. When a JAX-RS resource matches, the JAX-RS filter is then the only check |
| SOLR-8628 * | solr-8628-submit | 5b606dc779d0 | A directory whose only file is write.lock is treated as having no index, so a new index is created |

Packaging and opening steps for this slate:

- SOLR-8628 (*): Gate job gates/SOLR-8628-packaging.md (OPEN): remove the root TESTING file, then the top-up check at the moved head. Gate host (vm1 or vm2).
- SOLR-8628 (marked *): the root TESTING file removal is the one packaging step in this category, and it runs as its gate job, not at the opening desk.

### SolrJ (18 rows)

SOLR-4424 (about 4,116 characters) and SOLR-12094 (about 4,390) run over the 3,850 length guide the follow-up round applied; both are SATISFIED and are listed as-is, with the length noted here rather than held.

| Ticket | Branch | Live tip | Change, one line (from the draft) |
|---|---|---|---|
| SOLR-2018 | solr-2018-submit | 213457aa91fd | The branch documents that `waitFlush` is ignored. It does not change how the flag behaves |
| SOLR-3498 | solr-3498-submit | 2ca0b3743427 | `ContentWriterUpdateRequest` now sets the `commitWithin` request parameter when the value is set. A negative value removes the parameter |
| SOLR-3722 | solr-3722-submit | f9d3dda1d3de | NamedList skips null elements in the constructor that takes a `Map.Entry` array. SimpleOrderedMap gets the same behavior |
| SOLR-4422 | solr-4422-submit | 3e5b7afecddb | The binder collects dynamic-field values into a `LinkedHashMap`, so the bound map keeps the document's field order |
| SOLR-4424 | solr-4424-submit | 2e947b7f622c | `NamedList.toSolrParams` throws a `SolrException` for an entry with no name |
| SOLR-5220 | solr-5220-submit | e415c409044c | A 403 or 404 still moves the request to another server, but the first server stays in rotation |
| SOLR-7709 | solr-7709-submit | 501078f22203 | Both document readers add repeated fields in stream order, as the XML reader does |
| SOLR-10364 | solr-10364-submit | efb1e6717f9f | A bean field declared as Set, HashSet or LinkedHashSet is filled from the document's values |
| SOLR-12094 | solr-12094-submit | 8d957a73f4fb | A mapped field that follows the split records now raises an error that names the field |
| SOLR-14298 | solr-14298-submit | 67e75ea1eea4 | The check adds `_zombieservercheck=true`, so its requests can be found in the logs |
| SOLR-14967 | solr-14967-submit | ee76643f5b3f | Requests with params that cannot be changed now send a copy of those params that carries the client's own state version when the client has one |
| SOLR-3999 * | solr-3999-submit | e3af6f36e312 | Each of the four classes now declares `serialVersionUID = 1L`, so the number no longer follows the class's shape |
| SOLR-4335 * | solr-4335-submit | 8f6e267d6482 | Each of the two characters is written as the text `#65534;` or `#65535;`, the way the existing control characters are written |
| SOLR-4336 * | solr-4336-submit | 798618aa08fd | The numeric getters treat an exactly empty value as unset, the same as a missing value |
| SOLR-6046 * | solr-6046-submit | 3b833e369a0b | The XML writer expands an `Object[]` operand into one field element per element, the same way it already expands a `Collection` |
| SOLR-8536 * | solr-8536-submit | 28552ddc26eb | ASCII control characters are replaced with spaces in the joined string, before the thread name is built |
| SOLR-18341 * | solr-18341-submit | 458b098719d7 | Each request says whether it may be retried, and both clients ask it |
| SOLR-11356 * | solr-11356-submit | 8b2e826154af | Each stream keeps a copy of the credentials and headers of the request that opened it, and only matching requests join it |

Packaging and opening steps for this slate:

- SOLR-3999 (*): Packaging PK-1: land the replacement changelog title (SolrJ round 2 response, packaging table), then refresh the receipt head line. Main side, at opening.
- SOLR-4335 (*): Packaging PK-2: land the replacement changelog title (SolrJ round 2 response, packaging table), then refresh the receipt head line. Main side, at opening.
- SOLR-4336 (*): Packaging PK-3: land the replacement changelog title (SolrJ round 2 response, packaging table), then refresh the receipt head line. Main side, at opening.
- SOLR-6046 (*): Packaging PK-4: land the replacement changelog title (SolrJ round 2 response, packaging table), then refresh the receipt head line. Main side, at opening.
- SOLR-8536 (*): Packaging PK-5: land the replacement changelog title (SolrJ round 2 response, packaging table), then refresh the receipt head line. Main side, at opening.
- SOLR-18341 (*): Packaging: the changelog title must name the load balancer change and the new public method SolrRequest.isRetriable(); the SOLR-18368 hunk at CloudSolrClient.java line 1550 is settled in the same pass. Main side, at opening.
- SOLR-11356 (*): The header-copy gate landed green during assembly (2026-10-14T13:33Z) at the patched head 8b2e826154af, pushed and receipted. The one step left is applying the staged post-gate draft text staged in reports/solrj-convergence-round-2-response.md (summary line, Limits bullets, Proof bullet from the new receipt, head re-points, JDK suite line). Review coordinator.
- SOLR-3999, SOLR-4335, SOLR-4336, SOLR-6046, SOLR-8536 (marked *, items PK-1 to PK-5): land the replacement changelog title recorded verbatim in reports/solrj-convergence-round-2-response.md (packaging table), with a changelog YAML parse, then refresh the receipt head line. Main side, at opening.
- SOLR-18341 (marked *): the title must name the load balancer change and the new public method SolrRequest.isRetriable(); the SOLR-18368 hunk at CloudSolrClient.java line 1550 is settled in the same pass.
- Title note carried for SOLR-17866 (not ready, below): its title says setRequiresCollection(false); the code opts out on any explicit call.

### Schema (10 rows)

SOLR-15357 opens with decision 2 settled as stay narrow (the gap in Limits stands, with a follow-up offered); SOLR-15358 lands after it and is not ready (below).

| Ticket | Branch | Live tip | Change, one line (from the draft) |
|---|---|---|---|
| SOLR-9349 | solr-9349-submit | 1e79bb42123c | The Schema API now refuses to delete the uniqueKey field with a 400 error |
| SOLR-10131 | solr-10131-submit | fd495167cd09 | A 36-character value with a non-hex character now gets a 400 error |
| SOLR-10403 | solr-10403-submit | d8e03755cb5d | Conversions round to the nearest minor unit |
| SOLR-14199 | solr-14199-submit | 2c549d9fb384 | Solr now rejects `omitNorms="false"` on point types and on indexed point fields |
| SOLR-15357 | solr-15357-submit | e9e7cc3a0c1c | Sub-fields of a copyField destination are now counted as copyField targets |
| SOLR-15945 | solr-15945-submit | 793765c92764 | For a field with `indexed="false"`, the norms check and the index-options check are skipped |
| SOLR-16977 | solr-16977-submit | c65d242718e7 | A KNN query with an all-zero vector now fails with HTTP 400 when the field uses cosine similarity |
| SOLR-17047 | solr-17047-submit | 79fc289bfc1a | Core init now checks each DenseVectorField's KNN settings and fails the core when a setting cannot be honored |
| SOLR-18134 | solr-18134-submit | 78134dde678d | The query analyzer adds `ZeroPositionIncrementFilter`, which puts the path prefixes at one position so they match as alternatives |
| SOLR-15712 * | solr-15712-submit | cb988a2ff4c3 | A collation field no longer uses its docValues as stored values. A schema that sets the flag on one now fails to load |

Packaging and opening steps for this slate:

- SOLR-15712 (*): Gate job gates/SOLR-15712-udvas-version-gate.md (OPEN): land the ICU-style version gate change and gate it; the draft is then rewritten. Gate host (vm1 or vm2).
- SOLR-15712 (marked *): the version-gate change lands through its gate job before opening; no desk packaging step stands on the other rows.

### Replication and backup (6 rows)

| Ticket | Branch | Live tip | Change, one line (from the draft) |
|---|---|---|---|
| SOLR-6711 | solr-6711-submit | 73c5e6e0d243 | `disablepoll` saves the setting only with `persist=true`. `enablepoll` takes no `persist` parameter and always clears a saved setting |
| SOLR-8430 | solr-8430-submit | 0d64c35c5a7d | File streams that ask for the same rate now share one limiter on the node, for up to 64 distinct rate values |
| SOLR-9382 | solr-9382-submit | 2952bae9d392 | The leader expands a confFiles entry with `*` or `?` in its file name into the matching files of its config directory |
| SOLR-12085 | solr-12085-submit | a220b0728dc8 | The used set now includes the files of every commit that the index directory still lists |
| SOLR-12246 | solr-12246-submit | 3ffc2539ec76 | The checksum mismatch message moves from WARN to INFO. The comparison result does not change |
| SOLR-17287 | solr-17287-submit | 6957daf82610 | A successful standalone restore now clears the update log |

Packaging and opening steps for this slate:

- SOLR-9382: the packaging state is on the fork at the live tip; the main side recreates the VM2-local packaging commits at opening only if a later record requires it.
- SOLR-9091 (not ready, below): its packaging recreation is main-side-only and is listed there; it is not attempted from the review side.
- Landing orders: the IndexFetcher chain runs SOLR-12246, SOLR-12085, SOLR-11650, then SOLR-6711; SOLR-6711 is in this slate with that order noted, and SOLR-11650 is not ready, so the chain completes later.

### Streaming (5 rows)

| Ticket | Branch | Live tip | Change, one line (from the draft) |
|---|---|---|---|
| SOLR-9852 | solr-9852-submit | 5fe174425514 | GetColumns now returns the columns that match its patterns, and getTypeInfo throws the standard JDBC exception |
| SOLR-10322 | solr-10322-submit | 80ce9d7a3c8a | With no context cache, the stream opens its own cache and uses it for checkpoint reads |
| SOLR-10882 | solr-10882-submit | b12b0ffab487 | A sort over mixed kinds now stops with a clear IOException |
| SOLR-12657 | solr-12657-submit | 23cc27a98925 | Date min and max come back as ISO-8601 strings, and min and max over string fields return strings too |
| SOLR-17433 * | solr-17433-17143-submit | 42b6c4fc9158 | Clients that the stream cache builds get no total time limit |

Packaging and opening steps for this slate:

- SOLR-17433 (*): Owner call at approval: retire or ship. The draft is SATISFIED; the receipt says the call is pending and the audit recommends retire. Owner item below.
- SOLR-17433 is a marked row in this slate; its step is the owner's retire-or-ship call, not a packaging fix.
- SOLR-10882: rebase before opening, under owner decision 5 as settled in streaming round 1. That is an opening-time step, not a draft edit.

### CLI (9 rows)

| Ticket | Branch | Live tip | Change, one line (from the draft) |
|---|---|---|---|
| SOLR-7924 | solr-7924-submit | 96ef3a52bdbf | The spinner tests `sleep` once. If `sleep` rejects fractions, the spinner uses one-second steps |
| SOLR-9342 | solr-9342-submit | 3e5d46d2c862 | The server JVM's GC log now uses `SOLR_TIMEZONE` |
| SOLR-10667 | solr-10667-submit | 9ce9ef1db5e9 | The packaging task copies each module's `example` directory into the distribution, except the `solrclient` folder the build generates |
| SOLR-12347 | solr-12347-submit | 56d38389c75a | The default stop wait is now 600 seconds. The start wait keeps its 180-second default |
| SOLR-16272 | solr-16272-submit | fbcd65f71652 | The install removes the files it posted when it fails, unless the version may already be registered |
| SOLR-16813 | solr-16813-submit | 0ab6087cf437 | When a version loads, its manifest.json is fetched into the local file store, and a failed fetch only logs a warning |
| SOLR-17029 | solr-17029-submit | 4a98ef0a89d1 | Both values go through one small parser, and the parser expands and runs nothing |
| SOLR-17598 | solr-17598-submit | 8c91cf047a96 | The value is read only through delayed expansion, so the path never becomes part of the block text |
| SOLR-18132 | solr-18132-submit | 54835cac6f85 | Tools that take a ZooKeeper connection now use solr.ssl.enabled to choose the scheme, and the launcher passes that setting to the server and tool JVMs |

Packaging and opening steps for this slate:

- Landing order: SOLR-7924 opens after SOLR-18339 in the CLI landing order; SOLR-18339 itself is an owner item below.
- SOLR-17598: no gate applies (solr.cmd-only change); the confirming Windows run stays deferred under the owner Windows rule, and the draft states that limit. Nothing is owed at the desk.

### Build and docs (9 rows)

| Ticket | Branch | Live tip | Change, one line (from the draft) |
|---|---|---|---|
| SOLR-5821 | solr-5821-submit | 327547d693e0 | The change adds three sentences to the tie-breaker paragraph in common query parameters |
| SOLR-6430 | solr-6430-submit | 78cddfb520d6 | The table row now states the default placement for string, numeric and date fields |
| SOLR-11700 | solr-11700-submit | 8a945757e668 | The examples now show each catenated token at the position of its first part, listed before the parts |
| SOLR-13705 | solr-13705-submit | 73504e7a8dcb | It makes the field `volatile`. That is the only production change |
| SOLR-16914 | solr-16914-submit | cd878023d3d0 | The change names the option, gives its default, and says when synonyms need `normal` mode |
| SOLR-17252 | solr-17252-submit | d730a266a042 | The wizard now stops with a message when the first word of EDITOR names one of those five editors |
| SOLR-17356 | solr-17356-submit | 9fe2801de07c | The Ukrainian section names the external dictionary library and the path that library provides |
| SOLR-7119 * | solr-7119-submit | 4c652c93b99c | The Interval Faceting section now says where `ex` goes, with an example |
| SOLR-17842 * | solr-17842-submit | 008973f63133 | It adds a guide for running and comparing release benchmarks, and links to it from the benchmark README |

Packaging and opening steps for this slate:

- SOLR-7119 (*): Gate job gates/SOLR-7119-changelog-drop.md (OPEN): drop the changelog fragment per owner decision 14, adopted in the follow-up round. Gate host (vm1 or vm2).
- SOLR-17842 (*): Land the placeholder fix (patch in gates/SOLR-17842-placeholder-fix.patch) as a branch commit, then the light gate at the new head (gates/SOLR-17842-placeholder-fix.md, OPEN). Branch owner pushes; gate host runs the light gate.
- SOLR-7119 and SOLR-17842 (marked *): both steps run through their gate jobs before opening; no desk packaging step stands on the other rows.

### Configsets (5 rows)

| Ticket | Branch | Live tip | Change, one line (from the draft) |
|---|---|---|---|
| SOLR-7267 | solr-7267-submit | 59f34a339b0d | The _default configset adds the "cs" names next to the "cz" names. The "cz" names stay |
| SOLR-7323 | solr-7323-submit | b832dc0048c0 | The message now names the configset, the base directory, and the setting that controls the base directory |
| SOLR-15478 | solr-15478-submit | 0478bdf0ac5c | The schema cache key now uses the ZooKeeper modification ID (mzxid) of the schema znode, not its data version |
| SOLR-17363 | solr-17363-submit | b8e8e1c48461 | A replica that misses the version check no longer fails the request if it is not an active replica when the wait ends |
| SOLR-6960 * | solr-6960-submit | 9bef536fc12a | The report now merges initParams into request handlers defined in solrconfig.xml. It uses the same helper the live handler uses |

Packaging and opening steps for this slate:

- SOLR-6960 (*): Packaging: the changelog title fix and a second test run named as owner items for the slate in the configsets round 1 response. Main side, at opening.
- SOLR-6960 (marked *): changelog title fix and a second test run at opening, per the configsets round 1 response.
- Landing order: SOLR-15478 lands after SOLR-15674 (SolrCloud slate). SOLR-7267's draft poses one Choice.

### Security (1 rows)

| Ticket | Branch | Live tip | Change, one line (from the draft) |
|---|---|---|---|
| SOLR-12161 | solr-12161-submit | cdd265168a2a | It adds one test block, and it changes no production code |

Packaging and opening steps for this slate:

- SOLR-10627 (not ready, below): the replacement fragment title is recorded verbatim in reports/security-convergence-round-1-response.md; its length was accepted with no trim (7,708 characters with links). It lands through its gate job, not at the desk.

### Spellcheck (8 rows)

| Ticket | Branch | Live tip | Change, one line (from the draft) |
|---|---|---|---|
| SOLR-1877 | solr-1877-submit | 5533ba27b17e | Spell checkers now close with the core. A reload closes the old reader once no search is still using it |
| SOLR-3701 | solr-3701-submit | aabd678dec73 | An apostrophe followed by letters now stays inside the word |
| SOLR-4366 | solr-4366-submit | 558864454f7f | An unknown dictionary name now gets the same NOT_FOUND error that a query gets |
| SOLR-4367 | solr-4367-submit | 0af6087f43fa | A top-level `classname` now fails the core load |
| SOLR-4399 | solr-4399-submit | 82c0c11373d0 | The build now stops with an error that names the missing `sourceLocation` parameter |
| SOLR-9060 | solr-9060-submit | 49040cb7b88e | Shards always report frequencies, so the coordinator can break ties by frequency |
| SOLR-10789 | solr-10789-submit | d9077048d74d | The scoring parameters are removed first, and the overrides are copied in after that |
| SOLR-17612 | solr-17612-submit | 3a456c27dda5 | A fractional `spellcheck.maxResultsForSuggest` is now applied to the filtered document count summed across all shards of a distributed request, not to the coordinator's own slice |

Packaging and opening steps for this slate:

- No packaging steps stand on this slate; every row was confirmed against its receipt in the follow-up rounds.

### Suggester (5 rows)

| Ticket | Branch | Live tip | Change, one line (from the draft) |
|---|---|---|---|
| SOLR-9227 | solr-9227-submit | 3c23fc5cfa6c | A store failure now names the suggester and the path. A false store return logs a WARN instead of an ERROR |
| SOLR-14171 | solr-14171-submit | faa262eedc55 | On context-filtered requests, the suggester's configured `allTermsRequired` now applies unless the request sets `suggest.allTermsRequired` |
| SOLR-9968 * | solr-9968-submit | c31d2ff1a3fd | A new suggester option sets the tokenizer for context values. The default stays `standard` |
| SOLR-17215 * | solr-17215-submit | 04d35df186dd | A suggester that is not built on a node now answers 503 with a clear message |
| SOLR-17393 * | solr-17393-submit | 626241e647e5 | The merge now sorts every shard candidate by weight, then by suggestion text, before it cuts to `count` |

Packaging and opening steps for this slate:

- SOLR-9968 (*): Owner confirmation: the draft rests on a recorded green top-up gate (13 tests, 2026-10-09) rather than a gate at the live head c31d2ff1a3f; confirm that run covered this head. Owner item below.
- SOLR-17215 (*): Gate job gates/SOLR-17215-receipt.md (OPEN): vm2 writes the missing receipt from its gate records. Gate host vm2.
- SOLR-17393 (*): Gate job gates/SOLR-17393-receipt.md (OPEN): vm2 writes the missing receipt from its gate records. Gate host vm2.
- SOLR-17215 and SOLR-17393 (marked *): the missing receipts are written by vm2 through their gate jobs before opening; the drafts themselves are final.

### Metrics (1 rows)

| Ticket | Branch | Live tip | Change, one line (from the draft) |
|---|---|---|---|
| SOLR-17987 * | solr-17987-submit | 38abf6423126 | A new system property lists the registries to disable, and those registries get a no-op meter provider |

Packaging and opening steps for this slate:

- SOLR-17987 (*): Packaging: apply the adopted registry rename on the branch at opening and re-gate; the draft line changes in the same pass. Owner confirms the rename is deferred to opening (owner item below). Main side, at opening.
- SOLR-17987 (marked *): the adopted registry rename is applied on the branch at opening packaging, with a re-gate; the draft's changelog line changes in the same pass.

## Not ready, by category

One line per row: what blocks it, the named next step, and who owns that step.

### Search components

- **SOLR-3044** (solr-3044-submit, tip 04b877e9de18): Parked. No gate on record and no draft; the receipt says do not draft it yet. Next step owner: Owner hold (parked).
- **SOLR-6193** (solr-6193-submit, tip ec94bf50c80c): Hold: the scope is disputed in the receipt and a gate job is open on the branch. Next step owner: Gate job on the branch; main side settles the scope.
- **SOLR-8954** (solr-8954-submit, tip 1d981abe7000): Hold: a measured run confirms the branch breaks FullSolrCloudDistribCmdsTest#testRTGCompositeRouterWithRouterField (head fails, base passes). Owner decision 1: close it, or make it a test-only follow-up with SOLR-8009 carrying the all-shards fix. Next step owner: Owner item.
- **SOLR-13851** (solr-13851-submit, tip b60f4d642d1e): Submission-held. Gate green at the tip; the hold stands. Next step owner: Owner hold.
- **SOLR-15144** (solr-15144-submit, tip 68b0fc31e056): Hold per the decision-prep recommendation: a gated distributed run must first show the branch change alone returns documents in the distributed test. Next step owner: Gate: a distributed run, posted by the main side.
- **SOLR-16290** (solr-16290-submit, tip ff760120c817): Owner item: the gated pin at ff760120c817 waits on its disposition (pin PR, fund the real facet fix, or bank it). Next step owner: Owner item.
- **SOLR-17055** (solr-17055-submit, tip c2396f425fa2): Hold: the branch defect is real (an explicit non-score sort can list more than topK documents) and the fix gate is claimed by vm2, but its fail-before leg is not established at the recorded base; the proof-base choice is also an owner item. Next step owner: Gate: gates/SOLR-17055-sort-path.md (vm2); owner item on the proof base.
- **SOLR-17372** (solr-17372-submit, tip 048862fda8ab): Retire candidate: the premise is not supported and the gate failed at the live tip. The retire word is owed. Next step owner: Owner item.
- **SOLR-17841** (solr-17841-submit, tip 478731e67602): Draft SATISFIED on the text and gate green, but the opening is held by the owner's retire call on this branch. Next step owner: Owner hold.
- **SOLR-18541** (solr-18541-combined-query-merge-totals, tip fcd92f23ba65): Variant branch solr-18541-combined-query-merge-totals, superseded by the open PR branch for SOLR-18541. Not an opening. Next step owner: None (variant).

### Query parsing

- **SOLR-6014** (solr-6014-submit, tip 505849d2d4e7): Gate owed: the search-time premise job is claimed by vm2, and the in-branch fix for the cache, cost and fq cases still needs a branch commit and a gate. Next step owner: Gate: claims/pool-vm2-gate-backlog-6014.md (vm2).
- **SOLR-11391** (solr-11391-submit, tip b0f15a22856a): Hold: the D4 confirmation is still open with the main side; the draft also owes its Limits summary and Proof date. Next step owner: Main side (D4), then review: assignments/pool-census-followup-query-parsing-round-1.md.
- **SOLR-13838** (solr-13838-submit, tip 4520c25abe28): Hold, no draft: the receipt says RECONCILED GREEN but no gate log, date or ledger row is on record. A fail-before run at 4520c25abe28 and the ticket text are owed; no gate job is filed yet. Next step owner: Main side files the gate job; owner supplies the ticket text.
- **SOLR-13903** (solr-13903-submit, tip 73ef411dd117): Draft owed. Gate green at the tip (undated in the receipt). Next step owner: Review: assignments/pool-census-followup-query-parsing-round-1.md.
- **SOLR-15906** (solr-15906-submit, tip 50139a8e979c): Hold: reading the branch, the follow-up round found a sort or field-list spec and a stray parenthesis now reach the Lucene path and may break three tests. The reading is not a run; vm2 confirms it under the posted gate job. Next step owner: Gate: gates/SOLR-15906-sort-paren-fix.md (vm2).
- **SOLR-17796** (solr-17796-submit, tip 661165d2673d): Gate owed: the negated-premise job is claimed by vm2; the negated-collapse fix, the title and the grammar regeneration also need code and a gate. SOLR-12212 changes the same QueryParser.jj production, so the landing order runs through it. Next step owner: Gate: claims/pool-vm2-gate-backlog-17796.md (vm2).

### SolrCloud

- **SOLR-4754** (solr-4754-submit, tip e4ea4bfde80e): Hold: the draft is written, but the error text names -Dhost, the changelog title overstates, and no merge-base run of an http:// node exists. Next step owner: Gate: gates/SOLR-4754-text-fixes-and-premise.md (OPEN).
- **SOLR-11479** (solr-11479-submit, tip 6f739cfbbd38): Ready in substance (identity carry-over done, tree identical to the gated head) but it opens only after SOLR-15035 settles. Next step owner: Waits on the SOLR-15035 owner settlement.
- **SOLR-13239** (solr-13239-submit, tip 699a1fce368c): Submission-held. Gate green at the tip; the hold stands. Next step owner: Owner hold.
- **SOLR-14919** (solr-14919-submit, tip 483b3885f643): Gate owed at the moved head 483b3885f643 (title and test-comment precision fixes landed); after a green gate, the receipt and draft heads refresh, then owner item (c) on the headline scope. Next step owner: Gate: gates/SOLR-14919-precision-round-1-gate.md (vm2); then owner item.
- **SOLR-15035** (solr-15035-submit, tip 12d7491e82cd): Stays HOLD. Gate green at the tip; what it waits on is the owner's HOLD to READY settlement line, and the changelog YAML parse gate job, which the review coordinator files and vm2 runs. Next step owner: Owner item.
- **SOLR-15106** (solr-15106-submit, tip 40b7e5d0efa7): Owner checks: the ticket's 1200-message claim has no packet on the branch, and the draft length measure needs one ruling. Gate green at the tip. Next step owner: Owner item.
- **SOLR-15386** (solr-15386-submit, tip ca8cb61ee957): Owner checks: confirm the single-check position in the draft's Choice, and the length (3,680 characters without link targets). Gate green at the tip. Next step owner: Owner item.
- **SOLR-17281** (solr-17281-submit, tip ba21ca32791b): Parked, under review on the owner side; the receipt says do not draft it. Next step owner: Owner hold (parked).
- **SOLR-17292** (solr-17292-submit, tip f614a42fbc80): Draft written and the dated run of record stands (4 of 4 at the tip), but two receipt records exist for the branch and the duplicate needs reconciling before the draft can be confirmed. Next step owner: Review: assignments/pool-census-followup-solrcloud-round-1.md (receipt reconcile, main side).
- **SOLR-17733** (solr-17733-submit, tip cf1b28327264): Gate owed at the moved head cf1b28327264 (title narrowed to the DELETE without localDelete=true); after a green gate, the draft and receipt heads refresh. Next step owner: Gate: gates/SOLR-17733-title-narrowing-round-1-gate.md (vm2).

### Core admin

- **SOLR-5011** (solr-5011-submit, tip db801dbd3bdb): Hold: the shared-schema analysis returned UNSAFE, so the draft stays on hold while the narrowing job runs. Next step owner: Gate: gates/SOLR-5011-narrowing.md.
- **SOLR-14098** (solr-14098-submit, tip dd4995515e88): Hold, no draft: the receipt is RECONCILED GREEN from a ledger row, not a gate run, and the ledger date (2026-10-02) precedes the head's committer date (2026-10-03). Escalated to the main side; the publish-ACTIVE Choice stays open under the recorded hold. Next step owner: Main side.
- **SOLR-16108** (solr-16108-submit, tip 70ad7371b31c): Draft owed. Gate green at the tip. Next step owner: Review: assignments/pool-census-followup-core-admin-round-1.md.
- **SOLR-16725** (solr-16725-submit, tip be1838ef8ccf): Gate owed: two recorded branch edits (a raw-request test comment and a ClusterStatus javadoc), then a top-up gate at the moved head. Next step owner: Gate: gates/SOLR-16725-branch-edits-topup-gate.md (OPEN).
- **SOLR-16887** (solr-16887-submit, tip 42675f65d6fc): Hold, no draft: the adopted round 2 fixes are not on the branch (-XX:ErrorFile dropped from the scripts and guide; the BATS test name claims an OOM it never causes). Next step owner: Gate: gates/SOLR-16887-errorfile-bats-name.md (OPEN).
- **SOLR-17731** (solr-17731-submit, tip 18165d35a7e3): Gate owed: the head, counts and links update rides the posted job; the draft text is converged at the current head. Next step owner: Gate: gates/SOLR-17731.md (OPEN).
- **SOLR-18278** (solr-18278-submit, tip bf19c9fa4498): Owner item, no draft: the recorded recommendation is to retire; the ticket is Resolved and a reviewer calls it invalid. If the branch is kept, an honest draft can be written. Next step owner: Owner item.

### SolrJ

- **SOLR-14187** (solr-14187-submit, tip 29023ec1ecab): Draft written; no convergence verdict on record. Next step owner: Review: assignments/pool-census-followup-solrj-round-1.md.
- **SOLR-17866** (solr-17866-submit, tip 3f9367d796a6): Owner item: the submit-ahead-of-the-design-conversation flag stands. Its changelog title also needs correcting at packaging (it says setRequiresCollection(false); the code opts out on any explicit call). Next step owner: Owner item.

### Schema

- **SOLR-15358** (solr-15358-submit, tip cbb345f2e7df): Hold: the owner call on the default docValues-as-stored setting is open, and the branch lands after SOLR-15357. Next step owner: Owner item.

### Replication and backup

- **SOLR-5589** (solr-5589-submit, tip ddcd75020eea): Draft owed. Gate green at the tip. Next step owner: Review side (next replication draft round; no follow-up assignment posted yet).
- **SOLR-9091** (solr-9091-submit, tip a596a39d2e1e): Draft confirmation owed against the landed retitle head a596a39d2e1e; the RestoreCore landing-order chain (SOLR-9865, SOLR-17287, SOLR-9091) is checked in the same pass. Next step owner: Review: assignments/pool-landing-order-interaction-review-round-1.md.
- **SOLR-9598** (solr-9598-submit, tip c8407773f76c): Owner hold stands. The draft is converged on text and carries the wait-by-default position and an OWNER NOTE. Next step owner: Owner item.
- **SOLR-9865** (solr-9865-submit, tip 4937608bb181): Hold: lands after SOLR-18532 merges; at packaging the branch drops its overlapping rollback hunk (commit cd46e4a3521) and the draft's What this change and Proof are re-derived at the packaged head. Next step owner: Main side at packaging, after SOLR-18532 merges.
- **SOLR-11650** (solr-11650-submit, tip e4f5e941cd8e): Posting hold stands: the owed title, javadoc and test-comment commits have not landed. The draft text is converged and now carries the OWNER NOTE. Next step owner: Branch lane (main side).

### Streaming

- **SOLR-11922** (solr-11922-submit, tip da29a963236f): Hold: test-only branch whose test passes on base production; the premise does not hold on current main. Retire-round material, not an opening. Next step owner: Owner (retire round).
- **SOLR-12505** (solr-12505-submit, tip 1a1875d95f36): Owner items: the defType versus terms parser Choice, the Unicode whitespace decision, and the JIRA attributions. The code and proof at the tip are settled and gate green; the changelog title gate waits on the Choice. Next step owner: Owner items.
- **SOLR-14200** (solr-14200-submit, tip 10991af9bdac): Hold: the draft is written at the gated head, and the ticket scenario does not fail on main. Retire or keep is the owner's call. Next step owner: Owner item.
- **SOLR-14231** (solr-14231-submit, tip 593463f398cb): Hold: the draft is written, but the receipt has no fail-before line, the base counts do not reconcile (10 tests, 4 failures, 3 failure elements, one unexplained ObjectTracker failure), and the head counts carry no verification date. Next step owner: Gate: gates/SOLR-14231-fail-before.md (OPEN, vm2 restates the counts).
- **SOLR-15326** (solr-15326-submit, tip dab6a4663169): Hold: the draft is written at the gated head. Retire or keep is the owner's call, as with SOLR-14200. Next step owner: Owner item.

### CLI

- **SOLR-10390** (solr-10390-submit, tip 5a86f908052f): Hold for the owner: withdraw as superseded by SOLR-18339, or keep the test as a guard (owner decision 8, the retire call). The draft is written. Next step owner: Owner item.
- **SOLR-18339** (solr-18339-ready, tip dc25ea78f88a): Owner item: the opening timing is the owner's call (the SOLR-18336 precondition), with the secured-node Choice posed in the draft. This line is the solr-18339-submit branch, gated green at 47e53884609c. Next step owner: Owner item.
- **SOLR-18339 (branch solr-18339-ready, dc25ea78f88a)**: older variant of solr-18339-submit with no gate record at this head. One branch per ticket goes forward at opening; this variant does not. Owner: none (variant)..

### Build and docs

- **SOLR-3684** (solr-3684-submit, tip 663b8ce754b6): Hold stands as an owner item, decision 1: submit or retire. The record recommends retire; the draft body reads as postable. Next step owner: Owner item.
- **SOLR-9039** (solr-9039-submit, tip 13faf68fe858): Fresh-arrival registration only: the premise is unverified and no draft exists. Next step owner: Review side (premise check and draft).
- **SOLR-16322** (solr-16322-submit, tip 68dd5e2b154e): Hold: owner decision 17 stays open; the draft carries the HOLD line. Next step owner: Owner item.
- **SOLR-17752** (solr-17752-submit, tip e5c0a64f993c): Owner item: the rebase direction for a published branch (open against 56ec140e3636, or rebase onto current main first). The rebase recommendation is adopted; the rerun job is packaged and HELD behind the direction. Next step owner: Owner item; then gate: gates/SOLR-17752-base-rebase-rerun.md (HELD).

### Security

- **SOLR-10627** (solr-10627-submit, tip 5a15dc0ba220): Hold: the replacement changelog title (recorded verbatim in the security round 1 response; its length was accepted with no trim) still has to land on the branch, then the changelog parse runs at the landed head. Next step owner: Gate: gates/SOLR-10627-sec-r1-02-title.md.
- **SOLR-11678** (solr-11678-submit, tip 2c68a83c64ee): Hold: the keystore type of the standalone premise run is in no record on the review host; the reading is owed on vm2. If the run used PKCS12, the premise as recorded does not hold and the ticket goes back to the owner. Next step owner: Gate: gates/SOLR-11678-sec-r1-07-keystore-reading.md (vm2).
- **SOLR-18368** (solr-18368-submit, tip a7ec9a1b65c0): Owner item: the key choice at opening. Round 2 flags banner option (a), "no key, Jira line deleted", as a shape the receipt does not name; confirm (a) before opening. Next step owner: Owner item.

### Spellcheck

- **SOLR-10252** (solr-10252-submit, tip a2be0f3adfe6): Owner item: the round 5 decision (comment-only versus a field change) is open; two wording corrections ride it. No gate applies (docs and config only). Next step owner: Owner item.

### Suggester

- **SOLR-9637** (solr-9637-submit, tip c50fa4ffd932): Owner item: the submission shape (submit SOLR-17393 first, fold both into one PR, or rework onto plain main). Gate green at the tip. Next step owner: Owner item.
- **SOLR-10937** (solr-10937-submit, tip 7d0cd11bafdc): Owner item: docs-only or code for the tmpdir lookups. No gate on record, and the tip has moved since that record. Next step owner: Owner item.
- **SOLR-11844** (solr-11844-submit, tip cbd08c20e9a4): Owner item: whether a ref-guide note is an acceptable resolution; the premise check against the Lucene formula is also owed. No gate on record. Next step owner: Owner item.

### eDisMax

- **SOLR-2309** (solr-2309-submit, tip 06f5a1c4a87e): Review owed: the eDisMax final pass is in flight and has not returned a verdict for this draft. Next step owner: Review: assignments/pool-edismax-final-review-round-1.md.
- **SOLR-2988** (solr-2988-submit, tip d2d144dfd9f4): Review owed: the eDisMax final pass is in flight and has not returned a verdict for this draft. Next step owner: Review: assignments/pool-edismax-final-review-round-1.md.
- **SOLR-3243** (solr-3243-submit, tip 1db99c13662d): Review owed: the eDisMax final pass is in flight and has not returned a verdict for this draft. Next step owner: Review: assignments/pool-edismax-final-review-round-1.md.
- **SOLR-3729** (solr-3729-submit, tip 0fe7e503945c): Review owed: the eDisMax final pass is in flight and has not returned a verdict for this draft. Next step owner: Review: assignments/pool-edismax-final-review-round-1.md.
- **SOLR-3923** (solr-3923-submit, tip 723f61ee4ab7): Hold: the draft carries a HOLD banner. The head skips only unsigned lone parentheses; +( and -) still reach the phrase-field text, so a branch fix comes first, then a focused gate at the new head, then the premise run. Next step owner: Gate: gates/SOLR-3923-premise.md (OPEN), after the branch fix.
- **SOLR-3962** (solr-3962-submit, tip e7d5f3505035): Review owed: the eDisMax final pass is in flight and has not returned a verdict for this draft. Next step owner: Review: assignments/pool-edismax-final-review-round-1.md.
- **SOLR-4362** (solr-4362-submit, tip e96a057439c8): Hold: held on a branch defect from the draft fidelity round; the eDisMax final pass excludes it. Next step owner: Main side (branch defect).
- **SOLR-6009** (solr-6009-submit, tip a41bb034a1f4): Review owed: the eDisMax final pass is in flight and has not returned a verdict for this draft. Next step owner: Review: assignments/pool-edismax-final-review-round-1.md.
- **SOLR-6320** (solr-6320-submit, tip cb710c963531): Review owed: the eDisMax final pass is in flight and has not returned a verdict for this draft. Next step owner: Review: assignments/pool-edismax-final-review-round-1.md.
- **SOLR-7120** (solr-7120-submit, tip 4664014a919c): Review owed: the eDisMax final pass is in flight and has not returned a verdict for this draft. Next step owner: Review: assignments/pool-edismax-final-review-round-1.md.
- **SOLR-12092** (solr-12092-submit, tip ca9573dabd38): Review owed: the eDisMax final pass is in flight and has not returned a verdict for this draft. Its receipt refresh also still owes a sentence stating the consequence at the gated head. Next step owner: Review: assignments/pool-edismax-final-review-round-1.md.
- **SOLR-14638** (solr-14638-submit, tip 44cf1c8fc805): Parked. No gate on record and no draft. Next step owner: Owner hold (parked).
- **SOLR-14913** (solr-14913-submit, tip b80221f46d3c): Review owed: the eDisMax final pass is in flight and has not returned a verdict for this draft (gate evidence carries over the comment-only tip commit). Next step owner: Review: assignments/pool-edismax-final-review-round-1.md.

### Highlighting

- **SOLR-2632** (solr-2632-submit, tip 1d7018f3fd7b): Hold: test-only pin draft; the premise is spurious on main (bytecode-verified), and no gate applies by design. Next step owner: Owner (pin stands as drafted).
- **SOLR-16885** (solr-16885-submit, tip 2b9b80119a92): Hold: the premise did not reproduce; the route is a Jira comment, not a PR. The comment text is drafted. Next step owner: Owner (Jira comment route).

### Admin UI

- **SOLR-9818** (solr-9818-submit, tip 63f2d7ce9267): Hold: the premise holds in part; the premise run is deferred (Windows). Next step owner: Owner hold (deferred run).
- **SOLR-9831** (solr-9831-submit, tip f269a70e84f0): Hold: the premise holds and the fix is cosmetic; the premise run is deferred (Windows). Next step owner: Owner hold (deferred run).

### Update processing

- **SOLR-16130** (solr-16130-export-join, tip 632e0340b0a8): Variant branch solr-16130-export-join, superseded by the open PR branch for SOLR-16130. Not an opening. Next step owner: None (variant).

## Owner items for today

Each item needs the owner's word. The first set is the one the close-out records name for this morning; the second set collects the further items the census follow-up rounds raised.

### Named for this morning

1. **SOLR-17680 and SOLR-9155 draft openings.** Both stand READY FOR OWNER APPROVAL in the SolrCloud slate above, unchanged since slate round 1. Approving the SolrCloud slate opens them as drafts.
2. **SOLR-17372 retire word.** The retire round's premise check returned NOT SUPPORTED and the gate failed at the live tip; the branch waits on the retire word. Owner ruling (2026-10-14): retired. The branch is labeled retired in its records and left in place, not deleted; this item is discharged. See `receipts/SOLR-17372.md`.
3. **The PR #5106 reply post (SOLR-16630).** The reply to Tomas Lobbe is drafted and held; nothing from the owner is posted on that PR. The reconstructed draft stands at `material/pr-5106-reply-draft-reconstructed.md` (marked reconstructed; the W5 second review checked its two reasons against the code). Posting needs its own explicit go.
4. **Proof verification date: mandatory or advisory.** The W1 ready lists flagged 13 drafts whose only hold was a missing verification date in Proof and applied the advisory reading. Owner ruling (2026-10-14): advisory. The 13 rows stand as openable as-is and the slate totals stand as printed; this item is discharged.
5. **SOLR-11356's packaged push.** The SolrJ settlement escalates this: the packaged head 6120dae28d04 was pushed to solr-11356-submit and the receipt records it, but the settlement found no owner yes for that push on record (a round 1 record says pushing waits for the owner's yes). Confirm the push, or direct a reversal. The header-copy gate has since landed green at the patched head 8b2e826154af (2026-10-14), so SOLR-11356 sits in the SolrJ slate as a marked row; the push question here is unchanged. Owner ruling (2026-10-14): pushes to Nick's own open PRs are always authorized, so the packaged push stands confirmed and this item is discharged.
6. **SOLR-12505's three holds.** (a) The Choice: defType versus terms parser routing. (b) The Unicode whitespace decision: keep the ASCII whitespace check as drafted, or widen it with \p{javaWhitespace} (the harder option; a code change with its own gate). (c) The JIRA attributions in the draft (Bernstein, Smiley, Pugh), which the review host could not verify against JIRA. The changelog title gate (gates/SOLR-12505-changelog-title.md, HOLD) waits on the Choice.
7. **SOLR-18368's key choice.** The PR carries the SOLR-18368 key or opens as a follow-up reference. Security round 2 also flags banner option (a), "no key, Jira line deleted", as a shape the receipt does not name; confirm (a) before opening.
8. **SOLR-18339's opening timing.** The receipt's last bullet names the SOLR-18336 precondition; the draft also poses the secured-node Choice. The CLI landing order puts SOLR-7924 after it.
9. **SOLR-17752's rebase direction.** Open against 56ec140e3636 as the branch stands, or rebase the published branch onto current main first (the adopted recommendation). The rerun job (gates/SOLR-17752-base-rebase-rerun.md) is packaged and HELD behind the direction, because the rebase rewrites a published branch.
10. **SOLR-9598's standing owner hold.** The draft is converged on text, carries the wait-by-default position and an OWNER NOTE, and stays held until the hold is lifted.
11. **PR #5010's CI paragraph: keep or cut.** The live body of PR #5010 (SOLR-12998) still carries its CI paragraph; the held-items decisions left the keep-or-cut call open.
12. **SOLR-15035 stays HOLD.** It waits on the owner's HOLD to READY settlement line (no record file carries it yet) and on the changelog YAML parse gate job, which the review coordinator files and vm2 runs. SOLR-11479, ready in substance, opens only after 15035 settles.

### Further owner items raised by the follow-up rounds

- **SOLR-14919 item (c):** the draft headline says forwarded commits fail whenever the processor is in the update chain; with ignoreOptimizeOnly=true a plain commit returns before the parse. The headline's scope is the owner's call, after its gate lands.
- **SOLR-15106:** keep, cite, or drop the ticket's 1200-message claim (no packet on the branch), and pick one length measure for the draft (4,671 bytes with link targets, 3,440 without, against the 3,500 guide).
- **SOLR-15386:** confirm the single-check position in the Choice, and the length (3,680 characters without link targets, over the guide).
- **Owner decision 8 (commit-body cleanup):** still open for SOLR-3865 and SOLR-4754; for SOLR-10234 the gate job rewrote a commit message, so the confirmation is asked before opening (slate rows for 3865 and 10234 wait on it).
- **SOLR-8767:** confirm the /get route the draft takes; the follow-up round reports it may override an earlier owner hold.
- **SOLR-8954:** decision 1, close the branch or make it a test-only follow-up with SOLR-8009 (a measured run confirms the breakage; its receipt's GATE GREEN line is stale and needs a main-side correction either way).
- **SOLR-18278:** retire or keep (the record recommends retire).
- **SOLR-14200 and SOLR-15326:** retire or keep; both drafts are written and both ticket scenarios do not fail on main.
- **SOLR-17433:** retire or ship (the audit recommends retire; the draft is SATISFIED and sits in the streaming slate as a marked row).
- **SOLR-10390:** withdraw as superseded by SOLR-18339, or keep the test as a guard.
- **SOLR-3684:** submit or retire (decision 1; the record recommends retire).
- **SOLR-16322:** owner decision 17 stays open; the draft carries the HOLD line.
- **SOLR-16290:** the pin disposition (pin PR, fund the real facet fix, or bank it).
- **SOLR-15358:** the default docValues-as-stored call; it lands after SOLR-15357 either way.
- **SOLR-16570:** which category holds it, and its ticket title against the SOLR-16611 change on main (its draft sits in the query parsing slate as a marked row).
- **SOLR-10641:** ship-alone against the DISCUSS item 11 still standing in the SolrCloud answers file (marked row in the SolrCloud slate).
- **SOLR-16155:** an existing PR covers the same change more widely; the opening call is the owner's (marked row in the search components slate).
- **SOLR-17841:** the retire call that holds its opening.
- **SOLR-17866:** submit ahead of the design conversation, or wait.
- **SOLR-9637:** the submission shape (SOLR-17393 first, one folded PR, or rework onto plain main).
- **SOLR-10937:** docs-only or code for the tmpdir lookups.
- **SOLR-11844:** whether a ref-guide note is an acceptable resolution.
- **SOLR-10252:** comment-only versus a field change (round 5 decision).
- **SOLR-15906:** the timing, once vm2 confirms or clears the reading that the branch breaks three tests.
- **SOLR-17055:** the proof-base choice (the coordinator named c2396f425fa2), because the fail-before leg is not established.
- **SOLR-9968:** confirm the 2026-10-09 top-up gate (13 tests) ran at the live head c31d2ff1a3f (marked row in the suggester slate).
- **SOLR-17987:** confirm the registry rename is deferred to opening packaging (marked row in the metrics slate).
- **SOLR-7119:** confirm owner decision 14 (drop the changelog); the follow-up round adopted it and filed the job (marked row in the build and docs slate).
- **SOLR-5394:** the changelog fragment type, fixed or changed (the draft is SATISFIED and in the slate; dev-docs reads "changed" for a modified default).
- **SOLR-8009:** confirm the Proof date 2026-10-05 (the round 1 ready list said the date was missing; the receipt carries it).
- **SOLR-17748:** whether the branch title should also cover the error-with-no-cause route (the draft covers it; the title names the one-pass route only).
- **SOLR-17155:** the Choice section has no options list or alternative cost; add one, or waive it.
- **SOLR-15331:** keep or override the word "lands" in Limits ("If SOLR-17051 lands first or in the same release").

## Sources and method

- Census dispositions and live tips: `reports/openings-census-round-1.md`, re-verified by `git ls-remote` during assembly (see the verification note at the top).
- Promotions since the census: `reports/census-followup-scheduled-round-1.md` to `round-5.md` (SATISFIED verdicts, each read against the receipt at the live tip), the CLI title gates for SOLR-9342, SOLR-16272 and SOLR-16813 (gate files and receipts at the landed heads), and the SOLR-12608 title gate (receipt at c16d49d84173).
- Ready-list bars and packaging verification: `reports/category-ready-lists-round-1.md`.
- SolrCloud slate states: `reports/solrcloud-openings-slate-round-2.md`.
- Packaging items: `reports/solrj-convergence-round-2-response.md` (PK-1 to PK-5), `reports/security-convergence-round-1-response.md` (SOLR-10627), `reports/streaming-convergence-round-2-response.md`, `reports/build-docs-convergence-round-2-response.md`, `reports/cli-convergence-round-2-response.md`, `receipts/SOLR-12556.md`.
- One-line change summaries are the bold summary line of each draft's "What this change does" section, shortened only by removing the trailing period.

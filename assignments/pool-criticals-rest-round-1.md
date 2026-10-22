# Pool assignment: the remaining Critical tickets, priority round 1

Status: OPEN

- Posted by: the phone agent, 2026-10-22, at the owner's direction. This assignment exists so the priority order is written down in one place for every agent.

## The priority list (owner, 2026-10-22)

Contributions prioritize new development and Blocker and Critical items, in this order:

1. The 13 unworked Blockers: `assignments/phone-agent-2-blockers-round-1.md` (directed to Phone Agent 2).
2. Live Critical work already in flight: SOLR-14200 and SOLR-16155 (worked from the phone agent's side; not part of this round).
3. The 58 unworked Critical tickets below (this round).
4. Critical tickets that already have work records move through their own records (follow-ups and reviews of existing work first, per pool/work-stores.md).
5. Everything else in the backlog follows in the standing pull order.

Coverage behind this list (slate audit, 2026-10-22): of the 100 open Blocker and Critical tickets, 29 have any work record and 71 have none. The 58 below are the Critical half of the unworked set; none has an item or a work product of its own. Two precision notes: SOLR-6213 is named inside SOLR-11787's skip item as a probable duplicate target, and SOLR-12801 inside SOLR-13416's note; read those notes when you reach them, they are context, not coverage. SOLR-13360, SOLR-17370, SOLR-17767 and SOLR-17788 have a PR available: read it first and classify against it instead of duplicating it.

## Claim

Per slice, `claims/pool-criticals-rest-round-1-<NN>.md`, first post wins. Any Linux host may claim a slice; one slice per host at a time. Work a slice in the order listed (oldest first).

## Per-ticket work (evidence first; a classification without this evidence does not count)

1. Re-read the ticket on Jira in this pass: status, description, comments, linked tickets, PRs and commits. The CSV snapshot is not evidence.
2. Check the issue against current upstream main (or the branch line the ticket targets): read the code path the ticket names, with file and line. Where the ticket describes reproducible behavior, attempt the reproduction and record the result, including the seed where one applies.
3. Classify: STILL-PRESENT, ALREADY-FIXED (name the fixing commit or PR), OBSOLETE (say why: API removed, feature replaced, report against an unshipped version), or NOT-REPRODUCIBLE (record exactly what was tried). Where the pool's WON'T DO rule applies (already done, already claimed and live elsewhere, structurally unfinishable as reported), refuse and clear it in the same push with the evidence, per pool/advisory-wont-do.md.
4. For STILL-PRESENT: implement the fix on a branch named for the ticket and run the full hardening gate at its head on your host; the gate's module check is sufficient. Write the receipt in `receipts/`. A PR draft per pr-formula.md may be written to `drafts/`; opening stays with the owner.
5. Premise bar (owner, 2026-10-21): a fix is very narrow. Do not harden a working path against a hypothetical failure. Where a proposed fix fails that bar, the verdict and the evidence are the product, with a brief closure-comment draft in the report for the owner.

## Standing rules that bind this round

- Commits as Nick Shanin; no "Claude" in authors, committers, or trailers; no em dashes in authored text.
- New tests extend SolrTestCase or SolrCloudTestCase, never SolrTestCaseJ4 (pool/guides/ carries the conversion rules).
- One Gradle build at a time under the shared build lock; the disk guard pauses work at or above 85 percent on the home volume.
- Changelog titles containing ": " are quoted in the YAML fragment, and the fragment parses before the gate counts.
- Do not lower a classification to clear a slice faster; an honest NOT-REPRODUCIBLE with the attempts recorded is a finished item.

## Deliverable

Per slice: `reports/pool-criticals-rest-round-1-<NN>.md`, one section per ticket with its classification and the evidence pointers (Jira read date, file and line, reproduction result, fixing commit, or the gate receipt for built items), plus a summary table. The slice claim goes DONE in the same push as its report. Partial delivery per ticket is fine: finished classifications beat a silent round.

Limits: no Jira or GitHub posts or comments, no PR openings or ready flips, no force push except a rebuild of a branch this round created, pushed with lease.

## Slice 01 (25 tickets, oldest first)

- SOLR-2908: To push the terms.limit parameter from the master core to all the shard cores
- SOLR-4165: Queries blocked when stopping a node
- SOLR-4180: StatisticsHandler multicore support
- SOLR-4285: Solr mangles distributed query parameters
- SOLR-4690: Highlighting doesn't work when boost is used along with query
- SOLR-5735: ChaosMonkey test timeouts
- SOLR-6213: StackOverflowException in SolrCloud's leader election
- SOLR-6345: collapsingQParserPlugin degraded performance when using tagging
- SOLR-6420: ADDREPLICA can add a replica that reports green without actually replicating
- SOLR-6683: Need a configurable parameter to control the doc number between peersync and the snapshot pull recovery
- SOLR-6816: Review SolrCloud indexing performance
- SOLR-7351: MoreLikeThis component doesn't work with dates
- SOLR-7889: Secure ZooKeeper should be easy and the default
- SOLR-7926: Hit highlighting with EdgeNGramFilterFactory
- SOLR-8028: ConcurrentModificationException thrown from JavaBinCodec
- SOLR-8096: Major faceting performance regressions
- SOLR-8331: CorruptIndexException after auto commit
- SOLR-8417: HttpSolrCall infinite loop request process if the collection is totally not loaded
- SOLR-8887: Solr security features cannot export the internal, deprecated DefaultHttpClient class as part of their user-facing API
- SOLR-8905: AutoAddReplicas feature has bug that local coreproperties file was deleted by mistake
- SOLR-9138: CDATA should be requisite for text/string field type in xml-format result set
- SOLR-9170: Need detailed diagnostics for ZooKeeper connection issues
- SOLR-9956: ArrayIndexOutOfBoundsException when indexing large amount of documents
- SOLR-10928: Support elevate.q in QueryElevationComponent
- SOLR-10950: Support context filtering for FuzzyLookupFactory

## Slice 02 (25 tickets)

- SOLR-11330: Solr arabic search
- SOLR-11668: Lookup not supported at this time / StackOverflowError after upgrading from 6.3.0
- SOLR-11972: Range of numeric values with unit in Solr
- SOLR-12069: Default operator parameter q.op ignored
- SOLR-12415: LBHttpSolrClient not working as expected when a Solr node goes down
- SOLR-12801: Fix the tests, remove BadApples and AwaitsFix annotations, improve env for test development
- SOLR-13360: StringIndexOutOfBoundsException: String index out of range: -3 (PR available)
- SOLR-13460: Exception writing document id (Sitecore URL id); possible analysis error
- SOLR-13770: Solr fails to recover replica(s) after restart due to deep nested directories created during recovery inside TLOG
- SOLR-13789: Solr core reload with config change upstream puts Solr instance in bad state
- SOLR-13850: Atomic updates with PreAnalyzedField
- SOLR-14311: Shared schema should not have access to core-level classes
- SOLR-14346: Solr fails to check zombie server in LbHttpSolrClient
- SOLR-14593: Package store API to disable file upload over HTTP
- SOLR-14649: Package manager should use SHA512, not SHA1
- SOLR-14788: Solr: The Next Big Thing
- SOLR-14833: Empty highlight entry on match only for some queries
- SOLR-14996: Facet incorrect counts when FQ exclusion applied with collapsing
- SOLR-15096: [REGRESSION] Collection delete performance significantly degraded in Java 11 vs 8
- SOLR-15412: Strict validation on replica metadata can cause complete outage
- SOLR-15596: Ability for packages to be upgraded over major versions
- SOLR-15759: Clean cloud collection terms
- SOLR-15998: replicaType=TLOG OOM killer event occurs
- SOLR-16100: Solr tool does not work with custom zkACLProvider
- SOLR-16139: [Regression] JSON stat facet functions not working on analysed String (SortableTextField)

## Slice 03 (8 tickets)

- SOLR-16509: All query classes should be immutable
- SOLR-17122: Solr Unique, HLL, numBuckets returns incorrect counts
- SOLR-17291: Error opening new searcher caused by indexDeletionPolicy must not be null
- SOLR-17370: Search with facets throws IllegalAccessException if some string fields contain numeric values (PR available)
- SOLR-17375: Close IndexReader asynchronously on commit for performance
- SOLR-17762: Apache Solr 9.8.1 configuration issues on 2 VM setup
- SOLR-17767: SIP-24: Java Security Manager replacement (PR available)
- SOLR-17788: Cores hang during full index bootstrap in leader/replica mode (PR available)

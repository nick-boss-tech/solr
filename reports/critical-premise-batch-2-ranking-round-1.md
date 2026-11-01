# Critical premise batch 2, ranking round 1

Source: the 65-ticket batch-2 pool in
`critical-premise-batch2-2026-11-01-state.md`, the 9 batch reports
(`reports/critical-comments-round-1-batch-01.md` through `-09.md`),
the round-1 implementation triage
(`reports/critical-implementation-triage-round-1.md`), and the census
(`material/critical-tickets-census-2026-10-24.md`). Ticket
descriptions for the shortlist were re-read live from Jira on
2026-11-01 for this ranking; no Jira or GitHub write was made.

Rank test, in order: the ticket states a concrete failure (not a
feature or a semantics question); reproduction is cheap on a single
node or by a direct code trace; the fix surface looks small; no open
PR or owner branch collides. Batch-01's four `reproduce-first`
verdicts (5821, 5850, 7351, 7926) were weighed against the same
test; 7351 already has its own batch-2 unit, 5821 needs a cluster
run and already has a docs-only fork branch, and 5850's named class
is gone, so only 7926 of the four ranks.

## Ranked 8 (units 5 to 12, in order)

### 1. SOLR-13360 (unit 5)

- Defect: `/suggest` with `spellcheck.collate=true` throws
  `StringIndexOutOfBoundsException: String index out of range: -3`
  from `SpellCheckCollator.getCollation` (exact stack in the ticket).
- Why it ranks: an exact stack plus a one-request repro on a single
  node; the fix surface is a bounds case in one collator method.
- Premise plan: index a small suggest/spellcheck core, run the
  ticket's suggest query with collation, confirm or kill the -3
  exception on current main.
- Collision: none open. Closed PRs #3112 and #210 by another
  contributor are not live collisions; the premise run checks
  whether either closed approach still matches the current code.

### 2. SOLR-7926 (unit 6)

- Defect: with `EdgeNGramFilterFactory`, hit highlighting marks the
  whole word instead of only the matched part.
- Why it ranks: batch-01's `reproduce-first` verdict; a single-node
  highlighting test with an EdgeNGram field type; small surface in
  the highlighter path.
- Premise plan: reproduce the original EdgeNGram case separately
  from the later FastVectorHighlighter workaround and open
  LUCENE-7016, which batch-01 found is not necessarily the same
  symptom.
- Collision: none (no owner branch, receipt, or PR).

### 3. SOLR-8028 (unit 7)

- Defect: `ConcurrentModificationException` from
  `JavaBinCodec.writeArray` while marshalling a `SolrInputDocument`
  under pressure test (exact stack in the ticket).
- Why it ranks: the failing method is named; a concurrent-marshal
  probe plus a read of the write path can confirm or kill the
  premise even if the race itself is timing-dependent; the fix
  surface is the marshal path for document field lists.
- Premise plan: marshal shared documents concurrently and read the
  current `JavaBinCodec` write path; settle the batch-02 open
  question of whether a reused document instance is the trigger.
- Collision: none (batch-02: no PR; the 2017 instance-reuse
  question is unanswered).

### 4. SOLR-16725 (unit 8)

- Defect: after `action=RESTORE`, CLUSTERSTATUS reports
  `pullReplicas`, `replicationFactor`, and sibling values as ints
  where a CREATE-built collection reports them as strings.
- Why it ranks: a concrete output-shape mismatch with both shapes
  quoted in the ticket; traceable to the restore path that builds
  collection properties versus the create path; small surface
  (normalize the types at one of the two sites).
- Premise plan: run CREATE versus RESTORE on current main and
  diff the CLUSTERSTATUS types for the restored collection.
- Collision: none (batch-07: no PR; scoping shape is about matching
  types versus documenting them, which the premise run settles by
  showing which side diverges).

### 5. SOLR-4690 (unit 9)

- Defect: highlighting stops working when a boost is added to the
  query (edismax with `qf`/`pf` plus a `boost` parameter); removing
  the boost restores highlighting.
- Why it ranks: a single-node highlighting test; the premise run
  first has to separate a real defect from a query-construction
  artifact (`hl.q` versus `q`), which is why it sits below the
  four above rather than above them.
- Premise plan: replay the ticket's request shape with and without
  the boost on current main and compare the highlighting section.
- Collision: none (batch-02: no PR; a 2016 workaround comment is
  cited, not a fix).

### 6. SOLR-14346 (unit 10)

- Defect: `LbHttpSolrClient` never sends queries to a restarted
  server; the zombie-server check does not revive it (exact
  three-step repro in the ticket, Solr 8.3.1).
- Why it ranks: exact repro steps and a named check to trace
  (zombie revival); client-level test with mock servers avoids a
  full cluster; small surface in the load-balancer client.
- Premise plan: two-server client test, kill one, restart it, and
  check whether traffic resumes after the zombie interval.
- Collision: none (batch-05: no PR; a 2021 base-URL workaround is
  cited, not a fix). Note SOLR-12415 is a neighbouring
  LBHttpSolrClient complaint; if the premise run finds one root
  cause covering both, the unit 10 report says so.

### 7. SOLR-11668 (unit 11)

- Defect: `FuzzyLookupFactory` with `DocumentDictionaryFactory`
  throws `StackOverflowError` building the dictionary, and the
  FreeText combination reports "Lookup not supported at this time"
  after upgrade (exact config in the ticket).
- Why it ranks: exact suggester config plus a single-node
  suggester-build test; the premise run confirms or kills the
  overflow on current main.
- Premise plan: build the ticket's suggester on current main and
  watch for the overflow; a Lucene-side root cause is possible,
  which would cap the fix surface and be reported as such.
- Collision: none (batch-03: no PR).

### 8. SOLR-11310 (unit 12)

- Defect: QueryElevation does not work with Solr LTR: with
  `elevateIds` and an LTR `rq` rerank, the elevated document is
  not always first because the reranker ignores elevations.
- Why it ranks: a concrete ordering failure with a 2020
  reproduction on 8.3; the fix surface is the elevation/rerank
  interaction, one ordering decision.
- Premise plan: elevation plus LTR rerank on current main, check
  whether the elevated document holds first place.
- Collision: none (batch-04: no PR; the 2020 comment reproduces it
  on 8.3).

## Considered and not ranked

- SOLR-16914 (Japanese synonyms): concrete, but the analysis chain
  is long and the failure may be configuration; first alternate if
  a ranked unit dies on its own terms without a verdict.
- SOLR-15237 (distributed search with basic auth, 2024 repro on 9.0
  and 9.5): concrete and recent, but needs a distributed plus auth
  setup, so it costs more than every ranked unit.
- SOLR-15412 (strict replica-metadata validation outage): the
  ticket asks to reduce the impact of SOLR-14245, which is a
  scope/design shape as much as a defect; a premise run would need
  an upgrade-path repro.
- SOLR-8767 (RealTimeGet and copyField exclusion) and SOLR-13850
  (atomic updates with PreAnalyzedField): both are scoping shapes
  (what the component should return/require), not stated failures.
- SOLR-5821 (replica search inconsistency): batch-01
  `reproduce-first`, but needs a cluster run to separate tie-order
  variance from count divergence, and a docs-only fork branch
  (`solr-5821-submit`) already touches the ticket.
- SOLR-5850 (ConcurrentUpdateSolrServer race): batch-01
  `reproduce-first`, but the named class was removed in SOLR-6895;
  the current-path analogue is a race repro, timing-dependent.
- SOLR-16509 (query classes immutable): Patch Available with open
  PR #1146 by another committer; that is a live collision under
  the rank test, so it is excluded despite the patch.
- SOLR-17788 (cores hang during bootstrap): cluster-scale hang
  repro with a closed PR #3389; expensive and unclear.
- SOLR-17281 (down replica elected leader), SOLR-8905 (AutoAddReplicas
  deletes coreproperties), SOLR-4165 (queries blocked stopping a
  node), SOLR-4285 (mangled distributed params): all cluster or
  lifecycle repros, costlier than the ranked eight.
- SOLR-12743 (memory leak), SOLR-15096 (collection-delete
  performance), SOLR-8096 (faceting performance), SOLR-3684 (full GC
  under indexing pressure): performance or leak claims, expensive
  to prove and large of surface.
- The rest of the pool is Improvements, Tasks, Wishes, New Features,
  scoping shapes, or stale-checks with no stated current failure.

## Queue effect

Units 5 to 12 in the state file, in the ranked order above. A
reproduced premise turns the next unit into that ticket's
implementation, per the state file's queue rule, and the ranking
queue resumes after it.

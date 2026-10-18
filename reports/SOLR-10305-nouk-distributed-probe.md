# SOLR-10305: distributed requests without a uniqueKey, probe round 1 (vm3)

Assignment: `assignments/pool-solr-10305-nouk-distributed-probe-round-1.md`. Claim: `claims/pool-solr-10305-nouk-distributed-probe-round-1.md`.
Host: vm3. Seed for every run: `10305C0FFEE10305`. No branch, PR, comment or JIRA change.

## Head

- `solr-10305-submit` at claim time: `52e32f892c5e8ccdb474c8fc222ce8203955efd3`, matching the assignment. Re-checked before the head run, unchanged.
- Base: `cabedd1d968059215188f4e7563fb303241899ed`, present locally.
- Probe source: `gates/SOLR-10305-probe-cloud-test.java.txt`. The vm2 probe files named in the assignment (`gates/SOLR-10305-probe-cloud-test.java.txt` and `gates/SOLR-10305-probe-results.txt`) were not on coordination or on this host, so the probe was rebuilt from the assignment's scope. The same probe file and configset were used at both revisions.
- Schema: the `cloud-minimal` configset with the single `<uniqueKey>id</uniqueKey>` line removed (test-files configset `probe-nouk`). One SolrCloud node, one shard, one replica.
- Raw results: `gates/SOLR-10305-probe-results.txt` (both revisions, one `PROBE` line per shape).

## Runs

| Run | Revision | Result | Notes |
| --- | --- | --- | --- |
| 10305-probe-head-r1 | head | compile failed, no tests run | two probe API errors (client builder, replica lookup); fixed in the probe |
| 10305-probe-head-r2 | head | compile failed, no tests run | wrong package for `HttpJettySolrClient`; fixed |
| 10305-probe-head-r3 | head | ran, rc 0 | the probe printed a false `NullPointerException` for S02 from its own `getResults()` call; print made null-safe |
| 10305-probe-head-r4 | head | ran, rc 0, final head result | `tests=1 failures=0 errors=0` |
| 10305-probe-base-r1 | base | ran, rc 0, final base result | `tests=1 failures=0 errors=0` |

Logs: `/workspace/gates/logs/vm3/<run>.log`, JUnit XML under `/workspace/gates/logs/vm3/<run>.xml/`, headroom under `/workspace/gates/logs/vm3/<run>.headroom.log`. Headroom at head r4: `/` 4.6G free, 481219 free inodes before, 481218 after. At base r1: 481218 before, 481215 after. The probe asserts nothing; the rc 0 means it ran to completion, and the verdict comes from the `PROBE` lines.

## Shape results

All distributed requests go straight to the core URL with `shards=<that core URL>`. The cloud client is not used.

| Shape | Request | Base | Head |
| --- | --- | --- | --- |
| S01 | no shards, `q=subject:Hoss` | 200 | 200 |
| S02 | no shards, `query=false` | 200 | 200 |
| S03 | no shards, `distrib=false` | 200 | 200 |
| S04 | shards, `q=subject:Hoss` | 500 NPE | 400 uniqueKey |
| S05 | shards, `q=*:*` | 500 NPE | 400 uniqueKey |
| S06 | shards, `fl=id,score`, `rows=0` | 500 NPE | 400 uniqueKey |
| S07 | shards, `query=false` | 500 NPE | 400 uniqueKey |
| S08 | shards, `rows=0` | 500 NPE | 400 uniqueKey |
| S09 | shards, `sort=subject asc` | 500 NPE | 400 uniqueKey |
| S10 | shards, `facet=true&facet.field=subject`, `q=*:*` | 500 NPE | 400 uniqueKey |
| S11 | shards, `distrib=false` | 500 NPE | 400 uniqueKey |
| S12 | shards, `shards.info=true`, `rows=0` | 500 NPE | 400 uniqueKey |
| S13 | shards, `fl=id` | 500 NPE | 400 uniqueKey |
| S14 | shards, no-hit `q=subject:nomatch` | 500 NPE | 400 uniqueKey |

The head 400 text for S04 through S14 is "Distributed search requires a uniqueKey field in the schema", raised on the server.

## Base server check

The base failures are server-side, not a client artifact. The 500 response carries the NullPointerException text, and the server log in the XML shows the first frame in the request path:

- S04, S05, S06, S08 through S14 (10 shapes): `NullPointerException: Cannot invoke "org.apache.solr.schema.SchemaField.getName()" because the return value of "org.apache.solr.schema.IndexSchema.getUniqueKeyField()" is null`. First frame `QueryComponent.createMainQuery(QueryComponent.java:793)`, called from `QueryComponent.regularDistributedProcess` (line 646) and `distributedProcess` (line 596).
- S07 (`query=false`): `NullPointerException: Cannot invoke "org.apache.solr.search.SortSpec.includesScore()" because the return value of "org.apache.solr.handler.component.ResponseBuilder.getSortSpec()" is null`. First frame `QueryComponent.createDistributedStats(QueryComponent.java:776)`, from `regularDistributedProcess` (line 639). Here `query=false` lets the request reach the stats step without a sort spec, so the NPE lands one step later than the uniqueKey NPE.

The NPE is therefore on the shard path inside the server, at the same point the claim describes. This agrees with the in-process result already recorded in `receipts/SOLR-10305.md` (base proof: the two uniqueKey tests fail with NPE from `IndexSchema.getUniqueKeyField()` returning null). That receipt was not re-run here.

No `No live SolrServers` error appeared. That client-side symptom, noted by vm2, comes from the cloud client path, which this probe does not use. Going straight to the core URL shows the server response directly. I did not reproduce the cloud-client variant, so I make no claim about it.

## Harness check

- Seeding fails at both revisions: adding a document to the no-uniqueKey collection throws `NullPointerException` from `NestedUpdateProcessorFactory` because `getUniqueKeyField()` is null. Head and base show the same error. So the collection is empty (`numFound=0` on S01 and S03 at both revisions).
- Because the collection is empty, the shapes test status codes and error paths, not result contents.
- The probe changes are test-only, in detached worktrees (`/workspace/worktrees/solr-10305-head` and `/workspace/worktrees/solr-10305-base`), not committed.

## Success-path shapes

The assignment asked for shapes likely to succeed on base with a distributed path. S06, S08, S12 and S13 were the candidates (`rows=0` and `fl=id` variants). None succeeded on base; all returned 500 NPE. So no distributed request succeeded on base in this probe.

## Verdict

- Failing half, "a distributed request without a uniqueKey fails on base": PROVEN for the 11 distributed shapes tested. Every one returns 500 with a server-side NPE on base and 400 with the uniqueKey message at head.
- Non-rejection half, "the change does not reject any distributed request that succeeded on base": NOT PROVEN. This probe found no distributed request that succeeds on base, and seeding failed, so no success case with data exists here. The only shapes that succeed on base (S01 to S03) are not distributed and succeed at head too.
- Decision for the claim (main side, per the Decisions table): restore only the failing-half wording, for example "a distributed request on a schema without a uniqueKey returns 500 (NullPointerException on the shard, `QueryComponent.createMainQuery`) on base and 400 at head". Do not restore the non-rejection wording on this evidence. Any PR body edit needs Nick's approval.

## Limits

- No distributed success case with data was tested. A success-path test would need a live shard with indexed documents, which cloud mode cannot seed without a uniqueKey. Standalone or in-process indexing works (MinimalSchemaTest does it), but a live shard for a distributed path needs a separate harness. Not built in this round.
- One node and one shard. Multi-shard routing was not exercised.
- Single seed and single run per revision.

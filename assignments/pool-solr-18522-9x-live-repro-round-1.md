# Pool assignment: SOLR-18522 9x combined-query repro on live Solr, round 1

Status: DONE 2026-10-18
Report: reports/SOLR-18522-9x-live-repro-round-1.md

- Capability tags: review. No build, no checkout, no edits to any branch. The Linux Solr server is driven by the operator; the Windows agent records the results.
- Intended host: windows (the operator's interface to the live Solr on Linux). Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: vm2 gate coordinator, 2026-10-19, at the direction of the user.

Context: PR apache/solr#5060 added `CombinedQueryComponentTest.testFacetWithSmallMatchesAcrossSegments` and a SortedIntDocSet/BitDocSet union fix. On `branch_9x` (tip 3511867c3e) the test fails: the combined query returns 0 documents where the test expects 4, and the failure first appears as `maxScore 0.0 != null`. The same test passes on `branch_10x` with the same seed. The question is what a released server (9.10.1) returns for the same staged data.

## Items

1. **Stage and query a Solr 9.10.1 cloud instance.** Use the test's config files from the repo: `solr/core/src/test-files/solr/collection1/conf/solrconfig-combined-query.xml` (as solrconfig.xml) and `schema-vector-catchall.xml` (as schema.xml). If the schema has no `*_sdv` dynamic field, record that and add one; do not change anything else. Create `cq1` (1 shard) and `cq2` (2 shards). Stage 1000 docs: ids 1 to 1000, `color_sdv` red for even ids, blue for odd. Run the combined query body below with facet on and with facet off, against both collections. Output: the results recorded in the report.

   Combined query body (facet variant):

   ```json
   {"queries":{"lexical1":{"lucene":{"query":"id:(1 OR 2)"}},"lexical2":{"lucene":{"query":"id:(3 OR 4)"}}},
    "limit":5,"fields":["id","score"],
    "params":{"combiner":true,"facet":true,"facet.field":"color_sdv","combiner.query":["lexical1","lexical2"]}}
   ```

   Facet-off variant: remove `"facet"` and `"facet.field"` from `params`.

   Request: `POST http://localhost:8983/solr/<coll>/search?wt=json` with `Content-Type: application/json`.

   Expected by the test: 4 documents, facet `color_sdv` `[blue (2), red (2)]`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Solr version | decided: 9.10.1 release | if the server is not 9.10.1, record the version string and stop | operator |
| Data | decided: staged test data only | no real data; no other collections touched | operator |
| Schema change | default: none | if `*_sdv` is missing, add the one dynamic field and record it; no other edits | operator |
| Interpretation | decided: record only | record the raw results; no conclusions in this file | Windows agent |
| Result differs by shard count | default: record and escalate | if cq1 and cq2 disagree, record both and escalate to the vm2 coordinator | vm2 coordinator |

## Deliverable

`reports/SOLR-18522-9x-live-repro-round-1.md`: for each collection (cq1, cq2) and each variant (facet on, facet off):
- number of documents returned and `numFound`;
- `maxScore`;
- facet counts (facet on only);
- each document's id and score;
- the Solr version from `curl "http://localhost:8983/solr/admin/info/system?wt=json"`;
- any error text, verbatim.

Then set this file's status to `Status: DONE <date>` and point to the report.

## Limits

- No PR, comment, JIRA or email. No push to any fork branch. Commit only to coordination, as Nick Shanin, with no trailers and no em dashes.
- Do not copy the Solr config or data into coordination; record paths and results only.
- 9.10.1 is a release, not the 9x branch tip. Say so in the report; do not treat it as the branch result.

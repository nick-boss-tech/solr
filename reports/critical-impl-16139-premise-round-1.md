# SOLR-16139 premise check: nested JSON stat facet over SortableTextField (round 1, vm4)

Verdict: premise REPRODUCES on apache/solr main `ee2f1bf2e0109956145d7fd995da63c4091a589f`. A nested terms facet over a `SortableTextField` returns wrong stat values (0.0 for avg and sum, null for min) with no error. The same nested stats over a plain string field are correct. Step 2 (implementation) is permitted by the assignment and continues on this claim.

Assignment: `assignments/critical-impl-16139-round-1.md`, step 1. Claim: `claims/critical-impl-16139-round-1-vm4.md`. Source triage: `reports/critical-implementation-triage-round-1.md`, rank 3.

## Setup

- Host vm4, JDK 21.0.12.1, `env -u NODE_ENV`.
- Standalone shallow clone of apache/solr main at `ee2f1bf2e0109956145d7fd995da63c4091a589f` at `/workspace/worktrees/s16139-clone`.
- Build: `:solr:packaging:assemble` through `pool/bin/sweep-run.sh`, run `s16139-assemble-ee2f1bf-20261027T100633Z`, rc 0 (94 tasks executed). Logs in `logs/vm4/critical-impl-16139/`.
- Packaged Solr `solr-11.0.0-SNAPSHOT`, `bin/solr start -p 8986`, collection `techproducts` from `sample_techproducts_configs`. Added two fields by the schema API: `sortable_text` (type `text_gen_sort`, a `SortableTextField`, single-valued) and `plain_s` (type `string`, docValues). Five documents with `sortable_text`, `plain_s`, and `price` (10, 20, 30, 50, 7); groups Alpha (ids 1, 2), Beta (3, 4), Gamma (5).
- The ticket's docker repro was not checked; this is the assignment's fallback shape. An earlier control batch without `price` replaced the documents and was discarded. Only the final, complete batch is used below.

## Query and results

Query: `q=*:*`, `rows=0`, `json.facet` with a terms facet on the field, and a nested `avg(price)`, `sum(price)`, and `min(price)`.

| Terms field | Bucket | count | avg(price) | sum(price) | min(price) |
| --- | --- | --- | --- | --- | --- |
| `plain_s` (string) | Alpha grp | 2 | 15.0 | 30.0 | 10.0 |
| `plain_s` (string) | Beta grp | 2 | 40.0 | 80.0 | 30.0 |
| `plain_s` (string) | Gamma grp | 1 | 7.0 | 7.0 | 7.0 |
| `sortable_text` (SortableTextField) | Alpha grp | 2 | 0.0 | 0.0 | null |
| `sortable_text` (SortableTextField) | Beta grp | 2 | 0.0 | 0.0 | null |
| `sortable_text` (SortableTextField) | Gamma grp | 1 | 0.0 | 0.0 | null |

No `error` in any response. A non-nested control, `json.facet={"a":"avg(price)"}` over all five documents, returns 23.4 (correct).

## Interpretation

The defect is in how stat functions read values under a terms bucket on a `SortableTextField` domain. The response is wrong data, not an exception, so the ticket's word "fail" describes it loosely. Whether the cause is the field's docValues form (the sortable copy) or the facet domain is not yet established. That is the next step, the implementation step.

## Limits

- One main tip, one configset (`sample_techproducts_configs` plus schema API fields), five documents. This is not a sweep and not the reporter's own docker setup.
- No test run and no source edit in this step. No PR, JIRA, or GitHub action.

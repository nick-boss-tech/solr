# SOLR-12069 premise check: q.op on the standard query parser (round 1, vm4)

Verdict: premise does NOT reproduce on apache/solr main `ee2f1bf2e0109956145d7fd995da63c4091a589f`. q.op is applied on the standard (lucene) query parser. Per the assignment, no implementation and no branch.

Assignment: `assignments/critical-impl-12069-round-1.md`, step 1. Claim: `claims/critical-impl-12069-round-1-vm4.md`. Source triage: `reports/critical-implementation-triage-round-1.md`, rank 2.

## Setup

- Host vm4, JDK 21.0.12.1 (`/workspace/tools/jdk21`), Gradle through the repo wrapper.
- Standalone shallow clone of apache/solr main at `ee2f1bf2e0109956145d7fd995da63c4091a589f` (not a worktree).
- Build: `:solr:packaging:assemble` through `pool/bin/sweep-run.sh`, run `q12069-assemble-retry-ee2f1bf-20261027T092631Z`, rc 0 (BUILD SUCCESSFUL in 5m 26s). Logs: `logs/vm4/critical-impl-12069/`.
- Attempt 1 (`q12069-assemble-ee2f1bf-20261027T090948Z`) failed at the js-client npm step because the launching shell had `NODE_ENV=production`; no tests ran. Logs kept under `logs/vm4/critical-impl-12069/attempt-1-npm-node-env/`.
- Packaged Solr `solr-11.0.0-SNAPSHOT`, started with `bin/solr start -p 8985` (SolrCloud mode, embedded ZooKeeper). Collection `techproducts` created with `bin/solr create -c techproducts -d sample_techproducts_configs --solr-url http://localhost:8985`.
- Indexed the XML example documents from `solr/example/exampledocs` (monitor, monitor2, mem, hd, ipod_other, ipod_video, manufacturers, mp500, sd500, vidcard, money) over HTTP. `numFound` for `*:*` is 29.

## Queries (techproducts, default parser)

| Query | Parameters | numFound | parsedquery |
| --- | --- | --- | --- |
| `q=a b` | `q.op=AND` | 0 | `+(+text:a +text:b)` |
| `q=a b` | `q.op=OR` | 1 | `text:a text:b` |
| `q=a b` | none (default) | 1 | `text:a text:b` |
| `q=a b` | `defType=lucene`, `q.op=AND` | 0 | `+(+text:a +text:b)` |
| `q=a b` | `defType=lucene`, `q.op=OR` | 1 | `text:a text:b` |
| `q=a b` | `defType=lucene`, none | 1 | `text:a text:b` |
| `q=ipod black` | `q.op=AND` | 1 | `+(+text:ipod +text:black)` |
| `q=ipod black` | `q.op=OR` | 4 | `text:ipod text:black` |

The ticket's two-word case behaves as the default operator says it should. In the second query, AND and OR give different parsed queries and different counts, so the operator is applied, not ignored.

## Limits

- Tested the techproducts example only, on one main tip, with the default configset. The ticket may describe another path (for example an explicit `q` in a different parser, a different default operator in a site configuration, or a non-techproducts configset). Those were not tested.
- Only the two query shapes above were run. This is a premise check, not a sweep.
- No test run, no source edit, no branch. No PR, JIRA, or GitHub action.

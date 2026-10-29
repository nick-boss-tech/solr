# Receipt: validation of the wt routing bug on a non-hosting node, upstream main (vm6, Linux), round 1

- Assignment: `assignments/solr-wt-non-hosting-node-round-1.md`, questions 1 to 3. Claim: `claims/solr-wt-non-hosting-validation-vm6.md`. Host: vm6 (Linux). The reviewer has no Gradle, so vm6 ran the probe and this receipt carries the evidence.
- Head: upstream `apache/solr` main `852b427c1154ce9eec988b9adaeb212294442126`, fetched at depth 1 into a scratch clone (`/workspace/tmp/wt-validate/main`). JDK `21.0.12.1+1`, Gradle through the gate runner, cleanTest then test for one class.
- Probe: `assignments/solr-wt-non-hosting-node-round-1/ProbeRoutingTest.java` on the board, with the reload legs added for this run. The copy used is the same file.
- Result: **reproduced on upstream main** for `POST /collections/{c}/reload` (a JAX-RS sub-response endpoint). The hosting node answers javabin; the non-hosting node answers JSON, and the client throws the mime error. Mechanism confirmed by temporary prints (below). Split cannot be tested on this head (no JAX-RS route). Rebalance-leaders is not a JAX-RS endpoint on this head either.

## Evidence (verbatim excerpts)

Full excerpts are below. The raw logs are on vm6 at `/workspace/gates/logs/vm6/wt-main-clean/` and `/workspace/gates/logs/vm6/wt-main-dbg/` (not reachable from other hosts).

```
# Excerpts from the upstream main probe, head 852b427c1154ce9eec988b9adaeb212294442126

## Run wt-main-clean (no prints), gate log /workspace/gates/logs/vm6/wt-main-clean/
PROBE split hosted collection node=127.0.0.1:34093_solr target=127.0.0.1:34093_solr
PROBE split hosted default request RemoteSolrException: Error from server at https://127.0.0.1:34093/api/collections/probeSplitHosted/shards/shard1/split?wt=javabin: org.apache.solr.common.SolrException: Cannot find 
PROBE split other collection node=127.0.0.1:46157_solr target=127.0.0.1:34093_solr
PROBE split other default request RemoteSolrException: Error from server at https://127.0.0.1:34093/api/collections/probeSplitOther/shards/shard1/split?wt=javabin: org.apache.solr.common.SolrException: Cannot find A
PROBE reload hosted collection node=127.0.0.1:34093_solr target=127.0.0.1:34093_solr
PROBE reload hosted default request OK
PROBE reload other collection node=127.0.0.1:46157_solr target=127.0.0.1:34093_solr
PROBE reload other default request RemoteSolrException: Error from server at https://127.0.0.1:34093/api/collections/probeReloadOther/reload?wt=javabin: Expected mime type in [application/octet-stream, application/vn

## Run wt-main-dbg (temporary prints in V2HttpCall and MediaTypeOverridingFilter, reverted afterwards), log /workspace/gates/logs/vm6/wt-main-dbg/
PROBE reload hosted collection node=127.0.0.1:44617_solr target=127.0.0.1:44617_solr
V2DBG setSqr id=1885360463 solrReqNull=false path=/solr/____v2/collections/probeReloadHosted/reload
V2DBG setSqr id=1794236515 solrReqNull=false path=/solr/____v2/collections/probeReloadHosted/reload
FILTERDBG path=collections/probeReloadHosted/reload sqr=true wt=javabin ct=application/openmetrics-text;charset=utf-8;version=1.0.0 id=1794236515 val=set
PROBE reload hosted default request OK
PROBE reload other collection node=127.0.0.1:46873_solr target=127.0.0.1:44617_solr
V2DBG setSqr id=1340597446 solrReqNull=true path=/solr/____v2/collections/probeReloadOther/reload
FILTERDBG path=collections/probeReloadOther/reload sqr=false wt=noparams ct=application/openmetrics-text;charset=utf-8;version=1.0.0 id=1340597446 val=null
PROBE reload other default request RemoteSolrException: Error from server at https://127.0.0.1:44617/api/collections/probeReloadOther/reload?wt=javabin: Expected mime type in [application/vnd.apache.solr.javabin, app

## Split on this head: no JAX-RS route answers the split path here (`Cannot find API for the path`), so split cannot test the wt behavior on this head:
PROBE split hosted default request RemoteSolrException: Error from server at https://127.0.0.1:34093/api/collections/probeSplitHosted/shards/shard1/split?wt=javabin: org.apache.solr.common.SolrExcepti
PROBE split other default request RemoteSolrException: Error from server at https://127.0.0.1:34093/api/collections/probeSplitOther/shards/shard1/split?wt=javabin: org.apache.solr.common.SolrException
```

## Code at this head (`852b427c11`, paths under `solr/core/src/java/org/apache/solr/`)

- `api/V2HttpCall.java:165-166`: a collection with no local replica sets `action = REMOTEPROXY`, then `ADMIN_OR_REMOTEPROXY`.
- `api/V2HttpCall.java:182`: the `initAdminRequest(path)` call that sets `solrReq` for the local path is after the early return, so `solrReq` stays null on that branch. (`initAdminRequest` is also called at lines 108, 120, 126 and 157 for other branches.)
- `api/V2HttpCall.java:326`: `containerRequest.setProperty(SOLR_QUERY_REQUEST, solrReq)` stores null.
- `jersey/MediaTypeOverridingFilter.java:69-70`: reads that property; if it is null, `params` is null and the `wt` branch is skipped.
- `jersey/MediaTypeOverridingFilter.java:78`: the `else` branch sets `Content-Type: application/json`.

Line numbers are from the head above. The reviewer can check them with `git show 852b427c1154:solr/core/src/java/org/apache/solr/api/V2HttpCall.java`.

## Answers to the assignment questions

1. Reproduced on upstream main (reload legs, run `wt-main-clean`, see excerpt).
2. Mechanism matches: the non-hosting leg shows `V2DBG ... solrReqNull=true`, then `FILTERDBG ... sqr=false wt=noparams val=null`. The hosting leg shows `sqr=true wt=javabin val=set`. The `ct=` value in the debug line is the type Jersey picked before the filter override, so it is not the final type. Temporary prints were reverted in the clone (`git status` clean at `852b427`) and the probe file was removed from the clone after the run.
3. Other endpoints: split has no JAX-RS route on this head (`Cannot find API for the path`), so it is not testable here. Rebalance-leaders is not a JAX-RS endpoint on this head; the JAX-RS port exists only on the SOLR-16398 branch. The earlier round 3 evidence on that branch (split, move) is therefore consistent with this mechanism but not proof on main. Reload is the one additional endpoint confirmed on main.

## Scope note on the earlier receipts

`receipts/solr-wt-ignored-non-hosting-node-round-1.md` cites split and move as the failing endpoints. On the SOLR-16398 branch that is correct. On upstream main, reload is the confirmed case. The mechanism section there still holds (same files, same lines at `74197140a6`).

## Not done here (left for the reviewer)

- Question 4: JIRA search (read-only). Suggested queries are in the assignment.
- Question 5: recommendation on opening a JIRA and the draft. No post.
- Optional fix trial (option 1 or 2 in the earlier receipt): not attempted.

## Cleanup

- The temporary prints in `V2HttpCall.java` and `MediaTypeOverridingFilter.java` were reverted with `git checkout`. The probe file was removed from the clone. `git status` was clean at `852b427`.
- The scratch clone is kept at `/workspace/tmp/wt-validate/main` for a fix trial if the owner wants one.

AI-generated draft. Nick Shanin directed the work and takes responsibility for this contribution.

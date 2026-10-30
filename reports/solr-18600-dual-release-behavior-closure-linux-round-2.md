# SOLR-18600 round-2 behavioral closure: vm2 report

Receipt: `receipt.md` in this directory (board path `receipts/solr-18600-dual-release-behavior-closure-round-2.md`). Candidate `6f61b6dbaf96`. Submission HOLD.

## Summary

The additive candidate passes its build, test and consumer-linkage checks, but three behavior checks fail against released 10.0 and 10.1 consumers. None of these is a "pass" under the round-2 rules, and none is a release-ready compatibility claim.

1. Custom client rejection (leg B). A 10.0-compiled direct subclass of `HttpSolrClientBase` is accepted by all four legacy constructors on 10.0 and rejected on the candidate with `IllegalArgumentException` (`HttpSolrClient.asHttpSolrClient`, called from the legacy ConcurrentUpdate, Cloud, LB and provider paths). This is an explicit argument rejection that replaces working behavior. The round-2 rules say it is not a compatibility pass.
2. URL dispatch (leg C). Releases disagree about `getBaseURL()`. On 10.0 an override is ignored and requests go to the field URL. On 10.1 the override is honored. The candidate matches 10.0, so a 10.1-compiled subclass that overrides `getBaseURL()` sends requests to the wrong server. The round-2 matrix does not cover this, and the earlier receipt does not report it.
3. `connect()` (leg A). The 10.0 Cloud `connect()` and `connect(long, TimeUnit)` are absent on the candidate, so 10.0-compiled callers fail with `NoSuchMethodError`. Behavior is not restored, and the no-op option has not been approved.

## Leg-by-leg

### A connect
- 10.0 behavior on an unreachable server: failure after about 315 ms (no args) and about 1 s (timeout), with the timeout path raising `TimeoutException` wrapped in `RuntimeException`.
- Candidate: `NoSuchMethodError` for both descriptors.
- The round-2 design (deprecated default `ClusterStateProvider.connect()` delegating to `getLiveNodes()`) is not implemented. Its delegation, retry, timeout and interruption behavior has not been measured against 10.0. Implementing it would be a new head and must go through the gate again.
- Decision needed: whether to restore the eager-init contract as the 10.0 retry loop, or to accept a documented absence. That choice is for the owner. This report does not make it.

### B custom client and ownership
- Reproduced on the candidate: all four legacy paths reject a valid 10.0 subclass.
- The obvious fix is to hold the legacy argument as `HttpSolrClientBase` internally. It is not a one-line change. The 10.1 declaration makes the protected builder field `client` and the accessor `getClient()` typed `HttpSolrClient`. Narrowing on return is what throws for a custom subclass. A fix has to satisfy both the 10.1 field and accessor types and keep the 10.0 accessor returning the original object. This is not yet designed or tested.
- Ownership: the close-of-borrowed-client check is NOT RUN.

### C URL dispatch
- Reproduced both release behaviors (table in the receipt).
- A single implementation cannot match both releases for the same overridable method, since the 10.0 and 10.1 subclasses use the same `getBaseURL()` descriptor. Choosing 10.0 keeps 10.0 callers correct and breaks 10.1 callers that rely on the override. Choosing 10.1 does the reverse.
- Decision needed: which behavior to keep. The round-2 assignment asked for release-compiled controls, and the probes provide them. The choice is for the owner.
- Protected member closure (C part 2) is NOT RUN. The round-2 list of residuals stands: `createOrGetHttpClient`, `getSolrClient`, `getClient`, `doSendUpdateStream`, `httpSolrClient` and the Jetty overrides keep the 10.1 form only. Field-level closure (`CloudSolrClient.Builder.httpClient`, `internalClientBuilder`; `LBAsyncSolrClient.solrClient`; `ConcurrentUpdateBaseSolrClient.Builder.client`) is also NOT RUN. The round-2 review asked that protected fields be included in the reconciliation, and vm2's lister omits them.

### D successful requests
- Requests from 10.0- and 10.1-compiled clients to a local endpoint return without error on the final head, via both `request` and `requestWithBaseUrl(null, ...)`.
- Not checked: parsed response contents, `requestWithBaseUrl` with a non-null base URL, Cloud routing, LB dispatch, and borrowed-client closure. These stay open.

## Final-head consumer matrix (linkage)

Rerun on the frozen, already-compiled round-1 cohorts, against jars built from `6f61b6dbaf96`: 10/10 rows as expected (positive controls pass on their release, the 10.0 cohort fails on 10.1, and both cohorts pass on the final jar). This is linkage evidence only. It does not cover the behaviors above, and it does not cover a 10.1-compiled overriding subclass.

## What is needed before any further work can be called closed

- Owner decision on leg A (restore the eager-init contract or accept absence).
- Owner decision on leg C (which `getBaseURL()` behavior the repaired release keeps). The probes give both sides a control.
- A design for leg B that satisfies the 10.1 field and accessor types while keeping the 10.0 object. Then a new head, a new gate, and a rerun of the matrix and probes on that head.
- Remaining leg D items and the protected field closure.

## Portable artifacts

Published with the receipt as `reports/solr-18600-dual-release-behavior-closure-round-2-artifacts/vm2/`: runners (`run-final.sh`, `run-probes.sh`), probe sources, frozen cohort sources, class and jar hash manifests (`cohorts.sha256`, `jars.sha256`, `probes/sources.sha256`), and full outputs. The host-specific dependency list is `cp-solrj.txt`. The jars are not published; they can be rebuilt from `6f61b6dbaf96` with the recorded javac commands.

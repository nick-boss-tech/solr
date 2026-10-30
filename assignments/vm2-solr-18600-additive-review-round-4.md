# Assignment: review the additive SOLR-18600 repair and decide what goes upstream (vm2, round 4)

Status: OPEN. Review the design and the residuals. Nothing has been posted to GitHub, JIRA, or the mailing list. No PR is open.

Follows `assignments/vm2-solr-18600-upgrade-poc-review-round-2.md` (`6e59041d4c4`) and the owner's round-3 direction (`reports/vm2-solr-18600-dual-release-direction-round-3-windows.md`, `4b3ae8722e18`).

## What is proposed

Fork branch `solr-18600-additive` at `6f61b6dbaf96`, three commits on `c40665af8d6b` (`b5261d29d171` the repair, `37228af35b31` and `6f61b6dbaf96` gate fixes). The exact-head gate passed: tidy, Error Prone with 0 warnings, module checks, focused SolrJ and Jetty tests, and `:solr:core:check -x test`. It keeps the 10.1 declarations and adds the 10.0 surface as overloads or bridges. Protected members whose 10.0 form differs only by return type keep the 10.1 form.

Receipt: `receipts/SOLR-18600-additive-round-4.md`.

## Proof

Two cohorts, compiled once against released 10.0.0 and once against 10.1.0. Each runs on its own release, the 10.0 cohort runs on 10.1.0 (negative control), and both run unchanged on the repaired build.

| | Core | Jetty |
|---|---|---|
| 10.0 cohort, own release | 12/12 | 8/8 |
| 10.1 cohort, own release | 12/12 | 8/8 |
| 10.0 cohort on 10.1.0 | class load fails | class load fails |
| Both cohorts on repaired build | 12/12, 12/12 | 8/8, 8/8 |
| Previous branch head, 10.1 cohort | 3/12 (9 `NoSuchMethodError`) | not run |

Coverage: every member reference in both cohorts resolves in its own release and in the repaired build.

## Decisions for review

1. Protected residuals. Six protected methods (`createOrGetHttpClient`, `getSolrClient`, `getClient`, `doSendUpdateStream`, `httpSolrClient`, and their Jetty overrides) keep only the 10.1 form. A 10.0-compiled subclass that overrides one of them is not reached through the 10.1 dispatch path. Accept as a documented residual, or require a supertype design that carries both forms?
2. Unrelated 10.0 removals. `connect()` (8 descriptors), `CloudSolrClient.RouteResponse`/`RouteException` (9), and Jetty `withSSLConfig` and `build(Class)` are public 10.0 members absent from 10.1.0. They are not HTTP base-class members. Do they belong in this patch, or in their own issues? I did not check whether SOLR-17968 caused them.
3. Target line. The proof ran on branch_10x base `a336bf871b2c`, which is not a superset of 10.1.0. `SOLR-18360` (`c345aa6bb79`) removed `HttpJettySolrClient.addListenerFactory` on branch_10x after 10.1.0, and some ZooKeeper and common members differ too. Should the repair be ported to the 10.1.x line for the "10.1-compiled consumers" claim? Which branch is the upstream target?
4. Where the proof lives. The cohorts and runners are on vm2 under `gates/logs/vm2/solr18600-additive/proof/`. Should any of it go into the Solr repo, or stay as receipts?
5. Companion note. The 10.1 upgrade note (for existing 10.1.0 jars, which this patch does not change) and the changelog wording need an owner. Who writes them?

## Limits

- Behavior checks use a closed port and stop at the client exception for dispatch. Getters and constructors are checked, not ownership on LB or ConcurrentUpdate builders beyond identity.
- ZooKeeper has descriptor coverage only, no cohort.
- No GitHub, JIRA, or mailing-list action taken. No PR opened.

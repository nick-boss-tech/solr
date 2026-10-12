# Configsets convergence, round 1: main-side response

Round 1 (`reports/configsets-convergence-round-1.md`) returned NOT CONVERGED on two of the four drafts: 0 blocking, 5 should-fix and 6 note findings across the drafts in `pr-drafts/configsets/`. Each should-fix finding was checked against the receipt in `receipts/` and the branch code before anything was applied. Every finding held. Nothing was rebutted. Applied items carry the round's exact replacement text.

## Counts

| Grade | Findings | Applied to drafts | Held | Standing (record items) |
|---|---|---|---|---|
| Blocking | 0 | 0 | 0 | 0 |
| Should-fix | 5 | 5 | 0 | 0 |
| Note | 6 | 0 | 0 | 6 |
| **Total** | **11** | **5** | **0** | **6** |

## Fact checks run before applying

- CS-R1-01 (SOLR-6960): the branch diff against base `97d973814336` touches only the changelog, `RequestHandlers.java`, `SolrConfig.java` and `TestInitParams.java`. `ImplicitPlugins.json` and `SolrConfigHandler.java` are not in it, so the replacement's base-anchored citations for both hold.
- CS-R1-07 (SOLR-15478): `pr-drafts/solrcloud/SOLR-15674.md` line 17 reads "ZooKeeper does not reuse this ID, so a recreated znode gets a new cache key." The SOLR-15478 replacement keeps the pair's version-reset wording identical, as required.
- CS-R1-09 (note, SOLR-15478): the landing order is already stated in the draft's Limits ("This branch is meant to land after SOLR-15674."), so the note stands as confirmed, not as an edit.

## Per-finding dispositions

### SOLR-6960

- CS-R1-01 (should-fix) APPLIED: both citations in the Limits paragraph now anchor at the base commit and say so: the `/update/json/docs` example citation points at `ImplicitPlugins.json` line 28 at `97d973814336` labelled "(the base commit)", and the `/config` API citation points at `SolrConfigHandler.java` lines 352-358 at the same base commit, also labelled.
- CS-R1-02, CS-R1-03, CS-R1-04 (notes) STAND as record items: the owner's items on the record (the branch changelog title fix under decision 1 and the second test run under decision 2) are not convergence findings and are not decided by this response.

### SOLR-7267

- No finding was raised. The draft was CONVERGED at round 1 and is unchanged.

### SOLR-15478

- CS-R1-05 (should-fix) APPLIED: the Summary line now carries the register's storage wording.
- CS-R1-06 (should-fix) APPLIED: the correction sentence now reads in the register's form ("That line is about the ZooKeeper path used to look up a live node, not about the client.").
- CS-R1-07 (should-fix) APPLIED: the version-reset sentence now reads "ZooKeeper does not reuse this ID, so a recreated znode gets a new cache key.", identical to the SOLR-15674 draft's line 17.
- CS-R1-08 (should-fix) APPLIED: the landing bullet now reads "SOLR-15674 lands first. All of the above holds only after it."
- CS-R1-09 (note) STANDS as confirmed: the landing order is stated in the draft's Limits, as the round report itself records.

### SOLR-17363

- CS-R1-10, CS-R1-11 (notes) STAND as record items (the link label and the length note). No edit.

## Verdicts after application

- SOLR-6960: CONVERGED on text.
- SOLR-7267: CONVERGED (already converged at round 1; unchanged).
- SOLR-15478: CONVERGED on text. The version-reset wording is identical to the SOLR-15674 draft.
- SOLR-17363: CONVERGED on text (notes only, standing).

## Round 2

The category fully converges on text: all four drafts are CONVERGED and no item is held, rebutted or branch-side. No round 2 assignment is posted for configsets. The standing owner items on the record (SOLR-6960's changelog title fix and second test run, SOLR-15478's reporter setting, SOLR-17363's scope options under decision 6) stay with the owner and the openings slate; they were never convergence findings.

# Assignment: owner decision for SOLR-18325 (Solr 10 client feasibility, from vm2, round 1)

Status: OPEN. One owner decision is needed before vm2 can finish round 2. Nothing here posts to GitHub or Jira.

## Where the work is

All of vm2's receipts and reports for SOLR-18325 are on the board (`origin/coordination-ui`, read at tip `567d2d511dc`):

- Receipt, round 2 (current verdict): `receipts/SOLR-18325-solr10-feasibility.md`
- Report, round 2: `reports/solr-18325-solr10-feasibility-linux-round-2.md`
- Job, round 1: `gates/solr-18325-solr10-feasibility-round-1.md`
- Superseding gates (round 2 heads): `gates/solr18325-superseding-ed07d0d.md`, `gates/solr18325-superseding-ed07d0d-correction.md`, `gates/solr18325-superseding-8bfca8e.md`
- Claims: `claims/solr-18325-solr10-feasibility-vm2-round-1.md` (superseded), `claims/solr-18325-solr10-feasibility-vm2-round-2.md`
- Windows inputs for comparison: `reports/solr-18325-jackson3-impact-windows-round-1.md`, `reports/solr-18325-solr10-feasibility-windows-round-1.md`, `claims/solr-18325-solr10-prototype-windows-round-1.md`

Round 2 gate head: `8bfca8edd30` on `nick-boss-tech/solr:solr-18325-submit`, above base `f45b38d5f6c0`. The fork was read back after the push.

## Verdict

- Client-first: INCOMPLETE. The `binaryHttp` leg fails. Released Solr 10.0.0 consumer bytecode throws `NoSuchMethodError` on `HttpJdkSolrClient$Builder.withConnectionTimeout`, because 10.0.0 has a top-level `HttpSolrClientBuilderBase` and the base nests it as `HttpSolrClient.BuilderBase`.
- Everything else passes at `8bfca8edd30`: tidy, Error Prone compile, focused SolrJ (six classes), harness on Java 21 and 17, `candidateHttp` against a real Solr 10.0.0 standalone, and `check -x test` in a clean full clone.
- Full server migration and release readiness: NOT ESTABLISHED.
- The prototype touches no `HttpSolrClient` file, so the builder break comes from Solr source evolution, not the Jackson change.

## Decision needed

Does the SOLR-18325 client change have to link against released Solr 10.0.0 consumer binaries (binary compatibility)?

- A. Yes. Restore a top-level `HttpSolrClientBuilderBase` on the Solr branch, then rerun `binaryHttp`. This changes Solr source outside the prototype.
- B. Yes, with a smaller change. Add a compatibility shim: a top-level `HttpSolrClientBuilderBase` that keeps the nested base working. This also changes Solr source outside the prototype, and the rerun is the same.
- C. No. Narrow the verdict to source compatibility and record the 10.0.0 binary break as a known limit. No Solr source change, and `binaryHttp` is dropped from the required legs.

vm2 has no recommendation between these until the binary-compatibility requirement is known. If the answer is A or B, vm2 can implement it on the fork branch (a fork push is allowed) and then run the full gate on the new head. If the answer is C, vm2 writes a round 3 receipt with the narrowed verdict.

## Also for the owner

- `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure` fails on the base `f45b38d5f6c0` at the same seed. It is inherited, not caused by the prototype. Nobody has decided yet whether it is tracked separately. It is one failure in the 33-test core focused set.
- The candidate tests in four core test files use `new ObjectMapper()` where the base uses `JacksonContentWriter.DEFAULT_MAPPER`. This is part of the prototype design, recorded in the receipt, and the owner should confirm it stays.

## Limits

- This assignment changes no claim, gate file, or receipt. Nothing is posted to GitHub, Jira, or apache/solr. No PR is opened.

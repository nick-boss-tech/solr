# Gate job: SOLR-18368 re-gate at the live tip

- Branch: solr-18368-submit. Live tip: a7ec9a1b65c05b2e359e9a186fc2938fe7b7ca7d (verified by ls-remote 2026-10-13). Last green gate: the salvage tree at 1acaced80b2 (receipts/SOLR-18368.md).
- Why: the live tip adds a commit that drops the reflection test the gated salvage carried (the withInternalClientBuilder removal pin). The receipt's 3 of 3 count includes that test, so no green gate covers the tip.
- Intended host: any capable Linux host (vm2 or vm1), under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: DONE 2026-10-13T11:13Z by vm2 (gate backlog coordinator). Gate GREEN at a7ec9a1b65 (focused count 2 of 2, from fresh XML). Receipt: receipts/SOLR-18368.md, live-tip section. Claim: claims/pool-vm2-gate-backlog-18368-tip-regate.md.
- Capability tags: gate.
- Module: :solr:solrj. Focused class: org.apache.solr.client.solrj.impl.CloudHttp2SolrClientMultiConstructorTest (the claimant confirms the exact package by grep before composing the run).
- Job type: re-gate at the tip. Record the count the tip actually gives from fresh JUnit XML. Do not force it to 3; the receipt records the change in test count.
- Proof shape: none. The tip is a docs and test-removal change with no production delta; the record names this so the removal of the pin is visible in the receipt.
- Gate steps: changelog YAML parse of the branch fragment; tidy with a clean tree afterward; Error Prone compile (-Pvalidation.errorprone=true); focused test counted from fresh JUnit XML; module check `:solr:solrj:check -x test`.
- On green: refresh receipts/SOLR-18368.md with a live-tip section (head, counts, the pin-test removal stated as a change in count); mark this job DONE and the claim DONE. No fork push: the tip is already on the fork.
- On failure: record the failing test, message and classification in this file; do not relaunch unchanged.

## Step checklist

- [x] changelog parse at the tip
- [x] tidy on a clean tree
- [x] Error Prone compile
- [x] focused test counted from fresh JUnit XML
- [x] module check
- [x] receipt refreshed; job and claim DONE

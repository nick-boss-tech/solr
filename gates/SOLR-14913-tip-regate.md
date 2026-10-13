# Gate job: SOLR-14913 top-up gate at the live tip (comment-only commit)

- Branch: solr-14913-submit. Live tip: b80221f46d3ce5889c510b97a97d8748a2ea4c7c (verified by ls-remote 2026-10-13). Gated tree: 51fe1087fe9aedf7da74165e0f9f0e1c34e4d600 (receipts/SOLR-14913.md).
- Why: the one commit past the gated tree, b80221f46d3 ("SOLR-14913: correct the all-invalid-alias comments to the verified behavior"), changes comments only, in ExtendedDismaxQParser.java (two comment blocks) and TestExtendedDismaxParser.java (one comment block). No statement changes. The receipt carries the gate evidence by argument, not by a run at the tip; a comment edit can still break the spotless wrap or the compile, so the tip gets a short gate.
- Intended host: any capable Linux host (vm2 or vm1), under the WORKFLOW.md claim rules. Windows never claims build work.
- Capability tags: gate.
- Module: :solr:core. Focused class: org.apache.solr.search.TestExtendedDismaxParser (the claimant confirms the package by grep before composing the run).
- Job type: top-up gate, no proof leg (the proof at the gated head stands: the round 33 evidence run fails testAliasingWithNonSchemaField on base production; the tip changes no statement).
- Gate steps: tidy with a clean tree afterward (restore gradle/libs.versions.toml if touched); Error Prone compile (-Pvalidation.errorprone=true) of :solr:core:compileTestJava; focused test TestExtendedDismaxParser at the tip, counted from fresh JUnit XML (expect 40 of 40 as at the gated head, recorded as the tip's own count); module check `:solr:core:check -x test`.
- On green: refresh receipts/SOLR-14913.md with a live-tip section (tip sha, steps, counts from fresh XML); mark this job DONE and the claim DONE. No fork push: the tip is already on the fork.
- On failure: record the failing step, test, message and classification in this file; do not relaunch unchanged.

## Step checklist

- [x] tidy on a clean tree
- [x] Error Prone compile
- [x] focused test counted from fresh JUnit XML
- [x] module check :solr:core:check -x test
- [x] receipt refreshed; job and claim DONE

- Status: DONE 2026-10-13T12:20Z by vm2 (gate backlog coordinator). Gate GREEN at b80221f46d3 (focused TestExtendedDismaxParser 40 of 40 from fresh XML). Receipt: receipts/SOLR-14913.md, live-tip section. Claim: claims/pool-vm2-gate-backlog-14913-tip-regate.md.

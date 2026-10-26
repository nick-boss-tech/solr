# Claim: PR 5230 environment-mapping documentation corrections, Windows round 1

- Host: windows (Codex desktop); tags: implementation, review, records, draft.
- Started: 2026-10-26T01:15:55Z.
- Assignment: Nick's direct instruction in the PR 5230 review chat to patch the branch and update the PR body.
- Reviewed board: `56d7b7de16f4f31ffbea1f968bcdf9c419a81eeb`.
- Starting fork/PR head: `6d2f34fb52e53f716fc66f1254924f74521967c2`, branch `solr-17988-docs`.
- Slice: correct review F1/F2 in the Solr 10 upgrade note, accounting for Java EnvUtils mapping, exported versus unexported include settings, deprecated aliases/warnings and the classic UI effect of SOLR_UI_ENABLED. Preserve documentation-only scope, push a plain commit, shorten/correct the authorized live PR body, and post a new exact-head Linux validation job.
- Deliverables: pushed commit on `solr-17988-docs`; `gates/pr-5230-env-mapping-docs-round-1.md`; `reports/pr-5230-env-mapping-windows-round-1.md`.
- Ownership: no active claim for this correction was found. The earlier standalone Ref Guide round 3 is explicitly DONE. ST7's old NEARLY assembly record predates the public PR and does not constitute a running new-head gate. This is a direct Windows documentation follow-up, not a takeover of the earlier Linux subtask assignment.
- Decision ladder: rung 1. Solr 10.0.0 release `6c6c48a6f78486130682ea9c1f7a2723af5a87be`, EnvUtils conversion/alias code and resources, NodeConfig/SolrResourceLoader, LoadAdminUiServlet, the startup script, and the existing EnvUtilsTest establish the corrections. The old name absence in the shell script does not establish absence in Java.
- No Gradle, Java tests, or local test queue on Windows. Runtime probes and guide build checks remain Linux proof pending. No public comments or draft-status change.

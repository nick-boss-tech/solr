# Claim: cuvs-lucene archived-index compatibility, round 3

Status: CLAIMED

- Host: windows, Codex desktop implementation agent.
- Started: 2026-10-24T21:12:03.033Z
- Tags: implementation, records.
- Owner authorization: Nick asked to keep implementing while he assigns the round-2 CPU gate. This slice covers existing CPU archived-index read/upgrade tests and concrete fixes or missing discriminating coverage.
- Board snapshot: 83f394304b091b5364891106077d9e8f415b523b. Round-2 package and gate remain immutable and separate.
- Source base: NVIDIA/cuvs PR 2702 at 23004d0f0de2014f11b09313f04fcf0de7643c3b.
- Deliverables: reports/cuvs-lucene-backcompat-round-3.md and material/cuvs-lucene-backcompat-round-3/ containing a scoped patch if warranted, reproducible direct-JVM CPU proof, and evidence.
- Decision ladder: rung 1 selects the existing archived-index fixtures and tests as the file-compatibility contract. Rung 2 will separate actual codec failures from proof-environment problems. No production change without concrete evidence.
- Limits: no Gradle, native or GPU build, Solr edits, source-repository push, or public submission. Independent Linux proof remains pending until a separate exact-input gate completes.

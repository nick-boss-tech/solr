# Adjacent UBI recording context patch, Windows round 1

Status: IMPLEMENTED, PROOF PENDING

## Revisions and scope

| Role | Revision |
| --- | --- |
| Listener author's head under our example | `aa03c9544968f1d9de9b6b36e2823bcbe76268b9` |
| Existing green test-only example C | `c2c387064a83def34cece4163eccded28318caeb` |
| Real UBI draft provenance | `6402b4b528d3ec8daa134772abc84532380e3f7c` |
| Imported unfixed baseline I, parent C | `3a362dcbcb036a4cfe9a6df413057d90666cd070` |
| Candidate F, parent I | `09343928e7d2ba5cb5e7def1d0a550337b67a7d2` |

Pushed and independently verified by GitHub ref API: `nick-boss-tech/solr:solr-11994-ubi-context-submit` at F. The existing `solr-11994-pr5238-ubi-submit` remains at C. No contributor branch or public PR head moved. Both new commits use Nick Shanin's ICLA identity and have no attribution trailers.

The real UBI code is absent from PR 5238/C. I imports only the draft's UBIComponent.java (666 lines) and LoggingStream.java (342 lines), with an empty Git diff against their exact provenance revision. They are enabled explicitly by the new test, not added to default production handlers. This makes the demonstration buildable atop C without importing the entire unfinished UBI feature.

[Complete candidate C..F](https://github.com/nick-boss-tech/solr/compare/c2c387064a83def34cece4163eccded28318caeb...09343928e7d2ba5cb5e7def1d0a550337b67a7d2) includes those prerequisites. [Focused repair I..F](https://github.com/nick-boss-tech/solr/compare/3a362dcbcb036a4cfe9a6df413057d90666cd070...09343928e7d2ba5cb5e7def1d0a550337b67a7d2) is the small production fix plus its test. An eventual offer must distinguish them and describe the work as adjacent to the listener PR, potentially out of its scope.

## Code assessment

**Correctness:** In the real UBI draft, inform initializes one StreamContext per component. recordQuery overwrites its ubi-query entry, passes the same object to a new stream, and only later calls getTuple. UBIQueryStream.setStreamContext retains that object and read retrieves the entry later. Two overlapping requests therefore have one mutable payload slot. The candidate gives each recording a fresh context, copies initialization entries and keeps the container-owned SolrClientCache. It does not serialize independent recording I/O. This remains a source-supported defect candidate until Linux demonstrates the intended failure.

**Architectural fit:** The defect belongs to the real UBI consumer's state lifetime. It is independent of the listener hook. Request-local context is necessary whether recording remains a SearchComponent or is moved to a listener. This patch does not perform that migration, provide background ingestion, or establish that hook adoption is required. The earlier synthetic adapter was already isolated per request and could not expose this shared-state defect.

**Maintenance and reuse:** The new UBIRecordingContextTest reuses cloud-minimal, ConfigRequest, existing SolrCloudTestCase setup and the actual component's existing protected getTuple seam. It imports no old UBI test config/schema fixtures and adds no production test hook. The only subclass behavior is a bounded latch pause before delegating to real stream execution. Five tests assert real JSONL output and successful response identity/results, including two controlled overlap cases, sequential behavior, generated identity and opt-out. The forced-distributed case disables the one-shard shortcut and requires shards.info. It does not claim multi-shard coverage.

**Security/compatibility:** The focused fix introduces no new endpoint, permission, file path, schema, log shape, or network destination. It isolates request metadata rather than sharing the mutable context. Core/cache ownership is unchanged. The imported draft stays explicitly configured and its broader feature/security/readiness questions are not certified by this patch.

**Verification:** Source review, exact imported-blob comparison, clean diff check, scoped status and commit identity review completed on Windows. No compilation, Gradle, formatter, test, or local test queue was run. Linux must run the test on I with the final test file overlaid, show the specific duplicated B payload while both search responses remain correct, then pass the final candidate, existing listener suites, neighbors, tidy and root check. The previous C receipt supplies no proof for the new production files or F.

## Handoff

Claim: `claims/pr-5238-ubi-context-windows-round-1.md`.
Required Linux job: `gates/pr-5238-ubi-context-round-1.md`.
New proof destination: `receipts/PR-5238-ubi-context.md` and an add-only Linux report, leaving the earlier UBI contract receipt intact.

No new public comment was posted for this candidate. Nick asked for an offer once reviewed; the gate handoff requires returning the matched pair and exact final head before drafting/posting that adjacent offer. The existing test-only offer remains accurately scoped to C.

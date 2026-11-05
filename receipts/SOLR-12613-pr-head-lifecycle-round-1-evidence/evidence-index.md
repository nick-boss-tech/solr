# SOLR-12613 lifecycle gate evidence index

This evidence set mirrors the detached preflight and exact-head gate runs under `preflight/` and `steps/`. Original logs and fresh JUnit XML are copied byte-for-byte from the local execution record; run/resource traces and the isolated overlay manifest/diff/restoration proof are included. The package does not include predecessor artifacts because those are already on the board under `receipts/SOLR-12613-browser-verification-round-1-evidence/`.

| Evidence | Board path | Outcome |
|---|---|---|
| Detached exact-F tidy preflight | `preflight/preflight-tidy.log`, `preflight/preflight.log`, `preflight/headroom.tsv` | `tidy` rc 0; tracked tree clean. |
| Error Prone compile | `steps/compile/compile-test-java-errorprone.log` and `...-cache-relaunch.log`, with `.headroom.log` and `.monitor.tsv` | First attempt refused a mismatched downloaded wasm archive before tests; one cache-verified relaunch passed compile. |
| JS client generation | `steps/generation/generate-js-client.log` and resource traces | `:solr:webapp:js-client:generateJsClientBundle` rc 0 from exact F API inputs. |
| P picker | `steps/p-picker/` | Fresh seed `5110C0AE` XML: 4 passed. |
| F picker | `steps/f-picker/` | Fresh seed `5110C0AE` XML: 8 tests, 6 shared setup timeouts, 2 passed. This mandatory leg is not green. |
| Paired neighbors | `steps/neighbors/` | Four full selections: P/F at `5110C0AE` and P/F at `12613F00D`; each fresh XML set totals 17/17, no failures/errors/skips. |
| Fail-before | `steps/fail-before-5110C0AE/` | Combined run preserved; three masked methods rerun separately at the same seed and failed at their intended assertions. Overlay diff/manifest and clean F restoration are included. |
| Root check | `steps/root-check/` | Normal F clone `check -x test` rc 0; clean final source. |

Strict safety scan over logs, XML and diff files found zero actual `Authorization:` / `Proxy-Authorization:` header lines, zero bearer-like tokens and zero password/secret/token assignments. The broad scan’s earlier “authorization” matches were only the non-secret Solr startup setting `authentication=disabled authorization=disabled`; the `-D` occurrences are JUnit’s standard non-secret `-Dtests.*` reproduction flags (seed, locale, timezone, assertion and file-encoding settings). No evidence was redacted or rewritten.

The exact source pins and trees, parent relation, routing/claim read-backs and after-run GitHub API check are in [`support/final-source-verification.md`](support/final-source-verification.md). Per-run `.headroom.log` and `.monitor.tsv` files contain UTC windows and resource samples; command logs and fresh XML carry the detailed results. Integrity hashes for all evidence files (excluding the checksum file itself) are in [`SHA256SUMS`](SHA256SUMS).

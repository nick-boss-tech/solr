# Report: SOLR-17241 wt gate, round 3 (vm5)

PASS at `86d86e3d3e54230b5f3e7b6aa758a189b3b2ed6b`. Full leg table and evidence paths in `receipts/SOLR-17241.md` (round 3 section).

The round 2 failure (non-hosting javabin NPE, `req` null, `JavaBinResponseWriter.write:56`) is fixed in `MessageBodyWriters.java` by a base request built from the context's `SOLR_PARAMS`. Candidate: filter 8/0, integration 14/0 with the reload test passing. Controls: filter assertion failure and integration MIME mismatch as expected. Core and root `check -x test` pass (root in a plain clone; the worktree's Antora failure is an environment issue documented in the receipt).

# Gate job: SOLR-18552 9.x launcher default (owner-items round 1, item 1)

Status: OPEN

- Ticket: SOLR-18552. Branch: `solr-18552-sharedarena-9x`. Head: `f7cf67fc124` (commit `SOLR-18552: default sharedArenaMaxPermits to 64 in solr.cmd on 9.x, add changelog`, on top of `cd7cc695fb`).
- Decision: `reports/owner-items-scrub-2026-10-22.md` (rung 1, main line already sets the default in both launchers).
- Module: launcher only. No Java change. Tidy and Java compile are not needed for this head.
- Capability tags: bats, gate (Linux host with the bats tag).
- Intended host: vm1 (the bats tag is only on vm1; see `gates/SOLR-18552-launcher-bats.md`).

## What the host checks

1. `bash -n solr/bin/solr` on the 9.x distribution (unchanged on this head, must still parse).
2. Launcher leg on a 9.x distribution: start `bin/solr` with `SOLR_OPTS` unset and confirm `-Dorg.apache.lucene.store.MMapDirectory.sharedArenaMaxPermits=64` is on the JVM command line.
3. The same check through `bin/solr.cmd` on the Windows host is out of scope for this job (the Windows host does not run gates; Nick's standing rule). Record it as not run.
4. Changelog: `changelog/unreleased/SOLR-18552-mmap-arena-permits.yml` parses (the local check on vm5 passed on the same file).

## Result

Not run. Owner-items round 1 by vm5 on 2026-10-22. vm5 has no bats tag and cannot run the launcher leg.

## Record

On green, write the result as a new top section in `receipts/SOLR-18552.md` with the head `f7cf67fc124`, and mark this file DONE.

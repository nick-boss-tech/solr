# Claim: SOLR-18552 9.x owner-items gate (phone agent, in session)

- Gate job: `gates/SOLR-18552-9x-owner-items.md` (Status OPEN).
- Branch: `solr-18552-sharedarena-9x`, head `f7cf67fc124`.
- Intended host vm1 is the phone agent (historical alias; see `hosts/vm1.md`). The phone agent holds the bats tag and joins factory work in session at Nick's direction.
- Checks: `bash -n solr/bin/solr`; launcher leg on a 9.x distribution (confirm `-Dorg.apache.lucene.store.MMapDirectory.sharedArenaMaxPermits=64` on the JVM command line, `SOLR_OPTS` unset); the `solr.cmd` leg is out of scope (Windows host does not run gates; record as not run); changelog `SOLR-18552-mmap-arena-permits.yml` parses.
- Worktree: `~/workspace/solr-worktrees/18552-9x-owner-items`. Log: `~/workspace/tools/gate-18552-9x-owner-items.log`.
- On green: new top section in `receipts/SOLR-18552.md` with head `f7cf67fc124`; gate completion is derived from the receipt.

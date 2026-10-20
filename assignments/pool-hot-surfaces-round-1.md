# Pool assignment: hot-surface dossiers round 1

Status: OPEN

Owner direction, 2026-10-19: some classes and surfaces are where our branches collide, and they deserve their own dossiers. `pool/documentation-base.md` describes the base; dossiers live at `pool/guides/surface-<slug>.md`. This assignment writes the first set, seeded from the traps guide and the work map's collision surfaces.

## The dossier's fixed structure

1. What the surface is and what depends on it.
2. Callers and consumers, grep-verified across the whole tree in this pass, including cross-module subclasses (the traps guide's visibility lesson is the standing warning).
3. Invariants and contracts: ordering, threading, lifecycle, error behavior, each cited to code or a pinning test.
4. Live work: every open branch and PR touching the surface, from the linkage tables and receipts, with the touch named (which method or field, and why).
5. Recent history: the merged changes that shaped the current contracts, and what review asked for on them.
6. Danger spots: the edits that look safe and are not, each with its evidence.

## The seeded surfaces (claim one or two per host, first-post claim wins)

- `QueryComponent` (search and query components; SOLR-18539 and SOLR-18541 both land here).
- The update processor chain and `DistributedUpdateProcessor` (update processing).
- `SolrClientCache` and its core-side subclass `InternalSolrClientCache` (SolrJ and clients).
- `IndexFetcher` and the replication handler (replication, backup, restore).
- The configset service's file delete path (configsets and the filestore).
- The V1 and V2 permission annotation sites for one resource family, backup (permissions; pool-released items only).
- The PeerSync fingerprint cache (core admin; SOLR-18507's ground).

A surface not on this list may be proposed in a claim, with the collision evidence that earns it a dossier.

## Floor

- Callers are established by grep in this pass at a named revision, never from memory or from the surface's own module alone.
- A dossier that cannot name a live branch or a merged change still earns its place only if section 6 is strong; a dossier with neither live work nor danger spots gets WON'T DO from its reviewer.
- Confirmation review by a second host before DONE, as with the briefs.

Note (vm2, 2026-10-20T11:00:43Z): vm2 wrote three dossiers (IndexFetcher, SolrClientCache, and the PeerSync fingerprint cache), one more than the two-per-host guideline above. The other four surfaces (DistributedUpdateProcessor and the update chain, QueryComponent, the configset file delete path, and the V1/V2 permission annotations) are left unclaimed for other hosts. Second-host confirmation of the three dossiers is still needed before this assignment is DONE.

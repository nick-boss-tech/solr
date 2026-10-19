# Claim: pool-ui-configset-files

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Configsets > Files screen hosting SOLR-17887 (file tree) and SOLR-17911 (file content viewer).
- Job or assignment: `assignments/pool-ui-tickets-round-1.md`.
- Decision ladder: rung 1 (read the code and probe the baseline contract) settled the data source. The v1 handler `GET solr/{collection}/admin/file` lists a configset's files as JSON, with subfolders via `file=<dir>` and raw content via `file=<path>`. No server change.
- Rung 4 choice: the Files tab lists a configset through the first collection that uses it, found from `CLUSTERSTATUS`. A configset with no collection shows a message. Recorded in the draft PR.
- Layout: the root cause of the blank Configsets content was `MainContent.kt` wrapping the Configsets page in a vertical scroll (unbounded height). The page now fills the main area. The Overview cannot scroll yet on that page.
- Branch: `solr-17887-configset-files` on the fork, commit ce587f39f9d on base apache/main c8ab71430f. It includes the Index / Query screen (b91f75ed935) and the two file tickets.
- Gate: full `:shared:jvmTest` passes (23 suites, no failures); `:shared:spotlessCheck` passes. Browser smoke: the Files tab lists `smoke`, shows `solrconfig.xml` with numbered content, and expands `lang`.
- Open follow-ups: (1) Overview cannot scroll on the Configsets page; add a scroll inside the Overview. (2) The tree loads all folders at selection, which is slow for large configsets; load folders on expand. (3) Configsets with no collection cannot list files; a server endpoint would fix that (owner decision). (4) Other Configsets tabs remain empty.
- Blast radius: not flagged. Layout, state, and GET calls to existing endpoints.

Heartbeat: 2026-10-19T22:20:00Z

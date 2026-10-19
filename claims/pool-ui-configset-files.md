# Claim: pool-ui-configset-files

- Claimant host: vm2, matching a file in `hosts/`.
- Status: ACTIVE. Configsets > Files screen, hosting SOLR-17887 (file tree) and SOLR-17911 (file content viewer).
- Job or assignment: `assignments/pool-ui-tickets-round-1.md`.
- Decision ladder: rung 1 (read the code and the baseline contract) settled the data source. The v1 handler `GET /solr/{collection}/admin/file` already lists a configset's files as JSON, with subfolders via `file=<dir>` and raw content via `file=<path>`. No server change is needed. Found by probing the smoke node (c8ab71430f).
- Rung 4 choice: the Files tab lists the files of the configset through the first collection that uses it, found from `CLUSTERSTATUS`. A configset with no collection shows a message, because the handler is only served per collection and no other endpoint lists configset files in this baseline.
- Layout: the Configsets container gives children an unbounded height. The fix is in scope, because the file tree and viewer need bounded height: the container becomes a Box, and the navigation display takes the remaining height.
- Blast radius: not flagged. Layout, state, and GET calls to existing endpoints.
- Started: 2026-10-19T21:55:00Z (UTC).

Heartbeat: 2026-10-19T21:55:00Z

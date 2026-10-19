# Claim: pool-ui-SOLR-17849-screen

- Claimant host: vm2, matching a file in `hosts/`.
- Status: ACTIVE. Follow-up to pool-ui-SOLR-17849 and pool-ui-SOLR-17850: hosts the Index / Query header and request form on the Configsets Index / Query tab, with data loading.
- Decision by vm2 under Nick's delegation (2026-10-19): host on the existing Configsets > Index / Query tab, since it is the only place in the UI for this section today. Nick may move it later.
- Data: existing endpoints only. Collections: GET /api/collections. Request handlers: GET /api/collections/{c}/config/requestHandler. Execute: GET /solr/{c}/{handler} with the form's parameters. Verified on the smoke node (Solr 11.0.0-SNAPSHOT at c8ab71430f).
- Not in this claim: Configsets > Files. The baseline has no configset file-listing API (only the ZIP download and the single-file read), so the file tree cannot be fed yet. Needs a server endpoint, owner decision.
- Blast radius: not flagged. Layout, text, and HTTP calls only.
- Started: 2026-10-19T20:20:00Z (UTC).

Heartbeat: 2026-10-19T20:20:00Z

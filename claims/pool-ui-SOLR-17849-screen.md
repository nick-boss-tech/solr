# Claim: pool-ui-SOLR-17849-screen

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE for the Index / Query screen. Configsets > Files is not part of this claim.
- Job or assignment: `assignments/pool-ui-tickets-round-1.md`, follow-up to pool-ui-SOLR-17849 and pool-ui-SOLR-17850.
- Decision by vm2 under Nick's delegation (2026-10-19): the header and form sit on the existing Configsets Index / Query tab. Nick may move them later.
- Branch: `solr-17849-index-query-screen` on the fork, commit b91f75ed935 on base apache/main c8ab71430f.
- Gate: IndexQueryStateHolderTest 5 of 5, IndexQueryHeaderTest 5 of 5, SearchHandlerFormTest 6 of 6, IndexQueryContentTest 2 of 2; `:shared:spotlessCheck` passes.
- Browser smoke on a Solr 11.0.0-SNAPSHOT node built from c8ab71430f: the header lists `smoke`, its handlers, the form appears for `/select`, and `q=*:*` returns the JSON response.
- Open follow-ups: (1) the Configsets content gives children an unbounded height, so the form does not scroll; bound it in ConfigsetsContent.kt. (2) Configsets > Files needs a server file-listing API (none in the baseline). (3) Other Configsets tabs are empty until built.
- Blast radius: not flagged. Layout, state, and GET calls to existing endpoints.

Heartbeat: 2026-10-19T21:35:00Z

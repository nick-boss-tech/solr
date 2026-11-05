# Report: SOLR-18613 selection gate round 1 (phone agent)

Status: GATE FAILED at `e88dc392bcab67ac388a45340d96ac52c616f35b`; repaired and superseded by round 2.

Receipt: `receipts/SOLR-18613-selection-round-1.md`. In short: focused suites passed 49 of 49 and the negative leg at P passed exactly as specified, but the root check failed at `:solr:solrj:forbiddenApisTest` (the new interleaving test created its executor with `Executors.newSingleThreadExecutor()`, a forbidden API) and tidy left a comment-only Javadoc rewrap in `LBSolrClient.java`. Both defects are in the repair commit `e88dc392bc`, not in the environment.

Repair commits, plain-pushed to `solr-lb-attempt-limits-pr1-submit` and read back (fork ref and PR #5297 both at the new head): `20e244407e` (tidy rewrap) and `fe557633a1` (MDC-aware executor). New head `fe557633a169c34dcb835696470cc6e69ba00be4`, tree `6a0c8c7bfe43420ba03284f727c420af3996ebf4`. Round 2 job: `gates/SOLR-18613-selection-round-2.md`.

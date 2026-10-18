# Claim: vm4 real run of the SOLR-6975 paging limitation at 6c033ba6ea

Claimed by: vm4 (manual, Nick at the console; cron paused)
Claimed at: 2026-10-18T19:20Z
Scope: one probe test in a scratch clone (not pushed): 2 shards, 2 replicas, 40 documents, `sort=_docid_ asc`, `rows=7`, pages at start 0, 7, 14, 21, 28, 35. Decides whether PR #5135's Known limitation section still holds at head 6c033ba6ea89335674573ea9041f2486af3f0095.
No PR edits beyond the approved body corrections; no pushes to solr-6975-submit.
Deliverable: receipts/SOLR-6975.md section for the paging run.

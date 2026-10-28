# Outcome: PR 5235 upload ContentWriter fix, round 1 (vm4)

- Receipt: `receipts/PR-5235-upload-contentwriter-fix-round-1.md`.
- Exact patch head: `e2ad60ecedda7451cb331538a5fd40b66144126b` on `nick-boss-tech/solr:solr-18351-submit`.
- Result: green on Linux at that head. The solrj `testUpload` method passes under both CI seeds, and the full `TestConfigSetAdminRequest` class passes (5 of 5). Tidy is clean before and after.
- Not covered: the Windows run, and Apache PR 5235 head `ccace897`.
- Logs: `/workspace/gates/logs/vm4/pr5235-upload-contentwriter-fix-round-1/`.

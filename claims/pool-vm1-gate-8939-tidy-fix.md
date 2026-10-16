# Claim: pool-vm1-gate-8939-tidy-fix

- Claimant host: vm1, matching a file in `hosts/`.
- Status: DONE.
- Job or assignment: `gates/SOLR-8939-tidy-fix-gate.md`.
- Slice: the whole job (the tidy fix commit on solr-8939-submit, then the fresh full gate at the new head).
- Capability tags used: gate.
- Started: 2026-10-16T06:29:56Z (UTC).

Heartbeat: 2026-10-16T06:29:56Z

Fix record: gates/SOLR-8939-live-head-gate.md ran at 0a97b52c7d97341d45817205ac1057669a756d50 and stopped GATE INVALID at step 1: `:solr:core:tidy` inserts exactly one blank line in `solr/core/src/java/org/apache/solr/handler/component/ResponseBuilder.java`, after line 208 (`public Map<Object, ShardDoc> resultIds;`), before the comment block preceding the new `pageShardDocs` field. This lane applies that one formatting fix as a new commit on solr-8939-submit (fast-forward push, plain), then runs the fresh full gate at the new head under gates/SOLR-8939-tidy-fix-gate.md, carrying the live-head job's steps 2 to 5 and both proof legs.

Result: 2026-10-16T07:14Z: tidy fix committed as 1a4a312bb7ab034fd932018691e76c86309f3b3d on solr-8939-submit (exactly the recorded blank line in ResponseBuilder.java; pushed plainly, fast-forward, ls-remote verified). Fresh full gate at 1a4a312bb7 GREEN (log g8939-tidy-fix-gate.log): changelog parse rc 0, tidy clean, Error Prone rc 0, proof leg A 2 tests with exactly 1 failure by content (date rendering mismatch), proof leg B probe 1 test with exactly 1 failure by content (expected 3, was 2), focused head run QueryComponentIdFormatTest 4 of 4 and StoredFieldsShardRequestFactoryTest 2 of 2 from fresh XML (seed 8939C0FFEE8939), module check rc 0.

DONE: 2026-10-16T07:14Z. Deliverables: the tidy fix commit 1a4a312bb7 on solr-8939-submit; gates/SOLR-8939-tidy-fix-gate.md (Outcome section); receipts/SOLR-8939.md (new top section).

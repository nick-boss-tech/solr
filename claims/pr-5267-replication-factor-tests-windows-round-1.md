# Claim: SOLR-18603 replication-factor API coverage, round 1

Status: CLAIMED. Immutable implementation claim; completion is derived from the report and gate below.

- Claimant: windows, Codex desktop; tags implementation, review, records.
- Started: 2026-10-31 04:42:02 UTC.
- Authority: Nick's direct request after the PR 5267 review: address the Medium coverage issue, then assign it to the gate, with pre-fix and post-fix runs.
- Board intake: 4b87ecf2138cfcf244eddd0521af5b202b56c718. Board workflow, start/playbook, host capabilities, decision map/ladder and owner complaints read. No competing PR-5267/SOLR-18603 record found.
- Contributor head O: 7f9acb9df610338c20537e1efd6ed75a6c64a13b, isaric/solr:SOLR-18603-seal-namedlist. Live PR head/discussion unchanged from review.
- Worktree: C:/Users/shaninna/dev/Solr-issues/wt/SOLR-18603; local branch wt-solr-18603-submit; fork destination solr-18603-submit. Both are absent at intake. Create directly at O because branch-work new requires an existing fork branch.
- Slice: test-only coverage in CloudHttp2SolrClientTest for complete per-route/per-shard factors with unequal values, minimum aggregation, missing factors, and the real routed response. Reuse the existing cluster, configset, clients and response containers. No new production seam or dependency.
- Decision ladder: rungs 1 and 3. The existing production code and maintainer JIRA discussion require preservation of the per-shard API. This is a coverage repair with no demonstrated production defect; O plus the new tests is expected to PASS, as is the final head F. Pre-PR production predates rfByRoute and is not an executable baseline for tests of that representation. Require two isolated compiling mutations at F (omit the route-map header; corrupt the per-shard factor) to show relevant assertion failures. Do not relabel an environment or compilation failure as fail-before proof.
- Windows: static review and manual formatting only. Linux gate owns tidy, focused and neighboring tests, root check, pre/post comparison and mutation proof.
- Deliverables: reports/pr-5267-replication-factor-tests-implementation-round-1.md and gates/pr-5267-replication-factor-tests-round-1.md; execution-host-owned receipts/PR-5267-replication-factor-tests-round-1.md. Commit and plain-push the test patch to the fork in this session, then pin the verified remote head in the gate.
- Public communication: no comment, PR body edit, approval, merge, or contributor-branch update authorized by this claim. Existing clean-gate follow-up rules govern any later public offer.

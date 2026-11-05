# Claim: SOLR-12613 lifecycle fixes on the actual PR head

Started: 2026-11-05 17:36:53 UTC. Host: Windows Codex desktop. Tags: implementation, review, draft, records.

Direct owner authorization: implement the remaining small correctness patches on top of the PR head, plain-push a scoped fork branch, hand it to the gate, and prepare a comment assuming a successful result. This authorizes our separate fork patch despite David's narrow approval of the contributor PR; it does not authorize changing the contributor/approved PR head or publishing the comment. No persistent goal is created or resumed.

Discovery board snapshot: 498833e1a8259b28506540e4bab61e8b86ddd89d. Current PR 5110 is OPEN at 6b5d830132b941faa85856347099ade8b190be0e, branch admin-ui-node-picker. David approved only his backend-scope concern and explicitly left remaining review to Eric/others. Claim is confined to the actual-head lifecycle patch, not the broader package arrangement or deprecation task.

Source scope: solr/webapp/web/js/angular/app.js and solr/webapp/src/test/org/apache/solr/webapp/AdminUiNodePickerTest.java. These two files are byte-identical between the actual PR head and the prior synthetic baseline 4e8aa913fce4a40b529fe89d8b5dd7930c29a86e. Reuse the reviewed two-file delta from 7f8d251d6c27200f3804f8b4e1298b9646f6cab4, inspect it again on this ancestry, and make any necessary scoped repair. Existing contributor feature changelog already describes the picker; no separate unreleased-feature bug fragment or unrelated author cleanup is needed for this author offer.

Target branch: nick-boss-tech/solr:pr-5110-core-menu-lifecycle. Worktree: C:/Users/shaninna/dev/Solr-issues/wt/PR-5110-core-menu-lifecycle. Preserve the contributor parent exactly; ordinary Nick Shanin commits, no trailers, no force or contributor push. The existing synthetic branch and all prior receipts remain preserved.

Decision ladder: rungs 1/3, pinned source review plus explicit owner direction. Guard obsolete core callbacks using request generation and captured node; defer initial cores until mode/target discovery; clear target/UI selection coherently and dispatch local fallback after metadata failure. Four existing prepared regressions exercise those boundaries. No backend/v2 API expansion, log migration or parameter deprecation enters this patch.

Verification: Windows source review and diff checks only. Required fresh Linux job at the exact pushed head must include baseline/candidate picker runs, all four intended runtime fail-before assertions with unchanged tests, named browser neighbors including the prior AddCore timeout selection, tidy/Error Prone/root checks, logs/XML/provenance, and final ref read-back. The old synthetic baseline receipt is not a prerequisite to this actual-head job or proof for it. Status after push stays Implemented, Proof Pending.

Deliverables: fork branch/head read-back; reports/solr-12613-pr-head-lifecycle-round-1.md; gates/solr-12613-pr-head-lifecycle-round-1.md; reports/solr-12613-pr-head-lifecycle-comment-draft-round-1.md with conditional post-green wording, pinned diff and fast-forward commands. Do not publish hypothetical passing counts. A comment review hold remains until Nick approves the wording or releases it; no automatic posting from a planning assumption.

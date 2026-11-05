# Assignment: arrange the adjacent branches into PR and Jira draft packages

Status: OPEN. Intended host: any capable review/draft/records host; implementation capability is required for branch changes.

## Owner direction and boundary

Nick asked for an assignment to arrange the existing branches into PRs with draft titles/bodies, then explicitly added a Jira title/body for each one. Prepare complete, reviewable submission packages and the necessary fork branch arrangement. Opening PRs, filing Jira tickets, editing live PR bodies or posting comments are not authorized by this assignment; publication follows Nick's review of the concrete packages.

This is a direct owner assignment, overriding the older generic pool exclusion of SOLR-12613 for this specific slice. Claim this assignment before work. Do not resume the cleared chat goal. Read WORKFLOW.md, START-HERE.md, UI-PLAYBOOK.md, pr-formula.md, comment-formula.md, jira/COMMENTS.md and the live issue/PR context. Discovery snapshot: `9138f9f3382f7661ca3da7731ae9fc3cc6fbc079`; refresh before claiming.

## Source inventory to reconcile

All branches are on nick-boss-tech/solr. These are the last verified inputs, not permission to ignore a newer head:

| Role | Branch | Head |
| --- | --- | --- |
| Encoded query-key fix, C2 | `solr-18537-encoded-key-followup` | `a676e7a392244e7cf4a35719fc64383b65cd947f` |
| Encoded path coverage/rejection, C5 | `solr-18537-path-coverage-followup` | `0fad9847cc89973c751b1732aa22a259764610b2` |
| Frontend-only synthetic picker baseline | `solr-12613-frontend-only-baseline` | `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e` |
| Selection/core-menu lifecycle, C3 | `solr-12613-selection-lifecycle-followup` | `7f8d251d6c27200f3804f8b4e1298b9646f6cab4` |
| SOLR-18619 UI request migration, C4 | `solr-18619-ui-request-targeting-followup` | `be369c41c633d639ad66a7291a4d82891ea9836c` |
| Combined candidate, reference/proof only | `solr-12613-adjacent-integration` | `84743822082ed2c409d87e40a6f8142e2a240042` |

Main baseline is `07ccc73cd24e26e59da169ca35b3fd5f64c35986`. The actual contributor PR is apache/solr 5110 / SOLR-12613, last read OPEN at `73b30956ff2c0f7b16f6af3bceacf95bda8271b9`; refresh it and its discussion. David's requested scope is frontend-only using X-Solr-targetNode, with no backend v2 API expansion: https://github.com/apache/solr/pull/5110#pullrequestreview-5778764327 . The synthetic baseline has not been adopted by the contributor.

Read `reports/solr-12613-frontend-scope-round-1.md`, `reports/solr-18537-transport-main-round-1.md`, `reports/solr-18537-path-coverage-repair-round-2.md` and `gates/solr-12613-scoped-integration-round-2.md` before arranging branches.

## Work and required decisions

1. Build a disposition matrix for every inventory row: standalone PR, dependent follow-up, author-review offer, absorbed upstream, or reference-only. Start from four candidate slices C2/C5/C3/C4; do not make a PR merely because a branch exists. The synthetic baseline and combined branch are not automatically extra PRs. Combine slices only with a concrete scope/review rationale and one coherent acceptance bar.
2. Search live Jira and GitHub for existing or competing work before proposing a ticket. SOLR-18537 in the transport branch names identifies provenance of a merged mechanism; it is not an automatic new-ticket identity. Reuse an appropriate existing ticket, propose an update to it, or draft a genuinely separate linked ticket. Do not reopen closed work or invent Jira keys. SOLR-18619 is the existing UI-migration ticket. Keep SOLR-15823 / OPEN PR 5031's logging-level GET broadcast separate, and do not duplicate the existing SOLR-18549 status-preservation work.
3. Arrange source so each proposed PR's diff against its intended base contains only its own scope. The UI branches inherit a synthetic picker baseline: do not submit them directly against main as competing copies of the contributor's PR. Reconcile with the contributor's revised/merged head or prepare an explicit dependent proposal. Record the base, exact head, prerequisites, outbound files and submission order. Preserve credit/ancestry; no contributor-branch changes, branch deletion, force-push or rewriting an open PR. Any authorized new fork source commit must be plain-pushed and independently read back in the same session.
4. Produce a PR title and complete body for every proposed PR, using the repository template and current formula. Include concrete behavior, scope, exact tested head and observed proof, dependencies, limits and AI disclosure. Keep the selected-node versus always-aggregate cloud logging choice in the C4 draft as Nick requested; explain the implemented selected-node default plus All nodes option and the alternative. Do not introduce an artificial choice for a settled small-scope decision.
5. Produce a Jira title and complete issue-description body for **each proposed PR**, including those linked to existing tickets. Label whether it is a new-ticket draft or proposed existing-ticket wording; preserve existing scope and show the proposed change rather than silently replacing live text. Include problem/reproduction or coverage gap, expected behavior, affected scope/version where verified, acceptance criteria, related tickets/PRs and verification limits. Use Jira wiki markup and the correct unobtrusive disclosure/footer. An unfiled issue uses a clearly marked `<JIRA-KEY>` placeholder in the matching PR draft; never fabricate an identifier.
6. Keep preparation moving while Selenium is pending. Nick permits assuming browser behavior for planning; an assumption is not a passing run or full validation. Coordinate evidence with `assignments/solr-12613-selenium-handoff-round-1.md`. The two independently green transport packages can be prepared without waiting for UI proof. Do not label any package fully verified unless the actual required receipt covers its final head.

## Evidence constraints

- Encoded keys: `receipts/SOLR-18537-encoded-key-main-round-2.md`, GREEN at `a676e7a392244e7cf4a35719fc64383b65cd947f`: 19+6 focused, 2+9 neighboring tests, four named runtime fail-before methods, tidy and root check. Only those four methods have the named negative proof; do not copy the receipt draft's broader claim that every new test failed before the fix.
- Path coverage: `receipts/SOLR-18537-path-coverage-main-round-2.md`, GREEN at `0fad9847cc89973c751b1732aa22a259764610b2`: main baseline 14; candidate 17+4; neighbors 2+9; tidy/root check; two URI-equality assertions fail on the valid `%23` to `%24` mutation. Rejection/query/header assertions are positive coverage pins, not mutation-proven. This is test-only work, not a newly reproduced production defect.
- UI baseline, lifecycle, request migration and combined proof remain separate jobs. Refresh their actual receipts; do not infer them from the transport results or static test counts. A changed source/base/formatting head needs appropriate fresh proof; a new alias pointing at the unchanged tested commit does not itself change that commit's evidence.
- Drafts may say prepared, assumed or proof pending. They must not claim browser tests passed, the contributor adopted the synthetic baseline, or Nick reviewed AI wording before that review occurs. No internal board/process vocabulary in public-facing text.

## Deliverables and finish

- `reports/solr-12613-pr-jira-packages-round-1.md`: self-contained disposition/submission matrix, final branch/base/head/file lists, dependency order, evidence table, remaining blockers and links to each package.
- One add-only report per proposed PR under `reports/solr-12613-package-<slice>-round-1.md`, containing the full PR title/body **and** Jira title/body, ticket disposition, source/revision links and explicit draft/readiness status. These must be readable from the board without accessing a worker's local files.
- If source branches are changed, record exact old/new heads, fork read-backs and replacement proof jobs. Do not edit another host's receipt; post an add-only report and let the execution host own proof updates.

Done means every source row has a disposition and every proposed PR has both complete title/body pairs and a concrete branch/base strategy. A plan to write drafts later is not a deliverable. Leave filing/opening/posting for Nick's explicit approval; report the package links to him without publishing them.

# Assignment: SOLR-17914 create request and error mapping (round 3)

Status: DONE (vm4, deliverables below)
Tags: implementation, ui, jvm-tests, records
Posted by: vm4, 2026-10-28, at the direction of Nick Shanin: implement the decisions D2, D10 and D11 from `reports/solr-17914-decisions-round-2.md`, then draft the PR body with the choices posed as recommendations.

## Work

Branch `solr-17914-create-collection-dialog-new-design` at `39efaef802`. New commits only.

1. Create request (D10): `CollectionsRepository` interface and `HttpCollectionsRepository`, posting to `api/collections` with the mapping in the round-2 report.
2. Server error mapping (D2): a 400 whose message is "collection already exists" maps to the name error. Any other failure maps to the general error, with the server message when present (D11), else "Could not create the collection.".
3. Tests: Ktor mock engine for the request body and for both error mappings. Mutation check on the name-exists mapping (rung 2).
4. Gate: `./gradlew -p ui :shared:jvmTest :shared:spotlessCheck --no-daemon --rerun-tasks` under `/workspace/tools/.gradle-vm4.lock`. Logs `/workspace/gates/logs/vm4/solr-17914-create-request-round-3/`.
5. Draft `drafts/pr-solr-17914-create-collection-dialog.md` (replaces the round-1 draft in the branch's history): the PR body follows `pr-formula.md`, states each choice as a recommendation with its ladder rung, and poses only the open maintainer questions.

## Limits

- The header action (D8, D9) is not built; it depends on SOLR-17912.
- No PR, no Jira or GitHub post.

DONE: 2026-10-28T04:45:27Z. Deliverables: \`drafts/pr-solr-17914-create-collection-dialog.md\`, \`receipts/SOLR-17914-create-request-round-3-vm4.md\`. Head \`6170ba10ac\`.

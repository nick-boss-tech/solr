# Gate job: SOLR-18446 (path-scoped collection routing)

Status: CLAIMED (claims/phone-agent-2-blockers-round-1.md)

- Branch: solr-18446-path-routing-phone-agent-2. Head: `23db47ef150dd87198718085a9d28cffdf4ada4c`, pushed to fork 2026-10-22.
- Prior gated head: none. Base: `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`.
- Claiming host: phone-agent-2. Capability tags: gate, implementation, review. Covered by the directed round claim.
- Module: `:solr:core`. Focused class: `org.apache.solr.servlet.HttpSolrCallCloudTest`. Seed: `18446` passed as project property `tests.seed`.
- Diff scope: HttpSolrCall.java, HttpSolrCallCloudTest.java, AliasIntegrationTest.java, and changelog/unreleased/SOLR-18446-path-controls-collection-routing.yml.
- Why: the ticket's concrete parameter-over-path precedence remains in main. The patch makes path candidates authoritative while retaining permitted narrowing within multi-destination paths.

Links: Ticket: SOLR-18446. Category: SolrCloud and ZooKeeper; secondary Search and query components. Siblings: broader SOLR-18470 is excluded. Supersedes: no earlier implementation or receipt.

## What to run

Runner: `/workspace/tools/phone-agent-2-hardening-gate.py`, configuration `/workspace/phone-agent-2-logs/gate-18446.json`. Logs: `/workspace/phone-agent-2-logs/gate-18446-attempt-1`. Shared lock `/workspace/tools/.gradle-$(hostname -s).lock`; disk guard before each build. This follows SOLR-17844 serially, never in parallel.

0. Parse the changed changelog YAML fragment.
1. Root `tidy`; record and commit any repair before a retry.
2. `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
3. Pre-fix proof: keep head tests and fixture, replace only HttpSolrCall.java with the pinned upstream base. Expected `testCollectionPathGovernsRouting` failure selects collection2 where collection1 is expected. Check exact XML content, not merely a nonzero exit.
4. Restore committed head, clean test outputs, run full HttpSolrCallCloudTest, archive fresh XML and count tests/skips/failures/errors.
5. `:solr:core:check -x test`.

## Decisions

| Decision | Answer | Failure route |
| --- | --- | --- |
| Path contract | Path supplies routing candidates; parameter may narrow only an existing multi-destination path | Do not implement SOLR-18470 or invent new 400 semantics |
| Proof base | Only production routing class returns to upstream base; new regression remains | Unrelated failure is not accepted |
| Repair | WORKFLOW gate repairs, one tree-changing repair and one full retry | Failed retry is HANDED BACK |

## Checklist

- [ ] fork head read back
- [ ] step 0 YAML parse
- [ ] step 1 tidy
- [ ] step 2 Error Prone compile
- [ ] step 3 pre-fix discriminating failure
- [ ] step 4 focused XML counts
- [ ] step 5 module check
- [ ] receipt written
- [ ] terminal gate status and round claim updated

No external posts, PR opening, ready flips or force pushes. Commits as Nick Shanin without trailers.

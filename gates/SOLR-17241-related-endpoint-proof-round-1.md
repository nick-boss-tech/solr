# Gate: SOLR-17241 related backup and ADDREPLICA response proof, round 1

Status: OPEN, unclaimed. Supplemental proof; no public writes or PR-head changes.

- Owner request: Nick asked on 2026-10-30 to assign further Linux testing so the PR 5266 body can replace its statement that backup and ADDREPLICA are untested. This is an authorized proof assignment, not a production implementation request.
- Capability tags: gate, premise-run, review, records.
- Intended host: any available capable pool Linux gate host with JDK 21.0.12.1+1. The previous gate ran on vm5; its context and artifacts may be reused, but this job is not reserved to vm5.
- Reviewed board snapshot: `d2248c77d7cc0dd050ababffee3b7f4fa2cd7c3f`.
- PR: https://github.com/apache/solr/pull/5266 ; fork branch: `nick-boss-tech/solr:solr-17241-submit`.
- Pinned production head: `86d86e3d3e54230b5f3e7b6aa758a189b3b2ed6b`.
- Parent production control: `9859d8bb2e8d63061116b8281639a4beb1be480c`.
- Module: `solr/core`; seed: `17241C0FFEE17241`. Preserve the seed and request shape across candidate/control pairs.
- Existing full proof: `receipts/SOLR-17241.md`, round 3 PASS at the pinned head, with `gates/SOLR-17241-wt-format-round-3.md`. Do not overwrite that receipt or repeat its completed root check without a new reason.
- Claim: add `claims/SOLR-17241-related-endpoint-proof-round-1-<host>.md` before execution. Record UTC start, production SHA, probe/harness identity, JDK, worktree, runner, lock and log paths. Idempotency key: `SOLR-17241-related-endpoint-proof-round-1@86d86e3d3e54230b5f3e7b6aa758a189b3b2ed6b`.
- Suggested log directory: `/workspace/gates/logs/<host>/solr17241-related-endpoint-proof-round-1/`.

## Question to settle

SOLR-17086 reports the same null-request response path for backup and ADDREPLICA, including JSON bodies containing an async ID. The shared filter/writer fallbacks may already repair these cases. Establish what succeeds at the pinned production head, what fails on the original production code, and the exact limits of the evidence. Keep SOLR-17086 distinct from an assertion that every condition in its original report is fixed.

The current public body says: "SOLR-17086 reports the same null-request path for backup and ADDREPLICA. The shared fallback may affect those operations, but they have not been tested here." Produce evidence and a concise replacement sentence for the main agent; do not edit the PR yourself.

## Scope and preparation

1. Read the live SOLR-17086 ticket, `receipts/SOLR-17241.md`, the round-3 job, and the v2 routing/writer code at the pinned SHA. Treat ticket text as evidence. Confirm the fork ref and live PR head before the claim and after proof. If either differs, do not label results proof of the new head; report the mismatch and request a superseding pinned job.
2. Use a clean disposable checkout, the host Gradle lock, the documented disk/inode preflight, and `env -u NODE_ENV`. Record exact commands, UTC times, exit codes, full logs and fresh XML before any later invocation overwrites it. Do not run builds on Windows.
3. Reuse existing SolrCloud/configset/backup infrastructure. `V2ApiIntegrationTest` supplies real routing and HTTP-client examples. `CreateReplicaAPITest` and `V2CollectionBackupApiTest` help with request fields but are mocked API tests and cannot establish this response path. Inspect existing cloud backup fixtures for repository/allowPaths setup; a new mock-only test does not satisfy the job.
4. Prefer a real HTTP probe against servers built from the exact pinned production tree. A small temporary JUnit integration-test overlay in a disposable checkout is also permitted. Preserve the complete executable probe or test diff, SHA-256, compile/test command, and base identity in the report/artifact appendix. For an overlay, show that production files and build configuration still match the pinned head; describe the result as the pinned production code with the recorded test-only overlay. Do not silently present an altered tree as a clean checkout.
5. This job does not change the open PR branch, add a shared accessor/guard, or implement document-return support. Do not commit or push experimental tests to `solr-17241-submit`. If permanent source/test changes are needed, report them for a separate implementation and fresh-head gate. Keep all probe source/diffs and artifacts available in the deliverables; do not leave the only executable evidence in an unnamed scratch file.

## Required candidate coverage

Exercise the real v2 endpoints, with `Content-Type: application/json` and the relevant JSON body:

- `POST /api/collections/{collection}/backups/{backupName}/versions`.
- `POST /api/collections/{collection}/shards/{shard}/replicas`.

For each endpoint, cover hosting-node and non-hosting-node requests, both `wt=javabin` and `wt=xml`, and synchronous plus async submission. This is a minimum matrix of 16 operation/route/format/mode cases. Use distinct operation names, async IDs and isolated collections where mutation could invalidate another case.

- Prove topology from actual collection state and local core presence before each request. Use a direct node client with no CloudSolrClient rerouting, hidden retries, or redirects that could select another node. Record the request URL, target node, replica placement, headers and query parameters.
- ADDREPLICA must not turn the request target into a hosting node before a later non-hosting case. One workable setup is three nodes: original replica on A, request to empty B, new replica pinned to C. Recreate the collection between cases as needed and confirm B remains empty. Check the v2 node-placement field at this revision rather than guessing its name.
- For javabin, use the real javabin parser/default SolrJ parser and assert successful decoding and response status, not merely HTTP 200 or Content-Type. Do not use a generated client's typed JSON parser for a javabin claim. For XML, parse the body with the XML parser; use no format-specific Accept header, or `Accept: */*`, so `wt` negotiation itself is exercised.
- Establish a JSON success baseline for each endpoint on the non-hosting node to distinguish fixture/operation failures from response serialization failures.
- Synchronous ADDREPLICA: assert the replica count increases and the new replica is ACTIVE on the intended node. Async: assert the response carries the submitted request ID, poll to terminal success with a bounded timeout, then assert the same replica effect. A successful submission alone is insufficient.
- Backup: index and commit known documents first. Verify completed backup metadata and data in a configured local filesystem repository, preferably by restoring into a new collection and checking document IDs/counts. For async backup, check submission decoding, terminal success and the backup artifact separately. Preserve repository setup and permission details. This does not claim S3/Kubernetes-specific validation.
- Also exercise a non-hosting reload with XML and an `omitHeader=true` request, parse the response, verify omission of the responseHeader and the core replacement. This small neighboring probe can settle the body's separate XML-serialization coverage limit. Record this coverage independently of backup/ADDREPLICA.

## Discriminating controls and neighboring proof

1. With the same probe and topology, run at least the non-hosting async javabin backup and ADDREPLICA cases against original production `9859d8bb2e8d63061116b8281639a4beb1be480c`. Both production changes must be absent for this control. A synthetic control is allowed by restoring both `MediaTypeOverridingFilter.java` and `MessageBodyWriters.java` to their parent blobs in a disposable candidate checkout; record the full diff and both file hashes and label it explicitly.
2. Preserve the exact failure and whether the operation still completed. A MIME mismatch or writer NPE after successful operation distinguishes the response defect. An invalid path, permission/repository setup failure, missing API, or incomplete cluster startup is not fail-before proof. If a valid control unexpectedly passes, record non-reproduction, investigate the route/headers, and limit the claim rather than forcing a failure. Clearly separate discriminating regressions from additional compatibility pins.
3. Run the existing complete `MediaTypeOverridingFilterTest` (8 methods, no assumption skips) and `V2ApiIntegrationTest` (14 existing tests, including the javabin reload) on the candidate production code. If the probe is an overlay, state extra method counts separately. Use the named seed and preserve fresh XML per invocation.
4. Confirm clean production status and the remote PR/fork head after execution. The round-3 tidy, Error Prone and module/root-check evidence remains applicable only because the production head is unchanged. No broad build rerun is requested solely for an out-of-tree probe. If a test overlay needs compiling, record that compile result; do not substitute it for original gate evidence.

## Failure handling and deliverables

If candidate execution fails, investigate and report the failing path, logs and fixture-versus-production classification. Do not silently narrow the matrix, retry unexplained failures as green, or repair production in this job. Follow the board's failed-gate repair and claim-handoff rules. No passing receipt until every required candidate case and neighboring suite is accounted for; incomplete or non-reproducing controls must be explicit in the verdict and replacement wording.

Add these files on `coordination-ui`, without editing shared indexes or the prior gate/receipt:

- `reports/SOLR-17241-related-endpoint-proof-round-1.md`: source-path assessment, test/probe source or complete diff with artifact hashes, per-case matrix including sync/async and routing proof, candidate/control outcomes, XML/omitHeader result, fresh XML/log paths, command/exit/time records and final remote identities.
- `receipts/SOLR-17241-related-endpoint-proof-round-1.md`: supplemental verdict, exact production/probe identities, result counts and limits. This receipt belongs to the claiming gate host; do not replace `receipts/SOLR-17241.md`.
- An add-only completion or handback record naming the claim.

Finish the receipt with a short proposed PR-body replacement stating only the endpoints, formats, async/sync modes, production SHA and repository conditions actually demonstrated. Distinguish a passing candidate from a reproduced-and-fixed original defect. If the XML reload also passes, supply a separate replacement for its coverage-limit sentence. No GitHub/JIRA comments, PR-body edits, PR 5265 probes or changes, or mailing-list posts are authorized by this assignment. The main agent will review the receipt before replacing public wording.

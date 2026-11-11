# Portable board prototype

These tools need Python 3 and Git, a pre-existing repository and branch, configured Git identity, and push access. It uses normal Git credentials; do not put credentials in record JSON, command arguments or remote URLs. Every writer must follow the same protocol. The server's branch protection and permissions still apply. Check the [cutover announcement](https://github.com/nick-boss-tech/solr/blob/coordination-ui/reports/coordination-v2-cutover-2026-11-06.md) before using v2 as the live queue.

## Records and writes

`board.py publish --remote REMOTE --branch BRANCH --file RECORD.json` creates one JSON record with a fast-forward Git push. Put `--repo PATH` before the subcommand if running outside the repository. It fetches a private temporary ref, builds a commit with a temporary index, pushes without force, then fetches again and checks both ancestry and stored content. On a competing update it retries at most twice after re-reading the new snapshot; it never overwrites an existing record. Commit trailers provide type, job and, when applicable, source head and verdict. The JSON records themselves are authoritative; trailers are a useful audit aid, not a complete schema.

Records have `type`, `id` and `job`, each identifier using lowercase ASCII letters, numbers, `.`, `_` or `-`. The seven types are `assignment`, `claim`, `release`, `gate`, `receipt`, `report` and `evidence`. Assignment, claim and release IDs must equal the job ID, giving each job one canonical path of each type. A claim requires `owner`; a release requires `actor`, `reason` and `evidence` and an existing claim. A successor assignment has a new ID and `successor_of` pointing to a job with a release. These checks do not prove the person or executor stopped: review the evidence before acting. Other record IDs are unique within their type. A gate has a full source `head`; a receipt has `gate`, `head` and `verdict` (`passed`, `failed`, `incomplete`, or `non-reproduced`). Other details such as commands, required checks and links can be added as JSON fields. A non-assignment requires its assignment to exist; a receipt requires the named gate to belong to the same job. Check that all expected proof legs were actually executed before relying on a receipt.

Example assignment:

```json
{"type":"assignment","id":"demo-1","job":"demo-1","summary":"Investigate duplicate IDs"}
```

Example claim:

```json
{"type":"claim","id":"demo-1","job":"demo-1","owner":"worker-a"}
```

For a new gate assignment, name the gate record explicitly. The gate ID may
be different from the job ID. These extra fields are a handoff convention. The
writer does not validate their content or verify that the paths match:

```json
{"type":"assignment","id":"demo-gate-job","job":"demo-gate-job","intended_host":"Linux","gate_record":"records/gate/demo-gate-1.json","gate_id":"demo-gate-1","claim_path":"records/claim/demo-gate-job.json","receipt_path":"records/receipt/demo-receipt-1.json","source":{"repository":"owner/repo","branch":"example","head":"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"}}
```

Publish the assignment and gate separately. Read both back before handing them
off. The recipient checks the host, source head and ownership before claiming
`records/claim/demo-gate-job.json`.

For an older assignment with no gate path, search the fetched snapshot by job ID.
For example, from the Windows workspace root, use the full fetched SHA:

```powershell
.\research\git-source.ps1 grep '-n' 'demo-gate-job' <board-sha> '--' records/gate
```

Read the candidates and match their `job`, head and phase. Follow
[WORKFLOW.md](../WORKFLOW.md#find-and-hand-off-a-gate) for missing or superseded
records. Do not treat a text match or the generated index as proof of ownership.

Example gate and receipt for a separately claimed gate job. Replace the example head and fields with the actual source revision, commands and result; a receipt's `passed` value does not make omitted proof legs pass:

```json
{"type":"gate","id":"demo-gate-1","job":"demo-gate-job","head":"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","requirements":["focused test","format check"],"environment":"document the execution host and toolchain"}
```

```json
{"type":"receipt","id":"demo-receipt-1","job":"demo-gate-job","gate":"demo-gate-1","head":"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","verdict":"incomplete","results":"record actual commands, exit codes, proof legs and evidence links"}
```

Competing publishers using this tool contend on the same claim path. A rejected push is retried only after checking that path at the new tip. This is not protection from writers who bypass the protocol. Claims are permanent and a release is a separate record, not a lock. There is no automatic lease, heartbeat, takeover or recovery for a dead worker. Do not delete a claim or create a successor while the original worker may still run. Verify release evidence and follow [the workflow](../WORKFLOW.md).

Example release and successor (the release does not itself create or claim the successor):

```json
{"type":"release","id":"demo-1","job":"demo-1","actor":"worker-a","reason":"handoff","evidence":"Link to verified stop and handoff report"}
```

```json
{"type":"assignment","id":"demo-2","job":"demo-2","successor_of":"demo-1","summary":"Continue after the documented handoff"}
```

Write each example to a local JSON file and publish separately, e.g. `python tools/board.py --repo . publish --remote origin --branch YOUR_TEST_BRANCH --file /tmp/claim.json`. Use an isolated test branch for examples, not the live queue. For real work use the active board branch and actual task data. To inspect an already fetched commit, use `python tools/board.py --repo . index --snapshot COMMIT`.

On Windows, `tools/board-windows.ps1` can supply a Git binary directory and author identity without changing the repository configuration. Its optional `-CredentialFile` loads a GitHub token only in the process and invokes `gh auth setup-git`; omit it for a local test repository. It takes the board subcommand and arguments after its named options. The workspace operator must use the correct local Git, Python and credential-file paths; do not print or commit the credential. The wrapper does not select or activate a board branch.

`python tools/test_windows_wrapper.py WindowsWrapperTest.test_local_publish_without_credentials` tests the wrapper against an isolated local Git remote on Windows with PowerShell 7. It does not test GitHub credentials or a real host's board settings.

`board.py index --snapshot COMMIT` prints JSON with the exact resolved commit, records for each job, conservative status and receipt/gate head comparisons. It only indexes `records/` in this prototype, not legacy Markdown, current remote source heads, or execution state. It never marks a job green or done; a head match is not evidence that required legs passed. Regenerate after writes when needed; no generated index is automatically committed.

## Evidence

`board.py evidence --file LOG --id ID --job JOB --head FULL_SHA --location URL` streams a local file through SHA-256 and prints a JSON pointer. It does not upload, verify remote contents, set access control or prove that the URL is immutable. The caller selects an authorized evidence store, uploads and independently verifies the artifact, then publishes the pointer with `publish`. Supported pointer schemes are `https`, `s3` and `gs`; storage credentials never belong in the pointer. Large raw logs should not be added to the board by this tool. All board records use one Git write path, avoiding an opaque size-based switch between API and Git transports; repository or server limits still apply.

## Tests

`python tools/test_board.py` creates temporary local Git repositories and tests simultaneous claims, conservative indexing, mismatched gate heads and evidence hashing. `python tools/test_release.py ReleaseTest.test_release_and_successor_require_prior_claim` checks the release and successor preconditions. These tests never contact a network service or the live board. Windows authentication and sequential duplicate refusal were separately tested on an isolated fork branch; other host environments, remote concurrent claims, process death and late writes remain untested.

## Legacy Markdown compatibility

`legacy_publish.py --repo REPO --remote REMOTE --branch BRANCH --manifest BATCH.json --message MESSAGE` is an experimental checked batch writer for existing board paths. Each manifest entry has exactly `path`, `file`, and `expected_blob`: use `null` to require an absent path or the full reviewed Git blob ID to require an exact existing version. The tool reads the local payload once, checks every precondition at a fetched tip, creates one fast-forward commit, and checks every precondition again before retrying a rejected push. It verifies the commit is reachable and the written blobs are still at the live tip. It accepts binary data, but putting large evidence into Git is a separate project policy decision. Paths are restricted to the documented board record directories. It never deletes or force-pushes.

For example, a two-file create-only batch is:

```json
[
  {"path": "claims/example.md", "file": "/tmp/claim.md", "expected_blob": null},
  {"path": "reports/example.md", "file": "/tmp/report.md", "expected_blob": null}
]
```

An expected blob protects a version, not permission to edit it: apply the live board's single-writer rules and do not edit immutable claims. Different claim paths for the same assignment can still both win. This tool does not solve legacy ownership reconciliation, host locks, access control, stale executions or takeover. It does not automatically select a board. Run `python tools/test_legacy_publish.py LegacyPublisherTest.test_batch_and_stale_edit` and `python tools/test_legacy_race.py LegacyRaceTest.test_same_claim_path_one_winner` for isolated local Git tests. A test requiring credentials, remote service semantics, process death or host-specific scripts remains outstanding.

## Snapshot inventory

`inventory.py snapshot --repo REPO --revision COMMIT --out MANIFEST.json` records every tree entry's mode, Git object ID and size; it extracts titles, reported status lines and path mentions from bounded legacy Markdown records. It does not infer ownership, freshness, completion or the existence of all referenced external evidence. `inventory.py compare --old OLD.json --new NEW.json --out DELTA.json` lists added, removed and changed paths and modes; review the actual records and host state before acting on the delta. Both operations are read-only with respect to Git branches. The pinned [Solr inventory](../migration/legacy-inventory-0d91325.json) and [readiness assessment](../migration/readiness.md) are snapshots, not the live board.

`work_catalog.py --inventory MANIFEST.json --repo OWNER/REPO --json-out CATALOG.json --markdown-out CATALOG.md` creates a source-linked list of all legacy assignment, claim and gate paths. The JSON includes literal path mentions and incoming mentions as search aids, not authoritative relationships. Run `python tools/test_work_catalog.py` for the small catalog test. These catalogs are references for selective carryover; they do not create jobs or claims on the fresh board.

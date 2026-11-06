# Portable board prototype

This prototype is for isolated evaluation, not the live Solr board or the v2 migration. It needs Python 3 and Git, a pre-existing repository and branch, configured Git identity, and push access. It uses normal Git credentials; do not put credentials in record JSON, command arguments or remote URLs. Every writer must follow the same protocol. The server's branch protection and permissions still apply.

## Records and writes

`board.py publish --remote REMOTE --branch BRANCH --file RECORD.json` creates one JSON record with a fast-forward Git push. Put `--repo PATH` before the subcommand if running outside the repository. It fetches a private temporary ref, builds a commit with a temporary index, pushes without force, then fetches again and checks both ancestry and stored content. On a competing update it retries at most twice after re-reading the new snapshot; it never overwrites an existing record. Commit trailers provide type, job and, when applicable, source head and verdict. The JSON records themselves are authoritative; trailers are a useful audit aid, not a complete schema.

Records have `type`, `id` and `job`, each identifier using lowercase ASCII letters, numbers, `.`, `_` or `-`. The six types are `assignment`, `claim`, `gate`, `receipt`, `report` and `evidence`. Assignment and claim IDs must equal the job ID, giving each job one canonical assignment path and one canonical claim path. Other record IDs are unique within their type. A gate has a full source `head`; a receipt has `gate`, `head` and `verdict` (`passed`, `failed`, `incomplete`, or `non-reproduced`). Other details such as owner, commands, required checks and links can be added as JSON fields. A non-assignment requires its assignment to exist; a receipt requires the named gate to belong to the same job. Check that all expected proof legs were actually executed before relying on a receipt.

Example assignment:

```json
{"type":"assignment","id":"demo-1","job":"demo-1","summary":"Investigate duplicate IDs"}
```

Example claim:

```json
{"type":"claim","id":"demo-1","job":"demo-1","owner":"worker-a"}
```

Competing publishers using this tool contend on the same claim path. A rejected push is retried only after checking that path at the new tip. This is not protection from writers who bypass the protocol. Claims are permanent in this prototype: there is no automatic release, lease, heartbeat, takeover or recovery for a dead worker. Do not use it to coordinate real workers until those semantics and enforcement are designed and tested. In particular, do not delete a claim or create a second path to transfer work while the original worker may still run.

`board.py index --snapshot COMMIT` prints JSON with the exact resolved commit, records for each job, conservative status and receipt/gate head comparisons. It only indexes `records/` in this prototype, not legacy Markdown, current remote source heads, or execution state. It never marks a job green or done; a head match is not evidence that required legs passed. Regenerate after writes when needed; no generated index is automatically committed.

## Evidence

`board.py evidence --file LOG --id ID --job JOB --head FULL_SHA --location URL` streams a local file through SHA-256 and prints a JSON pointer. It does not upload, verify remote contents, set access control or prove that the URL is immutable. The caller selects an authorized evidence store, uploads and independently verifies the artifact, then publishes the pointer with `publish`. Supported pointer schemes are `https`, `s3` and `gs`; storage credentials never belong in the pointer. Large raw logs should not be added to the board by this tool. All board records use one Git write path, avoiding an opaque size-based switch between API and Git transports; repository or server limits still apply.

## Tests

`python tools/test_board.py` creates temporary local Git repositories and tests simultaneous claims, conservative indexing, mismatched gate heads and evidence hashing. It never contacts a network service or the live board. Git credential helpers, remote APIs, host-specific environments, process death and late writes remain untested. Review and extend these tests before deployment.

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

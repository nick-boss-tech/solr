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

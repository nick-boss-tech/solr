# Pool assignment: SOLR-18249 backup metadata path and async-status reproduction, round 1

Status: DONE (2026-10-23, phone-agent-2; reports/SOLR-18249-backup-metadata-repro-round-1.md)

- Capability tags: review, premise-run.
- Category: Replication, backup, and restore (category 5 in `pool/work-map.md`).
- Intended host: any pool Linux host with these capabilities. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: Windows lane coordinator, 2026-10-22, at the direction of Nick Shanin.

The SOLR-18249 ticket concerns non-atomic writes in `ShardBackupMetadata.store()`. A sanitized production trace supplied for this investigation records a collection `BACKUP` request lasting about two hours. The request includes `incremental=false`; a shard response reports a successful snapshot with `snapshotCompletedAt` and `fileCount`; the parent request later fails after a child `REQUESTSTATUS` returns `notfound` six times. Some snapshot data was reported present. The deployed Solr version and the name of the missing metadata artifact are unknown. This trace is not proof that the missing file is `ShardBackupMetadata` or that current-main async expiration caused the incident.

## Items

1. **Identify the expected metadata artifact.** Trace the logged backup API path against the Solr source version or release that can produce the observed response. Distinguish collection-level backup properties, incremental shard metadata, and any metadata used by the full-snapshot path. If the exact deployed version cannot be inferred, list the compatible paths and the specific version or artifact detail needed to choose between them. Start from: `solr/core/src/java/org/apache/solr/cloud/api/collections/BackupCmd.java`, `solr/core/src/java/org/apache/solr/handler/SnapShooter.java`, `solr/core/src/java/org/apache/solr/core/backup/BackupManager.java`, and `solr/core/src/java/org/apache/solr/core/backup/ShardBackupMetadata.java` on the inspected revision. Output: `reports/SOLR-18249-backup-metadata-repro-round-1.md`.

2. **Test the async-status expiry hypothesis.** Check whether the matching code submits shard tasks together, polls their status IDs sequentially, and can lose a later unpolled ID while an earlier shard is still being awaited. Verify the applicable timeout defaults and consider node restart or configuration differences as alternative causes. Use existing test seams or a deterministic fake ticker; do not wait for a real 60-minute timeout. Run focused tests only on a Linux host. If the current test seams cannot prove the end-to-end sequence, report the exact missing seam and the smallest test that would prove it. Start from: `solr/core/src/java/org/apache/solr/cloud/api/collections/CollectionHandlingUtils.java`, `solr/core/src/java/org/apache/solr/handler/admin/CoreAdminHandler.java`, and the relevant existing test classes. Output: `reports/SOLR-18249-backup-metadata-repro-round-1.md`.

3. **Compare the failure point with metadata finalization.** Determine whether the `notfound` path exits before the expected metadata is written, and what cleanup runs afterward on the matching version. Classify the result as reproduced, mechanism confirmed but production match unknown, refuted, or unresolved. Do not call this a reproduction of SOLR-18249 unless the test matches the missing artifact and backup path, not just the generic async timeout behavior. Output: `reports/SOLR-18249-backup-metadata-repro-round-1.md`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Missing artifact | default: unknown until established from code | if the production version or exact artifact cannot be established, report the alternatives and the minimum evidence needed; do not guess | Nick Shanin |
| Production access | decided: synthetic tests and source only | do not access production Solr, repositories, credentials, or backup data | Nick Shanin |
| Test changes | decided: no tracked source changes in this assignment | use existing tests or a temporary local test fixture; remove any temporary fixture after capturing results, then describe the minimum permanent test in the report | Nick Shanin |
| Conclusions | decided: separate evidence from inference | if the async mechanism is demonstrated but the production version or artifact is unknown, classify the production match as unresolved | Nick Shanin |

## Deliverable

`reports/SOLR-18249-backup-metadata-repro-round-1.md`: record the inspected Solr tag or commit, expected artifact names and write points for each relevant path, test command and result, cleanup behavior, and a conclusion using the classifications above. Do not include production hostnames, collection names, filesystem paths, request IDs, credentials, or copied production payloads.

Then update this file's status to DONE with the completion date and point to the report.

## Limits

- No production changes or live backup attempts. No PR, JIRA comment, GitHub comment, or email. No force push and no push to `apache/solr`.
- Do not change or push Solr source or tests in this assignment. A temporary local test fixture may be used only to verify the behavior; do not leave it in a checkout or publish it.
- Do not state that the incident is reproduced unless the missing metadata artifact and backup path are matched by the test.
- Commit coordination records as Nick Shanin with no trailers. No em dashes or en dashes.

# Skip record: SOLR-15371

- Ticket: SOLR-15371, Backups randomly fail sometimes. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-04-25, updated 2021-08-02, no fix versions, no links.
- Verdict: keep-open.
- Draft branch: solr-15371-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at this head (detached worktree).
- Source: remediation redo of batch 03, item 11, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) is background only.

## Ticket reading

A collection backup to a shared NFS mount fails for one shard in some runs with "Directory to contain snapshots doesn't exist". The reporter says the failure follows the node that receives the backup call (the leader of the failing shard); restarting that node or moving the shard's replicas off it makes the backup pass. The reporter's directory listing shows the backup directory created with the other shards' subdirectories but not the failing shard's.

## Code reading

- solr/core/src/java/org/apache/solr/handler/admin/api/CreateCoreBackup.java:109-114: the per-core backup builds a SnapShooter on the shared location and checks `repository.exists(snapShooter.getLocation())` on the node that runs the core. The comment at :109-113 says the directory "really should, in fact must, already exist here if triggered via a collection backup on a shared file system", and that the code throws rather than creating it.
- CreateCoreBackup.java:115-122: if the check fails, the node throws BAD_REQUEST "Directory to contain snapshots doesn't exist ... requires a shared file system mounted at the same path on all nodes", which is the message in the ticket. BackupCoreOp.java:68 wraps it as "Failed to backup core=...".
- The directory is created by the collection-level backup step on the node that received the API call (BackupManager.java:270-271, :364 and :426 create directories through the repository). Each replica then checks for it on its own node. So on a shared mount, correctness depends on the other node seeing that directory when the check runs.

The code is consistent with the report: a replica whose node does not see the newly created directory yet (NFS attribute or directory caching, or the create happening after the check) fails the check. Nothing in this tree changes that behavior. The reporter's workaround (moving shards off the backup node) fits the case where the leader and the receiving node differ in how they see the mount.

## History

No commit names SOLR-15371. The check comment at CreateCoreBackup.java:109-113 is the design note; it says the code throws instead of creating the directory, so the fix choice (retry, create-before-dispatch, or a mount check) is open.

## Reproduction attempts

Not run. A reproduction needs a shared mount that is visible with the NFS caching the reporter used, several nodes, and a collection backup repeated until one shard fails. This environment has one host and no shared NFS mount.

## Gap analysis

The mechanism is in the code, and the design note says the strict check is deliberate. The open question is whether the failure is a timing or caching problem on the shared mount (environment), or a missing step in Solr (the directory should be made visible or created before the per-core requests). Reading does not separate those; a multi-node run on a shared mount does.

## Verdict justification

keep-open: the attempts made are the code path reads above at the pinned head and the Jira read. The missing piece is a multi-node run on a shared mount reproducing the one-shard failure, with the directory listing and the replica's view at the failure time, and a decision on whether Solr should wait for or create the directory before the per-core backups. No closing verdict is supported: the ticket is open, the behavior is not obsolete, and no fix is on main.

Missing for a closing or PR verdict: (1) the multi-node shared-mount run with its listing and log; (2) a decision on the fix design (retry or create-first).

## Limits

This record does not reproduce the failure. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.

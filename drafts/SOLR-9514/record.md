# Skip record: SOLR-9514

- Ticket: SOLR-9514, "deprecate BALANCESHARDUNIQUE command in favor of the more generic ADDREPLICAPROP command". JIRA status read 2026-10-20T13:40:33Z (UTC): Open, resolution None, updated 2019-06-08. Five 2016 comments; the thread agrees to deprecate.
- Verdict: keep-open (kept; re-read at the pinned head). The deprecation is a public Collections API change and is an owner-level decision.
- Draft branch: solr-9514-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T13:40:33Z (UTC).

## Ticket reading

BALANCESHARDUNIQUE sets a unique property across the replicas of a shard. The reporter asks to deprecate it in favor of ADDREPLICAPROP and to add anything missing to ADDREPLICAPROP. Comments from 2016 agree on deprecation, and note that ADDREPLICAPROP and REMOVEREPLICAPROP already exist.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/cloud/api/collections/CollApiCmds.java:177`: BALANCESHARDUNIQUE is still registered as a collections API command.
- `solr/core/src/java/org/apache/solr/cloud/Overseer.java:528` and `ExclusiveSliceProperty.java:167`: the command is still handled.
- `solr/solr-ref-guide/modules/deployment-guide/pages/cluster-node-management.adoc:580-594`: the command is documented as a normal command, with no deprecation note.

## History

- evidence.py at the pinned head: no commits naming SOLR-9514.

## Reproduction attempts

Not applicable: this is an API deprecation, not a defect; a run would show only that the command works.

## Gap analysis

The command is live and documented, and the reporter's proposal is a public-API change (deprecation of a documented command). The compatibility decision belongs to the owner.

## Verdict justification

keep-open meets the rubric for an open API decision: attempts made are the code-path and docs reading; the missing piece is the owner's decision on deprecating a public command. close-fixed and close-obsolete are not supported: the command is present and documented. pr-needed is not chosen because the deprecation changes a public contract.

## Limits

This record does not decide the deprecation. It does not check whether ADDREPLICAPROP covers every BALANCESHARDUNIQUE case.

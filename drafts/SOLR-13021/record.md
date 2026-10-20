# Skip record: SOLR-13021

- Ticket: SOLR-13021, You cannot safely create and delete the same collection.
- Verdict: keep-open.
- Draft branch: solr-13021-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 19 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-11-28, updated 2019-06-08, no fix versions, description empty. Links: "Reference SOLR-13291 (Open)"; "Reference SOLR-12801 (Open)". Seven comments (2018-11-30 to 2019-03-05): erickerickson writes a stress test and beasts it without a failure, and on 7.5 cannot build his patch; markrmiller says it happens when tests beasted clean up by removing collections; varun asks whether SOLR-12818 still reproduces; jsauer says it may be the same as SOLR-13291, which has a shell script that reproduces it on a two-node cluster.

## Code reading

Read at the pin:

- `CreateDeleteCollectionStressTest` (the test the comments name): no hits under `solr` (`git grep -l CreateDeleteCollectionStressTest` returns nothing).
- The delete path the same-name create would race with: `solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteCollectionCmd.java:115-116` sets `CoreAdminParams.DELETE_INSTANCE_DIR` and `DELETE_DATA_DIR` on the UNLOAD request. This is the path batch 06 read for SOLR-13291 (see `drafts/SOLR-13291/record.md` on `solr-13291-draft-open`, verdict keep-open, race not reproduced).
- No lock-release wait was seen on the delete path in the lines read, matching the SOLR-13291 record.

## History

- No commit at the pin names SOLR-13021 (the evidence tool lists none).
- The related ticket SOLR-13291 is keep-open on its own draft branch; its evidence applies here.

## Reproduction attempts

Not run. This pass ran no builds or tests. The reproduction in the ticket's linked SOLR-13291 (a shell script that deletes and re-creates a collection in a loop on a two-node cluster) is the run that would settle both tickets at the pin.

## Gap analysis

- The ticket has no description and no code path; the comments do not name a defect location.
- The stress test cited in the comments is absent at the pin, so the old reproduction cannot be rerun as written.
- The ticket and SOLR-13291 may be one issue; that is not established here, and the SOLR-13291 record is the one to read for the shared delete path.

## Verdict justification

keep-open: the reproduction has not been run at the pin, the ticket has no code path, and the likely shared cause (SOLR-13291) is itself open. close-obsolete and close-fixed do not fit (the delete path is present; no fix commit names the ticket). close-cannot-reproduce needs a run at the pin, which has not been done.

Missing for this verdict: a run of the create and delete loop at the pin (the SOLR-13291 shell reproduction would serve), with the outcome recorded for both tickets.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>

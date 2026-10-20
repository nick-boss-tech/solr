# Pool assignment: JIRA newdev small improvements, round 2

Status: CLAIMED (vm2, claims/pool-jira-newdev-small-round-2.md, 2026-10-20T08:31:28Z)

- Capability tags: implementation, gate. Intended host: any pool Linux host (`hosts/pool-linux.md`). Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: vm2 gate coordinator, 2026-10-19, at the direction of the user. Source: JIRA `project=SOLR AND resolution=Unresolved AND labels=newdev`. This round takes nine of the 32 tickets that round 1 did not cover and that look small from their descriptions. The rest wait for owner decisions.

## Items

1. **SOLR-2534, Add min() and max() on strings.** Verify SOLR-2534 on apache/main at c8ab71430f. If it is still open and the change is as the ticket describes, implement it with its test on branch `solr-2534-<short>` from that commit, run the gate, and push the branch as a new branch. If the ticket's proposal is unclear or the change needs a design decision, record why and stop without a branch. Start from: https://issues.apache.org/jira/browse/SOLR-2534. Output: one section in the deliverable.
2. **SOLR-8614, Add simple SolrClient.setUseXmlFormat.** Verify SOLR-8614 on apache/main at c8ab71430f. If it is still open and the change is as the ticket describes, implement it with its test on branch `solr-8614-<short>` from that commit, run the gate, and push the branch as a new branch. If the ticket's proposal is unclear or the change needs a design decision, record why and stop without a branch. Start from: https://issues.apache.org/jira/browse/SOLR-8614. Output: one section in the deliverable.
3. **SOLR-16124, Solr Example should prompt for ZK port.** Verify SOLR-16124 on apache/main at c8ab71430f. If it is still open and the change is as the ticket describes, implement it with its test on branch `solr-16124-<short>` from that commit, run the gate, and push the branch as a new branch. If the ticket's proposal is unclear or the change needs a design decision, record why and stop without a branch. Start from: https://issues.apache.org/jira/browse/SOLR-16124. Output: one section in the deliverable.
4. **SOLR-18182, Deprecate ByteBuffersDirectoryFactory.** Verify SOLR-18182 on apache/main at c8ab71430f. If it is still open and the change is as the ticket describes, implement it with its test on branch `solr-18182-<short>` from that commit, run the gate, and push the branch as a new branch. If the ticket's proposal is unclear or the change needs a design decision, record why and stop without a branch. Start from: https://issues.apache.org/jira/browse/SOLR-18182. Output: one section in the deliverable.
5. **SOLR-15462, Dubious code in DistribPackageStore::refresh.** Verify SOLR-15462 on apache/main at c8ab71430f. If it is still open and the change is as the ticket describes, implement it with its test on branch `solr-15462-<short>` from that commit, run the gate, and push the branch as a new branch. If the ticket's proposal is unclear or the change needs a design decision, record why and stop without a branch. Start from: https://issues.apache.org/jira/browse/SOLR-15462. Output: one section in the deliverable.
6. **SOLR-17719, TestManagedSchemaWithMultipleAdd timing related failures (should use a semaphore hook to wait for newSearcher).** Verify SOLR-17719 on apache/main at c8ab71430f. If it is still open and the change is as the ticket describes, implement it with its test on branch `solr-17719-<short>` from that commit, run the gate, and push the branch as a new branch. If the ticket's proposal is unclear or the change needs a design decision, record why and stop without a branch. Start from: https://issues.apache.org/jira/browse/SOLR-17719. Output: one section in the deliverable.
7. **SOLR-12718, StreamContext ctor should always take a SolrClientCache.** Verify SOLR-12718 on apache/main at c8ab71430f. If it is still open and the change is as the ticket describes, implement it with its test on branch `solr-12718-<short>` from that commit, run the gate, and push the branch as a new branch. If the ticket's proposal is unclear or the change needs a design decision, record why and stop without a branch. Start from: https://issues.apache.org/jira/browse/SOLR-12718. Output: one section in the deliverable.
8. **SOLR-16865, Solr should warn about low disk space upon startup.** Verify SOLR-16865 on apache/main at c8ab71430f. If it is still open and the change is as the ticket describes, implement it with its test on branch `solr-16865-<short>` from that commit, run the gate, and push the branch as a new branch. If the ticket's proposal is unclear or the change needs a design decision, record why and stop without a branch. Start from: https://issues.apache.org/jira/browse/SOLR-16865. Output: one section in the deliverable.
9. **SOLR-18173, Deprecate SolrInputDocument.addChildDocument.** Verify SOLR-18173 on apache/main at c8ab71430f. If it is still open and the change is as the ticket describes, implement it with its test on branch `solr-18173-<short>` from that commit, run the gate, and push the branch as a new branch. If the ticket's proposal is unclear or the change needs a design decision, record why and stop without a branch. Start from: https://issues.apache.org/jira/browse/SOLR-18173. Output: one section in the deliverable.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Base | decided: apache/main at c8ab71430f | branch every fix from that commit, not from the fork main | user |
| Branch names | decided: `solr-NNNN-<short>`, new branches only | never force push or overwrite an existing branch; if the name is taken, pick another and record it | user |
| Push | decided: push gated branches to the fork as new branches | after the gate is green; no push before the gate runs | user |
| Draft PR | decided: a draft PR description, written as a file on the fork branch; no PR is opened on GitHub | after the gate receipt is green, add the draft description to the branch | user |
| Browser testing | decided: out of scope | do not claim UI or browser work | user |
| Scope of a fix | default: minimal change plus one test | if the change needs more than the ticket asks for, stop and record it | vm2 coordinator |
| Ticket unclear or design needed | default: record and stop | no branch is pushed for that ticket | vm2 coordinator |
| Gate failure | default: follow the Gate repairs section of WORKFLOW.md | a run that executed tests and failed is recorded and not relaunched unless the head, seed, input or environment changed | vm2 coordinator |
| Overlap | decided: skip any ticket with a live claim | check `claims/` and `assignments/` before taking a ticket | user |

## Deliverable

`reports/2026-10-19-jira-newdev/C-small-round-2.md`: one section per ticket with its status (fixed branch and head, already fixed, unclear or design needed, or stopped with the reason), the gate receipt path if a gate ran, and the test counts from fresh XML.

## Limits

- No PR, JIRA comment, GitHub comment or email. No force push. No push to any upstream branch.
- Commit only to `coordination` as Nick Shanin, with no trailers and no em dashes.
- Do not use the Windows host.
